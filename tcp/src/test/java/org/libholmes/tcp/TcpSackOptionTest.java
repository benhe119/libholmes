// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.tcp;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.List;
import javax.json.JsonObject;
import javax.json.JsonArray;

import org.libholmes.HexOctetReader;
import org.libholmes.ArrayOctetReader;
import org.libholmes.ParseException;

public class TcpSackOptionTest {
    @Test
    public void test() throws ParseException {
        TcpOption rawOption = TcpOption.parse(null,
            new HexOctetReader("050a4fe652964fe65297"));
        assertTrue(rawOption instanceof TcpSackOption);
        TcpSackOption option = (TcpSackOption) rawOption;

        assertEquals(5, option.getKind());
        assertEquals(10, option.getLength());
        List<TcpSequenceNumberBlock> blocks = option.getBlocks();
        assertEquals(1, blocks.size());

        TcpSequenceNumberBlock block0 = blocks.get(0);
        assertEquals(0x4fe65296L, block0.getBegin());
        assertEquals(0x4fe65297L, block0.getEnd());

        JsonObject json = option.toJson();
        assertEquals(5, json.getInt("kind"));
        assertEquals(10, json.getInt("length"));
        JsonArray jsonBlocks = json.getJsonArray("blocks");
        assertEquals(1, blocks.size());

        JsonObject jsonBlock0 = jsonBlocks.getJsonObject(0);
        assertEquals(0x4fe65296L, jsonBlock0.getInt("begin"));
        assertEquals(0x4fe65297L, jsonBlock0.getInt("end"));
    }

    @Test
    public void testMultipleBlocks() throws ParseException {
        TcpOption rawOption = TcpOption.parse(null,
            new HexOctetReader("051a" +
                "0100000001000040" +
                "01000080010000c0" +
                "0100010001000140"));
        assertTrue(rawOption instanceof TcpSackOption);
        TcpSackOption option = (TcpSackOption) rawOption;

        List<TcpSequenceNumberBlock> blocks = option.getBlocks();
        assertEquals(3, blocks.size());

        TcpSequenceNumberBlock block0 = blocks.get(0);
        assertEquals(0x01000000L, block0.getBegin());
        assertEquals(0x01000040L, block0.getEnd());

        TcpSequenceNumberBlock block1 = blocks.get(1);
        assertEquals(0x01000080L, block1.getBegin());
        assertEquals(0x010000c0L, block1.getEnd());

        TcpSequenceNumberBlock block2 = blocks.get(2);
        assertEquals(0x01000100L, block2.getBegin());
        assertEquals(0x01000140L, block2.getEnd());
    }
}
