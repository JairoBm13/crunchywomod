// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import android.text.TextUtils;
import java.util.HashMap;

public final class zznz extends zznq<zznz>
{
    private String zzRA;
    public int zzaEE;
    public int zzaEF;
    public int zzaEG;
    public int zzyW;
    public int zzyX;
    
    public String getLanguage() {
        return this.zzRA;
    }
    
    public void setLanguage(final String zzRA) {
        this.zzRA = zzRA;
    }
    
    @Override
    public String toString() {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("language", this.zzRA);
        hashMap.put("screenColors", (String)this.zzaEE);
        hashMap.put("screenWidth", (String)this.zzyW);
        hashMap.put("screenHeight", (String)this.zzyX);
        hashMap.put("viewportWidth", (String)this.zzaEF);
        hashMap.put("viewportHeight", (String)this.zzaEG);
        return zznq.zzy(hashMap);
    }
    
    @Override
    public void zza(final zznz zznz) {
        if (this.zzaEE != 0) {
            zznz.zzhF(this.zzaEE);
        }
        if (this.zzyW != 0) {
            zznz.zzhG(this.zzyW);
        }
        if (this.zzyX != 0) {
            zznz.zzhH(this.zzyX);
        }
        if (this.zzaEF != 0) {
            zznz.zzhI(this.zzaEF);
        }
        if (this.zzaEG != 0) {
            zznz.zzhJ(this.zzaEG);
        }
        if (!TextUtils.isEmpty((CharSequence)this.zzRA)) {
            zznz.setLanguage(this.zzRA);
        }
    }
    
    public void zzhF(final int zzaEE) {
        this.zzaEE = zzaEE;
    }
    
    public void zzhG(final int zzyW) {
        this.zzyW = zzyW;
    }
    
    public void zzhH(final int zzyX) {
        this.zzyX = zzyX;
    }
    
    public void zzhI(final int zzaEF) {
        this.zzaEF = zzaEF;
    }
    
    public void zzhJ(final int zzaEG) {
        this.zzaEG = zzaEG;
    }
    
    public int zzwp() {
        return this.zzaEE;
    }
    
    public int zzwq() {
        return this.zzyW;
    }
    
    public int zzwr() {
        return this.zzyX;
    }
    
    public int zzws() {
        return this.zzaEF;
    }
    
    public int zzwt() {
        return this.zzaEG;
    }
}
