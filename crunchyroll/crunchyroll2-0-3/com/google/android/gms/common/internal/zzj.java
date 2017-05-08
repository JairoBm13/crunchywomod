// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.internal;

import com.google.android.gms.common.ConnectionResult;
import android.os.Bundle;
import java.util.Iterator;
import java.util.Collection;
import android.util.Log;
import android.os.Message;
import android.os.Looper;
import java.util.concurrent.atomic.AtomicInteger;
import com.google.android.gms.common.api.GoogleApiClient;
import java.util.ArrayList;
import android.os.Handler;
import android.os.Handler$Callback;

public final class zzj implements Handler$Callback
{
    private final Handler mHandler;
    private final zza zzaaC;
    private final ArrayList<GoogleApiClient.ConnectionCallbacks> zzaaD;
    final ArrayList<GoogleApiClient.ConnectionCallbacks> zzaaE;
    private final ArrayList<GoogleApiClient.OnConnectionFailedListener> zzaaF;
    private volatile boolean zzaaG;
    private final AtomicInteger zzaaH;
    private boolean zzaaI;
    private final Object zzqt;
    
    public zzj(final Looper looper, final zza zzaaC) {
        this.zzaaD = new ArrayList<GoogleApiClient.ConnectionCallbacks>();
        this.zzaaE = new ArrayList<GoogleApiClient.ConnectionCallbacks>();
        this.zzaaF = new ArrayList<GoogleApiClient.OnConnectionFailedListener>();
        this.zzaaG = false;
        this.zzaaH = new AtomicInteger(0);
        this.zzaaI = false;
        this.zzqt = new Object();
        this.zzaaC = zzaaC;
        this.mHandler = new Handler(looper, (Handler$Callback)this);
    }
    
    public boolean handleMessage(final Message message) {
        if (message.what == 1) {
            final GoogleApiClient.ConnectionCallbacks connectionCallbacks = (GoogleApiClient.ConnectionCallbacks)message.obj;
            synchronized (this.zzqt) {
                if (this.zzaaG && this.zzaaC.isConnected() && this.zzaaD.contains(connectionCallbacks)) {
                    connectionCallbacks.onConnected(this.zzaaC.zzlM());
                }
                return true;
            }
        }
        Log.wtf("GmsClientEvents", "Don't know how to handle this message.");
        return false;
    }
    
    public void registerConnectionCallbacks(final GoogleApiClient.ConnectionCallbacks connectionCallbacks) {
        zzu.zzu(connectionCallbacks);
        synchronized (this.zzqt) {
            if (this.zzaaD.contains(connectionCallbacks)) {
                Log.w("GmsClientEvents", "registerConnectionCallbacks(): listener " + connectionCallbacks + " is already registered");
            }
            else {
                this.zzaaD.add(connectionCallbacks);
            }
            // monitorexit(this.zzqt)
            if (this.zzaaC.isConnected()) {
                this.mHandler.sendMessage(this.mHandler.obtainMessage(1, (Object)connectionCallbacks));
            }
        }
    }
    
    public void registerConnectionFailedListener(final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        zzu.zzu(onConnectionFailedListener);
        synchronized (this.zzqt) {
            if (this.zzaaF.contains(onConnectionFailedListener)) {
                Log.w("GmsClientEvents", "registerConnectionFailedListener(): listener " + onConnectionFailedListener + " is already registered");
            }
            else {
                this.zzaaF.add(onConnectionFailedListener);
            }
        }
    }
    
    public void unregisterConnectionCallbacks(final GoogleApiClient.ConnectionCallbacks connectionCallbacks) {
        zzu.zzu(connectionCallbacks);
        synchronized (this.zzqt) {
            if (!this.zzaaD.remove(connectionCallbacks)) {
                Log.w("GmsClientEvents", "unregisterConnectionCallbacks(): listener " + connectionCallbacks + " not found");
            }
            else if (this.zzaaI) {
                this.zzaaE.add(connectionCallbacks);
            }
        }
    }
    
    public void unregisterConnectionFailedListener(final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        zzu.zzu(onConnectionFailedListener);
        synchronized (this.zzqt) {
            if (!this.zzaaF.remove(onConnectionFailedListener)) {
                Log.w("GmsClientEvents", "unregisterConnectionFailedListener(): listener " + onConnectionFailedListener + " not found");
            }
        }
    }
    
    public void zzbu(final int n) {
        this.mHandler.removeMessages(1);
        synchronized (this.zzqt) {
            this.zzaaI = true;
            final ArrayList<GoogleApiClient.ConnectionCallbacks> list = new ArrayList<GoogleApiClient.ConnectionCallbacks>(this.zzaaD);
            final int value = this.zzaaH.get();
            for (final GoogleApiClient.ConnectionCallbacks connectionCallbacks : list) {
                if (!this.zzaaG || this.zzaaH.get() != value) {
                    break;
                }
                if (!this.zzaaD.contains(connectionCallbacks)) {
                    continue;
                }
                connectionCallbacks.onConnectionSuspended(n);
            }
            this.zzaaE.clear();
            this.zzaaI = false;
        }
    }
    
    public void zzg(final Bundle bundle) {
        boolean b;
        boolean b2;
        boolean b3;
        ArrayList<GoogleApiClient.ConnectionCallbacks> list;
        int value;
        Label_0021_Outer:Label_0051_Outer:
        while (true) {
            b = true;
            while (true) {
            Label_0185:
                while (true) {
                    synchronized (this.zzqt) {
                        if (!this.zzaaI) {
                            b2 = true;
                            zzu.zzU(b2);
                            this.mHandler.removeMessages(1);
                            this.zzaaI = true;
                            if (this.zzaaE.size() == 0) {
                                b3 = b;
                                zzu.zzU(b3);
                                list = new ArrayList<GoogleApiClient.ConnectionCallbacks>(this.zzaaD);
                                value = this.zzaaH.get();
                                for (final GoogleApiClient.ConnectionCallbacks connectionCallbacks : list) {
                                    if (!this.zzaaG || !this.zzaaC.isConnected() || this.zzaaH.get() != value) {
                                        break;
                                    }
                                    if (this.zzaaE.contains(connectionCallbacks)) {
                                        continue Label_0021_Outer;
                                    }
                                    connectionCallbacks.onConnected(bundle);
                                }
                                this.zzaaE.clear();
                                this.zzaaI = false;
                                return;
                            }
                            break Label_0185;
                        }
                    }
                    b2 = false;
                    continue Label_0051_Outer;
                }
                b3 = false;
                continue;
            }
        }
    }
    
    public void zzh(final ConnectionResult connectionResult) {
        this.mHandler.removeMessages(1);
        synchronized (this.zzqt) {
            final ArrayList<GoogleApiClient.OnConnectionFailedListener> list = new ArrayList<GoogleApiClient.OnConnectionFailedListener>(this.zzaaF);
            final int value = this.zzaaH.get();
            for (final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener : list) {
                if (!this.zzaaG || this.zzaaH.get() != value) {
                    return;
                }
                if (!this.zzaaF.contains(onConnectionFailedListener)) {
                    continue;
                }
                onConnectionFailedListener.onConnectionFailed(connectionResult);
            }
        }
    }
    // monitorexit(o)
    
    public void zznT() {
        this.zzaaG = false;
        this.zzaaH.incrementAndGet();
    }
    
    public void zznU() {
        this.zzaaG = true;
    }
    
    public interface zza
    {
        boolean isConnected();
        
        Bundle zzlM();
    }
}
