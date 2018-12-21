// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import static org.junit.Assert.*;
import org.junit.Test;

public class ArrayOctetReaderTest extends OctetReaderTest {
    @Override
    public OctetReader makeOctetReader(byte[] content) {
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
        return new ArrayOctetReader(newContent, 3, content.length,
            OctetReader.BIG_ENDIAN);
    }

    @Test
    public void testConstructFromBytes() {
        OctetReader reader = new ArrayOctetReader(new byte[]{1, 2, 3},
            OctetReader.BIG_ENDIAN);
        assertArrayEquals(new byte[]{1, 2, 3}, reader.readBytes(3));
    }
}
