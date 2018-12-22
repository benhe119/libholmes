// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.ethernet;

import static org.junit.Assert.*;
import org.junit.Test;

import org.libholmes.HexOctetReader;
import org.libholmes.HexOctetString;
import org.libholmes.ParseException;

public class EthernetAddressTest {
    @Test
    public void testGetAddress() throws ParseException {
        EthernetAddress mac = EthernetAddress.parse("00-00-5e-00-53-01");
        assertEquals(new HexOctetString("00005e005301"), mac.getAddress());
    }

    @Test
    public void testLength() throws ParseException {
        EthernetAddress mac = EthernetAddress.parse("00-00-5e-00-53-01");
        assertEquals(6, mac.length());
    }

    @Test
    public void testUnicast() throws ParseException {
        EthernetAddress address = EthernetAddress.parse("00-00-5e-00-53-01");
        assertFalse(address.isMulticastAddress());
        assertFalse(address.isBroadcastAddress());
        assertFalse(address.isWildcardAddress());
        assertFalse(address.isLoopbackAddress());
    }

    @Test
    public void testMulticast() throws ParseException {
        EthernetAddress address = EthernetAddress.parse("01-00-5e-00-53-02");
        assertTrue(address.isMulticastAddress());
        assertFalse(address.isBroadcastAddress());
        assertFalse(address.isWildcardAddress());
        assertFalse(address.isLoopbackAddress());
    }

    @Test
    public void testBroadcast() throws ParseException {
        EthernetAddress address = EthernetAddress.parse("ff-ff-ff-ff-ff-ff");
        assertTrue(address.isMulticastAddress());
        assertTrue(address.isBroadcastAddress());
        assertFalse(address.isWildcardAddress());
        assertFalse(address.isLoopbackAddress());
    }

    @Test
    public void testWildcard() throws ParseException {
        EthernetAddress address = EthernetAddress.parse("00-00-00-00-00-00");
        assertFalse(address.isMulticastAddress());
        assertFalse(address.isBroadcastAddress());
        assertTrue(address.isWildcardAddress());
        assertFalse(address.isLoopbackAddress());
    }

    @Test
    public void testIsLocalGlobal() throws ParseException {
        EthernetAddress global =
            EthernetAddress.parse("00-00-5e-00-53-01");
        EthernetAddress local =
            EthernetAddress.parse("02-ff-55-aa-66-99");
        assertFalse(global.isLocal());
        assertTrue(global.isGlobal());
        assertTrue(local.isLocal());
        assertFalse(local.isGlobal());
    }

    @Test
    public void testEquals() throws ParseException {
        assertTrue(
            EthernetAddress.parse("00-00-5e-00-53-01").equals(
            EthernetAddress.parse("00-00-5e-00-53-01")));
        assertFalse(
            EthernetAddress.parse("00-00-5e-00-53-01").equals(
            EthernetAddress.parse("01-00-5e-00-53-01")));
        assertFalse(
            EthernetAddress.parse("00-00-5e-00-53-01").equals(
            EthernetAddress.parse("00-01-5e-00-53-01")));
        assertFalse(
            EthernetAddress.parse("00-00-5e-00-53-01").equals(
            EthernetAddress.parse("00-00-5f-00-53-01")));
        assertFalse(
            EthernetAddress.parse("00-00-5e-00-53-01").equals(
            EthernetAddress.parse("00-00-5e-01-53-01")));
        assertFalse(
            EthernetAddress.parse("00-00-5e-00-53-01").equals(
            EthernetAddress.parse("00-00-5e-00-54-01")));
        assertFalse(
            EthernetAddress.parse("00-00-5e-00-53-01").equals(
            EthernetAddress.parse("00-00-5e-00-53-02")));
    }

    @Test
    public void testHashCode() throws ParseException {
        assertEquals(
            EthernetAddress.parse("00-00-00-00-00-00").hashCode(),
            EthernetAddress.parse("00-00-00-00-00-00").hashCode());
        assertEquals(
            EthernetAddress.parse("00-00-5e-00-53-01").hashCode(),
            EthernetAddress.parse("00-00-5e-00-53-01").hashCode());
        assertEquals(
            EthernetAddress.parse("ff-ff-ff-ff-ff-ff").hashCode(),
            EthernetAddress.parse("ff-ff-ff-ff-ff-ff").hashCode());
    }

    @Test
    public void testToString() throws ParseException {
        assertEquals(
            EthernetAddress.parse("00-00-00-00-00-00").toString(),
            "00-00-00-00-00-00");
        assertEquals(
            EthernetAddress.parse("00-00-5e-00-53-01").toString(),
            "00-00-5e-00-53-01");
        assertEquals(
            EthernetAddress.parse("ff-ff-ff-ff-ff-ff").toString(),
            "ff-ff-ff-ff-ff-ff");
    }
}
