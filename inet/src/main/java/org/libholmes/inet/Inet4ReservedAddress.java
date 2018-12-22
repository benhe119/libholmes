// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.inet;

import org.libholmes.ParseException;

/** A class to represent a reserved IPv4 address. */
class Inet4ReservedAddress extends Inet4Address {
    /** Construct Inet4ReservedAddress from a generic Inet4Address.
     * @param address the generic address
     */
    Inet4ReservedAddress(Inet4Address address) throws ParseException {
        super(address);
    }

    @Override
    public int getFlags() {
        return 0;
    }
}
