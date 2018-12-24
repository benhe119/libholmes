// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import javax.json.JsonValue;
import javax.json.JsonObject;
import javax.json.JsonArray;

/** An abstract base class to represent a pattern for matching against a
 * sequence of octets.
 */
public abstract class OctetPattern {
    /** Determine whether this pattern matches a given sequence of octets.
     * @param octets the octet sequence to be matched
     * @return true if the whole octet sequence matched, otherwise false
     */
    public abstract boolean matches(OctetReader octets);

    /** Parse OctetPattern from a specification in JSON format.
     * @param jsonSpec the specification to be parsed
     * @return the resulting OctetPattern
     */
    public static OctetPattern parse(JsonValue jsonSpec) {
        throw new IllegalArgumentException(
            "invalid specification for OctetPattern");
    }
}
