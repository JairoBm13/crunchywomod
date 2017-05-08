// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.hash;

import java.nio.ByteBuffer;
import com.google.common.base.Preconditions;

public final class Hashing
{
    private static final HashFunction GOOD_FAST_HASH_FUNCTION_128;
    private static final HashFunction GOOD_FAST_HASH_FUNCTION_32;
    private static final int GOOD_FAST_HASH_SEED;
    private static final HashFunction MD5;
    private static final Murmur3_128HashFunction MURMUR3_128;
    private static final Murmur3_32HashFunction MURMUR3_32;
    private static final HashFunction SHA_1;
    private static final HashFunction SHA_256;
    private static final HashFunction SHA_512;
    
    static {
        GOOD_FAST_HASH_SEED = (int)System.currentTimeMillis();
        GOOD_FAST_HASH_FUNCTION_32 = murmur3_32(Hashing.GOOD_FAST_HASH_SEED);
        GOOD_FAST_HASH_FUNCTION_128 = murmur3_128(Hashing.GOOD_FAST_HASH_SEED);
        MURMUR3_32 = new Murmur3_32HashFunction(0);
        MURMUR3_128 = new Murmur3_128HashFunction(0);
        MD5 = new MessageDigestHashFunction("MD5");
        SHA_1 = new MessageDigestHashFunction("SHA-1");
        SHA_256 = new MessageDigestHashFunction("SHA-256");
        SHA_512 = new MessageDigestHashFunction("SHA-512");
    }
    
    static int checkPositiveAndMakeMultipleOf32(final int n) {
        Preconditions.checkArgument(n > 0, (Object)"Number of bits must be positive");
        return n + 31 & 0xFFFFFFE0;
    }
    
    public static HashFunction goodFastHash(int i) {
        i = checkPositiveAndMakeMultipleOf32(i);
        if (i == 32) {
            return Hashing.GOOD_FAST_HASH_FUNCTION_32;
        }
        if (i <= 128) {
            return Hashing.GOOD_FAST_HASH_FUNCTION_128;
        }
        final int n = (i + 127) / 128;
        final HashFunction[] array = new HashFunction[n];
        array[0] = Hashing.GOOD_FAST_HASH_FUNCTION_128;
        int good_FAST_HASH_SEED = Hashing.GOOD_FAST_HASH_SEED;
        for (i = 1; i < n; ++i) {
            good_FAST_HASH_SEED += 1500450271;
            array[i] = murmur3_128(good_FAST_HASH_SEED);
        }
        return new ConcatenatedHashFunction(array);
    }
    
    public static HashFunction murmur3_128(final int n) {
        return new Murmur3_128HashFunction(n);
    }
    
    public static HashFunction murmur3_32(final int n) {
        return new Murmur3_32HashFunction(n);
    }
    
    static final class ConcatenatedHashFunction extends AbstractCompositeHashFunction
    {
        private final int bits;
        
        ConcatenatedHashFunction(final HashFunction... array) {
            super(array);
            int bits = 0;
            for (int length = array.length, i = 0; i < length; ++i) {
                bits += array[i].bits();
            }
            this.bits = bits;
        }
        
        @Override
        public int bits() {
            return this.bits;
        }
        
        @Override
        HashCode makeHash(final Hasher[] array) {
            final byte[] array2 = new byte[this.bits / 8];
            final ByteBuffer wrap = ByteBuffer.wrap(array2);
            for (int length = array.length, i = 0; i < length; ++i) {
                wrap.put(array[i].hash().asBytes());
            }
            return HashCodes.fromBytesNoCopy(array2);
        }
    }
}
