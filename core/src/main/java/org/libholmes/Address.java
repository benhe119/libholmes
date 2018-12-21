// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

/** An abstract base class to represent an address, at any layer of the
 * network stack.
 */
public abstract class Address {
    /** A flag to indicate that an address is a multicast address.
     * Broadcast addresses qualify as multicast.
     */
    public static final int FLAG_MULTICAST = 0x0001;

    /** A flag to indicate that an address is a broadcast address. */
    public static final int FLAG_BROADCAST = 0x0002;

    /** A flag to indicate that an address is a wildcard address. */
    public static final int FLAG_WILDCARD = 0x0004;

    /** A flag to indicate that an address is a loopback address. */
    public static final int FLAG_LOOPBACK = 0x0008;

    /** The raw content of this address. */
    protected final OctetString content;

    /** Copy-construct an Address from an Address.
     * @param that the Address to be copied
     */
    protected Address(Address that) {
        this.content = that.content;
    }

    /** Construct address from an OctetString.
     * @param content the required content
     */
    public Address(OctetString content) {
        this.content = content;
    }

    /** Get content.
     * @return the content of this address
     */
    public final OctetString getAddress() {
        return content;
    }

    /** Get length.
     * @return the length of this address, in octets
     */
    public final int length() {
        return content.length();
    }

    /** Get the flags applicable to this address.
     * @return the bitwise OR of any applicable FLAG constants
     */
    protected abstract int getFlags();

    /** Test whether this is a multicast address.
     * Broadcast addresses are classified as multicast.
     * @return true if multicast, otherwise false
     */
    public final boolean isMulticastAddress() {
        return (getFlags() & FLAG_MULTICAST) != 0;
    }

    /** Test whether this is a broadcast address.
     * @return true if broadcast, otherwise false
     */
    public final boolean isBroadcastAddress() {
        return (getFlags() & FLAG_BROADCAST) != 0;
    }

    /** Test whether this is a wildcard address.
     * @return true if wildcard, otherwise false
     */
    public final boolean isWildcardAddress() {
        return (getFlags() & FLAG_WILDCARD) != 0;
    }

    /** Test whether this is a loopback address.
     * @return true if loopback, otherwise false
     */
    public final boolean isLoopbackAddress() {
        return (getFlags() & FLAG_LOOPBACK) != 0;
    }

    @Override
    public boolean equals(Object thatObject) {
        if (thatObject == this) {
            return true;
        }
        if (thatObject == null) {
            return false;
        }
        // Require an exact match between the address types.
        if (thatObject.getClass() == this.getClass()) {
            Address that = (Address) thatObject;
            return this.content.equals(that.content);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return content.hashCode();
    }
}
