// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

/** An OctetReader class for reading the content of a hex string. */
public class HexOctetReader extends ArrayOctetReader {
    /** Extract the byte content from a hex string
     * The byte order character, if there is one, is skipped.
     * @param hex the hex string to be parsed
     * @return the content as an array of bytes
     */
    private static byte[] parseContent(String hex) {
        int offset = 0;
        int remaining = hex.length();
        if (remaining != 0) {
            char firstChar = hex.charAt(0);
            if ((firstChar == '<') || (firstChar == '>')) {
                offset += 1;
                remaining -= 1;
            }
        }
        byte[] content = new byte[remaining / 2];
        int index = 0;
        while (remaining > 0) {
            int d0 = Character.digit(hex.charAt(offset + 0), 16);
            if (d0 == -1) {
                throw new NumberFormatException(
                    "invalid character in hex string");
            }
            if (remaining < 2) {
                throw new NumberFormatException(
                    "odd number of digits in hex string");
            }
            int d1 = Character.digit(hex.charAt(offset + 1), 16);
            if (d1 == -1) {
                throw new NumberFormatException(
                    "invalid character in hex string");
            }
            content[index] = (byte) ((d0 << 4) | d1);
            index += 1;
            offset += 2;
            remaining -= 2;
        }
        return content;
    }

    /** Extract the byte order from a hex string.
     * @param hex the hex string to be parsed
     * @return the byte order
     */
    private static int parseByteOrder(String hex) {
        if (hex.isEmpty()) {
            return BIG_ENDIAN;
        }
        if (hex.charAt(0) == '<') {
            return LITTLE_ENDIAN;
        } else {
            return BIG_ENDIAN;
        }
    }

    /** Construct HexOctetReader from a hex string.
     * The string may optionally begin with a '>' or '<' character to indicate
     * big- or little-endian byte order respectively. If no byte order is
     * specified then the default is network byte order (big endian).
     * The remainder of the string may use upper case, lower case or a mixture,
     * but must contain an even number of hex digits with no other characters.
     * @param hex the required content, as a hex string
     */
    public HexOctetReader(String hex) {
        super(parseContent(hex), parseByteOrder(hex));
    }
}
