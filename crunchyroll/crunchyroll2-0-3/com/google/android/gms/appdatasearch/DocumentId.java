// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class DocumentId implements SafeParcelable
{
    public static final zzc CREATOR;
    final int zzCY;
    final String zzMZ;
    final String zzNa;
    final String zzNb;
    
    static {
        CREATOR = new zzc();
    }
    
    DocumentId(final int zzCY, final String zzMZ, final String zzNa, final String zzNb) {
        this.zzCY = zzCY;
        this.zzMZ = zzMZ;
        this.zzNa = zzNa;
        this.zzNb = zzNb;
    }
    
    public DocumentId(final String s, final String s2, final String s3) {
        this(1, s, s2, s3);
    }
    
    public int describeContents() {
        final zzc creator = DocumentId.CREATOR;
        return 0;
    }
    
    @Override
    public String toString() {
        return String.format("DocumentId[packageName=%s, corpusName=%s, uri=%s]", this.zzMZ, this.zzNa, this.zzNb);
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        final zzc creator = DocumentId.CREATOR;
        zzc.zza(this, parcel, n);
    }
}
