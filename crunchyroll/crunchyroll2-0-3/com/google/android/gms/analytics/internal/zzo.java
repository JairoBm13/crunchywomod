// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics.internal;

public enum zzo
{
    zzKK, 
    zzKL;
    
    public static zzo zzbd(final String s) {
        if ("GZIP".equalsIgnoreCase(s)) {
            return zzo.zzKL;
        }
        return zzo.zzKK;
    }
}
