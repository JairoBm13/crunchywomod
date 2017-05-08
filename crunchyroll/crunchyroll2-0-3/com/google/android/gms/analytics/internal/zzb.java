// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics.internal;

import com.google.android.gms.internal.zzns;
import android.content.Context;
import android.content.Intent;
import com.google.android.gms.analytics.AnalyticsService;
import com.google.android.gms.analytics.AnalyticsReceiver;
import com.google.android.gms.common.internal.zzu;

public class zzb extends zzd
{
    private final zzl zzJq;
    
    public zzb(final zzf zzf, final zzg zzg) {
        super(zzf);
        zzu.zzu(zzg);
        this.zzJq = zzg.zzj(zzf);
    }
    
    void onServiceConnected() {
        this.zzhO();
        this.zzJq.onServiceConnected();
    }
    
    public void start() {
        this.zzJq.start();
    }
    
    public void zzG(final boolean b) {
        this.zza("Network connectivity status changed", b);
        this.zzhS().zze(new Runnable() {
            @Override
            public void run() {
                zzb.this.zzJq.zzG(b);
            }
        });
    }
    
    public long zza(final zzh zzh) {
        this.zzia();
        zzu.zzu(zzh);
        this.zzhO();
        final long zza = this.zzJq.zza(zzh, true);
        if (zza == 0L) {
            this.zzJq.zzc(zzh);
        }
        return zza;
    }
    
    public void zza(final zzab zzab) {
        zzu.zzu(zzab);
        this.zzia();
        this.zzb("Hit delivery requested", zzab);
        this.zzhS().zze(new Runnable() {
            @Override
            public void run() {
                zzb.this.zzJq.zza(zzab);
            }
        });
    }
    
    public void zza(final zzw zzw) {
        this.zzia();
        this.zzhS().zze(new Runnable() {
            @Override
            public void run() {
                zzb.this.zzJq.zzb(zzw);
            }
        });
    }
    
    public void zza(final String s, final Runnable runnable) {
        zzu.zzh(s, "campaign param can't be empty");
        this.zzhS().zze(new Runnable() {
            @Override
            public void run() {
                zzb.this.zzJq.zzbb(s);
                if (runnable != null) {
                    runnable.run();
                }
            }
        });
    }
    
    public void zzhH() {
        this.zzia();
        final Context context = this.getContext();
        if (AnalyticsReceiver.zzT(context) && AnalyticsService.zzU(context)) {
            final Intent intent = new Intent(context, (Class)AnalyticsService.class);
            intent.setAction("com.google.android.gms.analytics.ANALYTICS_DISPATCH");
            context.startService(intent);
            return;
        }
        this.zza((zzw)null);
    }
    
    public void zzhJ() {
        this.zzia();
        zzns.zzhO();
        this.zzJq.zzhJ();
    }
    
    public void zzhK() {
        this.zzaT("Radio powered up");
        this.zzhH();
    }
    
    void zzhL() {
        this.zzhO();
        this.zzJq.zzhL();
    }
    
    @Override
    protected void zzhn() {
        this.zzJq.zza();
    }
}
