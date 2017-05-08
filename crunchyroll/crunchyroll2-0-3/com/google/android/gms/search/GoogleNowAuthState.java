// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.search;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class GoogleNowAuthState implements SafeParcelable
{
    public static final Parcelable$Creator<GoogleNowAuthState> CREATOR;
    final int zzCY;
    String zzaJE;
    String zzaJF;
    long zzaJG;
    
    static {
        CREATOR = (Parcelable$Creator)new zza();
    }
    
    GoogleNowAuthState(final int zzCY, final String zzaJE, final String zzaJF, final long zzaJG) {
        this.zzCY = zzCY;
        this.zzaJE = zzaJE;
        this.zzaJF = zzaJF;
        this.zzaJG = zzaJG;
    }
    
    public int describeContents() {
        return 0;
    }
    
    public String getAccessToken() {
        return this.zzaJF;
    }
    
    public String getAuthCode() {
        return this.zzaJE;
    }
    
    public long getNextAllowedTimeMillis() {
        return this.zzaJG;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zza.zza(this, parcel, n);
    }
}
