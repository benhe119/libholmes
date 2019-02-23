// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.dns;

import static org.junit.Assert.*;
import org.junit.Test;

import org.libholmes.HexOctetReader;
import org.libholmes.ParseException;

public class DnsTextLabelTest {
    @Test
    public void test() throws ParseException {
        HexOctetReader reader = new HexOctetReader(
            "03777777");
        DnsTextLabel label = new DnsTextLabel(reader);

        assertEquals(4, label.length());
        assertFalse(label.isFinal());
        assertEquals("www", label.toString());
    }
}
