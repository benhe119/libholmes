// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.udp;

import static org.junit.Assert.*;
import org.junit.Test;

import javax.json.JsonObject;

import org.libholmes.HexOctetReader;
import org.libholmes.ParseException;
import org.libholmes.inet.Inet4Datagram;

public class UdpDatagramTest {
    @Test
    public void testFull() throws ParseException {
        HexOctetReader reader = new HexOctetReader(
            "45000020f99340004011be75c0a80088" +
            "c0a800ebb60d3039000caef174657374" +
            "0000000000000000000000000000");
        Inet4Datagram inetDatagram = Inet4Datagram.parse(null, reader);
        UdpDatagram udpDatagram = UdpDatagram.parse(inetDatagram,
            inetDatagram.getPayload());

        assertEquals("192.168.0.136",
            udpDatagram.getSrcAddr().toString());
        assertEquals("192.168.0.235",
            udpDatagram.getDstAddr().toString());
        assertEquals(0xb60d, udpDatagram.getSrcPort());
        assertEquals(12345, udpDatagram.getDstPort());
        assertEquals(12, udpDatagram.getLength());
        assertEquals(0xaef1, udpDatagram.getRecordedChecksum());
        assertEquals(0xaef1, udpDatagram.getCalculatedChecksum());
        assertEquals("74657374", udpDatagram.getPayload().toString());

        JsonObject udpJson = udpDatagram.toJson();
        assertEquals(0xb60d, udpJson.getInt("srcPort"));
        assertEquals(12345, udpJson.getInt("dstPort"));
        assertEquals(12, udpJson.getInt("length"));
        assertEquals(0xaef1, udpJson.getInt("recordedChecksum"));
        assertEquals(0xaef1, udpJson.getInt("calculatedChecksum"));
        assertEquals("74657374", udpJson.getString("payload"));
    }
}
