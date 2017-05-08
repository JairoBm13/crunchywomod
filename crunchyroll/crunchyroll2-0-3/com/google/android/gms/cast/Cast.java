// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.cast;

import com.google.android.gms.cast.internal.zzb;
import com.google.android.gms.cast.internal.zzh;
import android.os.RemoteException;
import com.google.android.gms.common.api.zza;
import java.io.IOException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.cast.internal.zzk;
import com.google.android.gms.common.internal.zzu;
import com.google.android.gms.common.api.GoogleApiClient;
import android.os.Looper;
import android.content.Context;
import com.google.android.gms.cast.internal.zze;
import com.google.android.gms.common.api.Api;

public final class Cast
{
    public static final Api<CastOptions> API;
    public static final CastApi CastApi;
    private static final Api.zza<zze, CastOptions> zzNY;
    
    static {
        zzNY = new Api.zza<zze, CastOptions>() {
            @Override
            public int getPriority() {
                return Integer.MAX_VALUE;
            }
            
            public zze zza(final Context context, final Looper looper, final com.google.android.gms.common.internal.zze zze, final CastOptions castOptions, final GoogleApiClient.ConnectionCallbacks connectionCallbacks, final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
                zzu.zzb(castOptions, "Setting the API options is required.");
                return new zze(context, looper, castOptions.zzQE, castOptions.zzQG, castOptions.zzQF, connectionCallbacks, onConnectionFailedListener);
            }
        };
        API = new Api<CastOptions>("Cast.API", (Api.zza<C, CastOptions>)Cast.zzNY, (Api.ClientKey<C>)zzk.zzNX, new Scope[0]);
        CastApi = new CastApi.zza();
    }
    
    public interface ApplicationConnectionResult extends Result
    {
        String getSessionId();
    }
    
    public interface CastApi
    {
        String getApplicationStatus(final GoogleApiClient p0) throws IllegalStateException;
        
        double getVolume(final GoogleApiClient p0) throws IllegalStateException;
        
        PendingResult<ApplicationConnectionResult> joinApplication(final GoogleApiClient p0, final String p1, final String p2);
        
        PendingResult<ApplicationConnectionResult> launchApplication(final GoogleApiClient p0, final String p1);
        
        PendingResult<Status> leaveApplication(final GoogleApiClient p0);
        
        void removeMessageReceivedCallbacks(final GoogleApiClient p0, final String p1) throws IOException, IllegalArgumentException;
        
        PendingResult<Status> sendMessage(final GoogleApiClient p0, final String p1, final String p2);
        
        void setMessageReceivedCallbacks(final GoogleApiClient p0, final String p1, final MessageReceivedCallback p2) throws IOException, IllegalStateException;
        
        void setVolume(final GoogleApiClient p0, final double p1) throws IOException, IllegalArgumentException, IllegalStateException;
        
        public static final class zza implements CastApi
        {
            @Override
            public String getApplicationStatus(final GoogleApiClient googleApiClient) throws IllegalStateException {
                return googleApiClient.zza(zzk.zzNX).getApplicationStatus();
            }
            
            @Override
            public double getVolume(final GoogleApiClient googleApiClient) throws IllegalStateException {
                return googleApiClient.zza(zzk.zzNX).zzlO();
            }
            
            @Override
            public PendingResult<ApplicationConnectionResult> joinApplication(final GoogleApiClient googleApiClient, final String s, final String s2) {
                return googleApiClient.zzb((PendingResult<ApplicationConnectionResult>)new Cast.zza(googleApiClient) {
                    protected void zza(final com.google.android.gms.cast.internal.zze zze) throws RemoteException {
                        try {
                            zze.zzb(s, s2, (com.google.android.gms.common.api.zza.zzb<ApplicationConnectionResult>)this);
                        }
                        catch (IllegalStateException ex) {
                            this.zzaL(2001);
                        }
                    }
                });
            }
            
            @Override
            public PendingResult<ApplicationConnectionResult> launchApplication(final GoogleApiClient googleApiClient, final String s) {
                return googleApiClient.zzb((PendingResult<ApplicationConnectionResult>)new Cast.zza(googleApiClient) {
                    protected void zza(final com.google.android.gms.cast.internal.zze zze) throws RemoteException {
                        try {
                            zze.zza(s, false, (com.google.android.gms.common.api.zza.zzb<ApplicationConnectionResult>)this);
                        }
                        catch (IllegalStateException ex) {
                            this.zzaL(2001);
                        }
                    }
                });
            }
            
            @Override
            public PendingResult<Status> leaveApplication(final GoogleApiClient googleApiClient) {
                return googleApiClient.zzb((PendingResult<Status>)new zzh(googleApiClient) {
                    protected void zza(final com.google.android.gms.cast.internal.zze zze) throws RemoteException {
                        try {
                            zze.zzd((com.google.android.gms.common.api.zza.zzb<Status>)this);
                        }
                        catch (IllegalStateException ex) {
                            this.zzaL(2001);
                        }
                    }
                });
            }
            
            @Override
            public void removeMessageReceivedCallbacks(final GoogleApiClient googleApiClient, final String s) throws IOException, IllegalArgumentException {
                try {
                    googleApiClient.zza(zzk.zzNX).zzbC(s);
                }
                catch (RemoteException ex) {
                    throw new IOException("service error");
                }
            }
            
            @Override
            public PendingResult<Status> sendMessage(final GoogleApiClient googleApiClient, final String s, final String s2) {
                return googleApiClient.zzb((PendingResult<Status>)new zzh(googleApiClient) {
                    protected void zza(final com.google.android.gms.cast.internal.zze zze) throws RemoteException {
                        try {
                            zze.zza(s, s2, (com.google.android.gms.common.api.zza.zzb<Status>)this);
                        }
                        catch (IllegalStateException ex) {}
                        catch (IllegalArgumentException ex2) {
                            goto Label_0015;
                        }
                    }
                });
            }
            
            @Override
            public void setMessageReceivedCallbacks(final GoogleApiClient googleApiClient, final String s, final MessageReceivedCallback messageReceivedCallback) throws IOException, IllegalStateException {
                try {
                    googleApiClient.zza(zzk.zzNX).zza(s, messageReceivedCallback);
                }
                catch (RemoteException ex) {
                    throw new IOException("service error");
                }
            }
            
            @Override
            public void setVolume(final GoogleApiClient googleApiClient, final double n) throws IOException, IllegalArgumentException, IllegalStateException {
                try {
                    googleApiClient.zza(zzk.zzNX).zzd(n);
                }
                catch (RemoteException ex) {
                    throw new IOException("service error");
                }
            }
        }
    }
    
    public static final class CastOptions implements HasOptions
    {
        final CastDevice zzQE;
        final Listener zzQF;
        private final int zzQG;
        
        private CastOptions(final Builder builder) {
            this.zzQE = builder.zzQH;
            this.zzQF = builder.zzQI;
            this.zzQG = builder.zzQJ;
        }
        
        @Deprecated
        public static Builder builder(final CastDevice castDevice, final Listener listener) {
            return new Builder(castDevice, listener);
        }
        
        public static final class Builder
        {
            CastDevice zzQH;
            Listener zzQI;
            private int zzQJ;
            
            public Builder(final CastDevice zzQH, final Listener zzQI) {
                zzu.zzb(zzQH, "CastDevice parameter cannot be null");
                zzu.zzb(zzQI, "CastListener parameter cannot be null");
                this.zzQH = zzQH;
                this.zzQI = zzQI;
                this.zzQJ = 0;
            }
            
            public CastOptions build() {
                return new CastOptions(this);
            }
            
            public Builder setVerboseLoggingEnabled(final boolean b) {
                if (b) {
                    this.zzQJ |= 0x1;
                    return this;
                }
                this.zzQJ &= 0xFFFFFFFE;
                return this;
            }
        }
    }
    
    public static class Listener
    {
        public void onActiveInputStateChanged(final int n) {
        }
        
        public void onApplicationDisconnected(final int n) {
        }
        
        public void onApplicationMetadataChanged(final ApplicationMetadata applicationMetadata) {
        }
        
        public void onApplicationStatusChanged() {
        }
        
        public void onStandbyStateChanged(final int n) {
        }
        
        public void onVolumeChanged() {
        }
    }
    
    public interface MessageReceivedCallback
    {
        void onMessageReceived(final CastDevice p0, final String p1, final String p2);
    }
    
    private abstract static class zza extends zzb<ApplicationConnectionResult>
    {
        public zza(final GoogleApiClient googleApiClient) {
            super(googleApiClient);
        }
        
        public ApplicationConnectionResult zzl(final Status status) {
            return new ApplicationConnectionResult() {
                @Override
                public String getSessionId() {
                    return null;
                }
                
                @Override
                public Status getStatus() {
                    return status;
                }
            };
        }
    }
}
