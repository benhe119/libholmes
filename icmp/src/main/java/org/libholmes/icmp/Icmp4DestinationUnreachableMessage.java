// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.icmp;

import javax.json.JsonObjectBuilder;

import org.libholmes.OctetString;
import org.libholmes.ParseException;

/** A class to represent an ICMPv4 destination unreachable message. */
public class Icmp4DestinationUnreachableMessage extends Icmp4Message {
    /** Parse ICMPv4 destination unreachable message from a generic message
     * @param message the generic message to be parsed
     * @throws ParseException if the message cannot be parsed
     */
    Icmp4DestinationUnreachableMessage(Icmp4Message message)
        throws ParseException {

        super(message);
        if (getType() != 3) {
            throw new ParseException("invalid message type for " +
                "ICMPv4 destination unreachable message");
        }
        if (getBody().length() < 4) {
            throw new ParseException("body too short for " +
                "ICMPv4 destination unreachable message");
        }
    }

    /** Get message description.
     * @return the description
     */
    public final String getDescription() {
        switch (getCode()) {
            case 0:
                return "Net Unreachable";
            case 1:
                return "Host Unreachable";
            case 2:
                return "Protocol Unreachable";
            case 3:
                return "Port Unreachable";
            case 4:
                return "Fragmentation Needed and Don't Fragment was Set";
            case 5:
                return "Source Route Failed";
            case 6:
                return "Destination Network Unknown";
            case 7:
                return "Destination Host Unknown";
            case 8:
                return "Source Host Isolated";
            case 9:
                return "Communication with Destination Network " +
                    "is Administratively Prohibited";
            case 10:
                return "Communication with Destination Host " +
                    "is Administratively Prohibited";
            case 11:
                return "Destination Network Unreachable for Type of Service";
            case 12:
                return "Destination Host Unreachable for Type of Service";
            case 13:
                return "Communication Administratively Prohibited";
            case 14:
                return "Host Precedence Violation";
            case 15:
                return "Precedence cutoff in effect";
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
