// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

/** An interface for recording findings resulting from the analysis of an
 * artefact.
 * A finding is an observation concerning an artefact which is forensically
 * interesting in some way.
 */
public interface Logger {
    /** Log a finding.
     * @param description a format string containing a textual description
        of the finding
     * @param args a list of arguments for substitution in the description
     */
    void log(String description, Object... args);
}
