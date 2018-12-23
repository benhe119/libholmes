// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.icmp;

import static org.junit.Assert.*;
import org.junit.Test;

import javax.json.JsonObject;

import org.libholmes.HexOctetReader;
import org.libholmes.ParseException;

public class Icmp4EchoReplyMessageTest {
    @Test
    public void test() throws ParseException {
        HexOctetReader content = new HexOctetReader(
            "0000b80151ef00074de9875a" +
            "0000000053f10e000000000010111213" +
            "1415161718191a1b1c1d1e1f20212223" +
            "2425262728292a2b2c2d2e2f30313233" +
            "34353637");
        Icmp4Message icmpMessage = Icmp4Message.parse(null, content);
        assertTrue(icmpMessage instanceof Icmp4EchoReplyMessage);

        Icmp4EchoReplyMessage decodedMessage =
            (Icmp4EchoReplyMessage) icmpMessage;
        assertEquals(0, decodedMessage.getType());
        assertEquals(0, decodedMessage.getCode());
        assertEquals(0x51ef, decodedMessage.getIdentifier());
        assertEquals(7, decodedMessage.getSequenceNumber());
        assertEquals(
            "4de9875a0000000053f10e0000000000" +
            "101112131415161718191a1b1c1d1e1f" +
            "202122232425262728292a2b2c2d2e2f" +
            "3031323334353637",
            decodedMessage.getData().toString());

        JsonObject jsonMessage = icmpMessage.toJson();
        assertEquals(0, jsonMessage.getInt("type"));
        assertEquals(0, jsonMessage.getInt("code"));
        assertEquals(0x51ef, jsonMessage.getInt("identifier"));
        assertEquals(7, jsonMessage.getInt("sequenceNumber"));
        assertEquals(
            "4de9875a0000000053f10e0000000000" +
            "101112131415161718191a1b1c1d1e1f" +
            "202122232425262728292a2b2c2d2e2f" +
            "3031323334353637",
            jsonMessage.getString("data"));
    }
}
