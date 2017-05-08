// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.internal;

import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;
import android.os.IBinder;
import android.content.Intent;
import android.content.ComponentName;
import android.os.Message;
import android.content.ServiceConnection;
import android.content.Context;
import com.google.android.gms.common.stats.zzb;
import java.util.HashMap;
import android.os.Handler;
import android.os.Handler$Callback;

final class zzl extends zzk implements Handler$Callback
{
    private final Handler mHandler;
    private final HashMap<zza, zzb> zzaaL;
    private final com.google.android.gms.common.stats.zzb zzaaM;
    private final long zzaaN;
    private final Context zzqw;
    
    zzl(final Context context) {
        this.zzaaL = new HashMap<zza, zzb>();
        this.zzqw = context.getApplicationContext();
        this.mHandler = new Handler(context.getMainLooper(), (Handler$Callback)this);
        this.zzaaM = com.google.android.gms.common.stats.zzb.zzoO();
        this.zzaaN = 5000L;
    }
    
    private boolean zza(final zza zza, final ServiceConnection serviceConnection, final String s) {
        while (true) {
            zzu.zzb(serviceConnection, "ServiceConnection must not be null");
            while (true) {
                zzb zzb;
                synchronized (this.zzaaL) {
                    zzb = this.zzaaL.get(zza);
                    if (zzb == null) {
                        zzb = new zzb(zza);
                        zzb.zza(serviceConnection, s);
                        zzb.zzcc(s);
                        this.zzaaL.put(zza, zzb);
                        final zzb zzb2 = zzb;
                        return zzb2.isBound();
                    }
                    this.mHandler.removeMessages(0, (Object)zzb);
                    if (zzb.zza(serviceConnection)) {
                        throw new IllegalStateException("Trying to bind a GmsServiceConnection that was already connected before.  config=" + zza);
                    }
                }
                zzb.zza(serviceConnection, s);
                switch (zzb.getState()) {
                    case 1: {
                        serviceConnection.onServiceConnected(zzb.getComponentName(), zzb.getBinder());
                        final zzb zzb2 = zzb;
                        continue;
                    }
                    case 2: {
                        zzb.zzcc(s);
                        final zzb zzb2 = zzb;
                        continue;
                    }
                    default: {
                        final zzb zzb2 = zzb;
                        continue;
                    }
                }
                break;
            }
        }
    }
    
    private void zzb(final zza zza, final ServiceConnection serviceConnection, final String s) {
        zzu.zzb(serviceConnection, "ServiceConnection must not be null");
        final zzb zzb;
        synchronized (this.zzaaL) {
            zzb = this.zzaaL.get(zza);
            if (zzb == null) {
                throw new IllegalStateException("Nonexistent connection status for service config: " + zza);
            }
        }
        if (!zzb.zza(serviceConnection)) {
            final Throwable t;
            throw new IllegalStateException("Trying to unbind a GmsServiceConnection  that was not bound before.  config=" + t);
        }
        zzb.zzb(serviceConnection, s);
        if (zzb.zznW()) {
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(0, (Object)zzb), this.zzaaN);
        }
    }
    // monitorexit(hashMap)
    
    public boolean handleMessage(final Message message) {
        switch (message.what) {
            default: {
                return false;
            }
            case 0: {
                final zzb zzb = (zzb)message.obj;
                synchronized (this.zzaaL) {
                    if (zzb.zznW()) {
                        if (zzb.isBound()) {
                            zzb.zzcd("GmsClientSupervisor");
                        }
                        this.zzaaL.remove(zzb.zzaaS);
                    }
                    return true;
                }
                break;
            }
        }
    }
    
    @Override
    public boolean zza(final String s, final ServiceConnection serviceConnection, final String s2) {
        return this.zza(new zza(s), serviceConnection, s2);
    }
    
    @Override
    public void zzb(final String s, final ServiceConnection serviceConnection, final String s2) {
        this.zzb(new zza(s), serviceConnection, s2);
    }
    
    private static final class zza
    {
        private final ComponentName zzaaO;
        private final String zzuO;
        
        public zza(final String s) {
            this.zzuO = zzu.zzcj(s);
            this.zzaaO = null;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this != o) {
                if (!(o instanceof zza)) {
                    return false;
                }
                final zza zza = (zza)o;
                if (!zzt.equal(this.zzuO, zza.zzuO) || !zzt.equal(this.zzaaO, zza.zzaaO)) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public int hashCode() {
            return zzt.hashCode(this.zzuO, this.zzaaO);
        }
        
        @Override
        public String toString() {
            if (this.zzuO == null) {
                return this.zzaaO.flattenToString();
            }
            return this.zzuO;
        }
        
        public Intent zznV() {
            if (this.zzuO != null) {
                return new Intent(this.zzuO).setPackage("com.google.android.gms");
            }
            return new Intent().setComponent(this.zzaaO);
        }
    }
    
    private final class zzb
    {
        private int mState;
        private IBinder zzZQ;
        private ComponentName zzaaO;
        private final zza zzaaP;
        private final Set<ServiceConnection> zzaaQ;
        private boolean zzaaR;
        private final zzl.zza zzaaS;
        
        public zzb(final zzl.zza zzaaS) {
            this.zzaaS = zzaaS;
            this.zzaaP = new zza();
            this.zzaaQ = new HashSet<ServiceConnection>();
            this.mState = 2;
        }
        
        public IBinder getBinder() {
            return this.zzZQ;
        }
        
        public ComponentName getComponentName() {
            return this.zzaaO;
        }
        
        public int getState() {
            return this.mState;
        }
        
        public boolean isBound() {
            return this.zzaaR;
        }
        
        public void zza(final ServiceConnection serviceConnection, final String s) {
            zzl.this.zzaaM.zza(zzl.this.zzqw, serviceConnection, s, this.zzaaS.zznV());
            this.zzaaQ.add(serviceConnection);
        }
        
        public boolean zza(final ServiceConnection serviceConnection) {
            return this.zzaaQ.contains(serviceConnection);
        }
        
        public void zzb(final ServiceConnection serviceConnection, final String s) {
            zzl.this.zzaaM.zzb(zzl.this.zzqw, serviceConnection);
            this.zzaaQ.remove(serviceConnection);
        }
        
        public void zzcc(final String s) {
            this.zzaaR = zzl.this.zzaaM.zza(zzl.this.zzqw, s, this.zzaaS.zznV(), (ServiceConnection)this.zzaaP, 129);
            if (this.zzaaR) {
                this.mState = 3;
                return;
            }
            try {
                zzl.this.zzaaM.zza(zzl.this.zzqw, (ServiceConnection)this.zzaaP);
            }
            catch (IllegalArgumentException ex) {}
        }
        
        public void zzcd(final String s) {
            zzl.this.zzaaM.zza(zzl.this.zzqw, (ServiceConnection)this.zzaaP);
            this.zzaaR = false;
            this.mState = 2;
        }
        
        public boolean zznW() {
            return this.zzaaQ.isEmpty();
        }
        
        public class zza implements ServiceConnection
        {
            public void onServiceConnected(final ComponentName componentName, final IBinder binder) {
                synchronized (zzl.this.zzaaL) {
                    zzb.this.zzZQ = binder;
                    zzb.this.zzaaO = componentName;
                    final Iterator<ServiceConnection> iterator = zzb.this.zzaaQ.iterator();
                    while (iterator.hasNext()) {
                        iterator.next().onServiceConnected(componentName, binder);
                    }
                }
                zzb.this.mState = 1;
            }
            // monitorexit(hashMap)
            
            public void onServiceDisconnected(final ComponentName componentName) {
                synchronized (zzl.this.zzaaL) {
                    zzb.this.zzZQ = null;
                    zzb.this.zzaaO = componentName;
                    final Iterator<ServiceConnection> iterator = zzb.this.zzaaQ.iterator();
                    while (iterator.hasNext()) {
                        iterator.next().onServiceDisconnected(componentName);
                    }
                }
                zzb.this.mState = 2;
            }
            // monitorexit(hashMap)
        }
    }
}
