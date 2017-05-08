// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.core;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

final class ByteString
{
    public static final ByteString EMPTY;
    private final byte[] bytes;
    private volatile int hash;
    
    static {
        EMPTY = new ByteString(new byte[0]);
    }
    
    private ByteString(final byte[] bytes) {
        this.hash = 0;
        this.bytes = bytes;
    }
    
    public static ByteString copyFrom(final byte[] array, final int n, final int n2) {
        final byte[] array2 = new byte[n2];
        System.arraycopy(array, n, array2, 0, n2);
        return new ByteString(array2);
    }
    
    public static ByteString copyFromUtf8(final String s) {
        try {
            return new ByteString(s.getBytes("UTF-8"));
        }
        catch (UnsupportedEncodingException ex) {
            throw new RuntimeException("UTF-8 not supported.", ex);
        }
    }
    
    public void copyTo(final byte[] array, final int n, final int n2, final int n3) {
        System.arraycopy(this.bytes, n, array, n2, n3);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o != this) {
            if (!(o instanceof ByteString)) {
                return false;
            }
            final ByteString byteString = (ByteString)o;
            final int length = this.bytes.length;
            if (length != byteString.bytes.length) {
                return false;
            }
            final byte[] bytes = this.bytes;
            final byte[] bytes2 = byteString.bytes;
            for (int i = 0; i < length; ++i) {
                if (bytes[i] != bytes2[i]) {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        int hash;
        if ((hash = this.hash) == 0) {
            final byte[] bytes = this.bytes;
            int length;
            for (int n = length = this.bytes.length, i = 0; i < n; ++i) {
                length = length * 31 + bytes[i];
            }
            if ((hash = length) == 0) {
                hash = 1;
            }
            this.hash = hash;
        }
        return hash;
    }
    
    public InputStream newInput() {
        return new ByteArrayInputStream(this.bytes);
    }
    
    public int size() {
        return this.bytes.length;
    }
}
