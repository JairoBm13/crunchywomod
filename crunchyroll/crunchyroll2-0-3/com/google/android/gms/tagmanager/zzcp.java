// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.tagmanager;

import java.util.List;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.HashMap;
import com.google.android.gms.internal.zzae;
import java.util.Set;
import com.google.android.gms.internal.zzqf;
import java.util.Map;
import com.google.android.gms.internal.zzag;

class zzcp
{
    private static final zzbw<zzag.zza> zzaNe;
    private final DataLayer zzaKz;
    private final zzah zzaNg;
    private final Map<String, zzak> zzaNh;
    private final Map<String, zzak> zzaNi;
    private final Map<String, zzak> zzaNj;
    private final zzl<zzqf.zza, zzbw<zzag.zza>> zzaNk;
    private final zzl<String, zzb> zzaNl;
    private final Set<zzqf.zze> zzaNm;
    private final Map<String, zzc> zzaNn;
    private volatile String zzaNo;
    private int zzaNp;
    
    static {
        zzaNe = new zzbw<zzag.zza>(zzdf.zzzQ(), true);
    }
    
    private zzbw<zzag.zza> zza(final zzag.zza zza, final Set<String> set, final zzdi zzdi) {
        if (!zza.zzjb) {
            return new zzbw<zzag.zza>(zza, true);
        }
        switch (zza.type) {
            default: {
                zzbg.zzaz("Unknown type: " + zza.type);
                return zzcp.zzaNe;
            }
            case 2: {
                final zzag.zza zzo = zzqf.zzo(zza);
                zzo.zziS = new zzag.zza[zza.zziS.length];
                for (int i = 0; i < zza.zziS.length; ++i) {
                    final zzbw<zzag.zza> zza2 = this.zza(zza.zziS[i], set, zzdi.zziU(i));
                    if (zza2 == zzcp.zzaNe) {
                        return zzcp.zzaNe;
                    }
                    zzo.zziS[i] = (zzag.zza)zza2.getObject();
                }
                return new zzbw<zzag.zza>(zzo, false);
            }
            case 3: {
                final zzag.zza zzo2 = zzqf.zzo(zza);
                if (zza.zziT.length != zza.zziU.length) {
                    zzbg.zzaz("Invalid serving value: " + zza.toString());
                    return zzcp.zzaNe;
                }
                zzo2.zziT = new zzag.zza[zza.zziT.length];
                zzo2.zziU = new zzag.zza[zza.zziT.length];
                for (int j = 0; j < zza.zziT.length; ++j) {
                    final zzbw<zzag.zza> zza3 = this.zza(zza.zziT[j], set, zzdi.zziV(j));
                    final zzbw<zzag.zza> zza4 = this.zza(zza.zziU[j], set, zzdi.zziW(j));
                    if (zza3 == zzcp.zzaNe || zza4 == zzcp.zzaNe) {
                        return zzcp.zzaNe;
                    }
                    zzo2.zziT[j] = (zzag.zza)zza3.getObject();
                    zzo2.zziU[j] = (zzag.zza)zza4.getObject();
                }
                return new zzbw<zzag.zza>(zzo2, false);
            }
            case 4: {
                if (set.contains(zza.zziV)) {
                    zzbg.zzaz("Macro cycle detected.  Current macro reference: " + zza.zziV + "." + "  Previous macro references: " + set.toString() + ".");
                    return zzcp.zzaNe;
                }
                set.add(zza.zziV);
                final zzbw<zzag.zza> zza5 = zzdj.zza(this.zza(zza.zziV, set, zzdi.zzyZ()), zza.zzja);
                set.remove(zza.zziV);
                return zza5;
            }
            case 7: {
                final zzag.zza zzo3 = zzqf.zzo(zza);
                zzo3.zziZ = new zzag.zza[zza.zziZ.length];
                for (int k = 0; k < zza.zziZ.length; ++k) {
                    final zzbw<zzag.zza> zza6 = this.zza(zza.zziZ[k], set, zzdi.zziX(k));
                    if (zza6 == zzcp.zzaNe) {
                        return zzcp.zzaNe;
                    }
                    zzo3.zziZ[k] = (zzag.zza)zza6.getObject();
                }
                return new zzbw<zzag.zza>(zzo3, false);
            }
        }
    }
    
    private zzbw<zzag.zza> zza(final String s, final Set<String> set, final zzbj zzbj) {
        ++this.zzaNp;
        final zzb zzb = this.zzaNl.get(s);
        if (zzb != null && !this.zzaNg.zzyL()) {
            this.zza(zzb.zzzs(), set);
            --this.zzaNp;
            return zzb.zzzr();
        }
        final zzc zzc = this.zzaNn.get(s);
        if (zzc == null) {
            zzbg.zzaz(this.zzzq() + "Invalid macro: " + s);
            --this.zzaNp;
            return zzcp.zzaNe;
        }
        final zzbw<Set<zzqf.zza>> zza = this.zza(s, zzc.zzzt(), zzc.zzzu(), zzc.zzzv(), zzc.zzzx(), zzc.zzzw(), set, zzbj.zzyB());
        zzqf.zza zzzy;
        if (zza.getObject().isEmpty()) {
            zzzy = zzc.zzzy();
        }
        else {
            if (zza.getObject().size() > 1) {
                zzbg.zzaC(this.zzzq() + "Multiple macros active for macroName " + s);
            }
            zzzy = zza.getObject().iterator().next();
        }
        if (zzzy == null) {
            --this.zzaNp;
            return zzcp.zzaNe;
        }
        final zzbw<zzag.zza> zza2 = this.zza(this.zzaNj, zzzy, set, zzbj.zzyR());
        final boolean b = zza.zzza() && zza2.zzza();
        zzbw<zzag.zza> zzaNe;
        if (zza2 == zzcp.zzaNe) {
            zzaNe = zzcp.zzaNe;
        }
        else {
            zzaNe = new zzbw<zzag.zza>(zza2.getObject(), b);
        }
        final zzag.zza zzzs = zzzy.zzzs();
        if (zzaNe.zzza()) {
            this.zzaNl.zzf(s, new zzb(zzaNe, zzzs));
        }
        this.zza(zzzs, set);
        --this.zzaNp;
        return zzaNe;
    }
    
    private zzbw<zzag.zza> zza(final Map<String, zzak> map, final zzqf.zza zza, final Set<String> set, final zzch zzch) {
        boolean b = true;
        final zzag.zza zza2 = zza.zzAn().get(zzae.zzfg.toString());
        zzbw<zzag.zza> zzaNe;
        if (zza2 == null) {
            zzbg.zzaz("No function id in properties");
            zzaNe = zzcp.zzaNe;
        }
        else {
            final String zziW = zza2.zziW;
            final zzak zzak = map.get(zziW);
            if (zzak == null) {
                zzbg.zzaz(zziW + " has no backing implementation.");
                return zzcp.zzaNe;
            }
            zzaNe = this.zzaNk.get(zza);
            if (zzaNe == null || this.zzaNg.zzyL()) {
                final HashMap<String, Object> hashMap = new HashMap<String, Object>();
                final Iterator<Map.Entry<String, zzag.zza>> iterator = zza.zzAn().entrySet().iterator();
                boolean b2 = true;
                while (iterator.hasNext()) {
                    final Map.Entry<String, zzag.zza> entry = iterator.next();
                    final zzbw<zzag.zza> zza3 = this.zza(entry.getValue(), set, zzch.zzez(entry.getKey()).zze(entry.getValue()));
                    if (zza3 == zzcp.zzaNe) {
                        return zzcp.zzaNe;
                    }
                    if (zza3.zzza()) {
                        zza.zza(entry.getKey(), zza3.getObject());
                    }
                    else {
                        b2 = false;
                    }
                    hashMap.put(entry.getKey(), zza3.getObject());
                }
                if (!zzak.zzg(hashMap.keySet())) {
                    zzbg.zzaz("Incorrect keys for function " + zziW + " required " + zzak.zzyN() + " had " + hashMap.keySet());
                    return zzcp.zzaNe;
                }
                if (!b2 || !zzak.zzyh()) {
                    b = false;
                }
                final zzbw zzbw = new zzbw<zzag.zza>(zzak.zzE((Map<String, zzag.zza>)hashMap), b);
                if (b) {
                    this.zzaNk.zzf(zza, (zzbw<zzag.zza>)zzbw);
                }
                zzch.zzd(zzbw.getObject());
                return (zzbw<zzag.zza>)zzbw;
            }
        }
        return zzaNe;
    }
    
    private zzbw<Set<zzqf.zza>> zza(final Set<zzqf.zze> set, final Set<String> set2, final zza zza, final zzco zzco) {
        final HashSet<zzqf.zza> set3 = new HashSet<zzqf.zza>();
        final HashSet<zzqf.zza> set4 = new HashSet<zzqf.zza>();
        final Iterator<zzqf.zze> iterator = set.iterator();
        boolean b = true;
        while (iterator.hasNext()) {
            final zzqf.zze zze = iterator.next();
            final zzck zzyY = zzco.zzyY();
            final zzbw<Boolean> zza2 = this.zza(zze, set2, zzyY);
            if (zza2.getObject()) {
                zza.zza(zze, set3, set4, zzyY);
            }
            b = (b && zza2.zzza());
        }
        set3.removeAll(set4);
        zzco.zzh(set3);
        return new zzbw<Set<zzqf.zza>>(set3, b);
    }
    
    private void zza(final zzag.zza zza, final Set<String> set) {
        if (zza != null) {
            final zzbw<zzag.zza> zza2 = this.zza(zza, set, new zzbu());
            if (zza2 != zzcp.zzaNe) {
                final Object zzl = zzdf.zzl(zza2.getObject());
                if (zzl instanceof Map) {
                    this.zzaKz.push((Map<String, Object>)zzl);
                    return;
                }
                if (!(zzl instanceof List)) {
                    zzbg.zzaC("pushAfterEvaluate: value not a Map or List");
                    return;
                }
                for (final Map<String, Object> next : (List<Object>)zzl) {
                    if (next instanceof Map) {
                        this.zzaKz.push(next);
                    }
                    else {
                        zzbg.zzaC("pushAfterEvaluate: value not a Map");
                    }
                }
            }
        }
    }
    
    private String zzzq() {
        if (this.zzaNp <= 1) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(Integer.toString(this.zzaNp));
        for (int i = 2; i < this.zzaNp; ++i) {
            sb.append(' ');
        }
        sb.append(": ");
        return sb.toString();
    }
    
    zzbw<Boolean> zza(final zzqf.zza zza, final Set<String> set, final zzch zzch) {
        final zzbw<zzag.zza> zza2 = this.zza(this.zzaNi, zza, set, zzch);
        final Boolean zzk = zzdf.zzk(zza2.getObject());
        zzch.zzd(zzdf.zzI(zzk));
        return new zzbw<Boolean>(zzk, zza2.zzza());
    }
    
    zzbw<Boolean> zza(final zzqf.zze zze, final Set<String> set, final zzck zzck) {
        final Iterator<zzqf.zza> iterator = zze.zzAv().iterator();
        boolean b = true;
        while (iterator.hasNext()) {
            final zzbw<Boolean> zza = this.zza(iterator.next(), set, zzck.zzyS());
            if (zza.getObject()) {
                zzck.zzf(zzdf.zzI(false));
                return new zzbw<Boolean>(false, zza.zzza());
            }
            b = (b && zza.zzza());
        }
        final Iterator<zzqf.zza> iterator2 = zze.zzAu().iterator();
        while (iterator2.hasNext()) {
            final zzbw<Boolean> zza2 = this.zza(iterator2.next(), set, zzck.zzyT());
            if (!zza2.getObject()) {
                zzck.zzf(zzdf.zzI(false));
                return new zzbw<Boolean>(false, zza2.zzza());
            }
            b = (b && zza2.zzza());
        }
        zzck.zzf(zzdf.zzI(true));
        return new zzbw<Boolean>(true, b);
    }
    
    zzbw<Set<zzqf.zza>> zza(final String s, final Set<zzqf.zze> set, final Map<zzqf.zze, List<zzqf.zza>> map, final Map<zzqf.zze, List<String>> map2, final Map<zzqf.zze, List<zzqf.zza>> map3, final Map<zzqf.zze, List<String>> map4, final Set<String> set2, final zzco zzco) {
        return this.zza(set, set2, (zza)new zza() {
            @Override
            public void zza(final zzqf.zze zze, final Set<zzqf.zza> set, final Set<zzqf.zza> set2, final zzck zzck) {
                final List<? extends zzqf.zza> list = map.get(zze);
                final List<String> list2 = map2.get(zze);
                if (list != null) {
                    set.addAll(list);
                    zzck.zzyU().zzc((List<zzqf.zza>)list, list2);
                }
                final List<? extends zzqf.zza> list3 = map3.get(zze);
                final List<String> list4 = map4.get(zze);
                if (list3 != null) {
                    set2.addAll(list3);
                    zzck.zzyV().zzc((List<zzqf.zza>)list3, list4);
                }
            }
        }, zzco);
    }
    
    zzbw<Set<zzqf.zza>> zza(final Set<zzqf.zze> set, final zzco zzco) {
        return this.zza(set, new HashSet<String>(), (zza)new zza() {
            @Override
            public void zza(final zzqf.zze zze, final Set<zzqf.zza> set, final Set<zzqf.zza> set2, final zzck zzck) {
                set.addAll(zze.zzAw());
                set2.addAll(zze.zzAx());
                zzck.zzyW().zzc(zze.zzAw(), zze.zzAB());
                zzck.zzyX().zzc(zze.zzAx(), zze.zzAC());
            }
        }, zzco);
    }
    
    void zzeE(final String zzaNo) {
        synchronized (this) {
            this.zzaNo = zzaNo;
        }
    }
    
    public void zzeh(final String s) {
        synchronized (this) {
            this.zzeE(s);
            final zzu zzyJ = this.zzaNg.zzeu(s).zzyJ();
            final Iterator<zzqf.zza> iterator = this.zza(this.zzaNm, zzyJ.zzyB()).getObject().iterator();
            while (iterator.hasNext()) {
                this.zza(this.zzaNh, iterator.next(), new HashSet<String>(), zzyJ.zzyA());
            }
        }
        final com.google.android.gms.tagmanager.zzag zzag;
        zzag.zzyK();
        this.zzeE(null);
    }
    // monitorexit(this)
    
    interface zza
    {
        void zza(final zzqf.zze p0, final Set<zzqf.zza> p1, final Set<zzqf.zza> p2, final zzck p3);
    }
    
    private static class zzb
    {
        private zzbw<zzag.zza> zzaNv;
        private zzag.zza zzaNw;
        
        public zzb(final zzbw<zzag.zza> zzaNv, final zzag.zza zzaNw) {
            this.zzaNv = zzaNv;
            this.zzaNw = zzaNw;
        }
        
        public zzbw<zzag.zza> zzzr() {
            return this.zzaNv;
        }
        
        public zzag.zza zzzs() {
            return this.zzaNw;
        }
    }
    
    private static class zzc
    {
        private final Map<zzqf.zze, List<String>> zzaNA;
        private zzqf.zza zzaNB;
        private final Set<zzqf.zze> zzaNm;
        private final Map<zzqf.zze, List<zzqf.zza>> zzaNx;
        private final Map<zzqf.zze, List<zzqf.zza>> zzaNy;
        private final Map<zzqf.zze, List<String>> zzaNz;
        
        public zzc() {
            this.zzaNm = new HashSet<zzqf.zze>();
            this.zzaNx = new HashMap<zzqf.zze, List<zzqf.zza>>();
            this.zzaNz = new HashMap<zzqf.zze, List<String>>();
            this.zzaNy = new HashMap<zzqf.zze, List<zzqf.zza>>();
            this.zzaNA = new HashMap<zzqf.zze, List<String>>();
        }
        
        public Set<zzqf.zze> zzzt() {
            return this.zzaNm;
        }
        
        public Map<zzqf.zze, List<zzqf.zza>> zzzu() {
            return this.zzaNx;
        }
        
        public Map<zzqf.zze, List<String>> zzzv() {
            return this.zzaNz;
        }
        
        public Map<zzqf.zze, List<String>> zzzw() {
            return this.zzaNA;
        }
        
        public Map<zzqf.zze, List<zzqf.zza>> zzzx() {
            return this.zzaNy;
        }
        
        public zzqf.zza zzzy() {
            return this.zzaNB;
        }
    }
}
