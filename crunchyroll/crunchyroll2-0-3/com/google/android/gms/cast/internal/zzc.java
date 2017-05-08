// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.cast.internal;

import android.os.SystemClock;
import android.os.Looper;
import android.os.Handler;

public abstract class zzc extends zzd
{
    protected final Handler mHandler;
    protected final long zzUe;
    protected final Runnable zzUf;
    protected boolean zzUg;
    
    public zzc(final String s, final String s2, final String s3, final long zzUe) {
        super(s, s2, s3);
        this.mHandler = new Handler(Looper.getMainLooper());
        this.zzUf = new zza();
        this.zzUe = zzUe;
        this.zzQ(false);
    }
    
    protected final void zzQ(final boolean zzUg) {
        if (this.zzUg != zzUg) {
            this.zzUg = zzUg;
            if (!zzUg) {
                this.mHandler.removeCallbacks(this.zzUf);
                return;
            }
            this.mHandler.postDelayed(this.zzUf, this.zzUe);
        }
    }
    
    @Override
    public void zzlJ() {
        this.zzQ(false);
    }
    
    protected abstract boolean zzz(final long p0);
    
    private class zza implements Runnable
    {
        @Override
        public void run() {
            zzc.this.zzUg = false;
            zzc.this.zzQ(zzc.this.zzz(SystemClock.elapsedRealtime()));
        }
    }
}
