// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.inet;

import static org.junit.Assert.*;
import org.junit.Test;

import org.libholmes.HexOctetReader;
import org.libholmes.ParseException;

public class Inet4AddressTest {
    @Test
    public void test() {
        Inet4Address address = Inet4Address.parse(
            new HexOctetReader("c0a80001"));
        assertEquals("192.168.0.1", address.toString());
        assertFalse(address.isMulticastAddress());
        assertFalse(address.isBroadcastAddress());
        assertFalse(address.isWildcardAddress());
        assertFalse(address.isLoopbackAddress());
    }

    @Test
    public void testMulticast() {
        Inet4Address address = Inet4Address.parse(
            new HexOctetReader("e0000001"));
        assertEquals("224.0.0.1", address.toString());
        assertTrue(address.isMulticastAddress());
        assertFalse(address.isBroadcastAddress());
        assertFalse(address.isWildcardAddress());
        assertFalse(address.isLoopbackAddress());
    }

    @Test
    public void testBroadcast() {
        Inet4Address address = Inet4Address.parse(
            new HexOctetReader("ffffffff"));
        assertEquals("255.255.255.255", address.toString());
        assertTrue(address.isMulticastAddress());
        assertTrue(address.isBroadcastAddress());
        assertFalse(address.isWildcardAddress());
        assertFalse(address.isLoopbackAddress());
    }

    @Test
    public void testWildcard() {
        Inet4Address address = Inet4Address.parse(
            new HexOctetReader("00000000"));
        assertEquals("0.0.0.0", address.toString());
        assertFalse(address.isMulticastAddress());
        assertFalse(address.isBroadcastAddress());
        assertTrue(address.isWildcardAddress());
        assertFalse(address.isLoopbackAddress());
    }

    @Test
    public void testLoopback() {
        Inet4Address address = Inet4Address.parse(
            new HexOctetReader("7f000001"));
        assertEquals("127.0.0.1", address.toString());
        assertFalse(address.isMulticastAddress());
        assertFalse(address.isBroadcastAddress());
        assertFalse(address.isWildcardAddress());
        assertTrue(address.isLoopbackAddress());

        assertTrue(Inet4Address.parse(new HexOctetReader("7f000000")).
            isLoopbackAddress());
        assertTrue(Inet4Address.parse(new HexOctetReader("7f00002a")).
            isLoopbackAddress());
        assertTrue(Inet4Address.parse(new HexOctetReader("7fffffff")).
            isLoopbackAddress());
    }
}
