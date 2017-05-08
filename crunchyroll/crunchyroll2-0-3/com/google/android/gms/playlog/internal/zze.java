// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.playlog.internal;

import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zze implements Parcelable$Creator<PlayLoggerContext>
{
    static void zza(final PlayLoggerContext playLoggerContext, final Parcel parcel, int zzac) {
        zzac = zzb.zzac(parcel);
        zzb.zzc(parcel, 1, playLoggerContext.versionCode);
        zzb.zza(parcel, 2, playLoggerContext.packageName, false);
        zzb.zzc(parcel, 3, playLoggerContext.zzaGP);
        zzb.zzc(parcel, 4, playLoggerContext.zzaGQ);
        zzb.zza(parcel, 5, playLoggerContext.zzaGR, false);
        zzb.zza(parcel, 6, playLoggerContext.zzaGS, false);
        zzb.zza(parcel, 7, playLoggerContext.zzaGT);
        zzb.zza(parcel, 8, playLoggerContext.zzaGU, false);
        zzb.zza(parcel, 9, playLoggerContext.zzaGV);
        zzb.zzH(parcel, zzac);
    }
    
    public PlayLoggerContext zzfH(final Parcel parcel) {
        String zzo = null;
        boolean zzc = false;
        final int zzab = zza.zzab(parcel);
        boolean zzc2 = true;
        String zzo2 = null;
        String zzo3 = null;
        int zzg = 0;
        int zzg2 = 0;
        String zzo4 = null;
        int zzg3 = 0;
        while (parcel.dataPosition() < zzab) {
            final int zzaa = zza.zzaa(parcel);
            switch (zza.zzbA(zzaa)) {
                default: {
                    zza.zzb(parcel, zzaa);
                    continue;
                }
                case 1: {
                    zzg3 = zza.zzg(parcel, zzaa);
                    continue;
                }
                case 2: {
                    zzo4 = zza.zzo(parcel, zzaa);
                    continue;
                }
                case 3: {
                    zzg2 = zza.zzg(parcel, zzaa);
                    continue;
                }
                case 4: {
                    zzg = zza.zzg(parcel, zzaa);
                    continue;
                }
                case 5: {
                    zzo3 = zza.zzo(parcel, zzaa);
                    continue;
                }
                case 6: {
                    zzo2 = zza.zzo(parcel, zzaa);
                    continue;
                }
                case 7: {
                    zzc2 = zza.zzc(parcel, zzaa);
                    continue;
                }
                case 8: {
                    zzo = zza.zzo(parcel, zzaa);
                    continue;
                }
                case 9: {
                    zzc = zza.zzc(parcel, zzaa);
                    continue;
                }
            }
        }
        if (parcel.dataPosition() != zzab) {
            throw new zza.zza("Overread allowed size end=" + zzab, parcel);
        }
        return new PlayLoggerContext(zzg3, zzo4, zzg2, zzg, zzo3, zzo2, zzc2, zzo, zzc);
    }
    
    public PlayLoggerContext[] zziw(final int n) {
        return new PlayLoggerContext[n];
    }
}
