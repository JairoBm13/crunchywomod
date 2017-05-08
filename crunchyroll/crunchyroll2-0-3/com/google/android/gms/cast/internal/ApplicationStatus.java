// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.cast.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.zzt;
import android.os.Parcelable$Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class ApplicationStatus implements SafeParcelable
{
    public static final Parcelable$Creator<ApplicationStatus> CREATOR;
    private final int zzCY;
    private String zzUd;
    
    static {
        CREATOR = (Parcelable$Creator)new zza();
    }
    
    public ApplicationStatus() {
        this(1, null);
    }
    
    ApplicationStatus(final int zzCY, final String zzUd) {
        this.zzCY = zzCY;
        this.zzUd = zzUd;
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof ApplicationStatus && zzf.zza(this.zzUd, ((ApplicationStatus)o).zzUd));
    }
    
    public int getVersionCode() {
        return this.zzCY;
    }
    
    @Override
    public int hashCode() {
        return zzt.hashCode(this.zzUd);
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zza.zza(this, parcel, n);
    }
    
    public String zzlI() {
        return this.zzUd;
    }
}
