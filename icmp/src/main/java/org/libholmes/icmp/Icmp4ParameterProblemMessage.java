// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.icmp;

import javax.json.JsonObjectBuilder;

import org.libholmes.OctetString;
import org.libholmes.ParseException;

/** A class to represent an ICMPv4 parameter problem message. */
public class Icmp4ParameterProblemMessage extends Icmp4Message {
    /** Parse ICMPv4 parameter problem message from a generic message
     * @param message the generic message to be parsed
     * @throws ParseException if the message cannot be parsed
     */
    Icmp4ParameterProblemMessage(Icmp4Message message)
        throws ParseException {

        super(message);
        if (getType() != 12) {
            throw new ParseException("invalid message type for " +
                "ICMPv4 parameter problem message");
        }
        if (getBody().length() < 4) {
            throw new ParseException("body too short for " +
                "ICMPv4 parameter problem message");
        }
    }

    /** Get pointer.
     * If getCode() == 0 then this is the index of the octet in
     * getOriginalDatagram() where the error was detected.
     * If getCode() == 1 then this is the type of the option that
     * was missing.
     * @return the pointer
     */
    public final int getPointer() {
        return getBody().getByte(0) & 0xff;
    }

    /** Get message description.
     * @return the description
     */
    public final String getDescription() {
        switch (getCode()) {
            case 0:
                return "Pointer indicates the error";
            case 1:
                return "Missing a Required Option";
            case 2:
                return "Bad length";
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
        builder.add("pointer", getPointer());
        builder.add("description", getDescription());
        builder.add("originalDatagram", getOriginalDatagram().toString());
    }
}
