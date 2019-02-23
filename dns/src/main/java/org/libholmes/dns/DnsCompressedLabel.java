// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.dns;

import java.util.ArrayList;
import java.util.HashSet;

import org.libholmes.OctetReader;
import org.libholmes.ParseException;

/** A class to represent a compressed domain name label. */
public class DnsCompressedLabel extends DnsLabel {
    /** The labels to which this compressed label corresponds.
     * (including the terminating empty label). */
    private final ArrayList<DnsLabel> labels = new ArrayList<DnsLabel>();

    /** Parse compressed label from OctetReader.
     * @param reader the OctetReader to be parsed
     * @param ptrReader an OctetReader positioned at the start of the message
     * @param offsets pointer offsets that were followed to reach this label
     * @throws ParseException if the octet sequence could not be parsed
     */
    DnsCompressedLabel(OctetReader reader, OctetReader ptrReader,
        HashSet<Integer> offsets) throws ParseException {

        OctetReader labelReader = reader;
        int offset = labelReader.readShort() & 0x3fff;
        if (offsets.contains(offset)) {
            throw new ParseException(
                "pointer loop detected in compressed DNS label");
        }
        offsets.add(offset);

        OctetReader expandedReader = ptrReader.dupOctetReader();
        expandedReader.skip(offset);
        boolean done = false;
        while (!done) {
            DnsLabel label = DnsLabel.parse(expandedReader, ptrReader,
                offsets);
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
    public final int length() {
        int len = 0;
        for (DnsLabel label : labels) {
            len += label.length();
        }
        return len;
    }

    @Override
    public final boolean isFinal() {
        return true;
    }

    @Override
    public final boolean equals(Object thatObject) {
        if (thatObject == this) {
            return true;
        }
        if (thatObject == null) {
            return false;
        }
        if (thatObject instanceof DnsCompressedLabel) {
            DnsCompressedLabel that = (DnsCompressedLabel) thatObject;
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
    public final void buildString(StringBuilder builder) {
        boolean first = true;
        for (DnsLabel label : labels) {
            if (first) {
                first = false;
            } else {
                builder.append(".");
            }
            label.buildString(builder);
        }
    }
}
