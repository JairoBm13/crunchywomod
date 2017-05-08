// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import java.io.IOException;

public abstract class zzrh<M extends zzrh<M>> extends zzrn
{
    protected zzrj zzaVU;
    
    @Override
    protected int zzB() {
        int n = 0;
        int n3;
        if (this.zzaVU != null) {
            int n2 = 0;
            while (true) {
                n3 = n2;
                if (n >= this.zzaVU.size()) {
                    break;
                }
                n2 += this.zzaVU.zzkS(n).zzB();
                ++n;
            }
        }
        else {
            n3 = 0;
        }
        return n3;
    }
    
    protected final int zzBI() {
        if (this.zzaVU == null || this.zzaVU.isEmpty()) {
            return 0;
        }
        return this.zzaVU.hashCode();
    }
    
    public M zzBJ() throws CloneNotSupportedException {
        final zzrh zzrh = (zzrh)super.zzBK();
        zzrl.zza(this, zzrh);
        return (M)zzrh;
    }
    
    @Override
    public void zza(final zzrg zzrg) throws IOException {
        if (this.zzaVU != null) {
            for (int i = 0; i < this.zzaVU.size(); ++i) {
                this.zzaVU.zzkS(i).zza(zzrg);
            }
        }
    }
    
    protected final boolean zza(final M m) {
        if (this.zzaVU == null || this.zzaVU.isEmpty()) {
            return m.zzaVU == null || m.zzaVU.isEmpty();
        }
        return this.zzaVU.equals(m.zzaVU);
    }
}
