// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.inet;

import javax.json.JsonObjectBuilder;

import org.libholmes.OctetReader;
import org.libholmes.OctetString;
import org.libholmes.Artefact;
import org.libholmes.Logger;
import org.libholmes.ParseException;

/** A base class to represent an IPv4 option. */
public abstract class Inet4Option extends Artefact {
    /** The IPv4 option type for an end of option list marker. */
    public static final int OPTION_EOOL = 0;

    /** The IPv4 option type for a no operation option. */
    public static final int OPTION_NOP = 1;

    /** The IPv4 option type for a DoD basic security option. */
    public static final int OPTION_SEC = 130;

    /** The IPv4 option type for a loose source route option. */
    public static final int OPTION_LSR = 131;

    /** The IPv4 option type for a timestamp option. */
    public static final int OPTION_TS = 68;

    /** The IPv4 option type for an DoD extended security option. */
    public static final int OPTION_E_SEC = 133;

    /** The IPv4 option type for a commercial IP security option. */
    public static final int OPTION_CIPSO = 134;

    /** The IPv4 option type for a record route option. */
    public static final int OPTION_RR = 7;

    /** The IPv4 option type for a stream ID option. */
    public static final int OPTION_SID = 136;

    /** The IPv4 option type for a strict source route option. */
    public static final int OPTION_SSR = 137;

    /** The IPv4 option type for a ZSU option (experimental). */
    public static final int OPTION_ZSU = 10;

    /** The IPv4 option type for an MTU probe option. */
    public static final int OPTION_MTUP = 11;

    /** The IPv4 option type for an MTU reply option. */
    public static final int OPTION_MTUR = 12;

    /** The IPv4 option type for a FINN option (experimental). */
    public static final int OPTION_FINN = 205;

    /** The IPv4 option type for a VISA option (experimental). */
    public static final int OPTION_VISA = 142;

    /** The IPv4 option type for an ENCODE option. */
    public static final int OPTION_ENCODE = 15;

    /** The IPv4 option type for an IMI traffic descriptor option. */
    public static final int OPTION_IMITD = 144;

    /** The IPv4 option type for an Extended Internet Protocol option. */
    public static final int OPTION_EIP = 145;

    /** The IPv4 option type for a traceroute option. */
    public static final int OPTION_TR = 82;

    /** The IPv4 option type for an address extension option. */
    public static final int OPTION_ADDEXT = 147;

    /** The IPv4 option type for a router alert option. */
    public static final int OPTION_RTRALT = 148;

    /** The IPv4 option type for a selective directed broadcast option. */
    public static final int OPTION_SDB = 149;

    /** The IPv4 option type for a dynamic packet state option. */
    public static final int OPTION_DPS = 151;

    /** The IPv4 option type for an upstream multicast packet option. */
    public static final int OPTION_UMP = 152;

    /** The IPv4 option type for a quick start option. */
    public static final int OPTION_QS = 25;

    /** The option type. */
    private final int type;

    /** The payload. */
    private final OctetString payload;

    /** Copy-construct IPv4 option from an existing option.
     * @param that the existing option
     */
    Inet4Option(Inet4Option that) {
        super(that.getParent());
        this.type = that.type;
        this.payload = that.payload;
    }

    /** Parse IPv4 option from an OctetReader.
     * @param parent the parent artefact, or null if none
     * @param reader the OctetReader to be parsed
     * @throws ParseException if the octet sequence cannot be parsed
     */
    Inet4Option(Artefact parent, OctetReader reader) throws ParseException {
        super(parent);
        this.type = reader.readByte() & 0xff;
        if (this.type >= 2) {
            int length = reader.readByte() & 0xff;
            if (length < 2) {
                throw new ParseException("invalid IPv4 option length");
            }
            this.payload = reader.readOctetString(length - 2);
        } else {
            this.payload = reader.readOctetString(0);
        }
    }

    /** Get the type of this option.
     * @return the option type
     */
    public final int getType() {
        return type;
    }

    /** Get the length of this option.
     * This includes the option type and (if present) length fields.
     * @return the length, in octets
     */
    public final int getLength() {
        return (type >= 2) ? payload.length() + 2 : 1;
    }

    /** Get the payload for this option.
     * This does not include the option type or length fields.
     * @return the payload
     */
    public final OctetString getPayload() {
        return payload;
    }

    /** Check whether multiple instances of this option should be reported.
     * @return true if multiple instances are reportable, otherwise false
     */
    public abstract boolean reportMultipleInstances();

    /** Build a JSON object for this option.
     * @param builder a JsonObjectBuilder for the object to be built
     */
    protected void buildJson(JsonObjectBuilder builder) {
        builder.add("type", type);
        builder.add("length", getLength());
        builder.add("payload", payload.toString());
    }

    /** Parse IPv4 option.
     * @param parent the parent artefact, or null if none
     * @param reader the octet source to be parsed
     * @return the resulting IPv4 option
     * @throws ParseException if the octet sequence cannot be parsed
     */
    static Inet4Option parse(Artefact parent, OctetReader reader)
        throws ParseException {

        Inet4Option option = new Inet4UnrecognisedOption(parent, reader);
        return option;
    }
}
