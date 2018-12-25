// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import java.util.ArrayList;
import javax.json.JsonValue;
import javax.json.JsonArray;

/** An OctetPattern class to represent a sequence of patterns. */
public class SequenceOctetPattern extends OctetPattern {
    /** The sequence of patterns to be matched. */
    private final ArrayList<OctetPattern> patterns =
        new ArrayList<OctetPattern>();

    /** Parse SequenceOctetPattern from a specification in JSON format.
     * @param jsonSpec the specification to be parsed
     */
    public SequenceOctetPattern(JsonArray jsonSpec) {
        for (JsonValue jsonPattern : jsonSpec) {
            OctetPattern pattern = OctetPattern.parse(jsonPattern);
            patterns.add(pattern);
        }
    }

    @Override
    public final boolean matches(OctetReader reader) {
        for (OctetPattern pattern : patterns) {
            if (!pattern.matches(reader)) {
                return false;
            }
        }
        return true;
    }
}
