// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/** A resolver class which returns no mappings.
 * This is intended for use as a fallback resolver when no other one has been
 * specified.
 */
public class NullResolver implements Resolver {
    /** An unmodifiable, empty list of address mappings. */
    private final List<AddressMapping> empty = Collections.unmodifiableList(
        new ArrayList<AddressMapping>());

    @Override
    public List<AddressMapping> find(String hostname, long when) {
        return empty;
    }

    @Override
    public List<AddressMapping> find(Address address, long when) {
        return empty;
    }

    /** An instance of this class. */
    private static final NullResolver instance = new NullResolver();

    /** Get an instance of this class.
     * @return the instance
     */
    public static NullResolver getInstance() {
        return instance;
    }
}
