// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.inet;

import org.libholmes.OctetReader;
import org.libholmes.OctetString;
import org.libholmes.Address;
import org.libholmes.ParseException;

/** A class to represent an IPv4 address. */
public class Inet4Address extends InetAddress {
    /** Copy-construct an Inet4Address from another Inet4Address.
     * @param that the Inet4Address to be copied
     */
    protected Inet4Address(Inet4Address that) {
        super(that);
    }

    /** Construct Inet4Address from an OctetString.
     * @param content the required content
     */
    protected Inet4Address(OctetString content) throws ParseException {
        super(content);
        if (content.length() != 4) {
            throw new ParseException("invalid length for IPv4 address");
        }
    }

    @Override
    public int getFlags() {
        return 0;
    }

    @Override
    public final String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0, n = content.length(); i != n; ++i) {
            if (builder.length() != 0) {
                builder.append('.');
            }
            int v = content.getByte(i) & 0xff;
            builder.append(String.format("%d", v));
        }
        return builder.toString();
    }

    /** Parse Inet4Address from an OctetString.
     * @param content the OctetString to be parsed
     */
    public static Inet4Address parse(OctetString content)
        throws ParseException {

        Inet4Address address = new Inet4Address(content);
        int numAddr = content.getInt(0);
        switch (numAddr) {
            case 0:
                return new Inet4UnspecifiedAddress(address);
            case -1:
                return new Inet4BroadcastAddress(address);
        }
        switch ((numAddr >> 24) & 0xff) {
            case 0x7f:
                return new Inet4LoopbackAddress(address);
        }
        switch ((numAddr >> 28) & 0xf) {
            case 0xe:
                return new Inet4MulticastAddress(address);
            case 0xf:
                return new Inet4ReservedAddress(address);
        }
        return address;
    }

    /** Parse Inet4Address from an OctetReader.
     * @param reader the OctetReader
     * @return the resulting Inet4Address
     */
    public static Inet4Address parse(OctetReader reader) {
        try {
            return parse(reader.readOctetString(4));
        } catch (ParseException ex) {
            // This should not happen.
            throw new RuntimeException(ex);
        }
    }
}
