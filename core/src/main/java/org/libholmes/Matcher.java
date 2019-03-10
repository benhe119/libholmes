// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

/** An abstract base class for matching a sequence of artefacts against
 * part or all of a fingerprint. */
public abstract class Matcher {
    /** Append an artefact to the sequence under consideration.
     * An artefact must not be added without first checking that it is
     * capable of matching the fingerprint in isolation, because this
     * method does not necessarily repeat any validation steps that are
     * performed by the match method. Furthermore, this should be done
     * for all relevant matchers before adding to any of them, since the
     * latter cannot be undone.
     * @param artefact the artefact to be matched
     * @param context information for context-dependent fingerprints
     */
    public abstract void add(Artefact artefact, AnalysisContext context);

    /** Determine whether an individual artefact matches.
     * @param artefact the artefact to be matched
     * @param context information for context-dependent fingerprints
     * @return true if matched, otherwise false
     */
    public abstract boolean match(Artefact artefact, AnalysisContext context);

    /** Determine whether the sequence as a whole matches.
     * @return true if matched, otherwise false
     */
    public abstract boolean matchAll();
}
