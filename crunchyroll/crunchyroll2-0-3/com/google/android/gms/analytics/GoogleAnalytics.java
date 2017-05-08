// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics;

import com.google.android.gms.analytics.internal.zzad;
import com.google.android.gms.analytics.internal.zzae;
import com.google.android.gms.common.internal.zzu;
import com.google.android.gms.analytics.internal.zzan;
import java.util.Iterator;
import android.content.Context;
import java.util.HashSet;
import com.google.android.gms.analytics.internal.zzf;
import java.util.ArrayList;
import java.util.Set;
import java.util.List;

public final class GoogleAnalytics extends zza
{
    private static List<Runnable> zzIt;
    private boolean zzIu;
    private Set<Object> zzIv;
    private boolean zzIx;
    private volatile boolean zzIy;
    private boolean zzpb;
    
    static {
        GoogleAnalytics.zzIt = new ArrayList<Runnable>();
    }
    
    public GoogleAnalytics(final zzf zzf) {
        super(zzf);
        this.zzIv = new HashSet<Object>();
    }
    
    public static GoogleAnalytics getInstance(final Context context) {
        return zzf.zzV(context).zzie();
    }
    
    public static void zzhj() {
        Label_0054: {
            synchronized (GoogleAnalytics.class) {
                if (GoogleAnalytics.zzIt == null) {
                    break Label_0054;
                }
                final Iterator<Runnable> iterator = GoogleAnalytics.zzIt.iterator();
                while (iterator.hasNext()) {
                    iterator.next().run();
                }
            }
            GoogleAnalytics.zzIt = null;
        }
    }
    // monitorexit(GoogleAnalytics.class)
    
    private zzan zzhm() {
        return this.zzhb().zzhm();
    }
    
    public boolean getAppOptOut() {
        return this.zzIy;
    }
    
    public String getClientId() {
        zzu.zzbZ("getClientId can not be called from the main thread");
        return this.zzhb().zzih().zziP();
    }
    
    @Deprecated
    public Logger getLogger() {
        return zzae.getLogger();
    }
    
    public boolean isDryRunEnabled() {
        return this.zzIx;
    }
    
    public boolean isInitialized() {
        return this.zzpb && !this.zzIu;
    }
    
    public Tracker newTracker(final String s) {
        synchronized (this) {
            final Tracker tracker = new Tracker(this.zzhb(), s, null);
            tracker.zza();
            return tracker;
        }
    }
    
    public void setDryRun(final boolean zzIx) {
        this.zzIx = zzIx;
    }
    
    public void zza() {
        this.zzhi();
        this.zzpb = true;
    }
    
    void zzhi() {
        final zzan zzhm = this.zzhm();
        if (zzhm.zzjO()) {
            this.getLogger().setLogLevel(zzhm.getLogLevel());
        }
        if (zzhm.zzjS()) {
            this.setDryRun(zzhm.zzjT());
        }
        if (zzhm.zzjO()) {
            final Logger logger = zzae.getLogger();
            if (logger != null) {
                logger.setLogLevel(zzhm.getLogLevel());
            }
        }
    }
}
