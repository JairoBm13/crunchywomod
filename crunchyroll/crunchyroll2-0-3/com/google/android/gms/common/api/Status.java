// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.api;

import android.os.Parcel;
import com.google.android.gms.common.internal.zzt;
import android.app.PendingIntent;
import android.os.Parcelable$Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class Status implements Result, SafeParcelable
{
    public static final Parcelable$Creator<Status> CREATOR;
    public static final Status zzXP;
    public static final Status zzXQ;
    public static final Status zzXR;
    public static final Status zzXS;
    public static final Status zzXT;
    private final PendingIntent mPendingIntent;
    private final int zzCY;
    private final int zzTS;
    private final String zzXU;
    
    static {
        zzXP = new Status(0);
        zzXQ = new Status(14);
        zzXR = new Status(8);
        zzXS = new Status(15);
        zzXT = new Status(16);
        CREATOR = (Parcelable$Creator)new zzk();
    }
    
    public Status(final int n) {
        this(n, null);
    }
    
    Status(final int zzCY, final int zzTS, final String zzXU, final PendingIntent mPendingIntent) {
        this.zzCY = zzCY;
        this.zzTS = zzTS;
        this.zzXU = zzXU;
        this.mPendingIntent = mPendingIntent;
    }
    
    public Status(final int n, final String s) {
        this(1, n, s, null);
    }
    
    public Status(final int n, final String s, final PendingIntent pendingIntent) {
        this(1, n, s, pendingIntent);
    }
    
    private String zzmU() {
        if (this.zzXU != null) {
            return this.zzXU;
        }
        return CommonStatusCodes.getStatusCodeString(this.zzTS);
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o instanceof Status) {
            final Status status = (Status)o;
            if (this.zzCY == status.zzCY && this.zzTS == status.zzTS && zzt.equal(this.zzXU, status.zzXU) && zzt.equal(this.mPendingIntent, status.mPendingIntent)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public Status getStatus() {
        return this;
    }
    
    public int getStatusCode() {
        return this.zzTS;
    }
    
    public String getStatusMessage() {
        return this.zzXU;
    }
    
    int getVersionCode() {
        return this.zzCY;
    }
    
    @Override
    public int hashCode() {
        return zzt.hashCode(this.zzCY, this.zzTS, this.zzXU, this.mPendingIntent);
    }
    
    public boolean isSuccess() {
        return this.zzTS <= 0;
    }
    
    @Override
    public String toString() {
        return zzt.zzt(this).zzg("statusCode", this.zzmU()).zzg("resolution", this.mPendingIntent).toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zzk.zza(this, parcel, n);
    }
    
    PendingIntent zzmT() {
        return this.mPendingIntent;
    }
}
