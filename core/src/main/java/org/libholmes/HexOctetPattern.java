// This file is part of libholmes.
// Copyright 2018-2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import javax.json.JsonObject;

/** An OctetPattern class to represent a literal hex pattern. */
public class HexOctetPattern extends OctetPattern {
    /** The pattern to be matched. */
    private final HexOctetString pattern;

    /** True if this is a repeating pattern, otherwise false. */
    private final boolean repeat;

    /** The minimum permitted length in octets. */
    private final int minLength;

    /** The maximum permitted length in octets, or -1 if no limit. */
    private final int maxLength;

    /** Parse HexOctetPattern from a specification in JSON format.
     * @param jsonSpec the specification to be parsed
     */
    public HexOctetPattern(JsonObject jsonSpec) {
        pattern = new HexOctetString(jsonSpec.getString("content"));
        repeat = jsonSpec.getBoolean("repeat", false);
        if (jsonSpec.containsKey("length")) {
            int length = jsonSpec.getInt("length", -1);
            minLength = length;
            maxLength = length;
        } else {
            minLength = jsonSpec.getInt("minLength", 0);
            maxLength = jsonSpec.getInt("maxLength", -1);
        }
    }

    @Override
    public final boolean matches(OctetReader reader,
        AnalysisContext context) {

        if (repeat) {
            int index = 0;
            int count = 0;
            while (((count < maxLength) || (maxLength == -1)) &&
                reader.hasRemaining()) {

                if (reader.readByte() != pattern.getByte(index)) {
                    return false;
                }
                index += 1;
                if (index == pattern.length()) {
                    index = 0;
                }
                count += 1;
            }
            if (count < minLength) return false;
            return true;
        } else {
            if (reader.remaining() < pattern.length()) {
                return false;
            }
            return reader.readOctetString(pattern.length()).equals(pattern);
        }
    }
}
