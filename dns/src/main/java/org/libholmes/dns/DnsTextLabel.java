// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.dns;

import java.util.ArrayList;
import java.util.HashSet;

import org.libholmes.OctetReader;
import org.libholmes.OctetString;

/** A class to represent a domain name label composed of a character string.
 */
public class DnsTextLabel extends DnsLabel {
    /** The undecoded (and therefore unescaped) content of this label.
     * This does not include the length field.
     */
    OctetString content;

    /** Parse text label from OctetReader.
     * @param reader the OctetReader to be parsed
     */
    DnsTextLabel(OctetReader reader) {
        OctetReader labelReader = reader;
        int length = labelReader.readByte() & 0x3f;
        this.content = labelReader.readOctetString(length);
    }

    @Override
    public final int length() {
        return content.length() + 1;
    }

    @Override
    public final boolean isFinal() {
        return content.length() == 0;
    }

    @Override
    public final boolean equals(Object thatObject) {
        if (thatObject == this) {
            return true;
        }
        if (thatObject == null) {
            return false;
        }
        if (thatObject instanceof DnsTextLabel) {
            DnsTextLabel that = (DnsTextLabel) thatObject;
            return this.content.equals(that.content);
        } else {
            return false;
        }
    }

    @Override
    public final int hashCode() {
        return content.hashCode();
    }

    @Override
    public final void buildString(StringBuilder builder) {
        for (int i = 0, n = content.length(); i != n; ++i) {
            int b = content.getByte(i) & 0xff;
            if ((b >= 0x21) && (b < 0x7f)) {
                if ((b == 0x22) || (b == 0x24) || (b == 0x28) ||
                    (b == 0x29) || (b == 0x2e) || (b == 0x3b) ||
                    (b == 0x40) || (b == 0x5c)) {
                    builder.append("\\");
                }
                builder.append((char) b);
            } else {
                builder.append(String.format("\\%03d", b));
            }
        }
    }
}
