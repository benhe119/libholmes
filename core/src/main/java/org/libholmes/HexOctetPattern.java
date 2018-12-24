// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import javax.json.JsonObject;

/** An OctetPattern class to represent a literal hex pattern. */
public class HexOctetPattern extends OctetPattern {
    /** The pattern to be matched. */
    private final HexOctetString pattern;

    /** Parse HexOctetPattern from a specification in JSON format.
     * @param jsonSpec the specification to be parsed
     */
    public HexOctetPattern(JsonObject jsonSpec) {
        pattern = new HexOctetString(jsonSpec.getString("content"));
    }

    @Override
    public final boolean matches(OctetReader reader) {
        if (reader.remaining() < pattern.length()) {
            return false;
        }
        return reader.readOctetString(pattern.length()).equals(pattern);
    }
}
