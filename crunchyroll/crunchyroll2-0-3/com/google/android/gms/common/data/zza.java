// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.data;

import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zza implements Parcelable$Creator<BitmapTeleporter>
{
    static void zza(final BitmapTeleporter bitmapTeleporter, final Parcel parcel, final int n) {
        final int zzac = zzb.zzac(parcel);
        zzb.zzc(parcel, 1, bitmapTeleporter.zzCY);
        zzb.zza(parcel, 2, (Parcelable)bitmapTeleporter.zzCZ, n, false);
        zzb.zzc(parcel, 3, bitmapTeleporter.zzSq);
        zzb.zzH(parcel, zzac);
    }
    
    public BitmapTeleporter zzR(final Parcel parcel) {
        int zzg = 0;
        final int zzab = com.google.android.gms.common.internal.safeparcel.zza.zzab(parcel);
        ParcelFileDescriptor parcelFileDescriptor = null;
        int zzg2 = 0;
        while (parcel.dataPosition() < zzab) {
            final int zzaa = com.google.android.gms.common.internal.safeparcel.zza.zzaa(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzbA(zzaa)) {
                default: {
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, zzaa);
                    continue;
                }
                case 1: {
                    zzg2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzaa);
                    continue;
                }
                case 2: {
                    parcelFileDescriptor = com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, zzaa, (android.os.Parcelable$Creator<ParcelFileDescriptor>)ParcelFileDescriptor.CREATOR);
                    continue;
                }
                case 3: {
                    zzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzaa);
                    continue;
                }
            }
        }
        if (parcel.dataPosition() != zzab) {
            throw new com.google.android.gms.common.internal.safeparcel.zza.zza("Overread allowed size end=" + zzab, parcel);
        }
        return new BitmapTeleporter(zzg2, parcelFileDescriptor, zzg);
    }
    
    public BitmapTeleporter[] zzbe(final int n) {
        return new BitmapTeleporter[n];
    }
}
