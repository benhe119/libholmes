// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import static org.junit.Assert.*;
import org.junit.Test;

public class OctetReaderTest {
    class MinimalOctetReader extends OctetReader {
        private final byte[] content;
        private int index;
        private int rem;
        public MinimalOctetReader(byte[] content) {
            super(BIG_ENDIAN);
            this.content = content;
            this.index = 0;
            this.rem = content.length;
        }
        public MinimalOctetReader(byte[] content, int offset, int count) {
            super(BIG_ENDIAN);
            this.content = content;
            this.index = offset;
            this.rem = count;
        }
        public byte peekByte(int offset) {
            if (offset < 0) {
                throw new IndexOutOfBoundsException(
                    "negative offset into octet stream");
            }
            if (offset > rem - 1) {
                throw new IndexOutOfBoundsException(
                    "read beyond end of octet stream");
            }
            return content[index + offset];
        }
        public void skip(int count) {
            if (count < 0) {
                throw new IllegalArgumentException(
                    "octet count is negative");
            }
            if (count > rem) {
                throw new IndexOutOfBoundsException(
                    "skip beyond end of octet stream");
            }
            index += count;
            rem -= count;
        }
        public int remaining() {
            return rem;
        }
    }

    public OctetReader makeOctetReader(byte[] content) {
        return new MinimalOctetReader(content);
    }

    @Test
    public void testReadByte() {
        byte[] raw = {0, 1, 2, 3, 0x7f, -0x80, -1};
        OctetReader reader = makeOctetReader(raw);
        assertEquals(0, reader.readByte());
        assertEquals(1, reader.readByte());
        assertEquals(2, reader.readByte());
        assertEquals(3, reader.readByte());
        assertEquals(0x7f, reader.readByte());
        assertEquals(-0x80, reader.readByte());
        assertEquals(-1, reader.readByte());
        try {
            reader.readByte();
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
    }

    @Test
    public void testReadShortBigEndian() {
        byte[] raw = { 0, 0, 0, 0, 1, 0, 2, 0, 3, 127, -1, -128, 0, -1, -1 };
        OctetReader reader = makeOctetReader(raw);
        reader.readByte(); // Ensure misaligned.
        assertEquals(0, reader.readShort());
        assertEquals(1, reader.readShort());
        assertEquals(2, reader.readShort());
        assertEquals(3, reader.readShort());
        assertEquals(0x7fff, reader.readShort());
        assertEquals(-0x8000, reader.readShort());
        assertEquals(-1, reader.readShort());
        try {
            reader.readShort();
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
    }

    @Test
    public void testReadShortLittleEndian() {
        byte[] raw = { 0, 0, 0, 1, 0, 2, 0, 3, 0, -1, 127, 0, -128, -1, -1 };
        OctetReader reader = makeOctetReader(raw);
        reader.setByteOrder(OctetReader.LITTLE_ENDIAN);
        reader.readByte(); // Ensure misaligned.
        assertEquals(0, reader.readShort());
        assertEquals(1, reader.readShort());
        assertEquals(2, reader.readShort());
        assertEquals(3, reader.readShort());
        assertEquals(0x7fff, reader.readShort());
        assertEquals(-0x8000, reader.readShort());
        assertEquals(-1, reader.readShort());
        try {
            reader.readShort();
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
    }

    @Test
    public void testReadIntBigEndian() {
        byte[] raw = { 0,
            0, 0, 0, 0,
            0, 0, 0, 1,
            0, 0, 0, 2,
            0, 0, 0, 3,
            127, -1, -1, -1,
            -128, 0, 0, 0,
            -1, -1, -1, -1 };
        OctetReader reader = makeOctetReader(raw);
        reader.readByte(); // Ensure misaligned.
        assertEquals(0, reader.readInt());
        assertEquals(1, reader.readInt());
        assertEquals(2, reader.readInt());
        assertEquals(3, reader.readInt());
        assertEquals(0x7fffffff, reader.readInt());
        assertEquals(-0x80000000, reader.readInt());
        assertEquals(-1, reader.readInt());
        try {
            reader.readInt();
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
    }

    @Test
    public void testReadIntLittleEndian() {
        byte[] raw = { 0,
            0, 0, 0, 0,
            1, 0, 0, 0,
            2, 0, 0, 0,
            3, 0, 0, 0,
            -1, -1, -1, 127,
            0, 0, 0, -128,
            -1, -1, -1, -1 };
        OctetReader reader = makeOctetReader(raw);
        reader.setByteOrder(OctetReader.LITTLE_ENDIAN);
        reader.readByte(); // Ensure misaligned.
        assertEquals(0, reader.readInt());
        assertEquals(1, reader.readInt());
        assertEquals(2, reader.readInt());
        assertEquals(3, reader.readInt());
        assertEquals(0x7fffffff, reader.readInt());
        assertEquals(-0x80000000, reader.readInt());
        assertEquals(-1, reader.readInt());
        try {
            reader.readInt();
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
    }

    @Test
    public void testReadLongBigEndian() {
        byte[] raw = { 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 1,
            0, 0, 0, 0, 0, 0, 0, 2,
            0, 0, 0, 0, 0, 0, 0, 3,
            127, -1, -1, -1, -1, -1, -1, -1,
            -128, 0, 0, 0, 0, 0, 0, 0,
            -1, -1, -1, -1, -1, -1, -1, -1 };
        OctetReader reader = makeOctetReader(raw);
        reader.readByte(); // Ensure misaligned.
        assertEquals(0, reader.readLong());
        assertEquals(1, reader.readLong());
        assertEquals(2, reader.readLong());
        assertEquals(3, reader.readLong());
        assertEquals(0x7fffffffffffffffL, reader.readLong());
        assertEquals(-0x8000000000000000L, reader.readLong());
        assertEquals(-1, reader.readLong());
        try {
            reader.readLong();
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
    }

    @Test
    public void testReadLongLittleEndian() {
        byte[] raw = { 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            1, 0, 0, 0, 0, 0, 0, 0,
            2, 0, 0, 0, 0, 0, 0, 0,
            3, 0, 0, 0, 0, 0, 0, 0,
            -1, -1, -1, -1, -1, -1, -1, 127,
            0, 0, 0, 0, 0, 0, 0, -128,
            -1, -1, -1, -1, -1, -1, -1, -1 };
        OctetReader reader = makeOctetReader(raw);
        reader.setByteOrder(OctetReader.LITTLE_ENDIAN);
        reader.readByte(); // Ensure misaligned.
        assertEquals(0, reader.readLong());
        assertEquals(1, reader.readLong());
        assertEquals(2, reader.readLong());
        assertEquals(3, reader.readLong());
        assertEquals(0x7fffffffffffffffL, reader.readLong());
        assertEquals(-0x8000000000000000L, reader.readLong());
        assertEquals(-1, reader.readLong());
        try {
            reader.readLong();
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
    }

    @Test
    public void testReadBytes() {
        byte[] raw = {0, 1, 4, 9, 16, 25, 36, 49};
        for (int i = 0; i != 8; ++i) {
            for (int j = 0; j != 8 - i; ++j) {
                OctetReader reader = makeOctetReader(raw);
                reader.skip(i);
                byte[] bytes = reader.readBytes(j);
                assertEquals(j, bytes.length);
                for (int k = 0; k != j; ++k) {
                    assertEquals(raw[i + k], bytes[k]);
                }
            }
        }
        OctetReader reader = makeOctetReader(raw);
        try {
            reader.readBytes(-1);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException ex) {}
        reader.skip(8);
        try {
            reader.readBytes(1);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
    }

    @Test
    public void testPeekByte() {
        byte[] raw = {0, 1, 2, 3, 0x7f, -0x80, -1};
        OctetReader reader = makeOctetReader(raw);
        assertEquals(0, reader.peekByte(0));
        assertEquals(2, reader.peekByte(2));
        assertEquals(0x7f, reader.peekByte(4));
        reader.readByte();
        assertEquals(1, reader.peekByte(0));
        assertEquals(2, reader.peekByte(1));
        assertEquals(3, reader.peekByte(2));
        assertEquals(0x7f, reader.peekByte(3));
        assertEquals(-0x80, reader.peekByte(4));
        assertEquals(-1, reader.peekByte(5));
        try {
            reader.peekByte(6);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
        try {
            reader.peekByte(-1);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
        assertEquals(-1, reader.peekByte(5));
        assertEquals(0x7f, reader.peekByte(3));
        assertEquals(2, reader.peekByte(1));
    }

    @Test
    public void testPeekShortBigEndian() {
        byte[] raw = { 0, 0, 0, 0, 1, 0, 2, 0, 3, 127, -1, -128, 0, -1, -1 };
        OctetReader reader = makeOctetReader(raw);
        assertEquals(0x7fff, reader.peekShort(9));
        reader.readByte(); // Ensure misaligned.
        assertEquals(0, reader.peekShort(0));
        assertEquals(1, reader.peekShort(2));
        assertEquals(2, reader.peekShort(4));
        assertEquals(3, reader.peekShort(6));
        assertEquals(0x7fff, reader.peekShort(8));
        assertEquals(-0x8000, reader.peekShort(10));
        assertEquals(-1, reader.peekShort(12));
        try {
            reader.peekShort(13);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
        try {
            reader.peekShort(-1);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
        assertEquals(-1, reader.peekShort(12));
        assertEquals(0x7fff, reader.peekShort(8));
        assertEquals(2, reader.peekShort(4));
    }

    @Test
    public void testPeekShortLittleEndian() {
        byte[] raw = { 0, 0, 0, 1, 0, 2, 0, 3, 0, -1, 127, 0, -128, -1, -1 };
        OctetReader reader = makeOctetReader(raw);
        reader.setByteOrder(OctetReader.LITTLE_ENDIAN);
        assertEquals(0x7fff, reader.peekShort(9));
        reader.readByte(); // Ensure misaligned.
        assertEquals(0, reader.peekShort(0));
        assertEquals(1, reader.peekShort(2));
        assertEquals(2, reader.peekShort(4));
        assertEquals(3, reader.peekShort(6));
        assertEquals(0x7fff, reader.peekShort(8));
        assertEquals(-0x8000, reader.peekShort(10));
        assertEquals(-1, reader.peekShort(12));
        try {
            reader.peekShort(13);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
        try {
            reader.peekShort(-1);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
        assertEquals(-1, reader.peekShort(12));
        assertEquals(0x7fff, reader.peekShort(8));
        assertEquals(2, reader.peekShort(4));
    }

    @Test
    public void testPeekIntBigEndian() {
        byte[] raw = { 0,
            0, 0, 0, 0,
            0, 0, 0, 1,
            0, 0, 0, 2,
            0, 0, 0, 3,
            127, -1, -1, -1,
            -128, 0, 0, 0,
            -1, -1, -1, -1 };
        OctetReader reader = makeOctetReader(raw);
        assertEquals(0x7fffffff, reader.peekInt(17));
        reader.readByte(); // Ensure misaligned.
        assertEquals(0, reader.peekInt(0));
        assertEquals(1, reader.peekInt(4));
        assertEquals(2, reader.peekInt(8));
        assertEquals(3, reader.peekInt(12));
        assertEquals(0x7fffffff, reader.peekInt(16));
        assertEquals(-0x80000000, reader.peekInt(20));
        assertEquals(-1, reader.peekInt(24));
        try {
            reader.peekInt(25);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
        try {
            reader.peekInt(-1);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
        assertEquals(-1, reader.peekInt(24));
        assertEquals(0x7fffffff, reader.peekInt(16));
        assertEquals(2, reader.peekInt(8));
    }

    @Test
    public void testPeekIntLittleEndian() {
        byte[] raw = { 0,
            0, 0, 0, 0,
            1, 0, 0, 0,
            2, 0, 0, 0,
            3, 0, 0, 0,
            -1, -1, -1, 127,
            0, 0, 0, -128,
            -1, -1, -1, -1 };
        OctetReader reader = makeOctetReader(raw);
        reader.setByteOrder(OctetReader.LITTLE_ENDIAN);
        assertEquals(0x7fffffff, reader.peekInt(17));
        reader.readByte(); // Ensure misaligned.
        assertEquals(0, reader.peekInt(0));
        assertEquals(1, reader.peekInt(4));
        assertEquals(2, reader.peekInt(8));
        assertEquals(3, reader.peekInt(12));
        assertEquals(0x7fffffff, reader.peekInt(16));
        assertEquals(-0x80000000, reader.peekInt(20));
        assertEquals(-1, reader.peekInt(24));
        try {
            reader.peekInt(25);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
        try {
            reader.peekInt(-1);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
        assertEquals(-1, reader.peekInt(24));
        assertEquals(0x7fffffff, reader.peekInt(16));
        assertEquals(2, reader.peekInt(8));
    }

    @Test
    public void testPeekLongBigEndian() {
        byte[] raw = { 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 1,
            0, 0, 0, 0, 0, 0, 0, 2,
            0, 0, 0, 0, 0, 0, 0, 3,
            127, -1, -1, -1, -1, -1, -1, -1,
            -128, 0, 0, 0, 0, 0, 0, 0,
            -1, -1, -1, -1, -1, -1, -1, -1 };
        OctetReader reader = makeOctetReader(raw);
        assertEquals(0x7fffffffffffffffL, reader.peekLong(33));
        reader.readByte(); // Ensure misaligned.
        assertEquals(0, reader.peekLong(0));
        assertEquals(1, reader.peekLong(8));
        assertEquals(2, reader.peekLong(16));
        assertEquals(3, reader.peekLong(24));
        assertEquals(0x7fffffffffffffffL, reader.peekLong(32));
        assertEquals(-0x8000000000000000L, reader.peekLong(40));
        assertEquals(-1, reader.peekLong(48));
        try {
            reader.peekLong(49);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
        try {
            reader.peekLong(-1);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
        assertEquals(-1, reader.peekLong(48));
        assertEquals(0x7fffffffffffffffL, reader.peekLong(32));
        assertEquals(2, reader.peekLong(16));
    }

    @Test
    public void testPeekLongLittleEndian() {
        byte[] raw = { 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            1, 0, 0, 0, 0, 0, 0, 0,
            2, 0, 0, 0, 0, 0, 0, 0,
            3, 0, 0, 0, 0, 0, 0, 0,
            -1, -1, -1, -1, -1, -1, -1, 127,
            0, 0, 0, 0, 0, 0, 0, -128,
            -1, -1, -1, -1, -1, -1, -1, -1 };
        OctetReader reader = makeOctetReader(raw);
        reader.setByteOrder(OctetReader.LITTLE_ENDIAN);
        assertEquals(0x7fffffffffffffffL, reader.peekLong(33));
        reader.readByte(); // Ensure misaligned.
        assertEquals(0, reader.peekLong(0));
        assertEquals(1, reader.peekLong(8));
        assertEquals(2, reader.peekLong(16));
        assertEquals(3, reader.peekLong(24));
        assertEquals(0x7fffffffffffffffL, reader.peekLong(32));
        assertEquals(-0x8000000000000000L, reader.peekLong(40));
        assertEquals(-1, reader.peekLong(48));
        try {
            reader.peekLong(49);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
        try {
            reader.peekLong(-1);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
        assertEquals(-1, reader.peekLong(48));
        assertEquals(0x7fffffffffffffffL, reader.peekLong(32));
        assertEquals(2, reader.peekLong(16));
    }

    @Test
    public void testPeekBytes() {
        byte[] raw = {0, 1, 4, 9, 16, 25, 36, 49};
        OctetReader reader = makeOctetReader(raw);
        for (int i = 0; i != 8; ++i) {
            for (int j = 0; j != 8 - i; ++j) {
                byte[] bytes = reader.peekBytes(i, j);
                assertEquals(j, bytes.length);
                for (int k = 0; k != j; ++k) {
                    assertEquals(raw[i + k], bytes[k]);
                }
            }
        }
        reader.skip(1);
        try {
            reader.peekBytes(-1, 0);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
        try {
            reader.peekBytes(7, 1);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
        try {
            reader.peekBytes(8, 0);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
        try {
            reader.peekBytes(4, -1);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException ex) {}
    }

    @Test
    public void testSkip() {
        byte[] raw = {0, 1, 2, 3, 4, 5, 6, 7};
        OctetReader reader = makeOctetReader(raw);
        try {
            reader.skip(-1);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException ex) {}
        reader.skip(2);
        assertEquals(2, reader.readByte());
        reader.skip(3);
        assertEquals(6, reader.readByte());
        reader.skip(0);
        assertEquals(7, reader.readByte());
        reader.skip(0);
        try {
            reader.skip(1);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
    }

    public void testRemaining() {
        byte[] raw = {0, 1, 2, 3};
        OctetReader reader = makeOctetReader(raw);
        assertEquals(4, reader.remaining());
        reader.readByte();
        assertEquals(3, reader.remaining());
        reader.readByte();
        assertEquals(2, reader.remaining());
        reader.readByte();
        assertEquals(1, reader.remaining());
        reader.readByte();
        assertEquals(0, reader.remaining());
    }

    @Test
    public void testHasRemaining() {
        byte[] raw = {0, 1, 2, 3};
        OctetReader reader = makeOctetReader(raw);
        assertTrue(reader.hasRemaining());
        reader.readByte();
        assertTrue(reader.hasRemaining());
        reader.readByte();
        assertTrue(reader.hasRemaining());
        reader.readByte();
        assertTrue(reader.hasRemaining());
        reader.readByte();
        assertFalse(reader.hasRemaining());
    }
}
