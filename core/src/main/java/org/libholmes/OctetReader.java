// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

/** An abstract base class for reading from an artefact composed of a
 * sequence of octets.
 *
 * Default implementations are provided for all methods except for peekByte,
 * skip and remaining, in order to reduce the amount of code required to
 * create a minimal working subclass. However, these defaults are unlikely to
 * be particularly efficient and it is expected that they will be overridden in
 * most cases.
 *
 * Byte order control has been designed in a way which could be extended to
 * allow for mixed-endian operation, however this is explicitly disallowed
 * until the consequences are better understood.
 */
public abstract class OctetReader implements Cloneable {
    /** A constant used to indicate little-endian byte order. */
    public static final int LITTLE_ENDIAN = 0;

    /** A constant used to indicate big-endian byte order. */
    public static final int BIG_ENDIAN = -1;

    /** The byte order mask.
     * This takes the value LITTLE_ENDIAN or BIG_ENDIAN.
     */
    private int byteOrder;

    /** Construct OctetReader.
     * @param byteOrder the required byte order
     */
    public OctetReader(int byteOrder) {
        this.byteOrder = byteOrder;
    }

    /** Read an 8-bit byte from the octet stream.
     * @return the byte that was read
     */
    public byte readByte() {
        byte result = peekByte(0);
        skip(1);
        return result;
    }

    /** Read a 16-bit short integer from the octet stream.
     * @return the short integer that was read
     */
    public short readShort() {
        int result = peekByte((1 ^ byteOrder) & 1) & 0xff;
        result <<= 8;
        result |= peekByte((0 ^ byteOrder) & 1) & 0xff;
        skip(2);
        return (short) result;
    }

    /** Read a 32-bit integer from the octet stream.
     * @return the integer that was read
     */
    public int readInt() {
        int result = peekByte((3 ^ byteOrder) & 3) & 0xff;
        result <<= 8;
        result |= peekByte((2 ^ byteOrder) & 3) & 0xff;
        result <<= 8;
        result |= peekByte((1 ^ byteOrder) & 3) & 0xff;
        result <<= 8;
        result |= peekByte((0 ^ byteOrder) & 3) & 0xff;
        skip(4);
        return result;
    }

    /** Read a 64-bit long integer from the octet stream.
     * @return the long integer that was read
     */
    public long readLong() {
        long result = peekByte((7 ^ byteOrder) & 7) & 0xff;
        result <<= 8;
        result |= peekByte((6 ^ byteOrder) & 7) & 0xff;
        result <<= 8;
        result |= peekByte((5 ^ byteOrder) & 7) & 0xff;
        result <<= 8;
        result |= peekByte((4 ^ byteOrder) & 7) & 0xff;
        result <<= 8;
        result |= peekByte((3 ^ byteOrder) & 7) & 0xff;
        result <<= 8;
        result |= peekByte((2 ^ byteOrder) & 7) & 0xff;
        result <<= 8;
        result |= peekByte((1 ^ byteOrder) & 7) & 0xff;
        result <<= 8;
        result |= peekByte((0 ^ byteOrder) & 7) & 0xff;
        skip(8);
        return result;
    }

    /** Read a given-length byte array from the octet stream.
     * @param count the number of octets to be read
     * @return the resulting array of bytes
     */
    public byte[] readBytes(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("octet count is negative");
        }
        byte[] result = new byte[count];
        for (int i = 0; i != count; ++i) {
            result[i] = readByte();
        }
        return result;
    }

    /** Read a given-length OctetString from the octet stream.
     * The exact type of the result is unspecified, and may vary depending
     * on both the type of the originating OctetReader, and the content to
     * be placed within it.
     * @param count the number of octets to be read
     * @return the resulting OctetString
     */
    public OctetString readOctetString(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("octet count is negative");
        }
        byte[] result = new byte[count];
        for (int i = 0; i != count; ++i) {
            result[i] = readByte();
        }
        return new ArrayOctetString(result, byteOrder);
    }

    /** Peek ahead to an 8-bit byte in the octet stream.
     * @param offset the offset in octets from the current position
     * @return the byte that was read
     */
    public abstract byte peekByte(int offset);

    /** Peek ahead to a 16-bit short integer in the octet stream.
     * @param offset the offset in octets from the current position
     * @return the short integer that was read
     */
    public short peekShort(int offset) {
        int result = peekByte(offset + ((1 ^ byteOrder) & 1)) & 0xff;
        result <<= 8;
        result |= peekByte(offset + ((0 ^ byteOrder) & 1)) & 0xff;
        return (short) result;
    }

    /** Peek ahead to a 32-bit integer in the octet stream.
     * @param offset the offset in octets from the current position
     * @return the integer that was read
     */
    public int peekInt(int offset) {
        int result = peekByte(offset + ((3 ^ byteOrder) & 3)) & 0xff;
        result <<= 8;
        result |= peekByte(offset + ((2 ^ byteOrder) & 3)) & 0xff;
        result <<= 8;
        result |= peekByte(offset + ((1 ^ byteOrder) & 3)) & 0xff;
        result <<= 8;
        result |= peekByte(offset + ((0 ^ byteOrder) & 3)) & 0xff;
        return result;
    }

    /** Peek ahead to a 64-bit long integer in the octet stream.
     * @param offset the offset in octets from the current position
     * @return the long integer that was read
     */
    public long peekLong(int offset) {
        long result = peekByte(offset + ((7 ^ byteOrder) & 7)) & 0xff;
        result <<= 8;
        result |= peekByte(offset + ((6 ^ byteOrder) & 7)) & 0xff;
        result <<= 8;
        result |= peekByte(offset + ((5 ^ byteOrder) & 7)) & 0xff;
        result <<= 8;
        result |= peekByte(offset + ((4 ^ byteOrder) & 7)) & 0xff;
        result <<= 8;
        result |= peekByte(offset + ((3 ^ byteOrder) & 7)) & 0xff;
        result <<= 8;
        result |= peekByte(offset + ((2 ^ byteOrder) & 7)) & 0xff;
        result <<= 8;
        result |= peekByte(offset + ((1 ^ byteOrder) & 7)) & 0xff;
        result <<= 8;
        result |= peekByte(offset + ((0 ^ byteOrder) & 7)) & 0xff;
        return result;
    }

    /** Peek ahead to a given-length byte array in the octet stream.
     * @param offset the offset in octets from the current position
     * @param count the number of octets to be read
     * @return the resulting array of bytes
     */
    public byte[] peekBytes(int offset, int count) {
        if (count < 0) {
            throw new IllegalArgumentException("octet count is negative");
        }
        if (offset < 0) {
            throw new IndexOutOfBoundsException(
                "negative offset into octet stream");
        }
        if (offset > remaining() - count) {
            throw new IndexOutOfBoundsException(
                "peek beyond end of octet stream");
        }
        byte[] result = new byte[count];
        for (int i = 0; i != count; ++i) {
            result[i] = peekByte(offset + i);
        }
        return result;
    }

    /** Peek ahead to a given-length OctetString in the octet stream.
     * The exact type of the result is unspecified, and may vary depending
     * on both the type of the originating OctetReader, and the content to
     * be placed within it.
     * @param offset the offset in octets from the current position
     * @param count the number of octets to be read
     * @return the resulting OctetString
     */
    public OctetString peekOctetString(int offset, int count) {
        if (count < 0) {
            throw new IllegalArgumentException("octet count is negative");
        }
        if (offset < 0) {
            throw new IndexOutOfBoundsException(
                "negative offset into octet stream");
        }
        if (offset > remaining() - count) {
            throw new IndexOutOfBoundsException(
                "peek beyond end of octet stream");
        }
        byte[] result = new byte[count];
        for (int i = 0; i != count; ++i) {
            result[i] = peekByte(offset + i);
        }
        return new ArrayOctetString(result, byteOrder);
    }

    /** Skip the given number of octets.
     * @param count the number of octets to skip
     */
    public abstract void skip(int count);

    /** Get the number of octets remaining to be read.
     * @return the number remaining
     */
    public abstract int remaining();

    /** Check whether there are any octets remaining to be read.
     * @return true if there are octets remaining, otherwise false
     */
    public boolean hasRemaining() {
        return remaining() > 0;
    }

    /** Get the current byte order.
     * Currently, this returns the value LITTLE_ENDIAN or BIG_ENDIAN.
     * @return the current byte order
     */
    public final int getByteOrder() {
        return byteOrder;
    }

    /** Set the byte order.
     * Currently, the byte order must be either LITTLE_ENDIAN or
     * BIG_ENDIAN.
     * @param byteOrder the required byte order
     */
    public void setByteOrder(int byteOrder) {
        if ((byteOrder != LITTLE_ENDIAN) && (byteOrder != BIG_ENDIAN)) {
            throw new IllegalArgumentException("invalid byte order");
        }
        this.byteOrder = byteOrder;
    }
}
