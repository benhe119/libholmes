// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import static org.junit.Assert.*;
import org.junit.Test;

public class TimevalOctetPatternTest {
    @Test
    public void test32() {
        OctetPattern pattern = OctetPattern.parse(
            Json.createObjectBuilder().
                add("type", "timeval").
                add("width", 32).
                add("byteOrder", "network").
                build());
        HexOctetReader reader = new HexOctetReader("00000000000000000000000000000000ff");
        assertTrue(pattern.matches(reader));
        assertEquals(9, reader.remaining());
    }

    @Test
    public void test64() {
        OctetPattern pattern = OctetPattern.parse(
            Json.createObjectBuilder().
                add("type", "timeval").
                add("width", 64).
                add("byteOrder", "network").
                build());
        HexOctetReader reader = new HexOctetReader("00000000000000000000000000000000ff");
        assertTrue(pattern.matches(reader));
        assertEquals(1, reader.remaining());
    }

    @Test
    public void testNetwork() {
        OctetPattern pattern = OctetPattern.parse(
            Json.createObjectBuilder().
                add("type", "timeval").
                add("width", 32).
                add("byteOrder", "network").
                build());
        HexOctetReader reader0 = new HexOctetReader("0000ffff0000ffff");
        HexOctetReader reader1 = new HexOctetReader("ffff0000ffff0000");
        assertTrue(pattern.matches(reader0));
        assertFalse(pattern.matches(reader1));
    }

    @Test
    public void testHost() {
        OctetPattern pattern = OctetPattern.parse(
            Json.createObjectBuilder().
                add("type", "timeval").
                add("width", 32).
                add("byteOrder", "host").
                build());
        HexOctetReader reader0 = new HexOctetReader("0000ffff0000ffff");
        HexOctetReader reader1 = new HexOctetReader("ffff0000ffff0000");
        assertFalse(pattern.matches(reader0));
        assertTrue(pattern.matches(reader1));
    }

    @Test
    public void testUsecIn() {
        OctetPattern pattern = OctetPattern.parse(
            Json.createObjectBuilder().
                add("type", "timeval").
                add("width", 32).
                add("byteOrder", "network").
                build());
        HexOctetReader reader = new HexOctetReader("00000000000f423fff");
        assertTrue(pattern.matches(reader));
        assertEquals(1, reader.remaining());
    }

    @Test
    public void testUsecOutA() {
        OctetPattern pattern = OctetPattern.parse(
            Json.createObjectBuilder().
                add("type", "timeval").
                add("width", 32).
                add("byteOrder", "network").
                build());
        HexOctetReader reader = new HexOctetReader("00000000000f4240ff");
        assertFalse(pattern.matches(reader));
        assertEquals(1, reader.remaining());
    }

    @Test
    public void testUsecOutB() {
        OctetPattern pattern = OctetPattern.parse(
            Json.createObjectBuilder().
                add("type", "timeval").
                add("width", 32).
                add("byteOrder", "network").
                build());
        HexOctetReader reader = new HexOctetReader("00000000ffffffffff");
        assertFalse(pattern.matches(reader));
        assertEquals(1, reader.remaining());
    }
    @Test
    public void testSecIn() {
        OctetPattern pattern = OctetPattern.parse(
            Json.createObjectBuilder().
                add("type", "timeval").
                add("width", 32).
                add("byteOrder", "network").
                build());
        HexOctetReader reader = new HexOctetReader("5fffffff00000000ff");
        assertTrue(pattern.matches(reader));
        assertEquals(1, reader.remaining());
    }

    @Test
    public void testSecOutA() {
        OctetPattern pattern = OctetPattern.parse(
            Json.createObjectBuilder().
                add("type", "timeval").
                add("width", 32).
                add("byteOrder", "network").
                build());
        HexOctetReader reader = new HexOctetReader("6000000000000000ff");
        assertFalse(pattern.matches(reader));
        assertEquals(1, reader.remaining());
    }

    @Test
    public void testSecOutB() {
        OctetPattern pattern = OctetPattern.parse(
            Json.createObjectBuilder().
                add("type", "timeval").
                add("width", 32).
                add("byteOrder", "network").
                build());
        HexOctetReader reader = new HexOctetReader("ffffffff00000000ff");
        assertFalse(pattern.matches(reader));
        assertEquals(1, reader.remaining());
    }
}
