// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import static org.junit.Assert.*;
import org.junit.Test;

public class OctetStringTest {
    class MinimalOctetString extends OctetString {
        private final byte[] content;
        private int len;
        public MinimalOctetString(byte[] content, int byteOrder) {
            super(byteOrder);
            this.content = content;
            this.len = content.length;
        }
        public byte getByte(int index) {
            return content[index];
        }
        public int length() {
            return len;
        }
    }

    public OctetString makeOctetString(byte[] content, int byteOrder) {
        return new MinimalOctetString(content, byteOrder);
    }

    public final OctetString makeOctetString(byte[] content) {
        return makeOctetString(content, OctetString.BIG_ENDIAN);
    }

    public final OctetString makeOctetStringBigEndian(byte[] content) {
        return makeOctetString(content, OctetString.BIG_ENDIAN);
    }

    public final OctetString makeOctetStringLittleEndian(byte[] content) {
        return makeOctetString(content, OctetString.LITTLE_ENDIAN);
    }

    @Test
    public void testGetByte() {
        byte[] raw = {0, 1, 2, 3, 0x7f, -0x80, -1};
        OctetString string = makeOctetString(raw);
        assertEquals(0, string.getByte(0));
        assertEquals(1, string.getByte(1));
        assertEquals(2, string.getByte(2));
        assertEquals(3, string.getByte(3));
        assertEquals(0x7f, string.getByte(4));
        assertEquals(-0x80, string.getByte(5));
        assertEquals(-1, string.getByte(6));
        try {
            string.getByte(7);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
        try {
            string.getByte(-1);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
    }

    @Test
    public void testGetShortBigEndian() {
        byte[] raw = { 0, 0, 0, 1, 0, 2, 0, 3, 127, -1, -128, 0, -1, -1 };
        OctetString string = makeOctetStringBigEndian(raw);
        assertEquals(0, string.getShort(0));
        assertEquals(0, string.getShort(1));
        assertEquals(1, string.getShort(2));
        assertEquals(0x100, string.getShort(3));
        assertEquals(2, string.getShort(4));
        assertEquals(0x200, string.getShort(5));
        assertEquals(3, string.getShort(6));
        assertEquals(0x37f, string.getShort(7));
        assertEquals(0x7fff, string.getShort(8));
        assertEquals(-0x80, string.getShort(9));
        assertEquals(-0x8000, string.getShort(10));
        assertEquals(0xff, string.getShort(11));
        assertEquals(-1, string.getShort(12));
        try {
            string.getShort(13);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
        try {
            string.getShort(-1);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
    }

    @Test
    public void testGetShortLittleEndian() {
        byte[] raw = { 0, 0, 0, 1, 0, 2, 0, 3, 127, -1, -128, 0, -1, -1 };
        OctetString string = makeOctetStringLittleEndian(raw);
        assertEquals(0, string.getShort(0));
        assertEquals(0, string.getShort(1));
        assertEquals(0x100, string.getShort(2));
        assertEquals(1, string.getShort(3));
        assertEquals(0x200, string.getShort(4));
        assertEquals(2, string.getShort(5));
        assertEquals(0x300, string.getShort(6));
        assertEquals(0x7f03, string.getShort(7));
        assertEquals(-0x81, string.getShort(8));
        assertEquals(-0x7f01, string.getShort(9));
        assertEquals(0x80, string.getShort(10));
        assertEquals(-0x100, string.getShort(11));
        assertEquals(-1, string.getShort(12));
        try {
            string.getShort(13);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
        try {
            string.getShort(-1);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
    }

    @Test
    public void testGetIntBigEndian() {
        byte[] raw = {
            0, 0, 0, 0,
            0, 0, 0, 1,
            0, 0, 0, 2,
            0, 0, 0, 3,
            127, -1, -1, -1,
            -128, 0, 0, 0,
            -1, -1, -1, -1 };
        OctetString string = makeOctetStringBigEndian(raw);
        assertEquals(0, string.getInt(0));
        assertEquals(1, string.getInt(4));
        assertEquals(2, string.getInt(8));
        assertEquals(3, string.getInt(12));
        assertEquals(0x7fffffff, string.getInt(16));
        assertEquals(-0x80000000, string.getInt(20));
        assertEquals(-1, string.getInt(24));
        try {
            string.getInt(25);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
        try {
            string.getInt(-1);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
    }

    @Test
    public void testGetIntLittleEndian() {
        byte[] raw = {
            0, 0, 0, 0,
            1, 0, 0, 0,
            2, 0, 0, 0,
            3, 0, 0, 0,
            -1, -1, -1, 127,
            0, 0, 0, -128,
            -1, -1, -1, -1 };
        OctetString string = makeOctetStringLittleEndian(raw);
        assertEquals(0, string.getInt(0));
        assertEquals(1, string.getInt(4));
        assertEquals(2, string.getInt(8));
        assertEquals(3, string.getInt(12));
        assertEquals(0x7fffffff, string.getInt(16));
        assertEquals(-0x80000000, string.getInt(20));
        assertEquals(-1, string.getInt(24));
        try {
            string.getInt(25);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
        try {
            string.getInt(-1);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
    }

    @Test
    public void testGetLongBigEndian() {
        byte[] raw = {
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 1,
            0, 0, 0, 0, 0, 0, 0, 2,
            0, 0, 0, 0, 0, 0, 0, 3,
            127, -1, -1, -1, -1, -1, -1, -1,
            -128, 0, 0, 0, 0, 0, 0, 0,
            -1, -1, -1, -1, -1, -1, -1, -1 };
        OctetString string = makeOctetStringBigEndian(raw);
        assertEquals(0, string.getLong(0));
        assertEquals(1, string.getLong(8));
        assertEquals(2, string.getLong(16));
        assertEquals(3, string.getLong(24));
        assertEquals(0x7fffffffffffffffL, string.getLong(32));
        assertEquals(-0x8000000000000000L, string.getLong(40));
        assertEquals(-1, string.getLong(48));
        try {
            string.getLong(49);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
        try {
            string.getLong(-1);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
    }

    @Test
    public void testGetLongLittleEndian() {
        byte[] raw = {
            0, 0, 0, 0, 0, 0, 0, 0,
            1, 0, 0, 0, 0, 0, 0, 0,
            2, 0, 0, 0, 0, 0, 0, 0,
            3, 0, 0, 0, 0, 0, 0, 0,
            -1, -1, -1, -1, -1, -1, -1, 127,
            0, 0, 0, 0, 0, 0, 0, -128,
            -1, -1, -1, -1, -1, -1, -1, -1 };
        OctetString string = makeOctetStringLittleEndian(raw);
        assertEquals(0, string.getLong(0));
        assertEquals(1, string.getLong(8));
        assertEquals(2, string.getLong(16));
        assertEquals(3, string.getLong(24));
        assertEquals(0x7fffffffffffffffL, string.getLong(32));
        assertEquals(-0x8000000000000000L, string.getLong(40));
        assertEquals(-1, string.getLong(48));
        try {
            string.getLong(49);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
        try {
            string.getLong(-1);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
    }

    @Test
    public void testGetBytes() {
        byte[] raw = {0, 1, 4, 9, 16, 25, 36, 49};
        OctetString string = makeOctetStringBigEndian(raw);
        for (int i = 0; i != 8; ++i) {
            for (int j = 0; j != 8 - i; ++j) {
                byte[] bytes = string.getBytes(i, j);
                assertEquals(j, bytes.length);
                for (int k = 0; k != j; ++k) {
                    assertEquals(raw[i + k], bytes[k]);
                }
            }
        }
        try {
            string.getBytes(0, 9);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
        try {
            string.getBytes(8, 1);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
        try {
            string.getBytes(-1, 1);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException ex) {}
        try {
            string.getBytes(4, -1);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException ex) {}
    }

    @Test
    public void testLength() {
        assertEquals(makeOctetString(new byte[]{}).length(), 0);
        assertEquals(makeOctetString(new byte[]{0}).length(), 1);
        assertEquals(makeOctetString(new byte[]{0, 0}).length(), 2);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(makeOctetString(new byte[]{}).isEmpty());
        assertFalse(makeOctetString(new byte[]{0}).isEmpty());
        assertFalse(makeOctetString(new byte[]{0, 0}).isEmpty());
    }

    @Test
    public void testEquals() {
        OctetString a0 = makeOctetString(new byte[]{});
        OctetString a1 = makeOctetString(new byte[]{0});
        OctetString a2 = makeOctetString(new byte[]{1});
        OctetString a3 = makeOctetString(new byte[]{1, 2});
        OctetString b0 = makeOctetString(new byte[]{});
        OctetString b1 = makeOctetString(new byte[]{0});
        OctetString b2 = makeOctetString(new byte[]{1});
        OctetString b3 = makeOctetString(new byte[]{1, 2});

        assertTrue(a0.equals(b0));
        assertFalse(a0.equals(b1));
        assertFalse(a0.equals(b2));
        assertFalse(a0.equals(b3));
        assertFalse(a1.equals(b0));
        assertTrue(a1.equals(b1));
        assertFalse(a1.equals(b2));
        assertFalse(a1.equals(b3));
        assertFalse(a2.equals(b0));
        assertFalse(a2.equals(b1));
        assertTrue(a2.equals(b2));
        assertFalse(a2.equals(b3));
        assertFalse(a3.equals(b0));
        assertFalse(a3.equals(b1));
        assertFalse(a3.equals(b2));
        assertTrue(a3.equals(b3));
    }

    @Test
    public void testHashCode() {
        assertEquals(
            makeOctetString(new byte[]{}).hashCode(),
            makeOctetString(new byte[]{}).hashCode());
        assertEquals(
            makeOctetString(new byte[]{0, 1, 2, 3}).hashCode(),
            makeOctetString(new byte[]{0, 1, 2, 3}).hashCode());
        assertEquals(
            makeOctetString(new byte[]{4, 5, 6}).hashCode(),
            makeOctetString(new byte[]{4, 5, 6}).hashCode());
        assertEquals(
            makeOctetString(new byte[]{7}).hashCode(),
            makeOctetString(new byte[]{7}).hashCode());
        assertEquals(
            makeOctetString(new byte[]{0, -1, -2}).hashCode(),
            makeOctetString(new byte[]{0, -1, -2}).hashCode());
    }

    @Test
    public void testToString() {
        assertEquals(makeOctetString(new byte[]{}).toString(), "");
        assertEquals(
            makeOctetString(new byte[]{0, 1, 2, 3}).toString(),
            "00010203");
        assertEquals(
            makeOctetString(new byte[]{0, 127, -128, -1}).toString(),
            "007f80ff");
    }
}
