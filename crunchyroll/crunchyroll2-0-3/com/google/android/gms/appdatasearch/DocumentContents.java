// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.appdatasearch;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import java.util.BitSet;
import android.accounts.Account;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class DocumentContents implements SafeParcelable
{
    public static final zzb CREATOR;
    public final Account account;
    final int zzCY;
    final DocumentSection[] zzMS;
    public final String zzMT;
    public final boolean zzMU;
    
    static {
        CREATOR = new zzb();
    }
    
    DocumentContents(final int zzCY, final DocumentSection[] zzMS, final String zzMT, final boolean zzMU, final Account account) {
        this.zzCY = zzCY;
        this.zzMS = zzMS;
        this.zzMT = zzMT;
        this.zzMU = zzMU;
        this.account = account;
    }
    
    DocumentContents(final String s, final boolean b, final Account account, final DocumentSection... array) {
        this(1, array, s, b, account);
        final BitSet set = new BitSet(zzh.zzkL());
        for (int i = 0; i < array.length; ++i) {
            final int zzNg = array[i].zzNg;
            if (zzNg != -1) {
                if (set.get(zzNg)) {
                    throw new IllegalArgumentException("Duplicate global search section type " + zzh.zzai(zzNg));
                }
                set.set(zzNg);
            }
        }
    }
    
    public int describeContents() {
        final zzb creator = DocumentContents.CREATOR;
        return 0;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        final zzb creator = DocumentContents.CREATOR;
        zzb.zza(this, parcel, n);
    }
    
    public static class zza
    {
        private List<DocumentSection> zzMV;
        private String zzMW;
        private boolean zzMX;
        private Account zzMY;
        
        public zza zzI(final boolean zzMX) {
            this.zzMX = zzMX;
            return this;
        }
        
        public zza zza(final Account zzMY) {
            this.zzMY = zzMY;
            return this;
        }
        
        public zza zza(final DocumentSection documentSection) {
            if (this.zzMV == null) {
                this.zzMV = new ArrayList<DocumentSection>();
            }
            this.zzMV.add(documentSection);
            return this;
        }
        
        public zza zzbp(final String zzMW) {
            this.zzMW = zzMW;
            return this;
        }
        
        public DocumentContents zzkJ() {
            final String zzMW = this.zzMW;
            final boolean zzMX = this.zzMX;
            final Account zzMY = this.zzMY;
            DocumentSection[] array;
            if (this.zzMV != null) {
                array = this.zzMV.toArray(new DocumentSection[this.zzMV.size()]);
            }
            else {
                array = null;
            }
            return new DocumentContents(zzMW, zzMX, zzMY, array);
        }
    }
}
