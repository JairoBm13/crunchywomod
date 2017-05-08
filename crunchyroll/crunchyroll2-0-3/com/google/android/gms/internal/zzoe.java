// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import android.text.TextUtils;
import java.util.HashMap;

public final class zzoe extends zznq<zzoe>
{
    public String zzaER;
    public String zzaES;
    public String zzuO;
    
    public String getAction() {
        return this.zzuO;
    }
    
    public String getTarget() {
        return this.zzaES;
    }
    
    @Override
    public String toString() {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("network", this.zzaER);
        hashMap.put("action", this.zzuO);
        hashMap.put("target", this.zzaES);
        return zznq.zzy(hashMap);
    }
    
    @Override
    public void zza(final zzoe zzoe) {
        if (!TextUtils.isEmpty((CharSequence)this.zzaER)) {
            zzoe.zzdL(this.zzaER);
        }
        if (!TextUtils.isEmpty((CharSequence)this.zzuO)) {
            zzoe.zzdH(this.zzuO);
        }
        if (!TextUtils.isEmpty((CharSequence)this.zzaES)) {
            zzoe.zzdM(this.zzaES);
        }
    }
    
    public void zzdH(final String zzuO) {
        this.zzuO = zzuO;
    }
    
    public void zzdL(final String zzaER) {
        this.zzaER = zzaER;
    }
    
    public void zzdM(final String zzaES) {
        this.zzaES = zzaES;
    }
    
    public String zzwI() {
        return this.zzaER;
    }
}
