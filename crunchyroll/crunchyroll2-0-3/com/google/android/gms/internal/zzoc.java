// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import android.text.TextUtils;
import java.util.HashMap;

public final class zzoc extends zznq<zzoc>
{
    public boolean zzaEI;
    public String zzakM;
    
    public String getDescription() {
        return this.zzakM;
    }
    
    public void setDescription(final String zzakM) {
        this.zzakM = zzakM;
    }
    
    @Override
    public String toString() {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("description", this.zzakM);
        hashMap.put("fatal", (String)this.zzaEI);
        return zznq.zzy(hashMap);
    }
    
    @Override
    public void zza(final zzoc zzoc) {
        if (!TextUtils.isEmpty((CharSequence)this.zzakM)) {
            zzoc.setDescription(this.zzakM);
        }
        if (this.zzaEI) {
            zzoc.zzag(this.zzaEI);
        }
    }
    
    public void zzag(final boolean zzaEI) {
        this.zzaEI = zzaEI;
    }
    
    public boolean zzwz() {
        return this.zzaEI;
    }
}
