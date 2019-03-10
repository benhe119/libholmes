// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.icmp;

import java.util.Map;
import java.util.HashMap;

import org.libholmes.AnalysisContext;
import org.libholmes.Artefact;
import org.libholmes.Matcher;

/** A matcher class for incrementing ICMPv4 echo request sequence numbers.
 * The current implementation assumes that host byte order is little-endian.
 */
public class Icmp4EchoStepSequenceMatcher extends Matcher {
    /** The byte order used by the sequence number. */
    private final int byteOrder;

    /** The most recently processed sequence number, or null if none. */
    private Integer curSeq = null;

    /** A histogram of differences between adjacent sequence numbers. */
    private final HashMap<Integer, Integer> histogram =
        new HashMap<Integer, Integer>();

    /** Construct Icmp4EchoStepSequenceMatcher.
     * @param byteOrder the required byte order
     */
    public Icmp4EchoStepSequenceMatcher(int byteOrder) {
        this.byteOrder = byteOrder;
    }

    @Override
    public final void add(Artefact artefact, AnalysisContext context) {
        Icmp4EchoMessage echo = (Icmp4EchoMessage) artefact;
        int newSeq = echo.getSequenceNumber();
        if (byteOrder == Icmp4EchoFingerprint.LITTLE_ENDIAN) {
            newSeq = ((newSeq << 8) & 0xff) | ((newSeq >> 8) & 0xff);
        }
        if (curSeq != null) {
            int diff = newSeq - curSeq;
            int count = histogram.getOrDefault(diff, 0) + 1;
            histogram.put(diff, count);
        }
        curSeq = newSeq;
    }

    @Override
    public final boolean match(Artefact artefact, AnalysisContext context) {
        if (!(artefact instanceof Icmp4EchoMessage)) {
            return false;
        }
        return true;
    }

    @Override
    public final boolean matchAll() {
        int mode = 0;
        int count = 0;
        for (Map.Entry<Integer, Integer> entry : histogram.entrySet()) {
            if (entry.getValue() > count) {
                mode = entry.getKey();
                count = entry.getValue();
            }
        }
        return (mode == 1);
    }
}
