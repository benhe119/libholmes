// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.icmp;

import org.libholmes.AnalysisContext;
import org.libholmes.Artefact;
import org.libholmes.Matcher;

/** A matcher class for fixed ICMPv4 echo request identifiers. */
public class Icmp4EchoFixedIdentifierMatcher extends Matcher {
    /** The identifer to be matched, or null if not yet fixed. */
    private Integer fixedIdentifier = null;

    @Override
    public final void add(Artefact artefact, AnalysisContext context) {
        Icmp4EchoMessage echo = (Icmp4EchoMessage) artefact;
        if (fixedIdentifier == null) {
            fixedIdentifier = echo.getIdentifier();
        }
    }

    @Override
    public final boolean match(Artefact artefact, AnalysisContext context) {
        if (!(artefact instanceof Icmp4EchoMessage)) {
            return false;
        }
        Icmp4EchoMessage echo = (Icmp4EchoMessage) artefact;

        if (fixedIdentifier != null) {
            if (echo.getIdentifier() != fixedIdentifier) {
                return false;
            }
        }
        return true;
    }

    @Override
    public final boolean matchAll() {
        return true;
    }
}
