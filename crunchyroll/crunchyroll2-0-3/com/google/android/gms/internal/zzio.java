// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import java.util.Collections;
import com.google.android.gms.common.internal.zzu;
import java.util.HashMap;
import java.util.Map;

public final class zzio extends zznq<zzio>
{
    private final Map<String, Object> zzyn;
    
    public zzio() {
        this.zzyn = new HashMap<String, Object>();
    }
    
    private String zzaM(final String s) {
        zzu.zzcj(s);
        String substring = s;
        if (s != null) {
            substring = s;
            if (s.startsWith("&")) {
                substring = s.substring(1);
            }
        }
        zzu.zzh(substring, "Name can not be empty or \"&\"");
        return substring;
    }
    
    public void set(String zzaM, final String s) {
        zzaM = this.zzaM(zzaM);
        this.zzyn.put(zzaM, s);
    }
    
    @Override
    public String toString() {
        return zznq.zzy(this.zzyn);
    }
    
    @Override
    public void zza(final zzio zzio) {
        zzu.zzu(zzio);
        zzio.zzyn.putAll(this.zzyn);
    }
    
    public Map<String, Object> zzhv() {
        return Collections.unmodifiableMap((Map<? extends String, ?>)this.zzyn);
    }
}
