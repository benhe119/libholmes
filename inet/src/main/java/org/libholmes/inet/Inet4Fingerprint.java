// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.inet;

import javax.json.JsonObject;

import org.libholmes.OctetReader;
import org.libholmes.OctetPattern;
import org.libholmes.AnalysisContext;
import org.libholmes.Artefact;
import org.libholmes.Fingerprint;
import org.libholmes.ParseException;

/** A class for fingerprinting IPv4 datagrams. */
public class Inet4Fingerprint extends Fingerprint {
    /** The identification field, or null for any value. */
    private final Integer id;

    /** The required value of the DF flag, or null for any value. */
    private final Boolean df;

    /** The initial value of the TTL field, or null if unspecified.
     * This is the TTL which would be observed if there were no
     * intervening routers. It acts as an upper limit for the value
     * allowed, and can be used to assess the plausibility of smaller
     * values.
     */
    private final Integer ttl;

    /** The required value of the protocol field, or null for any value.
     * This need not be set if a particular protocol is implied by
     * matching against another type of fingerprint.
     */
    private final Integer protocol;

    /** A pattern which the payload must match in full, or null for
     * any payload. */
    private final OctetPattern payloadPattern;

    /** A netblock which the source address must match in full, or
     * null for any address.
     */
    private final InetNetblock addr;

    /** Construct IPv4 fingerprint from JSON.
     * @param json the fingerprint, as JSON
     */
    public Inet4Fingerprint(JsonObject json) throws ParseException {
        id = json.containsKey("id") ?
            new Integer(json.getInt("id")) : null;
        df = json.containsKey("df") ?
            new Boolean(json.getBoolean("df")) : null;
        ttl = json.containsKey("ttl") ?
            new Integer(json.getInt("ttl")) : null;
        protocol = json.containsKey("protocol") ?
            new Integer(json.getInt("protocol")) : null;
        payloadPattern = json.containsKey("payload") ?
            OctetPattern.parse(json.get("payload")) : null;
        addr = json.containsKey("addr") ?
            InetNetblock.parse(json.getString("addr")) : null;
    }

    /** Determine whether this fingerprint matches a given IPv4 datagram.
     * @param artefact the artefact against which to match
     * @param context the pattern matching context
     * @return true if fingerprint matches, otherwise false
     */
    public final boolean matches(Artefact artefact, AnalysisContext context) {
        Inet4Datagram datagram = artefact.find(Inet4Datagram.class);
        if (datagram == null) {
            return false;
        }

        if ((id != null) && (datagram.getId() != id)) {
            return false;
        }
        if ((df != null) && (datagram.doNotFragment() != df)) {
            return false;
        }
        if (ttl != null) {
            int observedTtl = datagram.getTtl();
            if ((observedTtl > ttl) || (observedTtl < ttl - 32)) {
                return false;
            }
        }
        if ((protocol != null) && (datagram.getProtocol() != protocol)) {
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
        if (addr != null) {
            if (!addr.contains(datagram.getSrcAddr())) {
                return false;
            }
        }
        return true;
    }
}
