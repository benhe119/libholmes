// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.inet;

/** A class to represent an IPv6 multicast address. */
class Inet6MulticastAddress extends Inet6Address {
    /** Construct Inet6MulticastAddress from a generic Inet6Address.
     * @param address the generic address
     */
    Inet6MulticastAddress(Inet6Address address) {
        super(address);
    }

    @Override
    public int getFlags() {
        return FLAG_MULTICAST;
    }
}
