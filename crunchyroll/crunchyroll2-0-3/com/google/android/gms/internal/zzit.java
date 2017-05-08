// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import android.os.DeadObjectException;
import android.os.IInterface;
import android.os.IBinder;
import com.google.android.gms.common.api.GoogleApiClient;
import android.os.Looper;
import android.content.Context;
import com.google.android.gms.common.internal.zzi;

public class zzit extends zzi<zziq>
{
    public zzit(final Context context, final Looper looper, final GoogleApiClient.ConnectionCallbacks connectionCallbacks, final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 19, connectionCallbacks, onConnectionFailedListener);
    }
    
    @Override
    protected String getServiceDescriptor() {
        return "com.google.android.gms.appdatasearch.internal.ILightweightAppDataSearch";
    }
    
    @Override
    protected String getStartServiceAction() {
        return "com.google.android.gms.icing.LIGHTWEIGHT_INDEX_SERVICE";
    }
    
    protected zziq zzaf(final IBinder binder) {
        return zziq.zza.zzad(binder);
    }
    
    public zziq zzkO() throws DeadObjectException {
        return this.zznM();
    }
}
