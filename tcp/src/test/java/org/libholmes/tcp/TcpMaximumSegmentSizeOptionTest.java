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

public class TcpMaximumSegmentSizeOptionTest {
    @Test
    public void test() throws ParseException {
        TcpOption rawOption = TcpOption.parse(null,
            new HexOctetReader("020405b4"));
        assertTrue(rawOption instanceof TcpMaximumSegmentSizeOption);
        TcpMaximumSegmentSizeOption option =
            (TcpMaximumSegmentSizeOption) rawOption;

        assertEquals(2, option.getKind());
        assertEquals(4, option.getLength());
        assertEquals(1460, option.getMaximumSegmentSize());

        JsonObject json = option.toJson();
        assertEquals(2, json.getInt("kind"));
        assertEquals(4, json.getInt("length"));
        assertEquals(1460, json.getInt("mss"));
    }
}
