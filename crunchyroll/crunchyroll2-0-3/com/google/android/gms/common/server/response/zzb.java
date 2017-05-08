// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.server.response;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.safeparcel.zza;
import android.os.Parcelable;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zzb implements Parcelable$Creator<FieldMappingDictionary.FieldMapPair>
{
    static void zza(final FieldMappingDictionary.FieldMapPair fieldMapPair, final Parcel parcel, final int n) {
        final int zzac = com.google.android.gms.common.internal.safeparcel.zzb.zzac(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, fieldMapPair.versionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, fieldMapPair.zzaC, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, (Parcelable)fieldMapPair.zzabU, n, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzH(parcel, zzac);
    }
    
    public FieldMappingDictionary.FieldMapPair zzai(final Parcel parcel) {
        SafeParcelable safeParcelable = null;
        final int zzab = zza.zzab(parcel);
        int zzg = 0;
        String zzo = null;
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
                    zzo = zza.zzo(parcel, zzaa);
                    continue;
                }
                case 3: {
                    safeParcelable = zza.zza(parcel, zzaa, (android.os.Parcelable$Creator<FastJsonResponse.Field<?, ?>>)FastJsonResponse.Field.CREATOR);
                    continue;
                }
            }
        }
        if (parcel.dataPosition() != zzab) {
            throw new zza.zza("Overread allowed size end=" + zzab, parcel);
        }
        return new FieldMappingDictionary.FieldMapPair(zzg, zzo, (FastJsonResponse.Field<?, ?>)safeParcelable);
    }
    
    public FieldMappingDictionary.FieldMapPair[] zzbI(final int n) {
        return new FieldMappingDictionary.FieldMapPair[n];
    }
}
