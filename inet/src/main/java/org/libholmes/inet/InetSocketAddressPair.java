// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.inet;

/** A class to represent an ordered pair of InetSocketAddresses. */
public class InetSocketAddressPair {
    /** The source socket address. */
    private final InetSocketAddress srcAddr;

    /** The destination socket address. */
    private final InetSocketAddress dstAddr;

    /** Construct InetSocketAddressPair from two InetSocketAddresses.
     * @param srcAddr the required source socket address
     * @param dstAddr the required destination socket address
     */
    public InetSocketAddressPair(InetSocketAddress srcAddr,
        InetSocketAddress dstAddr) {

        this.srcAddr = srcAddr;
        this.dstAddr = dstAddr;
    }

    /** Get the source socket address.
     * @return the source socket address
     */
    public final InetSocketAddress getSrcAddr() {
        return srcAddr;
    }

    /** Get the destination socket address.
     * @return the destination socket address
     */
    public final InetSocketAddress getDstAddr() {
        return dstAddr;
    }

    /** Get a socket address pair with the addresses reversed.
     * @return the reversed socket address pair
     */
    public final InetSocketAddressPair reverse() {
        return new InetSocketAddressPair(dstAddr, srcAddr);
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
            InetSocketAddressPair that = (InetSocketAddressPair) thatObject;
            return (this.srcAddr.equals(that.srcAddr)) &&
                (this.dstAddr.equals(that.dstAddr));
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return srcAddr.hashCode() * 31 + dstAddr.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s->%s", srcAddr.toString(), dstAddr.toString());
    }
}
