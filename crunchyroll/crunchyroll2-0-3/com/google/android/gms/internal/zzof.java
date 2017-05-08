// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import android.text.TextUtils;
import java.util.HashMap;

public final class zzof extends zznq<zzof>
{
    public String mCategory;
    public String zzaEH;
    public String zzaET;
    public long zzaEU;
    
    public String getLabel() {
        return this.zzaEH;
    }
    
    public long getTimeInMillis() {
        return this.zzaEU;
    }
    
    public void setTimeInMillis(final long zzaEU) {
        this.zzaEU = zzaEU;
    }
    
    @Override
    public String toString() {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("variableName", this.zzaET);
        hashMap.put("timeInMillis", (String)this.zzaEU);
        hashMap.put("category", this.mCategory);
        hashMap.put("label", this.zzaEH);
        return zznq.zzy(hashMap);
    }
    
    @Override
    public void zza(final zzof zzof) {
        if (!TextUtils.isEmpty((CharSequence)this.zzaET)) {
            zzof.zzdN(this.zzaET);
        }
        if (this.zzaEU != 0L) {
            zzof.setTimeInMillis(this.zzaEU);
        }
        if (!TextUtils.isEmpty((CharSequence)this.mCategory)) {
            zzof.zzdG(this.mCategory);
        }
        if (!TextUtils.isEmpty((CharSequence)this.zzaEH)) {
            zzof.zzdI(this.zzaEH);
        }
    }
    
    public void zzdG(final String mCategory) {
        this.mCategory = mCategory;
    }
    
    public void zzdI(final String zzaEH) {
        this.zzaEH = zzaEH;
    }
    
    public void zzdN(final String zzaET) {
        this.zzaET = zzaET;
    }
    
    public String zzwJ() {
        return this.zzaET;
    }
    
    public String zzwy() {
        return this.mCategory;
    }
}
