// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common;

import android.os.Looper;
import android.content.ComponentName;
import java.util.concurrent.LinkedBlockingQueue;
import android.os.IBinder;
import java.util.concurrent.BlockingQueue;
import android.content.ServiceConnection;

public class zza implements ServiceConnection
{
    boolean zzVE;
    private final BlockingQueue<IBinder> zzVF;
    
    public zza() {
        this.zzVE = false;
        this.zzVF = new LinkedBlockingQueue<IBinder>();
    }
    
    public void onServiceConnected(final ComponentName componentName, final IBinder binder) {
        this.zzVF.add(binder);
    }
    
    public void onServiceDisconnected(final ComponentName componentName) {
    }
    
    public IBinder zzmh() throws InterruptedException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalStateException("BlockingServiceConnection.getService() called on main thread");
        }
        if (this.zzVE) {
            throw new IllegalStateException();
        }
        this.zzVE = true;
        return this.zzVF.take();
    }
}
