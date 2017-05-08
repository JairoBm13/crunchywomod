// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import android.os.SystemClock;

public final class zzld implements zzlb
{
    private static zzld zzacK;
    
    public static zzlb zzoQ() {
        synchronized (zzld.class) {
            if (zzld.zzacK == null) {
                zzld.zzacK = new zzld();
            }
            return zzld.zzacK;
        }
    }
    
    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }
    
    @Override
    public long elapsedRealtime() {
        return SystemClock.elapsedRealtime();
    }
}
