// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.tcp;

import javax.json.JsonObjectBuilder;

import org.libholmes.OctetReader;
import org.libholmes.OctetString;
import org.libholmes.ParseException;

/** A class to represent a TCP maximum segment size option. */
public class TcpMaximumSegmentSizeOption extends TcpOption {
    /** The maximum segment size. */
    private final int mss;

    /** Parse TCP maximum segment size option from a generic option.
     * @param option the generic option to be parsed
     * @throws ParseException if the octet sequence cannot be parsed
     */
    public TcpMaximumSegmentSizeOption(TcpOption option) throws ParseException {
        super(option);
        OctetReader reader = getPayload().makeOctetReader();
        this.mss = reader.readShort() & 0xffff;
    }

    /** Get the maximum segment size.
     * @return the maximum segment size
     */
    public final int getMaximumSegmentSize() {
        return mss;
    }

    @Override
    protected void buildJson(JsonObjectBuilder builder) {
        super.buildJson(builder);
        builder.add("mss", mss);
    }
}
