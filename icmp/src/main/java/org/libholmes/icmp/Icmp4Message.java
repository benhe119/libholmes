// This file is part of libholmes.
// Copyright 2018-2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.icmp;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.libholmes.OctetReader;
import org.libholmes.OctetString;
import org.libholmes.Artefact;
import org.libholmes.Logger;
import org.libholmes.ParseException;
import org.libholmes.inet.InetChecksum;

/** A base class to represent an ICMPv4 message. */
public abstract class Icmp4Message extends Artefact {
    /** The message header. */
    private final OctetString header;

    /** The message body. */
    private final OctetString body;

    /** Copy-construct ICMPv4 message from an existing message.
     * @param that the existing message
     */
    Icmp4Message(Icmp4Message that) {
        super(that.getParent());
        this.header = that.header;
        this.body = that.body;
    }

    /** Parse ICMPv4 message from an OctetReader.
     * @param parent the parent artefact, or null if none
     * @param reader the OctetReader to be parsed
     * @throws ParseException if the octet sequence cannot be parsed
     */
    Icmp4Message(Artefact parent, OctetReader reader) throws ParseException {
        super(parent);
        this.header = reader.readOctetString(4);
        this.body = reader.readOctetString(reader.remaining());
    }

    /** Get the message type.
     * @return the message type
     */
    public final int getType() {
        return header.getByte(0) & 0xff;
    }

    /** Get the message code.
     * @return the message code
     */
    public final int getCode() {
        return header.getByte(1) & 0xff;
    }

    /** Get the recorded message checksum.
     * This is the checksum as recorded in the checksum field of the
     * message.
     * @return the recorded message checksum
     */
    public final int getRecordedChecksum() {
        return header.getShort(2) & 0xffff;
    }

    /** Get the calculated message checksum.
     * This is the checksum as calculated by summing every word in the
     * message except for the checksum field.
     * @return the calculated message checksum
     */
    public final int getCalculatedChecksum() {
        InetChecksum checksum = new InetChecksum();
        checksum.add(header.getOctetString(0, 2));
        checksum.add(body);
        return checksum.get();
    }

    /** Get the body of this message.
     * @return the message body
     */
    public final OctetString getBody() {
        return body;
    }

    /** Build a JSON object for this message.
     * @param builder a JsonObjectBuilder for the object to be built
     */
    protected void buildJson(JsonObjectBuilder builder) {
        builder.add("type", getType());
        builder.add("code", getCode());
        builder.add("recordedChecksum", getRecordedChecksum());
        builder.add("calculatedChecksum", getCalculatedChecksum());
        builder.add("body", getBody().toString());
    }

    /** Parse ICMPv4 message from an OctetReader.
     * @param parent the parent artefact, or null if none
     * @param reader the octet source to be parsed
     * @return the resulting ICMPv4 message
     * @throws ParseException if the octet sequence cannot be parsed
     */
    static public Icmp4Message parse(Artefact parent, OctetReader reader)
        throws ParseException {

        Icmp4Message message = new Icmp4UnrecognisedMessage(parent, reader);
        try {
            switch (message.getType()) {
                case 0:
                    message = new Icmp4EchoReplyMessage(message);
                    break;
                case 3:
                    message = new Icmp4DestinationUnreachableMessage(message);
                    break;
                case 8:
                    message = new Icmp4EchoMessage(message);
                    break;
            }
        } catch (ParseException ex) {
            // No action: fall back to Icmp4UnrecognisedMessage.
        }
        return message;
    }

    /** Parse ICMPv4 message from an OctetString.
     * @param parent the parent artefact, or null if none
     * @param string the OctetString to be parsed
     * @return the resulting ICMPv4 message
     * @throws ParseException if the octets cannot be parsed
     */
    public static Icmp4Message parse(Artefact parent, OctetString string)
        throws ParseException {

        return parse(parent, string.makeOctetReader());
    }
}
