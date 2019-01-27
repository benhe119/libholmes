// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.inet;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonArrayBuilder;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import org.libholmes.OctetReader;
import org.libholmes.ParseException;

/** A base class to represent an IPv4 option containing a route.
 * This class is intended for use as a base for the Record Route (7), Loose
 * Source and Record Route (131) and Strict Source and Record Route (137)
 * options, all of which have an identical format.
 */
public class Inet4RouteOption extends Inet4Option {
    /** A pointer to the first unused route slot. */
    private final byte pointer;

    /** The route data. */
    private final ArrayList<Inet4Address> route =
        new ArrayList<Inet4Address>();

    /** Parse IPv4 route option from a generic option.
     * @param option the generic option to be parsed
     * @throws ParseException if the octet sequence cannot be parsed
     */
    Inet4RouteOption(Inet4Option option) throws ParseException {
        super(option);
        OctetReader reader = getPayload().makeOctetReader();
        this.pointer = reader.readByte();

        if ((reader.remaining() % 4) != 0) {
            throw new ParseException(
                "invalid length for loose source route option");
        }
        while (reader.hasRemaining()) {
            Inet4Address address = Inet4Address.parse(reader);
            this.route.add(address);
        }
    }

    /** Get pointer to first unused route slot.
     * This is an octet index, based at 1 and counted from the type field, to
     * the first route data slot that has not yet been used. It follows that:
     * - The smallest legal value is 4 (corresponding to the first route data
     *   slot).
     * - It should be a multiple of 4.
     * @return a pointer to the first unused route slot
     */
    public final int getPointer() {
        return pointer;
    }

    /** Get route.
     * @return the route, as a list of IPv4 addresses
     */
    public final List<Inet4Address> getRoute() {
        return Collections.unmodifiableList(route);
    }

    @Override
    public boolean reportMultipleInstances() {
        // Multiple instances are expressly forbidden by RFC 791.
        return true;
    }

    @Override
    protected void buildJson(JsonObjectBuilder builder) {
        super.buildJson(builder);
        JsonArrayBuilder routeBuilder = Json.createArrayBuilder();
        for (Inet4Address address : route) {
            routeBuilder.add(address.toString());
        }
        builder.add("route", routeBuilder.build());
        builder.add("pointer", pointer);
    }
}
