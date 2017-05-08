// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.auth;

import android.os.Parcel;
import com.google.android.gms.common.internal.zzu;
import java.util.List;
import android.os.Parcelable$Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class AccountChangeEventsResponse implements SafeParcelable
{
    public static final Parcelable$Creator<AccountChangeEventsResponse> CREATOR;
    final int mVersion;
    final List<AccountChangeEvent> zzoB;
    
    static {
        CREATOR = (Parcelable$Creator)new zzc();
    }
    
    AccountChangeEventsResponse(final int mVersion, final List<AccountChangeEvent> list) {
        this.mVersion = mVersion;
        this.zzoB = zzu.zzu(list);
    }
    
    public int describeContents() {
        return 0;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zzc.zza(this, parcel, n);
    }
}
