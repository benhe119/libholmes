// This file is part of libholmes.
// Copyright 2019 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

import javax.json.JsonObject;

/** An OctetPattern class to match a random octet sequence.
 * A random sequence differs from an arbitrary sequence in that it is
 * required to have high entropy.
 */
public class RandomOctetPattern extends OctetPattern {
    /** The required integer length, in octets. */
    private final int length;

    /** Parse RandomOctetPattern from a specification in JSON format.
     * @param jsonSpec the specification to be parsed
     */
    public RandomOctetPattern(JsonObject jsonSpec) {
        this.length = jsonSpec.getInt("length");
        if (this.length < 0) {
            throw new RuntimeException(
                "invalid length for RandomOctetPattern");
        }
    }

    /** Calculate cumulative probability for binomial distribution.
     * @param n the number of bits
     * @param k the number of ones
     * @return the probability of seeing that number of ones or fewer
     */
    private static final double cumulativeBinomial(int n, int k) {
        double coefficient = 1;
        double sum = coefficient;
        for (int i = 0; i != k; ++i) {
            coefficient *= (n - i);
            coefficient /= (1 + i);
            sum += coefficient;
        }
        return sum / Math.pow(2, n);
    }

    /** Apply monobit test.
     * Each bit position is tested individually. In the current
     * implementation, the probability threshold is set at 0.001.
     * @return true if sufficiently random, otherwise false
     */
    private static final boolean testMonobit(byte[] data) {
        for (int i = 0; i != 8; ++i) {
            int count = 0;
            for (int j = 0; j != data.length; ++j) {
                int bit = (data[j] >> i) & 1;
                count += bit;
            }
            if (2 * count > data.length) {
                count = data.length - count;
            }
            double p = cumulativeBinomial(data.length, count);
            if (p < 0.001) {
                return false;
            }
        }
        return true;
    }

    @Override
    public final boolean matches(OctetReader reader,
        AnalysisContext context) {

        if (reader.remaining() < length) {
            return false;
        }
        byte[] data = reader.readBytes(length);
        return testMonobit(data);
    }
}
