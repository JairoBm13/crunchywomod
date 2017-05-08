// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics.internal;

import com.google.android.gms.internal.zznx;

public class zzk extends zzd
{
    private final zznx zzKm;
    
    zzk(final zzf zzf) {
        super(zzf);
        this.zzKm = new zznx();
    }
    
    public void zzhi() {
        final zzan zzhm = this.zzhm();
        final String zzjL = zzhm.zzjL();
        if (zzjL != null) {
            this.zzKm.setAppName(zzjL);
        }
        final String zzjN = zzhm.zzjN();
        if (zzjN != null) {
            this.zzKm.setAppVersion(zzjN);
        }
    }
    
    @Override
    protected void zzhn() {
        this.zzhS().zzwc().zza(this.zzKm);
        this.zzhi();
    }
    
    public zznx zzix() {
        this.zzia();
        return this.zzKm;
    }
}
