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

public class Icmp4ParameterProblemMessageTest {
    @Test
    public void test() throws ParseException {
        HexOctetReader content = new HexOctetReader(
            "0c00cb03060000004500003c" +
            "a79740003e06c990c0a800eb0a000001" +
            "ea7200502e390a00");
        Icmp4Message icmpMessage = Icmp4Message.parse(null, content);
        assertTrue(icmpMessage instanceof Icmp4ParameterProblemMessage);

        Icmp4ParameterProblemMessage decodedMessage =
            (Icmp4ParameterProblemMessage) icmpMessage;
        assertEquals(12, decodedMessage.getType());
        assertEquals(0, decodedMessage.getCode());
        assertEquals(6, decodedMessage.getPointer());
        assertEquals(0xcb03, decodedMessage.getRecordedChecksum());
        assertEquals(0xcb03, decodedMessage.getCalculatedChecksum());
        assertEquals("4500003c" +
            "a79740003e06c990c0a800eb0a000001" +
            "ea7200502e390a00",
            decodedMessage.getOriginalDatagram().toString());

        JsonObject jsonMessage = icmpMessage.toJson();
        assertEquals(12, jsonMessage.getInt("type"));
        assertEquals(0, jsonMessage.getInt("code"));
        assertEquals(6, jsonMessage.getInt("pointer"));
        assertEquals(0xcb03, jsonMessage.getInt("recordedChecksum"));
        assertEquals(0xcb03, jsonMessage.getInt("calculatedChecksum"));
        assertEquals("4500003c" +
            "a79740003e06c990c0a800eb0a000001" +
            "ea7200502e390a00",
            jsonMessage.getString("originalDatagram"));
    }
}
