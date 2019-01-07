// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.tcp;

/** A class to represent a TCP SACK permitted option. */
public class TcpSackPermittedOption extends TcpOption {
    /** Parse TCP SACK permitted option from a generic option.
     * @param option the generic option to be parsed
     * @throws ParseException if the octet sequence cannot be parsed
     */
    public TcpSackPermittedOption(TcpOption option) {
        super(option);
    }
}
