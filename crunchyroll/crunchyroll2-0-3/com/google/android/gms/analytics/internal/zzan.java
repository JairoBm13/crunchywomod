// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics.internal;

import android.os.Bundle;
import android.content.pm.ApplicationInfo;
import android.content.Context;
import android.content.pm.PackageManager$NameNotFoundException;

public class zzan extends zzd
{
    protected boolean zzIx;
    protected int zzKR;
    protected String zzLU;
    protected String zzLV;
    protected int zzLX;
    protected boolean zzML;
    protected boolean zzMM;
    protected boolean zzMN;
    
    public zzan(final zzf zzf) {
        super(zzf);
    }
    
    private static int zzbo(String lowerCase) {
        lowerCase = lowerCase.toLowerCase();
        if ("verbose".equals(lowerCase)) {
            return 0;
        }
        if ("info".equals(lowerCase)) {
            return 1;
        }
        if ("warning".equals(lowerCase)) {
            return 2;
        }
        if ("error".equals(lowerCase)) {
            return 3;
        }
        return -1;
    }
    
    public int getLogLevel() {
        this.zzia();
        return this.zzKR;
    }
    
    void zza(final zzaa zzaa) {
        this.zzaT("Loading global XML config values");
        if (zzaa.zzjK()) {
            this.zzb("XML config - app name", this.zzLU = zzaa.zzjL());
        }
        if (zzaa.zzjM()) {
            this.zzb("XML config - app version", this.zzLV = zzaa.zzjN());
        }
        if (zzaa.zzjO()) {
            final int zzbo = zzbo(zzaa.zzjP());
            if (zzbo >= 0) {
                this.zzKR = zzbo;
                this.zza("XML config - log level", zzbo);
            }
        }
        if (zzaa.zzjQ()) {
            final int zzjR = zzaa.zzjR();
            this.zzLX = zzjR;
            this.zzMM = true;
            this.zzb("XML config - dispatch period (sec)", zzjR);
        }
        if (zzaa.zzjS()) {
            final boolean zzjT = zzaa.zzjT();
            this.zzIx = zzjT;
            this.zzMN = true;
            this.zzb("XML config - dry run", zzjT);
        }
    }
    
    @Override
    protected void zzhn() {
        this.zzkI();
    }
    
    public String zzjL() {
        this.zzia();
        return this.zzLU;
    }
    
    public String zzjN() {
        this.zzia();
        return this.zzLV;
    }
    
    public boolean zzjO() {
        this.zzia();
        return this.zzML;
    }
    
    public boolean zzjQ() {
        this.zzia();
        return this.zzMM;
    }
    
    public boolean zzjS() {
        this.zzia();
        return this.zzMN;
    }
    
    public boolean zzjT() {
        this.zzia();
        return this.zzIx;
    }
    
    public int zzkH() {
        this.zzia();
        return this.zzLX;
    }
    
    protected void zzkI() {
        zzaa zzaa;
        while (true) {
            final Context context = this.getContext();
            ApplicationInfo applicationInfo;
            while (true) {
                try {
                    applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 129);
                    if (applicationInfo == null) {
                        this.zzaW("Couldn't get ApplicationInfo to load global config");
                        return;
                    }
                }
                catch (PackageManager$NameNotFoundException ex) {
                    this.zzd("PackageManager doesn't know about the app package", ex);
                    applicationInfo = null;
                    continue;
                }
                break;
            }
            final Bundle metaData = applicationInfo.metaData;
            if (metaData == null) {
                return;
            }
            final int int1 = metaData.getInt("com.google.android.gms.analytics.globalConfigResource");
            if (int1 <= 0) {
                return;
            }
            zzaa = new zzz(this.zzhM()).zzab(int1);
            if (zzaa != null) {
                break;
            }
            return;
        }
        this.zza(zzaa);
    }
}
