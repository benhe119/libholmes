// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.inet;

import org.libholmes.OctetReader;
import org.libholmes.OctetString;
import org.libholmes.Address;
import org.libholmes.ParseException;

/** A class to represent an IPv4 loopback address. */
class Inet4LoopbackAddress extends Inet4Address {
    /** Construct Inet4LoopbackAddress from a generic Inet4Address.
     * @param address the generic address
     */
    Inet4LoopbackAddress(Inet4Address address) throws ParseException {
        super(address);
    }

    @Override
    public int getFlags() {
        return FLAG_LOOPBACK;
    }
}
