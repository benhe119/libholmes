// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.icmp;

import javax.json.JsonObjectBuilder;

import org.libholmes.OctetString;
import org.libholmes.ParseException;

/** A class to represent an ICMPv4 time exceeded message. */
public class Icmp4TimeExceededMessage extends Icmp4Message {
    /** Parse ICMPv4 time exceeded message from a generic message
     * @param message the generic message to be parsed
     * @throws ParseException if the message cannot be parsed
     */
    Icmp4TimeExceededMessage(Icmp4Message message)
        throws ParseException {

        super(message);
        if (getType() != 11) {
            throw new ParseException("invalid message type for " +
                "ICMPv4 time exceeded message");
        }
        if (getBody().length() < 4) {
            throw new ParseException("body too short for " +
                "ICMPv4 time exceeded message");
        }
    }

    /** Get message description.
     * @return the description
     */
    public final String getDescription() {
        switch (getCode()) {
            case 0:
                return "Time to Live exceeded in Transit";
            case 1:
                return "Fragment Reassembly Time Exceeded";
        }
        return null;
    }

    /** Get the (possibly truncated) original datagram.
     * RFC 792 required inclusion of the original internet header plus
     * 64 bits of the payload. This remains the behaviour of many routers,
     * however RFC 1812 added a recommendation that as much of the original
     * datagram be included as possible, subject to a maximum length of
     * 576 bytes for the resulting datagram as a whole.
     * @return the original message
     */
    public final OctetString getOriginalDatagram() {
        OctetString body = getBody();
        return body.getOctetString(4, body.length() - 4);
    }

    @Override
    public final void buildJson(JsonObjectBuilder builder) {
        super.buildJson(builder);
        builder.add("description", getDescription());
        builder.add("originalDatagram", getOriginalDatagram().toString());
    }
}
