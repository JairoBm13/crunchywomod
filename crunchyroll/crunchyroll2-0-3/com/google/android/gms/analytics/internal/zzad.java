// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics.internal;

public class zzad
{
    private final long zzMf;
    private final int zzMg;
    private double zzMh;
    private long zzMi;
    private final Object zzMj;
    private final String zzuO;
    
    public zzad(final int zzMg, final long zzMf, final String zzuO) {
        this.zzMj = new Object();
        this.zzMg = zzMg;
        this.zzMh = this.zzMg;
        this.zzMf = zzMf;
        this.zzuO = zzuO;
    }
    
    public zzad(final String s) {
        this(60, 2000L, s);
    }
    
    public boolean zzkb() {
        synchronized (this.zzMj) {
            final long currentTimeMillis = System.currentTimeMillis();
            if (this.zzMh < this.zzMg) {
                final double n = (currentTimeMillis - this.zzMi) / this.zzMf;
                if (n > 0.0) {
                    this.zzMh = Math.min(this.zzMg, n + this.zzMh);
                }
            }
            this.zzMi = currentTimeMillis;
            if (this.zzMh >= 1.0) {
                --this.zzMh;
                return true;
            }
            zzae.zzaC("Excessive " + this.zzuO + " detected; call ignored.");
            return false;
        }
    }
}
