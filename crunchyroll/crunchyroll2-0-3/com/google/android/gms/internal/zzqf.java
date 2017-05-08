// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import java.util.List;
import java.util.Collections;
import java.util.Map;

public class zzqf
{
    public static zzag.zza zzo(final zzag.zza zza) {
        final zzag.zza zza2 = new zzag.zza();
        zza2.type = zza.type;
        zza2.zzja = zza.zzja.clone();
        if (zza.zzjb) {
            zza2.zzjb = zza.zzjb;
        }
        return zza2;
    }
    
    public static class zza
    {
        private final zzag.zza zzaNw;
        private final Map<String, zzag.zza> zzaPy;
        
        @Override
        public String toString() {
            return "Properties: " + this.zzAn() + " pushAfterEvaluate: " + this.zzaNw;
        }
        
        public Map<String, zzag.zza> zzAn() {
            return Collections.unmodifiableMap((Map<? extends String, ? extends zzag.zza>)this.zzaPy);
        }
        
        public void zza(final String s, final zzag.zza zza) {
            this.zzaPy.put(s, zza);
        }
        
        public zzag.zza zzzs() {
            return this.zzaNw;
        }
    }
    
    public static class zze
    {
        private final List<zza> zzaPC;
        private final List<zza> zzaPD;
        private final List<zza> zzaPE;
        private final List<zza> zzaPF;
        private final List<zza> zzaPG;
        private final List<zza> zzaPH;
        private final List<String> zzaPK;
        private final List<String> zzaPL;
        
        @Override
        public String toString() {
            return "Positive predicates: " + this.zzAu() + "  Negative predicates: " + this.zzAv() + "  Add tags: " + this.zzAw() + "  Remove tags: " + this.zzAx() + "  Add macros: " + this.zzAy() + "  Remove macros: " + this.zzAD();
        }
        
        public List<String> zzAB() {
            return this.zzaPK;
        }
        
        public List<String> zzAC() {
            return this.zzaPL;
        }
        
        public List<zza> zzAD() {
            return this.zzaPH;
        }
        
        public List<zza> zzAu() {
            return this.zzaPC;
        }
        
        public List<zza> zzAv() {
            return this.zzaPD;
        }
        
        public List<zza> zzAw() {
            return this.zzaPE;
        }
        
        public List<zza> zzAx() {
            return this.zzaPF;
        }
        
        public List<zza> zzAy() {
            return this.zzaPG;
        }
    }
}
