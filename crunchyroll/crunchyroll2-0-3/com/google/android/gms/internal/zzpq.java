// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import com.google.android.gms.signin.internal.zzg;
import com.google.android.gms.common.api.Scope;
import java.util.concurrent.Executors;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.zze;
import android.os.Looper;
import android.content.Context;
import com.google.android.gms.signin.internal.zzh;
import com.google.android.gms.common.api.Api;

public final class zzpq
{
    public static final Api<zzpt> API;
    public static final Api.ClientKey<zzh> zzNX;
    public static final Api.zza<zzh, zzpt> zzNY;
    static final Api.zza<zzh, Api.ApiOptions.NoOptions> zzaJO;
    public static final zzpr zzaJP;
    public static final Api<Api.ApiOptions.NoOptions> zzada;
    public static final Api.ClientKey<zzh> zzajz;
    
    static {
        zzNX = new Api.ClientKey();
        zzajz = new Api.ClientKey();
        zzNY = new Api.zza<zzh, zzpt>() {
            @Override
            public int getPriority() {
                return Integer.MAX_VALUE;
            }
            
            public zzh zza(final Context context, final Looper looper, final zze zze, zzpt zzaJQ, final GoogleApiClient.ConnectionCallbacks connectionCallbacks, final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
                if (zzaJQ == null) {
                    zzaJQ = zzpt.zzaJQ;
                }
                return new zzh(context, looper, true, zze, zzaJQ, connectionCallbacks, onConnectionFailedListener, Executors.newSingleThreadExecutor());
            }
        };
        zzaJO = new Api.zza<zzh, Api.ApiOptions.NoOptions>() {
            @Override
            public int getPriority() {
                return Integer.MAX_VALUE;
            }
            
            public zzh zzv(final Context context, final Looper looper, final zze zze, final NoOptions noOptions, final GoogleApiClient.ConnectionCallbacks connectionCallbacks, final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
                return new zzh(context, looper, false, zze, zzpt.zzaJQ, connectionCallbacks, onConnectionFailedListener, Executors.newSingleThreadExecutor());
            }
        };
        API = new Api<zzpt>("SignIn.API", (Api.zza<C, zzpt>)zzpq.zzNY, (Api.ClientKey<C>)zzpq.zzNX, new Scope[0]);
        zzada = new Api<Api.ApiOptions.NoOptions>("SignIn.INTERNAL_API", (Api.zza<C, Api.ApiOptions.NoOptions>)zzpq.zzaJO, (Api.ClientKey<C>)zzpq.zzajz, new Scope[0]);
        zzaJP = new zzg();
    }
}
