// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.tcp;

import javax.json.JsonObjectBuilder;

import org.libholmes.OctetReader;
import org.libholmes.OctetString;
import org.libholmes.Artefact;
import org.libholmes.ParseException;

/** A class to represent a TCP option. */
public class TcpOption extends Artefact {
    /** The kind of this option. */
    private final int kind;

    /** The payload. */
    private final OctetString payload;

    /** Copy-construct TCP option from an existing option.
     * @param that the existing option
     */
    TcpOption(TcpOption that) {
        super(that.getParent());
        this.kind = that.kind;
        this.payload = that.payload;
    }

    /** Parse TCP option from an OctetReader.
     * @param parent the parent artefact, or null if none
     * @param reader the OctetReader to be parsed
     * @throws ParseException if the octet sequence cannot be parsed
     */
    TcpOption(Artefact parent, OctetReader reader) throws ParseException {
        super(parent);
        this.kind = reader.readByte() & 0xff;
        if (this.kind >= 2) {
            int length = reader.readByte() & 0xff;
            if (length < 2) {
                throw new ParseException("invalid TCP option length");
            }
            this.payload = reader.readOctetString(length - 2);
        } else {
            this.payload = reader.readOctetString(0);
        }
    }

    /** Get the kind of this option.
     * @return the option kind
     */
    public final int getKind() {
        return kind;
    }

    /** Get the length of this option.
     * This includes the option kind and (if present) length fields.
     * @return the length, in octets
     */
    public final int getLength() {
        return (kind >= 2) ? payload.length() + 2 : 1;
    }

    /** Get the payload for this option.
     * This does not include the option kind or length fields.
     * @return the payload
     */
    public final OctetString getPayload() {
        return payload;
    }

    @Override
    protected void buildJson(JsonObjectBuilder builder) {
        builder.add("kind", kind);
        builder.add("length", getLength());
        builder.add("payload", payload.toString());
    }

    /** Parse TCP option.
     * @param parent the parent artefact, or null if none
     * @param reader the octet source to be parsed
     * @return the resulting TCP option
     * @throws ParseException if the octet sequence cannot be parsed
     */
    static TcpOption parse(Artefact parent, OctetReader reader)
        throws ParseException {

        TcpOption option = new TcpUnrecognisedOption(parent, reader);
        try {
            switch (option.getKind()) {
                case 0:
                    option = new TcpEndOfOptionsList(option);
                    break;
               case 1:
                    option = new TcpNoOperationOption(option);
                    break;
                case 2:
                    option = new TcpMaximumSegmentSizeOption(option);
                    break;
                case 3:
                    option = new TcpWindowScaleOption(option);
                    break;
                case 4:
                    option = new TcpSackPermittedOption(option);
                    break;
                case 5:
                    option = new TcpSackOption(option);
                    break;
                case 8:
                    option = new TcpTimestampsOption(option);
                    break;
            }
        } catch (Exception ex) {
            // Failed to parse option as a specific type,
            // but can keep it as a generic option.
        }
        return option;
    }
}
