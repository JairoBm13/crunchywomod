// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.signin.internal;

import java.util.Collections;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import com.google.android.gms.common.internal.ResolveAccountResponse;
import com.google.android.gms.common.internal.ResolveAccountRequest;
import com.google.android.gms.common.internal.zzq;
import android.os.RemoteException;
import android.app.PendingIntent;
import com.google.android.gms.common.ConnectionResult;
import android.util.Log;
import com.google.android.gms.common.internal.AuthAccountRequest;
import com.google.android.gms.common.internal.zzu;
import com.google.android.gms.common.api.Scope;
import java.util.Set;
import com.google.android.gms.common.internal.IAccountAccessor;
import android.os.IInterface;
import android.os.IBinder;
import android.os.Parcelable;
import com.google.android.gms.common.internal.BinderWrapper;
import android.os.Bundle;
import com.google.android.gms.common.api.GoogleApiClient;
import android.os.Looper;
import android.content.Context;
import java.util.concurrent.ExecutorService;
import com.google.android.gms.internal.zzpt;
import com.google.android.gms.common.internal.zze;
import com.google.android.gms.internal.zzps;
import com.google.android.gms.common.internal.zzi;

public class zzh extends zzi<com.google.android.gms.signin.internal.zzf> implements zzps
{
    private final com.google.android.gms.common.internal.zze zzXa;
    private final zzpt zzZU;
    private Integer zzZV;
    private final boolean zzaKa;
    private final ExecutorService zzaKb;
    
    public zzh(final Context context, final Looper looper, final boolean zzaKa, final com.google.android.gms.common.internal.zze zzXa, final zzpt zzZU, final GoogleApiClient.ConnectionCallbacks connectionCallbacks, final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener, final ExecutorService zzaKb) {
        super(context, looper, 44, connectionCallbacks, onConnectionFailedListener, zzXa);
        this.zzaKa = zzaKa;
        this.zzXa = zzXa;
        this.zzZU = zzZU;
        this.zzZV = zzXa.zznC();
        this.zzaKb = zzaKb;
    }
    
    public static Bundle zza(final zzpt zzpt, final Integer n, final ExecutorService executorService) {
        final Bundle bundle = new Bundle();
        bundle.putBoolean("com.google.android.gms.signin.internal.offlineAccessRequested", zzpt.zzxZ());
        bundle.putBoolean("com.google.android.gms.signin.internal.idTokenRequested", zzpt.zzya());
        bundle.putString("com.google.android.gms.signin.internal.serverClientId", zzpt.zzxt());
        if (zzpt.zzyb() != null) {
            bundle.putParcelable("com.google.android.gms.signin.internal.signInCallbacks", (Parcelable)new BinderWrapper(((com.google.android.gms.signin.internal.zzd.zza)new zza(zzpt, executorService)).asBinder()));
        }
        if (n != null) {
            bundle.putInt("com.google.android.gms.common.internal.ClientSettings.sessionId", (int)n);
        }
        return bundle;
    }
    
    @Override
    public void connect() {
        this.connect(new zzf(this));
    }
    
    @Override
    protected String getServiceDescriptor() {
        return "com.google.android.gms.signin.internal.ISignInService";
    }
    
    @Override
    protected String getStartServiceAction() {
        return "com.google.android.gms.signin.service.START";
    }
    
    @Override
    public boolean requiresSignIn() {
        return this.zzaKa;
    }
    
    @Override
    public void zza(final IAccountAccessor accountAccessor, final Set<Scope> set, final com.google.android.gms.signin.internal.zze zze) {
        zzu.zzb(zze, "Expecting a valid ISignInCallbacks");
        try {
            this.zznM().zza(new AuthAccountRequest(accountAccessor, set), zze);
        }
        catch (RemoteException ex) {
            Log.w("SignInClientImpl", "Remote service probably died when authAccount is called");
            try {
                zze.zza(new ConnectionResult(8, null), new AuthAccountResult());
            }
            catch (RemoteException ex2) {
                Log.wtf("SignInClientImpl", "ISignInCallbacks#onAuthAccount should be executed from the same process, unexpected RemoteException.");
            }
        }
    }
    
    @Override
    public void zza(final IAccountAccessor accountAccessor, final boolean b) {
        try {
            this.zznM().zza(accountAccessor, this.zzZV, b);
        }
        catch (RemoteException ex) {
            Log.w("SignInClientImpl", "Remote service probably died when saveDefaultAccount is called");
        }
    }
    
    @Override
    public void zza(final zzq zzq) {
        zzu.zzb(zzq, "Expecting a valid IResolveAccountCallbacks");
        try {
            this.zznM().zza(new ResolveAccountRequest(this.zzXa.zznt(), this.zzZV), zzq);
        }
        catch (RemoteException ex) {
            Log.w("SignInClientImpl", "Remote service probably died when resolveAccount is called");
            try {
                zzq.zzb(new ResolveAccountResponse(8));
            }
            catch (RemoteException ex2) {
                Log.wtf("SignInClientImpl", "IResolveAccountCallbacks#onAccountResolutionComplete should be executed from the same process, unexpected RemoteException.");
            }
        }
    }
    
    protected com.google.android.gms.signin.internal.zzf zzdE(final IBinder binder) {
        return com.google.android.gms.signin.internal.zzf.zza.zzdD(binder);
    }
    
    @Override
    protected Bundle zzkR() {
        final Bundle zza = zza(this.zzZU, this.zzXa.zznC(), this.zzaKb);
        if (!this.getContext().getPackageName().equals(this.zzXa.zzny())) {
            zza.putString("com.google.android.gms.signin.internal.realClientPackageName", this.zzXa.zzny());
        }
        return zza;
    }
    
    @Override
    public void zzxY() {
        try {
            this.zznM().zziQ(this.zzZV);
        }
        catch (RemoteException ex) {
            Log.w("SignInClientImpl", "Remote service probably died when clearAccountFromSessionStore is called");
        }
    }
    
    private static class zza extends com.google.android.gms.signin.internal.zzd.zza
    {
        private final zzpt zzZU;
        private final ExecutorService zzaKb;
        
        public zza(final zzpt zzZU, final ExecutorService zzaKb) {
            this.zzZU = zzZU;
            this.zzaKb = zzaKb;
        }
        
        private GoogleApiClient.ServerAuthCodeCallbacks zzyb() throws RemoteException {
            return this.zzZU.zzyb();
        }
        
        public void zza(final String s, final String s2, final com.google.android.gms.signin.internal.zzf zzf) throws RemoteException {
            this.zzaKb.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        zzf.zzal(zza.this.zzyb().onUploadServerAuthCode(s, s2));
                    }
                    catch (RemoteException ex) {
                        Log.e("SignInClientImpl", "RemoteException thrown when processing uploadServerAuthCode callback", (Throwable)ex);
                    }
                }
            });
        }
        
        public void zza(final String s, final List<Scope> list, final com.google.android.gms.signin.internal.zzf zzf) throws RemoteException {
            this.zzaKb.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        final GoogleApiClient.ServerAuthCodeCallbacks.CheckResult onCheckServerAuthorization = zza.this.zzyb().onCheckServerAuthorization(s, Collections.unmodifiableSet((Set<? extends Scope>)new HashSet<Scope>(list)));
                        zzf.zza(new CheckServerAuthResult(onCheckServerAuthorization.zzmA(), onCheckServerAuthorization.zzmB()));
                    }
                    catch (RemoteException ex) {
                        Log.e("SignInClientImpl", "RemoteException thrown when processing checkServerAuthorization callback", (Throwable)ex);
                    }
                }
            });
        }
    }
}
