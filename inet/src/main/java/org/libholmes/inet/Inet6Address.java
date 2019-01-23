// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.inet;

import org.libholmes.OctetReader;
import org.libholmes.OctetString;
import org.libholmes.ArrayOctetString;
import org.libholmes.Address;
import org.libholmes.ParseException;

/** A class to represent an IPv6 address. */
public class Inet6Address extends InetAddress {
   /** Copy-construct an Inet6Address from another Inet6Address.
     * @param that the Inet6Address to be copied
     */
    protected Inet6Address(Inet6Address that) {
        super(that);
    }

    /** Construct Inet6Address from an OctetString.
     * @param content the required content
     */
    protected Inet6Address(OctetString content) throws ParseException {
        super(content);
        if (content.length() != 16) {
            throw new ParseException("invalid length for IPv6 address");
        }
    }

    @Override
    public int getFlags() {
        return 0;
    }

    @Override
    public final String toString() {
        // Use the canonical representation defined by RFC 5952
        // (but no support currently for embedded IPv4 addresses).
        int longestRunIndex = 0;
        int longestRunLength = 0;
        int currentRunLength = 0;
        for (int i = 0; i != 8; ++i) {
            if (content.getShort(i * 2) == 0) {
                currentRunLength += 1;
                if (currentRunLength > longestRunLength) {
                    longestRunIndex = i + 1 - currentRunLength;
                    longestRunLength = currentRunLength;
                }
            }
        }

        // Do not use compression for single zeros.
        if (longestRunLength < 2) {
            longestRunIndex = -1;
            longestRunLength = 0;
        }

        StringBuilder builder = new StringBuilder();
        boolean needSeparator = false;
        for (int i = 0; i != 8; ++i) {
            if (i == longestRunIndex) {
                // Beginning compressed region.
                builder.append("::");
                needSeparator = false;
            } else if ((i > longestRunIndex) &&
                (i < longestRunIndex + longestRunLength)) {
                // Continuing compressed region: no action
            } else {
                if (needSeparator) {
                    builder.append(":");
                }
                int v = content.getShort(i * 2) & 0xffff;
                builder.append(String.format("%x", v));
                needSeparator = true;
            }
        }
        return builder.toString();
    }

    @Override
    public final Inet6Address getNetworkAddress(int prefixLength) {
        if ((prefixLength < 0) || (prefixLength > 128)) {
            throw new IllegalArgumentException("Invalid CIDR prefix length");
        }
        byte[] bytes = content.getBytes();
        int index = 15;

        int remaining = 128 - prefixLength;
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
    public final Inet6Address getBroadcastAddress(int prefixLength) {
        if ((prefixLength < 0) || (prefixLength > 128)) {
            throw new IllegalArgumentException("Invalid CIDR prefix length");
        }
        byte[] bytes = content.getBytes();
        int index = 15;

        int remaining = 128 - prefixLength;
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

    /** Parse Inet6Address from an OctetString.
     * @param content the OctetString to be parsed
     */
    public static Inet6Address parse(OctetString content)
        throws ParseException {

        Inet6Address address = new Inet6Address(content);
        long numAddrPrefix = content.getLong(0);
        long numAddrSuffix = content.getLong(8);
        if (numAddrPrefix == 0) {
            if (numAddrSuffix == 0) {
                return new Inet6UnspecifiedAddress(address);
            } else if (numAddrSuffix == 1) {
                return new Inet6LoopbackAddress(address);
            }
        }
        if ((content.getByte(0) & 0xff) == 0xff) {
            return new Inet6MulticastAddress(address);
        }
        return address;
    }

    /** Parse Inet6Address from an OctetReader.
     * @param reader the OctetReader
     * @return the resulting Inet6Address
     */
    public static Inet6Address parse(OctetReader reader) {
        try {
            return parse(reader.readOctetString(16));
        } catch (ParseException ex) {
            // This should not happen.
            throw new RuntimeException(ex);
        }
    }

    /** Parse Inet6Address from a String.
     * Any of the textual formats defined by RFC 4291 are permitted
     * (uncompressed, compressed, or with a dotted quad suffix).
     * @param addrStr the address as a character string
     * @return the resulting Inet6Address
     */
    public static Inet6Address parse(String addrStr) throws ParseException {
        int byteIndex = 0;
        int byteLength = 16;
        byte[] bytes = new byte[byteLength];
        int acc = 0;
        int digits = 0;
        int compressIndex = -1;
        int charIndex = 0;
        int charLength = addrStr.length();

        while (charIndex < charLength) {
            char c = addrStr.charAt(charIndex);
            if (c == ':') {
                if (charIndex == 0) {
                    // Leading colon only allowed if part of double colon.
                    if ((charLength < 2) || (addrStr.charAt(1) != ':')) {
                        throw new ParseException(
                            "Invalid empty field in IPv6 address");
                    }
                } else if (digits == 0) {
                    // Empty field indicates compression, which may only occur
                    // once.
                    if (compressIndex == -1) {
                        compressIndex = byteIndex;
                    } else {
                        throw new ParseException(
                            "Multiple compressed fields in IPv6 address");
                    }
                } else {
                    // Non-empty field terminated by colon, record value.
                    if (byteIndex >= byteLength) {
                        throw new ParseException(
                            "Too many fields in IPv6 address");
                    }
                    bytes[byteIndex + 0] = (byte)(acc >> 8);
                    bytes[byteIndex + 1] = (byte)(acc >> 0);
                    byteIndex += 2;
                    acc = 0;
                    digits = 0;
                }
            } else if (c == '.') {
                break;
            } else {
                if ((c >= '0') && (c <= '9')) {
                    acc <<= 4;
                    acc += c - '0';
                } else if ((c >= 'A') && (c <= 'F')) {
                    acc <<= 4;
                    acc += c - 'A' + 0xa;
                } else if ((c >= 'a') && (c <= 'f')) {
                    acc <<= 4;
                    acc += c - 'a' + 0xa;
                } else {
                    throw new ParseException(
                        "Invalid character in IPv6 address");
                }
                digits += 1;
                if (digits > 4) {
                    throw new ParseException(
                        "field too long in IPv6 address");
                }
            }
            charIndex += 1;
        }

        if (charIndex == charLength) {
            // This is the normal outcome for an IPv6 address which does not
            // end with a dotted-quad IPv4 address.
            if (digits == 0) {
                // Trailing colon only allowed if part of double colon.
                if (compressIndex != byteIndex) {
                    throw new ParseException(
                        "Invalid empty field in IPv6 address");
                }
            } else {
                // Non-empty field at end of hex address, record value.
                if (byteIndex >= byteLength) {
                    throw new ParseException(
                        "Too many fields in IPv6 address");
                }
                bytes[byteIndex + 0] = (byte)(acc >> 8);
                bytes[byteIndex + 1] = (byte)(acc >> 0);
                byteIndex += 2;
            }
        } else {
            // This can only happen if a full stop was encountered.
            // Backtrack to the start of the preceding field, discarding
            // the hexadecimal interpretation of its content.
            charIndex -= digits;
            digits = 0;
            acc = 0;
            int decimalFields = 0;

            if (byteIndex > byteLength - 4) {
                throw new ParseException(
                    "Too many fields in IPv6 address");
            }
            while (charIndex < charLength) {
                char c = addrStr.charAt(charIndex);
                if (c == '.') {
                    if (digits == 0) {
                        // Empty field not allowed in dotted quad section.
                        throw new ParseException(
                            "Invalid empty field in IPv6 address");
                    } else {
                        // Non-empty field terminated by full stop,
                        // record value.
                        if (decimalFields >= 4) {
                            throw new ParseException(
                                "Too many decimal fields in IPv6 address");
                        }
                        if (acc >= 0x100) {
                            throw new ParseException(
                                "Decimal value too large in IPv6 address");
                        }
                        bytes[byteIndex] = (byte)acc;
                        byteIndex += 1;
                        decimalFields += 1;
                        acc = 0;
                        digits = 0;
                    }
                } else {
                    if ((c >= '0') && (c <= '9')) {
                        acc *= 10;
                        acc += c - '0';
                    } else {
                        throw new ParseException(
                            "Invalid character in IPv6 address");
                    }
                    digits += 1;
                    if (digits > 3) {
                        throw new ParseException(
                            "Decimal field too long in IPv6 address");
                    }
                }
                charIndex += 1;
            }
            if (digits == 0) {
                // Trailing full stop not allowed.
                throw new ParseException(
                    "Invalid empty field in IPv6 address");
            } else {
                // Non-empty field at end of decimal address, record value.
                if (decimalFields >= 4) {
                    throw new ParseException(
                        "Too many decimal fields in IPv6 address");
                }
                if (acc >= 0x100) {
                    throw new ParseException(
                        "Decimal value too large in IPv6 address");
                }
                bytes[byteIndex] = (byte)acc;
                byteIndex += 1;
                decimalFields += 1;
                acc = 0;
                digits = 0;
            }
            if (decimalFields < 4) {
                throw new ParseException(
                    "Too few decimal fields in IPv6 address");
            }
        }

        if (compressIndex == -1) {
            // If no compression then there must have been enough fields to
            // complete the address.
            if (byteIndex < byteLength) {
                throw new ParseException("Too few fields in IPv6 address");
            }
        } else {
            // Compression must expand to at least one field.
            if (byteIndex >= byteLength) {
                throw new ParseException(
                    "Too many fields in IPv6 address");
            }

            // Decompress by moving everything to the right of the double
            // colon to the end of the address, then filling with zeros.
            int offset = byteLength - byteIndex;
            for (int i = byteIndex - 1; i >= compressIndex; --i) {
                bytes[i + offset] = bytes[i];
            }
            for (int i = compressIndex + offset - 1; i >= compressIndex; --i) {
                bytes[i] = 0;
            }
        }

        return parse(new ArrayOctetString(bytes,
            ArrayOctetString.BIG_ENDIAN));
    }
}
