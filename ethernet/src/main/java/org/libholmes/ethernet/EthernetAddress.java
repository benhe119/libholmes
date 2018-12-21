// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.ethernet;

import org.libholmes.OctetReader;
import org.libholmes.OctetString;
import org.libholmes.ArrayOctetString;
import org.libholmes.Address;
import org.libholmes.ParseException;

/** A class to represent an Ethernet MAC address. */
public class EthernetAddress extends Address {
    /** Copy-constuct an EthernetAddress from another EthernetAddress.
     * @param that the address to be copied
     */
    protected EthernetAddress(EthernetAddress that) {
        super(that);
    }

    /** Construct EthernetAddress from an OctetString.
     * @param content the required content
     */
    protected EthernetAddress(OctetString content) throws ParseException {
        super(content);
        if (content.length() != 6) {
            throw new ParseException(
                "unexpected length for Ethernet MAC address");
        }
    }

    @Override
    public int getFlags() {
        int flags = 0;
        if ((content.getByte(0) & 1) != 0) {
            flags |= FLAG_MULTICAST;
        }
        return flags;
    }

    /** Test whether this is a globally-unique address.
     * @return true if global, false if local
     */
    public final boolean isGlobal() {
        return (content.getByte(0) & 2) == 0;
    }

    /** Test whether this is a locally-administered address.
     * @return true if local, false if global
     */
    public final boolean isLocal() {
        return (content.getByte(0) & 2) != 0;
    }

    @Override
    public final String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0, n = content.length(); i != n; ++i) {
            if (builder.length() != 0) {
                builder.append('-');
            }
            int b = content.getByte(i) & 0xff;
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    /** Convert to string with given separator.
     * It is permissible for the separator to be the empty string.
     * @param separator the required separator
     * @param upperCase true for upper case, false for lower case
     * @return the address as a string
     */
    public final String toString(String separator, boolean upperCase) {
        String format = (upperCase) ? "%02X" : "%02x";
        StringBuilder builder = new StringBuilder();
        for (int i = 0, n = content.length(); i != n; ++i) {
            if (builder.length() != 0) {
                builder.append(separator);
            }
            int b = content.getByte(i) & 0xff;
            builder.append(String.format(format, b));
        }
        return builder.toString();
    }

    /** Parse EthernetAddress from an OctetString.
     * @param content the OctetString to be parsed
     */
    public static EthernetAddress parse(OctetString content)
        throws ParseException {

        EthernetAddress address = new EthernetAddress(content);
        if ((content.getByte(0) == 0) && (content.getByte(1) == 0) &&
            (content.getByte(2) == 0) && (content.getByte(3) == 0) &&
            (content.getByte(4) == 0) && (content.getByte(5) == 0)) {
            address = new EthernetAllZerosAddress(address);
        } else if ((content.getByte(0) == -1) && (content.getByte(1) == -1) &&
            (content.getByte(2) == -1) && (content.getByte(3) == -1) &&
            (content.getByte(4) == -1) && (content.getByte(5) == -1)) {
            address = new EthernetBroadcastAddress(address);
        }
        return address;
    }

    /** Parse EthernetAddress from an OctetReader.
     * @param reader the OctetReader
     * @return the resulting EthernetAddress
     */
    public static EthernetAddress parse(OctetReader reader) {
        try {
            return parse(reader.readOctetString(6));
        } catch (ParseException ex) {
            // This should not happen.
            throw new RuntimeException(ex);
        }
    }

    /** Parse EthernetAddress from a character string.
     * @param string the required address, as a string
     * @return the resulting EthernetAddress
     * @throws ParseException if the string could not be parsed as an address
     */
    public static EthernetAddress parse(String string) throws ParseException {
        if (!string.matches("[0-9a-fA-F]{2}(-[0-9a-fA-F]{2}){5}")) {
            throw new ParseException("invalid character in MAC address");
        }
        byte[] content = new byte[6];
        String[] components = string.split("-");
        for (int i = 0; i != 6; ++i) {
            content[i] = (byte) Integer.parseInt(components[i], 16);
        }
        return parse(new ArrayOctetString(content, OctetString.BIG_ENDIAN));
    }
}
