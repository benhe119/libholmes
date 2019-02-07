// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

/** A class to represent an closed interval between two values.
 * A closed interval is one which includes both endpoints.
 */
public class ClosedInterval<T extends Comparable<T>> extends Interval<T> {
    /** The left endpoint, or null if left-unbounded. */
    private final T leftEndpoint;

    /** The right endpoint, or null if right-unbounded. */
    private final T rightEndpoint;

    /** Construct closed interval.
     * @param leftEndpoint the left endpoint, or null if left-unbounded
     * @param rightEndpoint the right endpoint, or null if right-unbounded
     */
    public ClosedInterval(T leftEndpoint, T rightEndpoint) {
        this.leftEndpoint = leftEndpoint;
        this.rightEndpoint = rightEndpoint;
    }

    @Override
    public final T getLeftEndpoint() {
        return leftEndpoint;
    }

    @Override
    public final T getRightEndpoint() {
        return rightEndpoint;
    }

    @Override
    public final boolean isLeftOpen() {
        return false;
    }

    @Override
    public final boolean isRightOpen() {
        return false;
    }

    @Override
    public final boolean isLeftClosed() {
        return true;
    }

    @Override
    public final boolean isRightClosed() {
        return true;
    }

    @Override
    public final boolean contains(T value) {
        if ((leftEndpoint != null) && (value.compareTo(leftEndpoint) < 0)) {
            return false;
        }
        if ((rightEndpoint != null) && (value.compareTo(rightEndpoint) > 0)) {
            return false;
        }
        return true;
    }
}
