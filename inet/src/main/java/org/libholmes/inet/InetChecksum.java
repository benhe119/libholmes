// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes.inet;

import org.libholmes.OctetString;

/** A class to represent an Internet Protocol checksum.
 * This checksum algorithm is used by several internet-related protocols
 * including IPv4, UDP, TCP and ICMP.
 *
 * The checksum is defined in RFC 791 to be the ones-complement of the
 * ones-complement sum of the 16-bit words to be checksummed. A more
 * detailed description can be found in RFC 1071.
 *
 * The implementation provided by this class does not limit the number
 * of words which can be incorporated into the checksum.
 */
public class InetChecksum {
    /** The accumulated checksum.
     * The value recorded here may become temporarily
     * denormalised, however by the end of any given method call
     * it will have been normalised into the range 0x0000 to
     * 0xffff. It must be inverted before use.
     */
    private int sum = 0;

    /** Incorporate a 16-bit word into the checksum.
     * High order bits beyond the first 16 are disregarded, therefore it is
     * immaterial whether the argument has been sign-extended or not.
     * @param word the 16-bit word to be incorporated
     */
    public final void add(int word) {
        sum += word & 0xffff;
        if (sum > 0xffff) {
            int carry = (sum >> 16) & 0xffff;
            sum = (sum & 0xffff) + carry;
        }
    }

    /** Incorporate an OctetString into the checksum.
     * If the OctetString contains an odd number of octets then it is
     * padded with a zero for incorporation into the checksum. Be aware
     * therefore that the effect of separately incorporating two odd-length
     * OctetStrings is not the same as concatenating them beforehand.
     * @param data the OctetString to be incorporated
     */
    public final void add(OctetString data) {
        // Handle complete 16-bit words.
        int length = data.length();
        int index = 0;
        while (index + 1 < length) {
            sum += data.getShort(index) & 0xffff;
            index += 2;
            if (sum > 0xffff) {
                int carry = (sum >> 16) & 0xffff;
                sum = (sum & 0xffff) + carry;
            }
        }

        // Handle remaining byte at end, if there is one.
        if ((length & 1) != 0) {
            sum += (data.getByte(length - 1) & 0xff) << 8;
            if (sum > 0xffff) {
                int carry = (sum >> 16) & 0xffff;
                sum = (sum & 0xffff) + carry;
            }
        }
    }

    /** Get resulting checksum.
     * @return the checksum value
     */
    public final int get() {
        return sum ^ 0xffff;
    }

    /** Check whether an embedded checksum had the expected value.
     * The IP checksum algorithm has the property that data which
     * contains an embedded checksum field (aligned on a 16-bit
     * boundary) can be verified by applying the algoirthm in the
     * usual manner to the whole of the data. The finalised result
     * will be equal to zero if and only if the checksum field had
     * the expected value.
     *
     * This function should be called after all of the data,
     * including the embedded checksum field, has been incorporated
     * into the checksum. It is immaterial whereabouts the embedded
     * checksum field is located within the data, provided that it
     * is aligned on a 16-bit boundary.
     * @return true if the embedded checksum field had the expected
     *  value, otherwise false.
     */
    public final boolean isValid() {
        return get() == 0x0000;
    }
}
