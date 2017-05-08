// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.appdatasearch;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class RegisterSectionInfo implements SafeParcelable
{
    public static final zzi CREATOR;
    public final String name;
    public final int weight;
    final int zzCY;
    public final String zzNs;
    public final boolean zzNt;
    public final boolean zzNu;
    public final String zzNv;
    public final Feature[] zzNw;
    final int[] zzNx;
    public final String zzNy;
    
    static {
        CREATOR = new zzi();
    }
    
    RegisterSectionInfo(final int zzCY, final String name, final String zzNs, final boolean zzNt, final int weight, final boolean zzNu, final String zzNv, final Feature[] zzNw, final int[] zzNx, final String zzNy) {
        this.zzCY = zzCY;
        this.name = name;
        this.zzNs = zzNs;
        this.zzNt = zzNt;
        this.weight = weight;
        this.zzNu = zzNu;
        this.zzNv = zzNv;
        this.zzNw = zzNw;
        this.zzNx = zzNx;
        this.zzNy = zzNy;
    }
    
    RegisterSectionInfo(final String s, final String s2, final boolean b, final int n, final boolean b2, final String s3, final Feature[] array, final int[] array2, final String s4) {
        this(2, s, s2, b, n, b2, s3, array, array2, s4);
    }
    
    public int describeContents() {
        final zzi creator = RegisterSectionInfo.CREATOR;
        return 0;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        final zzi creator = RegisterSectionInfo.CREATOR;
        zzi.zza(this, parcel, n);
    }
    
    public static final class zza
    {
        private final String mName;
        private boolean zzNA;
        private int zzNB;
        private boolean zzNC;
        private String zzND;
        private final List<Feature> zzNE;
        private BitSet zzNF;
        private String zzNG;
        private String zzNz;
        
        public zza(final String mName) {
            this.mName = mName;
            this.zzNB = 1;
            this.zzNE = new ArrayList<Feature>();
        }
        
        public zza zzJ(final boolean zzNA) {
            this.zzNA = zzNA;
            return this;
        }
        
        public zza zzK(final boolean zzNC) {
            this.zzNC = zzNC;
            return this;
        }
        
        public zza zzaj(final int n) {
            if (this.zzNF == null) {
                this.zzNF = new BitSet();
            }
            this.zzNF.set(n);
            return this;
        }
        
        public zza zzbr(final String zzNz) {
            this.zzNz = zzNz;
            return this;
        }
        
        public zza zzbs(final String zzNG) {
            this.zzNG = zzNG;
            return this;
        }
        
        public RegisterSectionInfo zzkM() {
            int n = 0;
            int[] array = null;
            if (this.zzNF != null) {
                final int[] array2 = new int[this.zzNF.cardinality()];
                int n2 = this.zzNF.nextSetBit(0);
                while (true) {
                    array = array2;
                    if (n2 < 0) {
                        break;
                    }
                    array2[n] = n2;
                    n2 = this.zzNF.nextSetBit(n2 + 1);
                    ++n;
                }
            }
            return new RegisterSectionInfo(this.mName, this.zzNz, this.zzNA, this.zzNB, this.zzNC, this.zzND, this.zzNE.toArray(new Feature[this.zzNE.size()]), array, this.zzNG);
        }
    }
}
