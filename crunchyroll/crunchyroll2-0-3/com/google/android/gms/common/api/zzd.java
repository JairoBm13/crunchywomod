// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.api;

import java.util.Iterator;
import com.google.android.gms.common.ConnectionResult;
import android.os.Bundle;
import android.util.Log;
import android.os.DeadObjectException;

public class zzd implements zzh
{
    private final zzg zzWK;
    
    public zzd(final zzg zzWK) {
        this.zzWK = zzWK;
    }
    
    private <A extends Api.Client> void zza(final zzg.zze<A> zze) throws DeadObjectException {
        this.zzWK.zzb((zzg.zze<Api.Client>)zze);
        final Api.Client zza = this.zzWK.zza((Api.ClientKey<A>)zze.zzms());
        if (!zza.isConnected() && this.zzWK.zzXv.containsKey(zze.zzms())) {
            zze.zzr(new Status(17));
            return;
        }
        zze.zzb(zza);
    }
    
    @Override
    public void begin() {
        while (!this.zzWK.zzXo.isEmpty()) {
            try {
                this.zza((zzg.zze<?>)this.zzWK.zzXo.remove());
            }
            catch (DeadObjectException ex) {
                Log.w("GoogleApiClientConnected", "Service died while flushing queue", (Throwable)ex);
            }
        }
    }
    
    @Override
    public void connect() {
    }
    
    @Override
    public String getName() {
        return "CONNECTED";
    }
    
    @Override
    public void onConnected(final Bundle bundle) {
    }
    
    @Override
    public void onConnectionSuspended(final int n) {
        switch (n) {
            default: {}
            case 2: {
                this.zzaV(n);
                this.zzWK.connect();
            }
            case 1: {
                this.zzWK.zzmQ();
                this.zzaV(n);
            }
        }
    }
    
    @Override
    public <A extends Api.Client, R extends Result, T extends zza.zza<R, A>> T zza(final T t) {
        return this.zzb(t);
    }
    
    @Override
    public void zza(final ConnectionResult connectionResult, final Api<?> api, final int n) {
    }
    
    @Override
    public void zzaV(final int n) {
        boolean b;
        if (n == -1) {
            b = true;
        }
        else {
            b = false;
        }
        if (b) {
            this.zzWK.zzmK();
            this.zzWK.zzXv.clear();
        }
        else {
            final Iterator<zzg.zze<?>> iterator = this.zzWK.zzXA.iterator();
            while (iterator.hasNext()) {
                iterator.next().forceFailureUnlessReady(new Status(8, "The connection to Google Play services was lost"));
            }
        }
        this.zzWK.zze(null);
        if (!b) {
            this.zzWK.zzXn.zzbu(n);
        }
        this.zzWK.zzXn.zznT();
    }
    
    @Override
    public <A extends Api.Client, T extends zza.zza<? extends Result, A>> T zzb(final T t) {
        try {
            this.zza((zzg.zze<Api.Client>)t);
            return t;
        }
        catch (DeadObjectException ex) {
            this.zzaV(1);
            return t;
        }
    }
}
