// This file is part of libholmes.
// Copyright 2018-2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.icmp;

import java.net.InetAddress;
import javax.json.JsonNumber;
import javax.json.JsonObject;

import org.libholmes.OctetReader;
import org.libholmes.OctetPattern;
import org.libholmes.AnalysisContext;
import org.libholmes.Artefact;
import org.libholmes.Fingerprint;

public class Icmp4EchoFingerprint extends Fingerprint {
    /** True if length suffix appended to ID, otherwise false. */
    private final boolean lengthSuffix;

    /** The checksum which must be matched, or null for any value.
     * This is included for matching tools such as Paris Traceroute which
     * manipulate the payload in order to obtain a particular checksum.
     */
    private final Integer checksum;

    /** The identifier which must be matched, or null for any value. */
    private final Integer identifier;

    /** The sequence number which must be matched, or null for any value. */
    private final Integer sequenceNumber;

    /** The pattern which the data field must match in full. */
    private final OctetPattern dataPattern;

    /** Construct ICMPv4 echo fingerprint from JSON.
     * @param json the fingerprint, as JSON
     */
    public Icmp4EchoFingerprint(JsonObject json) {
        lengthSuffix = json.getBoolean("lengthSuffix", false);
        checksum = json.containsKey("checksum") ?
            new Integer(json.getInt("checksum")): null;
        identifier = (json.get("identifier") instanceof JsonNumber) ?
            new Integer(json.getInt("identifier")) : null;
        sequenceNumber = (json.get("sequenceNumber") instanceof JsonNumber) ?
            new Integer(json.getInt("sequenceNumber")) : null;
        dataPattern = OctetPattern.parse(json.get("data"));
    }

    /** Determine whether this fingerprint matches a given ICMPv4 message.
     * @param message the message against which to match
     * @param context the pattern matching context
     * @return true if fingerprint matches, otherwise false
     */
    public final boolean matches(Artefact artefact, AnalysisContext context) {
        Icmp4Message message = artefact.find(Icmp4Message.class);
        if (message == null) {
            return false;
        }

        if ((checksum != null) && (message.getRecordedChecksum() != checksum)) {
            return false;
        }
        if (message instanceof Icmp4EchoMessage) {
            Icmp4EchoMessage request = (Icmp4EchoMessage) message;
            if ((identifier != null) && (request.getIdentifier() != identifier)) {
                return false;
            }
            if ((sequenceNumber != null) && (request.getSequenceNumber() != sequenceNumber)) {
                return false;
            }
            OctetReader reader = request.getData().makeOctetReader();
            if (!dataPattern.matches(reader, context)) {
                return false;
            }
            if (reader.hasRemaining()) {
                return false;
            }
        } else if (message instanceof Icmp4EchoReplyMessage) {
            Icmp4EchoReplyMessage reply = (Icmp4EchoReplyMessage) message;
            if ((identifier != null) && (reply.getIdentifier() != identifier)) {
                return false;
            }
            if ((sequenceNumber != null) && (reply.getSequenceNumber() != sequenceNumber)) {
                return false;
            }
            OctetReader reader = reply.getData().makeOctetReader();
            if (!dataPattern.matches(reader, context)) {
                return false;
            }
            if (reader.hasRemaining()) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }
}
