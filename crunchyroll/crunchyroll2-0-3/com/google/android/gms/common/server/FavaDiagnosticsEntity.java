// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.server;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class FavaDiagnosticsEntity implements SafeParcelable
{
    public static final zza CREATOR;
    final int zzCY;
    public final String zzaby;
    public final int zzabz;
    
    static {
        CREATOR = new zza();
    }
    
    public FavaDiagnosticsEntity(final int zzCY, final String zzaby, final int zzabz) {
        this.zzCY = zzCY;
        this.zzaby = zzaby;
        this.zzabz = zzabz;
    }
    
    public int describeContents() {
        return 0;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zza.zza(this, parcel, n);
    }
}
