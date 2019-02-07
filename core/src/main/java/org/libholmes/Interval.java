// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

/** An abstract base class to represent an interval between two values. */
public abstract class Interval<T extends Comparable<T>> {
    /** Get the left endpoint of the interval.
     * @return the endpoint, or null if left-unbounded
     */
    public abstract T getLeftEndpoint();

    /** Get the right endpoint of the interval.
     * @return the endpoint, or null if right-unbounded
     */
    public abstract T getRightEndpoint();

    /** Determine whether the left endpoint is inclusive
     * @return true if endpoint inclusive, false if exclusive
     */
    public abstract boolean isLeftOpen();

    /** Determine whether the right endpoint is inclusive
     * @return true if endpoint inclusive, false if exclusive
     */
    public abstract boolean isRightOpen();

    /** Determine whether the left endpoint is exclusive
     * @return true if endpoint exclusive, false if inclusive
     */
    public abstract boolean isLeftClosed();

    /** Determine whether the right endpoint is exclusive
     * @return true if endpoint exclusive, false if inclusive
     */
    public abstract boolean isRightClosed();

    /** Determine whether a given value falls within the interval.
     * @param value the value to be tested
     * @return true if inside the interval, otherwise false
     */
    public abstract boolean contains(T value);
}
