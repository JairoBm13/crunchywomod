// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics;

import com.google.android.gms.analytics.internal.zzaf;
import android.os.PowerManager;
import android.text.TextUtils;
import com.google.android.gms.analytics.internal.zzf;
import android.content.Intent;
import com.google.android.gms.analytics.internal.zzam;
import com.google.android.gms.common.internal.zzu;
import android.content.Context;
import android.os.PowerManager$WakeLock;
import android.content.BroadcastReceiver;

public class CampaignTrackingReceiver extends BroadcastReceiver
{
    static PowerManager$WakeLock zzIc;
    static Boolean zzId;
    static Object zzoW;
    
    static {
        CampaignTrackingReceiver.zzoW = new Object();
    }
    
    public static boolean zzT(final Context context) {
        zzu.zzu(context);
        if (CampaignTrackingReceiver.zzId != null) {
            return CampaignTrackingReceiver.zzId;
        }
        final boolean zza = zzam.zza(context, CampaignTrackingReceiver.class, true);
        CampaignTrackingReceiver.zzId = zza;
        return zza;
    }
    
    public void onReceive(final Context context, final Intent intent) {
        final zzf zzV = zzf.zzV(context);
        final zzaf zzhQ = zzV.zzhQ();
        final String stringExtra = intent.getStringExtra("referrer");
        final String action = intent.getAction();
        zzhQ.zza("CampaignTrackingReceiver received", action);
        if (!"com.android.vending.INSTALL_REFERRER".equals(action) || TextUtils.isEmpty((CharSequence)stringExtra)) {
            zzhQ.zzaW("CampaignTrackingReceiver received unexpected intent without referrer extra");
            return;
        }
        final boolean zzU = CampaignTrackingService.zzU(context);
        if (!zzU) {
            zzhQ.zzaW("CampaignTrackingService not registered or disabled. Installation tracking not possible. See http://goo.gl/8Rd3yj for instructions.");
        }
        this.zzaL(stringExtra);
        if (zzV.zzhR().zziW()) {
            zzhQ.zzaX("Received unexpected installation campaign on package side");
            return;
        }
        final Class<? extends CampaignTrackingService> zzhf = this.zzhf();
        zzu.zzu(zzhf);
        final Intent intent2 = new Intent(context, (Class)zzhf);
        intent2.putExtra("referrer", stringExtra);
        synchronized (CampaignTrackingReceiver.zzoW) {
            context.startService(intent2);
            if (!zzU) {
                return;
            }
        }
        while (true) {
            try {
                final Context context2;
                final PowerManager powerManager = (PowerManager)context2.getSystemService("power");
                if (CampaignTrackingReceiver.zzIc == null) {
                    (CampaignTrackingReceiver.zzIc = powerManager.newWakeLock(1, "Analytics campaign WakeLock")).setReferenceCounted(false);
                }
                CampaignTrackingReceiver.zzIc.acquire(1000L);
            }
            // monitorexit(intent)
            catch (SecurityException ex) {
                zzhQ.zzaW("CampaignTrackingService service at risk of not starting. For more reliable installation campaign reports, add the WAKE_LOCK permission to your manifest. See http://goo.gl/8Rd3yj for instructions.");
                continue;
            }
            break;
        }
    }
    
    protected void zzaL(final String s) {
    }
    
    protected Class<? extends CampaignTrackingService> zzhf() {
        return CampaignTrackingService.class;
    }
}
