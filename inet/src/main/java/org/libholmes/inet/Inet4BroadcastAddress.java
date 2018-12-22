// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.inet;

import org.libholmes.OctetReader;
import org.libholmes.OctetString;
import org.libholmes.Address;
import org.libholmes.ParseException;

/** A class to represent the IPv4 broadcast address. */
class Inet4BroadcastAddress extends Inet4Address {
    /** Construct Inet4BroadcastAddress from a generic Inet4Address.
     * @param address the generic address
     */
    Inet4BroadcastAddress(Inet4Address address) throws ParseException {
        super(address);
    }

    @Override
    public int getFlags() {
        return FLAG_MULTICAST | FLAG_BROADCAST;
    }
}
