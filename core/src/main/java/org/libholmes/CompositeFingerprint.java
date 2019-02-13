// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import java.util.ArrayList;
import java.util.Map;

import javax.json.JsonValue;
import javax.json.JsonObject;

/** A class to represent a fingerprint able to cover multiple protocols.
 * The composite fingerprint matches if and only if all of the fingerprints
 * for the individual protocols also match.
 */
public class CompositeFingerprint extends Fingerprint {
    /** The unique ID of this fingerprint. */
    private final String id;

    /** A list of fingerprints to be checked. */
    private final ArrayList<Fingerprint> fingerprints =
        new ArrayList<Fingerprint>();

    /** Construct composite fingerprint from JSON.
     * @param jsonSpec the fingerprint, as JSON
     */
    public CompositeFingerprint(JsonObject jsonSpec) {
        id = jsonSpec.getString("_id");
        for (Map.Entry<String, JsonValue> entry : jsonSpec.entrySet()) {
            if (entry.getValue() instanceof JsonObject) {
                Fingerprint fingerprint = Fingerprint.parse(
                    entry.getKey(), (JsonObject) entry.getValue());
                fingerprints.add(fingerprint);
            } else if (!entry.getKey().equals("_id")) {
                throw new IllegalArgumentException(
                    "fingerprint specification must be JSON object");
            }
        }
    }

    /** Get the unique ID of this fingerprint.
     * @return the unique ID
     */
    public final String getId() {
        return id;
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
}
