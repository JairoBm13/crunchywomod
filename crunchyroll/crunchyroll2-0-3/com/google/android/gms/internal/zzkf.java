// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import android.os.Binder;

public abstract class zzkf<T>
{
    private static zza zzYj;
    private static int zzYk;
    private static String zzYl;
    private static final Object zzoW;
    private T zzLS;
    protected final String zztw;
    protected final T zztx;
    
    static {
        zzoW = new Object();
        zzkf.zzYj = null;
        zzkf.zzYk = 0;
        zzkf.zzYl = "com.google.android.providers.gsf.permission.READ_GSERVICES";
    }
    
    protected zzkf(final String zztw, final T zztx) {
        this.zzLS = null;
        this.zztw = zztw;
        this.zztx = zztx;
    }
    
    public static boolean isInitialized() {
        return zzkf.zzYj != null;
    }
    
    public static zzkf<Float> zza(final String s, final Float n) {
        return new zzkf<Float>(s, n) {
            protected Float zzbT(final String s) {
                return zzkf.zzYj.zzb(this.zztw, (Float)this.zztx);
            }
        };
    }
    
    public static zzkf<Integer> zza(final String s, final Integer n) {
        return new zzkf<Integer>(s, n) {
            protected Integer zzbS(final String s) {
                return zzkf.zzYj.zzb(this.zztw, (Integer)this.zztx);
            }
        };
    }
    
    public static zzkf<Long> zza(final String s, final Long n) {
        return new zzkf<Long>(s, n) {
            protected Long zzbR(final String s) {
                return zzkf.zzYj.getLong(this.zztw, (Long)this.zztx);
            }
        };
    }
    
    public static zzkf<Boolean> zzg(final String s, final boolean b) {
        return new zzkf<Boolean>(s, Boolean.valueOf(b)) {
            protected Boolean zzbQ(final String s) {
                return zzkf.zzYj.zzb(this.zztw, (Boolean)this.zztx);
            }
        };
    }
    
    public static int zzmY() {
        return zzkf.zzYk;
    }
    
    public static zzkf<String> zzs(final String s, final String s2) {
        return new zzkf<String>(s, s2) {
            protected String zzbU(final String s) {
                return zzkf.zzYj.getString(this.zztw, (String)this.zztx);
            }
        };
    }
    
    public final T get() {
        if (this.zzLS != null) {
            return this.zzLS;
        }
        return this.zzbP(this.zztw);
    }
    
    protected abstract T zzbP(final String p0);
    
    public final T zzmZ() {
        final long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.get();
        }
        finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }
    
    private interface zza
    {
        Long getLong(final String p0, final Long p1);
        
        String getString(final String p0, final String p1);
        
        Boolean zzb(final String p0, final Boolean p1);
        
        Float zzb(final String p0, final Float p1);
        
        Integer zzb(final String p0, final Integer p1);
    }
}
