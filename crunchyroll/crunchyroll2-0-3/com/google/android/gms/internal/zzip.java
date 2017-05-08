// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import android.text.TextUtils;
import java.util.HashMap;
import com.google.android.gms.common.internal.zzu;

public final class zzip extends zznq<zzip>
{
    private String zzEO;
    private String zzJc;
    private String zzJd;
    private String zzJe;
    private boolean zzJf;
    private String zzJg;
    private boolean zzJh;
    private double zzJi;
    
    public String getClientId() {
        return this.zzJd;
    }
    
    public String getUserId() {
        return this.zzEO;
    }
    
    public void setClientId(final String zzJd) {
        this.zzJd = zzJd;
    }
    
    public void setSampleRate(final double zzJi) {
        zzu.zzb(zzJi >= 0.0 && zzJi <= 100.0, "Sample rate must be between 0% and 100%");
        this.zzJi = zzJi;
    }
    
    public void setUserId(final String zzEO) {
        this.zzEO = zzEO;
    }
    
    @Override
    public String toString() {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("hitType", this.zzJc);
        hashMap.put("clientId", this.zzJd);
        hashMap.put("userId", this.zzEO);
        hashMap.put("androidAdId", this.zzJe);
        hashMap.put("AdTargetingEnabled", (String)this.zzJf);
        hashMap.put("sessionControl", this.zzJg);
        hashMap.put("nonInteraction", (String)this.zzJh);
        hashMap.put("sampleRate", (String)this.zzJi);
        return zznq.zzy(hashMap);
    }
    
    public void zzE(final boolean zzJf) {
        this.zzJf = zzJf;
    }
    
    public void zzF(final boolean zzJh) {
        this.zzJh = zzJh;
    }
    
    @Override
    public void zza(final zzip zzip) {
        if (!TextUtils.isEmpty((CharSequence)this.zzJc)) {
            zzip.zzaN(this.zzJc);
        }
        if (!TextUtils.isEmpty((CharSequence)this.zzJd)) {
            zzip.setClientId(this.zzJd);
        }
        if (!TextUtils.isEmpty((CharSequence)this.zzEO)) {
            zzip.setUserId(this.zzEO);
        }
        if (!TextUtils.isEmpty((CharSequence)this.zzJe)) {
            zzip.zzaO(this.zzJe);
        }
        if (this.zzJf) {
            zzip.zzE(true);
        }
        if (!TextUtils.isEmpty((CharSequence)this.zzJg)) {
            zzip.zzaP(this.zzJg);
        }
        if (this.zzJh) {
            zzip.zzF(this.zzJh);
        }
        if (this.zzJi != 0.0) {
            zzip.setSampleRate(this.zzJi);
        }
    }
    
    public void zzaN(final String zzJc) {
        this.zzJc = zzJc;
    }
    
    public void zzaO(final String zzJe) {
        this.zzJe = zzJe;
    }
    
    public void zzaP(final String zzJg) {
        this.zzJg = zzJg;
    }
    
    public boolean zzhA() {
        return this.zzJh;
    }
    
    public double zzhB() {
        return this.zzJi;
    }
    
    public String zzhw() {
        return this.zzJc;
    }
    
    public String zzhx() {
        return this.zzJe;
    }
    
    public boolean zzhy() {
        return this.zzJf;
    }
    
    public String zzhz() {
        return this.zzJg;
    }
}
