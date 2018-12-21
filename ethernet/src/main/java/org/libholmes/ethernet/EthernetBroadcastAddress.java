// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.ethernet;

/** A class to represent the Ethernet broadcast address. */
class EthernetBroadcastAddress extends EthernetAddress {
    /** Construct EthernetBroadcastAddress from a generic EthernetAddress.
     * @param address the generic address
     */
    EthernetBroadcastAddress(EthernetAddress address) {
        super(address);
    }

    @Override
    public int getFlags() {
        return FLAG_MULTICAST | FLAG_BROADCAST;
    }
}
