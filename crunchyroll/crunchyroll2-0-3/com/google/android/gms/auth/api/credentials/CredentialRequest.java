// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.auth.api.credentials;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class CredentialRequest implements SafeParcelable
{
    public static final Parcelable$Creator<CredentialRequest> CREATOR;
    final int zzCY;
    private final boolean zzOY;
    private final String[] zzOZ;
    
    static {
        CREATOR = (Parcelable$Creator)new zzb();
    }
    
    CredentialRequest(final int zzCY, final boolean zzOY, final String[] zzOZ) {
        this.zzCY = zzCY;
        this.zzOY = zzOY;
        this.zzOZ = zzOZ;
    }
    
    public int describeContents() {
        return 0;
    }
    
    public String[] getAccountTypes() {
        return this.zzOZ;
    }
    
    public boolean getSupportsPasswordLogin() {
        return this.zzOY;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zzb.zza(this, parcel, n);
    }
}
