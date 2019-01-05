// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.inet;

import static org.junit.Assert.*;
import org.junit.Test;

import org.libholmes.HexOctetReader;
import org.libholmes.ParseException;

public class InetNetblockTest {
    @Test
    public void test() throws ParseException {
        InetNetblock netblock = new InetNetblock(
            Inet4Address.parse("192.168.231.0"), 24);
        assertEquals("192.168.231.0", netblock.getPrefix().toString());
    }

    @Test(expected = ParseException.class)
    public void testBadPrefix() throws ParseException {
        InetNetblock netblock = InetNetblock.parse("192.168.231.137/24");
    }

    @Test
    public void testContains24() throws ParseException {
        InetNetblock netblock = InetNetblock.parse("192.168.231.0/24");
        assertFalse(netblock.contains(Inet4Address.parse("192.168.230.255")));
        assertTrue(netblock.contains(Inet4Address.parse("192.168.231.0")));
        assertTrue(netblock.contains(Inet4Address.parse("192.168.231.137")));
        assertTrue(netblock.contains(Inet4Address.parse("192.168.231.255")));
        assertFalse(netblock.contains(Inet4Address.parse("192.168.232.0")));
    }

    @Test
    public void testContains20() throws ParseException {
        InetNetblock netblock = InetNetblock.parse("192.168.224.0/20");
        assertFalse(netblock.contains(Inet4Address.parse("192.168.223.255")));
        assertTrue(netblock.contains(Inet4Address.parse("192.168.224.0")));
        assertTrue(netblock.contains(Inet4Address.parse("192.168.231.137")));
        assertTrue(netblock.contains(Inet4Address.parse("192.168.239.255")));
        assertFalse(netblock.contains(Inet4Address.parse("192.168.240.0")));
    }

    @Test
    public void testContains16() throws ParseException {
        InetNetblock netblock = InetNetblock.parse("192.168.0.0/16");
        assertFalse(netblock.contains(Inet4Address.parse("192.167.255.255")));
        assertTrue(netblock.contains(Inet4Address.parse("192.168.0.0")));
        assertTrue(netblock.contains(Inet4Address.parse("192.168.231.137")));
        assertTrue(netblock.contains(Inet4Address.parse("192.168.255.255")));
        assertFalse(netblock.contains(Inet4Address.parse("192.169.0.0")));
    }
}
