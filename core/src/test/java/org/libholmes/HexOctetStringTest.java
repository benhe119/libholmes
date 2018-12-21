// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import static org.junit.Assert.*;
import org.junit.Test;

public class HexOctetStringTest {
    @Test
    public void testConstructorDefaultEndian() {
        OctetString string = new HexOctetString("01234567");
        assertEquals(0x01234567, string.getInt(0));
    }

    @Test
    public void testConstructorBigEndian() {
        OctetString string = new HexOctetString(">01234567");
        assertEquals(0x01234567, string.getInt(0));
    }

    @Test
    public void testConstructorLittleEndian() {
        OctetString string = new HexOctetString("<67452301");
        assertEquals(0x01234567, string.getInt(0));
    }

}
