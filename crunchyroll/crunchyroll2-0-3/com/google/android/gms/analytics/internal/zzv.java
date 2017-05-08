// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics.internal;

import com.google.android.gms.common.internal.zzu;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager$NameNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import com.google.android.gms.analytics.AnalyticsReceiver;
import android.app.PendingIntent;
import android.app.AlarmManager;

public class zzv extends zzd
{
    private boolean zzKW;
    private boolean zzKX;
    private AlarmManager zzKY;
    
    protected zzv(final zzf zzf) {
        super(zzf);
        this.zzKY = (AlarmManager)this.getContext().getSystemService("alarm");
    }
    
    private PendingIntent zzjI() {
        final Intent intent = new Intent(this.getContext(), (Class)AnalyticsReceiver.class);
        intent.setAction("com.google.android.gms.analytics.ANALYTICS_DISPATCH");
        return PendingIntent.getBroadcast(this.getContext(), 0, intent, 0);
    }
    
    public void cancel() {
        this.zzia();
        this.zzKX = false;
        this.zzKY.cancel(this.zzjI());
    }
    
    public boolean zzbp() {
        return this.zzKX;
    }
    
    @Override
    protected void zzhn() {
        try {
            this.zzKY.cancel(this.zzjI());
            if (this.zzhR().zzjf() > 0L) {
                final ActivityInfo receiverInfo = this.getContext().getPackageManager().getReceiverInfo(new ComponentName(this.getContext(), (Class)AnalyticsReceiver.class), 2);
                if (receiverInfo != null && receiverInfo.enabled) {
                    this.zzaT("Receiver registered. Using alarm for local dispatch.");
                    this.zzKW = true;
                }
            }
        }
        catch (PackageManager$NameNotFoundException ex) {}
    }
    
    public boolean zzjG() {
        return this.zzKW;
    }
    
    public void zzjH() {
        this.zzia();
        zzu.zza(this.zzjG(), (Object)"Receiver not registered");
        final long zzjf = this.zzhR().zzjf();
        if (zzjf > 0L) {
            this.cancel();
            final long elapsedRealtime = this.zzhP().elapsedRealtime();
            this.zzKX = true;
            this.zzKY.setInexactRepeating(2, elapsedRealtime + zzjf, 0L, this.zzjI());
        }
    }
}
