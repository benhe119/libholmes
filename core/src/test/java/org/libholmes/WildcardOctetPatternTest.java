// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import static org.junit.Assert.*;
import org.junit.Test;

public class WildcardOctetPatternTest {
    @Test
    public void test() {
        OctetPattern pattern = OctetPattern.parse(
            Json.createObjectBuilder().
                add("type", "wildcard").
                add("length", 3).
                build());
        HexOctetReader reader = new HexOctetReader("0001020304050607");
        assertTrue(pattern.matches(reader));
        assertEquals(5, reader.remaining());
        assertTrue(pattern.matches(reader));
        assertEquals(2, reader.remaining());
        assertFalse(pattern.matches(reader));
    }
}
