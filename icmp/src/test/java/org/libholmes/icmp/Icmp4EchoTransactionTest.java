// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.icmp;

import static org.junit.Assert.*;
import org.junit.Test;

import javax.json.JsonObject;

import org.libholmes.HexOctetReader;
import org.libholmes.ParseException;
import org.libholmes.inet.Inet4Datagram;

public class Icmp4EchoTransactionTest {
    @Test
    public void test() throws ParseException {
        Inet4Datagram request0Datagram =
            Inet4Datagram.parse(null, new HexOctetReader(
                "4500005460a14000400157bac0a800d1" +
                "c0a8002c0800666269770001161a4c5c" +
                "00000000fbdb0b000000000010111213" +
                "1415161718191a1b1c1d1e1f20212223" +
                "2425262728292a2b2c2d2e2f30313233" +
                "34353637"));

        Inet4Datagram reply0Datagram =
            Inet4Datagram.parse(null, new HexOctetReader(
                "450000542d7a00004001cae1c0a8002c" +
                "c0a800d100006e6269770001161a4c5c" +
                "00000000fbdb0b000000000010111213" +
                "1415161718191a1b1c1d1e1f20212223" +
                "2425262728292a2b2c2d2e2f30313233" +
                "34353637"));

        Inet4Datagram request1Datagram =
            Inet4Datagram.parse(null, new HexOctetReader(
                "4500005461644000400156f7c0a800d1" +
                "c0a8002c08004c6569770002171a4c5c" +
                "0000000014d80b000000000010111213" +
                "1415161718191a1b1c1d1e1f20212223" +
                "2425262728292a2b2c2d2e2f30313233" +
                "34353637"));

        Inet4Datagram reply1Datagram =
            Inet4Datagram.parse(null, new HexOctetReader(
                "450000542e4e00004001ca0dc0a8002c" +
                "c0a800d10000546569770002171a4c5c" +
                "0000000014d80b000000000010111213" +
                "1415161718191a1b1c1d1e1f20212223" +
                "2425262728292a2b2c2d2e2f30313233" +
                "34353637"));

        Icmp4EchoMessage request0Message =
            (Icmp4EchoMessage) Icmp4Message.parse(
                request0Datagram, request0Datagram.getPayload());
        Icmp4EchoReplyMessage reply0Message =
            (Icmp4EchoReplyMessage) Icmp4Message.parse(
                reply0Datagram, reply0Datagram.getPayload());
        Icmp4EchoMessage request1Message =
            (Icmp4EchoMessage) Icmp4Message.parse(
                request1Datagram, request1Datagram.getPayload());
        Icmp4EchoReplyMessage reply1Message =
            (Icmp4EchoReplyMessage) Icmp4Message.parse(
                reply1Datagram, reply1Datagram.getPayload());

        Icmp4EchoTransaction transaction = new Icmp4EchoTransaction();
        assertEquals(0, transaction.getRequestCount());
        assertEquals(0, transaction.getReplyCount());

        assertTrue(transaction.process(request0Message));
        assertEquals(1, transaction.getRequestCount());
        assertEquals(0, transaction.getReplyCount());

        assertTrue(transaction.process(request0Message));
        assertEquals(2, transaction.getRequestCount());
        assertEquals(0, transaction.getReplyCount());

        assertTrue(transaction.process(reply0Message));
        assertEquals(2, transaction.getRequestCount());
        assertEquals(1, transaction.getReplyCount());

        assertTrue(transaction.process(request0Message));
        assertEquals(3, transaction.getRequestCount());
        assertEquals(1, transaction.getReplyCount());

        assertTrue(transaction.process(reply0Message));
        assertEquals(3, transaction.getRequestCount());
        assertEquals(2, transaction.getReplyCount());

        assertFalse(transaction.process(request1Message));
        assertEquals(3, transaction.getRequestCount());
        assertEquals(2, transaction.getReplyCount());

        assertTrue(transaction.process(request0Message));
        assertEquals(4, transaction.getRequestCount());
        assertEquals(2, transaction.getReplyCount());

        assertFalse(transaction.process(reply1Message));
        assertEquals(4, transaction.getRequestCount());
        assertEquals(2, transaction.getReplyCount());

        assertTrue(transaction.process(request0Message));
        assertEquals(5, transaction.getRequestCount());
        assertEquals(2, transaction.getReplyCount());

        assertTrue(transaction.process(reply0Message));
        assertEquals(5, transaction.getRequestCount());
        assertEquals(3, transaction.getReplyCount());
    }
}
