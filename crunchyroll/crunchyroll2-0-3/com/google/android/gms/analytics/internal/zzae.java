// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics.internal;

import android.util.Log;
import com.google.android.gms.analytics.Logger;

@Deprecated
public class zzae
{
    private static volatile Logger zzMk;
    
    static {
        setLogger(new zzs());
    }
    
    public static Logger getLogger() {
        return zzae.zzMk;
    }
    
    public static void setLogger(final Logger zzMk) {
        zzae.zzMk = zzMk;
    }
    
    public static boolean zzL(final int n) {
        boolean b = false;
        if (getLogger() != null) {
            b = b;
            if (getLogger().getLogLevel() <= n) {
                b = true;
            }
        }
        return b;
    }
    
    public static void zzaC(final String s) {
        final zzaf zzkc = zzaf.zzkc();
        if (zzkc != null) {
            zzkc.zzaW(s);
        }
        else if (zzL(2)) {
            Log.w((String)zzy.zzLb.get(), s);
        }
        final Logger zzMk = zzae.zzMk;
        if (zzMk != null) {
            zzMk.warn(s);
        }
    }
    
    public static void zzf(final String s, final Object o) {
        final zzaf zzkc = zzaf.zzkc();
        if (zzkc != null) {
            zzkc.zze(s, o);
        }
        else if (zzL(3)) {
            String string;
            if (o != null) {
                string = s + ":" + o;
            }
            else {
                string = s;
            }
            Log.e((String)zzy.zzLb.get(), string);
        }
        final Logger zzMk = zzae.zzMk;
        if (zzMk != null) {
            zzMk.error(s);
        }
    }
}
