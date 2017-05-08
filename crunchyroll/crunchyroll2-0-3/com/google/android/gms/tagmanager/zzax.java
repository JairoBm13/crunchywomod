// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.tagmanager;

import android.content.Context;
import android.net.Uri;
import java.util.HashMap;
import java.util.Map;

class zzax
{
    private static String zzaLU;
    static Map<String, String> zzaLV;
    
    static {
        zzax.zzaLV = new HashMap<String, String>();
    }
    
    static String zzD(final String s, final String s2) {
        if (s2 != null) {
            return Uri.parse("http://hostname/?" + s).getQueryParameter(s2);
        }
        if (s.length() > 0) {
            return s;
        }
        return null;
    }
    
    static void zzex(final String zzaLU) {
        synchronized (zzax.class) {
            zzax.zzaLU = zzaLU;
        }
    }
    
    static void zzk(final Context context, final String s) {
        final String zzD = zzD(s, "conv");
        if (zzD != null && zzD.length() > 0) {
            zzax.zzaLV.put(zzD, s);
            zzcv.zza(context, "gtm_click_referrers", zzD, s);
        }
    }
}
