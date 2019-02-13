// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import java.util.HashSet;

/** A class for providing background context for use during analysis.
 * This includes:
 * - Mappings between hostnames and network addresses
 * - Recognition of notable host identifiers
 */
public class AnalysisContext {
    /** The resolver.
     * This must always be non-null.
     */
    private Resolver resolver = NullResolver.getInstance();

    /** The set of recognised host identifiers. */
    private final HashSet hostIdentifiers = new HashSet();

    /** Set the resolver to be used for mapping hostnames and addresses.
     * @param resolver the required resolver, or null for the null resolver
     */
    public final void setResolver(Resolver resolver) {
        if (resolver == null) {
            resolver = NullResolver.getInstance();
        }
        this.resolver = resolver;
    }

    /** Get the resolver to be used for mapping hostnames and addresses.
     * @return the resolver
     */
    public final Resolver getResolver() {
        return resolver;
    }

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
