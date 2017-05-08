// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.tagmanager;

class zzde extends Number implements Comparable<zzde>
{
    private double zzaOq;
    private long zzaOr;
    private boolean zzaOs;
    
    private zzde(final long zzaOr) {
        this.zzaOr = zzaOr;
        this.zzaOs = true;
    }
    
    public static zzde zzT(final long n) {
        return new zzde(n);
    }
    
    @Override
    public byte byteValue() {
        return (byte)this.longValue();
    }
    
    @Override
    public double doubleValue() {
        if (this.zzzG()) {
            return this.zzaOr;
        }
        return this.zzaOq;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof zzde && this.zza((zzde)o) == 0;
    }
    
    @Override
    public float floatValue() {
        return (float)this.doubleValue();
    }
    
    @Override
    public int hashCode() {
        return new Long(this.longValue()).hashCode();
    }
    
    @Override
    public int intValue() {
        return this.zzzI();
    }
    
    @Override
    public long longValue() {
        return this.zzzH();
    }
    
    @Override
    public short shortValue() {
        return this.zzzJ();
    }
    
    @Override
    public String toString() {
        if (this.zzzG()) {
            return Long.toString(this.zzaOr);
        }
        return Double.toString(this.zzaOq);
    }
    
    public int zza(final zzde zzde) {
        if (this.zzzG() && zzde.zzzG()) {
            return new Long(this.zzaOr).compareTo(Long.valueOf(zzde.zzaOr));
        }
        return Double.compare(this.doubleValue(), zzde.doubleValue());
    }
    
    public boolean zzzF() {
        return !this.zzzG();
    }
    
    public boolean zzzG() {
        return this.zzaOs;
    }
    
    public long zzzH() {
        if (this.zzzG()) {
            return this.zzaOr;
        }
        return (long)this.zzaOq;
    }
    
    public int zzzI() {
        return (int)this.longValue();
    }
    
    public short zzzJ() {
        return (short)this.longValue();
    }
}
