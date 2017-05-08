// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.auth.api.proxy;

import android.os.Parcel;
import android.os.Bundle;
import android.os.Parcelable$Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class ProxyRequest implements SafeParcelable
{
    public static final Parcelable$Creator<ProxyRequest> CREATOR;
    public static final int zzPh;
    public static final int zzPi;
    public static final int zzPj;
    public static final int zzPk;
    public static final int zzPl;
    public static final int zzPm;
    public static final int zzPn;
    public static final int zzPo;
    public static final int zzPp;
    final int versionCode;
    public final int zzPq;
    public final long zzPr;
    public final byte[] zzPs;
    Bundle zzPt;
    public final String zzzf;
    
    static {
        CREATOR = (Parcelable$Creator)new zzb();
        zzPh = 0;
        zzPi = 1;
        zzPj = 2;
        zzPk = 3;
        zzPl = 4;
        zzPm = 5;
        zzPn = 6;
        zzPo = 7;
        zzPp = 7;
    }
    
    ProxyRequest(final int versionCode, final String zzzf, final int zzPq, final long zzPr, final byte[] zzPs, final Bundle zzPt) {
        this.versionCode = versionCode;
        this.zzzf = zzzf;
        this.zzPq = zzPq;
        this.zzPr = zzPr;
        this.zzPs = zzPs;
        this.zzPt = zzPt;
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public String toString() {
        return "ProxyRequest[ url: " + this.zzzf + ", method: " + this.zzPq + " ]";
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zzb.zza(this, parcel, n);
    }
}
