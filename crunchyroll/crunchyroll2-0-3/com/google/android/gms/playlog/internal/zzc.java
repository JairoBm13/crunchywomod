// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.playlog.internal;

import android.os.Bundle;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zzc implements Parcelable$Creator<LogEvent>
{
    static void zza(final LogEvent logEvent, final Parcel parcel, int zzac) {
        zzac = zzb.zzac(parcel);
        zzb.zzc(parcel, 1, logEvent.versionCode);
        zzb.zza(parcel, 2, logEvent.zzaGF);
        zzb.zza(parcel, 3, logEvent.tag, false);
        zzb.zza(parcel, 4, logEvent.zzaGG, false);
        zzb.zza(parcel, 5, logEvent.zzaGH, false);
        zzb.zzH(parcel, zzac);
    }
    
    public LogEvent zzfG(final Parcel parcel) {
        Bundle zzq = null;
        final int zzab = zza.zzab(parcel);
        int zzg = 0;
        long zzi = 0L;
        byte[] zzr = null;
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
                    zzi = zza.zzi(parcel, zzaa);
                    continue;
                }
                case 3: {
                    zzo = zza.zzo(parcel, zzaa);
                    continue;
                }
                case 4: {
                    zzr = zza.zzr(parcel, zzaa);
                    continue;
                }
                case 5: {
                    zzq = zza.zzq(parcel, zzaa);
                    continue;
                }
            }
        }
        if (parcel.dataPosition() != zzab) {
            throw new zza.zza("Overread allowed size end=" + zzab, parcel);
        }
        return new LogEvent(zzg, zzi, zzo, zzr, zzq);
    }
    
    public LogEvent[] zziv(final int n) {
        return new LogEvent[n];
    }
}
