// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import java.util.Collections;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

public final class zzim extends zznq<zzim>
{
    private Map<Integer, String> zzJa;
    
    public zzim() {
        this.zzJa = new HashMap<Integer, String>(4);
    }
    
    @Override
    public String toString() {
        final HashMap<String, Object> hashMap = new HashMap<String, Object>();
        for (final Map.Entry<Integer, String> entry : this.zzJa.entrySet()) {
            hashMap.put("dimension" + entry.getKey(), entry.getValue());
        }
        return zznq.zzy(hashMap);
    }
    
    @Override
    public void zza(final zzim zzim) {
        zzim.zzJa.putAll(this.zzJa);
    }
    
    public Map<Integer, String> zzht() {
        return Collections.unmodifiableMap((Map<? extends Integer, ? extends String>)this.zzJa);
    }
}
