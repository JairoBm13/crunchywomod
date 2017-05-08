// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.internal;

import android.content.ServiceConnection;
import android.content.Context;

public abstract class zzk
{
    private static final Object zzaaJ;
    private static zzk zzaaK;
    
    static {
        zzaaJ = new Object();
    }
    
    public static zzk zzah(final Context context) {
        synchronized (zzk.zzaaJ) {
            if (zzk.zzaaK == null) {
                zzk.zzaaK = new zzl(context.getApplicationContext());
            }
            return zzk.zzaaK;
        }
    }
    
    public abstract boolean zza(final String p0, final ServiceConnection p1, final String p2);
    
    public abstract void zzb(final String p0, final ServiceConnection p1, final String p2);
}
