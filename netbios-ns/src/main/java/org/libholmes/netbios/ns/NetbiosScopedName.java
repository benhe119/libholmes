// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.netbios.ns;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.libholmes.OctetReader;
import org.libholmes.Artefact;
import org.libholmes.ParseException;

import org.libholmes.dns.DnsDomainName;

/** A class to represent an NetBIOS name with a scope ID. */
public class NetbiosScopedName extends DnsDomainName {
    /** The decoded NetBIOS name. */
    private final String netbiosName;

    /** Parse scoped NetBIOS name from OctetReader.
     * @param reader the OctetReader to be parsed
     * @param ptrReader an OctetReader positioned at the start of the message
     * @throws ParseException if the octet sequence could not be parsed
     */
    public NetbiosScopedName(OctetReader reader, OctetReader ptrReader)
        throws ParseException {

        super(reader, ptrReader);

        String encodedName = getLabel(0).toString();
        int length = encodedName.length();
        if (length % 2 != 0) {
            throw new ParseException(
                "Odd number of bytes in encoded NetBIOS name");
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i != length; i += 2) {
            char c0 = encodedName.charAt(i + 0);
            char c1 = encodedName.charAt(i + 1);
            if ((c0 < 'A') || (c0 > 'P') || (c1 < 'A') || (c1 > 'P')) {
                throw new ParseException(
                    "Invalid character in encoded NetBIOS name");
            }
            int c = (c0 - 'A') & 0xff;
            c <<= 4;
            c |= (c1 - 'A') & 0xff;
            builder.append((char) c);
        }
        netbiosName = builder.toString();
    }

    /** Get decoded NetBIOS name.
     * @return the decoded NetBIOS name
     */
    public final String getNetbiosName() {
        return netbiosName;
    }

    /** Get scope ID.
     * @return the scope ID
     */
    public final String getScopeId() {
        // RFCs 1001 and 1002 are not completely clear as to whether the
        // scope ID can or should include leading/trailing dots, however the
        // examples show it without either.
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (int i = 1, n = getLabelCount(); i < n; ++i) {
            if (first) {
                first = false;
            } else {
                builder.append(".");
            }
            getLabel(i).buildString(builder);
        }
        if (builder.length() != 0) {
            if (builder.charAt(builder.length() - 1) == '.') {
                builder.deleteCharAt(builder.length() - 1);
            }
        }
        return builder.toString();
    }
}
