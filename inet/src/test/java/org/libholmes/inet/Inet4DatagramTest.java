// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.inet;

import static org.junit.Assert.*;
import org.junit.Test;

import javax.json.JsonObject;
import javax.json.JsonArray;

import org.libholmes.HexOctetReader;
import org.libholmes.ArrayOctetReader;
import org.libholmes.ParseException;

public class Inet4DatagramTest {
    @Test
    public void testIhl() throws ParseException {
        Inet4Datagram datagram0 = Inet4Datagram.parse(null,
            new HexOctetReader(
                "4500001cc9b4000040012ef0c0a80001" +
                "c0a800eb0000d63129cd0001"));
        Inet4Datagram datagram1 = Inet4Datagram.parse(null,
            new HexOctetReader(
                "4600001cc9b40000ff012ef0c0a80001" +
                "c0a800eb0000d63129cd000100000000"));
        assertEquals(5, datagram0.getIhl());
        assertEquals(6, datagram1.getIhl());
        JsonObject json0 = datagram0.toJson();
        JsonObject json1 = datagram1.toJson();
        assertEquals(5, json0.getInt("ihl"));
        assertEquals(6, json1.getInt("ihl"));
    }

    @Test
    public void testTos() throws ParseException {
        Inet4Datagram datagram0 = Inet4Datagram.parse(null,
            new HexOctetReader(
                "4500001cc9b4000040012ef0c0a80001" +
                "c0a800eb0000d63129cd0001"));
        Inet4Datagram datagram1 = Inet4Datagram.parse(null,
            new HexOctetReader(
                "45ff001cc9b40000ff012ef0c0a80001" +
                "c0a800eb0000d63129cd0001"));
        assertEquals(0x00, datagram0.getTos());
        assertEquals(0xff, datagram1.getTos());
        JsonObject json0 = datagram0.toJson();
        JsonObject json1 = datagram1.toJson();
        assertEquals(0x00, json0.getInt("tos"));
        assertEquals(0xff, json1.getInt("tos"));
    }

    @Test
    public void testLength() throws ParseException {
        HexOctetReader raw0 = new HexOctetReader(
            "4500001cc9b4000040012ef0c0a80001" +
            "c0a800eb0000d63129cd000100000000");
        Inet4Datagram datagram0 = Inet4Datagram.parse(null, raw0);
        assertEquals(4, raw0.remaining());
        byte[] raw1 = new byte[0xffff];
        HexOctetReader content1 = new HexOctetReader(
            "4500ffffc9b40000ff012ef0c0a80001" +
            "c0a800eb0000d63129cd0001");
        for (int i = 0; i != 28; ++i) {
            raw1[i] = content1.readByte();
        }
        Inet4Datagram datagram1 = Inet4Datagram.parse(null,
            new ArrayOctetReader(raw1, ArrayOctetReader.BIG_ENDIAN));
        assertEquals(28, datagram0.getLength());
        assertEquals(0xffff, datagram1.getLength());
        JsonObject json0 = datagram0.toJson();
        JsonObject json1 = datagram1.toJson();
        assertEquals(28, json0.getInt("length"));
        assertEquals(0xffff, json1.getInt("length"));
    }

    @Test
    public void testId() throws ParseException {
        Inet4Datagram datagram0 = Inet4Datagram.parse(null,
            new HexOctetReader(
                "4500001cc9b4000040012ef0c0a80001" +
                "c0a800eb0000d63129cd0001"));
        Inet4Datagram datagram1 = Inet4Datagram.parse(null,
            new HexOctetReader(
                "4500001cffff0000ff012ef0c0a80001" +
                "c0a800eb0000d63129cd0001"));
        assertEquals(0xc9b4, datagram0.getId());
        assertEquals(0xffff, datagram1.getId());
        JsonObject json0 = datagram0.toJson();
        JsonObject json1 = datagram1.toJson();
        assertEquals(0xc9b4, json0.getInt("id"));
        assertEquals(0xffff, json1.getInt("id"));
    }

    @Test
    public void testEvil() throws ParseException {
        Inet4Datagram datagram0 = Inet4Datagram.parse(null,
            new HexOctetReader(
                "4500001cc9b4000040012ef0c0a80001" +
                "c0a800eb0000d63129cd0001"));
        Inet4Datagram datagram1 = Inet4Datagram.parse(null,
            new HexOctetReader(
                "4500001cc9b4800040012ef0c0a80001" +
                "c0a800eb0000d63129cd0001"));
        assertFalse(datagram0.isEvil());
        assertTrue(datagram1.isEvil());
        JsonObject json0 = datagram0.toJson();
        JsonObject json1 = datagram1.toJson();
        assertFalse(json0.getBoolean("evil"));
        assertTrue(json1.getBoolean("evil"));
    }

    @Test
    public void testDoNotFragment() throws ParseException {
        Inet4Datagram datagram0 = Inet4Datagram.parse(null,
            new HexOctetReader(
                "4500001cc9b4000040012ef0c0a80001" +
                "c0a800eb0000d63129cd0001"));
        Inet4Datagram datagram1 = Inet4Datagram.parse(null,
            new HexOctetReader(
                "4500001cc9b4400040012ef0c0a80001" +
                "c0a800eb0000d63129cd0001"));
        assertFalse(datagram0.doNotFragment());
        assertTrue(datagram1.doNotFragment());
        JsonObject json0 = datagram0.toJson();
        JsonObject json1 = datagram1.toJson();
        assertFalse(json0.getBoolean("doNotFragment"));
        assertTrue(json1.getBoolean("doNotFragment"));
    }

    @Test
    public void testHasMoreFragments() throws ParseException {
        Inet4Datagram datagram0 = Inet4Datagram.parse(null,
            new HexOctetReader(
                "4500001cc9b4000040012ef0c0a80001" +
                "c0a800eb0000d63129cd0001"));
        Inet4Datagram datagram1 = Inet4Datagram.parse(null,
            new HexOctetReader(
                "4500001cc9b4200040012ef0c0a80001" +
                "c0a800eb0000d63129cd0001"));
        assertFalse(datagram0.hasMoreFragments());
        assertTrue(datagram1.hasMoreFragments());
        JsonObject json0 = datagram0.toJson();
        JsonObject json1 = datagram1.toJson();
        assertFalse(json0.getBoolean("moreFragments"));
        assertTrue(json1.getBoolean("moreFragments"));
    }

    @Test
    public void testFragmentOffset() throws ParseException {
        Inet4Datagram datagram0 = Inet4Datagram.parse(null,
            new HexOctetReader(
                "4500001cc9b4000040012ef0c0a80001" +
                "c0a800eb0000d63129cd0001"));
        Inet4Datagram datagram1 = Inet4Datagram.parse(null,
            new HexOctetReader(
                "4500001cc9b41fff40012ef0c0a80001" +
                "c0a800eb0000d63129cd0001"));
        assertEquals(0x0000, datagram0.getFragmentOffset());
        assertEquals(0x1fff, datagram1.getFragmentOffset());
        JsonObject json0 = datagram0.toJson();
        JsonObject json1 = datagram1.toJson();
        assertEquals(0x0000, json0.getInt("fragmentOffset"));
        assertEquals(0x1fff, json1.getInt("fragmentOffset"));
    }

    @Test
    public void testTtl() throws ParseException {
        Inet4Datagram datagram0 = Inet4Datagram.parse(null,
            new HexOctetReader(
                "4500001cc9b4000040012ef0c0a80001" +
                "c0a800eb0000d63129cd0001"));
        Inet4Datagram datagram1 = Inet4Datagram.parse(null,
            new HexOctetReader(
                "4500001cc9b40000ff012ef0c0a80001" +
                "c0a800eb0000d63129cd0001"));
        assertEquals(0x40, datagram0.getTtl());
        assertEquals(0xff, datagram1.getTtl());
        JsonObject json0 = datagram0.toJson();
        JsonObject json1 = datagram1.toJson();
        assertEquals(0x40, json0.getInt("ttl"));
        assertEquals(0xff, json1.getInt("ttl"));
    }

    @Test
    public void testProtocol() throws ParseException {
        Inet4Datagram datagram0 = Inet4Datagram.parse(null,
            new HexOctetReader(
                "4500001cc9b4000040012ef0c0a80001" +
                "c0a800eb0000d63129cd0001"));
        Inet4Datagram datagram1 = Inet4Datagram.parse(null,
            new HexOctetReader(
                "4500001cc9b4000040ff2ef0c0a80001" +
                "c0a800eb0000d63129cd0001"));
        assertEquals(0x01, datagram0.getProtocol());
        assertEquals(0xff, datagram1.getProtocol());
        JsonObject json0 = datagram0.toJson();
        JsonObject json1 = datagram1.toJson();
        assertEquals(0x01, json0.getInt("protocol"));
        assertEquals(0xff, json1.getInt("protocol"));
    }

    @Test
    public void testChecksum() throws ParseException {
        Inet4Datagram datagram0 = Inet4Datagram.parse(null,
            new HexOctetReader(
                "4500001cc9b4000040012ef0c0a80001" +
                "c0a800eb0000d63129cd0001"));
        Inet4Datagram datagram1 = Inet4Datagram.parse(null,
            new HexOctetReader(
                "4500001cc9b400004001ffffc0a80001" +
                "c0a800eb0000d63129cd0001"));
        assertEquals(0x2ef0, datagram0.getRecordedChecksum());
        assertEquals(0x2ef0, datagram0.getCalculatedChecksum());
        assertEquals(0xffff, datagram1.getRecordedChecksum());
        assertEquals(0x2ef0, datagram1.getCalculatedChecksum());
        JsonObject json0 = datagram0.toJson();
        JsonObject json1 = datagram1.toJson();
        assertEquals(0x2ef0, json0.getInt("recordedChecksum"));
        assertEquals(0x2ef0, json0.getInt("calculatedChecksum"));
        assertEquals(0xffff, json1.getInt("recordedChecksum"));
        assertEquals(0x2ef0, json1.getInt("calculatedChecksum"));
    }

    @Test
    public void testSrcAddr() throws ParseException {
        Inet4Datagram datagram0 = Inet4Datagram.parse(null,
            new HexOctetReader(
                "4500001cc9b4000040012ef0c0a80001" +
                "c0a800eb0000d63129cd0001"));
        Inet4Datagram datagram1 = Inet4Datagram.parse(null,
            new HexOctetReader(
                "4500001cc9b4000040012ef1ffffffff" +
                "c0a800eb0000d63129cd0001"));
        assertEquals("192.168.0.1",
            datagram0.getSrcAddr().toString());
        assertEquals("255.255.255.255",
            datagram1.getSrcAddr().toString());
        JsonObject json0 = datagram0.toJson();
        JsonObject json1 = datagram1.toJson();
        assertEquals("192.168.0.1", json0.getString("srcAddr"));
        assertEquals("255.255.255.255", json1.getString("srcAddr"));
    }

    @Test
    public void testDstAddr() throws ParseException {
        Inet4Datagram datagram0 = Inet4Datagram.parse(null,
            new HexOctetReader(
                "4500001cc9b4000040012ef0c0a80001" +
                "c0a800eb0000d63129cd0001"));
        Inet4Datagram datagram1 = Inet4Datagram.parse(null,
            new HexOctetReader(
                "4500001cc9b4000040012ef1c0a80001" +
                "ffffffff0000d63129cd0001"));
        assertEquals("192.168.0.235",
            datagram0.getDstAddr().toString());
        assertEquals("255.255.255.255",
            datagram1.getDstAddr().toString());
        JsonObject json0 = datagram0.toJson();
        JsonObject json1 = datagram1.toJson();
        assertEquals("192.168.0.235", json0.getString("dstAddr"));
        assertEquals("255.255.255.255", json1.getString("dstAddr"));
    }

    @Test
    public void testOptions() throws ParseException {
        Inet4Datagram datagram0 = Inet4Datagram.parse(null,
            new HexOctetReader(
                "4500001cc9b4000040012ef0c0a80001" +
                "c0a800eb0000d63129cd0001"));
        Inet4Datagram datagram1 = Inet4Datagram.parse(null,
            new HexOctetReader(
                "47000024c9b4000040012ef1c0a80001" +
                "c0a800ebff050102030000000000d631" +
                "29cd0001"));
        assertEquals(0, datagram0.getOptions().size());
        assertEquals(2, datagram1.getOptions().size());
        assertTrue(datagram1.getOptions().get(0) instanceof
            Inet4UnrecognisedOption);
        assertTrue(datagram1.getOptions().get(1) instanceof
            Inet4EndOfOptionsList);
        JsonArray json0 = datagram0.toJson().getJsonArray("options");
        JsonArray json1 = datagram1.toJson().getJsonArray("options");
        assertEquals(0, json0.size());
        assertEquals(2, json1.size());
        assertEquals(0xff, json1.getJsonObject(0).getInt("type"));
        assertEquals(0x00, json1.getJsonObject(1).getInt("type"));
    }

    @Test
    public void testPadding() throws ParseException {
        Inet4Datagram datagram0 = Inet4Datagram.parse(null,
            new HexOctetReader(
                "4500001cc9b4000040012ef0c0a80001" +
                "c0a800eb0000d63129cd0001"));
        Inet4Datagram datagram1 = Inet4Datagram.parse(null,
            new HexOctetReader(
                "47000024c9b4000040012ef1c0a80001" +
                "c0a800ebff050000000001020000d631" +
                "29cd0001"));
        assertEquals("", datagram0.getPadding().toString());
        assertEquals("0102", datagram1.getPadding().toString());
        JsonObject json0 = datagram0.toJson();
        JsonObject json1 = datagram1.toJson();
        assertEquals("", json0.getString("padding"));
        assertEquals("0102", json1.getString("padding"));
    }

    @Test
    public void testPayload() throws ParseException {
        Inet4Datagram datagram0 = Inet4Datagram.parse(null,
            new HexOctetReader(
                "4500001cc9b4000040012ef0c0a80001" +
                "c0a800eb0000d63129cd0001"));
        byte[] raw1 = new byte[0xffff];
        HexOctetReader content1 = new HexOctetReader(
            "4500ffffc9b40000ff012ef0c0a80001" +
            "c0a800eb0000d63129cd0001");
        for (int i = 0; i != 28; ++i) {
            raw1[i] = content1.readByte();
        }
        Inet4Datagram datagram1 = Inet4Datagram.parse(null,
            new ArrayOctetReader(raw1, ArrayOctetReader.BIG_ENDIAN));
        assertEquals("0000d63129cd0001",
            datagram0.getPayload().toString());
        assertEquals(65515, datagram1.getPayload().length());
    }

    @Test
    public void testFull() throws ParseException {
        HexOctetReader reader = new HexOctetReader(
            "450000547eec000040017980c0a80001" +
            "c0a800eb000013e32846000120a54b5a" +
            "00000000920307000000000010111213" +
            "1415161718191a1b1c1d1e1f20212223" +
            "2425262728292a2b2c2d2e2f30313233" +
            "34353637");
        Inet4Datagram datagram = Inet4Datagram.parse(null, reader);
        assertEquals(4, datagram.getVersion());
        assertEquals(5, datagram.getIhl());
        assertEquals(0, datagram.getTos());
        assertEquals(84, datagram.getLength());
        assertEquals(0x7eec, datagram.getId());
        assertFalse(datagram.isEvil());
        assertFalse(datagram.doNotFragment());
        assertFalse(datagram.hasMoreFragments());
        assertEquals(0, datagram.getFragmentOffset());
        assertEquals(64, datagram.getTtl());
        assertEquals(1, datagram.getProtocol());
        assertEquals(0x7980, datagram.getRecordedChecksum());
        assertEquals(0x7980, datagram.getCalculatedChecksum());
        assertEquals("192.168.0.1",
            datagram.getSrcAddr().toString());
        assertEquals("192.168.0.235",
            datagram.getDstAddr().toString());
        assertEquals(0, datagram.getOptions().size());
        assertEquals("", datagram.getPadding().toString());
        assertEquals(
            "000013e32846000120a54b5a00000000" +
            "92030700000000001011121314151617" +
            "18191a1b1c1d1e1f2021222324252627" +
            "28292a2b2c2d2e2f3031323334353637",
            datagram.getPayload().toString());

        JsonObject json = datagram.toJson();
        assertEquals(4, json.getInt("version"));
        assertEquals(5, json.getInt("ihl"));
        assertEquals(0, json.getInt("tos"));
        assertEquals(84, json.getInt("length"));
        assertEquals(0x7eec, json.getInt("id"), 0xc9b4);
        assertFalse(json.getBoolean("evil"));
        assertFalse(json.getBoolean("doNotFragment"));
        assertFalse(json.getBoolean("moreFragments"));
        assertEquals(0, json.getInt("fragmentOffset"));
        assertEquals(64, json.getInt("ttl"));
        assertEquals(1, json.getInt("protocol"));
        assertEquals(0x7980, json.getInt("recordedChecksum"));
        assertEquals(0x7980, json.getInt("calculatedChecksum"));
        assertEquals("192.168.0.1", json.getString("srcAddr"));
        assertEquals("192.168.0.235", json.getString("dstAddr"));
        assertEquals("", json.getString("padding"));
        assertEquals(
            "000013e32846000120a54b5a00000000" +
            "92030700000000001011121314151617" +
            "18191a1b1c1d1e1f2021222324252627" +
            "28292a2b2c2d2e2f3031323334353637",
            json.getString("payload"));
    }

    @Test
    public void testDuplicate() throws ParseException {
        Inet4Datagram datagram0 = Inet4Datagram.parse(null,
            new HexOctetReader(
                "45000028303900004006c943c0a80002" +
                "c0a80001001400500000000000000000" +
                "500220000e2b0000"));

        // Modify the TOS, evil, DF and TTL fields
        // (and consequently also the checksum):
        // should still qualify as a duplicate.
        Inet4Datagram datagram1 = Inet4Datagram.parse(null,
            new HexOctetReader(
                "454800283039c0003f0609fbc0a80002" +
                "c0a80001001400500000000000000000" +
                "500220000e2b0000"));
        assertTrue(datagram1.isDuplicate(datagram0));
        assertTrue(datagram0.isDuplicate(datagram1));

        // Modify the identification field:
        // not now a duplicate.
        // Set the MF flag: not now a duplicate.
        Inet4Datagram datagram2 = Inet4Datagram.parse(null,
            new HexOctetReader(
                "45000028303a00004006c942c0a80002" +
                "c0a80001001400500000000000000000" +
                "500220000e2b0000"));
        assertFalse(datagram2.isDuplicate(datagram0));
        assertFalse(datagram0.isDuplicate(datagram2));

        // Set the MF flag: not now a duplicate.
        Inet4Datagram datagram3 = Inet4Datagram.parse(null,
            new HexOctetReader(
                "45000028303920004006a943c0a80002" +
                "c0a80001001400500000000000000000" +
                "500220000e2b0000"));
        assertFalse(datagram3.isDuplicate(datagram0));
        assertFalse(datagram0.isDuplicate(datagram3));

        // Change the fragment offset: not now a duplicate.
        Inet4Datagram datagram4 = Inet4Datagram.parse(null,
            new HexOctetReader(
                "45000028303900014006c942c0a80002" +
                "c0a80001001400500000000000000000" +
                "500220000e2b0000"));
        assertFalse(datagram4.isDuplicate(datagram0));
        assertFalse(datagram0.isDuplicate(datagram4));

        // Change the protocol: not now a duplicate.
        Inet4Datagram datagram5 = Inet4Datagram.parse(null,
            new HexOctetReader(
                "45000028303900004007c942c0a80002" +
                "c0a80001001400500000000000000000" +
                "500220000e2a0000"));
        assertFalse(datagram5.isDuplicate(datagram0));
        assertFalse(datagram0.isDuplicate(datagram5));

        // Change the source address: not now a duplicate.
        Inet4Datagram datagram6 = Inet4Datagram.parse(null,
            new HexOctetReader(
                "45000028303900004006c942c0a80003" +
                "c0a80001001400500000000000000000" +
                "500220000e2a0000"));
        assertFalse(datagram6.isDuplicate(datagram0));
        assertFalse(datagram0.isDuplicate(datagram6));

        // Change the destination address: not now a duplicate.
        Inet4Datagram datagram7 = Inet4Datagram.parse(null,
            new HexOctetReader(
                "45000028303900004006c941c0a80002" +
                "c0a80003001400500000000000000000" +
                "500220000e290000"));
        assertFalse(datagram7.isDuplicate(datagram0));
        assertFalse(datagram0.isDuplicate(datagram7));

        // Change the payload: not now a duplicate.
        Inet4Datagram datagram8 = Inet4Datagram.parse(null,
            new HexOctetReader(
                "45000028303900004006c943c0a80002" +
                "c0a80001001400500000000000000000" +
                "500420000e290000"));
        assertFalse(datagram8.isDuplicate(datagram0));
        assertFalse(datagram0.isDuplicate(datagram8));
    }
}
