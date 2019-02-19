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

public class Icmp4DestinationUnreachableMessageTest {
    @Test
    public void test() throws ParseException {
        HexOctetReader content = new HexOctetReader(
            "0300da03000000004500003c" +
            "a79740003e06c990c0a800eb0a000001" +
            "ea7200502e390a00");
        Icmp4Message icmpMessage = Icmp4Message.parse(null, content);
        assertTrue(icmpMessage instanceof Icmp4DestinationUnreachableMessage);

        Icmp4DestinationUnreachableMessage decodedMessage =
            (Icmp4DestinationUnreachableMessage) icmpMessage;
        assertEquals(3, decodedMessage.getType());
        assertEquals(0, decodedMessage.getCode());
        assertEquals(0xda03, decodedMessage.getRecordedChecksum());
        assertEquals(0xda03, decodedMessage.getCalculatedChecksum());
        assertEquals("4500003c" +
            "a79740003e06c990c0a800eb0a000001" +
            "ea7200502e390a00",
            decodedMessage.getOriginalDatagram().toString());

        JsonObject jsonMessage = icmpMessage.toJson();
        assertEquals(3, jsonMessage.getInt("type"));
        assertEquals(0, jsonMessage.getInt("code"));
        assertEquals(0xda03, jsonMessage.getInt("recordedChecksum"));
        assertEquals(0xda03, jsonMessage.getInt("calculatedChecksum"));
        assertEquals("4500003c" +
            "a79740003e06c990c0a800eb0a000001" +
            "ea7200502e390a00",
            jsonMessage.getString("originalDatagram"));
    }
}
