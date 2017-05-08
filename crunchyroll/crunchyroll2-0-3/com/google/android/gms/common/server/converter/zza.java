// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.server.converter;

import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zza implements Parcelable$Creator<ConverterWrapper>
{
    static void zza(final ConverterWrapper converterWrapper, final Parcel parcel, final int n) {
        final int zzac = zzb.zzac(parcel);
        zzb.zzc(parcel, 1, converterWrapper.getVersionCode());
        zzb.zza(parcel, 2, (Parcelable)converterWrapper.zzoh(), n, false);
        zzb.zzH(parcel, zzac);
    }
    
    public ConverterWrapper zzae(final Parcel parcel) {
        final int zzab = com.google.android.gms.common.internal.safeparcel.zza.zzab(parcel);
        int zzg = 0;
        StringToIntConverter stringToIntConverter = null;
        while (parcel.dataPosition() < zzab) {
            final int zzaa = com.google.android.gms.common.internal.safeparcel.zza.zzaa(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzbA(zzaa)) {
                default: {
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, zzaa);
                    continue;
                }
                case 1: {
                    zzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzaa);
                    continue;
                }
                case 2: {
                    stringToIntConverter = com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, zzaa, (android.os.Parcelable$Creator<StringToIntConverter>)StringToIntConverter.CREATOR);
                    continue;
                }
            }
        }
        if (parcel.dataPosition() != zzab) {
            throw new com.google.android.gms.common.internal.safeparcel.zza.zza("Overread allowed size end=" + zzab, parcel);
        }
        return new ConverterWrapper(zzg, stringToIntConverter);
    }
    
    public ConverterWrapper[] zzbE(final int n) {
        return new ConverterWrapper[n];
    }
}
