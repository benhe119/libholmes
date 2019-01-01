// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.pcap;

import javax.json.JsonObjectBuilder;

import org.libholmes.OctetReader;
import org.libholmes.OctetString;
import org.libholmes.Artefact;
import org.libholmes.Timestamped;

/** A class to represent a packet within a PCAP file. */
public class PcapPacket extends Artefact implements Timestamped {
    /** The seconds component of the timestamp.
     * This is the number of seconds since the UNIX epoch. */
    private final int tsSec;

    /** The microseconds component of the timestamp.
     * This is the number of microseconds since the start of the current
     * second.
     */
    private final int tsUsec;

    /** The captured length of this packet, in octets. */
    private final int caplen;

    /** The original length of this packet, in octets. */
    private final int len;

    /** The payload. */
    private final OctetString payload;

    /** Construct PcapPacket from octet source.
     * @param parent the parent of this artefact, or null if none
     * @param reader the octet source
     */
    protected PcapPacket(Artefact parent, OctetReader reader) {
        super(parent);
        this.tsSec = reader.readInt();
        this.tsUsec = reader.readInt();
        this.caplen = reader.readInt();
        this.len = reader.readInt();
        this.payload = reader.readOctetString(getCapturedLength());
    }

    /** Get the seconds component of the timestamp.
     * This is the number of seconds since the UNIX epoch.
     * @return the seconds component
     */
    public final int getTsSec() {
        return tsSec;
    }

    /** Get the microseconds component of the timestamp.
     * This is the number of microseconds since the start of the current
     * second.
     * @return the microseconds component
     */
    public final int getTsUsec() {
        return tsUsec;
    }

    @Override
    public final long getTimestamp() {
        return ((tsSec * 1000000L) + tsUsec) * 1000;
    }

    /** Get the captured length of this packet.
     * @return the captured length, in octets
     */
    public final int getCapturedLength() {
        return caplen;
    }

    /** Get the original length of this packet.
     * @return the original length, in octets
     */
    public final int getOriginalLength() {
        return len;
    }

    /** Get the payload.
     * Note that the payload may have been truncated during capture, as the
     * number of octets recorded is limited by the snaplen parameter. This
     * condition can be detected by comparing the values returned by
     * getCapturedLength() and getOriginalLength().
     * @return the payload
     */
    public final OctetString getPayload() {
        return payload;
    }

    @Override
    protected final void buildJson(JsonObjectBuilder builder) {
        builder.add("tsSec", getTsSec() & 0xffffffffL);
        builder.add("tsUsec", getTsUsec() & 0xffffffffL);
        builder.add("capturedLength", getCapturedLength() & 0xffffffffL);
        builder.add("originalLength", getOriginalLength() & 0xffffffffL);
        builder.add("payload", getPayload().toString());
    }

    /** Parse PcapPacket from octet source.
     * @param parent the parent of this artefact, or null if none
     * @param reader the octet source
     * @return the resulting PcapPacket
     */
    public static PcapPacket parse(Artefact parent, OctetReader reader) {
        return new PcapPacket(parent, reader);
    }
}
