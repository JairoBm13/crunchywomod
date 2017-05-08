// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import java.util.Collections;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

public final class zzin extends zznq<zzin>
{
    private Map<Integer, Double> zzJb;
    
    public zzin() {
        this.zzJb = new HashMap<Integer, Double>(4);
    }
    
    @Override
    public String toString() {
        final HashMap<String, Object> hashMap = new HashMap<String, Object>();
        for (final Map.Entry<Integer, Double> entry : this.zzJb.entrySet()) {
            hashMap.put("metric" + entry.getKey(), entry.getValue());
        }
        return zznq.zzy(hashMap);
    }
    
    @Override
    public void zza(final zzin zzin) {
        zzin.zzJb.putAll(this.zzJb);
    }
    
    public Map<Integer, Double> zzhu() {
        return Collections.unmodifiableMap((Map<? extends Integer, ? extends Double>)this.zzJb);
    }
}
