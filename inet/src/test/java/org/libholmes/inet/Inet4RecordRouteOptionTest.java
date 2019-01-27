// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.inet;

import static org.junit.Assert.*;
import org.junit.Test;

import javax.json.JsonObject;
import javax.json.JsonArray;

import org.libholmes.HexOctetReader;
import org.libholmes.ParseException;

public class Inet4RecordRouteOptionTest {
    @Test
    public void test() throws ParseException {
        // Test data generated using the ping -R option on Ubuntu Lucid.
        // (Note: does not work on Ubuntu Precise or Trusty.)
        HexOctetReader reader = new HexOctetReader(
            "072710c0a80088c0a80001c0a8000100" +
            "00000000000000000000000000000000" +
            "00000000000000");
        Inet4Option rawOption = Inet4Option.parse(null, reader);
        assertEquals(0, reader.remaining());
        assertTrue(rawOption instanceof Inet4RecordRouteOption);
        Inet4RecordRouteOption option = (Inet4RecordRouteOption) rawOption;

        assertEquals(7, option.getType());
        assertEquals(39, option.getLength());
        assertEquals(16, option.getPointer());
        assertEquals(9, option.getRoute().size());
        assertEquals("192.168.0.136", option.getRoute().get(0).toString());
        assertEquals("192.168.0.1", option.getRoute().get(1).toString());
        assertEquals("192.168.0.1", option.getRoute().get(2).toString());
        assertEquals("0.0.0.0", option.getRoute().get(3).toString());
        assertEquals("0.0.0.0", option.getRoute().get(4).toString());
        assertEquals("0.0.0.0", option.getRoute().get(5).toString());
        assertEquals("0.0.0.0", option.getRoute().get(6).toString());
        assertEquals("0.0.0.0", option.getRoute().get(7).toString());
        assertEquals("0.0.0.0", option.getRoute().get(8).toString());

        JsonObject json = option.toJson();
        assertEquals(7, json.getInt("type"));
        assertEquals(39, json.getInt("length"));
        assertEquals(16, json.getInt("pointer"));
        JsonArray jsonRoute = json.getJsonArray("route");
        assertEquals(9, jsonRoute.size());
        assertEquals("192.168.0.136", jsonRoute.getString(0));
        assertEquals("192.168.0.1", jsonRoute.getString(1));
        assertEquals("192.168.0.1", jsonRoute.getString(2));
        assertEquals("0.0.0.0", jsonRoute.getString(3));
        assertEquals("0.0.0.0", jsonRoute.getString(4));
        assertEquals("0.0.0.0", jsonRoute.getString(5));
        assertEquals("0.0.0.0", jsonRoute.getString(6));
        assertEquals("0.0.0.0", jsonRoute.getString(7));
        assertEquals("0.0.0.0", jsonRoute.getString(8));
    }
}
