// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics.internal;

import android.util.Pair;
import java.util.UUID;
import com.google.android.gms.common.internal.zzu;
import android.content.SharedPreferences$Editor;
import android.text.TextUtils;
import android.content.SharedPreferences;

public class zzai extends zzd
{
    private SharedPreferences zzMw;
    private long zzMx;
    private long zzMy;
    private final zza zzMz;
    
    protected zzai(final zzf zzf) {
        super(zzf);
        this.zzMy = -1L;
        this.zzMz = new zza("monitoring", this.zzhR().zzjC());
    }
    
    public void zzbf(final String s) {
        this.zzhO();
        this.zzia();
        final SharedPreferences$Editor edit = this.zzMw.edit();
        if (TextUtils.isEmpty((CharSequence)s)) {
            edit.remove("installation_campaign");
        }
        else {
            edit.putString("installation_campaign", s);
        }
        if (!edit.commit()) {
            this.zzaW("Failed to commit campaign data");
        }
    }
    
    @Override
    protected void zzhn() {
        this.zzMw = this.getContext().getSharedPreferences("com.google.android.gms.analytics.prefs", 0);
    }
    
    public long zzkk() {
        this.zzhO();
        this.zzia();
        if (this.zzMx == 0L) {
            final long long1 = this.zzMw.getLong("first_run", 0L);
            if (long1 != 0L) {
                this.zzMx = long1;
            }
            else {
                final long currentTimeMillis = this.zzhP().currentTimeMillis();
                final SharedPreferences$Editor edit = this.zzMw.edit();
                edit.putLong("first_run", currentTimeMillis);
                if (!edit.commit()) {
                    this.zzaW("Failed to commit first run time");
                }
                this.zzMx = currentTimeMillis;
            }
        }
        return this.zzMx;
    }
    
    public zzaj zzkl() {
        return new zzaj(this.zzhP(), this.zzkk());
    }
    
    public long zzkm() {
        this.zzhO();
        this.zzia();
        if (this.zzMy == -1L) {
            this.zzMy = this.zzMw.getLong("last_dispatch", 0L);
        }
        return this.zzMy;
    }
    
    public void zzkn() {
        this.zzhO();
        this.zzia();
        final long currentTimeMillis = this.zzhP().currentTimeMillis();
        final SharedPreferences$Editor edit = this.zzMw.edit();
        edit.putLong("last_dispatch", currentTimeMillis);
        edit.apply();
        this.zzMy = currentTimeMillis;
    }
    
    public String zzko() {
        this.zzhO();
        this.zzia();
        final String string = this.zzMw.getString("installation_campaign", (String)null);
        if (TextUtils.isEmpty((CharSequence)string)) {
            return null;
        }
        return string;
    }
    
    public zza zzkp() {
        return this.zzMz;
    }
    
    public final class zza
    {
        private final String mName;
        private final long zzMA;
        
        private zza(final String mName, final long zzMA) {
            zzu.zzcj(mName);
            zzu.zzV(zzMA > 0L);
            this.mName = mName;
            this.zzMA = zzMA;
        }
        
        private void zzkq() {
            final long currentTimeMillis = zzai.this.zzhP().currentTimeMillis();
            final SharedPreferences$Editor edit = zzai.this.zzMw.edit();
            edit.remove(this.zzkv());
            edit.remove(this.zzkw());
            edit.putLong(this.zzku(), currentTimeMillis);
            edit.commit();
        }
        
        private long zzkr() {
            final long zzkt = this.zzkt();
            if (zzkt == 0L) {
                return 0L;
            }
            return Math.abs(zzkt - zzai.this.zzhP().currentTimeMillis());
        }
        
        private long zzkt() {
            return zzai.this.zzMw.getLong(this.zzku(), 0L);
        }
        
        private String zzku() {
            return this.mName + ":start";
        }
        
        private String zzkv() {
            return this.mName + ":count";
        }
        
        public void zzbg(final String s) {
            if (this.zzkt() == 0L) {
                this.zzkq();
            }
            String s2;
            if ((s2 = s) == null) {
                s2 = "";
            }
            while (true) {
                while (true) {
                    synchronized (this) {
                        final long long1 = zzai.this.zzMw.getLong(this.zzkv(), 0L);
                        if (long1 <= 0L) {
                            final SharedPreferences$Editor edit = zzai.this.zzMw.edit();
                            edit.putString(this.zzkw(), s2);
                            edit.putLong(this.zzkv(), 1L);
                            edit.apply();
                            return;
                        }
                        if ((UUID.randomUUID().getLeastSignificantBits() & Long.MAX_VALUE) < Long.MAX_VALUE / (long1 + 1L)) {
                            final int n = 1;
                            final SharedPreferences$Editor edit2 = zzai.this.zzMw.edit();
                            if (n != 0) {
                                edit2.putString(this.zzkw(), s2);
                            }
                            edit2.putLong(this.zzkv(), long1 + 1L);
                            edit2.apply();
                            return;
                        }
                    }
                    final int n = 0;
                    continue;
                }
            }
        }
        
        public Pair<String, Long> zzks() {
            final long zzkr = this.zzkr();
            if (zzkr >= this.zzMA) {
                if (zzkr > this.zzMA * 2L) {
                    this.zzkq();
                    return null;
                }
                final String string = zzai.this.zzMw.getString(this.zzkw(), (String)null);
                final long long1 = zzai.this.zzMw.getLong(this.zzkv(), 0L);
                this.zzkq();
                if (string != null && long1 > 0L) {
                    return (Pair<String, Long>)new Pair((Object)string, (Object)long1);
                }
            }
            return null;
        }
        
        protected String zzkw() {
            return this.mName + ":value";
        }
    }
}
