// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.auth;

import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zza implements Parcelable$Creator<AccountChangeEvent>
{
    static void zza(final AccountChangeEvent accountChangeEvent, final Parcel parcel, int zzac) {
        zzac = zzb.zzac(parcel);
        zzb.zzc(parcel, 1, accountChangeEvent.mVersion);
        zzb.zza(parcel, 2, accountChangeEvent.zzOw);
        zzb.zza(parcel, 3, accountChangeEvent.zzOx, false);
        zzb.zzc(parcel, 4, accountChangeEvent.zzOy);
        zzb.zzc(parcel, 5, accountChangeEvent.zzOz);
        zzb.zza(parcel, 6, accountChangeEvent.zzOA, false);
        zzb.zzH(parcel, zzac);
    }
    
    public AccountChangeEvent[] zzap(final int n) {
        return new AccountChangeEvent[n];
    }
    
    public AccountChangeEvent zzz(final Parcel parcel) {
        String zzo = null;
        int zzg = 0;
        final int zzab = com.google.android.gms.common.internal.safeparcel.zza.zzab(parcel);
        long zzi = 0L;
        int zzg2 = 0;
        String zzo2 = null;
        int zzg3 = 0;
        while (parcel.dataPosition() < zzab) {
            final int zzaa = com.google.android.gms.common.internal.safeparcel.zza.zzaa(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzbA(zzaa)) {
                default: {
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, zzaa);
                    continue;
                }
                case 1: {
                    zzg3 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzaa);
                    continue;
                }
                case 2: {
                    zzi = com.google.android.gms.common.internal.safeparcel.zza.zzi(parcel, zzaa);
                    continue;
                }
                case 3: {
                    zzo2 = com.google.android.gms.common.internal.safeparcel.zza.zzo(parcel, zzaa);
                    continue;
                }
                case 4: {
                    zzg2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzaa);
                    continue;
                }
                case 5: {
                    zzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzaa);
                    continue;
                }
                case 6: {
                    zzo = com.google.android.gms.common.internal.safeparcel.zza.zzo(parcel, zzaa);
                    continue;
                }
            }
        }
        if (parcel.dataPosition() != zzab) {
            throw new com.google.android.gms.common.internal.safeparcel.zza.zza("Overread allowed size end=" + zzab, parcel);
        }
        return new AccountChangeEvent(zzg3, zzi, zzo2, zzg2, zzg, zzo);
    }
}
