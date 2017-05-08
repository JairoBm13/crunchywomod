// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.cast;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.internal.zzka;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.zze;
import android.os.Looper;
import android.content.Context;
import com.google.android.gms.internal.zzkb;
import com.google.android.gms.common.api.Api;

public final class CastRemoteDisplay
{
    public static final Api<CastRemoteDisplayOptions> API;
    public static final CastRemoteDisplayApi CastRemoteDisplayApi;
    private static final Api.ClientKey<zzkb> zzNX;
    private static final Api.zza<zzkb, CastRemoteDisplayOptions> zzNY;
    
    static {
        zzNX = new Api.ClientKey();
        zzNY = new Api.zza<zzkb, CastRemoteDisplayOptions>() {
            @Override
            public int getPriority() {
                return Integer.MAX_VALUE;
            }
            
            public zzkb zza(final Context context, final Looper looper, final zze zze, final CastRemoteDisplayOptions castRemoteDisplayOptions, final GoogleApiClient.ConnectionCallbacks connectionCallbacks, final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
                return new zzkb(context, looper, castRemoteDisplayOptions.zzQE, castRemoteDisplayOptions.zzQU, connectionCallbacks, onConnectionFailedListener);
            }
        };
        API = new Api<CastRemoteDisplayOptions>("CastRemoteDisplay.API", (Api.zza<C, CastRemoteDisplayOptions>)CastRemoteDisplay.zzNY, (Api.ClientKey<C>)CastRemoteDisplay.zzNX, new Scope[0]);
        CastRemoteDisplayApi = new zzka(CastRemoteDisplay.zzNX);
    }
    
    public static final class CastRemoteDisplayOptions implements HasOptions
    {
        final CastDevice zzQE;
        final CastRemoteDisplaySessionCallbacks zzQU;
    }
    
    public interface CastRemoteDisplaySessionCallbacks
    {
    }
    
    public interface CastRemoteDisplaySessionResult extends Result
    {
    }
}
