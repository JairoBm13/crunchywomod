// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics.internal;

import android.net.NetworkInfo;
import android.net.ConnectivityManager;
import android.os.Build$VERSION;
import android.content.IntentFilter;
import android.content.Intent;
import android.content.Context;
import com.google.android.gms.common.internal.zzu;
import android.content.BroadcastReceiver;

class zzag extends BroadcastReceiver
{
    static final String zzMo;
    private final zzf zzJy;
    private boolean zzMp;
    private boolean zzMq;
    
    static {
        zzMo = zzag.class.getName();
    }
    
    zzag(final zzf zzJy) {
        zzu.zzu(zzJy);
        this.zzJy = zzJy;
    }
    
    private Context getContext() {
        return this.zzJy.getContext();
    }
    
    private zzaf zzhQ() {
        return this.zzJy.zzhQ();
    }
    
    private zzb zzhl() {
        return this.zzJy.zzhl();
    }
    
    private void zzke() {
        this.zzhQ();
        this.zzhl();
    }
    
    public boolean isConnected() {
        if (!this.zzMp) {
            this.zzJy.zzhQ().zzaW("Connectivity unknown. Receiver not registered");
        }
        return this.zzMq;
    }
    
    public boolean isRegistered() {
        return this.zzMp;
    }
    
    public void onReceive(final Context context, final Intent intent) {
        this.zzke();
        final String action = intent.getAction();
        this.zzJy.zzhQ().zza("NetworkBroadcastReceiver received action", action);
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
            final boolean zzkg = this.zzkg();
            if (this.zzMq != zzkg) {
                this.zzMq = zzkg;
                this.zzhl().zzG(zzkg);
            }
        }
        else {
            if (!"com.google.analytics.RADIO_POWERED".equals(action)) {
                this.zzJy.zzhQ().zzd("NetworkBroadcastReceiver received unknown action", action);
                return;
            }
            if (!intent.hasExtra(zzag.zzMo)) {
                this.zzhl().zzhK();
            }
        }
    }
    
    public void unregister() {
        if (!this.isRegistered()) {
            return;
        }
        this.zzJy.zzhQ().zzaT("Unregistering connectivity change receiver");
        this.zzMp = false;
        this.zzMq = false;
        final Context context = this.getContext();
        try {
            context.unregisterReceiver((BroadcastReceiver)this);
        }
        catch (IllegalArgumentException ex) {
            this.zzhQ().zze("Failed to unregister the network broadcast receiver", ex);
        }
    }
    
    public void zzkd() {
        this.zzke();
        if (this.zzMp) {
            return;
        }
        final Context context = this.getContext();
        context.registerReceiver((BroadcastReceiver)this, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        final IntentFilter intentFilter = new IntentFilter("com.google.analytics.RADIO_POWERED");
        intentFilter.addCategory(context.getPackageName());
        context.registerReceiver((BroadcastReceiver)this, intentFilter);
        this.zzMq = this.zzkg();
        this.zzJy.zzhQ().zza("Registering connectivity change receiver. Network connected", this.zzMq);
        this.zzMp = true;
    }
    
    public void zzkf() {
        if (Build$VERSION.SDK_INT <= 10) {
            return;
        }
        final Context context = this.getContext();
        final Intent intent = new Intent("com.google.analytics.RADIO_POWERED");
        intent.addCategory(context.getPackageName());
        intent.putExtra(zzag.zzMo, true);
        context.sendOrderedBroadcast(intent, (String)null);
    }
    
    protected boolean zzkg() {
        final ConnectivityManager connectivityManager = (ConnectivityManager)this.getContext().getSystemService("connectivity");
        try {
            final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        catch (SecurityException ex) {
            return false;
        }
    }
}
