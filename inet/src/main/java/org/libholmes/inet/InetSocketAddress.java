// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.inet;

/** A class to represent an Internet Protocol socket address.
 * A socket address is the combination of a network address and a
 * port number.
 */
public class InetSocketAddress {
    /** The network address. */
    private final InetAddress address;

    /** The port number. */
    private final int port;

    /** Construct InetSocketAddress from an InetAddress and a port number.
     * @param address the required network address
     * @param port the required port number
     */
    public InetSocketAddress(InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }

    /** Get the network address.
     * @return the network address
     */
    public final InetAddress getAddress() {
        return address;
    }

    /** Get the port number.
     * @return the port number
     */
    public final int getPort() {
        return port;
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
            InetSocketAddress that = (InetSocketAddress) thatObject;
            return (this.address.equals(that.address)) &&
                (this.port == that.port);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return address.hashCode() * 31 + port;
    }

    @Override
    public String toString() {
        return String.format("%s:%d", address.toString(), port);
    }
}
