// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.inet;

/** A class to represent an IPv4 End of Options List marker. */
public class Inet4EndOfOptionsList extends Inet4Option {
    /** Parse IPv4 End of Options List marker from a generic option.
     * @param option the generic option to be parsed
     * @throws ParseException if the octet sequence cannot be parsed
     */
    public Inet4EndOfOptionsList(Inet4Option option) {
        super(option);
    }

    @Override
    public final boolean reportMultipleInstances() {
        // The issue of multiple instances should never arise, since the
        // first instance of the EOOL marker terminates the options list.
        return true;
    }
}
