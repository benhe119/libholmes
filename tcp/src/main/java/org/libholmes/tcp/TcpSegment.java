// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.tcp;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonArrayBuilder;

import org.libholmes.OctetReader;
import org.libholmes.OctetString;
import org.libholmes.Artefact;
import org.libholmes.ParseException;
import org.libholmes.inet.InetChecksum;
import org.libholmes.inet.InetAddress;
import org.libholmes.inet.InetSocketAddress;
import org.libholmes.inet.InetDatagram;

/** A class to represent a TCP segment.
 * The TCP protocol specification can be found in RFC 793.
 */
public class TcpSegment extends Artefact {
    /** The undecoded header. */
    private final OctetString header;

    /** The undecoded payload. */
    private final OctetString payload;

    /** The options present in this segment. */
    private final ArrayList<TcpOption> options =
        new ArrayList<TcpOption>();

    /** The padding, if any, at the end of the options. */
    private final OctetString padding;

    /** Parse TCP segment from source of octets.
     * @param parent the parent object, which must be an IP datagram
     * @param reader the octet source to be parsed
     * @throws ParseException if the octet sequence cannot be parsed
     */
    public TcpSegment(InetDatagram parent, OctetReader reader)
        throws ParseException {

        super(parent);
        int headerLength = ((reader.peekByte(12) >> 4) & 0xf) * 4;
        if (headerLength < 20) {
            throw new ParseException(
                "invalid header length for TCP");
        }
        this.header = reader.readOctetString(headerLength);
        this.payload = reader.readOctetString(reader.remaining());

        OctetReader headerReader = header.makeOctetReader();
        headerReader.skip(20);
        boolean foundEool = false;
        while (headerReader.hasRemaining() && !foundEool) {
            TcpOption option = TcpOption.parse(this, headerReader);
            options.add(option);
            if (option.getKind() == 0) {
                foundEool = true;
            }
        }
        this.padding = headerReader.readOctetString(
            headerReader.remaining());
    }

    /** Get IP datagram to which this segment belongs
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

    /** Get wrapped sequence number.
     * Note that the result is given as a signed integer. It is usable
     * for analysis purposes in that format, however RFC 793 defines the
     * sequence number space to be unsigned, and that is the preferred
     * representational format.
     * @return the wrapped sequence number for this segment
     */
    public final int getSeq() {
        return header.getInt(4);
    }

    /** Get wrapped acknowledgement number.
     * Note that the result is given as a signed integer. It is usable
     * for analysis purposes in that format, however RFC 793 defines the
     * sequence number space to be unsigned, and that is the preferred
     * representational format.
     * @return the wrapped acknowledgement number for this segment
     */
    public final int getAck() {
        return header.getInt(8);
    }

    /** Get the data offset, in 32-bit words.
     * @return the data offset
     */
    public final int getDataOffset() {
        return (header.getByte(12) >> 4) & 0xf;
    }

    /** Get NS flag.
     * @return the NS flag.
     */
    public final boolean getNsFlag() {
        return (header.getByte(12) & 0x01) != 0;
    }

    /** Get CWR flag.
     * @return the CWR flag.
     */
    public final boolean getCwrFlag() {
        return (header.getByte(13) & 0x80) != 0;
    }

    /** Get ECE flag.
     * @return the ECE flag.
     */
    public final boolean getEceFlag() {
        return (header.getByte(13) & 0x40) != 0;
    }

    /** Get URG flag.
     * @return the URG flag.
     */
    public final boolean getUrgFlag() {
        return (header.getByte(13) & 0x20) != 0;
    }

    /** Get ACK flag.
     * @return the ACK flag.
     */
    public final boolean getAckFlag() {
        return (header.getByte(13) & 0x10) != 0;
    }

    /** Get PSH flag.
     * @return the PSH flag.
     */
    public final boolean getPshFlag() {
        return (header.getByte(13) & 0x08) != 0;
    }

    /** Get RST flag.
     * @return the RST flag.
     */
    public final boolean getRstFlag() {
        return (header.getByte(13) & 0x04) != 0;
    }

    /** Get SYN flag.
     * @return the SYN flag.
     */
    public final boolean getSynFlag() {
        return (header.getByte(13) & 0x02) != 0;
    }

    /** Get FIN flag.
     * @return the FIN flag.
     */
    public final boolean getFinFlag() {
        return (header.getByte(13) & 0x01) != 0;
    }

    /** Get window size.
     * By default this is measured in octets, however an optional scaling
     * factor may be applicable.
     * @return the window size
     */
    public final int getWindowSize() {
        return header.getShort(14) & 0xffff;
    }

    /** Get the recorded checksum.
     * This is the checksum as recorded in the checksum field of the
     * header.
     * @return the recorded checksum
     */
    public final int getRecordedChecksum() {
        return header.getShort(16) & 0xffff;
    }

    /** Get the calculated checksum.
     * This is the checksum as calculated by summing every word in the
     * IP pseudo-header, plus every word in the TCP header except for the
     * checksum field, plus every word in the payload (padded with a zero
     * octet if necessary to make a whole number of words).
     * @return the calculated checksum
     */
    public final int getCalculatedChecksum() {
        InetChecksum checksum = getInetDatagram().makePseudoHeaderChecksum();
        checksum.add(header.getOctetString(0, 16));
        checksum.add(header.getOctetString(18, header.length() - 18));
        checksum.add(payload);
        return checksum.get();
    }

    /** Get urgent pointer.
     * @return the urgent pointer
     */
    public final int getUrgentPointer() {
        return header.getShort(18) & 0xffff;
    }

    /** Get the options list
     * @return the list of options present in this segment
     */
    public final List<TcpOption> getOptions() {
        return Collections.unmodifiableList(options);
    }

    /** Get padding.
     * @return any padding at the end of the options list
     */
    public final OctetString getPadding() {
        return padding;
    }

    /** Get payload.
     * @return the payload carried by this segment
     */
    public final OctetString getPayload() {
        return payload;
    }

    @Override
    public final void buildJson(JsonObjectBuilder builder) {
        JsonArrayBuilder optionsBuilder = Json.createArrayBuilder();
        for (TcpOption option : options) {
            optionsBuilder.add(option.toJson());
        }

        builder.add("srcPort", getSrcPort());
        builder.add("dstPort", getDstPort());
        builder.add("seq", getSeq() & 0xffffffffL);
        builder.add("ack", getAck() & 0xffffffffL);
        builder.add("dataOffset", getDataOffset());
        builder.add("nsFlag", getNsFlag());
        builder.add("cwrFlag", getCwrFlag());
        builder.add("eceFlag", getEceFlag());
        builder.add("urgFlag", getUrgFlag());
        builder.add("ackFlag", getAckFlag());
        builder.add("pshFlag", getPshFlag());
        builder.add("rstFlag", getRstFlag());
        builder.add("synFlag", getSynFlag());
        builder.add("finFlag", getFinFlag());
        builder.add("windowSize", getWindowSize());
        builder.add("recordedChecksum", getRecordedChecksum());
        builder.add("calculatedChecksum", getCalculatedChecksum());
        builder.add("urgentPointer", getUrgentPointer());
        builder.add("options", optionsBuilder.build());
        builder.add("padding", padding.toString());
        builder.add("payload", payload.toString());
    }

    /** Parse TCP segment from an OctetReader.
     * @param parent the parent object, which must be an IP datagram
     * @param reader the OctetReader to be parsed
     * @return the resulting segment
     * @throws ParseException if the octets cannot be parsed
     */
    public static TcpSegment parse(InetDatagram parent, OctetReader reader)
        throws ParseException {

        return new TcpSegment(parent, reader);
    }

    /** Parse TCP segment from an OctetString.
     * @param parent the parent object, which must be an IP datagram
     * @param string the OctetString to be parsed
     * @return the resulting segment
     * @throws ParseException if the octets cannot be parsed
     */
    public static TcpSegment parse(InetDatagram parent, OctetString string)
        throws ParseException {

        return new TcpSegment(parent, string.makeOctetReader());
    }
}
