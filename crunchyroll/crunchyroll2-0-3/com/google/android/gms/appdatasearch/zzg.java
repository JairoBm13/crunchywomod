// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.appdatasearch;

import java.util.ArrayList;
import java.util.List;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.safeparcel.zza;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zzg implements Parcelable$Creator<GetRecentContextCall.Response>
{
    static void zza(final GetRecentContextCall.Response response, final Parcel parcel, final int n) {
        final int zzac = zzb.zzac(parcel);
        zzb.zzc(parcel, 1000, response.zzCY);
        zzb.zza(parcel, 1, (Parcelable)response.zzNn, n, false);
        zzb.zzc(parcel, 2, response.zzNo, false);
        zzb.zza(parcel, 3, response.zzNp, false);
        zzb.zzH(parcel, zzac);
    }
    
    public GetRecentContextCall.Response[] zzah(final int n) {
        return new GetRecentContextCall.Response[n];
    }
    
    public GetRecentContextCall.Response zzw(final Parcel parcel) {
        String[] zzA = null;
        final int zzab = zza.zzab(parcel);
        int zzg = 0;
        ArrayList<UsageInfo> list = null;
        Status status = null;
        while (parcel.dataPosition() < zzab) {
            final int zzaa = zza.zzaa(parcel);
            Status status2 = null;
            ArrayList<UsageInfo> list3 = null;
            switch (zza.zzbA(zzaa)) {
                default: {
                    zza.zzb(parcel, zzaa);
                    final ArrayList<UsageInfo> list2 = list;
                    status2 = status;
                    list3 = list2;
                    break;
                }
                case 1000: {
                    zzg = zza.zzg(parcel, zzaa);
                    final Status status3 = status;
                    list3 = list;
                    status2 = status3;
                    break;
                }
                case 1: {
                    final Status status4 = zza.zza(parcel, zzaa, Status.CREATOR);
                    list3 = list;
                    status2 = status4;
                    break;
                }
                case 2: {
                    final ArrayList<UsageInfo> zzc = zza.zzc(parcel, zzaa, (android.os.Parcelable$Creator<UsageInfo>)UsageInfo.CREATOR);
                    status2 = status;
                    list3 = zzc;
                    break;
                }
                case 3: {
                    zzA = zza.zzA(parcel, zzaa);
                    final Status status5 = status;
                    list3 = list;
                    status2 = status5;
                    break;
                }
            }
            final Status status6 = status2;
            list = list3;
            status = status6;
        }
        if (parcel.dataPosition() != zzab) {
            throw new zza.zza("Overread allowed size end=" + zzab, parcel);
        }
        return new GetRecentContextCall.Response(zzg, status, list, zzA);
    }
}
