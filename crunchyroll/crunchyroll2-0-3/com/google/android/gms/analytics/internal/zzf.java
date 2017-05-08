// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics.internal;

import com.google.android.gms.internal.zzld;
import android.app.Application;
import com.google.android.gms.internal.zzlb;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.internal.zzns;
import android.content.Context;

public class zzf
{
    private static zzf zzJC;
    private final Context mContext;
    private final Context zzJD;
    private final zzr zzJE;
    private final zzaf zzJF;
    private final zzns zzJG;
    private final zzb zzJH;
    private final zzv zzJI;
    private final zzan zzJJ;
    private final zzai zzJK;
    private final GoogleAnalytics zzJL;
    private final zzn zzJM;
    private final zza zzJN;
    private final zzk zzJO;
    private final zzu zzJP;
    private final zzlb zzpw;
    
    protected zzf(final zzg zzg) {
        final Context applicationContext = zzg.getApplicationContext();
        com.google.android.gms.common.internal.zzu.zzb(applicationContext, "Application context can't be null");
        com.google.android.gms.common.internal.zzu.zzb(applicationContext instanceof Application, "getApplicationContext didn't return the application");
        final Context zzic = zzg.zzic();
        com.google.android.gms.common.internal.zzu.zzu(zzic);
        this.mContext = applicationContext;
        this.zzJD = zzic;
        this.zzpw = zzg.zzh(this);
        this.zzJE = zzg.zzg(this);
        final zzaf zzf = zzg.zzf(this);
        zzf.zza();
        this.zzJF = zzf;
        if (this.zzhR().zziW()) {
            this.zzhQ().zzaV("Google Analytics " + zze.VERSION + " is starting up.");
        }
        else {
            this.zzhQ().zzaV("Google Analytics " + zze.VERSION + " is starting up. " + "To enable debug logging on a device run:\n" + "  adb shell setprop log.tag.GAv4 DEBUG\n" + "  adb logcat -s GAv4");
        }
        final zzai zzq = zzg.zzq(this);
        zzq.zza();
        this.zzJK = zzq;
        final zzan zze = zzg.zze(this);
        zze.zza();
        this.zzJJ = zze;
        final zzb zzl = zzg.zzl(this);
        final zzn zzd = zzg.zzd(this);
        final zza zzc = zzg.zzc(this);
        final zzk zzb = zzg.zzb(this);
        final zzu zza = zzg.zza(this);
        final zzns zzW = zzg.zzW(applicationContext);
        zzW.zza(this.zzib());
        this.zzJG = zzW;
        final GoogleAnalytics zzi = zzg.zzi(this);
        zzd.zza();
        this.zzJM = zzd;
        zzc.zza();
        this.zzJN = zzc;
        zzb.zza();
        this.zzJO = zzb;
        zza.zza();
        this.zzJP = zza;
        final zzv zzp = zzg.zzp(this);
        zzp.zza();
        this.zzJI = zzp;
        zzl.zza();
        this.zzJH = zzl;
        if (this.zzhR().zziW()) {
            this.zzhQ().zzb("Device AnalyticsService version", com.google.android.gms.analytics.internal.zze.VERSION);
        }
        zzi.zza();
        this.zzJL = zzi;
        zzl.start();
    }
    
    public static zzf zzV(final Context context) {
        com.google.android.gms.common.internal.zzu.zzu(context);
        Label_0109: {
            if (zzf.zzJC != null) {
                break Label_0109;
            }
            synchronized (zzf.class) {
                if (zzf.zzJC == null) {
                    final zzlb zzoQ = zzld.zzoQ();
                    final long elapsedRealtime = zzoQ.elapsedRealtime();
                    final zzf zzf = com.google.android.gms.analytics.internal.zzf.zzJC = new zzf(new zzg(context.getApplicationContext()));
                    GoogleAnalytics.zzhj();
                    final long n = zzoQ.elapsedRealtime() - elapsedRealtime;
                    final long longValue = zzy.zzLP.get();
                    if (n > longValue) {
                        zzf.zzhQ().zzc("Slow initialization (ms)", n, longValue);
                    }
                }
                return zzf.zzJC;
            }
        }
    }
    
    private void zza(final zzd zzd) {
        com.google.android.gms.common.internal.zzu.zzb(zzd, "Analytics service not created/initialized");
        com.google.android.gms.common.internal.zzu.zzb(zzd.isInitialized(), "Analytics service not initialized");
    }
    
    public Context getContext() {
        return this.mContext;
    }
    
    public void zzhO() {
        zzns.zzhO();
    }
    
    public zzlb zzhP() {
        return this.zzpw;
    }
    
    public zzaf zzhQ() {
        this.zza(this.zzJF);
        return this.zzJF;
    }
    
    public zzr zzhR() {
        return this.zzJE;
    }
    
    public zzns zzhS() {
        com.google.android.gms.common.internal.zzu.zzu(this.zzJG);
        return this.zzJG;
    }
    
    public zzv zzhT() {
        this.zza(this.zzJI);
        return this.zzJI;
    }
    
    public zzai zzhU() {
        this.zza(this.zzJK);
        return this.zzJK;
    }
    
    public zzk zzhX() {
        this.zza(this.zzJO);
        return this.zzJO;
    }
    
    public zzu zzhY() {
        return this.zzJP;
    }
    
    public zzb zzhl() {
        this.zza(this.zzJH);
        return this.zzJH;
    }
    
    public zzan zzhm() {
        this.zza(this.zzJJ);
        return this.zzJJ;
    }
    
    protected Thread.UncaughtExceptionHandler zzib() {
        return new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(final Thread thread, final Throwable t) {
                final zzaf zzid = zzf.this.zzid();
                if (zzid != null) {
                    zzid.zze("Job execution failed", t);
                }
            }
        };
    }
    
    public Context zzic() {
        return this.zzJD;
    }
    
    public zzaf zzid() {
        return this.zzJF;
    }
    
    public GoogleAnalytics zzie() {
        com.google.android.gms.common.internal.zzu.zzu(this.zzJL);
        com.google.android.gms.common.internal.zzu.zzb(this.zzJL.isInitialized(), "Analytics instance not initialized");
        return this.zzJL;
    }
    
    public zzai zzif() {
        if (this.zzJK == null || !this.zzJK.isInitialized()) {
            return null;
        }
        return this.zzJK;
    }
    
    public zza zzig() {
        this.zza(this.zzJN);
        return this.zzJN;
    }
    
    public zzn zzih() {
        this.zza(this.zzJM);
        return this.zzJM;
    }
}
