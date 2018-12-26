// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import javax.json.JsonObject;

/** An OctetPattern class to match an arbitrary octet sequence. */
public class WildcardOctetPattern extends OctetPattern {
    /** The required integer length, in octets. */
    private final int length;

    /** Parse WildcardOctetPattern from a specification in JSON format.
     * @param jsonSpec the specification to be parsed
     */
    public WildcardOctetPattern(JsonObject jsonSpec) {
        this.length = jsonSpec.getInt("length");
        if (this.length < 0) {
            throw new RuntimeException(
                "invalid length for WildcardOctetPattern");
        }
    }

    @Override
    public final boolean matches(OctetReader reader) {
        if (reader.remaining() < length) {
            return false;
        }
        reader.skip(length);
        return true;
    }
}
