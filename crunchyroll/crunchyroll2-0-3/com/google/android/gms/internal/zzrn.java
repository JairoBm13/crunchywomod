// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import java.io.IOException;

public abstract class zzrn
{
    protected volatile int zzaWf;
    
    public zzrn() {
        this.zzaWf = -1;
    }
    
    public static final void zza(final zzrn zzrn, final byte[] array, final int n, final int n2) {
        try {
            final zzrg zzb = zzrg.zzb(array, n, n2);
            zzrn.zza(zzb);
            zzb.zzBH();
        }
        catch (IOException ex) {
            throw new RuntimeException("Serializing to a byte array threw an IOException (should never happen).", ex);
        }
    }
    
    public static final byte[] zzf(final zzrn zzrn) {
        final byte[] array = new byte[zzrn.zzBV()];
        zza(zzrn, array, 0, array.length);
        return array;
    }
    
    @Override
    public String toString() {
        return zzro.zzg(this);
    }
    
    protected int zzB() {
        return 0;
    }
    
    public zzrn zzBK() throws CloneNotSupportedException {
        return (zzrn)super.clone();
    }
    
    public int zzBU() {
        if (this.zzaWf < 0) {
            this.zzBV();
        }
        return this.zzaWf;
    }
    
    public int zzBV() {
        return this.zzaWf = this.zzB();
    }
    
    public void zza(final zzrg zzrg) throws IOException {
    }
}
