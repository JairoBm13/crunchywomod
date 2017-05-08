// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics;

import com.google.android.gms.analytics.internal.zzae;

public final class zzc
{
    public static String zzN(final int n) {
        return zzc("&cd", n);
    }
    
    public static String zzO(final int n) {
        return zzc("cd", n);
    }
    
    public static String zzQ(final int n) {
        return zzc("cm", n);
    }
    
    public static String zzR(final int n) {
        return zzc("&pr", n);
    }
    
    public static String zzS(final int n) {
        return zzc("pr", n);
    }
    
    public static String zzT(final int n) {
        return zzc("&promo", n);
    }
    
    public static String zzU(final int n) {
        return zzc("promo", n);
    }
    
    public static String zzV(final int n) {
        return zzc("pi", n);
    }
    
    public static String zzW(final int n) {
        return zzc("&il", n);
    }
    
    public static String zzX(final int n) {
        return zzc("il", n);
    }
    
    private static String zzc(final String s, final int n) {
        if (n < 1) {
            zzae.zzf("index out of range for prefix", s);
            return "";
        }
        return s + n;
    }
}
