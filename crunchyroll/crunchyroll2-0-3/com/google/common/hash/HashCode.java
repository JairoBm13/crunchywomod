// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.hash;

import java.security.MessageDigest;

public abstract class HashCode
{
    private static final char[] hexDigits;
    
    static {
        hexDigits = "0123456789abcdef".toCharArray();
    }
    
    public abstract byte[] asBytes();
    
    public abstract int asInt();
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof HashCode && MessageDigest.isEqual(this.asBytes(), ((HashCode)o).asBytes());
    }
    
    @Override
    public int hashCode() {
        return this.asInt();
    }
    
    @Override
    public String toString() {
        final byte[] bytes = this.asBytes();
        final StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (int length = bytes.length, i = 0; i < length; ++i) {
            final byte b = bytes[i];
            sb.append(HashCode.hexDigits[b >> 4 & 0xF]).append(HashCode.hexDigits[b & 0xF]);
        }
        return sb.toString();
    }
}
