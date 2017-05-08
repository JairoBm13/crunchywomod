// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.appdatasearch;

import com.google.android.gms.internal.zziv;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.zze;
import android.os.Looper;
import android.content.Context;
import com.google.android.gms.internal.zzit;
import com.google.android.gms.common.api.Api;

public final class zza
{
    public static final Api.ClientKey<zzit> zzMO;
    private static final Api.zza<zzit, Api.ApiOptions.NoOptions> zzMP;
    public static final Api<Api.ApiOptions.NoOptions> zzMQ;
    public static final zzk zzMR;
    
    static {
        zzMO = new Api.ClientKey();
        zzMP = new Api.zza<zzit, Api.ApiOptions.NoOptions>() {
            @Override
            public int getPriority() {
                return Integer.MAX_VALUE;
            }
            
            public zzit zza(final Context context, final Looper looper, final zze zze, final NoOptions noOptions, final GoogleApiClient.ConnectionCallbacks connectionCallbacks, final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
                return new zzit(context, looper, connectionCallbacks, onConnectionFailedListener);
            }
        };
        zzMQ = new Api<Api.ApiOptions.NoOptions>("AppDataSearch.LIGHTWEIGHT_API", (Api.zza<C, Api.ApiOptions.NoOptions>)zza.zzMP, (Api.ClientKey<C>)zza.zzMO, new Scope[0]);
        zzMR = new zziv();
    }
}
