// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.internal;

import android.os.Parcel;
import android.app.PendingIntent;
import android.os.IBinder;
import com.google.android.gms.common.ConnectionResult;
import android.os.Parcelable$Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class ResolveAccountResponse implements SafeParcelable
{
    public static final Parcelable$Creator<ResolveAccountResponse> CREATOR;
    final int zzCY;
    private boolean zzWY;
    private ConnectionResult zzYh;
    IBinder zzZO;
    private boolean zzabd;
    
    static {
        CREATOR = (Parcelable$Creator)new zzw();
    }
    
    public ResolveAccountResponse(final int n) {
        this(new ConnectionResult(n, null));
    }
    
    ResolveAccountResponse(final int zzCY, final IBinder zzZO, final ConnectionResult zzYh, final boolean zzWY, final boolean zzabd) {
        this.zzCY = zzCY;
        this.zzZO = zzZO;
        this.zzYh = zzYh;
        this.zzWY = zzWY;
        this.zzabd = zzabd;
    }
    
    public ResolveAccountResponse(final ConnectionResult connectionResult) {
        this(1, null, connectionResult, false, false);
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (!(o instanceof ResolveAccountResponse)) {
                return false;
            }
            final ResolveAccountResponse resolveAccountResponse = (ResolveAccountResponse)o;
            if (!this.zzYh.equals(resolveAccountResponse.zzYh) || !this.zznZ().equals(resolveAccountResponse.zznZ())) {
                return false;
            }
        }
        return true;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zzw.zza(this, parcel, n);
    }
    
    public IAccountAccessor zznZ() {
        return IAccountAccessor.zza.zzaD(this.zzZO);
    }
    
    public ConnectionResult zzoa() {
        return this.zzYh;
    }
    
    public boolean zzob() {
        return this.zzWY;
    }
    
    public boolean zzoc() {
        return this.zzabd;
    }
}
