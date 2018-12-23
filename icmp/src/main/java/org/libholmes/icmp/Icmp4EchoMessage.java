// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.icmp;

import javax.json.JsonObjectBuilder;

import org.libholmes.OctetString;
import org.libholmes.ParseException;

/** A class to represent an ICMPv4 echo message. */
public class Icmp4EchoMessage extends Icmp4Message {
    /** Parse ICMPv4 echo message from a generic message
     * @param message the generic message to be parsed
     * @throws ParseException if the message cannot be parsed
     */
    Icmp4EchoMessage(Icmp4Message message) throws ParseException {
        super(message);
        if (getType() != 8) {
            throw new ParseException(
                "invalid message type for ICMPv4 echo message");
        }
        if (getCode() != 0) {
            throw new ParseException(
                "invalid message code for ICMPv4 echo message");
        }
        if (getBody().length() < 4) {
            throw new ParseException(
                "body too short for ICMPv4 echo message");
        }
    }

    /** Get the identifier type.
     * @return the identifier
     */
    public final int getIdentifier() {
        return getBody().getShort(0) & 0xffff;
    }

    /** Get the sequence number.
     * @return the sequence number
     */
    public final int getSequenceNumber() {
        return getBody().getShort(2) & 0xffff;
    }

    /** Get the data carried by the message.
     * @return the data
     */
    public final OctetString getData() {
        return getBody().getOctetString(4, getBody().length() - 4);
    }

    @Override
    public final void buildJson(JsonObjectBuilder builder) {
        super.buildJson(builder);
        builder.add("identifier", getIdentifier());
        builder.add("sequenceNumber", getSequenceNumber());
        builder.add("data", getData().toString());
    }
}
