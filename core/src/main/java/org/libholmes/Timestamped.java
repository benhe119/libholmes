// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

/** A interface for objects with a timestamp. */
public interface Timestamped {
    /** Get the timestamp.
     * This is expressed as a number of nanoseconds since the epoch.
     * Timestamps using different units are converted.
     * @return the timestamp
     */
    long getTimestamp();
}
