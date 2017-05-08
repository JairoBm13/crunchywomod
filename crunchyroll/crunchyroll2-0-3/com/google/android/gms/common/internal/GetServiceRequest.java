// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.internal;

import java.util.Collection;
import android.os.Parcel;
import com.google.android.gms.common.GooglePlayServicesUtil;
import android.accounts.Account;
import android.os.Bundle;
import com.google.android.gms.common.api.Scope;
import android.os.IBinder;
import android.os.Parcelable$Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class GetServiceRequest implements SafeParcelable
{
    public static final Parcelable$Creator<GetServiceRequest> CREATOR;
    final int version;
    final int zzaad;
    int zzaae;
    String zzaaf;
    IBinder zzaag;
    Scope[] zzaah;
    Bundle zzaai;
    Account zzaaj;
    
    static {
        CREATOR = (Parcelable$Creator)new zzh();
    }
    
    public GetServiceRequest(final int zzaad) {
        this.version = 2;
        this.zzaae = GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE;
        this.zzaad = zzaad;
    }
    
    GetServiceRequest(final int version, final int zzaad, final int zzaae, final String zzaaf, final IBinder zzaag, final Scope[] zzaah, final Bundle zzaai, final Account zzaaj) {
        this.version = version;
        this.zzaad = zzaad;
        this.zzaae = zzaae;
        this.zzaaf = zzaaf;
        if (version < 2) {
            this.zzaaj = this.zzaC(zzaag);
        }
        else {
            this.zzaag = zzaag;
            this.zzaaj = zzaaj;
        }
        this.zzaah = zzaah;
        this.zzaai = zzaai;
    }
    
    private Account zzaC(final IBinder binder) {
        Account zza = null;
        if (binder != null) {
            zza = com.google.android.gms.common.internal.zza.zza(IAccountAccessor.zza.zzaD(binder));
        }
        return zza;
    }
    
    public int describeContents() {
        return 0;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zzh.zza(this, parcel, n);
    }
    
    public GetServiceRequest zzb(final Account zzaaj) {
        this.zzaaj = zzaaj;
        return this;
    }
    
    public GetServiceRequest zzb(final IAccountAccessor accountAccessor) {
        if (accountAccessor != null) {
            this.zzaag = accountAccessor.asBinder();
        }
        return this;
    }
    
    public GetServiceRequest zzb(final Collection<Scope> collection) {
        this.zzaah = collection.toArray(new Scope[collection.size()]);
        return this;
    }
    
    public GetServiceRequest zzcb(final String zzaaf) {
        this.zzaaf = zzaaf;
        return this;
    }
    
    public GetServiceRequest zzf(final Bundle zzaai) {
        this.zzaai = zzaai;
        return this;
    }
}
