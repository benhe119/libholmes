// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.pcap;

import static org.junit.Assert.*;
import org.junit.Test;

import org.libholmes.HexOctetReader;
import org.libholmes.HexOctetString;

public class PcapPacketTest {
    private static final String content = new String(
        "000102030405060708090a0b0c0d0e0f" +
        "101112131415161718191a1b1c1d1e1f" +
        "202122232425262728292a2b2c2d2e2f" +
        "303132233435363738393a3b3c3d3e3f" +
        "40414243");

    @Test
    public void testLittleEndian() {
        HexOctetReader reader = new HexOctetReader(
            "<ff79495a3f420f0044000000dc050000" + content);
        PcapPacket packet = PcapPacket.parse(null, reader);
        assertEquals(1514764799, packet.getTsSec());
        assertEquals(999999, packet.getTsUsec());
        assertEquals(68, packet.getCapturedLength());
        assertEquals(1500, packet.getOriginalLength());
        assertEquals(new HexOctetString(content), packet.getPayload());
    }

    @Test
    public void testBigEndian() {
        HexOctetReader reader = new HexOctetReader(
            ">5a4979ff000f423f00000044000005dc" + content);
        PcapPacket packet = PcapPacket.parse(null, reader);
        assertEquals(1514764799, packet.getTsSec());
        assertEquals(999999, packet.getTsUsec());
        assertEquals(68, packet.getCapturedLength());
        assertEquals(1500, packet.getOriginalLength());
        assertEquals(new HexOctetString(content), packet.getPayload());
    }
}
