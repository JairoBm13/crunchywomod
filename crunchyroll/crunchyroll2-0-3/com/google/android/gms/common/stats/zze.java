// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.stats;

import android.util.Log;
import android.os.SystemClock;
import android.support.v4.util.SimpleArrayMap;

public class zze
{
    private final long zzacG;
    private final int zzacH;
    private final SimpleArrayMap<String, Long> zzacI;
    
    public zze() {
        this.zzacG = 60000L;
        this.zzacH = 10;
        this.zzacI = new SimpleArrayMap<String, Long>(10);
    }
    
    public zze(final int zzacH, final long zzacG) {
        this.zzacG = zzacG;
        this.zzacH = zzacH;
        this.zzacI = new SimpleArrayMap<String, Long>();
    }
    
    private void zzc(final long n, final long n2) {
        for (int i = this.zzacI.size() - 1; i >= 0; --i) {
            if (n2 - this.zzacI.valueAt(i) > n) {
                this.zzacI.removeAt(i);
            }
        }
    }
    
    public Long zzcp(final String s) {
        final long elapsedRealtime = SystemClock.elapsedRealtime();
        long zzacG = this.zzacG;
        synchronized (this) {
            while (this.zzacI.size() >= this.zzacH) {
                this.zzc(zzacG, elapsedRealtime);
                zzacG /= 2L;
                Log.w("ConnectionTracker", "The max capacity " + this.zzacH + " is not enough. Current durationThreshold is: " + zzacG);
            }
        }
        final String s2;
        // monitorexit(this)
        return this.zzacI.put(s2, elapsedRealtime);
    }
    
    public boolean zzcq(final String s) {
        while (true) {
            synchronized (this) {
                if (this.zzacI.remove(s) != null) {
                    return true;
                }
            }
            return false;
        }
    }
}
