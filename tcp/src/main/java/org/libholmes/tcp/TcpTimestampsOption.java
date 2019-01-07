// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.tcp;

import javax.json.JsonObjectBuilder;

import org.libholmes.OctetReader;
import org.libholmes.OctetString;
import org.libholmes.ParseException;

/** A class to represent a TCP timestamps option. */
public class TcpTimestampsOption extends TcpOption {
    /** The timestamp value. */
    private final int tsVal;

    /** The timestamp echo reply. */
    private final int tsEcr;

    /** Parse TCP timestamp option from a generic option.
     * @param option the generic option to be parsed
     * @throws ParseException if the octet sequence cannot be parsed
     */
    public TcpTimestampsOption(TcpOption option) throws ParseException {
        super(option);
        OctetReader reader = getPayload().makeOctetReader();
        this.tsVal = reader.readInt();
        this.tsEcr = reader.readInt();
    }

    /** Get the timestamp value.
     * Note that this is returned as a signed 32-bit integer, however it
     * will typically be more appropriate to interpret it as an unsigned
     * integer. The length of a clock tick is host-dependent.
     * @return the timestamp value
     */
    public final int getTimestampValue() {
        return tsVal;
    }

    /** Get the timestamp echo reply.
     * Note that this is returned as a signed 32-bit integer, however it
     * will typically be more appropriate to interpret it as an unsigned
     * integer. The length of a clock tick is host-dependent.
     * @return the timestamp echo reply
     */
    public final int getTimestampEchoReply() {
        return tsEcr;
    }

    @Override
    protected void buildJson(JsonObjectBuilder builder) {
        super.buildJson(builder);
        builder.add("tsVal", tsVal);
        builder.add("tsEcr", tsEcr);
    }
}
