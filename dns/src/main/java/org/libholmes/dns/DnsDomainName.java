// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.dns;

import java.util.ArrayList;
import java.util.HashSet;

import org.libholmes.OctetReader;
import org.libholmes.OctetString;
import org.libholmes.Logger;
import org.libholmes.ParseException;

/** A class to represent a DNS domain name.
 * A domain name is composed of a sequence of labels, terminated by an empty
 * label (which represents the root domain).
 * For presentational purposes, domain names are escaped using a scheme
 * compatible with the one described in RFC 1035 for use in DNS master zone
 * files. Specifically:
 * - Non-space characters within the printable range for US-ASCII (33 to 126
 *   inclusive) are decoded using that character set.
 * - The following characters are additionally escaped using a backslash:
 *   - 0x22 (double quote)
 *   - 0x24 (dollar sign)
 *   - 0x28 (open parenthesis)
 *   - 0x29 (close parenthesis)
 *   - 0x2e (full stop)
 *   - 0x3b (semicolon)
 *   - 0x40 (at sign)
 *   - 0x5c (backslash)
 * - All other characters are represented by numerical escape sequences
 *   consisting of a backslash followed by three decimal digits (padded with
 *   zeros if necessary).
 * This differs from the format used for character strings in zone files due
 * to the expanded set of characters which are escaped (including numerical
 * representation of the space character). However, for domain names which
 * are suitable for use as hostnames, no escaping is necessary.
 */
public class DnsDomainName {
    /** The raw labels from which this domain name is composed.
     * The list should consist of zero or more labels for which isFinal()
     * is false, followed by a single label for which isFinal() is true.
     */
    private final ArrayList<DnsLabel> labels = new ArrayList<DnsLabel>();

    /** Parse domain name from OctetReader.
     * @param reader the OctetReader to be parsed
     * @param ptrReader an OctetReader positioned at the start of the message
     * @throws ParseException if the octet sequence could not be parsed
     */
    public DnsDomainName(OctetReader reader, OctetReader ptrReader)
        throws ParseException {

        OctetReader labelReader = reader;
        HashSet<Integer> offsets = new HashSet<Integer>();
        boolean done = false;
        while (!done) {
            DnsLabel label = DnsLabel.parse(reader, ptrReader, offsets);
            this.labels.add(label);
            done = label.isFinal();
        }
    }

    /** Get the label at a given physical index.
     * @param index the physical index
     * @return the corresponding label
     */
    public final DnsLabel getRawLabel(int index) {
        return labels.get(index);
    }

    /** Get the number of physical labels.
     * @return the number of physical labels
     */
    public final int getRawLabelCount() {
        return labels.size();
    }

    /** Get the label at a given logical index.
     * @param index the logical index
     * @return the corresponding label
     */
    public final DnsLabel getLabel(int index) {
        int lastLabelIndex = labels.size() - 1;
        if (index >= lastLabelIndex) {
            DnsLabel lastLabel = labels.get(lastLabelIndex);
            if (lastLabel instanceof DnsCompressedLabel) {
                DnsCompressedLabel compressedLabel =
                    (DnsCompressedLabel) lastLabel;
                return compressedLabel.getLabel(index - lastLabelIndex);
            }
        }
        return labels.get(index);
    }

    /** Get the number of logical labels.
     * @return the number of logical labels
     */
    public final int getLabelCount() {
        int lastLabelIndex = labels.size() - 1;
        if (lastLabelIndex >= 0) {
            DnsLabel lastLabel = labels.get(lastLabelIndex);
            if (lastLabel instanceof DnsCompressedLabel) {
                DnsCompressedLabel compressedLabel =
                    (DnsCompressedLabel) lastLabel;
                return lastLabelIndex + compressedLabel.getLabelCount();
            }
        }
        return labels.size();
    }

    @Override
    public final boolean equals(Object thatObject) {
        if (thatObject == this) {
            return true;
        }
        if (thatObject == null) {
            return false;
        }
        if (thatObject instanceof DnsDomainName) {
            DnsDomainName that = (DnsDomainName) thatObject;
            return this.labels.equals(that.labels);
        } else {
            return false;
        }
    }

    @Override
    public final int hashCode() {
        return labels.hashCode();
    }

    @Override
    public final String toString() {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (DnsLabel label : labels) {
            if (first) {
                first = false;
            } else {
                builder.append(".");
            }
            label.buildString(builder);
        }
        if (builder.length() == 0) {
            builder.append(".");
        }
        return builder.toString();
    }
}
