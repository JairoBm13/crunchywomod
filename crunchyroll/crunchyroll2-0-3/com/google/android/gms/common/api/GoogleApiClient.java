// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.api;

import com.google.android.gms.common.ConnectionResult;
import android.os.Bundle;
import com.google.android.gms.common.internal.zzu;
import java.util.Collection;
import com.google.android.gms.internal.zzpq;
import java.util.HashMap;
import java.util.HashSet;
import com.google.android.gms.common.internal.zze;
import java.util.Map;
import android.view.View;
import java.util.Set;
import com.google.android.gms.internal.zzpt;
import com.google.android.gms.internal.zzps;
import android.support.v4.app.FragmentActivity;
import android.accounts.Account;
import android.os.Looper;
import android.content.Context;
import java.io.PrintWriter;
import java.io.FileDescriptor;

public interface GoogleApiClient
{
    void connect();
    
    void disconnect();
    
    void dump(final String p0, final FileDescriptor p1, final PrintWriter p2, final String[] p3);
    
    Context getContext();
    
    Looper getLooper();
    
    boolean isConnected();
    
    boolean isConnecting();
    
    void registerConnectionCallbacks(final ConnectionCallbacks p0);
    
    void registerConnectionFailedListener(final OnConnectionFailedListener p0);
    
    void unregisterConnectionCallbacks(final ConnectionCallbacks p0);
    
    void unregisterConnectionFailedListener(final OnConnectionFailedListener p0);
    
     <C extends Api.Client> C zza(final Api.ClientKey<C> p0);
    
     <A extends Api.Client, R extends Result, T extends zza.zza<R, A>> T zza(final T p0);
    
     <A extends Api.Client, T extends zza.zza<? extends Result, A>> T zzb(final T p0);
    
    public static final class Builder
    {
        private final Context mContext;
        private Account zzMY;
        private String zzOd;
        private String zzOe;
        private FragmentActivity zzWA;
        private int zzWB;
        private int zzWC;
        private OnConnectionFailedListener zzWD;
        private Api.zza<? extends zzps, zzpt> zzWE;
        private final Set<ConnectionCallbacks> zzWF;
        private final Set<OnConnectionFailedListener> zzWG;
        private zzpt.zza zzWH;
        private Looper zzWt;
        private final Set<Scope> zzWv;
        private int zzWw;
        private View zzWx;
        private final Map<Api<?>, zze.zza> zzWy;
        private final Map<Api<?>, Api.ApiOptions> zzWz;
        
        public Builder(final Context mContext) {
            this.zzWv = new HashSet<Scope>();
            this.zzWy = new HashMap<Api<?>, zze.zza>();
            this.zzWz = new HashMap<Api<?>, Api.ApiOptions>();
            this.zzWB = -1;
            this.zzWC = -1;
            this.zzWF = new HashSet<ConnectionCallbacks>();
            this.zzWG = new HashSet<OnConnectionFailedListener>();
            this.zzWH = new zzpt.zza();
            this.mContext = mContext;
            this.zzWt = mContext.getMainLooper();
            this.zzOe = mContext.getPackageName();
            this.zzOd = mContext.getClass().getName();
            this.zzWE = zzpq.zzNY;
        }
        
        private GoogleApiClient zzmy() {
            final zzm zza = zzm.zza(this.zzWA);
            final zzg zzg = new zzg(this.mContext.getApplicationContext(), this.zzWt, this.zzmx(), this.zzWE, this.zzWz, this.zzWF, this.zzWG, this.zzWB, -1);
            zza.zza(this.zzWB, zzg, this.zzWD);
            return zzg;
        }
        
        private GoogleApiClient zzmz() {
            final zzn zzb = zzn.zzb(this.zzWA);
            GoogleApiClient zzbc;
            if ((zzbc = zzb.zzbc(this.zzWC)) == null) {
                zzbc = new zzg(this.mContext.getApplicationContext(), this.zzWt, this.zzmx(), this.zzWE, this.zzWz, this.zzWF, this.zzWG, -1, this.zzWC);
            }
            zzb.zza(this.zzWC, zzbc, this.zzWD);
            return zzbc;
        }
        
        public Builder addApi(final Api<? extends Api.ApiOptions.NotRequiredOptions> api) {
            this.zzWz.put(api, null);
            this.zzWv.addAll(api.zzmr());
            return this;
        }
        
        public <O extends Api.ApiOptions.HasOptions> Builder addApi(final Api<O> api, final O o) {
            zzu.zzb(o, "Null options are not permitted for this Api");
            this.zzWz.put(api, (Api.ApiOptions)o);
            this.zzWv.addAll(api.zzmr());
            return this;
        }
        
        public Builder addConnectionCallbacks(final ConnectionCallbacks connectionCallbacks) {
            this.zzWF.add(connectionCallbacks);
            return this;
        }
        
        public Builder addOnConnectionFailedListener(final OnConnectionFailedListener onConnectionFailedListener) {
            this.zzWG.add(onConnectionFailedListener);
            return this;
        }
        
        public GoogleApiClient build() {
            zzu.zzb(!this.zzWz.isEmpty(), "must call addApi() to add at least one API");
            if (this.zzWB >= 0) {
                return this.zzmy();
            }
            if (this.zzWC >= 0) {
                return this.zzmz();
            }
            return new zzg(this.mContext, this.zzWt, this.zzmx(), this.zzWE, this.zzWz, this.zzWF, this.zzWG, -1, -1);
        }
        
        public zze zzmx() {
            return new zze(this.zzMY, this.zzWv, this.zzWy, this.zzWw, this.zzWx, this.zzOe, this.zzOd, this.zzWH.zzyc());
        }
    }
    
    public interface ConnectionCallbacks
    {
        void onConnected(final Bundle p0);
        
        void onConnectionSuspended(final int p0);
    }
    
    public interface ConnectionProgressReportCallbacks
    {
        void onReportAccountValidation(final ConnectionResult p0);
        
        void onReportServiceBinding(final ConnectionResult p0);
    }
    
    public interface OnConnectionFailedListener
    {
        void onConnectionFailed(final ConnectionResult p0);
    }
    
    public interface ServerAuthCodeCallbacks
    {
        CheckResult onCheckServerAuthorization(final String p0, final Set<Scope> p1);
        
        boolean onUploadServerAuthCode(final String p0, final String p1);
        
        public static class CheckResult
        {
            private boolean zzWI;
            private Set<Scope> zzWJ;
            
            public boolean zzmA() {
                return this.zzWI;
            }
            
            public Set<Scope> zzmB() {
                return this.zzWJ;
            }
        }
    }
}
