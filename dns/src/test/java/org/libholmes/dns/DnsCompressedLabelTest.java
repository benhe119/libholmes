// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.dns;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.HashSet;

import org.libholmes.HexOctetReader;
import org.libholmes.ParseException;

public class DnsCompressedLabelTest {
    @Test
    public void test() throws ParseException {
        HexOctetReader ptrReader = new HexOctetReader(
            "404040400003636f6dc004076578616d" +
            "706c65c00503777777c00b4040404040");
        HexOctetReader reader = new HexOctetReader(
            "c01540404040");
        HashSet<Integer> offsets = new HashSet<Integer>();

        DnsCompressedLabel label =
            new DnsCompressedLabel(reader, ptrReader, offsets);
        assertEquals(17, label.length());
        assertTrue(label.isFinal());
        assertEquals(2, label.getRawLabelCount());
        assertTrue(label.getRawLabel(0) instanceof DnsTextLabel);
        assertTrue(label.getRawLabel(1) instanceof DnsCompressedLabel);
        assertEquals("www", label.getRawLabel(0).toString());
        assertEquals("example.com.", label.getRawLabel(1).toString());
        assertEquals(4, label.getLabelCount());
        assertTrue(label.getLabel(0) instanceof DnsTextLabel);
        assertTrue(label.getLabel(1) instanceof DnsTextLabel);
        assertTrue(label.getLabel(2) instanceof DnsTextLabel);
        assertTrue(label.getLabel(3) instanceof DnsTextLabel);
        assertEquals("www", label.getLabel(0).toString());
        assertEquals("example", label.getLabel(1).toString());
        assertEquals("com", label.getLabel(2).toString());
        assertEquals("", label.getLabel(3).toString());
        assertEquals("www.example.com.", label.toString());
    }
}
