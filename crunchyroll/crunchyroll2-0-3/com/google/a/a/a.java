// 
// Decompiled by Procyon v0.5.30
// 

package com.google.a.a;

public class a
{
    private final byte[] a;
    private int b;
    private int c;
    
    public a(final byte[] array) {
        this.a = new byte[256];
        for (int i = 0; i < 256; ++i) {
            this.a[i] = (byte)i;
        }
        int n = 0;
        for (int j = 0; j < 256; ++j) {
            n = (n + this.a[j] + array[j % array.length] & 0xFF);
            final byte b = this.a[j];
            this.a[j] = this.a[n];
            this.a[n] = b;
        }
        this.b = 0;
        this.c = 0;
    }
    
    public void a(final byte[] array) {
        int b = this.b;
        int c = this.c;
        for (int i = 0; i < array.length; ++i) {
            b = (b + 1 & 0xFF);
            c = (c + this.a[b] & 0xFF);
            final byte b2 = this.a[b];
            this.a[b] = this.a[c];
            this.a[c] = b2;
            array[i] ^= this.a[this.a[b] + this.a[c] & 0xFF];
        }
        this.b = b;
        this.c = c;
    }
}
