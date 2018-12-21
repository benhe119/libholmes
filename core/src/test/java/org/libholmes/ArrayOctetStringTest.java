// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import static org.junit.Assert.*;
import org.junit.Test;

public class ArrayOctetStringTest extends OctetStringTest {
    @Override
    public OctetString makeOctetString(byte[] content, int byteOrder) {
        byte[] newContent = new byte[content.length + 8];
        newContent[0] = 0x55;
        newContent[1] = 0x55;
        newContent[2] = 0x55;
        for (int i = 0; i != content.length; ++i) {
            newContent[3 + i] = content[i];
        }
        newContent[3 + content.length] = 0x55;
        newContent[4 + content.length] = 0x55;
        newContent[5 + content.length] = 0x55;
        newContent[6 + content.length] = 0x55;
        newContent[7 + content.length] = 0x55;
        return new ArrayOctetString(newContent, 3, content.length,
            byteOrder);
    }

    @Test
    public void testConstructFromBytesBigEndian() {
        OctetString string = new ArrayOctetString(new byte[]{0, 1, 2, 3},
            OctetString.BIG_ENDIAN);
        assertEquals(0x00010203, string.getInt(0));
    }

    @Test
    public void testConstructFromBytesLittleEndian() {
        OctetString string = new ArrayOctetString(new byte[]{7, 6, 5, 4},
            OctetString.LITTLE_ENDIAN);
        assertEquals(0x04050607, string.getInt(0));
    }
}
