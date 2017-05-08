// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.server.converter;

import java.util.ArrayList;
import com.google.android.gms.common.internal.safeparcel.zza;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zzb implements Parcelable$Creator<StringToIntConverter>
{
    static void zza(final StringToIntConverter stringToIntConverter, final Parcel parcel, int zzac) {
        zzac = com.google.android.gms.common.internal.safeparcel.zzb.zzac(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, stringToIntConverter.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 2, stringToIntConverter.zzoj(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzH(parcel, zzac);
    }
    
    public StringToIntConverter zzaf(final Parcel parcel) {
        final int zzab = zza.zzab(parcel);
        int zzg = 0;
        ArrayList<StringToIntConverter.Entry> zzc = null;
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
                    zzc = zza.zzc(parcel, zzaa, (android.os.Parcelable$Creator<StringToIntConverter.Entry>)StringToIntConverter.Entry.CREATOR);
                    continue;
                }
            }
        }
        if (parcel.dataPosition() != zzab) {
            throw new zza.zza("Overread allowed size end=" + zzab, parcel);
        }
        return new StringToIntConverter(zzg, zzc);
    }
    
    public StringToIntConverter[] zzbF(final int n) {
        return new StringToIntConverter[n];
    }
}
