// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import static org.junit.Assert.*;
import org.junit.Test;

public class StepOctetPatternTest {
    @Test
    public void test8() {
        JsonObjectBuilder specBuilder = Json.createObjectBuilder();
        specBuilder.add("type", "step");
        specBuilder.add("init", 8);
        specBuilder.add("step", 1);
        specBuilder.add("count", 8);
        OctetPattern pattern = OctetPattern.parse(specBuilder.build());
        assertFalse(pattern.matches(new HexOctetReader("0708090a0b0c0d0e")));
        assertFalse(pattern.matches(new HexOctetReader("0708090a0b0c0d0e0f")));
        assertFalse(pattern.matches(new HexOctetReader("0708090a0b0c0d0e0f10")));
        assertFalse(pattern.matches(new HexOctetReader("08090a0b0c0d0e")));
        assertTrue(pattern.matches(new HexOctetReader("08090a0b0c0d0e0f")));
        assertTrue(pattern.matches(new HexOctetReader("08090a0b0c0d0e0f10")));
        assertFalse(pattern.matches(new HexOctetReader("090a0b0c0d0e")));
        assertFalse(pattern.matches(new HexOctetReader("090a0b0c0d0e0f")));
        assertFalse(pattern.matches(new HexOctetReader("090a0b0c0d0e0f10")));
    }

    @Test
    public void test16n() {
        JsonObjectBuilder specBuilder = Json.createObjectBuilder();
        specBuilder.add("type", "step");
        specBuilder.add("init", 8);
        specBuilder.add("step", 1);
        specBuilder.add("width", 16);
        specBuilder.add("byteOrder", "network");
        specBuilder.add("count", 4);
        OctetPattern pattern = OctetPattern.parse(specBuilder.build());
        assertFalse(pattern.matches(new HexOctetReader("000700080009000a")));
        assertFalse(pattern.matches(new HexOctetReader("000700080009000a000b")));
        assertFalse(pattern.matches(new HexOctetReader("000700080009000a000b000c")));
        assertFalse(pattern.matches(new HexOctetReader("00080009000a")));
        assertFalse(pattern.matches(new HexOctetReader("00080009000a00")));
        assertTrue(pattern.matches(new HexOctetReader("00080009000a000b")));
        assertTrue(pattern.matches(new HexOctetReader("00080009000a000b00")));
        assertTrue(pattern.matches(new HexOctetReader("00080009000a000b000c")));
        assertFalse(pattern.matches(new HexOctetReader("0009000a")));
        assertFalse(pattern.matches(new HexOctetReader("0009000a000b")));
        assertFalse(pattern.matches(new HexOctetReader("0009000a000b000c")));
    }

    @Test
    public void test16h() {
        JsonObjectBuilder specBuilder = Json.createObjectBuilder();
        specBuilder.add("type", "step");
        specBuilder.add("init", 8);
        specBuilder.add("step", 1);
        specBuilder.add("width", 16);
        specBuilder.add("byteOrder", "host");
        specBuilder.add("count", 4);
        OctetPattern pattern = OctetPattern.parse(specBuilder.build());
        assertFalse(pattern.matches(new HexOctetReader("0700080009000a00")));
        assertFalse(pattern.matches(new HexOctetReader("0700080009000a000b00")));
        assertFalse(pattern.matches(new HexOctetReader("0700080009000a000b000c00")));
        assertFalse(pattern.matches(new HexOctetReader("080009000a00")));
        assertFalse(pattern.matches(new HexOctetReader("080009000a000b")));
        assertTrue(pattern.matches(new HexOctetReader("080009000a000b00")));
        assertTrue(pattern.matches(new HexOctetReader("080009000a000b000c")));
        assertTrue(pattern.matches(new HexOctetReader("080009000a000b000c00")));
        assertFalse(pattern.matches(new HexOctetReader("09000a00")));
        assertFalse(pattern.matches(new HexOctetReader("09000a000b00")));
        assertFalse(pattern.matches(new HexOctetReader("09000a000b000c00")));
    }

    @Test
    public void test32n() {
        JsonObjectBuilder specBuilder = Json.createObjectBuilder();
        specBuilder.add("type", "step");
        specBuilder.add("init", 323);
        specBuilder.add("step", 1);
        specBuilder.add("width", 32);
        specBuilder.add("byteOrder", "network");
        specBuilder.add("count", 3);
        OctetPattern pattern = OctetPattern.parse(specBuilder.build());
        assertFalse(pattern.matches(new HexOctetReader("000143000001440000014500")));
        assertTrue(pattern.matches(new HexOctetReader("000001430000014400000145")));
        assertTrue(pattern.matches(new HexOctetReader("00000143000001440000014500")));
    }

    @Test
    public void test32h() {
        JsonObjectBuilder specBuilder = Json.createObjectBuilder();
        specBuilder.add("type", "step");
        specBuilder.add("init", 323);
        specBuilder.add("step", 1);
        specBuilder.add("width", 32);
        specBuilder.add("byteOrder", "host");
        specBuilder.add("count", 3);
        OctetPattern pattern = OctetPattern.parse(specBuilder.build());
        assertFalse(pattern.matches(new HexOctetReader("010000440100004501000000")));
        assertTrue(pattern.matches(new HexOctetReader("430100004401000045010000")));
        assertTrue(pattern.matches(new HexOctetReader("43010000440100004501000000")));
    }

    @Test
    public void test64n() {
        JsonObjectBuilder specBuilder = Json.createObjectBuilder();
        specBuilder.add("type", "step");
        specBuilder.add("init", 323);
        specBuilder.add("step", 1);
        specBuilder.add("width", 64);
        specBuilder.add("byteOrder", "network");
        specBuilder.add("count", 3);
        OctetPattern pattern = OctetPattern.parse(specBuilder.build());
        assertFalse(pattern.matches(new HexOctetReader("000000000001430000000000000144000000000000014500")));
        assertTrue(pattern.matches(new HexOctetReader("000000000000014300000000000001440000000000000145")));
        assertTrue(pattern.matches(new HexOctetReader("00000000000001430000000000000144000000000000014500")));
    }

    @Test
    public void test64h() {
        JsonObjectBuilder specBuilder = Json.createObjectBuilder();
        specBuilder.add("type", "step");
        specBuilder.add("init", 323);
        specBuilder.add("step", 1);
        specBuilder.add("width", 64);
        specBuilder.add("byteOrder", "host");
        specBuilder.add("count", 3);
        OctetPattern pattern = OctetPattern.parse(specBuilder.build());
        assertFalse(pattern.matches(new HexOctetReader("010000000000004401000000000000450100000000000000")));
        assertTrue(pattern.matches(new HexOctetReader("430100000000000044010000000000004501000000000000")));
        assertTrue(pattern.matches(new HexOctetReader("43010000000000004401000000000000450100000000000000")));
    }
}
