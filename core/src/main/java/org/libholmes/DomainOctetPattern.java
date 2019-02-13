// This file is part of libholmes.
// Copyright 2018-2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import java.nio.charset.Charset;
import java.util.List;
import java.util.ArrayList;

import javax.json.JsonObject;

/** An OctetPattern class to represent a known domain name. */
public class DomainOctetPattern extends OctetPattern {
    /** Parse DomainOctetPattern from a specification in JSON format.
     * @param jsonSpec the specification to be parsed
     */
    public DomainOctetPattern(JsonObject jsonSpec) {}

    @Override
    public final boolean matches(OctetReader reader,
        AnalysisContext context) {

        int index = 0;
        int limit = reader.remaining();
        int labels = 1;

        StringBuilder domainBuilder = new StringBuilder();
        while (index < limit) {
            char c = (char)reader.peekByte(index);
            if (((c >= '0') && (c <= '9')) ||
                ((c >= 'A') && (c <= 'Z')) ||
                ((c >= 'a') && (c <= 'z')) ||
                (c == '-')) {

                domainBuilder.append(c);
            } else if (c == '.') {
                domainBuilder.append(c);
                labels += 1;
            } else {
                return false;
            }
            index += 1;

            if (labels > 1) {
                if (context.isHostIdentifier(domainBuilder.toString())) {
                    reader.skip(index);
                    return true;
                }
            }
        }
        return false;
    }
}
