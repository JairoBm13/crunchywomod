// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.internal;

import android.os.Parcel;
import com.google.android.gms.common.GooglePlayServicesUtil;
import android.os.Bundle;
import com.google.android.gms.common.api.Scope;
import android.os.IBinder;
import android.os.Parcelable$Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class ValidateAccountRequest implements SafeParcelable
{
    public static final Parcelable$Creator<ValidateAccountRequest> CREATOR;
    final int zzCY;
    final IBinder zzZO;
    private final Scope[] zzZP;
    private final int zzabg;
    private final Bundle zzabh;
    private final String zzabi;
    
    static {
        CREATOR = (Parcelable$Creator)new zzaa();
    }
    
    ValidateAccountRequest(final int zzCY, final int zzabg, final IBinder zzZO, final Scope[] zzZP, final Bundle zzabh, final String zzabi) {
        this.zzCY = zzCY;
        this.zzabg = zzabg;
        this.zzZO = zzZO;
        this.zzZP = zzZP;
        this.zzabh = zzabh;
        this.zzabi = zzabi;
    }
    
    public ValidateAccountRequest(final IAccountAccessor accountAccessor, final Scope[] array, final String s, final Bundle bundle) {
        final int google_PLAY_SERVICES_VERSION_CODE = GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE;
        IBinder binder;
        if (accountAccessor == null) {
            binder = null;
        }
        else {
            binder = accountAccessor.asBinder();
        }
        this(1, google_PLAY_SERVICES_VERSION_CODE, binder, array, bundle, s);
    }
    
    public int describeContents() {
        return 0;
    }
    
    public String getCallingPackage() {
        return this.zzabi;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zzaa.zza(this, parcel, n);
    }
    
    public int zzod() {
        return this.zzabg;
    }
    
    public Scope[] zzoe() {
        return this.zzZP;
    }
    
    public Bundle zzof() {
        return this.zzabh;
    }
}
