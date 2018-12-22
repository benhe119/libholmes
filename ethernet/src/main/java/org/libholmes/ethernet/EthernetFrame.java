// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.ethernet;

import javax.json.JsonObjectBuilder;

import org.libholmes.OctetReader;
import org.libholmes.OctetString;
import org.libholmes.Artefact;
import org.libholmes.Logger;

/** A class to represent an Ethernet II frame. */
public final class EthernetFrame extends Artefact {
    /** The destination MAC address. */
    private final EthernetAddress dstAddr;

    /** The source MAC address. */
    private final EthernetAddress srcAddr;

    /** The EtherType. */
    private final int etherType;

    /** The payload.
     * Note that this may contain padding, however due to the potential
     * absence of a length field, the amount cannot generally be
     * determined without reference to the network layer protocol.
     */
    private final OctetString payload;

    /** Construct EthernetFrame from an OctetReader.
     * @param parent the parent artefact, or null if none
     * @param reader the OctetReader
     */
    private EthernetFrame(Artefact parent, OctetReader reader) {
        super(parent);
        reader.setByteOrder(reader.BIG_ENDIAN);
        dstAddr = EthernetAddress.parse(reader);
        srcAddr = EthernetAddress.parse(reader);
        etherType = reader.readShort() & 0xffff;
        payload = reader.readOctetString(reader.remaining());
    }

    /** Get the destination MAC address.
     * @return the destination address
     */
    public EthernetAddress getDstAddr() {
        return dstAddr;
    }

    /** Get the source MAC address.
     * @return the source address
     */
    public EthernetAddress getSrcAddr() {
        return srcAddr;
    }

    /** Get the EtherType.
     * @return the EtherType
     */
    public int getEtherType() {
        return etherType;
    }

    /** Get the payload.
     * Note that this may contain padding, however due to the potential
     * absence of a length field, the amount cannot generally be
     * determined without reference to the network layer protocol.
     * @return the payload
     */
    public OctetString getPayload() {
        return payload;
    }

    @Override
    protected void buildJson(JsonObjectBuilder builder) {
        builder.add("dstAddr", getDstAddr().toString());
        builder.add("srcAddr", getSrcAddr().toString());
        builder.add("etherType", getEtherType());
        builder.add("payload", getPayload().toString());
    }

    @Override
    public void examine(Logger logger) {
        // Technically the minimum frame size is defined to be dependent on
        // the data rate, however it has a fixed value of 14 + 46 + 4 = 64
        // for all listed rates (IEEE Std 802.3-2012 section 4.4.2).
        // Maximum frame sizes are listed, however jumbo frames are
        // sufficiently widespread that there would be little value in
        // reporting them as findings.
        if (payload.length() < 46) {
            logger.log("Runt Ethernet frame (payload length %d octets)",
                payload.length());
        }

        // Bit 0 of the source address is reserved and must be set to zero
        // (IEEE Std 802.3-2012 section 3.2.3).
        if (srcAddr.isBroadcastAddress()) {
            logger.log("Broadcast address as Ethernet source address");
        } else if (srcAddr.isMulticastAddress()) {
            logger.log("Multicast address as Ethernet source address");
        }
    }

    /** Parse Ethernet frame from an OctetReader.
     * @param parent the parent artefact, or null if none
     * @param reader the OctetReader
     * @return the resulting EthernetFrame
     */
    public static EthernetFrame parse(Artefact parent, OctetReader reader) {
        return new EthernetFrame(parent, reader);
    }

    /** Parse Ethernet frame from an OctetString.
     * @param string the OctetString
     * @return the resulting EthernetFrame
     */
    public static EthernetFrame parse(Artefact parent, OctetString string) {
        return new EthernetFrame(parent, string.makeOctetReader());
    }
}
