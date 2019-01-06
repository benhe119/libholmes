// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.tcp;

import static org.junit.Assert.*;
import org.junit.Test;

import javax.json.JsonObject;

import org.libholmes.HexOctetReader;
import org.libholmes.ArrayOctetReader;
import org.libholmes.ParseException;

public class TcpEndOfOptionsListTest {
    @Test
    public void test() throws ParseException {
        TcpOption option = TcpOption.parse(null,
            new HexOctetReader("00"));
        assertEquals(0, option.getKind());
        assertTrue(option instanceof TcpEndOfOptionsList);

        JsonObject json = option.toJson();
        assertEquals(json.getInt("kind"), 0);
    }
}
