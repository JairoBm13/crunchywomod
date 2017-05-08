// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.cast.internal;

import java.util.Locale;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.internal.zzu;

public class zzl
{
    private static boolean zzUT;
    protected final String mTag;
    private final boolean zzUU;
    private boolean zzUV;
    private boolean zzUW;
    private String zzUX;
    
    static {
        zzl.zzUT = false;
    }
    
    public zzl(final String s) {
        this(s, zzmb());
    }
    
    public zzl(final String mTag, final boolean zzUV) {
        zzu.zzh(mTag, "The log tag cannot be null or empty.");
        this.mTag = mTag;
        this.zzUU = (mTag.length() <= 23);
        this.zzUV = zzUV;
        this.zzUW = false;
    }
    
    public static boolean zzmb() {
        return zzl.zzUT;
    }
    
    public void zza(final String s, final Object... array) {
        if (this.zzma()) {
            Log.v(this.mTag, this.zzg(s, array));
        }
    }
    
    public void zzb(final String s, final Object... array) {
        if (this.zzlZ() || zzl.zzUT) {
            Log.d(this.mTag, this.zzg(s, array));
        }
    }
    
    public void zzb(final Throwable t, final String s, final Object... array) {
        if (this.zzlZ() || zzl.zzUT) {
            Log.d(this.mTag, this.zzg(s, array), t);
        }
    }
    
    public void zzbJ(String format) {
        if (TextUtils.isEmpty((CharSequence)format)) {
            format = null;
        }
        else {
            format = String.format("[%s] ", format);
        }
        this.zzUX = format;
    }
    
    public void zzc(final String s, final Object... array) {
        Log.e(this.mTag, this.zzg(s, array));
    }
    
    public void zze(final String s, final Object... array) {
        Log.i(this.mTag, this.zzg(s, array));
    }
    
    public void zzf(final String s, final Object... array) {
        Log.w(this.mTag, this.zzg(s, array));
    }
    
    protected String zzg(String format, final Object... array) {
        if (array.length != 0) {
            format = String.format(Locale.ROOT, format, array);
        }
        String string = format;
        if (!TextUtils.isEmpty((CharSequence)this.zzUX)) {
            string = this.zzUX + format;
        }
        return string;
    }
    
    public boolean zzlZ() {
        return this.zzUV || (this.zzUU && Log.isLoggable(this.mTag, 3));
    }
    
    public boolean zzma() {
        return this.zzUW;
    }
}
