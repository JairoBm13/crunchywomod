// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import android.text.TextUtils;
import java.util.HashMap;
import android.util.Log;
import java.util.UUID;
import com.google.android.gms.common.internal.zzu;

public final class zzod extends zznq<zzod>
{
    private String zzaEJ;
    private int zzaEK;
    private int zzaEL;
    private String zzaEM;
    private String zzaEN;
    private boolean zzaEO;
    private boolean zzaEP;
    private boolean zzaEQ;
    
    public zzod() {
        this(false);
    }
    
    public zzod(final boolean b) {
        this(b, zzwA());
    }
    
    public zzod(final boolean zzaEP, final int zzaEK) {
        zzu.zzbw(zzaEK);
        this.zzaEK = zzaEK;
        this.zzaEP = zzaEP;
    }
    
    static int zzwA() {
        final UUID randomUUID = UUID.randomUUID();
        int n = (int)(randomUUID.getLeastSignificantBits() & 0x7FFFFFFFL);
        if (n == 0 && (n = (int)(randomUUID.getMostSignificantBits() & 0x7FFFFFFFL)) == 0) {
            Log.e("GAv4", "UUID.randomUUID() returned 0.");
            return Integer.MAX_VALUE;
        }
        return n;
    }
    
    private void zzwH() {
        if (this.zzaEQ) {
            throw new IllegalStateException("ScreenViewInfo is immutable");
        }
    }
    
    public void setScreenName(final String zzaEJ) {
        this.zzwH();
        this.zzaEJ = zzaEJ;
    }
    
    @Override
    public String toString() {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("screenName", this.zzaEJ);
        hashMap.put("interstitial", (String)this.zzaEO);
        hashMap.put("automatic", (String)this.zzaEP);
        hashMap.put("screenId", (String)this.zzaEK);
        hashMap.put("referrerScreenId", (String)this.zzaEL);
        hashMap.put("referrerScreenName", this.zzaEM);
        hashMap.put("referrerUri", this.zzaEN);
        return zznq.zzy(hashMap);
    }
    
    public void zzah(final boolean zzaEP) {
        this.zzwH();
        this.zzaEP = zzaEP;
    }
    
    public void zzai(final boolean zzaEO) {
        this.zzwH();
        this.zzaEO = zzaEO;
    }
    
    public int zzbn() {
        return this.zzaEK;
    }
    
    public void zzc(final zzod zzod) {
        if (!TextUtils.isEmpty((CharSequence)this.zzaEJ)) {
            zzod.setScreenName(this.zzaEJ);
        }
        if (this.zzaEK != 0) {
            zzod.zzhK(this.zzaEK);
        }
        if (this.zzaEL != 0) {
            zzod.zzhL(this.zzaEL);
        }
        if (!TextUtils.isEmpty((CharSequence)this.zzaEM)) {
            zzod.zzdJ(this.zzaEM);
        }
        if (!TextUtils.isEmpty((CharSequence)this.zzaEN)) {
            zzod.zzdK(this.zzaEN);
        }
        if (this.zzaEO) {
            zzod.zzai(this.zzaEO);
        }
        if (this.zzaEP) {
            zzod.zzah(this.zzaEP);
        }
    }
    
    public void zzdJ(final String zzaEM) {
        this.zzwH();
        this.zzaEM = zzaEM;
    }
    
    public void zzdK(final String zzaEN) {
        this.zzwH();
        if (TextUtils.isEmpty((CharSequence)zzaEN)) {
            this.zzaEN = null;
            return;
        }
        this.zzaEN = zzaEN;
    }
    
    public void zzhK(final int zzaEK) {
        this.zzwH();
        this.zzaEK = zzaEK;
    }
    
    public void zzhL(final int zzaEL) {
        this.zzwH();
        this.zzaEL = zzaEL;
    }
    
    public String zzwB() {
        return this.zzaEJ;
    }
    
    public String zzwE() {
        return this.zzaEN;
    }
}
