// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.udp;

import javax.json.JsonObjectBuilder;

import org.libholmes.OctetReader;
import org.libholmes.OctetString;
import org.libholmes.Artefact;
import org.libholmes.ParseException;
import org.libholmes.inet.InetChecksum;
import org.libholmes.inet.InetAddress;
import org.libholmes.inet.InetSocketAddress;
import org.libholmes.inet.InetDatagram;

/** A class to represent a UDP datagram.
 * The UDP protocol specification can be found in RFC 768.
 */
public class UdpDatagram extends Artefact {
    /** The undecoded header. */
    private final OctetString header;

    /** The undecoded payload. */
    private final OctetString payload;

    /** Parse UDP datagram from source of octets.
     * @param parent the parent artefact, which must be an IP datagram
     * @param reader the octet source to be parsed
     * @throws ParseException if the octet sequence cannot be parsed
     */
    public UdpDatagram(InetDatagram parent, OctetReader reader)
        throws ParseException {

        super(parent);
        this.header = reader.readOctetString(8);
        int length = this.getLength();
        if (length < 8) {
            throw new ParseException(
                "UDP datagram length less than header length");
        }
        this.payload = reader.readOctetString(length - 8);
    }

    /** Get IP datagram to which this UDP datagram belongs
     * @return the IP datagram
     */
    public final InetDatagram getInetDatagram() {
        return (InetDatagram) getParent();
    }

    /** Get source IP address.
     * @return the source IP address
     */
    public final InetAddress getSrcAddr() {
        return ((InetDatagram) getParent()).getSrcAddr();
    }

    /** Get destination IP address.
     * @return the destination IP address
     */
    public final InetAddress getDstAddr() {
        return ((InetDatagram) getParent()).getDstAddr();
    }

    /** Get source port.
     * @return the source port
     */
    public final int getSrcPort() {
        return header.getShort(0) & 0xffff;
    }

    /** Get destination port.
     * @return the destination port
     */
    public final int getDstPort() {
        return header.getShort(2) & 0xffff;
    }

    /** Get source socket address.
     * @return the source socket address
     */
    public final InetSocketAddress getSrcSockAddr() {
        return new InetSocketAddress(getSrcAddr(), getSrcPort());
    }

    /** Get destination socket address.
     * @return the destination socket address
     */
    public final InetSocketAddress getDstSockAddr() {
        return new InetSocketAddress(getDstAddr(), getDstPort());
    }

    /** Get length.
     * @return the length, in octets
     */
    public final int getLength() {
        return header.getShort(4) & 0xffff;
    }

    /** Get the recorded checksum.
     * This is the checksum as recorded in the checksum field of the
     * header.
     * @return the recorded checksum
     */
    public final int getRecordedChecksum() {
        return header.getShort(6) & 0xffff;
    }

    /** Get the calculated checksum.
     * This is the checksum as calculated by summing every word in the
     * IP pseudo-header, plus every word in the UDP header except for the
     * checksum field, plus every word in the payload (padded with a zero
     * octet if necessary to make a whole number of words).
     * @return the calculated checksum
     */
    public final int getCalculatedChecksum() {
        InetChecksum checksum = getInetDatagram().makePseudoHeaderChecksum();
        checksum.add(header.getOctetString(0, 6));
        checksum.add(payload);
        return checksum.get();
    }

    /** Get payload.
     * @return the payload carried by this datagram
     */
    public final OctetString getPayload() {
        return payload;
    }

    @Override
    public final void buildJson(JsonObjectBuilder builder) {
        builder.add("srcPort", getSrcPort());
        builder.add("dstPort", getDstPort());
        builder.add("length", getLength());
        builder.add("recordedChecksum", getRecordedChecksum());
        builder.add("calculatedChecksum", getCalculatedChecksum());
        builder.add("payload", payload.toString());
    }

    /** Parse UDP datagram from an OctetReader.
     * @param parent the parent artefact, which must be an IP datagram
     * @param reader the OctetReader to be parsed
     * @return the resulting datagram
     * @throws ParseException if the octets cannot be parsed
     */
    public static UdpDatagram parse(InetDatagram parent, OctetReader reader)
        throws ParseException {

        return new UdpDatagram(parent, reader);
    }

    /** Parse UDP datagram from an OctetString.
     * @param parent the parent artefact, which must be an IP datagram
     * @param string the OctetString to be parsed
     * @return the resulting datagram
     * @throws ParseException if the octets cannot be parsed
     */
    public static UdpDatagram parse(InetDatagram parent, OctetString string)
        throws ParseException {

        return new UdpDatagram(parent, string.makeOctetReader());
    }
}
