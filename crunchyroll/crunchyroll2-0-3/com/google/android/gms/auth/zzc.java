// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.auth;

import java.util.List;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zzc implements Parcelable$Creator<AccountChangeEventsResponse>
{
    static void zza(final AccountChangeEventsResponse accountChangeEventsResponse, final Parcel parcel, int zzac) {
        zzac = zzb.zzac(parcel);
        zzb.zzc(parcel, 1, accountChangeEventsResponse.mVersion);
        zzb.zzc(parcel, 2, accountChangeEventsResponse.zzoB, false);
        zzb.zzH(parcel, zzac);
    }
    
    public AccountChangeEventsResponse zzB(final Parcel parcel) {
        final int zzab = zza.zzab(parcel);
        int zzg = 0;
        List<AccountChangeEvent> zzc = null;
        while (parcel.dataPosition() < zzab) {
            final int zzaa = zza.zzaa(parcel);
            switch (zza.zzbA(zzaa)) {
                default: {
                    zza.zzb(parcel, zzaa);
                    continue;
                }
                case 1: {
                    zzg = zza.zzg(parcel, zzaa);
                    continue;
                }
                case 2: {
                    zzc = zza.zzc(parcel, zzaa, AccountChangeEvent.CREATOR);
                    continue;
                }
            }
        }
        if (parcel.dataPosition() != zzab) {
            throw new zza.zza("Overread allowed size end=" + zzab, parcel);
        }
        return new AccountChangeEventsResponse(zzg, zzc);
    }
    
    public AccountChangeEventsResponse[] zzar(final int n) {
        return new AccountChangeEventsResponse[n];
    }
}
