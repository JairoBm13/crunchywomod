// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.server;

import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zza implements Parcelable$Creator<FavaDiagnosticsEntity>
{
    static void zza(final FavaDiagnosticsEntity favaDiagnosticsEntity, final Parcel parcel, int zzac) {
        zzac = zzb.zzac(parcel);
        zzb.zzc(parcel, 1, favaDiagnosticsEntity.zzCY);
        zzb.zza(parcel, 2, favaDiagnosticsEntity.zzaby, false);
        zzb.zzc(parcel, 3, favaDiagnosticsEntity.zzabz);
        zzb.zzH(parcel, zzac);
    }
    
    public FavaDiagnosticsEntity zzad(final Parcel parcel) {
        int zzg = 0;
        final int zzab = com.google.android.gms.common.internal.safeparcel.zza.zzab(parcel);
        String zzo = null;
        int zzg2 = 0;
        while (parcel.dataPosition() < zzab) {
            final int zzaa = com.google.android.gms.common.internal.safeparcel.zza.zzaa(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzbA(zzaa)) {
                default: {
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, zzaa);
                    continue;
                }
                case 1: {
                    zzg2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzaa);
                    continue;
                }
                case 2: {
                    zzo = com.google.android.gms.common.internal.safeparcel.zza.zzo(parcel, zzaa);
                    continue;
                }
                case 3: {
                    zzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzaa);
                    continue;
                }
            }
        }
        if (parcel.dataPosition() != zzab) {
            throw new com.google.android.gms.common.internal.safeparcel.zza.zza("Overread allowed size end=" + zzab, parcel);
        }
        return new FavaDiagnosticsEntity(zzg2, zzo, zzg);
    }
    
    public FavaDiagnosticsEntity[] zzbD(final int n) {
        return new FavaDiagnosticsEntity[n];
    }
}
