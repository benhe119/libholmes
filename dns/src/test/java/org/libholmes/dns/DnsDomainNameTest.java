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

public class DnsDomainNameTest {
    @Test
    public void test() throws ParseException {
        HexOctetReader ptrReader = new HexOctetReader(
            "404040400003636f6dc004076578616d" +
            "706c65c0054040404040404040404040");
        HexOctetReader reader = new HexOctetReader(
            "03777777c00b40404040");
        HashSet<Integer> offsets = new HashSet<Integer>();

        DnsDomainName domain = new DnsDomainName(reader, ptrReader);
        assertEquals(2, domain.getRawLabelCount());
        assertTrue(domain.getRawLabel(0) instanceof DnsTextLabel);
        assertTrue(domain.getRawLabel(1) instanceof DnsCompressedLabel);
        assertEquals("www", domain.getRawLabel(0).toString());
        assertEquals("example.com.", domain.getRawLabel(1).toString());
        assertEquals(4, domain.getLabelCount());
        assertTrue(domain.getLabel(0) instanceof DnsTextLabel);
        assertTrue(domain.getLabel(1) instanceof DnsTextLabel);
        assertTrue(domain.getLabel(2) instanceof DnsTextLabel);
        assertTrue(domain.getLabel(3) instanceof DnsTextLabel);
        assertEquals("www", domain.getLabel(0).toString());
        assertEquals("example", domain.getLabel(1).toString());
        assertEquals("com", domain.getLabel(2).toString());
        assertEquals("", domain.getLabel(3).toString());
        assertEquals("www.example.com.", domain.toString());
    }
}
