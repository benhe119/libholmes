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

    @Test
    public void testString() throws ParseException {
        Inet4Address address = Inet4Address.parse("192.168.0.1");
        assertEquals("192.168.0.1", address.toString());
        assertFalse(address.isMulticastAddress());
        assertFalse(address.isBroadcastAddress());
        assertFalse(address.isWildcardAddress());
        assertFalse(address.isLoopbackAddress());
    }

    @Test
    public void testMulticastString() throws ParseException {
        Inet4Address address = Inet4Address.parse("224.0.0.1");
        assertEquals("224.0.0.1", address.toString());
        assertTrue(address.isMulticastAddress());
        assertFalse(address.isBroadcastAddress());
        assertFalse(address.isWildcardAddress());
        assertFalse(address.isLoopbackAddress());
    }

    @Test
    public void testBroadcastString() throws ParseException {
        Inet4Address address = Inet4Address.parse("255.255.255.255");
        assertEquals("255.255.255.255", address.toString());
        assertTrue(address.isMulticastAddress());
        assertTrue(address.isBroadcastAddress());
        assertFalse(address.isWildcardAddress());
        assertFalse(address.isLoopbackAddress());
    }

    @Test
    public void testWildcardString() throws ParseException {
        Inet4Address address = Inet4Address.parse("0.0.0.0");
        assertEquals("0.0.0.0", address.toString());
        assertFalse(address.isMulticastAddress());
        assertFalse(address.isBroadcastAddress());
        assertTrue(address.isWildcardAddress());
        assertFalse(address.isLoopbackAddress());
    }

    @Test
    public void testLoopbackString() throws ParseException {
        Inet4Address address = Inet4Address.parse("127.0.0.1");
        assertEquals("127.0.0.1", address.toString());
        assertFalse(address.isMulticastAddress());
        assertFalse(address.isBroadcastAddress());
        assertFalse(address.isWildcardAddress());
        assertTrue(address.isLoopbackAddress());
    }

    @Test
    public void testNetworkBroadcastAddresses() throws ParseException {
        Inet4Address address1 = Inet4Address.parse("192.168.231.137");
        assertEquals("192.168.0.0",
            address1.getNetworkAddress(16).toString());
        assertEquals("192.168.255.255",
            address1.getBroadcastAddress(16).toString());

        Inet4Address address2 = Inet4Address.parse("172.21.169.54");
        assertEquals("172.16.0.0",
            address2.getNetworkAddress(12).toString());
        assertEquals("172.31.255.255",
            address2.getBroadcastAddress(12).toString());

        Inet4Address address3 = Inet4Address.parse("10.145.177.89");
        assertEquals("10.0.0.0",
            address3.getNetworkAddress(8).toString());
        assertEquals("10.255.255.255",
            address3.getBroadcastAddress(8).toString());
    }
}
