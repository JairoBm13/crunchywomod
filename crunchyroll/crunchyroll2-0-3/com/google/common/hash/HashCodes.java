// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.hash;

import java.io.Serializable;

public final class HashCodes
{
    static HashCode fromBytesNoCopy(final byte[] array) {
        return new BytesHashCode(array);
    }
    
    public static HashCode fromInt(final int n) {
        return new IntHashCode(n);
    }
    
    private static final class BytesHashCode extends HashCode implements Serializable
    {
        final byte[] bytes;
        
        BytesHashCode(final byte[] bytes) {
            this.bytes = bytes;
        }
        
        @Override
        public byte[] asBytes() {
            return this.bytes.clone();
        }
        
        @Override
        public int asInt() {
            return (this.bytes[0] & 0xFF) | (this.bytes[1] & 0xFF) << 8 | (this.bytes[2] & 0xFF) << 16 | (this.bytes[3] & 0xFF) << 24;
        }
    }
    
    private static final class IntHashCode extends HashCode implements Serializable
    {
        final int hash;
        
        IntHashCode(final int hash) {
            this.hash = hash;
        }
        
        @Override
        public byte[] asBytes() {
            return new byte[] { (byte)this.hash, (byte)(this.hash >> 8), (byte)(this.hash >> 16), (byte)(this.hash >> 24) };
        }
        
        @Override
        public int asInt() {
            return this.hash;
        }
    }
}
