// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import android.view.Display;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.zza;
import android.view.Surface;
import android.os.RemoteException;
import com.google.android.gms.cast.CastRemoteDisplay;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.GoogleApiClient;
import android.hardware.display.VirtualDisplay;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.cast.internal.zzl;
import com.google.android.gms.cast.CastRemoteDisplayApi;

public class zzka implements CastRemoteDisplayApi
{
    private static final zzl zzQW;
    private Api.ClientKey<zzkb> zzVu;
    private VirtualDisplay zzVv;
    private final zzke zzVw;
    
    static {
        zzQW = new zzl("CastRemoteDisplayApiImpl");
    }
    
    public zzka(final Api.ClientKey<zzkb> zzVu) {
        this.zzVw = new zzke.zza() {
            public void zzaR(final int n) {
                zzka.zzQW.zzb("onRemoteDisplayEnded", new Object[0]);
                zzka.this.zzmf();
            }
        };
        this.zzVu = zzVu;
    }
    
    private void zzmf() {
        if (this.zzVv != null) {
            if (this.zzVv.getDisplay() != null) {
                zzka.zzQW.zzb("releasing virtual display: " + this.zzVv.getDisplay().getDisplayId(), new Object[0]);
            }
            this.zzVv.release();
            this.zzVv = null;
        }
    }
    
    @Override
    public PendingResult<CastRemoteDisplay.CastRemoteDisplaySessionResult> stopRemoteDisplay(final GoogleApiClient googleApiClient) {
        zzka.zzQW.zzb("stopRemoteDisplay", new Object[0]);
        return googleApiClient.zzb((PendingResult<CastRemoteDisplay.CastRemoteDisplaySessionResult>)new zzb(googleApiClient) {
            protected void zza(final zzkb zzkb) throws RemoteException {
                zzkb.zza((zzka.zzb)this.new zzb());
            }
        });
    }
    
    private abstract class zza extends zzkc.zza
    {
        public void onDisconnected() throws RemoteException {
            throw new UnsupportedOperationException();
        }
        
        public void onError(final int n) throws RemoteException {
            throw new UnsupportedOperationException();
        }
        
        public void zza(final int n, final int n2, final Surface surface) throws RemoteException {
            throw new UnsupportedOperationException();
        }
        
        public void zzmg() throws RemoteException {
            throw new UnsupportedOperationException();
        }
    }
    
    private abstract class zzb extends zza.zza<CastRemoteDisplay.CastRemoteDisplaySessionResult, zzkb>
    {
        public zzb(final GoogleApiClient googleApiClient) {
            super(zzka.this.zzVu, googleApiClient);
        }
        
        protected CastRemoteDisplay.CastRemoteDisplaySessionResult zzq(final Status status) {
            return new zzka.zzc(status);
        }
        
        protected final class zzb extends zzka.zza
        {
            @Override
            public void onDisconnected() throws RemoteException {
                zzka.zzQW.zzb("onDisconnected", new Object[0]);
                zzka.this.zzmf();
                zzka.zzb.this.setResult((R)new zzka.zzc(Status.zzXP));
            }
            
            @Override
            public void onError(final int n) throws RemoteException {
                zzka.zzQW.zzb("onError: %d", n);
                zzka.this.zzmf();
                zzka.zzb.this.setResult((R)new zzka.zzc(Status.zzXR));
            }
        }
    }
    
    private static final class zzc implements CastRemoteDisplaySessionResult
    {
        private final Status zzOt;
        private final Display zzRi;
        
        public zzc(final Status zzOt) {
            this.zzOt = zzOt;
            this.zzRi = null;
        }
        
        @Override
        public Status getStatus() {
            return this.zzOt;
        }
    }
}
