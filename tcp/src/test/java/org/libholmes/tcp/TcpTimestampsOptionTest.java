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

public class TcpTimestampsOptionTest {
    @Test
    public void test() throws ParseException {
        TcpOption rawOption = TcpOption.parse(null,
            new HexOctetReader("080a4c13982fbd379925"));
        assertTrue(rawOption instanceof TcpTimestampsOption);
        TcpTimestampsOption option = (TcpTimestampsOption) rawOption;

        assertEquals(8, option.getKind());
        assertEquals(10, option.getLength());
        assertEquals(1276352559L,
            option.getTimestampValue() & 0xffffffffL);
        assertEquals(3174537509L,
            option.getTimestampEchoReply() & 0xffffffffL);

        JsonObject json = option.toJson();
        assertEquals(8, json.getInt("kind"));
        assertEquals(10, json.getInt("length"));
        assertEquals(1276352559L, json.getInt("tsVal") & 0xffffffffL);
        assertEquals(3174537509L, json.getInt("tsEcr") & 0xffffffffL);
    }
}
