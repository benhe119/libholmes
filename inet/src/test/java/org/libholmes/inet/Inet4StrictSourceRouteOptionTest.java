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

public class Inet4StrictSourceRouteOptionTest {
    @Test
    public void test() throws ParseException {
        // Test data generated using nmap with the --ip-options S option.
        HexOctetReader reader = new HexOctetReader(
            "891304c0a80001ac1000010a0000010a000002");
        Inet4Option rawOption = Inet4Option.parse(null, reader);
        assertEquals(0, reader.remaining());
        assertTrue(rawOption instanceof Inet4StrictSourceRouteOption);
        Inet4StrictSourceRouteOption option = (Inet4StrictSourceRouteOption) rawOption;

        assertEquals(137, option.getType());
        assertEquals(19, option.getLength());
        assertEquals(4, option.getPointer());
        assertEquals(4, option.getRoute().size());
        assertEquals("192.168.0.1", option.getRoute().get(0).toString());
        assertEquals("172.16.0.1", option.getRoute().get(1).toString());
        assertEquals("10.0.0.1", option.getRoute().get(2).toString());
        assertEquals("10.0.0.2", option.getRoute().get(3).toString());

        JsonObject json = option.toJson();
        assertEquals(137, json.getInt("type"));
        assertEquals(19, json.getInt("length"));
        assertEquals(4, json.getInt("pointer"));
        JsonArray jsonRoute = json.getJsonArray("route");
        assertEquals(4, jsonRoute.size());
        assertEquals("192.168.0.1", jsonRoute.getString(0));
        assertEquals("172.16.0.1", jsonRoute.getString(1));
        assertEquals("10.0.0.1", jsonRoute.getString(2));
        assertEquals("10.0.0.2", jsonRoute.getString(3));
    }
}
