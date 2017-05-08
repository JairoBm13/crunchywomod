// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import java.io.IOException;

public interface zznj
{
    public static final class zza extends zzrh<zznj.zza>
    {
        public zznj.zza.zza[] zzawk;
        
        public zza() {
            this.zztU();
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
                if (o instanceof zznj.zza) {
                    final zznj.zza zza = (zznj.zza)o;
                    b2 = b;
                    if (zzrl.equals(this.zzawk, zza.zzawk)) {
                        return this.zza(zza);
                    }
                }
            }
            return b2;
        }
        
        @Override
        public int hashCode() {
            return (zzrl.hashCode(this.zzawk) + 527) * 31 + this.zzBI();
        }
        
        @Override
        protected int zzB() {
            int zzB;
            int n = zzB = super.zzB();
            if (this.zzawk != null) {
                zzB = n;
                if (this.zzawk.length > 0) {
                    int n2 = 0;
                    while (true) {
                        zzB = n;
                        if (n2 >= this.zzawk.length) {
                            break;
                        }
                        final zznj.zza.zza zza = this.zzawk[n2];
                        int n3 = n;
                        if (zza != null) {
                            n3 = n + zzrg.zzc(1, zza);
                        }
                        ++n2;
                        n = n3;
                    }
                }
            }
            return zzB;
        }
        
        @Override
        public void zza(final zzrg zzrg) throws IOException {
            if (this.zzawk != null && this.zzawk.length > 0) {
                for (int i = 0; i < this.zzawk.length; ++i) {
                    final zznj.zza.zza zza = this.zzawk[i];
                    if (zza != null) {
                        zzrg.zza(1, zza);
                    }
                }
            }
            super.zza(zzrg);
        }
        
        public zznj.zza zztU() {
            this.zzawk = zznj.zza.zza.zztV();
            this.zzaVU = null;
            this.zzaWf = -1;
            return this;
        }
        
        public static final class zza extends zzrh<zza>
        {
            private static volatile zza[] zzawl;
            public int viewId;
            public String zzawm;
            public String zzawn;
            
            public zza() {
                this.zztW();
            }
            
            public static zza[] zztV() {
                Label_0027: {
                    if (zza.zzawl != null) {
                        break Label_0027;
                    }
                    synchronized (zzrl.zzaWe) {
                        if (zza.zzawl == null) {
                            zza.zzawl = new zza[0];
                        }
                        return zza.zzawl;
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
                        if (this.zzawm == null) {
                            b2 = b;
                            if (zza.zzawm != null) {
                                return b2;
                            }
                        }
                        else if (!this.zzawm.equals(zza.zzawm)) {
                            return false;
                        }
                        if (this.zzawn == null) {
                            b2 = b;
                            if (zza.zzawn != null) {
                                return b2;
                            }
                        }
                        else if (!this.zzawn.equals(zza.zzawn)) {
                            return false;
                        }
                        b2 = b;
                        if (this.viewId == zza.viewId) {
                            return this.zza(zza);
                        }
                    }
                }
                return b2;
            }
            
            @Override
            public int hashCode() {
                int hashCode = 0;
                int hashCode2;
                if (this.zzawm == null) {
                    hashCode2 = 0;
                }
                else {
                    hashCode2 = this.zzawm.hashCode();
                }
                if (this.zzawn != null) {
                    hashCode = this.zzawn.hashCode();
                }
                return (((hashCode2 + 527) * 31 + hashCode) * 31 + this.viewId) * 31 + this.zzBI();
            }
            
            @Override
            protected int zzB() {
                int zzB;
                final int n = zzB = super.zzB();
                if (!this.zzawm.equals("")) {
                    zzB = n + zzrg.zzk(1, this.zzawm);
                }
                int n2 = zzB;
                if (!this.zzawn.equals("")) {
                    n2 = zzB + zzrg.zzk(2, this.zzawn);
                }
                int n3 = n2;
                if (this.viewId != 0) {
                    n3 = n2 + zzrg.zzA(3, this.viewId);
                }
                return n3;
            }
            
            @Override
            public void zza(final zzrg zzrg) throws IOException {
                if (!this.zzawm.equals("")) {
                    zzrg.zzb(1, this.zzawm);
                }
                if (!this.zzawn.equals("")) {
                    zzrg.zzb(2, this.zzawn);
                }
                if (this.viewId != 0) {
                    zzrg.zzy(3, this.viewId);
                }
                super.zza(zzrg);
            }
            
            public zza zztW() {
                this.zzawm = "";
                this.zzawn = "";
                this.viewId = 0;
                this.zzaVU = null;
                this.zzaWf = -1;
                return this;
            }
        }
    }
    
    public static final class zzb extends zzrh<zzb>
    {
        private static volatile zzb[] zzawo;
        public String name;
        public zzd zzawp;
        
        public zzb() {
            this.zztY();
        }
        
        public static zzb[] zztX() {
            Label_0027: {
                if (zzb.zzawo != null) {
                    break Label_0027;
                }
                synchronized (zzrl.zzaWe) {
                    if (zzb.zzawo == null) {
                        zzb.zzawo = new zzb[0];
                    }
                    return zzb.zzawo;
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
                if (o instanceof zzb) {
                    final zzb zzb = (zzb)o;
                    if (this.name == null) {
                        b2 = b;
                        if (zzb.name != null) {
                            return b2;
                        }
                    }
                    else if (!this.name.equals(zzb.name)) {
                        return false;
                    }
                    if (this.zzawp == null) {
                        b2 = b;
                        if (zzb.zzawp != null) {
                            return b2;
                        }
                    }
                    else if (!this.zzawp.equals(zzb.zzawp)) {
                        return false;
                    }
                    return this.zza(zzb);
                }
            }
            return b2;
        }
        
        @Override
        public int hashCode() {
            int hashCode = 0;
            int hashCode2;
            if (this.name == null) {
                hashCode2 = 0;
            }
            else {
                hashCode2 = this.name.hashCode();
            }
            if (this.zzawp != null) {
                hashCode = this.zzawp.hashCode();
            }
            return ((hashCode2 + 527) * 31 + hashCode) * 31 + this.zzBI();
        }
        
        @Override
        protected int zzB() {
            int zzB;
            final int n = zzB = super.zzB();
            if (!this.name.equals("")) {
                zzB = n + zzrg.zzk(1, this.name);
            }
            int n2 = zzB;
            if (this.zzawp != null) {
                n2 = zzB + zzrg.zzc(2, this.zzawp);
            }
            return n2;
        }
        
        @Override
        public void zza(final zzrg zzrg) throws IOException {
            if (!this.name.equals("")) {
                zzrg.zzb(1, this.name);
            }
            if (this.zzawp != null) {
                zzrg.zza(2, this.zzawp);
            }
            super.zza(zzrg);
        }
        
        public zzb zztY() {
            this.name = "";
            this.zzawp = null;
            this.zzaVU = null;
            this.zzaWf = -1;
            return this;
        }
    }
    
    public static final class zzc extends zzrh<zzc>
    {
        public String type;
        public zzb[] zzawq;
        
        public zzc() {
            this.zztZ();
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
                if (o instanceof zzc) {
                    final zzc zzc = (zzc)o;
                    if (this.type == null) {
                        b2 = b;
                        if (zzc.type != null) {
                            return b2;
                        }
                    }
                    else if (!this.type.equals(zzc.type)) {
                        return false;
                    }
                    b2 = b;
                    if (zzrl.equals(this.zzawq, zzc.zzawq)) {
                        return this.zza(zzc);
                    }
                }
            }
            return b2;
        }
        
        @Override
        public int hashCode() {
            int hashCode;
            if (this.type == null) {
                hashCode = 0;
            }
            else {
                hashCode = this.type.hashCode();
            }
            return ((hashCode + 527) * 31 + zzrl.hashCode(this.zzawq)) * 31 + this.zzBI();
        }
        
        @Override
        protected int zzB() {
            int zzB;
            final int n = zzB = super.zzB();
            if (!this.type.equals("")) {
                zzB = n + zzrg.zzk(1, this.type);
            }
            int n2 = zzB;
            if (this.zzawq != null) {
                n2 = zzB;
                if (this.zzawq.length > 0) {
                    int n3;
                    for (int i = 0; i < this.zzawq.length; ++i, zzB = n3) {
                        final zzb zzb = this.zzawq[i];
                        n3 = zzB;
                        if (zzb != null) {
                            n3 = zzB + zzrg.zzc(2, zzb);
                        }
                    }
                    n2 = zzB;
                }
            }
            return n2;
        }
        
        @Override
        public void zza(final zzrg zzrg) throws IOException {
            if (!this.type.equals("")) {
                zzrg.zzb(1, this.type);
            }
            if (this.zzawq != null && this.zzawq.length > 0) {
                for (int i = 0; i < this.zzawq.length; ++i) {
                    final zzb zzb = this.zzawq[i];
                    if (zzb != null) {
                        zzrg.zza(2, zzb);
                    }
                }
            }
            super.zza(zzrg);
        }
        
        public zzc zztZ() {
            this.type = "";
            this.zzawq = zzb.zztX();
            this.zzaVU = null;
            this.zzaWf = -1;
            return this;
        }
    }
    
    public static final class zzd extends zzrh<zzd>
    {
        public String zzabE;
        public boolean zzawr;
        public long zzaws;
        public double zzawt;
        public zzc zzawu;
        
        public zzd() {
            this.zzua();
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
                if (o instanceof zzd) {
                    final zzd zzd = (zzd)o;
                    b2 = b;
                    if (this.zzawr == zzd.zzawr) {
                        if (this.zzabE == null) {
                            b2 = b;
                            if (zzd.zzabE != null) {
                                return b2;
                            }
                        }
                        else if (!this.zzabE.equals(zzd.zzabE)) {
                            return false;
                        }
                        b2 = b;
                        if (this.zzaws == zzd.zzaws) {
                            b2 = b;
                            if (Double.doubleToLongBits(this.zzawt) == Double.doubleToLongBits(zzd.zzawt)) {
                                if (this.zzawu == null) {
                                    b2 = b;
                                    if (zzd.zzawu != null) {
                                        return b2;
                                    }
                                }
                                else if (!this.zzawu.equals(zzd.zzawu)) {
                                    return false;
                                }
                                return this.zza(zzd);
                            }
                        }
                    }
                }
            }
            return b2;
        }
        
        @Override
        public int hashCode() {
            int hashCode = 0;
            int n;
            if (this.zzawr) {
                n = 1231;
            }
            else {
                n = 1237;
            }
            int hashCode2;
            if (this.zzabE == null) {
                hashCode2 = 0;
            }
            else {
                hashCode2 = this.zzabE.hashCode();
            }
            final int n2 = (int)(this.zzaws ^ this.zzaws >>> 32);
            final long doubleToLongBits = Double.doubleToLongBits(this.zzawt);
            final int n3 = (int)(doubleToLongBits ^ doubleToLongBits >>> 32);
            if (this.zzawu != null) {
                hashCode = this.zzawu.hashCode();
            }
            return ((((hashCode2 + (n + 527) * 31) * 31 + n2) * 31 + n3) * 31 + hashCode) * 31 + this.zzBI();
        }
        
        @Override
        protected int zzB() {
            int zzB;
            final int n = zzB = super.zzB();
            if (this.zzawr) {
                zzB = n + zzrg.zzc(1, this.zzawr);
            }
            int n2 = zzB;
            if (!this.zzabE.equals("")) {
                n2 = zzB + zzrg.zzk(2, this.zzabE);
            }
            int n3 = n2;
            if (this.zzaws != 0L) {
                n3 = n2 + zzrg.zzd(3, this.zzaws);
            }
            int n4 = n3;
            if (Double.doubleToLongBits(this.zzawt) != Double.doubleToLongBits(0.0)) {
                n4 = n3 + zzrg.zzb(4, this.zzawt);
            }
            int n5 = n4;
            if (this.zzawu != null) {
                n5 = n4 + zzrg.zzc(5, this.zzawu);
            }
            return n5;
        }
        
        @Override
        public void zza(final zzrg zzrg) throws IOException {
            if (this.zzawr) {
                zzrg.zzb(1, this.zzawr);
            }
            if (!this.zzabE.equals("")) {
                zzrg.zzb(2, this.zzabE);
            }
            if (this.zzaws != 0L) {
                zzrg.zzb(3, this.zzaws);
            }
            if (Double.doubleToLongBits(this.zzawt) != Double.doubleToLongBits(0.0)) {
                zzrg.zza(4, this.zzawt);
            }
            if (this.zzawu != null) {
                zzrg.zza(5, this.zzawu);
            }
            super.zza(zzrg);
        }
        
        public zzd zzua() {
            this.zzawr = false;
            this.zzabE = "";
            this.zzaws = 0L;
            this.zzawt = 0.0;
            this.zzawu = null;
            this.zzaVU = null;
            this.zzaWf = -1;
            return this;
        }
    }
}
