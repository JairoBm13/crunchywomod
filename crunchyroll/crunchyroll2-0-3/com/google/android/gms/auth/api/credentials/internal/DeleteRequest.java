// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.auth.api.credentials.internal;

import android.os.Parcel;
import com.google.android.gms.auth.api.credentials.Credential;
import android.os.Parcelable$Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class DeleteRequest implements SafeParcelable
{
    public static final Parcelable$Creator<DeleteRequest> CREATOR;
    final int zzCY;
    private final Credential zzPb;
    
    static {
        CREATOR = (Parcelable$Creator)new zze();
    }
    
    DeleteRequest(final int zzCY, final Credential zzPb) {
        this.zzCY = zzCY;
        this.zzPb = zzPb;
    }
    
    public int describeContents() {
        return 0;
    }
    
    public Credential getCredential() {
        return this.zzPb;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zze.zza(this, parcel, n);
    }
}
