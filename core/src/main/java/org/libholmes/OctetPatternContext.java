// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import java.util.HashSet;

/** A class for providing information to context-dependent patterns. */
public class OctetPatternContext {
    /** The set of recognised host identifiers. */
    private final HashSet hostIdentifiers = new HashSet();

    /** Add a recognised host identifier.
     * This would typically be a domain name or a network address,
     * however there is no technical constraint on the types allowed.
     * @param identifier the identifier to be added
     */
    public final void addHostIdentifier(Object identifier) {
        hostIdentifiers.add(identifier);
    }

    /** Test whether an identifier is a recognised host identifier.
     * @param identifier the identifier to be tested
     * @return true if it is recognised, otherwise false
     */
    public final boolean isHostIdentifier(Object identifier) {
        return hostIdentifiers.contains(identifier);
    }
}
