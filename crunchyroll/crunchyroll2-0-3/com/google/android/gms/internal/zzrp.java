// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import java.io.IOException;
import java.util.Arrays;

final class zzrp
{
    final int tag;
    final byte[] zzaWg;
    
    @Override
    public boolean equals(final Object o) {
        if (o != this) {
            if (!(o instanceof zzrp)) {
                return false;
            }
            final zzrp zzrp = (zzrp)o;
            if (this.tag != zzrp.tag || !Arrays.equals(this.zzaWg, zzrp.zzaWg)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        return (this.tag + 527) * 31 + Arrays.hashCode(this.zzaWg);
    }
    
    int zzB() {
        return 0 + zzrg.zzkO(this.tag) + this.zzaWg.length;
    }
    
    void zza(final zzrg zzrg) throws IOException {
        zzrg.zzkN(this.tag);
        zzrg.zzD(this.zzaWg);
    }
}
