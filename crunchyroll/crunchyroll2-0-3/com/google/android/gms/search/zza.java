// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.search;

import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zza implements Parcelable$Creator<GoogleNowAuthState>
{
    static void zza(final GoogleNowAuthState googleNowAuthState, final Parcel parcel, int zzac) {
        zzac = zzb.zzac(parcel);
        zzb.zza(parcel, 1, googleNowAuthState.getAuthCode(), false);
        zzb.zzc(parcel, 1000, googleNowAuthState.zzCY);
        zzb.zza(parcel, 2, googleNowAuthState.getAccessToken(), false);
        zzb.zza(parcel, 3, googleNowAuthState.getNextAllowedTimeMillis());
        zzb.zzH(parcel, zzac);
    }
    
    public GoogleNowAuthState zzfX(final Parcel parcel) {
        String zzo = null;
        final int zzab = com.google.android.gms.common.internal.safeparcel.zza.zzab(parcel);
        int zzg = 0;
        long zzi = 0L;
        String zzo2 = null;
        while (parcel.dataPosition() < zzab) {
            final int zzaa = com.google.android.gms.common.internal.safeparcel.zza.zzaa(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzbA(zzaa)) {
                default: {
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, zzaa);
                    continue;
                }
                case 1: {
                    zzo2 = com.google.android.gms.common.internal.safeparcel.zza.zzo(parcel, zzaa);
                    continue;
                }
                case 1000: {
                    zzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzaa);
                    continue;
                }
                case 2: {
                    zzo = com.google.android.gms.common.internal.safeparcel.zza.zzo(parcel, zzaa);
                    continue;
                }
                case 3: {
                    zzi = com.google.android.gms.common.internal.safeparcel.zza.zzi(parcel, zzaa);
                    continue;
                }
            }
        }
        if (parcel.dataPosition() != zzab) {
            throw new com.google.android.gms.common.internal.safeparcel.zza.zza("Overread allowed size end=" + zzab, parcel);
        }
        return new GoogleNowAuthState(zzg, zzo2, zzo, zzi);
    }
    
    public GoogleNowAuthState[] zziN(final int n) {
        return new GoogleNowAuthState[n];
    }
}
