// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.appdatasearch;

import com.google.android.gms.common.internal.safeparcel.zza;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zzd implements Parcelable$Creator<DocumentSection>
{
    static void zza(final DocumentSection documentSection, final Parcel parcel, final int n) {
        final int zzac = zzb.zzac(parcel);
        zzb.zza(parcel, 1, documentSection.zzNe, false);
        zzb.zzc(parcel, 1000, documentSection.zzCY);
        zzb.zza(parcel, 3, (Parcelable)documentSection.zzNf, n, false);
        zzb.zzc(parcel, 4, documentSection.zzNg);
        zzb.zza(parcel, 5, documentSection.zzNh, false);
        zzb.zzH(parcel, zzac);
    }
    
    public DocumentSection[] zzae(final int n) {
        return new DocumentSection[n];
    }
    
    public DocumentSection zzt(final Parcel parcel) {
        byte[] zzr = null;
        final int zzab = zza.zzab(parcel);
        int zzg = 0;
        int zzg2 = -1;
        RegisterSectionInfo registerSectionInfo = null;
        String zzo = null;
        while (parcel.dataPosition() < zzab) {
            final int zzaa = zza.zzaa(parcel);
            switch (zza.zzbA(zzaa)) {
                default: {
                    zza.zzb(parcel, zzaa);
                    continue;
                }
                case 1: {
                    zzo = zza.zzo(parcel, zzaa);
                    continue;
                }
                case 1000: {
                    zzg = zza.zzg(parcel, zzaa);
                    continue;
                }
                case 3: {
                    registerSectionInfo = zza.zza(parcel, zzaa, (android.os.Parcelable$Creator<RegisterSectionInfo>)RegisterSectionInfo.CREATOR);
                    continue;
                }
                case 4: {
                    zzg2 = zza.zzg(parcel, zzaa);
                    continue;
                }
                case 5: {
                    zzr = zza.zzr(parcel, zzaa);
                    continue;
                }
            }
        }
        if (parcel.dataPosition() != zzab) {
            throw new zza.zza("Overread allowed size end=" + zzab, parcel);
        }
        return new DocumentSection(zzg, zzo, registerSectionInfo, zzg2, zzr);
    }
}
