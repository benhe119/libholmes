// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/** An abstract base class to represent a decoded artefact. */
public abstract class Artefact {
    /** The parent of this artefact.
     * @return the parent artefact, or null if none
     */
    private final Artefact parent;

    /** Construct artefact.
     * @param parent the parent of this artefact, or null if none
     */
    protected Artefact(Artefact parent) {
        this.parent = parent;
    }

    /** Get parent artefact.
     * @return the parent of this artefact, or null if none
     */
    public final Artefact getParent() {
        return parent;
    }

    /** Get ancestor artefaxct of given subclass.
     * @return the ancestor artefact
     */
    public final <T> T getAncestor(Class<T> type) {
        Artefact p = this.parent;
        while (p != null) {
            if (type.isInstance(p)) {
                return type.cast(p);
            }
            p = p.parent;
        }
        throw new ClassCastException("Artefact has no parent of requested type");
    }

    /** Build a JSON object for this option.
     * @param builder a JsonObjectBuilder for the object to be built
     */
    protected abstract void buildJson(JsonObjectBuilder builder);

    /** Get this artefact as a JSON object.
     * @return the JSON object
     */
    public final JsonObject toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        buildJson(builder);
        return builder.build();
    }

   /** Examine this artefact.
     * By default this method does nothing.
     * @param logger a logger for recording findings
     */
    public void examine(Logger logger) {}
}
