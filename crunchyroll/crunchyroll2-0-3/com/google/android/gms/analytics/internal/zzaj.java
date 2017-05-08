// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics.internal;

import com.google.android.gms.common.internal.zzu;
import com.google.android.gms.internal.zzlb;

class zzaj
{
    private long zzMC;
    private final zzlb zzpw;
    
    public zzaj(final zzlb zzpw) {
        zzu.zzu(zzpw);
        this.zzpw = zzpw;
    }
    
    public zzaj(final zzlb zzpw, final long zzMC) {
        zzu.zzu(zzpw);
        this.zzpw = zzpw;
        this.zzMC = zzMC;
    }
    
    public void clear() {
        this.zzMC = 0L;
    }
    
    public void start() {
        this.zzMC = this.zzpw.elapsedRealtime();
    }
    
    public boolean zzv(final long n) {
        return this.zzMC == 0L || this.zzpw.elapsedRealtime() - this.zzMC > n;
    }
}
