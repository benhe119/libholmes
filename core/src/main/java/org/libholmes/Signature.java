// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Collections;

import javax.json.JsonValue;
import javax.json.JsonString;
import javax.json.JsonObject;
import javax.json.JsonArray;

/** A class which associates a set of fingerprints with a source. */
public class Signature extends Fingerprint {
    /** The unique ID of this signature. */
    private final String id;

    /** The unique IDs of any signatures which match a subset of the
     * features of this one.
     */
    private final List<String> exclude;

    /** A list of fingerprints to be checked. */
    private final ArrayList<Fingerprint> fingerprints =
        new ArrayList<Fingerprint>();

    /** Construct signature from JSON.
     * @param jsonSpec the signature, as JSON
     */
    public Signature(JsonObject jsonSpec) {
        this.id = jsonSpec.getString("_id");

        ArrayList<String> exclude = new ArrayList<String>();
        JsonValue jsonExcludeSpec = jsonSpec.get("exclude");
        if (jsonExcludeSpec == null) {
            // No action.
        } else if (jsonExcludeSpec instanceof JsonString) {
            String excludeId = ((JsonString) jsonExcludeSpec).getString();
            exclude.add(excludeId);
        } else if (jsonExcludeSpec instanceof JsonArray) {
            for (JsonValue jsonExcludeId : ((JsonArray) jsonExcludeSpec)) {
                String excludeId = ((JsonString) jsonExcludeId).getString();
                exclude.add(excludeId);
            }
        } else {
            throw new IllegalArgumentException(
                "signature exclude attribute must be JSON list or string");
        }

        this.exclude = Collections.unmodifiableList(exclude);

        for (Map.Entry<String, JsonValue> entry : jsonSpec.entrySet()) {
            if (entry.getValue() instanceof JsonObject) {
                Fingerprint fingerprint = Fingerprint.parse(
                    entry.getKey(), (JsonObject) entry.getValue());
                fingerprints.add(fingerprint);
            } else if (entry.getKey().equals("_id")) {
                // No action
            } else if (entry.getKey().equals("exclude")) {
                // No action
            } else if (entry.getKey().equals("description")) {
                // No action
            } else if (entry.getKey().equals("attribution")) {
                // No action
            } else {
                throw new IllegalArgumentException(
                    "signature specification must be JSON object");
            }
        }
    }

    /** Get the unique ID of this signature.
     * @return the unique ID
     */
    public final String getId() {
        return id;
    }

    /** Get the unique IDs of any signatures which match a subset of the
     * features of this one.
     * @return the list of unique IDs
     */
    public final List<String> getExclude() {
        return exclude;
    }

    @Override
    public final boolean matches(Artefact artefact, AnalysisContext context) {
        for (Fingerprint fingerprint : fingerprints) {
            if (!fingerprint.matches(artefact, context)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public final Matcher createMatcher() {
        return new SignatureMatcher(fingerprints);
    }
}
