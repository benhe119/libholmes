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

public class Icmp4SourceQuenchMessageTest {
    @Test
    public void test() throws ParseException {
        HexOctetReader content = new HexOctetReader(
            "0400185ac0a800014500003c" +
            "a79740003e06c990c0a800eb0a000001" +
            "ea7200502e390a00");
        Icmp4Message icmpMessage = Icmp4Message.parse(null, content);
        assertTrue(icmpMessage instanceof Icmp4SourceQuenchMessage);

        Icmp4SourceQuenchMessage decodedMessage =
            (Icmp4SourceQuenchMessage) icmpMessage;
        assertEquals(4, decodedMessage.getType());
        assertEquals(0, decodedMessage.getCode());
        assertEquals(0x185a, decodedMessage.getRecordedChecksum());
        assertEquals(0x185a, decodedMessage.getCalculatedChecksum());
        assertEquals("192.168.0.1",
            decodedMessage.getGatewayInternetAddress().toString());
        assertEquals("4500003c" +
            "a79740003e06c990c0a800eb0a000001" +
            "ea7200502e390a00",
            decodedMessage.getOriginalDatagram().toString());

        JsonObject jsonMessage = icmpMessage.toJson();
        assertEquals(4, jsonMessage.getInt("type"));
        assertEquals(0, jsonMessage.getInt("code"));
        assertEquals(0x185a, jsonMessage.getInt("recordedChecksum"));
        assertEquals(0x185a, jsonMessage.getInt("calculatedChecksum"));
        assertEquals("192.168.0.1",
            jsonMessage.getString("gatewayInternetAddress"));
        assertEquals("4500003c" +
            "a79740003e06c990c0a800eb0a000001" +
            "ea7200502e390a00",
            jsonMessage.getString("originalDatagram"));
    }
}
