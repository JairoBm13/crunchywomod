// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import android.os.Process;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor;
import android.util.DisplayMetrics;
import com.google.android.gms.analytics.internal.zzam;
import java.util.Locale;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager$NameNotFoundException;
import android.util.Log;
import android.text.TextUtils;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.Iterator;
import android.net.Uri;
import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArrayList;
import com.google.android.gms.common.internal.zzu;
import java.util.List;
import android.content.Context;

public final class zzns
{
    private static volatile zzns zzaEh;
    private final Context mContext;
    private volatile zznx zzKm;
    private final List<zznt> zzaEi;
    private final zznn zzaEj;
    private final zza zzaEk;
    private Thread.UncaughtExceptionHandler zzaEl;
    
    zzns(Context applicationContext) {
        applicationContext = applicationContext.getApplicationContext();
        zzu.zzu(applicationContext);
        this.mContext = applicationContext;
        this.zzaEk = new zza();
        this.zzaEi = new CopyOnWriteArrayList<zznt>();
        this.zzaEj = new zznn();
    }
    
    public static zzns zzaB(final Context context) {
        zzu.zzu(context);
        Label_0034: {
            if (zzns.zzaEh != null) {
                break Label_0034;
            }
            synchronized (zzns.class) {
                if (zzns.zzaEh == null) {
                    zzns.zzaEh = new zzns(context);
                }
                return zzns.zzaEh;
            }
        }
    }
    
    private void zzb(final zzno zzno) {
        zzu.zzbZ("deliver should be called from worker thread");
        zzu.zzb(zzno.zzvU(), "Measurement must be submitted");
        final List<zznu> zzvR = zzno.zzvR();
        if (!zzvR.isEmpty()) {
            final HashSet<Uri> set = new HashSet<Uri>();
            for (final zznu zznu : zzvR) {
                final Uri zzhe = zznu.zzhe();
                if (!set.contains(zzhe)) {
                    set.add(zzhe);
                    zznu.zzb(zzno);
                }
            }
        }
    }
    
    public static void zzhO() {
        if (!(Thread.currentThread() instanceof zzc)) {
            throw new IllegalStateException("Call expected from worker thread");
        }
    }
    
    public Context getContext() {
        return this.mContext;
    }
    
    public void zza(final Thread.UncaughtExceptionHandler zzaEl) {
        this.zzaEl = zzaEl;
    }
    
    public <V> Future<V> zzb(final Callable<V> callable) {
        zzu.zzu(callable);
        if (Thread.currentThread() instanceof zzc) {
            final FutureTask<V> futureTask = new FutureTask<V>(callable);
            futureTask.run();
            return futureTask;
        }
        return this.zzaEk.submit(callable);
    }
    
    void zze(zzno zzvP) {
        if (zzvP.zzvY()) {
            throw new IllegalStateException("Measurement prototype can't be submitted");
        }
        if (zzvP.zzvU()) {
            throw new IllegalStateException("Measurement can only be submitted once");
        }
        zzvP = zzvP.zzvP();
        zzvP.zzvV();
        this.zzaEk.execute(new Runnable() {
            @Override
            public void run() {
                zzvP.zzvW().zza(zzvP);
                final Iterator<zznt> iterator = zzns.this.zzaEi.iterator();
                while (iterator.hasNext()) {
                    iterator.next().zza(zzvP);
                }
                zzns.this.zzb(zzvP);
            }
        });
    }
    
    public void zze(final Runnable runnable) {
        zzu.zzu(runnable);
        this.zzaEk.submit(runnable);
    }
    
    public zznx zzwc() {
        Label_0156: {
            if (this.zzKm != null) {
                break Label_0156;
            }
            synchronized (this) {
                Label_0154: {
                    if (this.zzKm != null) {
                        break Label_0154;
                    }
                    final zznx zzKm = new zznx();
                    final PackageManager packageManager = this.mContext.getPackageManager();
                    final String packageName = this.mContext.getPackageName();
                    zzKm.setAppId(packageName);
                    zzKm.setAppInstallerId(packageManager.getInstallerPackageName(packageName));
                    final String s = null;
                    String s2 = packageName;
                    try {
                        final PackageInfo packageInfo = packageManager.getPackageInfo(this.mContext.getPackageName(), 0);
                        String versionName = s;
                        String string = packageName;
                        if (packageInfo != null) {
                            s2 = packageName;
                            final CharSequence applicationLabel = packageManager.getApplicationLabel(packageInfo.applicationInfo);
                            string = packageName;
                            s2 = packageName;
                            if (!TextUtils.isEmpty(applicationLabel)) {
                                s2 = packageName;
                                string = applicationLabel.toString();
                            }
                            s2 = string;
                            versionName = packageInfo.versionName;
                        }
                        zzKm.setAppName(string);
                        zzKm.setAppVersion(versionName);
                        this.zzKm = zzKm;
                        // monitorexit(this)
                        return this.zzKm;
                    }
                    catch (PackageManager$NameNotFoundException ex) {
                        Log.e("GAv4", "Error retrieving package info: appName set to " + s2);
                        final String versionName = s;
                        final String string = s2;
                    }
                }
            }
        }
    }
    
    public zznz zzwd() {
        final DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
        final zznz zznz = new zznz();
        zznz.setLanguage(zzam.zza(Locale.getDefault()));
        zznz.zzhG(displayMetrics.widthPixels);
        zznz.zzhH(displayMetrics.heightPixels);
        return zznz;
    }
    
    private class zza extends ThreadPoolExecutor
    {
        public zza() {
            super(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
            this.setThreadFactory(new zzb());
        }
        
        @Override
        protected <T> RunnableFuture<T> newTaskFor(final Runnable runnable, final T t) {
            return new FutureTask<T>(runnable, t) {
                @Override
                protected void setException(final Throwable exception) {
                    final Thread.UncaughtExceptionHandler zzb = zzns.this.zzaEl;
                    if (zzb != null) {
                        zzb.uncaughtException(Thread.currentThread(), exception);
                    }
                    else if (Log.isLoggable("GAv4", 6)) {
                        Log.e("GAv4", "MeasurementExecutor: job failed with " + exception);
                    }
                    super.setException(exception);
                }
            };
        }
    }
    
    private static class zzb implements ThreadFactory
    {
        private static final AtomicInteger zzaEp;
        
        static {
            zzaEp = new AtomicInteger();
        }
        
        @Override
        public Thread newThread(final Runnable runnable) {
            return new zzc(runnable, "measurement-" + zzb.zzaEp.incrementAndGet());
        }
    }
    
    private static class zzc extends Thread
    {
        zzc(final Runnable runnable, final String s) {
            super(runnable, s);
        }
        
        @Override
        public void run() {
            Process.setThreadPriority(10);
            super.run();
        }
    }
}
