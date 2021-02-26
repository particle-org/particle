package com.particle.core.utils;

public class Murmur {
    /**
     * Helps convert integer to its unsigned value
     */
    private static final long UINT_MASK = 0xFFFFFFFFl;

    private static final long SEED = 0;

    /**
     * Compute the Murmur hash as described in the original source code.
     *
     * @param data the data that needs to be hashed
     * @return the computed hash value
     */
    public static long hash(long... data) {
        final long m = 0x5bd1e995l;
        final int r = 24;

        // Initialize the hash to a 'random' value
        long hash = ((SEED ^ (data.length * 4)) & UINT_MASK);

        for (int i = 0; i < data.length; i++) {
            hash = ((hash * m) & UINT_MASK);
            hash = ((hash ^ data[i]) & UINT_MASK);
        }

        hash ^= ((hash >>> 13) & UINT_MASK);
        hash = ((hash * m) & UINT_MASK);
        hash ^= hash >>> 15;

        return hash;
    }
}