// This file is part of libholmes.
// Copyright 2018-2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.inet;

import org.libholmes.OctetReader;
import org.libholmes.OctetString;
import org.libholmes.ArrayOctetString;
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

    @Override
    public final Inet4Address getNetworkAddress(int prefixLength) {
        if ((prefixLength < 0) || (prefixLength > 32)) {
            throw new IllegalArgumentException("Invalid CIDR prefix length");
        }
        byte[] bytes = content.getBytes();
        int index = 3;

        int remaining = 32 - prefixLength;
        while (remaining >= 8) {
            bytes[index] = 0;
            index -= 1;
            remaining -= 8;
        }
        if (remaining > 0) {
            bytes[index] &= ~((1 << remaining) - 1);
        }

        try {
            return parse(new ArrayOctetString(bytes, content.getByteOrder()));
        } catch (ParseException ex) {
            // It should not be possible for the address length to be incorrect.
            throw new RuntimeException(ex);
        }
    }

    @Override
    public final Inet4Address getBroadcastAddress(int prefixLength) {
        if ((prefixLength < 0) || (prefixLength > 32)) {
            throw new IllegalArgumentException("Invalid CIDR prefix length");
        }
        byte[] bytes = content.getBytes();
        int index = 3;

        int remaining = 32 - prefixLength;
        while (remaining >= 8) {
            bytes[index] = (byte)0xff;
            index -= 1;
            remaining -= 8;
        }
        if (remaining > 0) {
            bytes[index] |= ((1 << remaining) - 1);
        }

        try {
            return parse(new ArrayOctetString(bytes, content.getByteOrder()));
        } catch (ParseException ex) {
            // It should not be possible for the address length to be incorrect.
            throw new RuntimeException(ex);
        }
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

    /** Parse Inet4Address from a String in dotted quad format.
     * @param addrStr the address as a character string
     */
    public static Inet4Address parse(String addrStr) throws ParseException {
        int byteIndex = 0;
        int byteLength = 4;
        byte[] bytes = new byte[byteLength];
        int acc = 0;
        int digits = 0;
        int charIndex = 0;
        int charLength = addrStr.length();

        while (charIndex < charLength) {
            char c = addrStr.charAt(charIndex);
            if (c == '.') {
                if (digits == 0) {
                    // Empty field not allowed in dotted quad section.
                    throw new ParseException(
                        "Invalid empty field in IPv4 address");
                } else {
                    // Non-empty field terminated by full stop,
                    // record value.
                    if (byteIndex >= byteLength) {
                        throw new ParseException(
                            "Too many fields in IPv4 address");
                    }
                    if (acc >= 0x100) {
                        throw new ParseException(
                            "Value too large in IPv4 address");
                    }
                    bytes[byteIndex] = (byte)acc;
                    byteIndex += 1;
                    acc = 0;
                    digits = 0;
                }
            } else {
                if ((c >= '0') && (c <= '9')) {
                    acc *= 10;
                    acc += c - '0';
                } else {
                    throw new ParseException(
                        "Invalid character in IPv4 address");
                }
                digits += 1;
                if (digits > 3) {
                    throw new ParseException(
                        "Field too long in IPv4 address");
                }
            }
            charIndex += 1;
        }

        if (digits == 0) {
            // Trailing full stop not allowed.
            throw new ParseException(
                "Invalid empty field in IPv4 address");
        } else {
            // Non-empty field at end of decimal address, record value.
            if (byteIndex >= byteLength) {
                throw new ParseException(
                    "Too many fields in IPv4 address");
            }
            if (acc >= 0x100) {
                throw new ParseException(
                    "Value too large in IPv4 address");
            }
            bytes[byteIndex] = (byte)acc;
            byteIndex += 1;
            acc = 0;
            digits = 0;
        }

        if (byteIndex < byteLength) {
            throw new ParseException(
                "Too few fields in IPv4 address");
        }

        return parse(new ArrayOctetString(bytes,
            ArrayOctetString.BIG_ENDIAN));
    }
}
