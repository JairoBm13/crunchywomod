// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.api;

import java.util.Iterator;
import com.google.android.gms.common.ConnectionResult;
import android.os.Bundle;

public class zzf implements zzh
{
    private final zzg zzWK;
    
    public zzf(final zzg zzWK) {
        this.zzWK = zzWK;
    }
    
    @Override
    public void begin() {
        this.zzWK.zzmL();
    }
    
    @Override
    public void connect() {
        this.zzWK.zzmM();
    }
    
    @Override
    public String getName() {
        return "DISCONNECTED";
    }
    
    @Override
    public void onConnected(final Bundle bundle) {
    }
    
    @Override
    public void onConnectionSuspended(final int n) {
    }
    
    @Override
    public <A extends Api.Client, R extends Result, T extends zza.zza<R, A>> T zza(final T t) {
        this.zzWK.zzXo.add((zzg.zze<?>)t);
        return t;
    }
    
    @Override
    public void zza(final ConnectionResult connectionResult, final Api<?> api, final int n) {
    }
    
    @Override
    public void zzaV(int n) {
        if (n == -1) {
            n = 1;
        }
        else {
            n = 0;
        }
        if (n != 0) {
            final Iterator<zzg.zze> iterator = this.zzWK.zzXo.iterator();
            while (iterator.hasNext()) {
                iterator.next().cancel();
            }
            this.zzWK.zzXo.clear();
            this.zzWK.zzmK();
            this.zzWK.zzXv.clear();
        }
    }
    
    @Override
    public <A extends Api.Client, T extends zza.zza<? extends Result, A>> T zzb(final T t) {
        throw new IllegalStateException("GoogleApiClient is not connected yet.");
    }
}
