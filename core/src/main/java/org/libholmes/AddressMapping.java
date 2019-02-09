// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

/** A class to represent a mapping between a hostname and an address.
 * In addition to the items mapped, each mapping is characterised by a
 * direction and interval of validity. Two measures are given for the latter:
 * the interval for which there is observed evidence, and the interval for
 * which there is no contradictory evidence.
 *
 * Forward mappings translate a hostname into an address. Reverse mappings
 * translate an address into a hostname. Either of these is evidence of an
 * association, however the direction will affect the extent to which the
 * mapping can be trusted.
 *
 * Bidirectional mappings are permitted, but are expected to be of limited
 * utility due to the likelihood that the forward and reverse mappings have
 * different periods of validity. No distinction should be made between a
 * bidirectional mapping, and a pair of matching forward and reverse
 * mappings.
 */
public class AddressMapping {
    /** The mapped hostname. */
    private final String hostname;

    /** The mapped address. */
    private final Address address;

    /** True if this mapping operates in the forward direction,
     * otherwise false. */
    private final boolean forward;

    /** True if this mapping operates in the reverse direction,
     * otherwise false. */
    private final boolean reverse;

    /** The observed interval of validity. */
    private final Interval<Long> observedValidity;

    /** The potential interval of validity. */
    private final Interval<Long> potentialValidity;

    /** Construct address mapping.
     * @param hostname the mapped hostname
     * @param address the mapped address
     * @param forward true if operative in forward direction, otherwise false
     * @param reverse true if operative in reverse direction, otherwise false
     * @param observedValidity the observed interval of validity
     * @param potentialValidity the potential interval of validity
     */
    public AddressMapping(String hostname, Address address,
        boolean forward, boolean reverse,
        Interval<Long> observedValidity, Interval<Long> potentialValidity) {

        this.hostname = hostname;
        this.address = address;
        this.forward = forward;
        this.reverse = reverse;
        this.observedValidity = observedValidity;
        this.potentialValidity = potentialValidity;
    }

    /** Get the mapped hostname.
     * @return the mapped hostname
     */
    public final String getHostname() {
        return hostname;
    }

    /** Get the mapped address.
     * @return the mapped address
     */
    public final Address getAddress() {
        return address;
    }

    /** Determine whether this mapping operates in the forward direction.
     * @return true if operative in forward direction, otherwise false
     */
    public final boolean isForward() {
        return forward;
    }

    /** Determine whether this mapping operates in the reverse direction.
     * @return true if operative in reverse direction, otherwise false
     */
    public final boolean isReverse() {
        return reverse;
    }

    /** Get the observed interval of validity.
     * This is the period covered by the observed evidence of validity.
     * @return the observed interval of validity
     */
    public final Interval<Long> getObservedValidity() {
        return observedValidity;
    }

    /** Get the potential interval of validity.
     * This is the period for which there is no evidence of invalidity.
     * @return the potential interval of validity
     */
    public final Interval<Long> getPotentialValidity() {
        return potentialValidity;
    }
}
