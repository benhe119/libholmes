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

public class TcpWindowScaleOptionTest {
    @Test
    public void test() throws ParseException {
        TcpOption rawOption = TcpOption.parse(null,
            new HexOctetReader("030307"));
        assertTrue(rawOption instanceof TcpWindowScaleOption);
        TcpWindowScaleOption option = (TcpWindowScaleOption) rawOption;

        assertEquals(3, option.getKind());
        assertEquals(3, option.getLength());
        assertEquals(7, option.getShift());

        JsonObject json = option.toJson();
        assertEquals(3, json.getInt("kind"));
        assertEquals(3, json.getInt("length"));
        assertEquals(7, json.getInt("shift"));
    }
}
