// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.api;

import com.google.android.gms.common.internal.zzu;
import android.os.Looper;
import com.google.android.gms.common.internal.zzq;
import com.google.android.gms.signin.internal.AuthAccountResult;
import java.lang.ref.WeakReference;
import com.google.android.gms.signin.internal.zzb;
import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;
import com.google.android.gms.common.GooglePlayServicesUtil;
import android.app.PendingIntent;
import android.util.Log;
import com.google.android.gms.common.internal.ResolveAccountResponse;
import java.util.HashSet;
import java.util.Map;
import com.google.android.gms.common.internal.IAccountAccessor;
import java.util.Set;
import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import java.util.concurrent.locks.Lock;
import com.google.android.gms.internal.zzpt;
import com.google.android.gms.internal.zzps;
import android.content.Context;

public class zze implements zzh
{
    private final Context mContext;
    private final Api.zza<? extends zzps, zzpt> zzWE;
    private final zzg zzWK;
    private final Lock zzWL;
    private ConnectionResult zzWM;
    private int zzWN;
    private int zzWO;
    private boolean zzWP;
    private int zzWQ;
    private final Bundle zzWR;
    private final Set<Api.ClientKey> zzWS;
    private zzps zzWT;
    private int zzWU;
    private boolean zzWV;
    private boolean zzWW;
    private IAccountAccessor zzWX;
    private boolean zzWY;
    private boolean zzWZ;
    private final com.google.android.gms.common.internal.zze zzXa;
    private final Map<Api<?>, Integer> zzXb;
    
    public zze(final zzg zzWK, final com.google.android.gms.common.internal.zze zzXa, final Map<Api<?>, Integer> zzXb, final Api.zza<? extends zzps, zzpt> zzWE, final Lock zzWL, final Context mContext) {
        this.zzWO = 0;
        this.zzWP = false;
        this.zzWR = new Bundle();
        this.zzWS = new HashSet<Api.ClientKey>();
        this.zzWK = zzWK;
        this.zzXa = zzXa;
        this.zzXb = zzXb;
        this.zzWE = zzWE;
        this.zzWL = zzWL;
        this.mContext = mContext;
    }
    
    private void zzT(final boolean b) {
        if (this.zzWT != null) {
            if (((Api.Client)this.zzWT).isConnected()) {
                if (b) {
                    this.zzWT.zzxY();
                }
                ((Api.Client)this.zzWT).disconnect();
            }
            this.zzWX = null;
        }
    }
    
    private void zza(final ConnectionResult connectionResult) {
        while (true) {
            this.zzWL.lock();
            Label_0081: {
                try {
                    if (!this.zzaW(2)) {
                        return;
                    }
                    if (connectionResult.isSuccess()) {
                        this.zzmG();
                    }
                    else {
                        if (!this.zzc(connectionResult)) {
                            break Label_0081;
                        }
                        this.zzmI();
                        this.zzmG();
                    }
                    return;
                }
                finally {
                    this.zzWL.unlock();
                }
            }
            final ConnectionResult connectionResult2;
            this.zzd(connectionResult2);
        }
    }
    
    private void zza(final ResolveAccountResponse resolveAccountResponse) {
        while (true) {
            final ConnectionResult zzoa = resolveAccountResponse.zzoa();
            this.zzWL.lock();
            Label_0122: {
                try {
                    if (!this.zzaW(0)) {
                        return;
                    }
                    if (zzoa.isSuccess()) {
                        this.zzWX = resolveAccountResponse.zznZ();
                        this.zzWW = true;
                        this.zzWY = resolveAccountResponse.zzob();
                        this.zzWZ = resolveAccountResponse.zzoc();
                        this.zzmE();
                    }
                    else {
                        if (!this.zzc(zzoa)) {
                            break Label_0122;
                        }
                        this.zzmI();
                        if (this.zzWQ == 0) {
                            this.zzmG();
                        }
                    }
                    return;
                }
                finally {
                    this.zzWL.unlock();
                }
            }
            this.zzd(zzoa);
        }
    }
    
    private boolean zza(final int n, final int n2, final ConnectionResult connectionResult) {
        return (n2 != 1 || zzb(connectionResult)) && (this.zzWM == null || n < this.zzWN);
    }
    
    private boolean zzaW(final int n) {
        if (this.zzWO != n) {
            Log.wtf("GoogleApiClientConnecting", "GoogleApiClient connecting is in step " + this.zzaX(this.zzWO) + " but received callback for step " + this.zzaX(n));
            this.zzd(new ConnectionResult(8, null));
            return false;
        }
        return true;
    }
    
    private String zzaX(final int n) {
        switch (n) {
            default: {
                return "UNKNOWN";
            }
            case 0: {
                return "STEP_GETTING_SERVICE_BINDINGS";
            }
            case 1: {
                return "STEP_VALIDATING_ACCOUNT";
            }
            case 2: {
                return "STEP_AUTHENTICATING";
            }
            case 3: {
                return "STEP_GETTING_REMOTE_SERVICE";
            }
        }
    }
    
    private void zzb(final ConnectionResult zzWM, final Api<?> api, final int n) {
        if (n != 2) {
            final int priority = api.zzmp().getPriority();
            if (this.zza(priority, n, zzWM)) {
                this.zzWM = zzWM;
                this.zzWN = priority;
            }
        }
        this.zzWK.zzXv.put(api.zzms(), zzWM);
    }
    
    private static boolean zzb(final ConnectionResult connectionResult) {
        return connectionResult.hasResolution() || GooglePlayServicesUtil.zzaT(connectionResult.getErrorCode()) != null;
    }
    
    private boolean zzc(final ConnectionResult connectionResult) {
        return this.zzWU == 2 || (this.zzWU == 1 && !connectionResult.hasResolution());
    }
    
    private void zzd(final ConnectionResult zzWM) {
        boolean b = false;
        this.zzWP = false;
        this.zzWK.zzXw.clear();
        this.zzWM = zzWM;
        if (!zzWM.hasResolution()) {
            b = true;
        }
        this.zzT(b);
        this.zzaV(3);
        if (!this.zzWK.zzmO() || !GooglePlayServicesUtil.zze(this.mContext, zzWM.getErrorCode())) {
            this.zzWK.zzmR();
            this.zzWK.zzXn.zzh(zzWM);
        }
        this.zzWK.zzXn.zznT();
    }
    
    private boolean zzmC() {
        --this.zzWQ;
        if (this.zzWQ > 0) {
            return false;
        }
        if (this.zzWQ < 0) {
            Log.wtf("GoogleApiClientConnecting", "GoogleApiClient received too many callbacks for the given step. Clients may be in an unexpected state; GoogleApiClient will now disconnect.");
            this.zzd(new ConnectionResult(8, null));
            return false;
        }
        if (this.zzWM != null) {
            this.zzd(this.zzWM);
            return false;
        }
        return true;
    }
    
    private void zzmD() {
        if (this.zzWV) {
            this.zzmE();
            return;
        }
        this.zzmG();
    }
    
    private void zzmE() {
        if (this.zzWW && this.zzWQ == 0) {
            this.zzWO = 1;
            this.zzWQ = this.zzWK.zzXu.size();
            for (final Api.ClientKey clientKey : this.zzWK.zzXu.keySet()) {
                if (this.zzWK.zzXv.containsKey(clientKey)) {
                    if (!this.zzmC()) {
                        continue;
                    }
                    this.zzmF();
                }
                else {
                    this.zzWK.zzXu.get(clientKey).validateAccount(this.zzWX);
                }
            }
        }
    }
    
    private void zzmF() {
        this.zzWO = 2;
        this.zzWK.zzXw = this.zzmJ();
        this.zzWT.zza(this.zzWX, this.zzWK.zzXw, new zza(this));
    }
    
    private void zzmG() {
        Set<Scope> set = this.zzWK.zzXw;
        if (set.isEmpty()) {
            set = this.zzmJ();
        }
        this.zzWO = 3;
        this.zzWQ = this.zzWK.zzXu.size();
        for (final Api.ClientKey clientKey : this.zzWK.zzXu.keySet()) {
            if (this.zzWK.zzXv.containsKey(clientKey)) {
                if (!this.zzmC()) {
                    continue;
                }
                this.zzmH();
            }
            else {
                this.zzWK.zzXu.get(clientKey).getRemoteService(this.zzWX, set);
            }
        }
    }
    
    private void zzmH() {
        this.zzWK.zzmN();
        if (this.zzWT != null) {
            if (this.zzWY) {
                this.zzWT.zza(this.zzWX, this.zzWZ);
            }
            this.zzT(false);
        }
        final Iterator<Api.ClientKey<?>> iterator = this.zzWK.zzXv.keySet().iterator();
        while (iterator.hasNext()) {
            this.zzWK.zzXu.get((Api.ClientKey)iterator.next()).disconnect();
        }
        if (this.zzWP) {
            this.zzWP = false;
            this.zzaV(-1);
            return;
        }
        Bundle zzWR;
        if (this.zzWR.isEmpty()) {
            zzWR = null;
        }
        else {
            zzWR = this.zzWR;
        }
        this.zzWK.zzXn.zzg(zzWR);
    }
    
    private void zzmI() {
        this.zzWV = false;
        this.zzWK.zzXw.clear();
        for (final Api.ClientKey clientKey : this.zzWS) {
            if (!this.zzWK.zzXv.containsKey(clientKey)) {
                this.zzWK.zzXv.put(clientKey, new ConnectionResult(17, null));
            }
        }
    }
    
    private Set<Scope> zzmJ() {
        final HashSet<Object> set = (HashSet<Object>)new HashSet<Scope>(this.zzXa.zznv());
        final Map<Api<?>, com.google.android.gms.common.internal.zze.zza> zznx = this.zzXa.zznx();
        for (final Api<?> api : zznx.keySet()) {
            if (!this.zzWK.zzXv.containsKey(api.zzms())) {
                set.addAll(((com.google.android.gms.common.internal.zze.zza)zznx.get(api)).zzWJ);
            }
        }
        return (Set<Scope>)set;
    }
    
    @Override
    public void begin() {
        this.zzWK.zzXn.zznU();
        this.zzWK.zzXv.clear();
        this.zzWP = false;
        this.zzWV = false;
        this.zzWM = null;
        this.zzWO = 0;
        this.zzWU = 2;
        this.zzWW = false;
        this.zzWY = false;
        final int googlePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.mContext);
        if (googlePlayServicesAvailable != 0) {
            this.zzWK.zzXs.post((Runnable)new Runnable() {
                final /* synthetic */ ConnectionResult zzXc = new ConnectionResult(googlePlayServicesAvailable, null);
                
                @Override
                public void run() {
                    zze.this.zzWL.lock();
                    try {
                        zze.this.zzd(this.zzXc);
                    }
                    finally {
                        zze.this.zzWL.unlock();
                    }
                }
            });
        }
        else {
            final HashMap<Api.Client, zzc> hashMap = (HashMap<Api.Client, zzc>)new HashMap<Object, zzc>();
            final Iterator<Api<?>> iterator = this.zzXb.keySet().iterator();
            int n = false ? 1 : 0;
            while (iterator.hasNext()) {
                final Api<?> api = iterator.next();
                final Api.Client client = this.zzWK.zzXu.get(api.zzms());
                final int intValue = this.zzXb.get(api);
                boolean b;
                if (api.zzmp().getPriority() == 1) {
                    b = true;
                }
                else {
                    b = false;
                }
                if (client.requiresSignIn()) {
                    this.zzWV = true;
                    if (intValue < this.zzWU) {
                        this.zzWU = intValue;
                    }
                    if (intValue != 0) {
                        this.zzWS.add((Api.ClientKey)api.zzms());
                    }
                }
                hashMap.put(client, new zzc(this, api, intValue));
                n |= (b ? 1 : 0);
            }
            if (n != 0) {
                this.zzWV = false;
            }
            if (this.zzWV) {
                this.zzXa.zza(this.zzWK.getSessionId());
                final zzd zzd = new zzd();
                (this.zzWT = (zzps)this.zzWE.zza(this.mContext, this.zzWK.getLooper(), this.zzXa, this.zzXa.zznB(), zzd, zzd)).connect();
            }
            this.zzWQ = this.zzWK.zzXu.size();
            for (final Api.Client client2 : this.zzWK.zzXu.values()) {
                client2.connect(hashMap.get(client2));
            }
        }
    }
    
    @Override
    public void connect() {
        this.zzWP = false;
    }
    
    @Override
    public String getName() {
        return "CONNECTING";
    }
    
    @Override
    public void onConnected(final Bundle bundle) {
        if (this.zzaW(3)) {
            if (bundle != null) {
                this.zzWR.putAll(bundle);
            }
            if (this.zzmC()) {
                this.zzmH();
            }
        }
    }
    
    @Override
    public void onConnectionSuspended(final int n) {
        this.zzd(new ConnectionResult(8, null));
    }
    
    @Override
    public <A extends Api.Client, R extends Result, T extends com.google.android.gms.common.api.zza.zza<R, A>> T zza(final T t) {
        this.zzWK.zzXo.add((zzg.zze<?>)t);
        return t;
    }
    
    @Override
    public void zza(final ConnectionResult connectionResult, final Api<?> api, final int n) {
        if (this.zzaW(3)) {
            this.zzb(connectionResult, api, n);
            if (this.zzmC()) {
                this.zzmH();
            }
        }
    }
    
    @Override
    public void zzaV(final int n) {
        if (n == -1) {
            final Iterator<zzg.zze> iterator = (Iterator<zzg.zze>)this.zzWK.zzXo.iterator();
            while (iterator.hasNext()) {
                final zzg.zze zze = iterator.next();
                if (zze.zzmv() != 1) {
                    zze.cancel();
                    iterator.remove();
                }
            }
            this.zzWK.zzmK();
            if (this.zzWM == null && !this.zzWK.zzXo.isEmpty()) {
                this.zzWP = true;
                return;
            }
            this.zzWK.zzXv.clear();
            this.zzWM = null;
            this.zzT(true);
        }
        this.zzWK.zze(this.zzWM);
    }
    
    @Override
    public <A extends Api.Client, T extends com.google.android.gms.common.api.zza.zza<? extends Result, A>> T zzb(final T t) {
        throw new IllegalStateException("GoogleApiClient is not connected yet.");
    }
    
    private static class zza extends zzb
    {
        private final WeakReference<zze> zzXe;
        
        zza(final zze zze) {
            this.zzXe = new WeakReference<zze>(zze);
        }
        
        @Override
        public void zza(final ConnectionResult connectionResult, final AuthAccountResult authAccountResult) {
            final zze zze = this.zzXe.get();
            if (zze == null) {
                return;
            }
            zze.zzWK.zzXs.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    zze.zza(connectionResult);
                }
            });
        }
    }
    
    private static class zzb extends zzq.zza
    {
        private final WeakReference<zze> zzXe;
        
        zzb(final zze zze) {
            this.zzXe = new WeakReference<zze>(zze);
        }
        
        public void zzb(final ResolveAccountResponse resolveAccountResponse) {
            final zze zze = this.zzXe.get();
            if (zze == null) {
                return;
            }
            zze.zzWK.zzXs.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    zze.zza(resolveAccountResponse);
                }
            });
        }
    }
    
    private static class zzc implements ConnectionProgressReportCallbacks
    {
        private final WeakReference<zze> zzXe;
        private final Api<?> zzXk;
        private final int zzXl;
        
        public zzc(final zze zze, final Api<?> zzXk, final int zzXl) {
            this.zzXe = new WeakReference<zze>(zze);
            this.zzXk = zzXk;
            this.zzXl = zzXl;
        }
        
        @Override
        public void onReportAccountValidation(final ConnectionResult connectionResult) {
            boolean zza = true;
            final zze zze = this.zzXe.get();
            if (zze == null) {
                return;
            }
            Label_0066: {
                if (Looper.myLooper() != zze.zzWK.getLooper()) {
                    break Label_0066;
                }
                while (true) {
                    zzu.zza(zza, (Object)"onReportAccountValidation must be called on the GoogleApiClient handler thread");
                    zze.zzWL.lock();
                    try {
                        zza = zze.zzaW(1);
                        if (!zza) {
                            return;
                        }
                        if (!connectionResult.isSuccess()) {
                            zze.zzb(connectionResult, this.zzXk, this.zzXl);
                        }
                        if (zze.zzmC()) {
                            zze.zzmF();
                        }
                        return;
                        zza = false;
                    }
                    finally {
                        zze.zzWL.unlock();
                    }
                }
            }
        }
        
        @Override
        public void onReportServiceBinding(final ConnectionResult connectionResult) {
            boolean b = false;
            final zze zze = this.zzXe.get();
            if (zze == null) {
                return;
            }
            if (Looper.myLooper() == zze.zzWK.getLooper()) {
                b = true;
            }
            zzu.zza(b, (Object)"onReportServiceBinding must be called on the GoogleApiClient handler thread");
            zze.zzWL.lock();
            try {
                if (!zze.zzaW(0)) {
                    return;
                }
                if (!connectionResult.isSuccess()) {
                    zze.zzb(connectionResult, this.zzXk, this.zzXl);
                }
                if (zze.zzmC()) {
                    zze.zzmD();
                }
            }
            finally {
                zze.zzWL.unlock();
            }
        }
    }
    
    private class zzd implements ConnectionCallbacks, OnConnectionFailedListener
    {
        @Override
        public void onConnected(final Bundle bundle) {
            zze.this.zzWT.zza(new zzb(zze.this));
        }
        
        @Override
        public void onConnectionFailed(final ConnectionResult connectionResult) {
            zze.this.zzWL.lock();
            try {
                if (zze.this.zzc(connectionResult)) {
                    zze.this.zzmI();
                    zze.this.zzmG();
                }
                else {
                    zze.this.zzd(connectionResult);
                }
            }
            finally {
                zze.this.zzWL.unlock();
            }
        }
        
        @Override
        public void onConnectionSuspended(final int n) {
        }
    }
}
