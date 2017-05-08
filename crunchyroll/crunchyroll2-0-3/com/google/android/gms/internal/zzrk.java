// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import java.util.Collection;
import java.util.Iterator;
import java.util.Arrays;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class zzrk implements Cloneable
{
    private zzri<?, ?> zzaWb;
    private Object zzaWc;
    private List<zzrp> zzaWd;
    
    zzrk() {
        this.zzaWd = new ArrayList<zzrp>();
    }
    
    private byte[] toByteArray() throws IOException {
        final byte[] array = new byte[this.zzB()];
        this.zza(zzrg.zzA(array));
        return array;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = false;
        boolean b2;
        if (o == this) {
            b2 = true;
        }
        else {
            b2 = b;
            if (o instanceof zzrk) {
                final zzrk zzrk = (zzrk)o;
                if (this.zzaWc != null && zzrk.zzaWc != null) {
                    b2 = b;
                    if (this.zzaWb == zzrk.zzaWb) {
                        if (!this.zzaWb.zzaVV.isArray()) {
                            return this.zzaWc.equals(zzrk.zzaWc);
                        }
                        if (this.zzaWc instanceof byte[]) {
                            return Arrays.equals((byte[])this.zzaWc, (byte[])zzrk.zzaWc);
                        }
                        if (this.zzaWc instanceof int[]) {
                            return Arrays.equals((int[])this.zzaWc, (int[])zzrk.zzaWc);
                        }
                        if (this.zzaWc instanceof long[]) {
                            return Arrays.equals((long[])this.zzaWc, (long[])zzrk.zzaWc);
                        }
                        if (this.zzaWc instanceof float[]) {
                            return Arrays.equals((float[])this.zzaWc, (float[])zzrk.zzaWc);
                        }
                        if (this.zzaWc instanceof double[]) {
                            return Arrays.equals((double[])this.zzaWc, (double[])zzrk.zzaWc);
                        }
                        if (this.zzaWc instanceof boolean[]) {
                            return Arrays.equals((boolean[])this.zzaWc, (boolean[])zzrk.zzaWc);
                        }
                        return Arrays.deepEquals((Object[])this.zzaWc, (Object[])zzrk.zzaWc);
                    }
                }
                else {
                    if (this.zzaWd != null && zzrk.zzaWd != null) {
                        return this.zzaWd.equals(zzrk.zzaWd);
                    }
                    try {
                        return Arrays.equals(this.toByteArray(), zzrk.toByteArray());
                    }
                    catch (IOException ex) {
                        throw new IllegalStateException(ex);
                    }
                }
            }
        }
        return b2;
    }
    
    @Override
    public int hashCode() {
        try {
            return Arrays.hashCode(this.toByteArray()) + 527;
        }
        catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }
    
    int zzB() {
        int zzQ;
        if (this.zzaWc != null) {
            zzQ = this.zzaWb.zzQ(this.zzaWc);
        }
        else {
            final Iterator<zzrp> iterator = this.zzaWd.iterator();
            int n = 0;
            while (true) {
                zzQ = n;
                if (!iterator.hasNext()) {
                    break;
                }
                n += iterator.next().zzB();
            }
        }
        return zzQ;
    }
    
    public final zzrk zzBM() {
        int i = 0;
        final zzrk zzrk = new zzrk();
        try {
            zzrk.zzaWb = this.zzaWb;
            if (this.zzaWd == null) {
                zzrk.zzaWd = null;
            }
            else {
                zzrk.zzaWd.addAll(this.zzaWd);
            }
            if (this.zzaWc == null) {
                return zzrk;
            }
        }
        catch (CloneNotSupportedException ex) {
            throw new AssertionError((Object)ex);
        }
        if (this.zzaWc instanceof zzrn) {
            zzrk.zzaWc = ((zzrn)this.zzaWc).zzBK();
            return zzrk;
        }
        if (this.zzaWc instanceof byte[]) {
            zzrk.zzaWc = ((byte[])this.zzaWc).clone();
            return zzrk;
        }
        if (this.zzaWc instanceof byte[][]) {
            final byte[][] array = (byte[][])this.zzaWc;
            final byte[][] zzaWc = new byte[array.length][];
            zzrk.zzaWc = zzaWc;
            for (int j = 0; j < array.length; ++j) {
                zzaWc[j] = array[j].clone();
            }
        }
        else {
            if (this.zzaWc instanceof boolean[]) {
                zzrk.zzaWc = ((boolean[])this.zzaWc).clone();
                return zzrk;
            }
            if (this.zzaWc instanceof int[]) {
                zzrk.zzaWc = ((int[])this.zzaWc).clone();
                return zzrk;
            }
            if (this.zzaWc instanceof long[]) {
                zzrk.zzaWc = ((long[])this.zzaWc).clone();
                return zzrk;
            }
            if (this.zzaWc instanceof float[]) {
                zzrk.zzaWc = ((float[])this.zzaWc).clone();
                return zzrk;
            }
            if (this.zzaWc instanceof double[]) {
                zzrk.zzaWc = ((double[])this.zzaWc).clone();
                return zzrk;
            }
            if (this.zzaWc instanceof zzrn[]) {
                final zzrn[] array2 = (zzrn[])this.zzaWc;
                final zzrn[] zzaWc2 = new zzrn[array2.length];
                zzrk.zzaWc = zzaWc2;
                while (i < array2.length) {
                    zzaWc2[i] = array2[i].zzBK();
                    ++i;
                }
            }
        }
        return zzrk;
    }
    
    void zza(final zzrg zzrg) throws IOException {
        if (this.zzaWc != null) {
            this.zzaWb.zza(this.zzaWc, zzrg);
        }
        else {
            final Iterator<zzrp> iterator = this.zzaWd.iterator();
            while (iterator.hasNext()) {
                iterator.next().zza(zzrg);
            }
        }
    }
}
