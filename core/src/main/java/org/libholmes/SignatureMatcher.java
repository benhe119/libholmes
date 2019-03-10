// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import java.util.List;
import java.util.ArrayList;

/** A matcher class for a signature. */
public class SignatureMatcher extends Matcher {
    /** The matcher objects for individual fingerprints. */
    private final ArrayList<Matcher> matchers = new ArrayList<Matcher>();

    /** Construct matcher object for signature.
     * @param the individual fingerprints to be matched
     */
    public SignatureMatcher(List<Fingerprint> fingerprints) {
        for (Fingerprint fingerprint : fingerprints) {
            Matcher matcher = fingerprint.createMatcher();
            if (matcher != null) {
                matchers.add(matcher);
            }
        }
    }

    @Override
    public final void add(Artefact artefact, AnalysisContext context) {
        for (Matcher matcher : matchers) {
            matcher.add(artefact, context);
        }
    }

    @Override
    public final boolean match(Artefact artefact, AnalysisContext context) {
        boolean result = true;
        for (Matcher matcher : matchers) {
            result &= matcher.match(artefact, context);
        }
        return result;
    }

    @Override
    public final boolean matchAll() {
        boolean result = true;
        for (Matcher matcher : matchers) {
            result &= matcher.matchAll();
        }
        return result;
    }
}
