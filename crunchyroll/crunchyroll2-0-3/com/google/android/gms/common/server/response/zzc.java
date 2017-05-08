// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.server.response;

import java.util.ArrayList;
import com.google.android.gms.common.internal.safeparcel.zza;
import java.util.List;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zzc implements Parcelable$Creator<FieldMappingDictionary>
{
    static void zza(final FieldMappingDictionary fieldMappingDictionary, final Parcel parcel, int zzac) {
        zzac = zzb.zzac(parcel);
        zzb.zzc(parcel, 1, fieldMappingDictionary.getVersionCode());
        zzb.zzc(parcel, 2, fieldMappingDictionary.zzoB(), false);
        zzb.zza(parcel, 3, fieldMappingDictionary.zzoC(), false);
        zzb.zzH(parcel, zzac);
    }
    
    public FieldMappingDictionary zzaj(final Parcel parcel) {
        String zzo = null;
        final int zzab = zza.zzab(parcel);
        int zzg = 0;
        ArrayList<FieldMappingDictionary.Entry> zzc = null;
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
                    zzc = zza.zzc(parcel, zzaa, (android.os.Parcelable$Creator<FieldMappingDictionary.Entry>)FieldMappingDictionary.Entry.CREATOR);
                    continue;
                }
                case 3: {
                    zzo = zza.zzo(parcel, zzaa);
                    continue;
                }
            }
        }
        if (parcel.dataPosition() != zzab) {
            throw new zza.zza("Overread allowed size end=" + zzab, parcel);
        }
        return new FieldMappingDictionary(zzg, zzc, zzo);
    }
    
    public FieldMappingDictionary[] zzbJ(final int n) {
        return new FieldMappingDictionary[n];
    }
}
