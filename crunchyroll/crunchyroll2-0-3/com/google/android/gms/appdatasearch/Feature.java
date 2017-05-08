// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import android.os.Bundle;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class Feature implements SafeParcelable
{
    public static final zze CREATOR;
    public final int id;
    final int zzCY;
    final Bundle zzNi;
    
    static {
        CREATOR = new zze();
    }
    
    Feature(final int zzCY, final int id, final Bundle zzNi) {
        this.zzCY = zzCY;
        this.id = id;
        this.zzNi = zzNi;
    }
    
    public int describeContents() {
        final zze creator = Feature.CREATOR;
        return 0;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        final zze creator = Feature.CREATOR;
        zze.zza(this, parcel, n);
    }
}
