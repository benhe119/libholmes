// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.inet;

import org.libholmes.OctetReader;
import org.libholmes.Artefact;
import org.libholmes.ParseException;

/** A class to represent an unrecognised IPv4 option. */
public class Inet4UnrecognisedOption extends Inet4Option {
    /** Parse unrecognised IPv4 option from an OctetReader.
     * @param parent the parent artefact, or null if none
     * @param reader the octet source to be parsed
     * @throws ParseException if the octet sequence cannot be parsed
     */
    Inet4UnrecognisedOption(Artefact parent, OctetReader reader)
        throws ParseException {

        super(parent, reader);
    }

    @Override
    public final boolean reportMultipleInstances() {
        return false;
    }
}
