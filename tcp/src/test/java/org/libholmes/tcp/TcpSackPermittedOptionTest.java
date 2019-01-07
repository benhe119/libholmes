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

public class TcpSackPermittedOptionTest {
    @Test
    public void test() throws ParseException {
        TcpOption rawOption = TcpOption.parse(null,
            new HexOctetReader("0402"));
        assertTrue(rawOption instanceof TcpSackPermittedOption);
        TcpSackPermittedOption option = (TcpSackPermittedOption) rawOption;

        assertEquals(4, option.getKind());
        assertEquals(2, option.getLength());

        JsonObject json = option.toJson();
        assertEquals(4, json.getInt("kind"));
        assertEquals(2, json.getInt("length"));
    }
}
