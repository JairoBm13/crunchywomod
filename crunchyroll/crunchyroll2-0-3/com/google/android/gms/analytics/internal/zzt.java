// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics.internal;

import android.os.Looper;
import com.google.android.gms.common.internal.zzu;
import android.os.Handler;

abstract class zzt
{
    private static volatile Handler zzKS;
    private final zzf zzJy;
    private volatile long zzKT;
    private boolean zzKU;
    private final Runnable zzx;
    
    zzt(final zzf zzJy) {
        zzu.zzu(zzJy);
        this.zzJy = zzJy;
        this.zzx = new Runnable() {
            @Override
            public void run() {
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    zzt.this.zzJy.zzhS().zze(this);
                }
                else {
                    final boolean zzbp = zzt.this.zzbp();
                    zzt.this.zzKT = 0L;
                    if (zzbp && !zzt.this.zzKU) {
                        zzt.this.run();
                    }
                }
            }
        };
    }
    
    private Handler getHandler() {
        if (zzt.zzKS != null) {
            return zzt.zzKS;
        }
        synchronized (zzt.class) {
            if (zzt.zzKS == null) {
                zzt.zzKS = new Handler(this.zzJy.getContext().getMainLooper());
            }
            return zzt.zzKS;
        }
    }
    
    public void cancel() {
        this.zzKT = 0L;
        this.getHandler().removeCallbacks(this.zzx);
    }
    
    public abstract void run();
    
    public boolean zzbp() {
        return this.zzKT != 0L;
    }
    
    public long zzjD() {
        if (this.zzKT == 0L) {
            return 0L;
        }
        return Math.abs(this.zzJy.zzhP().currentTimeMillis() - this.zzKT);
    }
    
    public void zzt(final long n) {
        this.cancel();
        if (n >= 0L) {
            this.zzKT = this.zzJy.zzhP().currentTimeMillis();
            if (!this.getHandler().postDelayed(this.zzx, n)) {
                this.zzJy.zzhQ().zze("Failed to schedule delayed post. time", n);
            }
        }
    }
    
    public void zzu(long n) {
        final long n2 = 0L;
        if (this.zzbp()) {
            if (n < 0L) {
                this.cancel();
                return;
            }
            n -= Math.abs(this.zzJy.zzhP().currentTimeMillis() - this.zzKT);
            if (n < 0L) {
                n = n2;
            }
            this.getHandler().removeCallbacks(this.zzx);
            if (!this.getHandler().postDelayed(this.zzx, n)) {
                this.zzJy.zzhQ().zze("Failed to adjust delayed post. time", n);
            }
        }
    }
}
