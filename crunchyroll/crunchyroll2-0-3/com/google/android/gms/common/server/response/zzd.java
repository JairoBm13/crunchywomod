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

public class zzd implements Parcelable$Creator<FieldMappingDictionary.Entry>
{
    static void zza(final FieldMappingDictionary.Entry entry, final Parcel parcel, int zzac) {
        zzac = zzb.zzac(parcel);
        zzb.zzc(parcel, 1, entry.versionCode);
        zzb.zza(parcel, 2, entry.className, false);
        zzb.zzc(parcel, 3, entry.zzabT, false);
        zzb.zzH(parcel, zzac);
    }
    
    public FieldMappingDictionary.Entry zzak(final Parcel parcel) {
        ArrayList<FieldMappingDictionary.FieldMapPair> zzc = null;
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
                    zzc = zza.zzc(parcel, zzaa, (android.os.Parcelable$Creator<FieldMappingDictionary.FieldMapPair>)FieldMappingDictionary.FieldMapPair.CREATOR);
                    continue;
                }
            }
        }
        if (parcel.dataPosition() != zzab) {
            throw new zza.zza("Overread allowed size end=" + zzab, parcel);
        }
        return new FieldMappingDictionary.Entry(zzg, zzo, zzc);
    }
    
    public FieldMappingDictionary.Entry[] zzbK(final int n) {
        return new FieldMappingDictionary.Entry[n];
    }
}
