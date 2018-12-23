// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.inet;

import static org.junit.Assert.*;
import org.junit.Test;

import org.libholmes.HexOctetReader;
import org.libholmes.HexOctetString;
import org.libholmes.ArrayOctetReader;
import org.libholmes.ParseException;

public class InetDatagramTest {
    @Test
    public void testInet4() throws ParseException {
        HexOctetReader reader = new HexOctetReader(
            "4500001cc9b4000040012ef0c0a80001" +
            "c0a800eb0000d63129cd000100000000" +
            "0000000000000000000000000000");
        InetDatagram datagram = InetDatagram.parse(null, reader);
        assertEquals(4, datagram.getVersion());
    }

    @Test
    public void testUnsupported() {
        for (int v = 0; v != 16; ++v) {
            if ((v != 4) && (v != 6)) {
                byte[] raw = new byte[256];
                raw[0] = (byte) (v << 4);
                try {
                    InetDatagram datagram = InetDatagram.parse(null,
                        new ArrayOctetReader(raw, ArrayOctetReader.BIG_ENDIAN));
                    fail("ParseException expected");
                } catch (ParseException ex) {}
            }
        }
    }
}
