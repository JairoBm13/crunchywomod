// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.server.response;

import com.google.android.gms.common.internal.safeparcel.zza;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zze implements Parcelable$Creator<SafeParcelResponse>
{
    static void zza(final SafeParcelResponse safeParcelResponse, final Parcel parcel, final int n) {
        final int zzac = zzb.zzac(parcel);
        zzb.zzc(parcel, 1, safeParcelResponse.getVersionCode());
        zzb.zza(parcel, 2, safeParcelResponse.zzoE(), false);
        zzb.zza(parcel, 3, (Parcelable)safeParcelResponse.zzoF(), n, false);
        zzb.zzH(parcel, zzac);
    }
    
    public SafeParcelResponse zzal(final Parcel parcel) {
        FieldMappingDictionary fieldMappingDictionary = null;
        final int zzab = zza.zzab(parcel);
        int zzg = 0;
        Parcel zzD = null;
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
                    zzD = zza.zzD(parcel, zzaa);
                    continue;
                }
                case 3: {
                    fieldMappingDictionary = zza.zza(parcel, zzaa, (android.os.Parcelable$Creator<FieldMappingDictionary>)FieldMappingDictionary.CREATOR);
                    continue;
                }
            }
        }
        if (parcel.dataPosition() != zzab) {
            throw new zza.zza("Overread allowed size end=" + zzab, parcel);
        }
        return new SafeParcelResponse(zzg, zzD, fieldMappingDictionary);
    }
    
    public SafeParcelResponse[] zzbL(final int n) {
        return new SafeParcelResponse[n];
    }
}
