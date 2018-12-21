// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import static org.junit.Assert.*;
import org.junit.Test;

public class HexOctetReaderTest {
    @Test
    public void testConstructorDefaultEndian() {
        OctetReader reader = new HexOctetReader("01234567");
        assertEquals(0x01234567, reader.readInt());
    }

    @Test
    public void testConstructorBigEndian() {
        OctetReader reader = new HexOctetReader(">01234567");
        assertEquals(0x01234567, reader.readInt());
    }

    @Test
    public void testConstructorLittleEndian() {
        OctetReader reader = new HexOctetReader("<67452301");
        assertEquals(0x01234567, reader.readInt());
    }
}
