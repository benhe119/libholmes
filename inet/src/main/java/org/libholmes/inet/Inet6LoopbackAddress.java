// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.inet;

/** A class to represent the IPv6 loopback address. */
class Inet6LoopbackAddress extends Inet6Address {
    /** Construct Inet6LoopbackAddress from a generic Inet6Address.
     * @param address the generic address
     */
    Inet6LoopbackAddress(Inet6Address address) {
        super(address);
    }

    @Override
    public int getFlags() {
        return FLAG_LOOPBACK;
    }
}
