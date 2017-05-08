// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import java.io.IOException;
import java.lang.reflect.Array;

public class zzri<M extends zzrh<M>, T>
{
    public final int tag;
    protected final int type;
    protected final Class<T> zzaVV;
    protected final boolean zzaVW;
    
    int zzQ(final Object o) {
        if (this.zzaVW) {
            return this.zzR(o);
        }
        return this.zzS(o);
    }
    
    protected int zzR(final Object o) {
        int n = 0;
        int n2;
        for (int length = Array.getLength(o), i = 0; i < length; ++i, n = n2) {
            n2 = n;
            if (Array.get(o, i) != null) {
                n2 = n + this.zzS(Array.get(o, i));
            }
        }
        return n;
    }
    
    protected int zzS(final Object o) {
        final int zzkV = zzrq.zzkV(this.tag);
        switch (this.type) {
            default: {
                throw new IllegalArgumentException("Unknown type " + this.type);
            }
            case 10: {
                return zzrg.zzb(zzkV, (zzrn)o);
            }
            case 11: {
                return zzrg.zzc(zzkV, (zzrn)o);
            }
        }
    }
    
    void zza(final Object o, final zzrg zzrg) throws IOException {
        if (this.zzaVW) {
            this.zzc(o, zzrg);
            return;
        }
        this.zzb(o, zzrg);
    }
    
    protected void zzb(final Object o, final zzrg zzrg) {
        while (true) {
            Label_0101: {
                Label_0076: {
                    while (true) {
                        Label_0110: {
                            try {
                                zzrg.zzkN(this.tag);
                                switch (this.type) {
                                    case 10: {
                                        break Label_0076;
                                    }
                                    case 11: {
                                        break Label_0101;
                                    }
                                    default: {
                                        break Label_0110;
                                    }
                                }
                                throw new IllegalArgumentException("Unknown type " + this.type);
                            }
                            catch (IOException ex) {
                                throw new IllegalStateException(ex);
                            }
                            break Label_0076;
                        }
                        continue;
                    }
                }
                final zzrn zzrn = (zzrn)o;
                final int zzkV = zzrq.zzkV(this.tag);
                zzrg.zzb(zzrn);
                zzrg.zzC(zzkV, 4);
                return;
            }
            zzrg.zzc((zzrn)o);
        }
    }
    
    protected void zzc(final Object o, final zzrg zzrg) {
        for (int length = Array.getLength(o), i = 0; i < length; ++i) {
            final Object value = Array.get(o, i);
            if (value != null) {
                this.zzb(value, zzrg);
            }
        }
    }
}
