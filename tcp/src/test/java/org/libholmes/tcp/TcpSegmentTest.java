// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.tcp;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.List;
import javax.json.JsonObject;
import javax.json.JsonArray;

import org.libholmes.HexOctetReader;
import org.libholmes.ParseException;
import org.libholmes.inet.Inet4Datagram;

public class TcpSegmentTest {
    @Test
    public void testFull() throws ParseException {
        HexOctetReader reader = new HexOctetReader(
            "4500003c000040004006b7fcc0a80084" +
            "c0a800eb0050c3c2e89b7d7979d976b4" +
            "a01216a002b80000020405b40402080a" +
            "e765064c04829ef001030305");
        Inet4Datagram inetDatagram = Inet4Datagram.parse(null, reader);
        TcpSegment tcpSegment = TcpSegment.parse(inetDatagram,
            inetDatagram.getPayload());

        assertEquals("192.168.0.132",
            tcpSegment.getSrcAddr().toString());
        assertEquals("192.168.0.235",
            tcpSegment.getDstAddr().toString());
        assertEquals(80, tcpSegment.getSrcPort());
        assertEquals(50114, tcpSegment.getDstPort());
        assertEquals(3902504313L, tcpSegment.getSeq() & 0xffffffffL);
        assertEquals(2044294836L, tcpSegment.getAck() & 0xffffffffL);
        assertEquals(10, tcpSegment.getDataOffset());
        assertFalse(tcpSegment.getNsFlag());
        assertFalse(tcpSegment.getCwrFlag());
        assertFalse(tcpSegment.getEceFlag());
        assertFalse(tcpSegment.getUrgFlag());
        assertTrue(tcpSegment.getAckFlag());
        assertFalse(tcpSegment.getPshFlag());
        assertFalse(tcpSegment.getRstFlag());
        assertTrue(tcpSegment.getSynFlag());
        assertFalse(tcpSegment.getFinFlag());
        assertEquals(5792, tcpSegment.getWindowSize());
        assertEquals(0x02b8, tcpSegment.getRecordedChecksum());
        assertEquals(0x02b8, tcpSegment.getCalculatedChecksum());
        assertEquals(0x0000, tcpSegment.getUrgentPointer());
        assertEquals("", tcpSegment.getPadding().toString());

        List<TcpOption> options = tcpSegment.getOptions();
        assertEquals(5, options.size());

        assertTrue(options.get(0) instanceof TcpMaximumSegmentSizeOption);
        TcpMaximumSegmentSizeOption option0 =
            (TcpMaximumSegmentSizeOption) options.get(0);
        assertEquals(2, option0.getKind());
        assertEquals(4, option0.getLength());
        assertEquals(1460, option0.getMaximumSegmentSize());

        assertTrue(options.get(1) instanceof TcpSackPermittedOption);
        TcpSackPermittedOption option1 =
            (TcpSackPermittedOption) options.get(1);
        assertEquals(4, option1.getKind());
        assertEquals(2, option1.getLength());

        assertTrue(options.get(2) instanceof TcpTimestampsOption);
        TcpTimestampsOption option2 =
            (TcpTimestampsOption) options.get(2);
        assertEquals(8, option2.getKind());
        assertEquals(10, option2.getLength());
        assertEquals(0xe765064cL,
            option2.getTimestampValue() & 0xffffffffL);
        assertEquals(0x04829ef0L,
            option2.getTimestampEchoReply() & 0xffffffffL);

        assertTrue(options.get(3) instanceof TcpNoOperationOption);
        TcpNoOperationOption option3 =
            (TcpNoOperationOption) options.get(3);
        assertEquals(1, option3.getKind());

        assertTrue(options.get(4) instanceof TcpWindowScaleOption);
        TcpWindowScaleOption option4 =
            (TcpWindowScaleOption) options.get(4);
        assertEquals(3, option4.getKind());
        assertEquals(3, option4.getLength());
        assertEquals(5, option4.getShift());

        JsonObject jsonSegment = tcpSegment.toJson();
        assertEquals(80, jsonSegment.getInt("srcPort"));
        assertEquals(50114, jsonSegment.getInt("dstPort"));
        assertEquals(0xe89b7d79L,
            jsonSegment.getJsonNumber("seq").longValue());
        assertEquals(0x79d976b4L,
            jsonSegment.getJsonNumber("ack").longValue());
        assertEquals(10, jsonSegment.getInt("dataOffset"));
        assertFalse(jsonSegment.getBoolean("nsFlag"));
        assertFalse(jsonSegment.getBoolean("cwrFlag"));
        assertFalse(jsonSegment.getBoolean("eceFlag"));
        assertFalse(jsonSegment.getBoolean("urgFlag"));
        assertTrue(jsonSegment.getBoolean("ackFlag"));
        assertFalse(jsonSegment.getBoolean("pshFlag"));
        assertFalse(jsonSegment.getBoolean("rstFlag"));
        assertTrue(jsonSegment.getBoolean("synFlag"));
        assertFalse(jsonSegment.getBoolean("finFlag"));
        assertEquals(5792, jsonSegment.getInt("windowSize"));
        assertEquals(0x02b8, jsonSegment.getInt("recordedChecksum"));
        assertEquals(0x02b8, jsonSegment.getInt("calculatedChecksum"));
        assertEquals(0x0000, jsonSegment.getInt("urgentPointer"));

        JsonArray jsonOptions = jsonSegment.getJsonArray("options");
        assertEquals(5, jsonOptions.size());

        JsonObject jsonOption0 = jsonOptions.getJsonObject(0);
        assertEquals(2, jsonOption0.getInt("kind"));
        assertEquals(4, jsonOption0.getInt("length"));
        assertEquals(1460, jsonOption0.getInt("mss"));

        JsonObject jsonOption1 = jsonOptions.getJsonObject(1);
        assertEquals(4, jsonOption1.getInt("kind"));
        assertEquals(2, jsonOption1.getInt("length"));

        JsonObject jsonOption2 = jsonOptions.getJsonObject(2);
        assertEquals(8, jsonOption2.getInt("kind"));
        assertEquals(10, jsonOption2.getInt("length"));

        JsonObject jsonOption3 = jsonOptions.getJsonObject(3);
        assertEquals(1, jsonOption3.getInt("kind"));

        JsonObject jsonOption4 = jsonOptions.getJsonObject(4);
        assertEquals(3, jsonOption4.getInt("kind"));
        assertEquals(3, jsonOption4.getInt("length"));
        assertEquals(5, jsonOption4.getInt("shift"));
    }

    @Test
    public void testSrcPort() throws ParseException {
        HexOctetReader reader = new HexOctetReader(
            "4500003c000040004006b7fcc0a80084" +
            "c0a800ebc001c3c2e89b7d7979d976b4" +
            "a01216a002b80000020405b40402080a" +
            "e765064c04829ef001030305");
        Inet4Datagram inetDatagram = Inet4Datagram.parse(null, reader);
        TcpSegment tcpSegment = TcpSegment.parse(inetDatagram,
            inetDatagram.getPayload());

        assertEquals(49153, tcpSegment.getSrcPort());
        assertEquals(49153, tcpSegment.toJson().getInt("srcPort"));
    }

    @Test
    public void testDstPort() throws ParseException {
        HexOctetReader reader = new HexOctetReader(
            "4500003c000040004006b7fcc0a80084" +
            "c0a800eb00504001e89b7d7979d976b4" +
            "a01216a002b80000020405b40402080a" +
            "e765064c04829ef001030305");
        Inet4Datagram inetDatagram = Inet4Datagram.parse(null, reader);
        TcpSegment tcpSegment = TcpSegment.parse(inetDatagram,
            inetDatagram.getPayload());

        assertEquals(16385, tcpSegment.getDstPort());
        assertEquals(16385, tcpSegment.toJson().getInt("dstPort"));
    }

    @Test
    public void testSeq() throws ParseException {
        HexOctetReader reader = new HexOctetReader(
            "4500003c000040004006b7fcc0a80084" +
            "c0a800eb0050c3c21234567879d976b4" +
            "a01216a002b80000020405b40402080a" +
            "e765064c04829ef001030305");
        Inet4Datagram inetDatagram = Inet4Datagram.parse(null, reader);
        TcpSegment tcpSegment = TcpSegment.parse(inetDatagram,
            inetDatagram.getPayload());

        assertEquals(0x12345678L, tcpSegment.getSeq() & 0xffffffffL);
        assertEquals(0x12345678L,
            tcpSegment.toJson().getJsonNumber("seq").longValue());
    }

    @Test
    public void testAck() throws ParseException {
        HexOctetReader reader = new HexOctetReader(
            "4500003c000040004006b7fcc0a80084" +
            "c0a800eb0050c3c2e89b7d7912345678" +
            "a01216a002b80000020405b40402080a" +
            "e765064c04829ef001030305");
        Inet4Datagram inetDatagram = Inet4Datagram.parse(null, reader);
        TcpSegment tcpSegment = TcpSegment.parse(inetDatagram,
            inetDatagram.getPayload());

        assertEquals(0x12345678L, tcpSegment.getAck() & 0xffffffffL);
        assertEquals(0x12345678L,
            tcpSegment.toJson().getJsonNumber("ack").longValue());
    }

    @Test
    public void testDataOffset() throws ParseException {
        HexOctetReader reader = new HexOctetReader(
            "45000040000040004006b7fcc0a80084" +
            "c0a800eb0050c3c2e89b7d7979d976b4" +
            "b01216a002b80000020405b40402080a" +
            "e765064c04829ef00103030500000000");
        Inet4Datagram inetDatagram = Inet4Datagram.parse(null, reader);
        TcpSegment tcpSegment = TcpSegment.parse(inetDatagram,
            inetDatagram.getPayload());

        assertEquals(11, tcpSegment.getDataOffset());
        assertEquals(11, tcpSegment.toJson().getInt("dataOffset"));
    }

    @Test
    public void testNsFlag() throws ParseException {
        HexOctetReader reader0 = new HexOctetReader(
            "4500003c000040004006b7fcc0a80084" +
            "c0a800eb0050c3c2e89b7d7979d976b4" +
            "a01216a002b80000020405b40402080a" +
            "e765064c04829ef001030305");
        Inet4Datagram inetDatagram0 = Inet4Datagram.parse(null, reader0);
        TcpSegment tcpSegment0 = TcpSegment.parse(inetDatagram0,
            inetDatagram0.getPayload());

        HexOctetReader reader1 = new HexOctetReader(
            "4500003c000040004006b7fcc0a80084" +
            "c0a800eb0050c3c2e89b7d7979d976b4" +
            "a11216a002b80000020405b40402080a" +
            "e765064c04829ef001030305");
        Inet4Datagram inetDatagram1 = Inet4Datagram.parse(null, reader1);
        TcpSegment tcpSegment1 = TcpSegment.parse(inetDatagram1,
            inetDatagram1.getPayload());

        assertFalse(tcpSegment0.getNsFlag());
        assertTrue(tcpSegment1.getNsFlag());
        assertFalse(tcpSegment0.toJson().getBoolean("nsFlag"));
        assertTrue(tcpSegment1.toJson().getBoolean("nsFlag"));
    }

    @Test
    public void testCwrFlag() throws ParseException {
        HexOctetReader reader0 = new HexOctetReader(
            "4500003c000040004006b7fcc0a80084" +
            "c0a800eb0050c3c2e89b7d7979d976b4" +
            "a01216a002b80000020405b40402080a" +
            "e765064c04829ef001030305");
        Inet4Datagram inetDatagram0 = Inet4Datagram.parse(null, reader0);
        TcpSegment tcpSegment0 = TcpSegment.parse(inetDatagram0,
            inetDatagram0.getPayload());

        HexOctetReader reader1 = new HexOctetReader(
            "4500003c000040004006b7fcc0a80084" +
            "c0a800eb0050c3c2e89b7d7979d976b4" +
            "a09216a002b80000020405b40402080a" +
            "e765064c04829ef001030305");
        Inet4Datagram inetDatagram1 = Inet4Datagram.parse(null, reader1);
        TcpSegment tcpSegment1 = TcpSegment.parse(inetDatagram1,
            inetDatagram1.getPayload());

        assertFalse(tcpSegment0.getCwrFlag());
        assertTrue(tcpSegment1.getCwrFlag());
        assertFalse(tcpSegment0.toJson().getBoolean("cwrFlag"));
        assertTrue(tcpSegment1.toJson().getBoolean("cwrFlag"));
    }

    @Test
    public void testEceFlag() throws ParseException {
        HexOctetReader reader0 = new HexOctetReader(
            "4500003c000040004006b7fcc0a80084" +
            "c0a800eb0050c3c2e89b7d7979d976b4" +
            "a01216a002b80000020405b40402080a" +
            "e765064c04829ef001030305");
        Inet4Datagram inetDatagram0 = Inet4Datagram.parse(null, reader0);
        TcpSegment tcpSegment0 = TcpSegment.parse(inetDatagram0,
            inetDatagram0.getPayload());

        HexOctetReader reader1 = new HexOctetReader(
            "4500003c000040004006b7fcc0a80084" +
            "c0a800eb0050c3c2e89b7d7979d976b4" +
            "a05216a002b80000020405b40402080a" +
            "e765064c04829ef001030305");
        Inet4Datagram inetDatagram1 = Inet4Datagram.parse(null, reader1);
        TcpSegment tcpSegment1 = TcpSegment.parse(inetDatagram1,
            inetDatagram1.getPayload());

        assertFalse(tcpSegment0.getEceFlag());
        assertTrue(tcpSegment1.getEceFlag());
        assertFalse(tcpSegment0.toJson().getBoolean("eceFlag"));
        assertTrue(tcpSegment1.toJson().getBoolean("eceFlag"));
    }

    @Test
    public void testUrgFlag() throws ParseException {
        HexOctetReader reader0 = new HexOctetReader(
            "4500003c000040004006b7fcc0a80084" +
            "c0a800eb0050c3c2e89b7d7979d976b4" +
            "a01216a002b80000020405b40402080a" +
            "e765064c04829ef001030305");
        Inet4Datagram inetDatagram0 = Inet4Datagram.parse(null, reader0);
        TcpSegment tcpSegment0 = TcpSegment.parse(inetDatagram0,
            inetDatagram0.getPayload());

        HexOctetReader reader1 = new HexOctetReader(
            "4500003c000040004006b7fcc0a80084" +
            "c0a800eb0050c3c2e89b7d7979d976b4" +
            "a03216a002b80000020405b40402080a" +
            "e765064c04829ef001030305");
        Inet4Datagram inetDatagram1 = Inet4Datagram.parse(null, reader1);
        TcpSegment tcpSegment1 = TcpSegment.parse(inetDatagram1,
            inetDatagram1.getPayload());

        assertFalse(tcpSegment0.getUrgFlag());
        assertTrue(tcpSegment1.getUrgFlag());
        assertFalse(tcpSegment0.toJson().getBoolean("urgFlag"));
        assertTrue(tcpSegment1.toJson().getBoolean("urgFlag"));
    }

    @Test
    public void testAckFlag() throws ParseException {
        HexOctetReader reader0 = new HexOctetReader(
            "4500003c000040004006b7fcc0a80084" +
            "c0a800eb0050c3c2e89b7d7979d976b4" +
            "a00216a002b80000020405b40402080a" +
            "e765064c04829ef001030305");
        Inet4Datagram inetDatagram0 = Inet4Datagram.parse(null, reader0);
        TcpSegment tcpSegment0 = TcpSegment.parse(inetDatagram0,
            inetDatagram0.getPayload());

        HexOctetReader reader1 = new HexOctetReader(
            "4500003c000040004006b7fcc0a80084" +
            "c0a800eb0050c3c2e89b7d7979d976b4" +
            "a01216a002b80000020405b40402080a" +
            "e765064c04829ef001030305");
        Inet4Datagram inetDatagram1 = Inet4Datagram.parse(null, reader1);
        TcpSegment tcpSegment1 = TcpSegment.parse(inetDatagram1,
            inetDatagram1.getPayload());

        assertFalse(tcpSegment0.getAckFlag());
        assertTrue(tcpSegment1.getAckFlag());
        assertFalse(tcpSegment0.toJson().getBoolean("ackFlag"));
        assertTrue(tcpSegment1.toJson().getBoolean("ackFlag"));
    }

    @Test
    public void testPshFlag() throws ParseException {
        HexOctetReader reader0 = new HexOctetReader(
            "4500003c000040004006b7fcc0a80084" +
            "c0a800eb0050c3c2e89b7d7979d976b4" +
            "a01216a002b80000020405b40402080a" +
            "e765064c04829ef001030305");
        Inet4Datagram inetDatagram0 = Inet4Datagram.parse(null, reader0);
        TcpSegment tcpSegment0 = TcpSegment.parse(inetDatagram0,
            inetDatagram0.getPayload());

        HexOctetReader reader1 = new HexOctetReader(
            "4500003c000040004006b7fcc0a80084" +
            "c0a800eb0050c3c2e89b7d7979d976b4" +
            "a01a16a002b80000020405b40402080a" +
            "e765064c04829ef001030305");
        Inet4Datagram inetDatagram1 = Inet4Datagram.parse(null, reader1);
        TcpSegment tcpSegment1 = TcpSegment.parse(inetDatagram1,
            inetDatagram1.getPayload());

        assertFalse(tcpSegment0.getPshFlag());
        assertTrue(tcpSegment1.getPshFlag());
        assertFalse(tcpSegment0.toJson().getBoolean("pshFlag"));
        assertTrue(tcpSegment1.toJson().getBoolean("pshFlag"));
    }

    @Test
    public void testRstFlag() throws ParseException {
        HexOctetReader reader0 = new HexOctetReader(
            "4500003c000040004006b7fcc0a80084" +
            "c0a800eb0050c3c2e89b7d7979d976b4" +
            "a01216a002b80000020405b40402080a" +
            "e765064c04829ef001030305");
        Inet4Datagram inetDatagram0 = Inet4Datagram.parse(null, reader0);
        TcpSegment tcpSegment0 = TcpSegment.parse(inetDatagram0,
            inetDatagram0.getPayload());

        HexOctetReader reader1 = new HexOctetReader(
            "4500003c000040004006b7fcc0a80084" +
            "c0a800eb0050c3c2e89b7d7979d976b4" +
            "a01616a002b80000020405b40402080a" +
            "e765064c04829ef001030305");
        Inet4Datagram inetDatagram1 = Inet4Datagram.parse(null, reader1);
        TcpSegment tcpSegment1 = TcpSegment.parse(inetDatagram1,
            inetDatagram1.getPayload());

        assertFalse(tcpSegment0.getRstFlag());
        assertTrue(tcpSegment1.getRstFlag());
        assertFalse(tcpSegment0.toJson().getBoolean("rstFlag"));
        assertTrue(tcpSegment1.toJson().getBoolean("rstFlag"));
    }

    @Test
    public void testSynFlag() throws ParseException {
        HexOctetReader reader0 = new HexOctetReader(
            "4500003c000040004006b7fcc0a80084" +
            "c0a800eb0050c3c2e89b7d7979d976b4" +
            "a01016a002b80000020405b40402080a" +
            "e765064c04829ef001030305");
        Inet4Datagram inetDatagram0 = Inet4Datagram.parse(null, reader0);
        TcpSegment tcpSegment0 = TcpSegment.parse(inetDatagram0,
            inetDatagram0.getPayload());

        HexOctetReader reader1 = new HexOctetReader(
            "4500003c000040004006b7fcc0a80084" +
            "c0a800eb0050c3c2e89b7d7979d976b4" +
            "a01216a002b80000020405b40402080a" +
            "e765064c04829ef001030305");
        Inet4Datagram inetDatagram1 = Inet4Datagram.parse(null, reader1);
        TcpSegment tcpSegment1 = TcpSegment.parse(inetDatagram1,
            inetDatagram1.getPayload());

        assertFalse(tcpSegment0.getSynFlag());
        assertTrue(tcpSegment1.getSynFlag());
        assertFalse(tcpSegment0.toJson().getBoolean("synFlag"));
        assertTrue(tcpSegment1.toJson().getBoolean("synFlag"));
    }

    @Test
    public void testFinFlag() throws ParseException {
        HexOctetReader reader0 = new HexOctetReader(
            "4500003c000040004006b7fcc0a80084" +
            "c0a800eb0050c3c2e89b7d7979d976b4" +
            "a01216a002b80000020405b40402080a" +
            "e765064c04829ef001030305");
        Inet4Datagram inetDatagram0 = Inet4Datagram.parse(null, reader0);
        TcpSegment tcpSegment0 = TcpSegment.parse(inetDatagram0,
            inetDatagram0.getPayload());

        HexOctetReader reader1 = new HexOctetReader(
            "4500003c000040004006b7fcc0a80084" +
            "c0a800eb0050c3c2e89b7d7979d976b4" +
            "a01316a002b80000020405b40402080a" +
            "e765064c04829ef001030305");
        Inet4Datagram inetDatagram1 = Inet4Datagram.parse(null, reader1);
        TcpSegment tcpSegment1 = TcpSegment.parse(inetDatagram1,
            inetDatagram1.getPayload());

        assertFalse(tcpSegment0.getFinFlag());
        assertTrue(tcpSegment1.getFinFlag());
        assertFalse(tcpSegment0.toJson().getBoolean("finFlag"));
        assertTrue(tcpSegment1.toJson().getBoolean("finFlag"));
    }

    @Test
    public void testWindow() throws ParseException {
        HexOctetReader reader = new HexOctetReader(
            "4500003c000040004006b7fcc0a80084" +
            "c0a800eb0050c3c2e89b7d7979d976b4" +
            "a012cdef02b80000020405b40402080a" +
            "e765064c04829ef001030305");
        Inet4Datagram inetDatagram = Inet4Datagram.parse(null, reader);
        TcpSegment tcpSegment = TcpSegment.parse(inetDatagram,
            inetDatagram.getPayload());

        assertEquals(0xcdef, tcpSegment.getWindowSize());
        assertEquals(0xcdef, tcpSegment.toJson().getInt("windowSize"));
    }

    @Test
    public void testRecordedChecksum() throws ParseException {
        HexOctetReader reader = new HexOctetReader(
            "4500003c000040004006b7fcc0a80084" +
            "c0a800eb0050c3c2e89b7d7979d976b4" +
            "a01216a089ab0000020405b40402080a" +
            "e765064c04829ef001030305");
        Inet4Datagram inetDatagram = Inet4Datagram.parse(null, reader);
        TcpSegment tcpSegment = TcpSegment.parse(inetDatagram,
            inetDatagram.getPayload());

        assertEquals(0x89ab, tcpSegment.getRecordedChecksum());
        assertEquals(0x89ab,
            tcpSegment.toJson().getInt("recordedChecksum"));
    }

    @Test
    public void testCalculatedChecksum() throws ParseException {
        HexOctetReader reader = new HexOctetReader(
            "4500003c000040004006b7fcc0a80084" +
            "c0a800eb0050c3c2e89b7d7979d976b4" +
            "a01216a002b80000020405b40402080a" +
            "e765064c04829ef001030306");
        Inet4Datagram inetDatagram = Inet4Datagram.parse(null, reader);
        TcpSegment tcpSegment = TcpSegment.parse(inetDatagram,
            inetDatagram.getPayload());

        assertEquals(0x02b7, tcpSegment.getCalculatedChecksum());
        assertEquals(0x02b7,
            tcpSegment.toJson().getInt("calculatedChecksum"));
    }

    @Test
    public void testUrgentPointer() throws ParseException {
        HexOctetReader reader = new HexOctetReader(
            "4500003c000040004006b7fcc0a80084" +
            "c0a800eb0050c3c2e89b7d7979d976b4" +
            "a01216a002b8cdef020405b40402080a" +
            "e765064c04829ef001030305");
        Inet4Datagram inetDatagram = Inet4Datagram.parse(null, reader);
        TcpSegment tcpSegment = TcpSegment.parse(inetDatagram,
            inetDatagram.getPayload());

        assertEquals(0xcdef, tcpSegment.getUrgentPointer());
        assertEquals(0xcdef, tcpSegment.toJson().getInt("urgentPointer"));
    }

    @Test
    public void testPadding() throws ParseException {
        HexOctetReader reader = new HexOctetReader(
            "4500003c000040004006b7fcc0a80084" +
            "c0a800eb0050c3c2e89b7d7979d976b4" +
            "a01216a002b80000020405b40402080a" +
            "e765064c04829ef001001234");
        Inet4Datagram inetDatagram = Inet4Datagram.parse(null, reader);
        TcpSegment tcpSegment = TcpSegment.parse(inetDatagram,
            inetDatagram.getPayload());

        assertEquals("1234", tcpSegment.getPadding().toString());
        assertEquals("1234", tcpSegment.toJson().getString("padding"));
    }

    @Test
    public void testPayload() throws ParseException {
        HexOctetReader reader = new HexOctetReader(
            "45000040000040004006b7fcc0a80084" +
            "c0a800eb0050c3c2e89b7d7979d976b4" +
            "a01216a002b80000020405b40402080a" +
            "e765064c04829ef00103030512345678");
        Inet4Datagram inetDatagram = Inet4Datagram.parse(null, reader);
        TcpSegment tcpSegment = TcpSegment.parse(inetDatagram,
            inetDatagram.getPayload());

        assertEquals("12345678", tcpSegment.getPayload().toString());
        assertEquals("12345678", tcpSegment.toJson().getString("payload"));
    }
}
