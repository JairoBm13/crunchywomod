// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics;

import com.google.android.gms.internal.zznx;
import com.google.android.gms.analytics.internal.zzh;
import com.google.android.gms.analytics.internal.zzc;
import com.google.android.gms.analytics.internal.zzab;
import com.google.android.gms.analytics.internal.zze;
import android.text.TextUtils;
import com.google.android.gms.analytics.internal.zzam;
import com.google.android.gms.analytics.internal.zzb;
import com.google.android.gms.analytics.internal.zzaf;
import com.google.android.gms.analytics.internal.zzk;
import com.google.android.gms.analytics.internal.zza;
import java.util.Iterator;
import com.google.android.gms.common.internal.zzu;
import java.util.Random;
import java.util.HashMap;
import com.google.android.gms.analytics.internal.zzf;
import com.google.android.gms.analytics.internal.zzad;
import java.util.Map;
import com.google.android.gms.analytics.internal.zzd;

public class Tracker extends zzd
{
    private boolean zzIH;
    private final Map<String, String> zzII;
    private final zzad zzIJ;
    private final zza zzIK;
    private final Map<String, String> zzyn;
    
    Tracker(final zzf zzf, final String s, final zzad zzIJ) {
        super(zzf);
        this.zzyn = new HashMap<String, String>();
        this.zzII = new HashMap<String, String>();
        if (s != null) {
            this.zzyn.put("&tid", s);
        }
        this.zzyn.put("useSecure", "1");
        this.zzyn.put("&a", Integer.toString(new Random().nextInt(Integer.MAX_VALUE) + 1));
        if (zzIJ == null) {
            this.zzIJ = new zzad("tracking");
        }
        else {
            this.zzIJ = zzIJ;
        }
        this.zzIK = new zza(zzf);
    }
    
    private static void zza(final Map<String, String> map, final Map<String, String> map2) {
        zzu.zzu(map2);
        if (map != null) {
            for (final Map.Entry<String, String> entry : map.entrySet()) {
                final String zzb = zzb(entry);
                if (zzb != null) {
                    map2.put(zzb, entry.getValue());
                }
            }
        }
    }
    
    private static boolean zza(final Map.Entry<String, String> entry) {
        final String s = entry.getKey();
        final String s2 = entry.getValue();
        return s.startsWith("&") && s.length() >= 2;
    }
    
    private static String zzb(final Map.Entry<String, String> entry) {
        if (!zza(entry)) {
            return null;
        }
        return entry.getKey().substring(1);
    }
    
    private static void zzb(final Map<String, String> map, final Map<String, String> map2) {
        zzu.zzu(map2);
        if (map != null) {
            for (final Map.Entry<String, String> entry : map.entrySet()) {
                final String zzb = zzb(entry);
                if (zzb != null && !map2.containsKey(zzb)) {
                    map2.put(zzb, entry.getValue());
                }
            }
        }
    }
    
    public void send(final Map<String, String> map) {
        final long currentTimeMillis = this.zzhP().currentTimeMillis();
        if (this.zzhg().getAppOptOut()) {
            this.zzaU("AppOptOut is set to true. Not sending Google Analytics hit");
            return;
        }
        final boolean dryRunEnabled = this.zzhg().isDryRunEnabled();
        final HashMap<Object, String> hashMap = new HashMap<Object, String>();
        zza(this.zzyn, (Map<String, String>)hashMap);
        zza(map, (Map<String, String>)hashMap);
        final boolean zze = zzam.zze(this.zzyn.get("useSecure"), true);
        zzb(this.zzII, (Map<String, String>)hashMap);
        this.zzII.clear();
        final String s = hashMap.get("t");
        if (TextUtils.isEmpty((CharSequence)s)) {
            this.zzhQ().zzg((Map<String, String>)hashMap, "Missing hit type parameter");
            return;
        }
        final String s2 = hashMap.get("tid");
        if (TextUtils.isEmpty((CharSequence)s2)) {
            this.zzhQ().zzg((Map<String, String>)hashMap, "Missing tracking id parameter");
            return;
        }
        final boolean zzhp = this.zzhp();
        synchronized (this) {
            if ("screenview".equalsIgnoreCase(s) || "pageview".equalsIgnoreCase(s) || "appview".equalsIgnoreCase(s) || TextUtils.isEmpty((CharSequence)s)) {
                int n;
                if ((n = Integer.parseInt(this.zzyn.get("&a")) + 1) >= Integer.MAX_VALUE) {
                    n = 1;
                }
                this.zzyn.put("&a", Integer.toString(n));
            }
            // monitorexit(this)
            this.zzhS().zze(new Runnable() {
                @Override
                public void run() {
                    boolean b = true;
                    if (Tracker.this.zzIK.zzhq()) {
                        hashMap.put("sc", "start");
                    }
                    zzam.zzc(hashMap, "cid", Tracker.this.zzhg().getClientId());
                    final String s = hashMap.get("sf");
                    if (s != null) {
                        final double zza = zzam.zza(s, 100.0);
                        if (zzam.zza(zza, hashMap.get("cid"))) {
                            Tracker.this.zzb("Sampling enabled. Hit sampled out. sample rate", zza);
                            return;
                        }
                    }
                    final com.google.android.gms.analytics.internal.zza zzb = Tracker.this.zzhW();
                    if (zzhp) {
                        zzam.zzb(hashMap, "ate", zzb.zzhy());
                        zzam.zzb(hashMap, "adid", zzb.zzhC());
                    }
                    else {
                        hashMap.remove("ate");
                        hashMap.remove("adid");
                    }
                    final zznx zzix = Tracker.this.zzhX().zzix();
                    zzam.zzb(hashMap, "an", zzix.zzjL());
                    zzam.zzb(hashMap, "av", zzix.zzjN());
                    zzam.zzb(hashMap, "aid", zzix.zzsK());
                    zzam.zzb(hashMap, "aiid", zzix.zzwi());
                    hashMap.put("v", "1");
                    hashMap.put("_v", com.google.android.gms.analytics.internal.zze.zzJB);
                    zzam.zzb(hashMap, "ul", Tracker.this.zzhY().zzjE().getLanguage());
                    zzam.zzb(hashMap, "sr", Tracker.this.zzhY().zzjF());
                    boolean b2;
                    if (s.equals("transaction") || s.equals("item")) {
                        b2 = true;
                    }
                    else {
                        b2 = false;
                    }
                    if (!b2 && !Tracker.this.zzIJ.zzkb()) {
                        Tracker.this.zzhQ().zzg(hashMap, "Too many hits sent too quickly, rate limiting invoked");
                        return;
                    }
                    long n;
                    if ((n = zzam.zzbj(hashMap.get("ht"))) == 0L) {
                        n = currentTimeMillis;
                    }
                    if (dryRunEnabled) {
                        Tracker.this.zzhQ().zzc("Dry run enabled. Would have sent hit", new zzab(Tracker.this, hashMap, n, zze));
                        return;
                    }
                    final String s2 = hashMap.get("cid");
                    final HashMap<String, String> hashMap = new HashMap<String, String>();
                    zzam.zza(hashMap, "uid", hashMap);
                    zzam.zza(hashMap, "an", hashMap);
                    zzam.zza(hashMap, "aid", hashMap);
                    zzam.zza(hashMap, "av", hashMap);
                    zzam.zza(hashMap, "aiid", hashMap);
                    final String zzIT = s2;
                    if (TextUtils.isEmpty((CharSequence)hashMap.get("adid"))) {
                        b = false;
                    }
                    hashMap.put("_s", String.valueOf(Tracker.this.zzhl().zza(new zzh(0L, s2, zzIT, b, 0L, hashMap))));
                    Tracker.this.zzhl().zza(new zzab(Tracker.this, hashMap, n, zze));
                }
            });
        }
    }
    
    public void set(final String s, final String s2) {
        zzu.zzb(s, "Key should be non-null");
        if (TextUtils.isEmpty((CharSequence)s)) {
            return;
        }
        this.zzyn.put(s, s2);
    }
    
    public void setScreenName(final String s) {
        this.set("&cd", s);
    }
    
    @Override
    protected void zzhn() {
        this.zzIK.zza();
        final String zzjL = this.zzhm().zzjL();
        if (zzjL != null) {
            this.set("&an", zzjL);
        }
        final String zzjN = this.zzhm().zzjN();
        if (zzjN != null) {
            this.set("&av", zzjN);
        }
    }
    
    boolean zzhp() {
        return this.zzIH;
    }
    
    private class zza extends zzd
    {
        private long zzIX;
        private boolean zzIY;
        
        protected zza(final zzf zzf) {
            super(zzf);
            this.zzIX = -1L;
        }
        
        @Override
        protected void zzhn() {
        }
        
        public boolean zzhq() {
            synchronized (this) {
                final boolean zzIY = this.zzIY;
                this.zzIY = false;
                return zzIY;
            }
        }
    }
}
