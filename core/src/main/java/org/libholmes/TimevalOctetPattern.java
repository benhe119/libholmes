// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import javax.json.JsonObject;

/** An OctetPattern class to match a timeval structure. */
public class TimevalOctetPattern extends OctetPattern {
    /** The required integer width, in bits. */
    private final int width;

    /** The required byteorder, for use by an OctetReader. */
    private final int byteOrder;

    /** Parse TimevalOctetPattern from a specification in JSON format.
     * @param jsonSpec the specification to be parsed
     */
    public TimevalOctetPattern(JsonObject jsonSpec) {
        this.width = jsonSpec.getInt("width");
        if ((this.width != 32) && (this.width != 64)) {
            throw new RuntimeException(
                "invalid width for TimevalOctetPattern");
        }
        String byteOrderString = jsonSpec.getString("byteOrder", "");
        if (byteOrderString.equals("") || byteOrderString.equals("network")) {
            this.byteOrder = OctetReader.BIG_ENDIAN;
        } else if (byteOrderString.equals("host")) {
            this.byteOrder = OctetReader.LITTLE_ENDIAN;
        } else {
            throw new RuntimeException(
                "invalid byteOrder for TimevalOctetPattern");
        }
    }

    @Override
    public final boolean matches(OctetReader reader) {
        int savedByteOrder = reader.getByteOrder();
        reader.setByteOrder(byteOrder);
        boolean matched = (width == 64) ? match64(reader) : match32(reader);
        reader.setByteOrder(savedByteOrder);
        return matched;
    }

    /** Determine whether this pattern matches using 32-bit integers.
     * @param octets the octet sequence to be matched
     * @return true if the octet sequence matched, otherwise false
     */
    public final boolean match32(OctetReader reader) {
        if (reader.remaining() < 8) {
            return false;
        }
        int tv_sec = reader.readInt();
        int tv_usec = reader.readInt();
        if ((tv_sec < 0) || (tv_sec >= 0x60000000)) {
            return false;
        }
        if ((tv_usec < 0) || (tv_usec >= 1000000)) {
            return false;
        }
        return true;
    }

    /** Determine whether this pattern matches using 64-bit integers.
     * @param octets the octet sequence to be matched
     * @return true if the octet sequence matched, otherwise false
     */
    public final boolean match64(OctetReader reader) {
        if (reader.remaining() < 16) {
            return false;
        }
        long tv_sec = reader.readLong();
        long tv_usec = reader.readLong();
        if ((tv_sec < 0) || (tv_sec >= 0x60000000)) {
            return false;
        }
        if ((tv_usec < 0) || (tv_usec >= 1000000)) {
            return false;
        }
        return true;
    }
}
