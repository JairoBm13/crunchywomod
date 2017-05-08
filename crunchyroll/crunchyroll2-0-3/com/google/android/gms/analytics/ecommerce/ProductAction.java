// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics.ecommerce;

import java.util.Iterator;
import com.google.android.gms.internal.zznq;
import java.util.HashMap;
import java.util.Map;

public class ProductAction
{
    Map<String, String> zzJj;
    
    public Map<String, String> build() {
        return new HashMap<String, String>(this.zzJj);
    }
    
    @Override
    public String toString() {
        final HashMap<String, Object> hashMap = new HashMap<String, Object>();
        for (final Map.Entry<String, String> entry : this.zzJj.entrySet()) {
            if (entry.getKey().startsWith("&")) {
                hashMap.put(entry.getKey().substring(1), entry.getValue());
            }
            else {
                hashMap.put(entry.getKey(), entry.getValue());
            }
        }
        return zznq.zzD(hashMap);
    }
}
