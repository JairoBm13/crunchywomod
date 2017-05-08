// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics.internal;

public class zzz extends zzq<zzaa>
{
    public zzz(final zzf zzf) {
        super(zzf, (zzq.zza)new zza(zzf));
    }
    
    private static class zza implements zzq.zza<zzaa>
    {
        private final zzf zzJy;
        private final zzaa zzLT;
        
        public zza(final zzf zzJy) {
            this.zzJy = zzJy;
            this.zzLT = new zzaa();
        }
        
        @Override
        public void zzc(final String s, final boolean b) {
            if ("ga_dryRun".equals(s)) {
                final zzaa zzLT = this.zzLT;
                int zzLY;
                if (b) {
                    zzLY = 1;
                }
                else {
                    zzLY = 0;
                }
                zzLT.zzLY = zzLY;
                return;
            }
            this.zzJy.zzhQ().zzd("Bool xml configuration name not recognized", s);
        }
        
        @Override
        public void zzd(final String s, final int zzLX) {
            if ("ga_dispatchPeriod".equals(s)) {
                this.zzLT.zzLX = zzLX;
                return;
            }
            this.zzJy.zzhQ().zzd("Int xml configuration name not recognized", s);
        }
        
        public zzaa zzjJ() {
            return this.zzLT;
        }
        
        @Override
        public void zzk(final String s, final String s2) {
        }
        
        @Override
        public void zzl(final String s, final String zzLW) {
            if ("ga_appName".equals(s)) {
                this.zzLT.zzLU = zzLW;
                return;
            }
            if ("ga_appVersion".equals(s)) {
                this.zzLT.zzLV = zzLW;
                return;
            }
            if ("ga_logLevel".equals(s)) {
                this.zzLT.zzLW = zzLW;
                return;
            }
            this.zzJy.zzhQ().zzd("String xml configuration name not recognized", s);
        }
    }
}
