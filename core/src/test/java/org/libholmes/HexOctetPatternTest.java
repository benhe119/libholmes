// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import static org.junit.Assert.*;
import org.junit.Test;

public class HexOctetPatternTest {
    @Test
    public void testEmpty() {
        JsonObjectBuilder specBuilder = Json.createObjectBuilder();
        specBuilder.add("type", "hex");
        specBuilder.add("content", "");
        OctetPattern pattern = OctetPattern.parse(specBuilder.build());
        assertTrue(pattern.matches(new HexOctetReader("")));
        assertTrue(pattern.matches(new HexOctetReader("00")));
    }

    @Test
    public void test() {
        JsonObjectBuilder specBuilder = Json.createObjectBuilder();
        specBuilder.add("type", "hex");
        specBuilder.add("content", "55aa");
        OctetPattern pattern = OctetPattern.parse(specBuilder.build());
        assertFalse(pattern.matches(new HexOctetReader("")));
        assertFalse(pattern.matches(new HexOctetReader("55")));
        assertTrue(pattern.matches(new HexOctetReader("55aa")));
        assertFalse(pattern.matches(new HexOctetReader("5555aa")));
        assertTrue(pattern.matches(new HexOctetReader("55aa00")));
    }
}
