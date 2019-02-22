// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.dns;

/** An abstract base class to represent a label within a DNS domain name. */
public abstract class DnsLabel {
    /** Get the length of this label.
     * This is the contribution made by this label towards the limit of 255
     * octets for the domain name as a whole. It includes the length field,
     * but does not include any additional space occupied by escape sequences
     * when the label is converted to text. For compressed labels, it is
     * the decompressed length which is measured.
     * @return the length, in octets
     */
    public abstract int length();

    /** Determine whether this label ends the domain name.
     * The final label either corresponds to, or contains, the root domain.
     * @return true if final, otherwise false
     */
    public abstract boolean isFinal();

    /** Append the content of this label to a StringBuilder.
     * @param builder the StringBuilder to receive the content
     */
    public abstract void buildString(StringBuilder builder);

    @Override
    public final String toString() {
        StringBuilder builder = new StringBuilder();
        buildString(builder);
        return builder.toString();
    }
}
