// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.icmp;

import org.libholmes.OctetReader;
import org.libholmes.Artefact;
import org.libholmes.ParseException;

/** A base class to represent an unrecognised type of ICMPv4 message. */
public class Icmp4UnrecognisedMessage extends Icmp4Message {
   /** Parse unrecognised type of ICMPv4 message from an OctetReader.
     * @param parent the parent artefact, or null if none
     * @param reader the OctetReader to be parsed
     * @throws ParseException if the octet sequence cannot be parsed
     */
    Icmp4UnrecognisedMessage(Artefact parent, OctetReader reader)
        throws ParseException {

        super(parent, reader);
    }
}
