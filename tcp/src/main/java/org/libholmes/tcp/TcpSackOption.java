// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.tcp;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonArrayBuilder;

import org.libholmes.OctetReader;
import org.libholmes.OctetString;
import org.libholmes.ParseException;

/** A class to represent a TCP SACK option. */
public class TcpSackOption extends TcpOption {
    private final ArrayList<TcpSequenceNumberBlock> blocks =
        new ArrayList<TcpSequenceNumberBlock>();

    /** Parse TCP SACK option from a generic option.
     * @param option the generic option to be parsed
     * @throws ParseException if the octet sequence cannot be parsed
     */
    public TcpSackOption(TcpOption option) throws ParseException {
        super(option);
        OctetReader reader = getPayload().makeOctetReader();
        if (reader.remaining() % 8 != 0) {
            throw new ParseException("invalid length for TCP SACK option");
        }
        while (reader.hasRemaining()) {
            int begin = reader.readInt();
            int end = reader.readInt();
            blocks.add(new TcpSequenceNumberBlock(begin, end));
        }
    }

    /** Get the acknowledged sequence number blocks.
     * @return a list of acknowledged blocks
     */
    public final List<TcpSequenceNumberBlock> getBlocks() {
        return Collections.unmodifiableList(blocks);
    }

    @Override
    protected void buildJson(JsonObjectBuilder builder) {
        super.buildJson(builder);

        JsonArrayBuilder blocksBuilder = Json.createArrayBuilder();
        for (TcpSequenceNumberBlock block : blocks) {
            blocksBuilder.add(block.toJson());
        }
        builder.add("blocks", blocksBuilder.build());
    }
}
