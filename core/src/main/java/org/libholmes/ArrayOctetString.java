// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import java.util.Arrays;

/** An OctetString class for content stored in a byte array. */
public class ArrayOctetString extends OctetString {
    /** A byte array in which the required content is present (but potentially
     * with extra data at the start and/or end). */
    private final byte[] content;

    /** The offset, in octets, from the start of the byte array to the start of
     * the required content. */
    private final int offset;

    /** The length of the required content, in octets. */
    private final int len;

    /** Construct ArrayOctetString from byte array.
     * @param content an array with the required content (not copied)
     * @param byteOrder the required byte order
     */
    public ArrayOctetString(byte[] content, int byteOrder) {
        super(byteOrder);
        this.content = content;
        this.offset = 0;
        this.len = content.length;
    }

    /** Construct an ArrayOctetString from part of a byte array.
     * @param content an array with the required content (not copied)
     * @param offset the offset into the byte array of the first octet to be
     *  included
     * @param count the number of octets to be included
     * @param byteOrder the required byte order
     */
    public ArrayOctetString(byte[] content, int offset, int count,
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
        this.offset = offset;
        this.len = count;
    }

    @Override
    public byte getByte(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException(
                "negative index into octet string");
        }
        if (index > len - 1) {
            throw new IndexOutOfBoundsException(
                "read beyond end of octet string");
        }
        return content[offset + index];
    }

    @Override
    public short getShort(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException(
                "negative index into octet string");
        }
        if (index > len - 2) {
            throw new IndexOutOfBoundsException(
                "read beyond end of octet string");
        }
        int base = offset + index;
        int byteOrder = getByteOrder();
        int result = content[base + ((1 ^ byteOrder) & 1)] & 0xff;
        result <<= 8;
        result |= content[base + ((0 ^ byteOrder) & 1)] & 0xff;
        return (short) result;
    }

    @Override
    public int getInt(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException(
                "negative index into octet string");
        }
        if (index > len - 4) {
            throw new IndexOutOfBoundsException(
                "read beyond end of octet string");
        }
        int base = offset + index;
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
    public long getLong(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException(
                "negative index into octet string");
        }
        if (index > len - 8) {
            throw new IndexOutOfBoundsException(
                "read beyond end of octet string");
        }
        int base = offset + index;
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
    public byte[] getBytes(int index, int count) {
        if (count < 0) {
            throw new IllegalArgumentException("octet count is negative");
        }
        if (index < 0) {
            throw new IndexOutOfBoundsException(
                "negative index into octet string");
        }
        if (index > len - count) {
            throw new IndexOutOfBoundsException(
                "read beyond end of octet string");
        }
        int base = offset + index;
        return Arrays.copyOfRange(content, base, base + count);
    }

    @Override
    public OctetReader makeOctetReader(int index, int count) {
        if (count < 0) {
            throw new IllegalArgumentException("octet count is negative");
        }
        if (index < 0) {
            throw new IndexOutOfBoundsException(
                "negative index into octet string");
        }
        if (index > len - count) {
            throw new IndexOutOfBoundsException(
                "read beyond end of octet string");
        }
        int base = offset + index;
        return new ArrayOctetReader(content, base, count, getByteOrder());
    }

    @Override
    public OctetReader makeOctetReader() {
        return new ArrayOctetReader(content, offset, len, getByteOrder());
    }

    @Override
    public int length() {
        return len;
    }

    @Override
    public boolean isEmpty() {
        return len == 0;
    }

    @Override
    public boolean equals(Object thatObject) {
        // OctetStrings are considered equal if and only if they are of the
        // same length and represent the same sequence of octets. It is
        // immaterial which class they are implemented by or how they how
        // they were constructed.
        if (thatObject == this) {
            return true;
        }
        if (thatObject == null) {
            return false;
        }
        if (thatObject instanceof ArrayOctetString) {
            ArrayOctetString that = (ArrayOctetString) thatObject;
            if (that.len != this.len) {
                return false;
            }
            for (int i = 0; i != this.len; ++i) {
                byte thatByte = that.content[that.offset + i];
                byte thisByte = this.content[this.offset + i];
                if (thatByte != thisByte) {
                    return false;
                }
            }
            return true;
        } else if (thatObject instanceof OctetString) {
            OctetString that = (OctetString) thatObject;
            if (that.length() != this.len) {
                return false;
            }
            for (int i = 0; i != this.len; ++i) {
                byte thatByte = that.getByte(i);
                byte thisByte = this.content[this.offset + i];
                if (thatByte != thisByte) {
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
        int result = 17;
        for (int i = 0; i != len; ++i) {
            result = 31 * result + content[offset + i];
        }
        return result;
    }

    @Override
    public final String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i != len; ++i) {
            builder.append(String.format("%02x", content[offset + i] & 0xff));
        }
        return builder.toString();
    }
}
