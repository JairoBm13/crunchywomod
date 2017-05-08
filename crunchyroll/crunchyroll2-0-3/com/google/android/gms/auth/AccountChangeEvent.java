// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.auth;

import android.os.Parcel;
import com.google.android.gms.common.internal.zzt;
import com.google.android.gms.common.internal.zzu;
import android.os.Parcelable$Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class AccountChangeEvent implements SafeParcelable
{
    public static final Parcelable$Creator<AccountChangeEvent> CREATOR;
    final int mVersion;
    final String zzOA;
    final long zzOw;
    final String zzOx;
    final int zzOy;
    final int zzOz;
    
    static {
        CREATOR = (Parcelable$Creator)new zza();
    }
    
    AccountChangeEvent(final int mVersion, final long zzOw, final String s, final int zzOy, final int zzOz, final String zzOA) {
        this.mVersion = mVersion;
        this.zzOw = zzOw;
        this.zzOx = zzu.zzu(s);
        this.zzOy = zzOy;
        this.zzOz = zzOz;
        this.zzOA = zzOA;
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o != this) {
            if (!(o instanceof AccountChangeEvent)) {
                return false;
            }
            final AccountChangeEvent accountChangeEvent = (AccountChangeEvent)o;
            if (this.mVersion != accountChangeEvent.mVersion || this.zzOw != accountChangeEvent.zzOw || !zzt.equal(this.zzOx, accountChangeEvent.zzOx) || this.zzOy != accountChangeEvent.zzOy || this.zzOz != accountChangeEvent.zzOz || !zzt.equal(this.zzOA, accountChangeEvent.zzOA)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        return zzt.hashCode(this.mVersion, this.zzOw, this.zzOx, this.zzOy, this.zzOz, this.zzOA);
    }
    
    @Override
    public String toString() {
        String s = "UNKNOWN";
        switch (this.zzOy) {
            case 1: {
                s = "ADDED";
                break;
            }
            case 2: {
                s = "REMOVED";
                break;
            }
            case 4: {
                s = "RENAMED_TO";
                break;
            }
            case 3: {
                s = "RENAMED_FROM";
                break;
            }
        }
        return "AccountChangeEvent {accountName = " + this.zzOx + ", changeType = " + s + ", changeData = " + this.zzOA + ", eventIndex = " + this.zzOz + "}";
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zza.zza(this, parcel, n);
    }
}
