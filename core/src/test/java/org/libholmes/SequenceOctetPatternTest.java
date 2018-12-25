// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import javax.json.Json;
import javax.json.JsonArrayBuilder;

import static org.junit.Assert.*;
import org.junit.Test;

public class SequenceOctetPatternTest {
    @Test
    public void testEmpty() {
        JsonArrayBuilder specBuilder = Json.createArrayBuilder();
        OctetPattern pattern = OctetPattern.parse(specBuilder.build());
        assertTrue(pattern.matches(new HexOctetReader("")));
        assertTrue(pattern.matches(new HexOctetReader("00")));
    }

    @Test
    public void test() {
        JsonArrayBuilder specBuilder = Json.createArrayBuilder();
        specBuilder.add(
            Json.createObjectBuilder().
                add("type", "hex").
                add("content", "55aa"));
        specBuilder.add(
            Json.createObjectBuilder().
                add("type", "hex").
                add("content", "6699"));
        OctetPattern pattern = OctetPattern.parse(specBuilder.build());
        assertFalse(pattern.matches(new HexOctetReader("")));
        assertFalse(pattern.matches(new HexOctetReader("55aa")));
        assertFalse(pattern.matches(new HexOctetReader("6699")));
        assertTrue(pattern.matches(new HexOctetReader("55aa6699")));
        assertTrue(pattern.matches(new HexOctetReader("55aa669900")));
        assertFalse(pattern.matches(new HexOctetReader("0055aa6699")));
    }
}
