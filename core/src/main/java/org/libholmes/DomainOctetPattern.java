// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
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
    public final boolean matches(OctetReader reader) {
        int index = 0;
        int limit = reader.remaining();
        int labels = 0;

        StringBuilder domainBuilder = new StringBuilder();
        while (index < limit) {
            if (labels != 0) {
                if (index < limit) {
                    char c = (char)reader.peekByte(index);
                    if (c == '.') {
                        index += 1;
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }

            StringBuilder labelBuilder = new StringBuilder();
            while (index < limit) {
                char c = (char)reader.peekByte(index);
                if (((c >= '0') && (c <= '9')) ||
                    ((c >= 'A') && (c <= 'Z')) ||
                    ((c >= 'a') && (c <= 'z')) ||
                    (c == '-')) {

                    labelBuilder.append(c);
                    index += 1;
                } else {
                    break;
                }
            }

            String label = labelBuilder.toString();
            if (label.length() == 0) {
                break;
            }

            if (domainBuilder.length() != 0) {
                domainBuilder.append(".");
            }
            domainBuilder.append(label);
            labels += 1;
        }
        if (labels < 2) {
            return false;
        }
        reader.skip(index);
        return true;
    }
}
