// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.stats;

import com.google.android.gms.internal.zzkf;

public final class zzc
{
    public static zzkf<Boolean> zzacr;
    public static zzkf<Integer> zzacs;
    
    static {
        zzc.zzacr = zzkf.zzg("gms:common:stats:debug", false);
        zzc.zzacs = zzkf.zza("gms:common:stats:max_num_of_events", Integer.valueOf(100));
    }
    
    public static final class zza
    {
        public static zzkf<Integer> zzact;
        public static zzkf<String> zzacu;
        public static zzkf<String> zzacv;
        public static zzkf<String> zzacw;
        public static zzkf<String> zzacx;
        public static zzkf<Long> zzacy;
        
        static {
            zza.zzact = zzkf.zza("gms:common:stats:connections:level", Integer.valueOf(zzd.zzacz));
            zza.zzacu = zzkf.zzs("gms:common:stats:connections:ignored_calling_processes", "");
            zza.zzacv = zzkf.zzs("gms:common:stats:connections:ignored_calling_services", "");
            zza.zzacw = zzkf.zzs("gms:common:stats:connections:ignored_target_processes", "");
            zza.zzacx = zzkf.zzs("gms:common:stats:connections:ignored_target_services", "com.google.android.gms.auth.GetToken");
            zza.zzacy = zzkf.zza("gms:common:stats:connections:time_out_duration", Long.valueOf(600000L));
        }
    }
}
