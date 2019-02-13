// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.udp;

import java.net.InetAddress;
import javax.json.JsonObject;

import org.libholmes.OctetReader;
import org.libholmes.OctetPattern;
import org.libholmes.AnalysisContext;
import org.libholmes.Artefact;
import org.libholmes.Fingerprint;

/** A class for fingerprinting UDP datagrams. */
public class UdpFingerprint extends Fingerprint {
    /** The source port number which must be matched, or null for any
     * value. */
    private final Integer srcPort;

    /** The destination port number which must be matched, or null for any
     * value. */
    private final Integer dstPort;

    /** The pattern which the payload must match in full, or null for none. */
    private final OctetPattern payloadPattern;

    /** Construct UDP echo fingerprint from JSON.
     * @param json the fingerprint, as JSON
     */
    public UdpFingerprint(JsonObject json) {
        srcPort = json.containsKey("srcPort") ?
            new Integer(json.getInt("srcPort")) : null;
        dstPort = json.containsKey("dstPort") ?
            new Integer(json.getInt("dstPort")) : null;
        payloadPattern = json.containsKey("payload") ?
            OctetPattern.parse(json.get("payload")) : null;
    }

    @Override
    public final boolean matches(Artefact artefact, AnalysisContext context) {
        UdpDatagram datagram = artefact.find(UdpDatagram.class);
        if (datagram == null) {
            return false;
        }

        if ((srcPort != null) && (datagram.getSrcPort() != srcPort)) {
            return false;
        }
        if ((dstPort != null) && (datagram.getDstPort() != dstPort)) {
            return false;
        }
        if (payloadPattern != null) {
            OctetReader reader = datagram.getPayload().makeOctetReader();
            if (!payloadPattern.matches(reader, context)) {
                return false;
            }
            if (reader.hasRemaining()) {
                return false;
            }
        }
        return true;
    }
}
