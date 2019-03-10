// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.icmp;

import org.libholmes.AnalysisContext;
import org.libholmes.Artefact;
import org.libholmes.Matcher;

public class Icmp4EchoMatcher extends Matcher {
    /** The matcher to be applied to the identifier field, or null if none. */
    private final Matcher identMatcher;

    /** The matcher to be applied to the sequence number field,
     * or null if none. */
    private final Matcher snMatcher;

    /** Construct matcher for ICMPv4 echo request.
     * @param identMatcher the matcher to be applied to the indentifier field
     * @param identMatcher the matcher to be applied to the sequence number
     *  field
     */
    public Icmp4EchoMatcher(Matcher identMatcher, Matcher snMatcher) {
        this.identMatcher = identMatcher;
        this.snMatcher = snMatcher;
    }

    @Override
    public final void add(Artefact artefact, AnalysisContext context) {
        if (identMatcher != null) {
            identMatcher.add(artefact, context);
        }
        if (snMatcher != null) {
            snMatcher.add(artefact, context);
        }
    }

    @Override
    public final boolean match(Artefact artefact, AnalysisContext context) {
        boolean result = true;
        if (identMatcher != null) {
            result &= identMatcher.match(artefact, context);
        }
        if (snMatcher != null) {
            result &= snMatcher.match(artefact, context);
        }
        return result;
    }

    @Override
    public final boolean matchAll() {
        boolean result = true;
        if (identMatcher != null) {
            result &= identMatcher.matchAll();
        }
        if (snMatcher != null) {
            result &= snMatcher.matchAll();
        }
        return result;
    }
}
