// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics.internal;

public enum zzm
{
    zzKA, 
    zzKB, 
    zzKC, 
    zzKD, 
    zzKE, 
    zzKz;
    
    public static zzm zzbc(final String s) {
        if ("BATCH_BY_SESSION".equalsIgnoreCase(s)) {
            return zzm.zzKA;
        }
        if ("BATCH_BY_TIME".equalsIgnoreCase(s)) {
            return zzm.zzKB;
        }
        if ("BATCH_BY_BRUTE_FORCE".equalsIgnoreCase(s)) {
            return zzm.zzKC;
        }
        if ("BATCH_BY_COUNT".equalsIgnoreCase(s)) {
            return zzm.zzKD;
        }
        if ("BATCH_BY_SIZE".equalsIgnoreCase(s)) {
            return zzm.zzKE;
        }
        return zzm.zzKz;
    }
}
