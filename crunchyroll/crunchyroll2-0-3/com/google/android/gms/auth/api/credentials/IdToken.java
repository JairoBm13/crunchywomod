// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.auth.api.credentials;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class IdToken implements SafeParcelable
{
    public static final Parcelable$Creator<IdToken> CREATOR;
    final int zzCY;
    private final String zzOX;
    private final String zzPa;
    
    static {
        CREATOR = (Parcelable$Creator)new zzc();
    }
    
    IdToken(final int zzCY, final String zzOX, final String zzPa) {
        this.zzCY = zzCY;
        this.zzOX = zzOX;
        this.zzPa = zzPa;
    }
    
    public int describeContents() {
        return 0;
    }
    
    public String getAccountType() {
        return this.zzOX;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zzc.zza(this, parcel, n);
    }
    
    public String zzlc() {
        return this.zzPa;
    }
}
