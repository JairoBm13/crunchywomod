// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import com.google.android.gms.common.internal.zzt;
import android.graphics.drawable.Drawable;

public final class zzkj extends zzku<zza, Drawable>
{
    public static final class zza
    {
        public final int zzZL;
        public final int zzZM;
        
        public zza(final int zzZL, final int zzZM) {
            this.zzZL = zzZL;
            this.zzZM = zzZM;
        }
        
        @Override
        public boolean equals(final Object o) {
            final boolean b = true;
            boolean b2;
            if (!(o instanceof zza)) {
                b2 = false;
            }
            else {
                b2 = b;
                if (this != o) {
                    final zza zza = (zza)o;
                    if (zza.zzZL == this.zzZL) {
                        b2 = b;
                        if (zza.zzZM == this.zzZM) {
                            return b2;
                        }
                    }
                    return false;
                }
            }
            return b2;
        }
        
        @Override
        public int hashCode() {
            return zzt.hashCode(this.zzZL, this.zzZM);
        }
    }
}
