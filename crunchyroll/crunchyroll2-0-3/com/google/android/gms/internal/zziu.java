// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import java.util.Iterator;
import android.util.Log;
import java.util.ArrayList;
import com.google.android.gms.appdatasearch.DocumentContents;
import android.content.Intent;
import android.accounts.Account;
import com.google.android.gms.appindexing.AppIndexApi;
import java.util.List;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.gms.appdatasearch.UsageInfo;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appdatasearch.RegisterSectionInfo;
import com.google.android.gms.appdatasearch.DocumentSection;

public class zziu
{
    private static DocumentSection zza(final String s, final zznj.zzc zzc) {
        return new DocumentSection(zzrn.zzf(zzc), new RegisterSectionInfo.zza(s).zzJ(true).zzbs(s).zzbr("blob").zzkM());
    }
    
    public static UsageInfo zza(final Action action, final long n, final String s, final int n2) {
        boolean boolean1 = false;
        final Bundle bundle = new Bundle();
        bundle.putAll(action.zzkP());
        final Bundle bundle2 = bundle.getBundle("object");
        Uri parse;
        if (bundle2.containsKey("id")) {
            parse = Uri.parse(bundle2.getString("id"));
        }
        else {
            parse = null;
        }
        final String string = bundle2.getString("name");
        final String string2 = bundle2.getString("type");
        final Intent zza = zziv.zza(s, Uri.parse(bundle2.getString("url")));
        final DocumentContents.zza zza2 = UsageInfo.zza(zza, string, parse, string2, null);
        if (bundle.containsKey(".private:ssbContext")) {
            zza2.zza(DocumentSection.zzh(bundle.getByteArray(".private:ssbContext")));
            bundle.remove(".private:ssbContext");
        }
        if (bundle.containsKey(".private:accountName")) {
            zza2.zza(new Account(bundle.getString(".private:accountName"), "com.google"));
            bundle.remove(".private:accountName");
        }
        int n3;
        if (bundle.containsKey(".private:isContextOnly") && bundle.getBoolean(".private:isContextOnly")) {
            n3 = 4;
            bundle.remove(".private:isContextOnly");
        }
        else {
            n3 = 0;
        }
        if (bundle.containsKey(".private:isDeviceOnly")) {
            boolean1 = bundle.getBoolean(".private:isDeviceOnly", false);
            bundle.remove(".private:isDeviceOnly");
        }
        zza2.zza(zza(".private:action", zze(bundle)));
        return new UsageInfo.zza().zza(UsageInfo.zza(s, zza)).zzw(n).zzal(n3).zza(zza2.zzkJ()).zzL(boolean1).zzam(n2).zzkN();
    }
    
    static zznj.zzc zze(final Bundle bundle) {
        final zznj.zzc zzc = new zznj.zzc();
        final ArrayList<zznj.zzb> list = new ArrayList<zznj.zzb>();
        for (final String name : bundle.keySet()) {
            final Object value = bundle.get(name);
            final zznj.zzb zzb = new zznj.zzb();
            zzb.name = name;
            zzb.zzawp = new zznj.zzd();
            if (value instanceof String) {
                zzb.zzawp.zzabE = (String)value;
            }
            else if (value instanceof Bundle) {
                zzb.zzawp.zzawu = zze((Bundle)value);
            }
            else {
                Log.e("AppDataSearchClient", "Unsupported value: " + value);
            }
            list.add(zzb);
        }
        if (bundle.containsKey("type")) {
            zzc.type = bundle.getString("type");
        }
        zzc.zzawq = list.toArray(new zznj.zzb[list.size()]);
        return zzc;
    }
}
