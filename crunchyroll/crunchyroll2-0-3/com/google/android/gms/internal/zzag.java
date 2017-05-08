// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import java.io.IOException;

public interface zzag
{
    public static final class zza extends zzrh<zza>
    {
        private static volatile zza[] zziQ;
        public int type;
        public String zziR;
        public zza[] zziS;
        public zza[] zziT;
        public zza[] zziU;
        public String zziV;
        public String zziW;
        public long zziX;
        public boolean zziY;
        public zza[] zziZ;
        public int[] zzja;
        public boolean zzjb;
        
        public zza() {
            this.zzR();
        }
        
        public static zza[] zzQ() {
            Label_0027: {
                if (zza.zziQ != null) {
                    break Label_0027;
                }
                synchronized (zzrl.zzaWe) {
                    if (zza.zziQ == null) {
                        zza.zziQ = new zza[0];
                    }
                    return zza.zziQ;
                }
            }
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
                if (o instanceof zza) {
                    final zza zza = (zza)o;
                    b2 = b;
                    if (this.type == zza.type) {
                        if (this.zziR == null) {
                            b2 = b;
                            if (zza.zziR != null) {
                                return b2;
                            }
                        }
                        else if (!this.zziR.equals(zza.zziR)) {
                            return false;
                        }
                        b2 = b;
                        if (zzrl.equals(this.zziS, zza.zziS)) {
                            b2 = b;
                            if (zzrl.equals(this.zziT, zza.zziT)) {
                                b2 = b;
                                if (zzrl.equals(this.zziU, zza.zziU)) {
                                    if (this.zziV == null) {
                                        b2 = b;
                                        if (zza.zziV != null) {
                                            return b2;
                                        }
                                    }
                                    else if (!this.zziV.equals(zza.zziV)) {
                                        return false;
                                    }
                                    if (this.zziW == null) {
                                        b2 = b;
                                        if (zza.zziW != null) {
                                            return b2;
                                        }
                                    }
                                    else if (!this.zziW.equals(zza.zziW)) {
                                        return false;
                                    }
                                    b2 = b;
                                    if (this.zziX == zza.zziX) {
                                        b2 = b;
                                        if (this.zziY == zza.zziY) {
                                            b2 = b;
                                            if (zzrl.equals(this.zziZ, zza.zziZ)) {
                                                b2 = b;
                                                if (zzrl.equals(this.zzja, zza.zzja)) {
                                                    b2 = b;
                                                    if (this.zzjb == zza.zzjb) {
                                                        return this.zza(zza);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return b2;
        }
        
        @Override
        public int hashCode() {
            int n = 1231;
            int hashCode = 0;
            final int type = this.type;
            int hashCode2;
            if (this.zziR == null) {
                hashCode2 = 0;
            }
            else {
                hashCode2 = this.zziR.hashCode();
            }
            final int hashCode3 = zzrl.hashCode(this.zziS);
            final int hashCode4 = zzrl.hashCode(this.zziT);
            final int hashCode5 = zzrl.hashCode(this.zziU);
            int hashCode6;
            if (this.zziV == null) {
                hashCode6 = 0;
            }
            else {
                hashCode6 = this.zziV.hashCode();
            }
            if (this.zziW != null) {
                hashCode = this.zziW.hashCode();
            }
            final int n2 = (int)(this.zziX ^ this.zziX >>> 32);
            int n3;
            if (this.zziY) {
                n3 = 1231;
            }
            else {
                n3 = 1237;
            }
            final int hashCode7 = zzrl.hashCode(this.zziZ);
            final int hashCode8 = zzrl.hashCode(this.zzja);
            if (!this.zzjb) {
                n = 1237;
            }
            return ((((n3 + (((hashCode6 + ((((hashCode2 + (type + 527) * 31) * 31 + hashCode3) * 31 + hashCode4) * 31 + hashCode5) * 31) * 31 + hashCode) * 31 + n2) * 31) * 31 + hashCode7) * 31 + hashCode8) * 31 + n) * 31 + this.zzBI();
        }
        
        @Override
        protected int zzB() {
            final int n = 0;
            int n3;
            final int n2 = n3 = super.zzB() + zzrg.zzA(1, this.type);
            if (!this.zziR.equals("")) {
                n3 = n2 + zzrg.zzk(2, this.zziR);
            }
            int n4 = n3;
            if (this.zziS != null) {
                n4 = n3;
                if (this.zziS.length > 0) {
                    int n5;
                    for (int i = 0; i < this.zziS.length; ++i, n3 = n5) {
                        final zza zza = this.zziS[i];
                        n5 = n3;
                        if (zza != null) {
                            n5 = n3 + zzrg.zzc(3, zza);
                        }
                    }
                    n4 = n3;
                }
            }
            int n6 = n4;
            if (this.zziT != null) {
                n6 = n4;
                if (this.zziT.length > 0) {
                    n6 = n4;
                    int n7;
                    for (int j = 0; j < this.zziT.length; ++j, n6 = n7) {
                        final zza zza2 = this.zziT[j];
                        n7 = n6;
                        if (zza2 != null) {
                            n7 = n6 + zzrg.zzc(4, zza2);
                        }
                    }
                }
            }
            int n8 = n6;
            if (this.zziU != null) {
                n8 = n6;
                if (this.zziU.length > 0) {
                    int n9;
                    for (int k = 0; k < this.zziU.length; ++k, n6 = n9) {
                        final zza zza3 = this.zziU[k];
                        n9 = n6;
                        if (zza3 != null) {
                            n9 = n6 + zzrg.zzc(5, zza3);
                        }
                    }
                    n8 = n6;
                }
            }
            int n10 = n8;
            if (!this.zziV.equals("")) {
                n10 = n8 + zzrg.zzk(6, this.zziV);
            }
            int n11 = n10;
            if (!this.zziW.equals("")) {
                n11 = n10 + zzrg.zzk(7, this.zziW);
            }
            int n12 = n11;
            if (this.zziX != 0L) {
                n12 = n11 + zzrg.zzd(8, this.zziX);
            }
            int n13 = n12;
            if (this.zzjb) {
                n13 = n12 + zzrg.zzc(9, this.zzjb);
            }
            int n14 = n13;
            if (this.zzja != null) {
                n14 = n13;
                if (this.zzja.length > 0) {
                    int l = 0;
                    int n15 = 0;
                    while (l < this.zzja.length) {
                        n15 += zzrg.zzkJ(this.zzja[l]);
                        ++l;
                    }
                    n14 = n13 + n15 + this.zzja.length * 1;
                }
            }
            int n16 = n14;
            if (this.zziZ != null) {
                n16 = n14;
                if (this.zziZ.length > 0) {
                    int n17 = n;
                    while (true) {
                        n16 = n14;
                        if (n17 >= this.zziZ.length) {
                            break;
                        }
                        final zza zza4 = this.zziZ[n17];
                        int n18 = n14;
                        if (zza4 != null) {
                            n18 = n14 + zzrg.zzc(11, zza4);
                        }
                        ++n17;
                        n14 = n18;
                    }
                }
            }
            int n19 = n16;
            if (this.zziY) {
                n19 = n16 + zzrg.zzc(12, this.zziY);
            }
            return n19;
        }
        
        public zza zzR() {
            this.type = 1;
            this.zziR = "";
            this.zziS = zzQ();
            this.zziT = zzQ();
            this.zziU = zzQ();
            this.zziV = "";
            this.zziW = "";
            this.zziX = 0L;
            this.zziY = false;
            this.zziZ = zzQ();
            this.zzja = zzrq.zzaWh;
            this.zzjb = false;
            this.zzaVU = null;
            this.zzaWf = -1;
            return this;
        }
        
        @Override
        public void zza(final zzrg zzrg) throws IOException {
            final int n = 0;
            zzrg.zzy(1, this.type);
            if (!this.zziR.equals("")) {
                zzrg.zzb(2, this.zziR);
            }
            if (this.zziS != null && this.zziS.length > 0) {
                for (int i = 0; i < this.zziS.length; ++i) {
                    final zza zza = this.zziS[i];
                    if (zza != null) {
                        zzrg.zza(3, zza);
                    }
                }
            }
            if (this.zziT != null && this.zziT.length > 0) {
                for (int j = 0; j < this.zziT.length; ++j) {
                    final zza zza2 = this.zziT[j];
                    if (zza2 != null) {
                        zzrg.zza(4, zza2);
                    }
                }
            }
            if (this.zziU != null && this.zziU.length > 0) {
                for (int k = 0; k < this.zziU.length; ++k) {
                    final zza zza3 = this.zziU[k];
                    if (zza3 != null) {
                        zzrg.zza(5, zza3);
                    }
                }
            }
            if (!this.zziV.equals("")) {
                zzrg.zzb(6, this.zziV);
            }
            if (!this.zziW.equals("")) {
                zzrg.zzb(7, this.zziW);
            }
            if (this.zziX != 0L) {
                zzrg.zzb(8, this.zziX);
            }
            if (this.zzjb) {
                zzrg.zzb(9, this.zzjb);
            }
            if (this.zzja != null && this.zzja.length > 0) {
                for (int l = 0; l < this.zzja.length; ++l) {
                    zzrg.zzy(10, this.zzja[l]);
                }
            }
            if (this.zziZ != null && this.zziZ.length > 0) {
                for (int n2 = n; n2 < this.zziZ.length; ++n2) {
                    final zza zza4 = this.zziZ[n2];
                    if (zza4 != null) {
                        zzrg.zza(11, zza4);
                    }
                }
            }
            if (this.zziY) {
                zzrg.zzb(12, this.zziY);
            }
            super.zza(zzrg);
        }
    }
}
