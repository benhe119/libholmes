// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.inet;

import static org.junit.Assert.*;
import org.junit.Test;

import org.libholmes.HexOctetReader;
import org.libholmes.ParseException;

public class Inet6AddressTest {
    private void _testUnspecified(String addrStr)
        throws ParseException {

        InetAddress address = InetAddress.parse(addrStr);
        assertEquals("::", address.toString());
        assertFalse(address.isMulticastAddress());
        assertFalse(address.isBroadcastAddress());
        assertTrue(address.isWildcardAddress());
        assertFalse(address.isLoopbackAddress());
    }

    private void _testLoopback(String addrStr)
        throws ParseException {

        InetAddress address = InetAddress.parse(addrStr);
        assertEquals("::1", address.toString());
        assertFalse(address.isMulticastAddress());
        assertFalse(address.isBroadcastAddress());
        assertFalse(address.isWildcardAddress());
        assertTrue(address.isLoopbackAddress());
    }

    private void _testRegular(String addrStr, String normStr)
        throws ParseException {

        InetAddress address = InetAddress.parse(addrStr);
        assertEquals(normStr, address.toString());
        assertFalse(address.isMulticastAddress());
        assertFalse(address.isBroadcastAddress());
        assertFalse(address.isWildcardAddress());
        assertFalse(address.isLoopbackAddress());
    }

    private void _test96Zeros(String addrStr) throws ParseException {
        _testRegular(addrStr, "2001:db8::");
    }

    private void _test80Zeros(String addrStr) throws ParseException {
        _testRegular(addrStr, "2001:db8::1");
    }

    private void _test32Zeros(String addrStr) throws ParseException {
        _testRegular(addrStr, "2001:db8::ffff:ffff:ffff:ffff");
    }

    private void _test16Zeros(String addrStr) throws ParseException {
        // As per RFC 5952, single zeros are not compressed.
        _testRegular(addrStr, "2001:db8:0:ffff:ffff:ffff:ffff:ffff");
    }

    public void _testMulticast(String addrStr) throws ParseException {
        InetAddress address = InetAddress.parse(addrStr);
        assertEquals("ff02::1", address.toString());
        assertTrue(address.isMulticastAddress());
        assertFalse(address.isBroadcastAddress());
        assertFalse(address.isWildcardAddress());
        assertFalse(address.isLoopbackAddress());
    }

    private void _testInet4Mapped(String addrStr) throws ParseException {
        _testRegular(addrStr, "::ffff:c0a8:1");
    }

    @Test
    public void testUnspecified() throws ParseException {
        _testUnspecified("0:0:0:0:0:0:0:0");
        _testUnspecified("0000:000:00:0:0:00:000:0000");
        _testUnspecified("::");
        _testUnspecified("0::");
        _testUnspecified("::0");
    }

    @Test
    public void testLoopback() throws ParseException {
       _testLoopback("0:0:0:0:0:0:0:1");
       _testLoopback("0000:000:00:0:0:00:000:0001");
       _testLoopback("::1");
       _testLoopback("0::1");
    }

    @Test
    public void test96Zeros() throws ParseException {
        _test96Zeros("2001:db8:0:0:0:0:0:0");
        _test96Zeros("2001:db8:00:0:0:00:000:0000");
        _test96Zeros("2001:db8::");
        _test96Zeros("2001:db8::0");
    }

    @Test
    public void test80Zeros() throws ParseException {
        _test80Zeros("2001:db8:0:0:0:0:0:1");
        _test80Zeros("2001:db8:00:0:0:00:000:0001");
        _test80Zeros("2001:db8::1");
        _test80Zeros("2001:db8::0:1");
    }

    @Test
    public void test32Zeros() throws ParseException {
        _test32Zeros("2001:db8:0:0:ffff:ffff:ffff:ffff");
        _test32Zeros("2001:db8:00:0:ffff:ffff:ffff:ffff");
        _test32Zeros("2001:db8::ffff:ffff:ffff:ffff");
        _test32Zeros("2001:db8::0000:ffff:ffff:ffff:ffff");
    }

    @Test
    public void test16Zeros() throws ParseException {
        _test16Zeros("2001:db8:0:ffff:ffff:ffff:ffff:ffff");
        _test16Zeros("2001:db8:00:ffff:ffff:ffff:ffff:ffff");
        _test16Zeros("2001:db8::ffff:ffff:ffff:ffff:ffff");
    }

    @Test
    public void testMulticast() throws ParseException {
        _testMulticast("ff02:0:0:0:0:0:0:1");
        _testMulticast("ff02:000:00:0:0:00:000:0001");
        _testMulticast("ff02::1");
        _testMulticast("ff02::0:1");
    }

    @Test
    public void testInet4Mapped() throws ParseException {
        _testInet4Mapped("::ffff:192.168.0.1");
        _testInet4Mapped("::ffff:192.168.000.001");
        _testInet4Mapped("0:0:0:0:0:ffff:192.168.0.1");
        _testInet4Mapped("0:0:0:0:0:ffff:192.168.000.001");
    }

    @Test
    public void testNetworkBroadcastAddresses() throws ParseException {
        InetAddress address = InetAddress.parse(
            "2001:db8:2345:6789:0123:4567:89ab:cdef");

        assertEquals("2001:db8:2345:6789::",
            address.getNetworkAddress(64).toString());
        assertEquals("2001:db8:2345:6789:ffff:ffff:ffff:ffff",
            address.getBroadcastAddress(64).toString());
        assertEquals("2001:db8:2345:6789:123:4567::",
            address.getNetworkAddress(96).toString());
        assertEquals("2001:db8:2345:6789:123:4567:ffff:ffff",
            address.getBroadcastAddress(96).toString());
        assertEquals("2001:db8:2345:6789:123:4567:89ab:0",
            address.getNetworkAddress(112).toString());
        assertEquals("2001:db8:2345:6789:123:4567:89ab:ffff",
            address.getBroadcastAddress(112).toString());
    }
}
