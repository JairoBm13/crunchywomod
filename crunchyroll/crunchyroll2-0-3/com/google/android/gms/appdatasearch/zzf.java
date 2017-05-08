// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.appdatasearch;

import android.accounts.Account;
import com.google.android.gms.common.internal.safeparcel.zza;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zzf implements Parcelable$Creator<GetRecentContextCall.Request>
{
    static void zza(final GetRecentContextCall.Request request, final Parcel parcel, final int n) {
        final int zzac = zzb.zzac(parcel);
        zzb.zza(parcel, 1, (Parcelable)request.zzNj, n, false);
        zzb.zzc(parcel, 1000, request.zzCY);
        zzb.zza(parcel, 2, request.zzNk);
        zzb.zza(parcel, 3, request.zzNl);
        zzb.zza(parcel, 4, request.zzNm);
        zzb.zzH(parcel, zzac);
    }
    
    public GetRecentContextCall.Request[] zzag(final int n) {
        return new GetRecentContextCall.Request[n];
    }
    
    public GetRecentContextCall.Request zzv(final Parcel parcel) {
        boolean zzc = false;
        final int zzab = zza.zzab(parcel);
        Account account = null;
        boolean zzc2 = false;
        boolean zzc3 = false;
        int zzg = 0;
        while (parcel.dataPosition() < zzab) {
            final int zzaa = zza.zzaa(parcel);
            switch (zza.zzbA(zzaa)) {
                default: {
                    zza.zzb(parcel, zzaa);
                    continue;
                }
                case 1: {
                    account = zza.zza(parcel, zzaa, (android.os.Parcelable$Creator<Account>)Account.CREATOR);
                    continue;
                }
                case 1000: {
                    zzg = zza.zzg(parcel, zzaa);
                    continue;
                }
                case 2: {
                    zzc3 = zza.zzc(parcel, zzaa);
                    continue;
                }
                case 3: {
                    zzc2 = zza.zzc(parcel, zzaa);
                    continue;
                }
                case 4: {
                    zzc = zza.zzc(parcel, zzaa);
                    continue;
                }
            }
        }
        if (parcel.dataPosition() != zzab) {
            throw new zza.zza("Overread allowed size end=" + zzab, parcel);
        }
        return new GetRecentContextCall.Request(zzg, account, zzc3, zzc2, zzc);
    }
}
