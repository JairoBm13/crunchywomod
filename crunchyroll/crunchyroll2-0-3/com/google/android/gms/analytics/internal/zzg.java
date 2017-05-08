// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics.internal;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.internal.zzld;
import com.google.android.gms.internal.zzlb;
import com.google.android.gms.internal.zzns;
import com.google.android.gms.common.internal.zzu;
import android.content.Context;

public class zzg
{
    private final Context zzJR;
    private final Context zzqw;
    
    public zzg(Context applicationContext) {
        zzu.zzu(applicationContext);
        applicationContext = applicationContext.getApplicationContext();
        zzu.zzb(applicationContext, "Application context can't be null");
        this.zzqw = applicationContext;
        this.zzJR = applicationContext;
    }
    
    public Context getApplicationContext() {
        return this.zzqw;
    }
    
    protected zzns zzW(final Context context) {
        return zzns.zzaB(context);
    }
    
    protected com.google.android.gms.analytics.internal.zzu zza(final zzf zzf) {
        return new com.google.android.gms.analytics.internal.zzu(zzf);
    }
    
    protected zzk zzb(final zzf zzf) {
        return new zzk(zzf);
    }
    
    protected zza zzc(final zzf zzf) {
        return new zza(zzf);
    }
    
    protected zzn zzd(final zzf zzf) {
        return new zzn(zzf);
    }
    
    protected zzan zze(final zzf zzf) {
        return new zzan(zzf);
    }
    
    protected zzaf zzf(final zzf zzf) {
        return new zzaf(zzf);
    }
    
    protected zzr zzg(final zzf zzf) {
        return new zzr(zzf);
    }
    
    protected zzlb zzh(final zzf zzf) {
        return zzld.zzoQ();
    }
    
    protected GoogleAnalytics zzi(final zzf zzf) {
        return new GoogleAnalytics(zzf);
    }
    
    public Context zzic() {
        return this.zzJR;
    }
    
    zzl zzj(final zzf zzf) {
        return new zzl(zzf, this);
    }
    
    zzag zzk(final zzf zzf) {
        return new zzag(zzf);
    }
    
    protected zzb zzl(final zzf zzf) {
        return new zzb(zzf, this);
    }
    
    public zzj zzm(final zzf zzf) {
        return new zzj(zzf);
    }
    
    public zzah zzn(final zzf zzf) {
        return new zzah(zzf);
    }
    
    public zzi zzo(final zzf zzf) {
        return new zzi(zzf);
    }
    
    public zzv zzp(final zzf zzf) {
        return new zzv(zzf);
    }
    
    public zzai zzq(final zzf zzf) {
        return new zzai(zzf);
    }
}
