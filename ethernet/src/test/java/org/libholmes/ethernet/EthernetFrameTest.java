// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.ethernet;

import static org.junit.Assert.*;
import org.junit.Test;

import javax.json.JsonObject;

import org.libholmes.HexOctetReader;
import org.libholmes.HexOctetString;

public class EthernetFrameTest {
    @Test
    public void test() {
        HexOctetReader reader = new HexOctetReader(
            "00005e00530100005e00530208000102030405060708");
        EthernetFrame frame = EthernetFrame.parse(null, reader);
        assertEquals("00-00-5e-00-53-01", frame.getDstAddr().toString());
        assertEquals("00-00-5e-00-53-02", frame.getSrcAddr().toString());
        assertEquals(0x0800, frame.getEtherType());
        assertEquals(
            new HexOctetString("0102030405060708"),
            frame.getPayload());
    }

    @Test
    public void testJson() {
        HexOctetReader reader = new HexOctetReader(
            "00005e00530100005e00530208000102030405060708");
        EthernetFrame frame = EthernetFrame.parse(null, reader);
        JsonObject json = frame.toJson();
        assertEquals(json.getString("dstAddr"), "00-00-5e-00-53-01");
        assertEquals(json.getString("srcAddr"), "00-00-5e-00-53-02");
        assertEquals(json.getInt("etherType"), 0x0800);
        assertEquals(json.getString("payload"), "0102030405060708");
    }
}
