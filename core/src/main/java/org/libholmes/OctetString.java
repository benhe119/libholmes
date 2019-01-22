// This file is part of libholmes.
// Copyright 2018-2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import java.nio.charset.Charset;

/** An abstract base class to represent an artefact composed of a sequence
 * of octets.
 *
 * Default implementations are provided for all methods except for getByte and
 * length, in order to reduce the amount of code required to create a minimal
 * working subclass. However, these defaults is unlikely to be particularly
 * efficient, and it is expected that they will be overridden in most cases.
 *
 * Byte order control has been designed in a way which could be extended to
 * allow for mixed-endian operation, however this is explicitly disallowed
 * until the consequences are better understood.
 */
public abstract class OctetString {
    /** A constant used to indicate little-endian byte order. */
    public static final int LITTLE_ENDIAN = 0;

    /** A constant used to indicate big-endian byte order. */
    public static final int BIG_ENDIAN = -1;

    /** The byte order mask.
     * This takes the value LITTLE_ENDIAN or BIG_ENDIAN.
     */
    private final int byteOrder;

    /** Construct OctetString.
     * @param byteOrder the required byte order
     */
    public OctetString(int byteOrder) {
        this.byteOrder = byteOrder;
    }

    /** Get an 8-bit byte from the octet string.
     * @param index the index into the octet string
     * @return the byte that was read
     */
    public abstract byte getByte(int index);

    /** Get a 16-bit short integer from the octet string.
     * @param index the index into the octet string
     * @return the short integer that was read
     */
    public short getShort(int index) {
        int result = getByte(index + ((1 ^ byteOrder) & 1)) & 0xff;
        result <<= 8;
        result |= getByte(index + ((0 ^ byteOrder) & 1)) & 0xff;
        return (short) result;
    }

    /** Get a 32-bit integer from the octet string.
     * @param index the index into the octet string
     * @return the integer that was read
     */
    public int getInt(int index) {
        int result = getByte(index + ((3 ^ byteOrder) & 3)) & 0xff;
        result <<= 8;
        result |= getByte(index + ((2 ^ byteOrder) & 3)) & 0xff;
        result <<= 8;
        result |= getByte(index + ((1 ^ byteOrder) & 3)) & 0xff;
        result <<= 8;
        result |= getByte(index + ((0 ^ byteOrder) & 3)) & 0xff;
        return result;
    }

    /** Get a 64-bit long integer from the octet string.
     * @param index the index into the octet string
     * @return the long integer that was read
     */
    public long getLong(int index) {
        long result = getByte(index + ((7 ^ byteOrder) & 7)) & 0xff;
        result <<= 8;
        result |= getByte(index + ((6 ^ byteOrder) & 7)) & 0xff;
        result <<= 8;
        result |= getByte(index + ((5 ^ byteOrder) & 7)) & 0xff;
        result <<= 8;
        result |= getByte(index + ((4 ^ byteOrder) & 7)) & 0xff;
        result <<= 8;
        result |= getByte(index + ((3 ^ byteOrder) & 7)) & 0xff;
        result <<= 8;
        result |= getByte(index + ((2 ^ byteOrder) & 7)) & 0xff;
        result <<= 8;
        result |= getByte(index + ((1 ^ byteOrder) & 7)) & 0xff;
        result <<= 8;
        result |= getByte(index + ((0 ^ byteOrder) & 7)) & 0xff;
        return result;
    }

    /** Get a given-length byte array from this octet string.
     * @param index the index into the octet string
     * @param count the number of octets to be included
     * @return the resulting array of bytes
     */
    public byte[] getBytes(int index, int count) {
        if (count < 0) {
            throw new IllegalArgumentException("octet count is negative");
        }
        byte[] result = new byte[count];
        for (int i = 0; i != count; ++i) {
            result[i] = getByte(index + i);
        }
        return result;
    }

    /** Get a byte array from this octet string.
     * @return the resulting array of bytes
     */
    public byte[] getBytes() {
        return getBytes(0, length());
    }

    /** Get a character string from part of this octet string.
     * @param index the index into the octet string
     * @param count the number of octets to be included
     * @param charset the character set used to perform the conversion
     * @return the resulting character string
     */
    public String getString(int index, int count, Charset charset) {
        return new String(getBytes(index, count), charset);
    }

    /** Get a character string from this octet string.
     * @param charset the character set used to perform the conversion
     * @return the resulting character string
     */
    public String getString(Charset charset) {
        return new String(getBytes(0, length()), charset);
    }

    /** Get a given-length OctetString from this octet string.
     * @param index the index into the octet string
     * @param count the number of octets to be read
     * @return the resulting octet reader
     */
    public OctetString getOctetString(int index, int count) {
        if (count < 0) {
            throw new IllegalArgumentException("octet count is negative");
        }
        byte[] result = new byte[count];
        for (int i = 0; i != count; ++i) {
            result[i] = getByte(index + i);
        }
        return new ArrayOctetString(result, getByteOrder());
    }

    /** Make a reader for part of this OctetString.
     * @param index the index into the octet string
     * @param count the number of octets to be read
     * @return the resulting OctetReader
     */
    public OctetReader makeOctetReader(int index, int count) {
        if (count < 0) {
            throw new IllegalArgumentException("octet count is negative");
        }
        byte[] result = new byte[count];
        for (int i = 0; i != count; ++i) {
            result[i] = getByte(index + i);
        }
        return new ArrayOctetReader(result, getByteOrder());
    }

    /** Make a reader for the whole of this OctetString.
     * @return the resulting OctetReader
     */
    public OctetReader makeOctetReader() {
        return makeOctetReader(0, length());
    }

    /** Get the length of the octet string.
     * @return the length, in octets
     */
    public abstract int length();

    /** Test whether the string is empty.
     * @return true if empty, otherwise false
     */
    public boolean isEmpty() {
        return length() == 0;
    }

    /** Get the byte order.
     * Currently, this returns the value LITTLE_ENDIAN or BIG_ENDIAN.
     * @return the byte order
     */
    public final int getByteOrder() {
        return byteOrder;
    }

    @Override
    public boolean equals(Object thatObject) {
        // OctetStrings are considered equal if and only if they are of the
        // same length and represent the same sequence of octets. It is
        // immaterial which class they are implemented by or how they were
        // constructed.
        if (thatObject == this) {
            return true;
        }
        if (thatObject == null) {
            return false;
        }
        if (thatObject instanceof OctetString) {
            OctetString that = (OctetString) thatObject;
            int len = this.length();
            if (that.length() != len) {
                return false;
            }
            for (int i = 0; i != len; ++i) {
                if (that.getByte(i) != this.getByte(i)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int len = length();
        int result = 17;
        for (int i = 0; i != len; ++i) {
            result = 31 * result + getByte(i);
        }
        return result;
    }

    @Override
    public String toString() {
        int len = length();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i != len; ++i) {
            builder.append(String.format("%02x", getByte(i) & 0xff));
        }
        return builder.toString();
    }
}
