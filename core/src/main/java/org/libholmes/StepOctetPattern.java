// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import javax.json.JsonObject;

/** An OctetPattern class to represent an arithmetic progression. */
public class StepOctetPattern extends OctetPattern {
    /** A constant used to indicate little-endian byte order. */
    private static final int LITTLE_ENDIAN = 0;

    /** A constant used to indicate big-endian byte order. */
    private static final int BIG_ENDIAN = -1;

    /** The initial value. */
    private final long init;

    /** The step between values. */
    private final long step;

    /** The width of each value, in bits. */
    private final int bitWidth;

    /** The width of each value, in bytes. */
    private final int byteWidth;

    /** The byte order mask.
     * This is the value which, when bitwise XORed with an address
     * offset, allows values to be read with the intended byte order.
     * It is equal to the bitwise AND of (byteWidth-1) with either
     * LITTLE_ENDIAN or BIG_ENDIAN as appropriate.
     */
    private final int byteOrder;

    /** The minimum number of values required for a match. */
    private final int minCount;

    /** The maximum number of values allowed for a match,
     * or -1 for no maximum. */
    private final int maxCount;

    /** Parse StepOctetPattern from a specification in JSON format.
     * @param jsonSpec the specification to be parsed
     */
    public StepOctetPattern(JsonObject jsonSpec) {
        init = jsonSpec.getJsonNumber("init").longValue();
        step = jsonSpec.getJsonNumber("step").longValue();

        bitWidth = jsonSpec.getInt("width", 8);
        if ((bitWidth != 8) && (bitWidth != 16) && (bitWidth != 32) &&
            (bitWidth != 64)) {
                throw new RuntimeException(
                    "invalid width for StepOctetPattern");
        }
        byteWidth = bitWidth / 8;

        String byteOrderString = jsonSpec.getString("byteOrder", "");
        if (byteOrderString.equals("") || byteOrderString.equals("network")) {
            byteOrder = BIG_ENDIAN & (byteWidth - 1);
        } else if (byteOrderString.equals("host")) {
            byteOrder = LITTLE_ENDIAN & (byteWidth - 1);
        } else {
            throw new RuntimeException(
                "invalid byteOrder for StepOctetPattern");
        }

        if (jsonSpec.containsKey("count")) {
            minCount = jsonSpec.getInt("count");
            maxCount = minCount;
            if (minCount < 0) {
                throw new RuntimeException(
                    "invalid count for StepOctetPattern");
            }
        } else {
            minCount = jsonSpec.getInt("minCount", 0);
            maxCount = jsonSpec.getInt("maxCount", Integer.MAX_VALUE);
            if (minCount < 0) {
                throw new RuntimeException(
                    "invalid minCount for StepOctetPattern");
            }
            if (maxCount < 0) {
                throw new RuntimeException(
                    "invalid maxCount for StepOctetPattern");
            }
        }
    }

    @Override
    public final boolean matches(OctetReader reader) {
        int count = 0;
        long value = init;
        while ((count < maxCount) && (reader.remaining() >= byteWidth)) {
            long acc = 0;
            for (int i = byteWidth - 1; i >= 0; --i) {
                acc <<= 8;
                acc |= reader.peekByte(i ^ byteOrder) & 0xff;
            }
            if (acc != value) {
                return false;
            }
            reader.skip(byteWidth);
            count += 1;
            value += step;
        }
        return (count >= minCount);
    }
}
