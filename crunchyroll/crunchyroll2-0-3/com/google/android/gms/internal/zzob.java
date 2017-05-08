// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import android.text.TextUtils;
import java.util.HashMap;

public final class zzob extends zznq<zzob>
{
    private String mCategory;
    private String zzaEH;
    private long zzaoL;
    private String zzuO;
    
    public String getAction() {
        return this.zzuO;
    }
    
    public String getLabel() {
        return this.zzaEH;
    }
    
    public long getValue() {
        return this.zzaoL;
    }
    
    @Override
    public String toString() {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("category", this.mCategory);
        hashMap.put("action", this.zzuO);
        hashMap.put("label", this.zzaEH);
        hashMap.put("value", (String)this.zzaoL);
        return zznq.zzy(hashMap);
    }
    
    public void zzM(final long zzaoL) {
        this.zzaoL = zzaoL;
    }
    
    @Override
    public void zza(final zzob zzob) {
        if (!TextUtils.isEmpty((CharSequence)this.mCategory)) {
            zzob.zzdG(this.mCategory);
        }
        if (!TextUtils.isEmpty((CharSequence)this.zzuO)) {
            zzob.zzdH(this.zzuO);
        }
        if (!TextUtils.isEmpty((CharSequence)this.zzaEH)) {
            zzob.zzdI(this.zzaEH);
        }
        if (this.zzaoL != 0L) {
            zzob.zzM(this.zzaoL);
        }
    }
    
    public void zzdG(final String mCategory) {
        this.mCategory = mCategory;
    }
    
    public void zzdH(final String zzuO) {
        this.zzuO = zzuO;
    }
    
    public void zzdI(final String zzaEH) {
        this.zzaEH = zzaEH;
    }
    
    public String zzwy() {
        return this.mCategory;
    }
}
