// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.inet;

/** A class to represent an IPv4 no-operation option. */
public class Inet4NoOperationOption extends Inet4Option {
    /** Parse IPv4 NOP option from a generic option.
     * @param option the generic option to be parsed
     * @throws ParseException if the octet sequence cannot be parsed
     */
    public Inet4NoOperationOption(Inet4Option option) {
        super(option);
    }

    @Override
    public boolean reportMultipleInstances() {
        // Multiple instances are commonplace.
        return false;
    }
}
