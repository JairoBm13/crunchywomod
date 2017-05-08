// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.appdatasearch;

import com.google.android.gms.common.internal.safeparcel.zza;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zzj implements Parcelable$Creator<UsageInfo>
{
    static void zza(final UsageInfo usageInfo, final Parcel parcel, final int n) {
        final int zzac = zzb.zzac(parcel);
        zzb.zza(parcel, 1, (Parcelable)usageInfo.zzNH, n, false);
        zzb.zzc(parcel, 1000, usageInfo.zzCY);
        zzb.zza(parcel, 2, usageInfo.zzNI);
        zzb.zzc(parcel, 3, usageInfo.zzNJ);
        zzb.zza(parcel, 4, usageInfo.zztt, false);
        zzb.zza(parcel, 5, (Parcelable)usageInfo.zzNK, n, false);
        zzb.zza(parcel, 6, usageInfo.zzNL);
        zzb.zzc(parcel, 7, usageInfo.zzNM);
        zzb.zzc(parcel, 8, usageInfo.zzNN);
        zzb.zzH(parcel, zzac);
    }
    
    public UsageInfo[] zzan(final int n) {
        return new UsageInfo[n];
    }
    
    public UsageInfo zzy(final Parcel parcel) {
        DocumentContents documentContents = null;
        int zzg = 0;
        final int zzab = zza.zzab(parcel);
        long zzi = 0L;
        int zzg2 = -1;
        boolean zzc = false;
        String zzo = null;
        int zzg3 = 0;
        DocumentId documentId = null;
        int zzg4 = 0;
        while (parcel.dataPosition() < zzab) {
            final int zzaa = zza.zzaa(parcel);
            switch (zza.zzbA(zzaa)) {
                default: {
                    zza.zzb(parcel, zzaa);
                    continue;
                }
                case 1: {
                    documentId = zza.zza(parcel, zzaa, (android.os.Parcelable$Creator<DocumentId>)DocumentId.CREATOR);
                    continue;
                }
                case 1000: {
                    zzg4 = zza.zzg(parcel, zzaa);
                    continue;
                }
                case 2: {
                    zzi = zza.zzi(parcel, zzaa);
                    continue;
                }
                case 3: {
                    zzg3 = zza.zzg(parcel, zzaa);
                    continue;
                }
                case 4: {
                    zzo = zza.zzo(parcel, zzaa);
                    continue;
                }
                case 5: {
                    documentContents = zza.zza(parcel, zzaa, (android.os.Parcelable$Creator<DocumentContents>)DocumentContents.CREATOR);
                    continue;
                }
                case 6: {
                    zzc = zza.zzc(parcel, zzaa);
                    continue;
                }
                case 7: {
                    zzg2 = zza.zzg(parcel, zzaa);
                    continue;
                }
                case 8: {
                    zzg = zza.zzg(parcel, zzaa);
                    continue;
                }
            }
        }
        if (parcel.dataPosition() != zzab) {
            throw new zza.zza("Overread allowed size end=" + zzab, parcel);
        }
        return new UsageInfo(zzg4, documentId, zzi, zzg3, zzo, documentContents, zzc, zzg2, zzg);
    }
}
