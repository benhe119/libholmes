// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.icmp;

import javax.json.JsonObjectBuilder;

import org.libholmes.OctetString;
import org.libholmes.ParseException;
import org.libholmes.inet.Inet4Address;

/** A class to represent an ICMPv4 redirect message. */
public class Icmp4RedirectMessage extends Icmp4Message {
    /** The gateway address to which traffic should be sent. */
    private final Inet4Address gatewayInternetAddress;

    /** Parse ICMPv4 redirect message from a generic message
     * @param message the generic message to be parsed
     * @throws ParseException if the message cannot be parsed
     */
    Icmp4RedirectMessage(Icmp4Message message)
        throws ParseException {

        super(message);
        if (getType() != 5) {
            throw new ParseException("invalid message type for " +
                "ICMPv4 redirect message");
        }
        if (getBody().length() < 4) {
            throw new ParseException("body too short for " +
                "ICMPv4 redirect message");
        }
        gatewayInternetAddress = Inet4Address.parse(
            getBody().getOctetString(0, 4));
    }

    /** Get message description.
     * @return the description
     */
    public final String getDescription() {
        switch (getCode()) {
            case 0:
                return "Redirect Datagrams for the Network";
            case 1:
                return "Redirect Datagrams for the Host";
            case 2:
                return "Redirect Datagrams for the " +
                    "Type of Service and Network";
            case 3:
                return "Redirect Datagrams for the " +
                    "Type of Service and Host";
        }
        return null;
    }

    /** Get the gateway address to which traffic should be sent.
     * @return the gateway address
     */
    public final Inet4Address getGatewayInternetAddress() {
        return gatewayInternetAddress;
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
        builder.add("gatewayInternetAddress",
            gatewayInternetAddress.toString());
        builder.add("originalDatagram", getOriginalDatagram().toString());
    }
}
