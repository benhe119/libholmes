// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import java.util.List;

/** An interface for resolving hostnames and addresses.
 * The method used to resolve the hostname or address is unspecified, and may
 * vary between implementations of this interface. Note in particular that:
 *
 * - It is not required to perform any live queries of the DNS.
 * - If it does perform live queries, it is not required to limit their
 *   number by caching.
 * - It is not required to provide full coverage of all hostnames or all
 *   addresses (even within a given address family).
 * - It is not required to give repeatable results.
 *
 * Because coverage may be limited, an empty result list must not be
 * interpreted as evidence that the name or address in question had no
 * mapping. If an implementation is able to positively confirm the absence
 * of a mapping, it should do so by reporting a mapping to null.
 *
 * There will typically be significant uncertainty regarding the point in time
 * at which mappings become or cease to be valid. Furthermore, it will often
 * be the case that the requested point in time post-dates all relevant
 * observations. For these reasons, the implementation should return all
 * mappings which could plausibly have been valid at the requested point in
 * time, even if they suggest contradictory conclusions.
 *
 * All relevant forward and reverse mappings are returned, regardless of
 * whether it was an address or a hostname that was being searched for.
 */
public interface Resolver {
    /** Find the addresses corresponding to a given hostname.
     * @param hostname the hostname for which to search
     * @param when the point in time for which to search
     * @return a list of name-address mappings found
     */
    List<AddressMapping> find(String hostname, long when);

    /** Find the hostnames corresponding to a given address.
     * @param address the address for which to search
     * @param when the point in time for which to search
     * @return a list of name-address mappings found
     */
    List<AddressMapping> find(Address address, long when);
}
