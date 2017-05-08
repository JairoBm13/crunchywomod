// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics;

import com.google.android.gms.analytics.internal.zzw;
import com.google.android.gms.analytics.internal.zzaf;
import com.google.android.gms.analytics.internal.zzf;
import android.os.IBinder;
import android.content.Intent;
import android.os.PowerManager$WakeLock;
import com.google.android.gms.analytics.internal.zzam;
import com.google.android.gms.common.internal.zzu;
import android.content.Context;
import android.os.Handler;
import android.app.Service;

public final class AnalyticsService extends Service
{
    private static Boolean zzIe;
    private final Handler mHandler;
    
    public AnalyticsService() {
        this.mHandler = new Handler();
    }
    
    public static boolean zzU(final Context context) {
        zzu.zzu(context);
        if (AnalyticsService.zzIe != null) {
            return AnalyticsService.zzIe;
        }
        final boolean zza = zzam.zza(context, AnalyticsService.class);
        AnalyticsService.zzIe = zza;
        return zza;
    }
    
    private void zzhd() {
        try {
            synchronized (AnalyticsReceiver.zzoW) {
                final PowerManager$WakeLock zzIc = AnalyticsReceiver.zzIc;
                if (zzIc != null && zzIc.isHeld()) {
                    zzIc.release();
                }
            }
        }
        catch (SecurityException ex) {}
    }
    
    public IBinder onBind(final Intent intent) {
        return null;
    }
    
    public void onCreate() {
        super.onCreate();
        final zzf zzV = zzf.zzV((Context)this);
        final zzaf zzhQ = zzV.zzhQ();
        if (zzV.zzhR().zziW()) {
            zzhQ.zzaT("Device AnalyticsService is starting up");
            return;
        }
        zzhQ.zzaT("Local AnalyticsService is starting up");
    }
    
    public void onDestroy() {
        final zzf zzV = zzf.zzV((Context)this);
        final zzaf zzhQ = zzV.zzhQ();
        if (zzV.zzhR().zziW()) {
            zzhQ.zzaT("Device AnalyticsService is shutting down");
        }
        else {
            zzhQ.zzaT("Local AnalyticsService is shutting down");
        }
        super.onDestroy();
    }
    
    public int onStartCommand(final Intent intent, final int n, final int n2) {
        this.zzhd();
        final zzf zzV = zzf.zzV((Context)this);
        final zzaf zzhQ = zzV.zzhQ();
        final String action = intent.getAction();
        if (zzV.zzhR().zziW()) {
            zzhQ.zza("Device AnalyticsService called. startId, action", n2, action);
        }
        else {
            zzhQ.zza("Local AnalyticsService called. startId, action", n2, action);
        }
        if ("com.google.android.gms.analytics.ANALYTICS_DISPATCH".equals(action)) {
            zzV.zzhl().zza(new zzw() {
                @Override
                public void zzc(final Throwable t) {
                    AnalyticsService.this.mHandler.post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            if (AnalyticsService.this.stopSelfResult(n2)) {
                                if (!zzV.zzhR().zziW()) {
                                    zzhQ.zzaT("Local AnalyticsService processed last dispatch request");
                                    return;
                                }
                                zzhQ.zzaT("Device AnalyticsService processed last dispatch request");
                            }
                        }
                    });
                }
            });
        }
        return 2;
    }
}
