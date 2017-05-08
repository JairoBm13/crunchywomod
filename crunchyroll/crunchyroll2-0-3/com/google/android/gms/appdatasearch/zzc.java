// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.appdatasearch;

import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zzc implements Parcelable$Creator<DocumentId>
{
    static void zza(final DocumentId documentId, final Parcel parcel, int zzac) {
        zzac = zzb.zzac(parcel);
        zzb.zza(parcel, 1, documentId.zzMZ, false);
        zzb.zzc(parcel, 1000, documentId.zzCY);
        zzb.zza(parcel, 2, documentId.zzNa, false);
        zzb.zza(parcel, 3, documentId.zzNb, false);
        zzb.zzH(parcel, zzac);
    }
    
    public DocumentId[] zzad(final int n) {
        return new DocumentId[n];
    }
    
    public DocumentId zzs(final Parcel parcel) {
        String zzo = null;
        final int zzab = zza.zzab(parcel);
        int zzg = 0;
        String zzo2 = null;
        String zzo3 = null;
        while (parcel.dataPosition() < zzab) {
            final int zzaa = zza.zzaa(parcel);
            switch (zza.zzbA(zzaa)) {
                default: {
                    zza.zzb(parcel, zzaa);
                    continue;
                }
                case 1: {
                    zzo3 = zza.zzo(parcel, zzaa);
                    continue;
                }
                case 1000: {
                    zzg = zza.zzg(parcel, zzaa);
                    continue;
                }
                case 2: {
                    zzo2 = zza.zzo(parcel, zzaa);
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
        return new DocumentId(zzg, zzo3, zzo2, zzo);
    }
}
