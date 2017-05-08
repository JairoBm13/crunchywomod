// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.cast.internal;

import android.os.SystemClock;

public final class zzp
{
    private static final zzl zzQW;
    public static final Object zzVr;
    private long zzTM;
    private long zzVo;
    private long zzVp;
    private zzo zzVq;
    
    static {
        zzQW = new zzl("RequestTracker");
        zzVr = new Object();
    }
    
    public zzp(final long zzVo) {
        this.zzVo = zzVo;
        this.zzTM = -1L;
        this.zzVp = 0L;
    }
    
    private void zzmd() {
        this.zzTM = -1L;
        this.zzVq = null;
        this.zzVp = 0L;
    }
    
    public void clear() {
        synchronized (zzp.zzVr) {
            if (this.zzTM != -1L) {
                this.zzmd();
            }
        }
    }
    
    public boolean zzB(final long n) {
        while (true) {
            synchronized (zzp.zzVr) {
                if (this.zzTM != -1L && this.zzTM == n) {
                    return true;
                }
            }
            return false;
        }
    }
    
    public void zza(final long zzTM, final zzo zzVq) {
        synchronized (zzp.zzVr) {
            final zzo zzVq2 = this.zzVq;
            final long zzTM2 = this.zzTM;
            this.zzTM = zzTM;
            this.zzVq = zzVq;
            this.zzVp = SystemClock.elapsedRealtime();
            // monitorexit(zzp.zzVr)
            if (zzVq2 != null) {
                zzVq2.zzy(zzTM2);
            }
        }
    }
    
    public boolean zzc(final long n, final int n2) {
        return this.zzc(n, n2, null);
    }
    
    public boolean zzc(final long n, final int n2, final Object o) {
        while (true) {
            boolean b = true;
            zzo zzVq = null;
            while (true) {
                synchronized (zzp.zzVr) {
                    if (this.zzTM != -1L && this.zzTM == n) {
                        zzp.zzQW.zzb("request %d completed", this.zzTM);
                        zzVq = this.zzVq;
                        this.zzmd();
                        // monitorexit(zzp.zzVr)
                        if (zzVq != null) {
                            zzVq.zza(n, n2, o);
                        }
                        return b;
                    }
                }
                b = false;
                continue;
            }
        }
    }
    
    public boolean zzd(long zzTM, final int n) {
        while (true) {
            boolean b = true;
            final long n2 = 0L;
            while (true) {
                synchronized (zzp.zzVr) {
                    if (this.zzTM != -1L && zzTM - this.zzVp >= this.zzVo) {
                        zzp.zzQW.zzb("request %d timed out", this.zzTM);
                        zzTM = this.zzTM;
                        final zzo zzVq = this.zzVq;
                        this.zzmd();
                        // monitorexit(zzp.zzVr)
                        if (zzVq != null) {
                            zzVq.zza(zzTM, n, null);
                        }
                        return b;
                    }
                }
                b = false;
                final zzo zzVq = null;
                zzTM = n2;
                continue;
            }
        }
    }
    
    public boolean zzme() {
        while (true) {
            synchronized (zzp.zzVr) {
                if (this.zzTM != -1L) {
                    return true;
                }
            }
            return false;
        }
    }
}
