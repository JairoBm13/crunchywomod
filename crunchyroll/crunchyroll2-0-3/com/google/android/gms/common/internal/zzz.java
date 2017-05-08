// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.internal;

import android.os.IBinder;
import com.google.android.gms.common.api.GoogleApiClient;
import android.os.Looper;
import android.content.Context;
import com.google.android.gms.common.api.Api;
import android.os.IInterface;

public class zzz<T extends IInterface> extends zzi<T>
{
    private final Api.zzb<T> zzabf;
    
    public zzz(final Context context, final Looper looper, final int n, final GoogleApiClient.ConnectionCallbacks connectionCallbacks, final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener, final com.google.android.gms.common.internal.zze zze, final Api.zzb zzabf) {
        super(context, looper, n, connectionCallbacks, onConnectionFailedListener, zze);
        this.zzabf = (Api.zzb<T>)zzabf;
    }
    
    @Override
    protected String getServiceDescriptor() {
        return this.zzabf.getServiceDescriptor();
    }
    
    @Override
    protected String getStartServiceAction() {
        return this.zzabf.getStartServiceAction();
    }
    
    @Override
    protected T zzT(final IBinder binder) {
        return this.zzabf.zzT(binder);
    }
}
