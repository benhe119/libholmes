// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.inet;

import static org.junit.Assert.*;
import org.junit.Test;

import org.libholmes.HexOctetString;

public class InetChecksumTest {
    @Test
    public void test() {
        InetChecksum checksum = new InetChecksum();
        assertEquals(checksum.get(), 0xffff);
        checksum.add(0);
        assertEquals(checksum.get(), 0xffff);
        checksum.add(1);
        assertEquals(checksum.get(), 0xfffe);
        checksum.add(1);
        assertEquals(checksum.get(), 0xfffd);
        checksum.add(0xfffd);
        assertEquals(checksum.get(), 0x0000);
        checksum.add(1);
        assertEquals(checksum.get(), 0xfffe);
    }

    @Test
    public void testRfcExample() {
        InetChecksum checksum = new InetChecksum();
        checksum.add(0x0001);
        checksum.add(0xf203);
        checksum.add(0xf4f5);
        checksum.add(0xf6f7);
        assertEquals(checksum.get(), 0xddf2 ^ 0xffff);
    }

    @Test
    public void testInetHeader() {
        InetChecksum checksum = new InetChecksum();
        checksum.add(new HexOctetString(
            ">4500001cc9b4000040012ef0c0a80001" +
            "c0a800eb"));
        assertEquals(checksum.get(), 0);
        assertTrue(checksum.isValid());
    }
}
