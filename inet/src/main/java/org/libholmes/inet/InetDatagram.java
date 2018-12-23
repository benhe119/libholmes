// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.inet;

import org.libholmes.OctetReader;
import org.libholmes.OctetString;
import org.libholmes.Artefact;
import org.libholmes.ParseException;

/** An abstract base class to represent an Internet Protocol datagram. */
public abstract class InetDatagram extends Artefact {
    /** The protocol version. */
    private final byte version;

    /** Parse IPv4 datagram from an OctetReader.
     * Since only the first nibble is parsed by this base class,
     * no data it consumed from the OctetReader.
     * @param parent the parent artefact, or null if none
     * @param reader the OctetReader to be parsed
     */
    protected InetDatagram(Artefact parent, OctetReader reader) {
        super(parent);
        this.version = (byte) ((reader.peekByte(0) >> 4) & 0xf);
    }

    /** Get protocol version.
     * @return the protocol version
     */
    public final int getVersion() {
        return version;
    }

    /** Get source address.
     * @return the source address
     */
    public abstract InetAddress getSrcAddr();

    /** Get destination address.
     * @return the destination address
     */
    public abstract InetAddress getDstAddr();

    /** Make checksum for pseudo-header.
     * @return the checksum object
     */
    public abstract InetChecksum makePseudoHeaderChecksum();

    /** Parse IPv4 datagram from an OctetReader.
     * @param parent the parent artefact, or null if none
     * @param reader the OctetReader to be parsed
     * @return the resulting datagram
     * @throws ParseException if the octet sequence cannot be parsed
     */
    public static InetDatagram parse(Artefact parent, OctetReader reader)
        throws ParseException {

        int version = (reader.peekByte(0) >> 4) & 0xf;
        switch (version) {
        case 4:
            return new Inet4Datagram(parent, reader);
        default:
            throw new ParseException(String.format(
                "IP version %d not supported", version));
        }
    }

    /** Parse IPv4 datagram from an OctetString.
     * @param parent the parent artefact, or null if none
     * @param string the OctetString to be parsed
     * @return the resulting datagram
     * @throws ParseException if the octet sequence cannot be parsed
     */
    public static InetDatagram parse(Artefact parent, OctetString string)
        throws ParseException {

        return parse(parent, string.makeOctetReader());
    }
}
