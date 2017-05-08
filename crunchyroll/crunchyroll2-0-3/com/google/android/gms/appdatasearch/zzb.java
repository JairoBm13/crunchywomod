// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.appdatasearch;

import android.accounts.Account;
import com.google.android.gms.common.internal.safeparcel.zza;
import android.os.Parcelable;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zzb implements Parcelable$Creator<DocumentContents>
{
    static void zza(final DocumentContents documentContents, final Parcel parcel, final int n) {
        final int zzac = com.google.android.gms.common.internal.safeparcel.zzb.zzac(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 1, documentContents.zzMS, n, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1000, documentContents.zzCY);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, documentContents.zzMT, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, documentContents.zzMU);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, (Parcelable)documentContents.account, n, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzH(parcel, zzac);
    }
    
    public DocumentContents[] zzac(final int n) {
        return new DocumentContents[n];
    }
    
    public DocumentContents zzr(final Parcel parcel) {
        boolean zzc = false;
        Account account = null;
        final int zzab = zza.zzab(parcel);
        String zzo = null;
        DocumentSection[] array = null;
        int zzg = 0;
        while (parcel.dataPosition() < zzab) {
            final int zzaa = zza.zzaa(parcel);
            switch (zza.zzbA(zzaa)) {
                default: {
                    zza.zzb(parcel, zzaa);
                    continue;
                }
                case 1: {
                    array = zza.zzb(parcel, zzaa, (android.os.Parcelable$Creator<DocumentSection>)DocumentSection.CREATOR);
                    continue;
                }
                case 1000: {
                    zzg = zza.zzg(parcel, zzaa);
                    continue;
                }
                case 2: {
                    zzo = zza.zzo(parcel, zzaa);
                    continue;
                }
                case 3: {
                    zzc = zza.zzc(parcel, zzaa);
                    continue;
                }
                case 4: {
                    account = zza.zza(parcel, zzaa, (android.os.Parcelable$Creator<Account>)Account.CREATOR);
                    continue;
                }
            }
        }
        if (parcel.dataPosition() != zzab) {
            throw new zza.zza("Overread allowed size end=" + zzab, parcel);
        }
        return new DocumentContents(zzg, array, zzo, zzc, account);
    }
}
