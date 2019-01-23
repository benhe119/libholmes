// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.inet;

/** A class to represent the IPv6 unspecified address. */
class Inet6UnspecifiedAddress extends Inet6Address {
    /** Construct Inet6UnspecifiedAddress from a generic Inet6Address.
     * @param address the generic address
     */
    Inet6UnspecifiedAddress(Inet6Address address) {
        super(address);
    }

    @Override
    public int getFlags() {
        return FLAG_WILDCARD;
    }
}
