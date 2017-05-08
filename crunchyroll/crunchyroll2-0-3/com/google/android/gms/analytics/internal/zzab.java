// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics.internal;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import android.text.TextUtils;
import java.util.HashMap;
import java.util.Collections;
import com.google.android.gms.common.internal.zzu;
import java.util.Map;
import java.util.List;

public class zzab
{
    private final List<Command> zzLZ;
    private final long zzMa;
    private final long zzMb;
    private final int zzMc;
    private final boolean zzMd;
    private final String zzMe;
    private final Map<String, String> zzyn;
    
    public zzab(final zzc zzc, final Map<String, String> map, final long n, final boolean b) {
        this(zzc, map, n, b, 0L, 0, null);
    }
    
    public zzab(final zzc zzc, final Map<String, String> map, final long n, final boolean b, final long n2, final int n3) {
        this(zzc, map, n, b, n2, n3, null);
    }
    
    public zzab(final zzc zzc, final Map<String, String> map, final long zzMb, final boolean zzMd, final long zzMa, final int zzMc, final List<Command> list) {
        zzu.zzu(zzc);
        zzu.zzu(map);
        this.zzMb = zzMb;
        this.zzMd = zzMd;
        this.zzMa = zzMa;
        this.zzMc = zzMc;
        List<Command> empty_LIST;
        if (list != null) {
            empty_LIST = list;
        }
        else {
            empty_LIST = (List<Command>)Collections.EMPTY_LIST;
        }
        this.zzLZ = empty_LIST;
        this.zzMe = zze(list);
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        for (final Map.Entry<String, String> entry : map.entrySet()) {
            if (zzj(entry.getKey())) {
                final String zza = zza(zzc, entry.getKey());
                if (zza == null) {
                    continue;
                }
                hashMap.put(zza, zzb(zzc, entry.getValue()));
            }
        }
        for (final Map.Entry<String, String> entry2 : map.entrySet()) {
            if (!zzj(entry2.getKey())) {
                final String zza2 = zza(zzc, entry2.getKey());
                if (zza2 == null) {
                    continue;
                }
                hashMap.put(zza2, zzb(zzc, entry2.getValue()));
            }
        }
        if (!TextUtils.isEmpty((CharSequence)this.zzMe)) {
            zzam.zzb(hashMap, "_v", this.zzMe);
            if (this.zzMe.equals("ma4.0.0") || this.zzMe.equals("ma4.0.1")) {
                hashMap.remove("adid");
            }
        }
        this.zzyn = (Map<String, String>)Collections.unmodifiableMap((Map<?, ?>)hashMap);
    }
    
    public static zzab zza(final zzc zzc, final zzab zzab, final Map<String, String> map) {
        return new zzab(zzc, map, zzab.zzjW(), zzab.zzjY(), zzab.zzjV(), zzab.zzjU(), zzab.zzjX());
    }
    
    private static String zza(final zzc zzc, final Object o) {
        String s;
        if (o == null) {
            s = null;
        }
        else {
            String s3;
            final String s2 = s3 = o.toString();
            if (s2.startsWith("&")) {
                s3 = s2.substring(1);
            }
            final int length = s3.length();
            String substring = s3;
            if (length > 256) {
                substring = s3.substring(0, 256);
                zzc.zzc("Hit param name is too long and will be trimmed", length, substring);
            }
            s = substring;
            if (TextUtils.isEmpty((CharSequence)substring)) {
                return null;
            }
        }
        return s;
    }
    
    private static String zzb(final zzc zzc, final Object o) {
        String string;
        if (o == null) {
            string = "";
        }
        else {
            string = o.toString();
        }
        final int length = string.length();
        String substring = string;
        if (length > 8192) {
            substring = string.substring(0, 8192);
            zzc.zzc("Hit param value is too long and will be trimmed", length, substring);
        }
        return substring;
    }
    
    private static String zze(final List<Command> list) {
    Label_0047:
        while (true) {
            if (list != null) {
                for (final Command command : list) {
                    if ("appendVersion".equals(command.getId())) {
                        final String value = command.getValue();
                        break Label_0047;
                    }
                }
            }
            Label_0058: {
                break Label_0058;
                final String value;
                if (TextUtils.isEmpty((CharSequence)value)) {
                    return null;
                }
                return value;
            }
            final String value = null;
            continue Label_0047;
        }
    }
    
    private static boolean zzj(final Object o) {
        return o != null && o.toString().startsWith("&");
    }
    
    private String zzn(String s, final String s2) {
        zzu.zzcj(s);
        zzu.zzb(!s.startsWith("&"), "Short param name required");
        s = this.zzyn.get(s);
        if (s != null) {
            return s;
        }
        return s2;
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("ht=").append(this.zzMb);
        if (this.zzMa != 0L) {
            sb.append(", dbId=").append(this.zzMa);
        }
        if (this.zzMc != 0L) {
            sb.append(", appUID=").append(this.zzMc);
        }
        final ArrayList<String> list = new ArrayList<String>(this.zzyn.keySet());
        Collections.sort((List<Comparable>)list);
        for (final String s : list) {
            sb.append(", ");
            sb.append(s);
            sb.append("=");
            sb.append(this.zzyn.get(s));
        }
        return sb.toString();
    }
    
    public int zzjU() {
        return this.zzMc;
    }
    
    public long zzjV() {
        return this.zzMa;
    }
    
    public long zzjW() {
        return this.zzMb;
    }
    
    public List<Command> zzjX() {
        return this.zzLZ;
    }
    
    public boolean zzjY() {
        return this.zzMd;
    }
    
    public long zzjZ() {
        return zzam.zzbj(this.zzn("_s", "0"));
    }
    
    public String zzka() {
        return this.zzn("_m", "");
    }
    
    public Map<String, String> zzn() {
        return this.zzyn;
    }
}
