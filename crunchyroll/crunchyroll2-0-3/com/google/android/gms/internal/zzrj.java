// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

class zzrj implements Cloneable
{
    private static final zzrk zzaVX;
    private int mSize;
    private boolean zzaVY;
    private int[] zzaVZ;
    private zzrk[] zzaWa;
    
    static {
        zzaVX = new zzrk();
    }
    
    public zzrj() {
        this(10);
    }
    
    public zzrj(int idealIntArraySize) {
        this.zzaVY = false;
        idealIntArraySize = this.idealIntArraySize(idealIntArraySize);
        this.zzaVZ = new int[idealIntArraySize];
        this.zzaWa = new zzrk[idealIntArraySize];
        this.mSize = 0;
    }
    
    private void gc() {
        final int mSize = this.mSize;
        final int[] zzaVZ = this.zzaVZ;
        final zzrk[] zzaWa = this.zzaWa;
        int i = 0;
        int mSize2 = 0;
        while (i < mSize) {
            final zzrk zzrk = zzaWa[i];
            int n = mSize2;
            if (zzrk != zzrj.zzaVX) {
                if (i != mSize2) {
                    zzaVZ[mSize2] = zzaVZ[i];
                    zzaWa[mSize2] = zzrk;
                    zzaWa[i] = null;
                }
                n = mSize2 + 1;
            }
            ++i;
            mSize2 = n;
        }
        this.zzaVY = false;
        this.mSize = mSize2;
    }
    
    private int idealByteArraySize(final int n) {
        int n2 = 4;
        int n3;
        while (true) {
            n3 = n;
            if (n2 >= 32) {
                break;
            }
            if (n <= (1 << n2) - 12) {
                n3 = (1 << n2) - 12;
                break;
            }
            ++n2;
        }
        return n3;
    }
    
    private int idealIntArraySize(final int n) {
        return this.idealByteArraySize(n * 4) / 4;
    }
    
    private boolean zza(final int[] array, final int[] array2, final int n) {
        for (int i = 0; i < n; ++i) {
            if (array[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }
    
    private boolean zza(final zzrk[] array, final zzrk[] array2, final int n) {
        for (int i = 0; i < n; ++i) {
            if (!array[i].equals(array2[i])) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o != this) {
            if (!(o instanceof zzrj)) {
                return false;
            }
            final zzrj zzrj = (zzrj)o;
            if (this.size() != zzrj.size()) {
                return false;
            }
            if (!this.zza(this.zzaVZ, zzrj.zzaVZ, this.mSize) || !this.zza(this.zzaWa, zzrj.zzaWa, this.mSize)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        if (this.zzaVY) {
            this.gc();
        }
        int n = 17;
        for (int i = 0; i < this.mSize; ++i) {
            n = (n * 31 + this.zzaVZ[i]) * 31 + this.zzaWa[i].hashCode();
        }
        return n;
    }
    
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    public int size() {
        if (this.zzaVY) {
            this.gc();
        }
        return this.mSize;
    }
    
    public final zzrj zzBL() {
        int i = 0;
        final int size = this.size();
        final zzrj zzrj = new zzrj(size);
        System.arraycopy(this.zzaVZ, 0, zzrj.zzaVZ, 0, size);
        while (i < size) {
            if (this.zzaWa[i] != null) {
                zzrj.zzaWa[i] = this.zzaWa[i].zzBM();
            }
            ++i;
        }
        zzrj.mSize = size;
        return zzrj;
    }
    
    public zzrk zzkS(final int n) {
        if (this.zzaVY) {
            this.gc();
        }
        return this.zzaWa[n];
    }
}
