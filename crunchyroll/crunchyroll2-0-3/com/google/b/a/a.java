// 
// Decompiled by Procyon v0.5.30
// 

package com.google.b.a;

import java.io.IOException;

public final class a
{
    private final byte[] a;
    private final int b;
    private int c;
    
    private a(final byte[] a, final int c, final int n) {
        this.a = a;
        this.c = c;
        this.b = c + n;
    }
    
    public static a a(final byte[] array) {
        return a(array, 0, array.length);
    }
    
    public static a a(final byte[] array, final int n, final int n2) {
        return new a(array, n, n2);
    }
    
    public int a() {
        return this.b - this.c;
    }
    
    public void a(final byte b) throws IOException {
        if (this.c == this.b) {
            throw new a(this.c, this.b);
        }
        this.a[this.c++] = b;
    }
    
    public void a(final int n) throws IOException {
        this.a((byte)n);
    }
    
    public void a(final int n, final int n2) throws IOException {
        this.b(com.google.b.a.b.a(n, n2));
    }
    
    public void a(final int n, final long n2) throws IOException {
        this.a(n, 0);
        this.a(n2);
    }
    
    public void a(final int n, final String s) throws IOException {
        this.a(n, 2);
        this.a(s);
    }
    
    public void a(final long n) throws IOException {
        this.b(n);
    }
    
    public void a(final String s) throws IOException {
        final byte[] bytes = s.getBytes("UTF-8");
        this.b(bytes.length);
        this.b(bytes);
    }
    
    public void b(int n) throws IOException {
        while ((n & 0xFFFFFF80) != 0x0) {
            this.a((n & 0x7F) | 0x80);
            n >>>= 7;
        }
        this.a(n);
    }
    
    public void b(long n) throws IOException {
        while ((0xFFFFFFFFFFFFFF80L & n) != 0x0L) {
            this.a(((int)n & 0x7F) | 0x80);
            n >>>= 7;
        }
        this.a((int)n);
    }
    
    public void b(final byte[] array) throws IOException {
        this.b(array, 0, array.length);
    }
    
    public void b(final byte[] array, final int n, final int n2) throws IOException {
        if (this.b - this.c >= n2) {
            System.arraycopy(array, n, this.a, this.c, n2);
            this.c += n2;
            return;
        }
        throw new a(this.c, this.b);
    }
    
    public static class a extends IOException
    {
        a(final int n, final int n2) {
            super("CodedOutputStream was writing to a flat byte array and ran out of space (pos " + n + " limit " + n2 + ").");
        }
    }
}
