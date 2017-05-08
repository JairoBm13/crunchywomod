// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.hash;

import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.io.Serializable;

final class Murmur3_128HashFunction extends AbstractStreamingHashFunction implements Serializable
{
    private final int seed;
    
    Murmur3_128HashFunction(final int seed) {
        this.seed = seed;
    }
    
    @Override
    public int bits() {
        return 128;
    }
    
    @Override
    public Hasher newHasher() {
        return new Murmur3_128Hasher(this.seed);
    }
    
    private static final class Murmur3_128Hasher extends AbstractStreamingHasher
    {
        private long h1;
        private long h2;
        private int length;
        
        Murmur3_128Hasher(final int n) {
            super(16);
            this.h1 = n;
            this.h2 = n;
            this.length = 0;
        }
        
        private void bmix64(final long n, final long n2) {
            this.h1 ^= mixK1(n);
            this.h1 = Long.rotateLeft(this.h1, 27);
            this.h1 += this.h2;
            this.h1 = this.h1 * 5L + 1390208809L;
            this.h2 ^= mixK2(n2);
            this.h2 = Long.rotateLeft(this.h2, 31);
            this.h2 += this.h1;
            this.h2 = this.h2 * 5L + 944331445L;
        }
        
        private static long fmix64(long n) {
            n = (n ^ n >>> 33) * -49064778989728563L;
            n = (n ^ n >>> 33) * -4265267296055464877L;
            return n ^ n >>> 33;
        }
        
        private static long mixK1(final long n) {
            return Long.rotateLeft(n * -8663945395140668459L, 31) * 5545529020109919103L;
        }
        
        private static long mixK2(final long n) {
            return Long.rotateLeft(n * 5545529020109919103L, 33) * -8663945395140668459L;
        }
        
        public HashCode makeHash() {
            this.h1 ^= this.length;
            this.h2 ^= this.length;
            this.h1 += this.h2;
            this.h2 += this.h1;
            this.h1 = fmix64(this.h1);
            this.h2 = fmix64(this.h2);
            this.h1 += this.h2;
            this.h2 += this.h1;
            return HashCodes.fromBytesNoCopy(ByteBuffer.wrap(new byte[16]).order(ByteOrder.LITTLE_ENDIAN).putLong(this.h1).putLong(this.h2).array());
        }
        
        @Override
        protected void process(final ByteBuffer byteBuffer) {
            this.bmix64(byteBuffer.getLong(), byteBuffer.getLong());
            this.length += 16;
        }
        
        @Override
        protected void processRemaining(final ByteBuffer byteBuffer) {
            long n = 0L;
            long n2 = 0L;
            this.length += byteBuffer.remaining();
            long n3 = n2;
            long n4 = n2;
            long n5 = n2;
            long n6 = n2;
            long n7 = n2;
            long n8 = n2;
            long n9 = n2;
            long n10 = n;
            long n11 = n;
            long n12 = n;
            long n13 = n;
            long n14 = n;
            long n15 = 0L;
            switch (byteBuffer.remaining()) {
                default: {
                    throw new AssertionError((Object)"Should never get here.");
                }
                case 15: {
                    n3 = (0x0L ^ UnsignedBytes.toInt(byteBuffer.get(14)) << 48);
                }
                case 14: {
                    n4 = (n3 ^ UnsignedBytes.toInt(byteBuffer.get(13)) << 40);
                }
                case 13: {
                    n5 = (n4 ^ UnsignedBytes.toInt(byteBuffer.get(12)) << 32);
                }
                case 12: {
                    n6 = (n5 ^ UnsignedBytes.toInt(byteBuffer.get(11)) << 24);
                }
                case 11: {
                    n7 = (n6 ^ UnsignedBytes.toInt(byteBuffer.get(10)) << 16);
                }
                case 10: {
                    n8 = (n7 ^ UnsignedBytes.toInt(byteBuffer.get(9)) << 8);
                }
                case 9: {
                    n9 = (n8 ^ UnsignedBytes.toInt(byteBuffer.get(8)));
                }
                case 8: {
                    n15 = (0x0L ^ byteBuffer.getLong());
                    n2 = n9;
                    break;
                }
                case 7: {
                    n10 = (0x0L ^ UnsignedBytes.toInt(byteBuffer.get(6)) << 48);
                }
                case 6: {
                    n11 = (n10 ^ UnsignedBytes.toInt(byteBuffer.get(5)) << 40);
                }
                case 5: {
                    n12 = (n11 ^ UnsignedBytes.toInt(byteBuffer.get(4)) << 32);
                }
                case 4: {
                    n13 = (n12 ^ UnsignedBytes.toInt(byteBuffer.get(3)) << 24);
                }
                case 3: {
                    n14 = (n13 ^ UnsignedBytes.toInt(byteBuffer.get(2)) << 16);
                }
                case 2: {
                    n = (n14 ^ UnsignedBytes.toInt(byteBuffer.get(1)) << 8);
                }
                case 1: {
                    n15 = (n ^ UnsignedBytes.toInt(byteBuffer.get(0)));
                    break;
                }
            }
            this.h1 ^= mixK1(n15);
            this.h2 ^= mixK2(n2);
        }
    }
}
