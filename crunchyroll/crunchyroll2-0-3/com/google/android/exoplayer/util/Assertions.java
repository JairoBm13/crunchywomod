// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.util;

import android.text.TextUtils;

public final class Assertions
{
    public static void checkArgument(final boolean b) {
        if (!b) {
            throw new IllegalArgumentException();
        }
    }
    
    public static String checkNotEmpty(final String s) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            throw new IllegalArgumentException();
        }
        return s;
    }
    
    public static <T> T checkNotNull(final T t) {
        if (t == null) {
            throw new NullPointerException();
        }
        return t;
    }
    
    public static void checkState(final boolean b) {
        if (!b) {
            throw new IllegalStateException();
        }
    }
}
