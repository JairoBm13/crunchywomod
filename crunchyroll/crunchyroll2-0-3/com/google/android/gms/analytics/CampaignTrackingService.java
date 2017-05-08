// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics;

import com.google.android.gms.analytics.internal.zzaf;
import android.text.TextUtils;
import com.google.android.gms.analytics.internal.zzf;
import android.os.IBinder;
import android.content.Intent;
import android.os.PowerManager$WakeLock;
import com.google.android.gms.analytics.internal.zzam;
import com.google.android.gms.common.internal.zzu;
import android.content.Context;
import android.os.Handler;
import android.app.Service;

public class CampaignTrackingService extends Service
{
    private static Boolean zzIe;
    private Handler mHandler;
    
    private Handler getHandler() {
        Handler mHandler;
        if ((mHandler = this.mHandler) == null) {
            mHandler = new Handler(this.getMainLooper());
            this.mHandler = mHandler;
        }
        return mHandler;
    }
    
    public static boolean zzU(final Context context) {
        zzu.zzu(context);
        if (CampaignTrackingService.zzIe != null) {
            return CampaignTrackingService.zzIe;
        }
        final boolean zza = zzam.zza(context, CampaignTrackingService.class);
        CampaignTrackingService.zzIe = zza;
        return zza;
    }
    
    private void zzhd() {
        try {
            synchronized (CampaignTrackingReceiver.zzoW) {
                final PowerManager$WakeLock zzIc = CampaignTrackingReceiver.zzIc;
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
        zzf.zzV((Context)this).zzhQ().zzaT("CampaignTrackingService is starting up");
    }
    
    public void onDestroy() {
        zzf.zzV((Context)this).zzhQ().zzaT("CampaignTrackingService is shutting down");
        super.onDestroy();
    }
    
    public int onStartCommand(final Intent intent, int zzja, final int n) {
        this.zzhd();
        final zzf zzV = zzf.zzV((Context)this);
        final zzaf zzhQ = zzV.zzhQ();
        final String s = null;
        String s2;
        if (zzV.zzhR().zziW()) {
            zzhQ.zzaX("Unexpected installation campaign (package side)");
            s2 = s;
        }
        else {
            s2 = intent.getStringExtra("referrer");
        }
        final Handler handler = this.getHandler();
        if (TextUtils.isEmpty((CharSequence)s2)) {
            if (!zzV.zzhR().zziW()) {
                zzhQ.zzaW("No campaign found on com.android.vending.INSTALL_REFERRER \"referrer\" extra");
            }
            zzV.zzhS().zze(new Runnable() {
                @Override
                public void run() {
                    CampaignTrackingService.this.zza(zzhQ, handler, n);
                }
            });
            return 2;
        }
        zzja = zzV.zzhR().zzja();
        if (s2.length() > zzja) {
            zzhQ.zzc("Campaign data exceed the maximum supported size and will be clipped. size, limit", s2.length(), zzja);
            s2 = s2.substring(0, zzja);
        }
        zzhQ.zza("CampaignTrackingService called. startId, campaign", n, s2);
        zzV.zzhl().zza(s2, new Runnable() {
            @Override
            public void run() {
                CampaignTrackingService.this.zza(zzhQ, handler, n);
            }
        });
        return 2;
    }
    
    protected void zza(final zzaf zzaf, final Handler handler, final int n) {
        handler.post((Runnable)new Runnable() {
            @Override
            public void run() {
                final boolean stopSelfResult = CampaignTrackingService.this.stopSelfResult(n);
                if (stopSelfResult) {
                    zzaf.zza("Install campaign broadcast processed", stopSelfResult);
                }
            }
        });
    }
}
