// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import static org.junit.Assert.*;
import org.junit.Test;

public class TextOctetPatternTest {
    @Test
    public void testUtf8() {
        JsonObjectBuilder specBuilder = Json.createObjectBuilder();
        specBuilder.add("type", "text");
        specBuilder.add("content", "helloworld");
        OctetPattern pattern = OctetPattern.parse(specBuilder.build());
        assertTrue(pattern.matches(new HexOctetReader("68656c6c6f776f726c64")));
        assertTrue(pattern.matches(new HexOctetReader("68656c6c6f776f726c6400")));
        assertFalse(pattern.matches(new HexOctetReader("69656c6c6f776f726c64")));
        assertFalse(pattern.matches(new HexOctetReader("68656c6c6f776f726c65")));
    }

    @Test
    public void testUtf16() {
        JsonObjectBuilder specBuilder = Json.createObjectBuilder();
        specBuilder.add("type", "text");
        specBuilder.add("encoding", "UTF-16BE");
        specBuilder.add("content", "helloworld");
        OctetPattern pattern = OctetPattern.parse(specBuilder.build());
        assertTrue(pattern.matches(new HexOctetReader("00680065006c006c006f0077006f0072006c0064")));
        assertTrue(pattern.matches(new HexOctetReader("00680065006c006c006f0077006f0072006c00640000")));
        assertFalse(pattern.matches(new HexOctetReader("00690065006c006c006f0077006f0072006c0064")));
        assertFalse(pattern.matches(new HexOctetReader("00680065006c006c006f0077006f0072006c0065")));
    }
}
