// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.inet;

import javax.json.JsonObject;

import org.libholmes.OctetReader;
import org.libholmes.OctetPattern;
import org.libholmes.OctetPatternContext;

/** An OctetPattern class to match an IP address. */
public class InetAddressOctetPattern extends OctetPattern {
    /** Parse AddressOctetPattern from a specification in JSON format.
     * @param jsonSpec the specification to be parsed
     */
    public InetAddressOctetPattern(JsonObject jsonSpec) {}

    @Override
    public final boolean matches(OctetReader reader,
        OctetPatternContext context) {

        if (reader.remaining() < 4) {
            return false;
        }
        Inet4Address addr = Inet4Address.parse(reader);
        return context.isHostIdentifier(addr);
    }
}
