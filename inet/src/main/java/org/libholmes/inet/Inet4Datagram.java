// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.inet;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObjectBuilder;
import javax.json.JsonArrayBuilder;

import org.libholmes.OctetReader;
import org.libholmes.OctetString;
import org.libholmes.Artefact;
import org.libholmes.Timestamped;
import org.libholmes.Logger;
import org.libholmes.ParseException;

/** A class to represent an IPv4 datagram.
 * The original specification can be found in RFC 791. Notable amendments
 * include:
 * - Redefinition of the semantics of the TOS field, once by RFC 1349,
 *   then again by RFC 2474 (the latter with several subsequent updates).
 * - Modification of the semantics of the ID field by RFC 6864.
 * - Definition of the 'evil bit' by RFC 3514. Although this is an
 *   April Fools memo, this is as good a name as any for the flag bit in
 *   question, in the absence of any other defined usage.
 */
public class Inet4Datagram extends InetDatagram {
    /** The maximum segment lifetime for a TCP segment.
     * This is used as a proxy for the maximum lifetime of any IP datagram.
     */
    private static final long MSL = 120000000000L;

    /** The undecoded header. */
    private final OctetString header;

    /** The undecoded payload. */
    private final OctetString payload;

    /** The source address. */
    private final Inet4Address srcAddr;

    /** The destination address. */
    private final Inet4Address dstAddr;

    /** The options present in this datagram. */
    private final ArrayList<Inet4Option> options =
        new ArrayList<Inet4Option>();

    /** The padding, if any, at the end of the options. */
    private final OctetString padding;

    /** Parse IPv4 datagram from source of octets.
     * @param parent the parent artefact, or null if none
     * @param reader the octet source to be parsed
     * @throws ParseException if the octet sequence cannot be parsed
     */
    public Inet4Datagram(Artefact parent, OctetReader reader)
        throws ParseException {

        super(parent, reader);
        if (getVersion() != 4) {
            throw new ParseException(
                "invalid protocol version number for IPv4");
        }

        int headerLength = (reader.peekByte(0) & 0xf) * 4;
        if (headerLength < 20) {
            throw new ParseException(
                "invalid header length for IPv4");
        }
        this.header = reader.readOctetString(headerLength);

        int length = this.header.getShort(2) & 0xffff;
        if (length < headerLength) {
            throw new ParseException(
                 "IPv4 datagram length less than header length");
        }
        this.payload = reader.readOctetString(length - headerLength);
        this.srcAddr = Inet4Address.parse(this.header.getOctetString(12, 4));
        this.dstAddr = Inet4Address.parse(this.header.getOctetString(16, 4));

        OctetReader headerReader = header.makeOctetReader();
        headerReader.skip(20);
        boolean foundEool = false;
        while (headerReader.hasRemaining() && !foundEool) {
            Inet4Option option = Inet4Option.parse(this, headerReader);
            options.add(option);
            if (option.getType() == Inet4Option.OPTION_EOOL) {
                foundEool = true;
            }
        }
        this.padding = headerReader.readOctetString(
            headerReader.remaining());
    }

    /** Get internet header length.
     * @return the length, in 32-bit words
     */
    public final int getIhl() {
        return header.getByte(0) & 0xf;
    }

    /** Get Type of Service.
     * @return the type of service, as an uninterpreted octet.
     */
    public final int getTos() {
        return header.getByte(1) & 0xff;
    }

    /** Get datagram length.
     * @return the length of the datagram, in octets.
     */
    public final int getLength() {
        return header.getShort(2) & 0xffff;
    }

    /** Get identification field.
     * @return the identification field
     */
    public final int getId() {
        return header.getShort(4) & 0xffff;
    }

    /** Get evil bit.
     * @return true if datagram is declared to be malicious, otherwise false
     */
    public final boolean isEvil() {
        return (header.getByte(6) & 0x80) != 0;
    }

    /** Get DF (don't fragment) flag.
     * @return true if datagram should not be fragmented, otherwise false
     */
    public final boolean doNotFragment() {
        return (header.getByte(6) & 0x40) != 0;
    }

    /** Get MF (more fragments) flag.
     * @return true if datagram has more fragments, otherwise false
     */
    public final boolean hasMoreFragments() {
        return (header.getByte(6) & 0x20) != 0;
    }

    /** Get fragment offset.
     * @return the fragment offset, in units of 8-octet chunks
     */
    public final int getFragmentOffset() {
        return header.getShort(6) & 0x1fff;
    }

    /** Get time to live.
     * @return the time to live, in hops
     */
    public final int getTtl() {
        return header.getByte(8) & 0xff;
    }

    /** Get protocol number.
     * @return the protocol number
     */
    public final int getProtocol() {
        return header.getByte(9) & 0xff;
    }

    /** Get the recorded header checksum.
     * This is the checksum as recorded in the checksum field of the
     * header.
     * @return the recorded header checksum
     */
    public final int getRecordedChecksum() {
        return header.getShort(10) & 0xffff;
    }

    /** Get the calculated header checksum.
     * This is the checksum as calculated by summing every word in the
     * header except for the checksum field.
     * @return the calculated header checksum
     */
    public final int getCalculatedChecksum() {
        InetChecksum checksum = new InetChecksum();
        checksum.add(header.getOctetString(0, 10));
        checksum.add(header.getOctetString(12, header.length() - 12));
        return checksum.get();
    }

    /** Get source network address.
     * @return the source address
     */
    public final InetAddress getSrcAddr() {
        return srcAddr;
    }

    /** Get destination network address.
     * @return the destination address
     */
    public final InetAddress getDstAddr() {
        return dstAddr;
    }

    /** Get the options list
     * @return the list of options present in this datagram
     */
    public final List<Inet4Option> getOptions() {
        return Collections.unmodifiableList(options);
    }

    /** Get padding.
     * @return any padding at the end of the options list
     */
    public final OctetString getPadding() {
        return padding;
    }

    /** Get payload.
     * @return the payload carried by this datagram
     */
    public final OctetString getPayload() {
        return payload;
    }

    /** Examine this datagram.
     * Detected conditions are:
     * - Evil bit set
     * - MF bit set in unfragmentable datagram
     * - Non-zero fragment offset in unfragmentable datagram
     * - Non-zero padding following options
     * @param logger a logger for recording findings
     */
    public void examine(Logger logger) {
        if (isEvil()) {
            // Notwithstanding RFC 3514, this bit is reserved according to
            // RFC 791 and should always be zero.
            logger.log("Evil bit set in IPv4 datagram");
        }
        if (doNotFragment()) {
            if (hasMoreFragments()) {
                // A datagram has been fragmented when it should not have
                // been.
                logger.log("MF bit set in unfragmentable IPv4 datagram");
            }
            if (getFragmentOffset() != 0) {
                // Ditto (since the fragment offset is zero in an
                // unfragmented datagram).
                logger.log("Non-zero fragment offset in " +
                    "unfragmentable IPv4 datagram");
            }
        }

        if (srcAddr.isBroadcastAddress()) {
            logger.log("IPv4 broadcast address used as source");
        }
        if (dstAddr.isWildcardAddress()) {
            logger.log("IPv4 wildcard address used as desintation");
        }

        int[] optionCount = new int[0x100];
        for (Inet4Option option : options) {
            option.examine(logger);
            if (option.reportMultipleInstances()) {
                optionCount[option.getType()] += 1;
            }
        }
        for (int i = 0; i != 0x100; ++i) {
            if (optionCount[i] > 1) {
                logger.log("Multiple instances of " +
                    "option type %d in IPv4 datagram", i);
            }
        }

        int nzPadding = 0;
        OctetReader paddingReader = getPadding().makeOctetReader();
        while (paddingReader.hasRemaining()) {
            nzPadding |= paddingReader.readByte();
        }
        if (nzPadding != 0) {
            logger.log("Non-zero padding in IPv4 datagram");
        }
    }

    @Override
    public final void buildJson(JsonObjectBuilder builder) {
        JsonArrayBuilder jsonOptionsBuilder = Json.createArrayBuilder();
        for (Inet4Option option : options) {
            jsonOptionsBuilder.add(option.toJson());
        }
        JsonArray jsonOptions = jsonOptionsBuilder.build();

        builder.add("version", getVersion());
        builder.add("ihl", getIhl());
        builder.add("tos", getTos());
        builder.add("length", getLength());
        builder.add("id", getId());
        builder.add("evil", isEvil());
        builder.add("doNotFragment", doNotFragment());
        builder.add("moreFragments", hasMoreFragments());
        builder.add("fragmentOffset", getFragmentOffset());
        builder.add("ttl", getTtl());
        builder.add("protocol", getProtocol());
        builder.add("recordedChecksum", getRecordedChecksum());
        builder.add("calculatedChecksum", getCalculatedChecksum());
        builder.add("srcAddr", getSrcAddr().toString());
        builder.add("dstAddr", getDstAddr().toString());
        builder.add("options", jsonOptions);
        builder.add("padding", padding.toString());
        builder.add("payload", payload.toString());
    }

    @Override
    public final InetChecksum makePseudoHeaderChecksum() {
        InetChecksum checksum = new InetChecksum();
        checksum.add(header.getOctetString(12, 8));
        checksum.add(getProtocol());
        checksum.add(getPayload().length());
        return checksum;
    }

    /** Test whether this is a presumed duplicate of another datagram.
     * This method is intended for detecting duplication at the network layer
     * or below. It is not intended for detecting retransmission at the
     * transport layer or above, however it may report retransmissions as
     * duplicates if it is unable to make a distinction.
     *
     * To detect all duplication, traffic should be defragmented prior to
     * comparison. If that has not been done then this method will detect
     * duplicates of individual fragments which have the same length and
     * offset, but it does not attempt to detect partial duplicates.
     *
     * @param that the datagram to be compared
     * @return true if a presumed duplicate, otherwise false
     */
    public final boolean isDuplicate(Inet4Datagram that) {
        // See RFC 4302 for a discussion as to which parts of a datagram
        // are immutable in transit and which are not.

        // Do check the version and IHL fields. (Checking the version is not
        // strictly necessary, since both datagrams are known to be IPv4,
        // however it is easier to check both than just one.)
        if (this.header.getByte(0) != that.header.getByte(0)) {
            return false;
        }
        // Don't check the TOS field, since this can change in transit.
        // Don't check the total length field, since this will be
        // implicitly checked when the payloads are compared.
        // Do check identification field, which is immutable according to
        // RFC 4302.
        if (this.header.getShort(4) != that.header.getShort(4)) {
            return false;
        }
        // Do not check the 'evil bit', because (leaving aside RFC 3514)
        // this would do little to assist with the detection of duplicates
        // as used currently, and if a new use were defined then it might
        // not be immutable.
        // Don't check the DF flag, since RFC 4302 indicates that it could
        // be set in transit by a router.
        // Do check the MF flag and the fragment offset, to obtain the
        // specified behaviour in the presence of fragmentation.
        if ((this.header.getShort(6) & 0x3fff) !=
            (that.header.getShort(6) & 0x3fff)) {
            return false;
        }
        // Don't check the TTL field, since this is very likely to change
        // in transit.
        // Do check the protocol field.
        if (this.header.getByte(9) != that.header.getByte(9)) {
            return false;
        }
        // Don't check the checksum field, since this is very likely to
        // change in transit.
        // Do check the source IP address.
        if (this.header.getInt(12) != that.header.getInt(12)) {
            return false;
        }
        // Do check the destination IP address.
        if (this.header.getInt(16) != that.header.getInt(16)) {
            return false;
        }
        // Don't check the options field as a whole, since some options
        // could change in transit. It would be possible to check individual
        // options which do not change, however this is not currently
        // implemented.
        // Do check the payloads.
        if (!this.payload.equals(that.payload)) {
            return false;
        }

        // If the datagrams are timestamped then do check that the difference
        // is no greater than the TCP maximum segment lifetime, which is used
        // as a proxy here for the maximum lifetime of any IP datagram.
        Timestamped thisTs = this.find(Timestamped.class);
        Timestamped thatTs = that.find(Timestamped.class);
        if ((thisTs != null) && (thatTs != null)) {
            long diff = thisTs.getTimestamp() - thatTs.getTimestamp();
            if ((diff < -MSL) || (diff > MSL)) {
                return false;
            }
        }

        return true;
    }

    /** Parse IPv4 datagram from an OctetReader.
     * @param parent the parent artefact, or null if none
     * @param reader the OctetReader to be parsed
     * @return the resulting datagram
     * @throws ParseException if the octets cannot be parsed
     */
    public static Inet4Datagram parse(Artefact parent, OctetReader reader)
        throws ParseException {

        return new Inet4Datagram(parent, reader);
    }

    /** Parse IPv4 datagram from an OctetString.
     * @param parent the parent artefact, or null if none
     * @param string the OctetString to be parsed
     * @return the resulting datagram
     * @throws ParseException if the octets cannot be parsed
     */
    public static Inet4Datagram parse(Artefact parent, OctetString string)
        throws ParseException {

        return new Inet4Datagram(parent, string.makeOctetReader());
    }
}
