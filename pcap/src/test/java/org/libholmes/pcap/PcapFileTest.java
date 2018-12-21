// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.pcap;

import static org.junit.Assert.*;
import org.junit.Test;

import org.libholmes.HexOctetReader;
import org.libholmes.HexOctetString;
import org.libholmes.ParseException;

public class PcapFileTest {
    @Test
    public void testLittleEndian() throws ParseException {
        HexOctetReader reader = new HexOctetReader(
            "d4c3b2a1020004000000000000000000dc05000001000000" +
            "007a495a0000000008000000080000000104091019243140" +
            "017a495a00000000070000000700000001040910192431" +
            "027a495a000000000600000006000000010409101924" +
            "037a495a0000000005000000050000000104091019");
        PcapFile pcap = PcapFile.parse(null, reader);

        assertEquals(pcap.getMagicNumber(), (int)0xa1b2c3d4L);
        assertEquals(pcap.getVersionMajor(), 2);
        assertEquals(pcap.getVersionMinor(), 4);
        assertEquals(pcap.getThisZone(), 0);
        assertEquals(pcap.getSigFigs(), 0);
        assertEquals(pcap.getSnapLen(), 1500);
        assertEquals(pcap.getNetworkType(), 1);

        assertTrue(pcap.hasRemaining());
        PcapPacket packet0 = pcap.readPacket();
        assertTrue(pcap.hasRemaining());
        PcapPacket packet1 = pcap.readPacket();
        assertTrue(pcap.hasRemaining());
        PcapPacket packet2 = pcap.readPacket();
        assertTrue(pcap.hasRemaining());
        PcapPacket packet3 = pcap.readPacket();
        assertFalse(pcap.hasRemaining());
        assertEquals(8, packet0.getCapturedLength());
        assertEquals(7, packet1.getCapturedLength());
        assertEquals(6, packet2.getCapturedLength());
        assertEquals(5, packet3.getCapturedLength());
        assertEquals(
            new HexOctetString("0104091019243140"),
            packet0.getPayload());
        assertEquals(
            new HexOctetString("01040910192431"),
            packet1.getPayload());
        assertEquals(
            new HexOctetString("010409101924"),
            packet2.getPayload());
        assertEquals(
            new HexOctetString("0104091019"),
            packet3.getPayload());
        assertEquals(0, reader.remaining());
  }

    @Test
    public void testBigEndian() throws ParseException {
        HexOctetReader reader = new HexOctetReader(
            "a1b2c3d4000200040000000000000000000005dc00000001" +
            "5a497a000000000000000008000000080104091019243140" +
            "5a497a0100000000000000070000000701040910192431" +
            "5a497a02000000000000000600000006010409101924" +
            "5a497a030000000000000005000000050104091019");
        PcapFile pcap = PcapFile.parse(null, reader);

        assertEquals(pcap.getMagicNumber(), (int)0xa1b2c3d4L);
        assertEquals(pcap.getVersionMajor(), 2);
        assertEquals(pcap.getVersionMinor(), 4);
        assertEquals(pcap.getThisZone(), 0);
        assertEquals(pcap.getSigFigs(), 0);
        assertEquals(pcap.getSnapLen(), 1500);
        assertEquals(pcap.getNetworkType(), 1);

        assertTrue(pcap.hasRemaining());
        PcapPacket packet0 = pcap.readPacket();
        assertTrue(pcap.hasRemaining());
        PcapPacket packet1 = pcap.readPacket();
        assertTrue(pcap.hasRemaining());
        PcapPacket packet2 = pcap.readPacket();
        assertTrue(pcap.hasRemaining());
        PcapPacket packet3 = pcap.readPacket();
        assertFalse(pcap.hasRemaining());
        assertEquals(8, packet0.getCapturedLength());
        assertEquals(7, packet1.getCapturedLength());
        assertEquals(6, packet2.getCapturedLength());
        assertEquals(5, packet3.getCapturedLength());
        assertEquals(
            new HexOctetString("0104091019243140"),
            packet0.getPayload());
        assertEquals(
            new HexOctetString("01040910192431"),
            packet1.getPayload());
        assertEquals(
            new HexOctetString("010409101924"),
            packet2.getPayload());
        assertEquals(
            new HexOctetString("0104091019"),
            packet3.getPayload());
        assertEquals(0, reader.remaining());
    }
}
