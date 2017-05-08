// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.api;

import android.os.DeadObjectException;
import android.net.Uri;
import android.content.Intent;
import java.lang.ref.WeakReference;
import android.util.Log;
import android.os.Message;
import android.os.Handler;
import android.content.IntentFilter;
import com.google.android.gms.common.internal.zzu;
import java.io.PrintWriter;
import java.io.FileDescriptor;
import java.util.Iterator;
import com.google.android.gms.common.internal.zzz;
import android.os.Bundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Collections;
import java.util.WeakHashMap;
import java.util.HashSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;
import com.google.android.gms.common.ConnectionResult;
import android.content.BroadcastReceiver;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.Map;
import com.google.android.gms.common.internal.zze;
import com.google.android.gms.common.internal.zzj;
import java.util.Set;
import android.os.Looper;
import java.util.concurrent.locks.Lock;
import com.google.android.gms.internal.zzpt;
import com.google.android.gms.internal.zzps;
import android.content.Context;

final class zzg implements GoogleApiClient
{
    private final Context mContext;
    private final int zzWB;
    private final int zzWC;
    final Api.zza<? extends zzps, zzpt> zzWE;
    private final Lock zzWL;
    private final Looper zzWt;
    final Set<zze<?>> zzXA;
    private final zzc zzXB;
    private final ConnectionCallbacks zzXC;
    private final zzj.zza zzXD;
    final com.google.android.gms.common.internal.zze zzXa;
    final Map<Api<?>, Integer> zzXb;
    private final Condition zzXm;
    final zzj zzXn;
    final Queue<zze<?>> zzXo;
    private volatile boolean zzXp;
    private long zzXq;
    private long zzXr;
    final zza zzXs;
    BroadcastReceiver zzXt;
    final Map<Api.ClientKey<?>, Api.Client> zzXu;
    final Map<Api.ClientKey<?>, ConnectionResult> zzXv;
    Set<Scope> zzXw;
    private volatile zzh zzXx;
    private ConnectionResult zzXy;
    private final Set<zzi<?>> zzXz;
    
    public zzg(final Context mContext, final Looper zzWt, final com.google.android.gms.common.internal.zze zzXa, final Api.zza<? extends zzps, zzpt> zzWE, final Map<Api<?>, Api.ApiOptions> map, final Set<ConnectionCallbacks> set, final Set<OnConnectionFailedListener> set2, int zzWB, final int zzWC) {
        this.zzWL = new ReentrantLock();
        this.zzXo = new LinkedList<zze<?>>();
        this.zzXq = 120000L;
        this.zzXr = 5000L;
        this.zzXu = new HashMap<Api.ClientKey<?>, Api.Client>();
        this.zzXv = new HashMap<Api.ClientKey<?>, ConnectionResult>();
        this.zzXw = new HashSet<Scope>();
        this.zzXy = null;
        this.zzXz = Collections.newSetFromMap(new WeakHashMap<zzi<?>, Boolean>());
        this.zzXA = Collections.newSetFromMap(new ConcurrentHashMap<zze<?>, Boolean>(16, 0.75f, 2));
        this.zzXB = (zzc)new zzc() {
            @Override
            public void zzc(final zze<?> zze) {
                zzg.this.zzXA.remove(zze);
            }
        };
        this.zzXC = new zzd() {
            @Override
            public void onConnected(final Bundle bundle) {
                zzg.this.zzXx.onConnected(bundle);
            }
        };
        this.zzXD = new zzj.zza() {
            @Override
            public boolean isConnected() {
                return zzg.this.isConnected();
            }
            
            @Override
            public Bundle zzlM() {
                return null;
            }
        };
        this.mContext = mContext;
        this.zzXn = new zzj(zzWt, this.zzXD);
        this.zzWt = zzWt;
        this.zzXs = new zza(zzWt);
        this.zzWB = zzWB;
        this.zzWC = zzWC;
        this.zzXb = new HashMap<Api<?>, Integer>();
        this.zzXm = this.zzWL.newCondition();
        this.zzXx = new zzf(this);
        final Iterator<ConnectionCallbacks> iterator = set.iterator();
        while (iterator.hasNext()) {
            this.zzXn.registerConnectionCallbacks(iterator.next());
        }
        final Iterator<OnConnectionFailedListener> iterator2 = set2.iterator();
        while (iterator2.hasNext()) {
            this.zzXn.registerConnectionFailedListener(iterator2.next());
        }
        final Map<Api<?>, com.google.android.gms.common.internal.zze.zza> zznx = zzXa.zznx();
        for (final Api<?> api : map.keySet()) {
            final Api.ApiOptions value = map.get(api);
            if (zznx.get(api) != null) {
                if (((com.google.android.gms.common.internal.zze.zza)zznx.get(api)).zzZW) {
                    zzWB = 1;
                }
                else {
                    zzWB = 2;
                }
            }
            else {
                zzWB = 0;
            }
            this.zzXb.put(api, zzWB);
            zzz zzz;
            if (api.zzmt()) {
                zzz = zza(api.zzmq(), value, mContext, zzWt, zzXa, this.zzXC, this.zza(api, zzWB));
            }
            else {
                zzz = zza(api.zzmp(), value, mContext, zzWt, zzXa, this.zzXC, this.zza(api, zzWB));
            }
            this.zzXu.put(api.zzms(), zzz);
        }
        this.zzXa = zzXa;
        this.zzWE = zzWE;
    }
    
    private void resume() {
        this.zzWL.lock();
        try {
            if (this.zzmO()) {
                this.connect();
            }
        }
        finally {
            this.zzWL.unlock();
        }
    }
    
    private static <C extends Api.Client, O> C zza(final Api.zza<C, O> zza, final Object o, final Context context, final Looper looper, final com.google.android.gms.common.internal.zze zze, final ConnectionCallbacks connectionCallbacks, final OnConnectionFailedListener onConnectionFailedListener) {
        return zza.zza(context, looper, zze, (O)o, connectionCallbacks, onConnectionFailedListener);
    }
    
    private final OnConnectionFailedListener zza(final Api<?> api, final int n) {
        return new OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(final ConnectionResult connectionResult) {
                zzg.this.zzXx.zza(connectionResult, api, n);
            }
        };
    }
    
    private static <C extends Api.zzb, O> zzz zza(final Api.zzc<C, O> zzc, final Object o, final Context context, final Looper looper, final com.google.android.gms.common.internal.zze zze, final ConnectionCallbacks connectionCallbacks, final OnConnectionFailedListener onConnectionFailedListener) {
        return new zzz(context, looper, zzc.zzmu(), connectionCallbacks, onConnectionFailedListener, zze, zzc.zzl((O)o));
    }
    
    private void zzaY(final int n) {
        this.zzWL.lock();
        try {
            this.zzXx.zzaV(n);
        }
        finally {
            this.zzWL.unlock();
        }
    }
    
    private void zzmP() {
        this.zzWL.lock();
        try {
            if (this.zzmR()) {
                this.connect();
            }
        }
        finally {
            this.zzWL.unlock();
        }
    }
    
    @Override
    public void connect() {
        this.zzWL.lock();
        try {
            this.zzXx.connect();
        }
        finally {
            this.zzWL.unlock();
        }
    }
    
    @Override
    public void disconnect() {
        this.zzmR();
        this.zzaY(-1);
    }
    
    @Override
    public void dump(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
        printWriter.append(s).append("mState=").append(this.zzXx.getName());
        printWriter.append(" mResuming=").print(this.zzXp);
        printWriter.append(" mWorkQueue.size()=").print(this.zzXo.size());
        printWriter.append(" mUnconsumedRunners.size()=").println(this.zzXA.size());
        final String string = s + "  ";
        for (final Api<?> api : this.zzXb.keySet()) {
            printWriter.append(s).append(api.getName()).println(":");
            this.zzXu.get(api.zzms()).dump(string, fileDescriptor, printWriter, array);
        }
    }
    
    @Override
    public Context getContext() {
        return this.mContext;
    }
    
    @Override
    public Looper getLooper() {
        return this.zzWt;
    }
    
    public int getSessionId() {
        return System.identityHashCode(this);
    }
    
    @Override
    public boolean isConnected() {
        return this.zzXx instanceof com.google.android.gms.common.api.zzd;
    }
    
    @Override
    public boolean isConnecting() {
        return this.zzXx instanceof com.google.android.gms.common.api.zze;
    }
    
    @Override
    public void registerConnectionCallbacks(final ConnectionCallbacks connectionCallbacks) {
        this.zzXn.registerConnectionCallbacks(connectionCallbacks);
    }
    
    @Override
    public void registerConnectionFailedListener(final OnConnectionFailedListener onConnectionFailedListener) {
        this.zzXn.registerConnectionFailedListener(onConnectionFailedListener);
    }
    
    @Override
    public void unregisterConnectionCallbacks(final ConnectionCallbacks connectionCallbacks) {
        this.zzXn.unregisterConnectionCallbacks(connectionCallbacks);
    }
    
    @Override
    public void unregisterConnectionFailedListener(final OnConnectionFailedListener onConnectionFailedListener) {
        this.zzXn.unregisterConnectionFailedListener(onConnectionFailedListener);
    }
    
    @Override
    public <C extends Api.Client> C zza(final Api.ClientKey<C> clientKey) {
        final Api.Client client = this.zzXu.get(clientKey);
        zzu.zzb(client, "Appropriate Api was not requested.");
        return (C)client;
    }
    
    @Override
    public <A extends Api.Client, R extends Result, T extends com.google.android.gms.common.api.zza.zza<R, A>> T zza(final T t) {
        Label_0066: {
            if (t.zzms() == null) {
                break Label_0066;
            }
            boolean b = true;
            while (true) {
                zzu.zzb(b, "This task can not be enqueued (it's probably a Batch or malformed)");
                zzu.zzb(this.zzXu.containsKey(t.zzms()), "GoogleApiClient is not configured to use the API required for this call.");
                this.zzWL.lock();
                try {
                    return this.zzXx.zza(t);
                    b = false;
                }
                finally {
                    this.zzWL.unlock();
                }
            }
        }
    }
    
    @Override
    public <A extends Api.Client, T extends com.google.android.gms.common.api.zza.zza<? extends Result, A>> T zzb(final T t) {
        while (true) {
            Label_0097: {
                if (t.zzms() == null) {
                    break Label_0097;
                }
                final boolean b = true;
                zzu.zzb(b, "This task can not be executed (it's probably a Batch or malformed)");
                this.zzWL.lock();
                final T t2;
                Label_0113: {
                    Label_0102: {
                        try {
                            if (this.zzmO()) {
                                this.zzXo.add((zze<?>)t);
                                while (!this.zzXo.isEmpty()) {
                                    final zze<?> zze = this.zzXo.remove();
                                    this.zzb((zze<Api.Client>)zze);
                                    zze.zzr(Status.zzXR);
                                }
                                break Label_0102;
                            }
                            break Label_0113;
                        }
                        finally {
                            this.zzWL.unlock();
                        }
                        break Label_0097;
                    }
                    this.zzWL.unlock();
                    return t2;
                }
                final com.google.android.gms.common.api.zza.zza<? extends Result, A> zzb = this.zzXx.zzb(t2);
                this.zzWL.unlock();
                return (T)zzb;
            }
            final boolean b = false;
            continue;
        }
    }
    
     <A extends Api.Client> void zzb(final zze<A> zze) {
        this.zzXA.add(zze);
        zze.zza(this.zzXB);
    }
    
    void zze(final ConnectionResult zzXy) {
        this.zzWL.lock();
        try {
            this.zzXy = zzXy;
            (this.zzXx = new zzf(this)).begin();
            this.zzXm.signalAll();
        }
        finally {
            this.zzWL.unlock();
        }
    }
    
    void zzmK() {
        for (final zze<?> zze : this.zzXA) {
            zze.zza(null);
            zze.cancel();
        }
        this.zzXA.clear();
        final Iterator<zzi<?>> iterator2 = this.zzXz.iterator();
        while (iterator2.hasNext()) {
            iterator2.next().clear();
        }
        this.zzXz.clear();
        this.zzXw.clear();
    }
    
    void zzmL() {
        final Iterator<Api.Client> iterator = this.zzXu.values().iterator();
        while (iterator.hasNext()) {
            iterator.next().disconnect();
        }
    }
    
    void zzmM() {
        this.zzWL.lock();
        try {
            (this.zzXx = new com.google.android.gms.common.api.zze(this, this.zzXa, this.zzXb, this.zzWE, this.zzWL, this.mContext)).begin();
            this.zzXm.signalAll();
        }
        finally {
            this.zzWL.unlock();
        }
    }
    
    void zzmN() {
        this.zzWL.lock();
        try {
            this.zzmR();
            (this.zzXx = new com.google.android.gms.common.api.zzd(this)).begin();
            this.zzXm.signalAll();
        }
        finally {
            this.zzWL.unlock();
        }
    }
    
    boolean zzmO() {
        return this.zzXp;
    }
    
    void zzmQ() {
        if (this.zzmO()) {
            return;
        }
        this.zzXp = true;
        if (this.zzXt == null) {
            this.zzXt = new zzb(this);
            final IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_ADDED");
            intentFilter.addDataScheme("package");
            this.mContext.getApplicationContext().registerReceiver(this.zzXt, intentFilter);
        }
        this.zzXs.sendMessageDelayed(this.zzXs.obtainMessage(1), this.zzXq);
        this.zzXs.sendMessageDelayed(this.zzXs.obtainMessage(2), this.zzXr);
    }
    
    boolean zzmR() {
        this.zzWL.lock();
        try {
            if (!this.zzmO()) {
                return false;
            }
            this.zzXp = false;
            this.zzXs.removeMessages(2);
            this.zzXs.removeMessages(1);
            if (this.zzXt != null) {
                this.mContext.getApplicationContext().unregisterReceiver(this.zzXt);
                this.zzXt = null;
            }
            return true;
        }
        finally {
            this.zzWL.unlock();
        }
    }
    
    final class zza extends Handler
    {
        zza(final Looper looper) {
            super(looper);
        }
        
        public void handleMessage(final Message message) {
            switch (message.what) {
                default: {
                    Log.w("GoogleApiClientImpl", "Unknown message id: " + message.what);
                }
                case 1: {
                    zzg.this.zzmP();
                }
                case 2: {
                    zzg.this.resume();
                }
            }
        }
    }
    
    private static class zzb extends BroadcastReceiver
    {
        private WeakReference<zzg> zzXK;
        
        zzb(final zzg zzg) {
            this.zzXK = new WeakReference<zzg>(zzg);
        }
        
        public void onReceive(final Context context, final Intent intent) {
            final Uri data = intent.getData();
            String schemeSpecificPart = null;
            if (data != null) {
                schemeSpecificPart = data.getSchemeSpecificPart();
            }
            if (schemeSpecificPart != null && schemeSpecificPart.equals("com.google.android.gms")) {
                final zzg zzg = this.zzXK.get();
                if (zzg != null) {
                    zzg.resume();
                }
            }
        }
    }
    
    interface zzc
    {
        void zzc(final zze<?> p0);
    }
    
    public abstract class zzd implements ConnectionCallbacks
    {
        @Override
        public void onConnectionSuspended(final int n) {
            zzg.this.zzWL.lock();
            try {
                zzg.this.zzXx.onConnectionSuspended(n);
            }
            finally {
                zzg.this.zzWL.unlock();
            }
        }
    }
    
    interface zze<A extends Api.Client>
    {
        void cancel();
        
        void forceFailureUnlessReady(final Status p0);
        
        void zza(final zzc p0);
        
        void zzb(final A p0) throws DeadObjectException;
        
        Api.ClientKey<A> zzms();
        
        int zzmv();
        
        void zzr(final Status p0);
    }
}
