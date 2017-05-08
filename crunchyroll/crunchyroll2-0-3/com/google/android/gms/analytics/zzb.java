// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import com.google.android.gms.analytics.internal.zzab;
import com.google.android.gms.analytics.internal.zzh;
import com.google.android.gms.analytics.internal.zze;
import com.google.android.gms.analytics.internal.zzam;
import com.google.android.gms.analytics.ecommerce.ProductAction;
import java.util.Iterator;
import com.google.android.gms.internal.zznx;
import com.google.android.gms.internal.zznz;
import java.util.List;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.Promotion;
import com.google.android.gms.internal.zzoa;
import com.google.android.gms.internal.zzin;
import com.google.android.gms.internal.zzim;
import com.google.android.gms.internal.zzof;
import com.google.android.gms.internal.zzoe;
import com.google.android.gms.internal.zzoc;
import com.google.android.gms.internal.zzny;
import com.google.android.gms.internal.zzob;
import com.google.android.gms.internal.zzod;
import com.google.android.gms.internal.zzip;
import com.google.android.gms.internal.zzio;
import java.util.HashMap;
import com.google.android.gms.internal.zzno;
import android.net.Uri$Builder;
import android.text.TextUtils;
import java.util.Map;
import com.google.android.gms.common.internal.zzu;
import android.net.Uri;
import com.google.android.gms.analytics.internal.zzf;
import java.text.DecimalFormat;
import com.google.android.gms.internal.zznu;
import com.google.android.gms.analytics.internal.zzc;

public class zzb extends zzc implements zznu
{
    private static DecimalFormat zzIk;
    private final zzf zzIa;
    private final Uri zzIl;
    private final boolean zzIm;
    private final boolean zzIn;
    private final String zztd;
    
    public zzb(final zzf zzf, final String s) {
        this(zzf, s, true, false);
    }
    
    public zzb(final zzf zzIa, final String zztd, final boolean zzIm, final boolean zzIn) {
        super(zzIa);
        zzu.zzcj(zztd);
        this.zzIa = zzIa;
        this.zztd = zztd;
        this.zzIm = zzIm;
        this.zzIn = zzIn;
        this.zzIl = zzaK(this.zztd);
    }
    
    static String zza(final double n) {
        if (zzb.zzIk == null) {
            zzb.zzIk = new DecimalFormat("0.######");
        }
        return zzb.zzIk.format(n);
    }
    
    private static void zza(final Map<String, String> map, final String s, final double n) {
        if (n != 0.0) {
            map.put(s, zza(n));
        }
    }
    
    private static void zza(final Map<String, String> map, final String s, final int n, final int n2) {
        if (n > 0 && n2 > 0) {
            map.put(s, n + "x" + n2);
        }
    }
    
    private static void zza(final Map<String, String> map, final String s, final String s2) {
        if (!TextUtils.isEmpty((CharSequence)s2)) {
            map.put(s, s2);
        }
    }
    
    private static void zza(final Map<String, String> map, final String s, final boolean b) {
        if (b) {
            map.put(s, "1");
        }
    }
    
    static Uri zzaK(final String s) {
        zzu.zzcj(s);
        final Uri$Builder uri$Builder = new Uri$Builder();
        uri$Builder.scheme("uri");
        uri$Builder.authority("google-analytics.com");
        uri$Builder.path(s);
        return uri$Builder.build();
    }
    
    public static Map<String, String> zzc(final zzno zzno) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        final zzio zzio = zzno.zzd(zzio.class);
        if (zzio != null) {
            for (final Map.Entry<String, Object> entry : zzio.zzhv().entrySet()) {
                final String zzh = zzh(entry.getValue());
                if (zzh != null) {
                    hashMap.put(entry.getKey(), zzh);
                }
            }
        }
        final zzip zzip = zzno.zzd(zzip.class);
        if (zzip != null) {
            zza(hashMap, "t", zzip.zzhw());
            zza(hashMap, "cid", zzip.getClientId());
            zza(hashMap, "uid", zzip.getUserId());
            zza(hashMap, "sc", zzip.zzhz());
            zza(hashMap, "sf", zzip.zzhB());
            zza(hashMap, "ni", zzip.zzhA());
            zza(hashMap, "adid", zzip.zzhx());
            zza(hashMap, "ate", zzip.zzhy());
        }
        final zzod zzod = zzno.zzd(zzod.class);
        if (zzod != null) {
            zza(hashMap, "cd", zzod.zzwB());
            zza(hashMap, "a", zzod.zzbn());
            zza(hashMap, "dr", zzod.zzwE());
        }
        final zzob zzob = zzno.zzd(zzob.class);
        if (zzob != null) {
            zza(hashMap, "ec", zzob.zzwy());
            zza(hashMap, "ea", zzob.getAction());
            zza(hashMap, "el", zzob.getLabel());
            zza(hashMap, "ev", zzob.getValue());
        }
        final zzny zzny = zzno.zzd(zzny.class);
        if (zzny != null) {
            zza(hashMap, "cn", zzny.getName());
            zza(hashMap, "cs", zzny.getSource());
            zza(hashMap, "cm", zzny.zzwj());
            zza(hashMap, "ck", zzny.zzwk());
            zza(hashMap, "cc", zzny.getContent());
            zza(hashMap, "ci", zzny.getId());
            zza(hashMap, "anid", zzny.zzwl());
            zza(hashMap, "gclid", zzny.zzwm());
            zza(hashMap, "dclid", zzny.zzwn());
            zza(hashMap, "aclid", zzny.zzwo());
        }
        final zzoc zzoc = zzno.zzd(zzoc.class);
        if (zzoc != null) {
            zza(hashMap, "exd", zzoc.getDescription());
            zza(hashMap, "exf", zzoc.zzwz());
        }
        final zzoe zzoe = zzno.zzd(zzoe.class);
        if (zzoe != null) {
            zza(hashMap, "sn", zzoe.zzwI());
            zza(hashMap, "sa", zzoe.getAction());
            zza(hashMap, "st", zzoe.getTarget());
        }
        final zzof zzof = zzno.zzd(zzof.class);
        if (zzof != null) {
            zza(hashMap, "utv", zzof.zzwJ());
            zza(hashMap, "utt", zzof.getTimeInMillis());
            zza(hashMap, "utc", zzof.zzwy());
            zza(hashMap, "utl", zzof.getLabel());
        }
        final zzim zzim = zzno.zzd(zzim.class);
        if (zzim != null) {
            for (final Map.Entry<Integer, String> entry2 : zzim.zzht().entrySet()) {
                final String zzO = com.google.android.gms.analytics.zzc.zzO(entry2.getKey());
                if (!TextUtils.isEmpty((CharSequence)zzO)) {
                    hashMap.put(zzO, entry2.getValue());
                }
            }
        }
        final zzin zzin = zzno.zzd(zzin.class);
        if (zzin != null) {
            for (final Map.Entry<Integer, Double> entry3 : zzin.zzhu().entrySet()) {
                final String zzQ = com.google.android.gms.analytics.zzc.zzQ(entry3.getKey());
                if (!TextUtils.isEmpty((CharSequence)zzQ)) {
                    hashMap.put(zzQ, zza(entry3.getValue()));
                }
            }
        }
        final zzoa zzoa = zzno.zzd(zzoa.class);
        if (zzoa != null) {
            final ProductAction zzwu = zzoa.zzwu();
            if (zzwu != null) {
                for (final Map.Entry<String, String> entry4 : zzwu.build().entrySet()) {
                    if (entry4.getKey().startsWith("&")) {
                        hashMap.put(entry4.getKey().substring(1), entry4.getValue());
                    }
                    else {
                        hashMap.put(entry4.getKey(), entry4.getValue());
                    }
                }
            }
            final Iterator<Promotion> iterator5 = zzoa.zzwx().iterator();
            int n = 1;
            while (iterator5.hasNext()) {
                hashMap.putAll((Map<?, ?>)iterator5.next().zzaQ(com.google.android.gms.analytics.zzc.zzU(n)));
                ++n;
            }
            final Iterator<Product> iterator6 = zzoa.zzwv().iterator();
            int n2 = 1;
            while (iterator6.hasNext()) {
                hashMap.putAll((Map<?, ?>)iterator6.next().zzaQ(com.google.android.gms.analytics.zzc.zzS(n2)));
                ++n2;
            }
            final Iterator<Map.Entry<String, List<Product>>> iterator7 = zzoa.zzww().entrySet().iterator();
            int n3 = 1;
            while (iterator7.hasNext()) {
                final Map.Entry<String, List<Product>> entry5 = iterator7.next();
                final List<Product> list = entry5.getValue();
                final String zzX = com.google.android.gms.analytics.zzc.zzX(n3);
                final Iterator<Product> iterator8 = list.iterator();
                int n4 = 1;
                while (iterator8.hasNext()) {
                    hashMap.putAll((Map<?, ?>)iterator8.next().zzaQ(zzX + com.google.android.gms.analytics.zzc.zzV(n4)));
                    ++n4;
                }
                if (!TextUtils.isEmpty((CharSequence)entry5.getKey())) {
                    hashMap.put(zzX + "nm", entry5.getKey());
                }
                ++n3;
            }
        }
        final zznz zznz = zzno.zzd(zznz.class);
        if (zznz != null) {
            zza(hashMap, "ul", zznz.getLanguage());
            zza(hashMap, "sd", zznz.zzwp());
            zza(hashMap, "sr", zznz.zzwq(), zznz.zzwr());
            zza(hashMap, "vp", zznz.zzws(), zznz.zzwt());
        }
        final zznx zznx = zzno.zzd(zznx.class);
        if (zznx != null) {
            zza(hashMap, "an", zznx.zzjL());
            zza(hashMap, "aid", zznx.zzsK());
            zza(hashMap, "aiid", zznx.zzwi());
            zza(hashMap, "av", zznx.zzjN());
        }
        return hashMap;
    }
    
    private static String zzh(final Object o) {
        String s;
        if (o == null) {
            s = null;
        }
        else if (o instanceof String) {
            if (TextUtils.isEmpty((CharSequence)(s = (String)o))) {
                return null;
            }
        }
        else if (o instanceof Double) {
            final Double n = (Double)o;
            if (n != 0.0) {
                return zza(n);
            }
            return null;
        }
        else {
            if (!(o instanceof Boolean)) {
                return String.valueOf(o);
            }
            if (o != Boolean.FALSE) {
                return "1";
            }
            return null;
        }
        return s;
    }
    
    private static String zzz(final Map<String, String> map) {
        final StringBuilder sb = new StringBuilder();
        for (final Map.Entry<String, String> entry : map.entrySet()) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
        }
        return sb.toString();
    }
    
    @Override
    public void zzb(final zzno zzno) {
        zzu.zzu(zzno);
        zzu.zzb(zzno.zzvU(), "Can't deliver not submitted measurement");
        zzu.zzbZ("deliver should be called on worker thread");
        final zzno zzvP = zzno.zzvP();
        final zzip zzip = zzvP.zze(zzip.class);
        if (TextUtils.isEmpty((CharSequence)zzip.zzhw())) {
            this.zzhQ().zzg(zzc(zzvP), "Ignoring measurement without type");
        }
        else {
            if (TextUtils.isEmpty((CharSequence)zzip.getClientId())) {
                this.zzhQ().zzg(zzc(zzvP), "Ignoring measurement without client id");
                return;
            }
            if (!this.zzIa.zzie().getAppOptOut()) {
                final double zzhB = zzip.zzhB();
                if (zzam.zza(zzhB, zzip.getClientId())) {
                    this.zzb("Sampling enabled. Hit sampled out. sampling rate", zzhB);
                    return;
                }
                final Map<String, String> zzc = zzc(zzvP);
                zzc.put("v", "1");
                zzc.put("_v", zze.zzJB);
                zzc.put("tid", this.zztd);
                if (this.zzIa.zzie().isDryRunEnabled()) {
                    this.zzc("Dry run is enabled. GoogleAnalytics would have sent", zzz(zzc));
                    return;
                }
                final HashMap<String, String> hashMap = new HashMap<String, String>();
                zzam.zzb(hashMap, "uid", zzip.getUserId());
                final zznx zznx = zzno.zzd(zznx.class);
                if (zznx != null) {
                    zzam.zzb(hashMap, "an", zznx.zzjL());
                    zzam.zzb(hashMap, "aid", zznx.zzsK());
                    zzam.zzb(hashMap, "av", zznx.zzjN());
                    zzam.zzb(hashMap, "aiid", zznx.zzwi());
                }
                zzc.put("_s", String.valueOf(this.zzhl().zza(new zzh(0L, zzip.getClientId(), this.zztd, !TextUtils.isEmpty((CharSequence)zzip.zzhx()), 0L, hashMap))));
                this.zzhl().zza(new zzab(this.zzhQ(), zzc, zzno.zzvS(), true));
            }
        }
    }
    
    @Override
    public Uri zzhe() {
        return this.zzIl;
    }
}
