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

public class Icmp4TimeExceededMessageTest {
    @Test
    public void test() throws ParseException {
        HexOctetReader content = new HexOctetReader(
            "0b00f4ff0000000045000054" +
            "53de400001015428c0a800eb08080808" +
            "0800a41e2bd40001f961895a00000000" +
            "df7c0700000000001011121314151617" +
            "18191a1b1c1d1e1f2021222324252627" +
            "28292a2b2c2d2e2f3031323334353637");
        Icmp4Message icmpMessage = Icmp4Message.parse(null, content);
        assertTrue(icmpMessage instanceof Icmp4TimeExceededMessage);

        Icmp4TimeExceededMessage decodedMessage =
            (Icmp4TimeExceededMessage) icmpMessage;
        assertEquals(11, decodedMessage.getType());
        assertEquals(0, decodedMessage.getCode());
        assertEquals(0xf4ff, decodedMessage.getRecordedChecksum());
        assertEquals(0xf4ff, decodedMessage.getCalculatedChecksum());
        assertEquals("45000054" +
            "53de400001015428c0a800eb08080808" +
            "0800a41e2bd40001f961895a00000000" +
            "df7c0700000000001011121314151617" +
            "18191a1b1c1d1e1f2021222324252627" +
            "28292a2b2c2d2e2f3031323334353637",
            decodedMessage.getOriginalDatagram().toString());

        JsonObject jsonMessage = icmpMessage.toJson();
        assertEquals(11, jsonMessage.getInt("type"));
        assertEquals(0, jsonMessage.getInt("code"));
        assertEquals(0xf4ff, jsonMessage.getInt("recordedChecksum"));
        assertEquals(0xf4ff, jsonMessage.getInt("calculatedChecksum"));
        assertEquals("45000054" +
            "53de400001015428c0a800eb08080808" +
            "0800a41e2bd40001f961895a00000000" +
            "df7c0700000000001011121314151617" +
            "18191a1b1c1d1e1f2021222324252627" +
            "28292a2b2c2d2e2f3031323334353637",
            jsonMessage.getString("originalDatagram"));
    }
}
