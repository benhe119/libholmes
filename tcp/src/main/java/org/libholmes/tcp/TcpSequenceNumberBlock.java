// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.tcp;

import javax.json.Json;
import javax.json.JsonObject;

import org.libholmes.OctetReader;
import org.libholmes.OctetString;
import org.libholmes.ParseException;

/** A class to represent a block of TCP sequence numbers. */
public class TcpSequenceNumberBlock {
    /** The beginning of the block (inclusive). */
    private final int begin;

    /** The end of the block (exclusive). */
    private final int end;

    /** Constuct TCP sequence number block.
     * @param begin the start of the block
     * @param end the end of the block
     */
    public TcpSequenceNumberBlock(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    /** Get the beginning of the block.
     * @return the beginning of the block (inclusive)
     */
    public final int getBegin() {
        return begin;
    }

    /** Get the end of the block.
     * @return the end of the block (exclusive)
     */
    public final int getEnd() {
        return end;
    }

    /** Convert this block to JSON.
     * @return this block, as a JSON object
     */
    public final JsonObject toJson() {
        return Json.createObjectBuilder()
            .add("begin", begin & 0xffffffffL)
            .add("end", end & 0xffffffffL)
            .build();
    }
}
