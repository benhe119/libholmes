// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.inet;

import org.libholmes.OctetReader;
import org.libholmes.ParseException;

/** A class to represent an IPv4 record route option. */
public class Inet4RecordRouteOption extends Inet4RouteOption {
    /** Parse IPv4 record route option from a generic option.
     * @param option the generic option to be parsed
     * @throws ParseException if the octet sequence cannot be parsed
     */
    public Inet4RecordRouteOption(Inet4Option option) throws ParseException {
        super(option);
    }
}
