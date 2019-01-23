// This file is part of libholmes.
// Copyright 2018-2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.inet;

import org.libholmes.OctetString;
import org.libholmes.Address;
import org.libholmes.ParseException;

/** An abstract base class to represent an Internet Protocol address. */
public abstract class InetAddress extends Address {
    /** Copy-construct an InetAddress from another InetAddress.
     * @param that the InetAddress to be copied
     */
    protected InetAddress(InetAddress that) {
        super(that);
    }

    /** Construct InetAddress from an OctetString.
     * @param content the required content
     */
    protected InetAddress(OctetString content) {
        super(content);
    }

    /** Get the network address of a subnet containing this address.
     * @param prefixLength the required prefix length, in bits
     * @return the network address
     */
    public abstract InetAddress getNetworkAddress(int prefixLength);

    /** Get the broadcast address of a subnet containing this address.
     * @param prefixLength the required prefix length, in bits
     * @return the broadcast address
     */
    public abstract InetAddress getBroadcastAddress(int prefixLength);

    /** Parse InetAddress from a String.
     * IPv4 addresses must be in dotted quad format.
     * @param addrStr the address as a character string
     */
    public static InetAddress parse(String addrStr) throws ParseException {
        // The regular expression patterns below are intended to determine
        // detailed parsing should be attempted. A match does not necessarily
        // indicate that the address is valid.
        if (addrStr.matches("^[0-9]{1,3}[.][0-9]{1,3}[.][0-9]{1,3}[.][0-9]{1,3}$")) {
            return Inet4Address.parse(addrStr);
        } else if (addrStr.matches("([0-9A-Fa-f]{0,4}:){2,7}([0-9A-Fa-f]{0,4}|[0-9]{1,3}[.][0-9]{1,3}[.][0-9]{1,3}[.][0-9]{1,3})")) {
            return Inet6Address.parse(addrStr);
        } else {
            throw new ParseException("IP address string format not recognised");
        }
    }
}
