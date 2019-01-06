// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.tcp;

import org.libholmes.OctetReader;
import org.libholmes.Artefact;
import org.libholmes.ParseException;

/** A class to represent an unrecognised TCP option. */
public class TcpUnrecognisedOption extends TcpOption {
    /** Parse unrecognised TCP option from an OctetReader.
     * @param parent the parent artefact, or null if none
     * @param reader the octet source to be parsed
     * @throws ParseException if the octet sequence cannot be parsed
     */
    TcpUnrecognisedOption(Artefact parent, OctetReader reader)
        throws ParseException {

        super(parent, reader);
    }
}
