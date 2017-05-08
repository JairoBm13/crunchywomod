// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.server.converter;

import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zzc implements Parcelable$Creator<StringToIntConverter.Entry>
{
    static void zza(final StringToIntConverter.Entry entry, final Parcel parcel, int zzac) {
        zzac = zzb.zzac(parcel);
        zzb.zzc(parcel, 1, entry.versionCode);
        zzb.zza(parcel, 2, entry.zzabE, false);
        zzb.zzc(parcel, 3, entry.zzabF);
        zzb.zzH(parcel, zzac);
    }
    
    public StringToIntConverter.Entry zzag(final Parcel parcel) {
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
                    zzo = zza.zzo(parcel, zzaa);
                    continue;
                }
                case 3: {
                    zzg = zza.zzg(parcel, zzaa);
                    continue;
                }
            }
        }
        if (parcel.dataPosition() != zzab) {
            throw new zza.zza("Overread allowed size end=" + zzab, parcel);
        }
        return new StringToIntConverter.Entry(zzg2, zzo, zzg);
    }
    
    public StringToIntConverter.Entry[] zzbG(final int n) {
        return new StringToIntConverter.Entry[n];
    }
}
