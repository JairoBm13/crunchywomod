// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.appdatasearch;

import java.util.HashMap;
import java.util.Map;

public class zzh
{
    private static final String[] zzNq;
    private static final Map<String, Integer> zzNr;
    
    static {
        int i = 0;
        zzNq = new String[] { "text1", "text2", "icon", "intent_action", "intent_data", "intent_data_id", "intent_extra_data", "suggest_large_icon", "intent_activity" };
        zzNr = new HashMap<String, Integer>(zzh.zzNq.length);
        while (i < zzh.zzNq.length) {
            zzh.zzNr.put(zzh.zzNq[i], i);
            ++i;
        }
    }
    
    public static String zzai(final int n) {
        if (n < 0 || n >= zzh.zzNq.length) {
            return null;
        }
        return zzh.zzNq[n];
    }
    
    public static int zzbq(final String s) {
        final Integer n = zzh.zzNr.get(s);
        if (n == null) {
            throw new IllegalArgumentException("[" + s + "] is not a valid global search section name");
        }
        return n;
    }
    
    public static int zzkL() {
        return zzh.zzNq.length;
    }
}
