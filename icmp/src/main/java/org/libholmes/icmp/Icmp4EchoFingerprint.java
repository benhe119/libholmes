// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.icmp;

import java.net.InetAddress;
import javax.json.JsonObject;

import org.libholmes.OctetReader;
import org.libholmes.OctetPattern;
import org.libholmes.Artefact;

public class Icmp4EchoFingerprint {
    /** The unique ID of this fingerprint. */
    private final String id;

    /** True if length suffix appended to ID, otherwise false. */
    private final boolean lengthSuffix;

    /** The pattern which the data field must match in full. */
    private final OctetPattern dataPattern;

    /** Construct ICMPv4 echo fingerprint from JSON.
     * @param json the fingerprint, as JSON
     */
    public Icmp4EchoFingerprint(JsonObject json) {
        id = json.getString("_id");
        lengthSuffix = json.getBoolean("lengthSuffix", false);
        dataPattern = OctetPattern.parse(json.get("data"));
    }

    /** Get the unique ID of this fingerprint (without length).
     * @return the unique ID, without length
     */
    public String getId() {
        return id;
    }

    /** Get the unique ID of this fingerprint (with length).
     * @param message the message matched
     * @return the unique ID, with length if appropriate
     */
    public String getId(Icmp4Message message) {
        if (lengthSuffix) {
            if (message instanceof Icmp4EchoMessage) {
                Icmp4EchoMessage request = (Icmp4EchoMessage) message;
                return String.format("%s/%d", id, request.getData().length());
            } else if (message instanceof Icmp4EchoReplyMessage) {
                Icmp4EchoReplyMessage reply = (Icmp4EchoReplyMessage) message;
                return String.format("%s/%d", id, reply.getData().length());
            } else {
                return id;
            }
        } else {
            return id;
        }
    }

    /** Determine whether this fingerprint matches a given ICMPv4 message.
     * @param message the message against which to match
     * @return true if fingerprint matches, otherwise false
     */
    public final boolean matches(Icmp4Message message) {
        if (message instanceof Icmp4EchoMessage) {
            Icmp4EchoMessage request = (Icmp4EchoMessage) message;
            OctetReader reader = request.getData().makeOctetReader();
            if (!dataPattern.matches(reader)) {
                return false;
            }
            if (reader.hasRemaining()) {
                return false;
            }
        } else if (message instanceof Icmp4EchoReplyMessage) {
            Icmp4EchoReplyMessage reply = (Icmp4EchoReplyMessage) message;
            OctetReader reader = reply.getData().makeOctetReader();
            if (!dataPattern.matches(reader)) {
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
