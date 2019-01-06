// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.tcp;

import javax.json.JsonObjectBuilder;

import org.libholmes.OctetReader;
import org.libholmes.OctetString;
import org.libholmes.ParseException;

/** A class to represent a TCP window scale option. */
public class TcpWindowScaleOption extends TcpOption {
    /** The shift count, in bits. */
    private final int shift;

    /** Parse TCP window scale option from a generic option.
     * @param option the generic option to be parsed
     * @throws ParseException if the octet sequence cannot be parsed
     */
    public TcpWindowScaleOption(TcpOption option) throws ParseException {
        super(option);
        OctetReader reader = getPayload().makeOctetReader();
        this.shift = reader.readByte() & 0xff;
    }

    /** Get the shift count.
     * @return the shift count, in bits
     */
    public final int getShift() {
        return shift;
    }

    @Override
    protected void buildJson(JsonObjectBuilder builder) {
        super.buildJson(builder);
        builder.add("shift", shift);
    }
}
