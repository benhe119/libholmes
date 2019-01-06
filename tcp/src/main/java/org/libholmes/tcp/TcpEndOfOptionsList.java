// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.tcp;

/** A class to represent an TCP End of Options List marker. */
public class TcpEndOfOptionsList extends TcpOption {
    /** Parse TCP EOOL marker from a generic option.
     * @param option the generic option to be parsed
     * @throws ParseException if the octet sequence cannot be parsed
     */
    public TcpEndOfOptionsList(TcpOption option) {
        super(option);
    }
}
