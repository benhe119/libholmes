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

public class TcpNoOperationOptionTest {
    @Test
    public void test() throws ParseException {
        TcpOption option = TcpOption.parse(null,
            new HexOctetReader("01"));
        assertEquals(1, option.getKind());
        assertTrue(option instanceof TcpNoOperationOption);

        JsonObject json = option.toJson();
        assertEquals(json.getInt("kind"), 1);
    }
}
