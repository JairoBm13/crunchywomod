// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import android.os.IInterface;
import android.os.IBinder;
import android.os.RemoteException;
import com.google.android.gms.common.api.GoogleApiClient;
import android.os.Looper;
import android.content.Context;
import com.google.android.gms.cast.CastRemoteDisplay;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.internal.zzl;
import android.os.IBinder$DeathRecipient;
import com.google.android.gms.common.internal.zzi;

public class zzkb extends zzi<zzkd> implements IBinder$DeathRecipient
{
    private static final zzl zzQW;
    private CastDevice zzQH;
    private CastRemoteDisplay.CastRemoteDisplaySessionCallbacks zzVB;
    
    static {
        zzQW = new zzl("CastRemoteDisplayClientImpl");
    }
    
    public zzkb(final Context context, final Looper looper, final CastDevice zzQH, final CastRemoteDisplay.CastRemoteDisplaySessionCallbacks zzVB, final GoogleApiClient.ConnectionCallbacks connectionCallbacks, final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 83, connectionCallbacks, onConnectionFailedListener);
        this.zzVB = zzVB;
        this.zzQH = zzQH;
    }
    
    public void binderDied() {
    }
    
    @Override
    public void disconnect() {
        try {
            this.zznM().disconnect();
        }
        catch (RemoteException ex) {}
        finally {
            super.disconnect();
        }
    }
    
    @Override
    protected String getServiceDescriptor() {
        return "com.google.android.gms.cast.remote_display.ICastRemoteDisplayService";
    }
    
    @Override
    protected String getStartServiceAction() {
        return "com.google.android.gms.cast.remote_display.service.START";
    }
    
    public void zza(final zzkc zzkc) throws RemoteException {
        zzkb.zzQW.zzb("stopRemoteDisplay", new Object[0]);
        this.zznM().zza(zzkc);
    }
    
    public zzkd zzay(final IBinder binder) {
        return zzkd.zza.zzaA(binder);
    }
}
