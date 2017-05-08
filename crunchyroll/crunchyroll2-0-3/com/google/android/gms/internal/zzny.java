// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import android.text.TextUtils;
import java.util.HashMap;

public final class zzny extends zznq<zzny>
{
    private String mName;
    private String zzKI;
    private String zzaEA;
    private String zzaEB;
    private String zzaEC;
    private String zzaED;
    private String zzaEy;
    private String zzaEz;
    private String zzazL;
    private String zzuU;
    
    public String getContent() {
        return this.zzuU;
    }
    
    public String getId() {
        return this.zzKI;
    }
    
    public String getName() {
        return this.mName;
    }
    
    public String getSource() {
        return this.zzazL;
    }
    
    public void setName(final String mName) {
        this.mName = mName;
    }
    
    @Override
    public String toString() {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("name", this.mName);
        hashMap.put("source", this.zzazL);
        hashMap.put("medium", this.zzaEy);
        hashMap.put("keyword", this.zzaEz);
        hashMap.put("content", this.zzuU);
        hashMap.put("id", this.zzKI);
        hashMap.put("adNetworkId", this.zzaEA);
        hashMap.put("gclid", this.zzaEB);
        hashMap.put("dclid", this.zzaEC);
        hashMap.put("aclid", this.zzaED);
        return zznq.zzy(hashMap);
    }
    
    @Override
    public void zza(final zzny zzny) {
        if (!TextUtils.isEmpty((CharSequence)this.mName)) {
            zzny.setName(this.mName);
        }
        if (!TextUtils.isEmpty((CharSequence)this.zzazL)) {
            zzny.zzdx(this.zzazL);
        }
        if (!TextUtils.isEmpty((CharSequence)this.zzaEy)) {
            zzny.zzdy(this.zzaEy);
        }
        if (!TextUtils.isEmpty((CharSequence)this.zzaEz)) {
            zzny.zzdz(this.zzaEz);
        }
        if (!TextUtils.isEmpty((CharSequence)this.zzuU)) {
            zzny.zzdA(this.zzuU);
        }
        if (!TextUtils.isEmpty((CharSequence)this.zzKI)) {
            zzny.zzdB(this.zzKI);
        }
        if (!TextUtils.isEmpty((CharSequence)this.zzaEA)) {
            zzny.zzdC(this.zzaEA);
        }
        if (!TextUtils.isEmpty((CharSequence)this.zzaEB)) {
            zzny.zzdD(this.zzaEB);
        }
        if (!TextUtils.isEmpty((CharSequence)this.zzaEC)) {
            zzny.zzdE(this.zzaEC);
        }
        if (!TextUtils.isEmpty((CharSequence)this.zzaED)) {
            zzny.zzdF(this.zzaED);
        }
    }
    
    public void zzdA(final String zzuU) {
        this.zzuU = zzuU;
    }
    
    public void zzdB(final String zzKI) {
        this.zzKI = zzKI;
    }
    
    public void zzdC(final String zzaEA) {
        this.zzaEA = zzaEA;
    }
    
    public void zzdD(final String zzaEB) {
        this.zzaEB = zzaEB;
    }
    
    public void zzdE(final String zzaEC) {
        this.zzaEC = zzaEC;
    }
    
    public void zzdF(final String zzaED) {
        this.zzaED = zzaED;
    }
    
    public void zzdx(final String zzazL) {
        this.zzazL = zzazL;
    }
    
    public void zzdy(final String zzaEy) {
        this.zzaEy = zzaEy;
    }
    
    public void zzdz(final String zzaEz) {
        this.zzaEz = zzaEz;
    }
    
    public String zzwj() {
        return this.zzaEy;
    }
    
    public String zzwk() {
        return this.zzaEz;
    }
    
    public String zzwl() {
        return this.zzaEA;
    }
    
    public String zzwm() {
        return this.zzaEB;
    }
    
    public String zzwn() {
        return this.zzaEC;
    }
    
    public String zzwo() {
        return this.zzaED;
    }
}
