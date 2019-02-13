// This file is part of libholmes.
// Copyright 2018-2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import java.nio.charset.Charset;
import javax.json.JsonObject;

/** An OctetPattern class to represent a literal sequence of characters. */
public class TextOctetPattern extends OctetPattern {
    /** The pattern to be matched. */
    private final OctetString pattern;

    /** Parse TextOctetPattern from a specification in JSON format.
     * @param jsonSpec the specification to be parsed
     */
    public TextOctetPattern(JsonObject jsonSpec) {
        String text = jsonSpec.getString("content");
        String charsetName = jsonSpec.getString("encoding", "UTF-8");
        Charset charset = Charset.forName(charsetName);
        byte[] bytes = text.getBytes(charset);
        pattern = new ArrayOctetString(bytes, OctetString.BIG_ENDIAN);
    }

    @Override
    public final boolean matches(OctetReader reader,
        AnalysisContext context) {

        if (reader.remaining() < pattern.length()) {
            return false;
        }
        return reader.readOctetString(pattern.length()).equals(pattern);
    }
}
