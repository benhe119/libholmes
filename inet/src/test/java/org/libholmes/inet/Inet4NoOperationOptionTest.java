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

public class Inet4NoOperationOptionTest {
    @Test
    public void test() throws ParseException {
        Inet4Option option = Inet4Option.parse(null,
            new HexOctetReader("01"));
        assertEquals(1, option.getType());
        assertTrue(option instanceof Inet4NoOperationOption);

        JsonObject json = option.toJson();
        assertEquals(json.getInt("type"), 1);
    }
}
