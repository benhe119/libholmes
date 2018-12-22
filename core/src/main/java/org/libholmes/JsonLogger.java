// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonArray;
import javax.json.JsonObjectBuilder;
import javax.json.JsonArrayBuilder;

/** A class for recording findings resulting from the analysis of an
 * artefact in JSON format.
 */
public class JsonLogger implements Logger {
    /** The logged findings. */
    private JsonArrayBuilder logBuilder = Json.createArrayBuilder();

    @Override
    public final void log(String description, Object... args) {
        JsonObjectBuilder findingBuilder = Json.createObjectBuilder();
        findingBuilder.add("description", String.format(description, args));
        logBuilder.add(findingBuilder.build());
    }

    /** Return the logged findings as a JSON array.
     * @return the logged findings
     */
    public final JsonArray build() {
        return logBuilder.build();
    }
}
