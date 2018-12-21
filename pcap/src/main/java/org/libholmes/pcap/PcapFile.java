// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.pcap;

import javax.json.JsonObjectBuilder;

import org.libholmes.OctetReader;
import org.libholmes.ParseException;
import org.libholmes.Artefact;

/** A class to represent a PCAP file.
 * Both big- and little-endian variants are supported.
 */
public class PcapFile extends Artefact {
    /** The magic number.
     * This should always be equal to 0xa1b2c3d4.
     */
    private final int magicNumber;

    /** The major version number. */
    private final int versionMajor;

    /** The minor version number. */
    private final int versionMinor;

    /** The timezone correction, in seconds.
     * This is the number of seconds that would need to be added to each
     * packet timestamp in order to convert it to UTC.
     * According to the documentation, it is always equal to zero.
     */
    private final int thisZone;

    /** The number of significant figures in the timestamps.
     * According to the documentation, this is always equal to zero.
     */
    private final int sigFigs;

    /** The maximum number of octets captured from each packet. */
    private final int snapLen;

    /** The network type. */
    private final int network;

    /** The octet source for reading the PCAP file.
     * Once the PCAP file header has been parsed, this should be left
     * configured with the appropriate byte order for the remainder of the
     * file.
     */
    private OctetReader reader;

    /** Construct PcapFile from octet source.
     * @param parent the parent of this artefact, or null if none
     * @param reader the octet source
     * @throws ParseException if the octet stream could not be parsed
     */
    public PcapFile(Artefact parent, OctetReader reader) throws ParseException {
        super(parent);
        if ((reader.peekInt(0) & 0xffffffffL) == 0xa1b2c3d4L) {
            // No action required: already set to corect byte order.
        } else if ((reader.peekInt(0) & 0xffffffffL) == 0xd4c3b2a1L) {
            // Need to reverse byte order to get correct magic number.
            reader.setByteOrder(~reader.getByteOrder());
        } else {
            // Cannot continue, unable to determine byte order.
            throw new ParseException("invalid magic number in PCAP file");
        }
        this.magicNumber = reader.readInt();
        this.versionMajor = reader.readShort() & 0xffff;
        this.versionMinor = reader.readShort() & 0xffff;
        this.thisZone = reader.readInt();
        this.sigFigs = reader.readInt();
        this.snapLen = reader.readInt();
        this.network = reader.readInt();
        this.reader = reader;
    }

    /** Get the magic number.
     * This should always be equal to 0xa1b2c3d4.
     * @return the magic number
     */
    public final int getMagicNumber() {
        return magicNumber;
    }

    /** Get the major version number.
     * @return the major version number
     */
    public final int getVersionMajor() {
        return versionMajor;
    }

    /** Get the minor version number.
     * @return the minor version number
     */
    public final int getVersionMinor() {
        return versionMinor;
    }

    /** Get timezone correction.
     * This is the number of seconds that would need to be added to each
     * timestamp in order to convert it to UTC.
     * According to the documentation, it is always equal to zero.
     * @return the correction, in seconds
     */
    public final int getThisZone() {
        return thisZone;
    }

    /** Get the number of significant figures in the timestamps.
     * According to the documentation, this is always equal to zero.
     * @return the number of significant figures
     */
    public final int getSigFigs() {
        return sigFigs;
    }

    /** Get the maximum number of octets captured from each packet.
     * @return the number of octets captured
     */
    public final int getSnapLen() {
        return snapLen;
    }

    /** Get the network type.
     * @return the network type
     */
    public final int getNetworkType() {
        return network;
    }

    /** Read a packet from the PCAP file.
     * @return the resulting packet
     */
    public final PcapPacket readPacket() {
        return new PcapPacket(this, reader);
    }

    /** Check whether there is any data remaining to be read.
     * This does not necessarily imply that a packet can be read,
     * however if the PCAP file is complete and valid then that will
     * be the case.
     * @return true if there is data remaining, otherwise false
     */
    public boolean hasRemaining() {
        return reader.hasRemaining();
    }

    @Override
    protected final void buildJson(JsonObjectBuilder builder) {
        builder.add("magicNumber", getMagicNumber() & 0xffffffffL);
        builder.add("versionMajor", getVersionMajor());
        builder.add("versionMinor", getVersionMinor());
        builder.add("thisZone", getThisZone());
        builder.add("sigFigs", getSigFigs());
        builder.add("snapLen", getSnapLen());
        builder.add("networkType", getNetworkType());
    }

    /** Make PcapFile from octet source.
     * @param parent the parent of this artefact, or null if none
     * @param reader the octet source
     * @return the resulting PcapFile
     * @throws ParseException if the octet stream could not be parsed
     */
    public static PcapFile parse(Artefact parent, OctetReader reader)
        throws ParseException {

        return new PcapFile(parent, reader);
    }
}
