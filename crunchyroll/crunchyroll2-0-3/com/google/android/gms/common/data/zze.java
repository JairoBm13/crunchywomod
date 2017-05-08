// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.data;

import android.os.Bundle;
import android.database.CursorWindow;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zze implements Parcelable$Creator<DataHolder>
{
    static void zza(final DataHolder dataHolder, final Parcel parcel, final int n) {
        final int zzac = zzb.zzac(parcel);
        zzb.zza(parcel, 1, dataHolder.zzng(), false);
        zzb.zzc(parcel, 1000, dataHolder.getVersionCode());
        zzb.zza(parcel, 2, dataHolder.zznh(), n, false);
        zzb.zzc(parcel, 3, dataHolder.getStatusCode());
        zzb.zza(parcel, 4, dataHolder.zznb(), false);
        zzb.zzH(parcel, zzac);
    }
    
    public DataHolder zzS(final Parcel parcel) {
        int zzg = 0;
        Bundle zzq = null;
        final int zzab = zza.zzab(parcel);
        CursorWindow[] array = null;
        String[] zzA = null;
        int zzg2 = 0;
        while (parcel.dataPosition() < zzab) {
            final int zzaa = zza.zzaa(parcel);
            switch (zza.zzbA(zzaa)) {
                default: {
                    zza.zzb(parcel, zzaa);
                    continue;
                }
                case 1: {
                    zzA = zza.zzA(parcel, zzaa);
                    continue;
                }
                case 1000: {
                    zzg2 = zza.zzg(parcel, zzaa);
                    continue;
                }
                case 2: {
                    array = zza.zzb(parcel, zzaa, (android.os.Parcelable$Creator<CursorWindow>)CursorWindow.CREATOR);
                    continue;
                }
                case 3: {
                    zzg = zza.zzg(parcel, zzaa);
                    continue;
                }
                case 4: {
                    zzq = zza.zzq(parcel, zzaa);
                    continue;
                }
            }
        }
        if (parcel.dataPosition() != zzab) {
            throw new zza.zza("Overread allowed size end=" + zzab, parcel);
        }
        final DataHolder dataHolder = new DataHolder(zzg2, zzA, array, zzg, zzq);
        dataHolder.zznf();
        return dataHolder;
    }
    
    public DataHolder[] zzbj(final int n) {
        return new DataHolder[n];
    }
}
