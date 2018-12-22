// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.inet;

import static org.junit.Assert.*;
import org.junit.Test;

import javax.json.JsonObject;

import org.libholmes.HexOctetReader;
import org.libholmes.HexOctetString;
import org.libholmes.ArrayOctetReader;
import org.libholmes.ParseException;

public class Inet4UnrecognisedOptionTest {
    @Test
    public void test() throws ParseException {
        Inet4Option option = Inet4Option.parse(null,
            new HexOctetReader("ff0a0104091019243140"));
        assertEquals(0xff, option.getType());
        assertTrue(option instanceof Inet4UnrecognisedOption);
        Inet4UnrecognisedOption unrecognisedOption =
            (Inet4UnrecognisedOption) option;
        assertEquals(10, unrecognisedOption.getLength());
        assertEquals(new HexOctetString("0104091019243140"),
            unrecognisedOption.getPayload());
    }

    @Test
    public void testJson() throws ParseException {
        Inet4Option option = Inet4Option.parse(null,
            new HexOctetReader("ff0a0104091019243140"));
        JsonObject json = option.toJson();
        assertEquals(json.getInt("type"), 255);
        assertEquals(json.getInt("length"), 10);
        assertEquals(json.getString("payload"), "0104091019243140");
    }
}
