// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.ethernet;

/** A class to represent the all-zeros Ethernet MAC address.
 * This address is reserved, and is commonly used as a placeholder for a
 * MAC address which is absent or unknown.
 */
class EthernetAllZerosAddress extends EthernetAddress {
    /** Construct EthernetBroadcastAddress from a generic EthernetAddress.
     * @param address the generic address
     */
    EthernetAllZerosAddress(EthernetAddress address) {
        super(address);
    }

    @Override
    public int getFlags() {
        return FLAG_WILDCARD;
    }
}
