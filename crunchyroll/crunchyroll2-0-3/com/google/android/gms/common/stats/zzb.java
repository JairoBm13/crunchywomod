// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.stats;

import android.os.Process;
import java.util.Iterator;
import android.content.pm.ResolveInfo;
import android.util.Log;
import android.content.pm.ServiceInfo;
import android.os.Parcelable;
import android.os.SystemClock;
import android.os.Debug;
import com.google.android.gms.internal.zzll;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.Context;
import com.google.android.gms.internal.zzla;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import android.content.ComponentName;

public class zzb
{
    private static final Object zzaaJ;
    private static zzb zzack;
    private static final ComponentName zzacp;
    private final List<String> zzacl;
    private final List<String> zzacm;
    private final List<String> zzacn;
    private final List<String> zzaco;
    private zze zzacq;
    
    static {
        zzaaJ = new Object();
        zzacp = new ComponentName("com.google.android.gms", "com.google.android.gms.common.stats.GmsCoreStatsService");
    }
    
    private zzb() {
        if (this.getLogLevel() == zzd.zzacz) {
            this.zzacl = (List<String>)Collections.EMPTY_LIST;
            this.zzacm = (List<String>)Collections.EMPTY_LIST;
            this.zzacn = (List<String>)Collections.EMPTY_LIST;
            this.zzaco = (List<String>)Collections.EMPTY_LIST;
            return;
        }
        final String s = zzc.zza.zzacu.get();
        List<String> zzacl;
        if (s == null) {
            zzacl = (List<String>)Collections.EMPTY_LIST;
        }
        else {
            zzacl = Arrays.asList(s.split(","));
        }
        this.zzacl = zzacl;
        final String s2 = zzc.zza.zzacv.get();
        List<String> zzacm;
        if (s2 == null) {
            zzacm = (List<String>)Collections.EMPTY_LIST;
        }
        else {
            zzacm = Arrays.asList(s2.split(","));
        }
        this.zzacm = zzacm;
        final String s3 = zzc.zza.zzacw.get();
        List<String> zzacn;
        if (s3 == null) {
            zzacn = (List<String>)Collections.EMPTY_LIST;
        }
        else {
            zzacn = Arrays.asList(s3.split(","));
        }
        this.zzacn = zzacn;
        final String s4 = zzc.zza.zzacx.get();
        List<String> zzaco;
        if (s4 == null) {
            zzaco = (List<String>)Collections.EMPTY_LIST;
        }
        else {
            zzaco = Arrays.asList(s4.split(","));
        }
        this.zzaco = zzaco;
        this.zzacq = new zze(1024, zzc.zza.zzacy.get());
    }
    
    private int getLogLevel() {
        try {
            if (zzla.zziW()) {
                return zzc.zza.zzact.get();
            }
            return zzd.zzacz;
        }
        catch (SecurityException ex) {
            return zzd.zzacz;
        }
    }
    
    private void zza(final Context context, final ServiceConnection serviceConnection, final String s, final Intent intent, final int n) {
        if (com.google.android.gms.common.internal.zzd.zzZR) {
            final String zzb = this.zzb(serviceConnection);
            if (this.zza(context, s, intent, zzb, n)) {
                final long currentTimeMillis = System.currentTimeMillis();
                String zzl = null;
                if ((this.getLogLevel() & zzd.zzacD) != 0x0) {
                    zzl = zzll.zzl(3, 5);
                }
                long nativeHeapAllocatedSize = 0L;
                if ((this.getLogLevel() & zzd.zzacF) != 0x0) {
                    nativeHeapAllocatedSize = Debug.getNativeHeapAllocatedSize();
                }
                ConnectionEvent connectionEvent;
                if (n == 1 || n == 4) {
                    connectionEvent = new ConnectionEvent(currentTimeMillis, n, null, null, null, null, zzl, zzb, SystemClock.elapsedRealtime(), nativeHeapAllocatedSize);
                }
                else {
                    final ServiceInfo zzb2 = zzb(context, intent);
                    connectionEvent = new ConnectionEvent(currentTimeMillis, n, zzll.zzaj(context), s, zzb2.processName, zzb2.name, zzl, zzb, SystemClock.elapsedRealtime(), nativeHeapAllocatedSize);
                }
                context.startService(new Intent().setComponent(com.google.android.gms.common.stats.zzb.zzacp).putExtra("com.google.android.gms.common.stats.EXTRA_LOG_EVENT", (Parcelable)connectionEvent));
            }
        }
    }
    
    private boolean zza(final Context context, final Intent intent) {
        final ComponentName component = intent.getComponent();
        return component != null && (!com.google.android.gms.common.internal.zzd.zzZR || !"com.google.android.gms".equals(component.getPackageName())) && zzla.zzi(context, component.getPackageName());
    }
    
    private boolean zza(final Context context, final String s, final Intent intent, final String s2, final int n) {
        final int logLevel = this.getLogLevel();
        if (logLevel != zzd.zzacz && this.zzacq != null) {
            if (n == 4 || n == 1) {
                if (this.zzacq.zzcq(s2)) {
                    return true;
                }
            }
            else {
                final ServiceInfo zzb = zzb(context, intent);
                if (zzb == null) {
                    Log.w("ConnectionTracker", String.format("Client %s made an invalid request %s", s, intent.toUri(0)));
                    return false;
                }
                final String zzaj = zzll.zzaj(context);
                final String processName = zzb.processName;
                final String name = zzb.name;
                if (!this.zzacl.contains(zzaj) && !this.zzacm.contains(s) && !this.zzacn.contains(processName) && !this.zzaco.contains(name) && (!processName.equals(zzaj) || (logLevel & zzd.zzacE) == 0x0)) {
                    this.zzacq.zzcp(s2);
                    return true;
                }
            }
        }
        return false;
    }
    
    private static ServiceInfo zzb(final Context context, final Intent intent) {
        final List queryIntentServices = context.getPackageManager().queryIntentServices(intent, 128);
        if (queryIntentServices == null || queryIntentServices.size() == 0) {
            Log.w("ConnectionTracker", String.format("There are no handler of this intent: %s\n Stack trace: %s", intent.toUri(0), zzll.zzl(3, 20)));
            return null;
        }
        if (queryIntentServices.size() > 1) {
            Log.w("ConnectionTracker", String.format("Multiple handlers found for this intent: %s\n Stack trace: %s", intent.toUri(0), zzll.zzl(3, 20)));
            final Iterator<ResolveInfo> iterator = queryIntentServices.iterator();
            if (iterator.hasNext()) {
                Log.w("ConnectionTracker", iterator.next().serviceInfo.name);
                return null;
            }
        }
        return queryIntentServices.get(0).serviceInfo;
    }
    
    private String zzb(final ServiceConnection serviceConnection) {
        return String.valueOf(Process.myPid() << 32 | System.identityHashCode(serviceConnection));
    }
    
    public static zzb zzoO() {
        synchronized (zzb.zzaaJ) {
            if (zzb.zzack == null) {
                zzb.zzack = new zzb();
            }
            return zzb.zzack;
        }
    }
    
    public void zza(final Context context, final ServiceConnection serviceConnection) {
        this.zza(context, serviceConnection, null, null, 1);
        context.unbindService(serviceConnection);
    }
    
    public void zza(final Context context, final ServiceConnection serviceConnection, final String s, final Intent intent) {
        this.zza(context, serviceConnection, s, intent, 3);
    }
    
    public boolean zza(final Context context, final Intent intent, final ServiceConnection serviceConnection, final int n) {
        return this.zza(context, context.getClass().getName(), intent, serviceConnection, n);
    }
    
    public boolean zza(final Context context, final String s, final Intent intent, final ServiceConnection serviceConnection, final int n) {
        if (this.zza(context, intent)) {
            Log.w("ConnectionTracker", "Attempted to bind to a service in a STOPPED package.");
            return false;
        }
        this.zza(context, serviceConnection, s, intent, 2);
        return context.bindService(intent, serviceConnection, n);
    }
    
    public void zzb(final Context context, final ServiceConnection serviceConnection) {
        this.zza(context, serviceConnection, null, null, 4);
    }
}
