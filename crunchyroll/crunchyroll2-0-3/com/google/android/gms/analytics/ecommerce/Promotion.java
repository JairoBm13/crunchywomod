// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics.ecommerce;

import java.util.Iterator;
import com.google.android.gms.internal.zznq;
import java.util.HashMap;
import java.util.Map;

public class Promotion
{
    Map<String, String> zzJj;
    
    public Promotion() {
        this.zzJj = new HashMap<String, String>();
    }
    
    @Override
    public String toString() {
        return zznq.zzD(this.zzJj);
    }
    
    public Map<String, String> zzaQ(final String s) {
        final HashMap<String, Object> hashMap = new HashMap<String, Object>();
        for (final Map.Entry<String, String> entry : this.zzJj.entrySet()) {
            hashMap.put(s + entry.getKey(), entry.getValue());
        }
        return (Map<String, String>)hashMap;
    }
}
