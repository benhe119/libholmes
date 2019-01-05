// This file is part of libholmes.
// Copyright 2018-2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import java.util.ArrayList;

import javax.json.JsonObject;
import javax.json.JsonArray;

/** An OctetPattern class to match an time. */
public class TimeOctetPattern extends OctetPattern {
    /** The epoch.
     * This must be either "unix" or "uptime".
     */
    private final String epoch;

    /** The required byteorder, for use by an OctetReader. */
    private final int byteOrder;

   /** The required integer width, in bits. */
    private final int width;

    /** The list of field divisors. */
    private final ArrayList<Double> divisors = new ArrayList<Double>();

    /** Parse TimeOctetPattern from a specification in JSON format.
     * @param jsonSpec the specification to be parsed
     */
    public TimeOctetPattern(JsonObject jsonSpec) {
        epoch = jsonSpec.getString("epoch", "unix");
        if (!epoch.equals("unix") && !epoch.equals("uptime")) {
            throw new RuntimeException(
                "invalid epoch for TimeOctetPattern");
        }

        String byteOrderString = jsonSpec.getString("byteOrder", "");
        if (byteOrderString.equals("") || byteOrderString.equals("network")) {
            byteOrder = OctetReader.BIG_ENDIAN;
        } else if (byteOrderString.equals("host")) {
            byteOrder = OctetReader.LITTLE_ENDIAN;
        } else {
            throw new RuntimeException(
                "invalid byteOrder for TimeOctetPattern");
        }

        width = jsonSpec.getInt("width");
        if ((width != 32) && (width != 64)) {
            throw new RuntimeException(
                "invalid width for TimeOctetPattern");
        }

        JsonArray jsonDivisors = jsonSpec.getJsonArray("divisors");
        for (int i = 0; i != jsonDivisors.size(); ++i) {
            divisors.add(new Double(jsonDivisors.getJsonNumber(i).doubleValue()));
        }
    }

    @Override
    public final boolean matches(OctetReader reader,
        OctetPatternContext context) {

        if (reader.remaining() * 8 < width * divisors.size()) {
            return false;
        }

        int savedByteOrder = reader.getByteOrder();
        reader.setByteOrder(byteOrder);
        double time = 0;
        for (Double divisor : divisors) {
            double value = (width == 64) ?
                reader.readLong() :
                (reader.readInt() & 0xffffffffL);
            time += value / divisor;
        }
        reader.setByteOrder(savedByteOrder);

        if (epoch.equals("unix")) {
            // Limit to range 2018-01-01 to 2024-01-01, but would be
            // preferable to compare with PCAP timestamp.
            if ((time < 1514764800.0) || (time > 1704067200)) {
                return false;
            }
        } else if (epoch.equals("uptime")) {
            // Limit maximum plausible uptime to 25 years.
            if (time > 788918400.0) {
                return false;
            }
        }

        return true;
    }
}
