// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.hash;

import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import java.io.Serializable;

final class Murmur3_32HashFunction extends AbstractStreamingHashFunction implements Serializable
{
    private final int seed;
    
    Murmur3_32HashFunction(final int seed) {
        this.seed = seed;
    }
    
    private static HashCode fmix(int n, final int n2) {
        n ^= n2;
        n = (n ^ n >>> 16) * -2048144789;
        n = (n ^ n >>> 13) * -1028477387;
        return HashCodes.fromInt(n ^ n >>> 16);
    }
    
    private static int mixH1(final int n, final int n2) {
        return Integer.rotateLeft(n ^ n2, 13) * 5 - 430675100;
    }
    
    private static int mixK1(final int n) {
        return Integer.rotateLeft(n * -862048943, 15) * 461845907;
    }
    
    @Override
    public int bits() {
        return 32;
    }
    
    @Override
    public Hasher newHasher() {
        return new Murmur3_32Hasher(this.seed);
    }
    
    private static final class Murmur3_32Hasher extends AbstractStreamingHasher
    {
        private int h1;
        private int length;
        
        Murmur3_32Hasher(final int h1) {
            super(4);
            this.h1 = h1;
            this.length = 0;
        }
        
        public HashCode makeHash() {
            return fmix(this.h1, this.length);
        }
        
        @Override
        protected void process(final ByteBuffer byteBuffer) {
            this.h1 = mixH1(this.h1, mixK1(byteBuffer.getInt()));
            this.length += 4;
        }
        
        @Override
        protected void processRemaining(final ByteBuffer byteBuffer) {
            this.length += byteBuffer.remaining();
            int n = 0;
            int n2 = 0;
            switch (byteBuffer.remaining()) {
                default: {
                    throw new AssertionError((Object)"Should never get here.");
                }
                case 3: {
                    n2 = (0x0 ^ UnsignedBytes.toInt(byteBuffer.get(2)) << 16);
                }
                case 2: {
                    n = (n2 ^ UnsignedBytes.toInt(byteBuffer.get(1)) << 8);
                }
                case 1: {
                    this.h1 ^= mixK1(n ^ UnsignedBytes.toInt(byteBuffer.get(0)));
                }
            }
        }
    }
}
