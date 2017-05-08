// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.a.b;

public final class a
{
    public static <T> T a(final T t) {
        if (t == null) {
            throw new NullPointerException();
        }
        return t;
    }
    
    public static void a(final boolean b) {
        if (!b) {
            throw new IllegalArgumentException();
        }
    }
}
