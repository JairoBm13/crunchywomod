// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.appdatasearch;

import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zzi implements Parcelable$Creator<RegisterSectionInfo>
{
    static void zza(final RegisterSectionInfo registerSectionInfo, final Parcel parcel, final int n) {
        final int zzac = zzb.zzac(parcel);
        zzb.zza(parcel, 1, registerSectionInfo.name, false);
        zzb.zzc(parcel, 1000, registerSectionInfo.zzCY);
        zzb.zza(parcel, 2, registerSectionInfo.zzNs, false);
        zzb.zza(parcel, 3, registerSectionInfo.zzNt);
        zzb.zzc(parcel, 4, registerSectionInfo.weight);
        zzb.zza(parcel, 5, registerSectionInfo.zzNu);
        zzb.zza(parcel, 6, registerSectionInfo.zzNv, false);
        zzb.zza(parcel, 7, registerSectionInfo.zzNw, n, false);
        zzb.zza(parcel, 8, registerSectionInfo.zzNx, false);
        zzb.zza(parcel, 11, registerSectionInfo.zzNy, false);
        zzb.zzH(parcel, zzac);
    }
    
    public RegisterSectionInfo[] zzak(final int n) {
        return new RegisterSectionInfo[n];
    }
    
    public RegisterSectionInfo zzx(final Parcel parcel) {
        boolean zzc = false;
        String zzo = null;
        final int zzab = zza.zzab(parcel);
        int zzg = 1;
        int[] zzu = null;
        Feature[] array = null;
        String zzo2 = null;
        boolean zzc2 = false;
        String zzo3 = null;
        String zzo4 = null;
        int zzg2 = 0;
        while (parcel.dataPosition() < zzab) {
            final int zzaa = zza.zzaa(parcel);
            switch (zza.zzbA(zzaa)) {
                default: {
                    zza.zzb(parcel, zzaa);
                    continue;
                }
                case 1: {
                    zzo4 = zza.zzo(parcel, zzaa);
                    continue;
                }
                case 1000: {
                    zzg2 = zza.zzg(parcel, zzaa);
                    continue;
                }
                case 2: {
                    zzo3 = zza.zzo(parcel, zzaa);
                    continue;
                }
                case 3: {
                    zzc2 = zza.zzc(parcel, zzaa);
                    continue;
                }
                case 4: {
                    zzg = zza.zzg(parcel, zzaa);
                    continue;
                }
                case 5: {
                    zzc = zza.zzc(parcel, zzaa);
                    continue;
                }
                case 6: {
                    zzo2 = zza.zzo(parcel, zzaa);
                    continue;
                }
                case 7: {
                    array = zza.zzb(parcel, zzaa, (android.os.Parcelable$Creator<Feature>)Feature.CREATOR);
                    continue;
                }
                case 8: {
                    zzu = zza.zzu(parcel, zzaa);
                    continue;
                }
                case 11: {
                    zzo = zza.zzo(parcel, zzaa);
                    continue;
                }
            }
        }
        if (parcel.dataPosition() != zzab) {
            throw new zza.zza("Overread allowed size end=" + zzab, parcel);
        }
        return new RegisterSectionInfo(zzg2, zzo4, zzo3, zzc2, zzg, zzc, zzo2, array, zzu, zzo);
    }
}
