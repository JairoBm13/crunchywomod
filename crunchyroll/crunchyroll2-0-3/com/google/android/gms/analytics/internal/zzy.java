// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics.internal;

import com.google.android.gms.common.internal.zzd;
import com.google.android.gms.common.internal.zzu;
import com.google.android.gms.internal.zzkf;

public final class zzy
{
    public static zza<Boolean> zzKZ;
    public static zza<String> zzLA;
    public static zza<Integer> zzLB;
    public static zza<Long> zzLC;
    public static zza<Integer> zzLD;
    public static zza<Integer> zzLE;
    public static zza<Long> zzLF;
    public static zza<String> zzLG;
    public static zza<Integer> zzLH;
    public static zza<Boolean> zzLI;
    public static zza<Long> zzLJ;
    public static zza<Long> zzLK;
    public static zza<Long> zzLL;
    public static zza<Long> zzLM;
    public static zza<Long> zzLN;
    public static zza<Long> zzLO;
    public static zza<Long> zzLP;
    public static zza<Boolean> zzLa;
    public static zza<String> zzLb;
    public static zza<Long> zzLc;
    public static zza<Float> zzLd;
    public static zza<Integer> zzLe;
    public static zza<Integer> zzLf;
    public static zza<Integer> zzLg;
    public static zza<Long> zzLh;
    public static zza<Long> zzLi;
    public static zza<Long> zzLj;
    public static zza<Long> zzLk;
    public static zza<Long> zzLl;
    public static zza<Long> zzLm;
    public static zza<Integer> zzLn;
    public static zza<Integer> zzLo;
    public static zza<String> zzLp;
    public static zza<String> zzLq;
    public static zza<String> zzLr;
    public static zza<String> zzLs;
    public static zza<Integer> zzLt;
    public static zza<String> zzLu;
    public static zza<String> zzLv;
    public static zza<Integer> zzLw;
    public static zza<Integer> zzLx;
    public static zza<Integer> zzLy;
    public static zza<Integer> zzLz;
    
    static {
        zzy.zzKZ = zza.zzd("analytics.service_enabled", false);
        zzy.zzLa = zza.zzd("analytics.service_client_enabled", true);
        zzy.zzLb = zza.zzd("analytics.log_tag", "GAv4", "GAv4-SVC");
        zzy.zzLc = zza.zzc("analytics.max_tokens", 60L);
        zzy.zzLd = zza.zza("analytics.tokens_per_sec", 0.5f);
        zzy.zzLe = zza.zza("analytics.max_stored_hits", 2000, 20000);
        zzy.zzLf = zza.zze("analytics.max_stored_hits_per_app", 2000);
        zzy.zzLg = zza.zze("analytics.max_stored_properties_per_app", 100);
        zzy.zzLh = zza.zza("analytics.local_dispatch_millis", 1800000L, 120000L);
        zzy.zzLi = zza.zza("analytics.initial_local_dispatch_millis", 5000L, 5000L);
        zzy.zzLj = zza.zzc("analytics.min_local_dispatch_millis", 120000L);
        zzy.zzLk = zza.zzc("analytics.max_local_dispatch_millis", 7200000L);
        zzy.zzLl = zza.zzc("analytics.dispatch_alarm_millis", 7200000L);
        zzy.zzLm = zza.zzc("analytics.max_dispatch_alarm_millis", 32400000L);
        zzy.zzLn = zza.zze("analytics.max_hits_per_dispatch", 20);
        zzy.zzLo = zza.zze("analytics.max_hits_per_batch", 20);
        zzy.zzLp = zza.zzm("analytics.insecure_host", "http://www.google-analytics.com");
        zzy.zzLq = zza.zzm("analytics.secure_host", "https://ssl.google-analytics.com");
        zzy.zzLr = zza.zzm("analytics.simple_endpoint", "/collect");
        zzy.zzLs = zza.zzm("analytics.batching_endpoint", "/batch");
        zzy.zzLt = zza.zze("analytics.max_get_length", 2036);
        zzy.zzLu = zza.zzd("analytics.batching_strategy.k", zzm.zzKD.name(), zzm.zzKD.name());
        zzy.zzLv = zza.zzm("analytics.compression_strategy.k", zzo.zzKL.name());
        zzy.zzLw = zza.zze("analytics.max_hits_per_request.k", 20);
        zzy.zzLx = zza.zze("analytics.max_hit_length.k", 8192);
        zzy.zzLy = zza.zze("analytics.max_post_length.k", 8192);
        zzy.zzLz = zza.zze("analytics.max_batch_post_length", 8192);
        zzy.zzLA = zza.zzm("analytics.fallback_responses.k", "404,502");
        zzy.zzLB = zza.zze("analytics.batch_retry_interval.seconds.k", 3600);
        zzy.zzLC = zza.zzc("analytics.service_monitor_interval", 86400000L);
        zzy.zzLD = zza.zze("analytics.http_connection.connect_timeout_millis", 60000);
        zzy.zzLE = zza.zze("analytics.http_connection.read_timeout_millis", 61000);
        zzy.zzLF = zza.zzc("analytics.campaigns.time_limit", 86400000L);
        zzy.zzLG = zza.zzm("analytics.first_party_experiment_id", "");
        zzy.zzLH = zza.zze("analytics.first_party_experiment_variant", 0);
        zzy.zzLI = zza.zzd("analytics.test.disable_receiver", false);
        zzy.zzLJ = zza.zza("analytics.service_client.idle_disconnect_millis", 10000L, 10000L);
        zzy.zzLK = zza.zzc("analytics.service_client.connect_timeout_millis", 5000L);
        zzy.zzLL = zza.zzc("analytics.service_client.second_connect_delay_millis", 5000L);
        zzy.zzLM = zza.zzc("analytics.service_client.unexpected_reconnect_millis", 60000L);
        zzy.zzLN = zza.zzc("analytics.service_client.reconnect_throttle_millis", 1800000L);
        zzy.zzLO = zza.zzc("analytics.monitoring.sample_period_millis", 86400000L);
        zzy.zzLP = zza.zzc("analytics.initialization_warning_threshold", 5000L);
    }
    
    public static final class zza<V>
    {
        private final V zzLQ;
        private final zzkf<V> zzLR;
        private V zzLS;
        
        private zza(final zzkf<V> zzLR, final V zzLQ) {
            zzu.zzu(zzLR);
            this.zzLR = zzLR;
            this.zzLQ = zzLQ;
        }
        
        static zza<Float> zza(final String s, final float n) {
            return zza(s, n, n);
        }
        
        static zza<Float> zza(final String s, final float n, final float n2) {
            return new zza<Float>(zzkf.zza(s, Float.valueOf(n2)), n);
        }
        
        static zza<Integer> zza(final String s, final int n, final int n2) {
            return new zza<Integer>(zzkf.zza(s, Integer.valueOf(n2)), n);
        }
        
        static zza<Long> zza(final String s, final long n, final long n2) {
            return new zza<Long>(zzkf.zza(s, Long.valueOf(n2)), n);
        }
        
        static zza<Boolean> zza(final String s, final boolean b, final boolean b2) {
            return new zza<Boolean>(zzkf.zzg(s, b2), b);
        }
        
        static zza<Long> zzc(final String s, final long n) {
            return zza(s, n, n);
        }
        
        static zza<String> zzd(final String s, final String s2, final String s3) {
            return new zza<String>(zzkf.zzs(s, s3), s2);
        }
        
        static zza<Boolean> zzd(final String s, final boolean b) {
            return zza(s, b, b);
        }
        
        static zza<Integer> zze(final String s, final int n) {
            return zza(s, n, n);
        }
        
        static zza<String> zzm(final String s, final String s2) {
            return zzd(s, s2, s2);
        }
        
        public V get() {
            if (this.zzLS != null) {
                return this.zzLS;
            }
            if (zzd.zzZR && zzkf.isInitialized()) {
                return this.zzLR.zzmZ();
            }
            return this.zzLQ;
        }
    }
}
