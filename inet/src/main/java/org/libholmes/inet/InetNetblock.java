// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.inet;

import org.libholmes.OctetReader;
import org.libholmes.OctetString;
import org.libholmes.Address;
import org.libholmes.ParseException;

/** A class to represent an Internet Protocol netblock. */
public class InetNetblock {
    /** The prefix. */
    private final InetAddress prefix;

    /** The prefix length, in bits. */
    private final int prefixLength;

    /** Construct Inet4Netblock for given prefix and prefix length.
     * @param prefix the required prefix
     * @param prefixLength the required prefix length
     */
    public InetNetblock(InetAddress prefix, int prefixLength) {
        if (!prefix.equals(prefix.getNetworkAddress(prefixLength))) {
            throw new IllegalArgumentException(
                "Netblock size incompatible with prefix");
        }
        this.prefix = prefix;
        this.prefixLength = prefixLength;
    }

    /** Get the prefix for this netblock.
     * @return the prefix
     */
    public final InetAddress getPrefix() {
        return prefix;
    }

    /** Get the prefix length for this netblock.
     * @return the prefix length, in bits
     */
    public final int getPrefixLength() {
        return prefixLength;
    }

    /** Determine whether a given address is contained within this netblock.
     * It is not an error if the address to be tested is of a different family
     * to the nextblock prefix, nor will the result necessarily always be
     * false if this is the case, however the current implementation does not
     * support any mappings between address families.
     * @param address the address to be tested.
     * @return true if within netblock, otherwise false
     */
    public final boolean contains(InetAddress address) {
        return prefix.equals(address.getNetworkAddress(prefixLength));
    }

    /** Parse an InetNetblock from a String using CIDR prefix notation.
     * @param netblock the string to be parsed
     */
    public static InetNetblock parse(String netblock) throws ParseException {
        int f = netblock.indexOf('/');
        if (f == -1) {
            throw new ParseException("Prefix length missing from netblock");
        }

        int prefixLength = Integer.parseInt(netblock.substring(f + 1));
        InetAddress prefix = InetAddress.parse(netblock.substring(0, f));
        try {
            return new InetNetblock(prefix, prefixLength);
        } catch (IllegalArgumentException ex) {
            throw new ParseException(ex.getMessage());
        }
    }
}
