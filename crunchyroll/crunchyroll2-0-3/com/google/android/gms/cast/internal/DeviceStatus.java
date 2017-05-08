// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.cast.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.zzt;
import com.google.android.gms.cast.ApplicationMetadata;
import android.os.Parcelable$Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class DeviceStatus implements SafeParcelable
{
    public static final Parcelable$Creator<DeviceStatus> CREATOR;
    private final int zzCY;
    private double zzSh;
    private boolean zzSi;
    private ApplicationMetadata zzUF;
    private int zzUu;
    private int zzUv;
    
    static {
        CREATOR = (Parcelable$Creator)new zzg();
    }
    
    public DeviceStatus() {
        this(3, Double.NaN, false, -1, null, -1);
    }
    
    DeviceStatus(final int zzCY, final double zzSh, final boolean zzSi, final int zzUu, final ApplicationMetadata zzUF, final int zzUv) {
        this.zzCY = zzCY;
        this.zzSh = zzSh;
        this.zzSi = zzSi;
        this.zzUu = zzUu;
        this.zzUF = zzUF;
        this.zzUv = zzUv;
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o != this) {
            if (!(o instanceof DeviceStatus)) {
                return false;
            }
            final DeviceStatus deviceStatus = (DeviceStatus)o;
            if (this.zzSh != deviceStatus.zzSh || this.zzSi != deviceStatus.zzSi || this.zzUu != deviceStatus.zzUu || !zzf.zza(this.zzUF, deviceStatus.zzUF) || this.zzUv != deviceStatus.zzUv) {
                return false;
            }
        }
        return true;
    }
    
    public ApplicationMetadata getApplicationMetadata() {
        return this.zzUF;
    }
    
    public int getVersionCode() {
        return this.zzCY;
    }
    
    @Override
    public int hashCode() {
        return zzt.hashCode(this.zzSh, this.zzSi, this.zzUu, this.zzUF, this.zzUv);
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zzg.zza(this, parcel, n);
    }
    
    public double zzlO() {
        return this.zzSh;
    }
    
    public int zzlP() {
        return this.zzUu;
    }
    
    public int zzlQ() {
        return this.zzUv;
    }
    
    public boolean zzlX() {
        return this.zzSi;
    }
}
