// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.server.response;

import com.google.android.gms.common.server.converter.ConverterWrapper;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zza implements Parcelable$Creator<FastJsonResponse.Field>
{
    static void zza(final FastJsonResponse.Field field, final Parcel parcel, final int n) {
        final int zzac = zzb.zzac(parcel);
        zzb.zzc(parcel, 1, field.getVersionCode());
        zzb.zzc(parcel, 2, field.zzok());
        zzb.zza(parcel, 3, field.zzoq());
        zzb.zzc(parcel, 4, field.zzol());
        zzb.zza(parcel, 5, field.zzor());
        zzb.zza(parcel, 6, field.zzos(), false);
        zzb.zzc(parcel, 7, field.zzot());
        zzb.zza(parcel, 8, field.zzov(), false);
        zzb.zza(parcel, 9, (Parcelable)field.zzox(), n, false);
        zzb.zzH(parcel, zzac);
    }
    
    public FastJsonResponse.Field zzah(final Parcel parcel) {
        ConverterWrapper converterWrapper = null;
        int zzg = 0;
        final int zzab = com.google.android.gms.common.internal.safeparcel.zza.zzab(parcel);
        String zzo = null;
        String zzo2 = null;
        boolean zzc = false;
        int zzg2 = 0;
        boolean zzc2 = false;
        int zzg3 = 0;
        int zzg4 = 0;
        while (parcel.dataPosition() < zzab) {
            final int zzaa = com.google.android.gms.common.internal.safeparcel.zza.zzaa(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzbA(zzaa)) {
                default: {
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, zzaa);
                    continue;
                }
                case 1: {
                    zzg4 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzaa);
                    continue;
                }
                case 2: {
                    zzg3 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzaa);
                    continue;
                }
                case 3: {
                    zzc2 = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, zzaa);
                    continue;
                }
                case 4: {
                    zzg2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzaa);
                    continue;
                }
                case 5: {
                    zzc = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, zzaa);
                    continue;
                }
                case 6: {
                    zzo2 = com.google.android.gms.common.internal.safeparcel.zza.zzo(parcel, zzaa);
                    continue;
                }
                case 7: {
                    zzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzaa);
                    continue;
                }
                case 8: {
                    zzo = com.google.android.gms.common.internal.safeparcel.zza.zzo(parcel, zzaa);
                    continue;
                }
                case 9: {
                    converterWrapper = com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, zzaa, (android.os.Parcelable$Creator<ConverterWrapper>)ConverterWrapper.CREATOR);
                    continue;
                }
            }
        }
        if (parcel.dataPosition() != zzab) {
            throw new com.google.android.gms.common.internal.safeparcel.zza.zza("Overread allowed size end=" + zzab, parcel);
        }
        return new FastJsonResponse.Field(zzg4, zzg3, zzc2, zzg2, zzc, zzo2, zzg, zzo, converterWrapper);
    }
    
    public FastJsonResponse.Field[] zzbH(final int n) {
        return new FastJsonResponse.Field[n];
    }
}
