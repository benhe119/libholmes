// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import java.util.Arrays;

/** An OctetReader class for reading octets stored in a byte array. */
public class ArrayOctetReader extends OctetReader {
    /** The content to be read. */
    private final byte[] content;

    /** The index into the byte array of the next octet to be read. */
    private int index;

    /** The number of octets remaining. */
    private int rem;

    /** Construct an ArrayOctetReader from a byte array.
     * @param content an array with the required content (not copied)
     * @param byteOrder the required byte order
     */
    public ArrayOctetReader(byte[] content, int byteOrder) {
        super(byteOrder);
        this.content = content;
        this.index = 0;
        this.rem = content.length;
    }

    /** Construct an ArrayOctetReader from part of a byte array.
     * @param content an array with the required content (not copied)
     * @param offset the offset into the byte array of the first octet
     *  to be read
     * @param count the number of octets to be read
     * @param byteOrder the required byte order
     */
    public ArrayOctetReader(byte[] content, int offset, int count,
        int byteOrder) {

        super(byteOrder);
        if (count < 0) {
            throw new IllegalArgumentException("octet count is negative");
        }
        if (offset < 0) {
            throw new IndexOutOfBoundsException(
                "negative offset into byte array");
        }
        if (offset > content.length - count) {
            throw new IndexOutOfBoundsException(
                "read beyond end of byte array");
        }
        this.content = content;
        this.index = offset;
        this.rem = count;
    }

    @Override
    public byte readByte() {
        if (rem < 1) {
            throw new IndexOutOfBoundsException(
                "read beyond end of octet stream");
        }
        byte result = content[index];
        index += 1;
        rem -= 1;
        return result;
    }

    @Override
    public short readShort() {
        if (rem < 2) {
            throw new IndexOutOfBoundsException(
                "read beyond end of octet stream");
        }
        int byteOrder = getByteOrder();
        int result = content[index + ((1 ^ byteOrder) & 1)] & 0xff;
        result <<= 8;
        result |= content[index + ((0 ^ byteOrder) & 1)] & 0xff;
        index += 2;
        rem -= 2;
        return (short) result;
    }

    @Override
    public int readInt() {
        if (rem < 4) {
            throw new IndexOutOfBoundsException(
                "read beyond end of octet stream");
        }
        int byteOrder = getByteOrder();
        int result = content[index + ((3 ^ byteOrder) & 3)] & 0xff;
        result <<= 8;
        result |= content[index + ((2 ^ byteOrder) & 3)] & 0xff;
        result <<= 8;
        result |= content[index + ((1 ^ byteOrder) & 3)] & 0xff;
        result <<= 8;
        result |= content[index + ((0 ^ byteOrder) & 3)] & 0xff;
        index += 4;
        rem -= 4;
        return result;
    }

    @Override
    public long readLong() {
        if (rem < 8) {
            throw new IndexOutOfBoundsException(
                "read beyond end of octet stream");
        }
        int byteOrder = getByteOrder();
        long result = content[index + ((7 ^ byteOrder) & 7)] & 0xff;
        result <<= 8;
        result |= content[index + ((6 ^ byteOrder) & 7)] & 0xff;
        result <<= 8;
        result |= content[index + ((5 ^ byteOrder) & 7)] & 0xff;
        result <<= 8;
        result |= content[index + ((4 ^ byteOrder) & 7)] & 0xff;
        result <<= 8;
        result |= content[index + ((3 ^ byteOrder) & 7)] & 0xff;
        result <<= 8;
        result |= content[index + ((2 ^ byteOrder) & 7)] & 0xff;
        result <<= 8;
        result |= content[index + ((1 ^ byteOrder) & 7)] & 0xff;
        result <<= 8;
        result |= content[index + ((0 ^ byteOrder) & 7)] & 0xff;
        index += 8;
        rem -= 8;
        return result;
    }

    @Override
    public byte[] readBytes(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("octet count is negative");
        }
        if (rem < count) {
            throw new IndexOutOfBoundsException(
                "read beyond end of octet stream");
        }
        byte[] result = Arrays.copyOfRange(content, index, index + count);
        index += count;
        rem -= count;
        return result;
    }

    @Override
    public OctetString readOctetString(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("octet count is negative");
        }
        if (rem < count) {
            throw new IndexOutOfBoundsException(
                "read beyond end of octet stream");
        }
        OctetString result = new ArrayOctetString(content, index, count,
            getByteOrder());
        index += count;
        rem -= count;
        return result;
    }

    @Override
    public byte peekByte(int offset) {
        if (offset < 0) {
            throw new IndexOutOfBoundsException(
                "negative offset into octet stream");
        }
        if (offset > rem - 1) {
            throw new IndexOutOfBoundsException(
                "peek beyond end of octet stream");
        }
        return content[index + offset];
    }

    @Override
    public short peekShort(int offset) {
        if (offset < 0) {
            throw new IndexOutOfBoundsException(
                "negative offset into octet stream");
        }
        if (offset > rem - 2) {
            throw new IndexOutOfBoundsException(
                "peek beyond end of octet stream");
        }
        int base = index + offset;
        int byteOrder = getByteOrder();
        int result = content[base + ((1 ^ byteOrder) & 1)] & 0xff;
        result <<= 8;
        result |= content[base + ((0 ^ byteOrder) & 1)] & 0xff;
        return (short) result;
    }

    @Override
    public int peekInt(int offset) {
        if (offset < 0) {
            throw new IndexOutOfBoundsException(
                "negative offset into octet stream");
        }
        if (offset > rem - 4) {
            throw new IndexOutOfBoundsException(
                "peek beyond end of octet stream");
        }
        int base = index + offset;
        int byteOrder = getByteOrder();
        int result = content[base + ((3 ^ byteOrder) & 3)] & 0xff;
        result <<= 8;
        result |= content[base + ((2 ^ byteOrder) & 3)] & 0xff;
        result <<= 8;
        result |= content[base + ((1 ^ byteOrder) & 3)] & 0xff;
        result <<= 8;
        result |= content[base + ((0 ^ byteOrder) & 3)] & 0xff;
        return result;
    }

    @Override
    public long peekLong(int offset) {
        if (offset < 0) {
            throw new IndexOutOfBoundsException(
                "negative offset into octet stream");
        }
        if (offset > rem - 8) {
            throw new IndexOutOfBoundsException(
                "peek beyond end of octet stream");
        }
        int base = index + offset;
        int byteOrder = getByteOrder();
        long result = content[base + ((7 ^ byteOrder) & 7)] & 0xff;
        result <<= 8;
        result |= content[base + ((6 ^ byteOrder) & 7)] & 0xff;
        result <<= 8;
        result |= content[base + ((5 ^ byteOrder) & 7)] & 0xff;
        result <<= 8;
        result |= content[base + ((4 ^ byteOrder) & 7)] & 0xff;
        result <<= 8;
        result |= content[base + ((3 ^ byteOrder) & 7)] & 0xff;
        result <<= 8;
        result |= content[base + ((2 ^ byteOrder) & 7)] & 0xff;
        result <<= 8;
        result |= content[base + ((1 ^ byteOrder) & 7)] & 0xff;
        result <<= 8;
        result |= content[base + ((0 ^ byteOrder) & 7)] & 0xff;
        return result;
    }

    @Override
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
        int base = index + offset;
        return Arrays.copyOfRange(content, base, base + count);
    }

    @Override
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
        int base = index + offset;
        return new ArrayOctetString(content, base, count, getByteOrder());
    }

    @Override
    public OctetReader dupOctetReader() {
        return new ArrayOctetReader(content, index, rem, getByteOrder());
    }

    @Override
    public void skip(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("octet count is negative");
        }
        if (rem < count) {
            throw new IndexOutOfBoundsException(
                "read beyond end of octet stream");
        }
        index += count;
        rem -= count;
    }

    @Override
    public int remaining() {
        return rem;
    }

    @Override
    public boolean hasRemaining() {
        return rem > 0;
    }
}
