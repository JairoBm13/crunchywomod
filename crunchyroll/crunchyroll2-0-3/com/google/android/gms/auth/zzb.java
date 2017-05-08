// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.auth;

import android.accounts.Account;
import com.google.android.gms.common.internal.safeparcel.zza;
import android.os.Parcelable;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zzb implements Parcelable$Creator<AccountChangeEventsRequest>
{
    static void zza(final AccountChangeEventsRequest accountChangeEventsRequest, final Parcel parcel, final int n) {
        final int zzac = com.google.android.gms.common.internal.safeparcel.zzb.zzac(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, accountChangeEventsRequest.mVersion);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 2, accountChangeEventsRequest.zzOz);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, accountChangeEventsRequest.zzOx, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, (Parcelable)accountChangeEventsRequest.zzMY, n, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzH(parcel, zzac);
    }
    
    public AccountChangeEventsRequest zzA(final Parcel parcel) {
        Account account = null;
        int zzg = 0;
        final int zzab = zza.zzab(parcel);
        String zzo = null;
        int zzg2 = 0;
        while (parcel.dataPosition() < zzab) {
            final int zzaa = zza.zzaa(parcel);
            switch (zza.zzbA(zzaa)) {
                default: {
                    zza.zzb(parcel, zzaa);
                    continue;
                }
                case 1: {
                    zzg2 = zza.zzg(parcel, zzaa);
                    continue;
                }
                case 2: {
                    zzg = zza.zzg(parcel, zzaa);
                    continue;
                }
                case 3: {
                    zzo = zza.zzo(parcel, zzaa);
                    continue;
                }
                case 4: {
                    account = zza.zza(parcel, zzaa, (android.os.Parcelable$Creator<Account>)Account.CREATOR);
                    continue;
                }
            }
        }
        if (parcel.dataPosition() != zzab) {
            throw new zza.zza("Overread allowed size end=" + zzab, parcel);
        }
        return new AccountChangeEventsRequest(zzg2, zzg, zzo, account);
    }
    
    public AccountChangeEventsRequest[] zzaq(final int n) {
        return new AccountChangeEventsRequest[n];
    }
}
