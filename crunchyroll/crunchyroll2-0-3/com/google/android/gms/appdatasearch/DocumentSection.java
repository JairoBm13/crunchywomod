// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import com.google.android.gms.common.internal.zzu;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class DocumentSection implements SafeParcelable
{
    public static final zzd CREATOR;
    public static final int zzNc;
    private static final RegisterSectionInfo zzNd;
    final int zzCY;
    public final String zzNe;
    final RegisterSectionInfo zzNf;
    public final int zzNg;
    public final byte[] zzNh;
    
    static {
        zzNc = Integer.parseInt("-1");
        CREATOR = new zzd();
        zzNd = new RegisterSectionInfo.zza("SsbContext").zzJ(true).zzbr("blob").zzkM();
    }
    
    DocumentSection(final int zzCY, String zzkK, final RegisterSectionInfo zzNf, final int zzNg, final byte[] zzNh) {
        zzu.zzb(zzNg == DocumentSection.zzNc || zzh.zzai(zzNg) != null, "Invalid section type " + zzNg);
        this.zzCY = zzCY;
        this.zzNe = zzkK;
        this.zzNf = zzNf;
        this.zzNg = zzNg;
        this.zzNh = zzNh;
        zzkK = this.zzkK();
        if (zzkK != null) {
            throw new IllegalArgumentException(zzkK);
        }
    }
    
    public DocumentSection(final String s, final RegisterSectionInfo registerSectionInfo) {
        this(1, s, registerSectionInfo, DocumentSection.zzNc, null);
    }
    
    public DocumentSection(final String s, final RegisterSectionInfo registerSectionInfo, final String s2) {
        this(1, s, registerSectionInfo, zzh.zzbq(s2), null);
    }
    
    public DocumentSection(final byte[] array, final RegisterSectionInfo registerSectionInfo) {
        this(1, null, registerSectionInfo, DocumentSection.zzNc, array);
    }
    
    public static DocumentSection zzh(final byte[] array) {
        return new DocumentSection(array, DocumentSection.zzNd);
    }
    
    public int describeContents() {
        final zzd creator = DocumentSection.CREATOR;
        return 0;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        final zzd creator = DocumentSection.CREATOR;
        zzd.zza(this, parcel, n);
    }
    
    public String zzkK() {
        if (this.zzNg != DocumentSection.zzNc && zzh.zzai(this.zzNg) == null) {
            return "Invalid section type " + this.zzNg;
        }
        if (this.zzNe != null && this.zzNh != null) {
            return "Both content and blobContent set";
        }
        return null;
    }
}
