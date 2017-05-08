// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.auth;

import android.os.Parcel;
import android.text.TextUtils;
import android.accounts.Account;
import android.os.Parcelable$Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class AccountChangeEventsRequest implements SafeParcelable
{
    public static final Parcelable$Creator<AccountChangeEventsRequest> CREATOR;
    final int mVersion;
    Account zzMY;
    @Deprecated
    String zzOx;
    int zzOz;
    
    static {
        CREATOR = (Parcelable$Creator)new zzb();
    }
    
    public AccountChangeEventsRequest() {
        this.mVersion = 1;
    }
    
    AccountChangeEventsRequest(final int mVersion, final int zzOz, final String zzOx, final Account zzMY) {
        this.mVersion = mVersion;
        this.zzOz = zzOz;
        this.zzOx = zzOx;
        if (zzMY == null && !TextUtils.isEmpty((CharSequence)zzOx)) {
            this.zzMY = new Account(zzOx, "com.google");
            return;
        }
        this.zzMY = zzMY;
    }
    
    public int describeContents() {
        return 0;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zzb.zza(this, parcel, n);
    }
}
