// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.cast;

import android.os.Parcel;
import com.google.android.gms.common.internal.zzt;
import com.google.android.gms.cast.internal.zzf;
import java.util.Locale;
import android.os.Parcelable$Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class LaunchOptions implements SafeParcelable
{
    public static final Parcelable$Creator<LaunchOptions> CREATOR;
    private final int zzCY;
    private String zzRA;
    private boolean zzRz;
    
    static {
        CREATOR = (Parcelable$Creator)new zzc();
    }
    
    public LaunchOptions() {
        this(1, false, zzf.zzb(Locale.getDefault()));
    }
    
    LaunchOptions(final int zzCY, final boolean zzRz, final String zzRA) {
        this.zzCY = zzCY;
        this.zzRz = zzRz;
        this.zzRA = zzRA;
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o != this) {
            if (!(o instanceof LaunchOptions)) {
                return false;
            }
            final LaunchOptions launchOptions = (LaunchOptions)o;
            if (this.zzRz != launchOptions.zzRz || !zzf.zza(this.zzRA, launchOptions.zzRA)) {
                return false;
            }
        }
        return true;
    }
    
    public String getLanguage() {
        return this.zzRA;
    }
    
    public boolean getRelaunchIfRunning() {
        return this.zzRz;
    }
    
    int getVersionCode() {
        return this.zzCY;
    }
    
    @Override
    public int hashCode() {
        return zzt.hashCode(this.zzRz, this.zzRA);
    }
    
    @Override
    public String toString() {
        return String.format("LaunchOptions(relaunchIfRunning=%b, language=%s)", this.zzRz, this.zzRA);
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zzc.zza(this, parcel, n);
    }
}
