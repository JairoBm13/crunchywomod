// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import android.text.TextUtils;
import java.util.HashMap;

public final class zznx extends zznq<zznx>
{
    private String zzLU;
    private String zzLV;
    private String zzaEw;
    private String zzaEx;
    
    public void setAppId(final String zzaEw) {
        this.zzaEw = zzaEw;
    }
    
    public void setAppInstallerId(final String zzaEx) {
        this.zzaEx = zzaEx;
    }
    
    public void setAppName(final String zzLU) {
        this.zzLU = zzLU;
    }
    
    public void setAppVersion(final String zzLV) {
        this.zzLV = zzLV;
    }
    
    @Override
    public String toString() {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("appName", this.zzLU);
        hashMap.put("appVersion", this.zzLV);
        hashMap.put("appId", this.zzaEw);
        hashMap.put("appInstallerId", this.zzaEx);
        return zznq.zzy(hashMap);
    }
    
    @Override
    public void zza(final zznx zznx) {
        if (!TextUtils.isEmpty((CharSequence)this.zzLU)) {
            zznx.setAppName(this.zzLU);
        }
        if (!TextUtils.isEmpty((CharSequence)this.zzLV)) {
            zznx.setAppVersion(this.zzLV);
        }
        if (!TextUtils.isEmpty((CharSequence)this.zzaEw)) {
            zznx.setAppId(this.zzaEw);
        }
        if (!TextUtils.isEmpty((CharSequence)this.zzaEx)) {
            zznx.setAppInstallerId(this.zzaEx);
        }
    }
    
    public String zzjL() {
        return this.zzLU;
    }
    
    public String zzjN() {
        return this.zzLV;
    }
    
    public String zzsK() {
        return this.zzaEw;
    }
    
    public String zzwi() {
        return this.zzaEx;
    }
}
