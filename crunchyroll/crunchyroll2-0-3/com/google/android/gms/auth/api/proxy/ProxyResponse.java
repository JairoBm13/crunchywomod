// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.auth.api.proxy;

import android.os.Parcel;
import android.app.PendingIntent;
import android.os.Bundle;
import android.os.Parcelable$Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class ProxyResponse implements SafeParcelable
{
    public static final Parcelable$Creator<ProxyResponse> CREATOR;
    final int versionCode;
    public final byte[] zzPs;
    final Bundle zzPt;
    public final int zzPu;
    public final PendingIntent zzPv;
    public final int zzPw;
    
    static {
        CREATOR = (Parcelable$Creator)new zzc();
    }
    
    ProxyResponse(final int versionCode, final int zzPu, final PendingIntent zzPv, final int zzPw, final Bundle zzPt, final byte[] zzPs) {
        this.versionCode = versionCode;
        this.zzPu = zzPu;
        this.zzPw = zzPw;
        this.zzPt = zzPt;
        this.zzPs = zzPs;
        this.zzPv = zzPv;
    }
    
    public int describeContents() {
        return 0;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zzc.zza(this, parcel, n);
    }
}
