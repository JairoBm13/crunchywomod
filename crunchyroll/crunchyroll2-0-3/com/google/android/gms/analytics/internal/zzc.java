// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics.internal;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.internal.zzns;
import com.google.android.gms.internal.zzlb;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.internal.zzu;

public class zzc
{
    private final zzf zzJy;
    
    protected zzc(final zzf zzJy) {
        zzu.zzu(zzJy);
        this.zzJy = zzJy;
    }
    
    private void zza(final int n, final String s, final Object o, final Object o2, final Object o3) {
        zzaf zzid = null;
        if (this.zzJy != null) {
            zzid = this.zzJy.zzid();
        }
        if (zzid != null) {
            zzid.zza(n, s, o, o2, o3);
        }
        else {
            final String s2 = zzy.zzLb.get();
            if (Log.isLoggable(s2, n)) {
                Log.println(n, s2, zzc(s, o, o2, o3));
            }
        }
    }
    
    protected static String zzc(String s, final Object o, final Object o2, final Object o3) {
        String s2 = s;
        if (s == null) {
            s2 = "";
        }
        final String zzi = zzi(o);
        final String zzi2 = zzi(o2);
        final String zzi3 = zzi(o3);
        final StringBuilder sb = new StringBuilder();
        s = "";
        if (!TextUtils.isEmpty((CharSequence)s2)) {
            sb.append(s2);
            s = ": ";
        }
        String s3 = s;
        if (!TextUtils.isEmpty((CharSequence)zzi)) {
            sb.append(s);
            sb.append(zzi);
            s3 = ", ";
        }
        s = s3;
        if (!TextUtils.isEmpty((CharSequence)zzi2)) {
            sb.append(s3);
            sb.append(zzi2);
            s = ", ";
        }
        if (!TextUtils.isEmpty((CharSequence)zzi3)) {
            sb.append(s);
            sb.append(zzi3);
        }
        return sb.toString();
    }
    
    private static String zzi(final Object o) {
        if (o == null) {
            return "";
        }
        if (o instanceof String) {
            return (String)o;
        }
        if (o instanceof Boolean) {
            String s;
            if (o == Boolean.TRUE) {
                s = "true";
            }
            else {
                s = "false";
            }
            return s;
        }
        if (o instanceof Throwable) {
            return ((Throwable)o).toString();
        }
        return o.toString();
    }
    
    protected Context getContext() {
        return this.zzJy.getContext();
    }
    
    public void zza(final String s, final Object o) {
        this.zza(2, s, o, null, null);
    }
    
    public void zza(final String s, final Object o, final Object o2) {
        this.zza(2, s, o, o2, null);
    }
    
    public void zza(final String s, final Object o, final Object o2, final Object o3) {
        this.zza(3, s, o, o2, o3);
    }
    
    public void zzaT(final String s) {
        this.zza(2, s, null, null, null);
    }
    
    public void zzaU(final String s) {
        this.zza(3, s, null, null, null);
    }
    
    public void zzaV(final String s) {
        this.zza(4, s, null, null, null);
    }
    
    public void zzaW(final String s) {
        this.zza(5, s, null, null, null);
    }
    
    public void zzaX(final String s) {
        this.zza(6, s, null, null, null);
    }
    
    public void zzb(final String s, final Object o) {
        this.zza(3, s, o, null, null);
    }
    
    public void zzb(final String s, final Object o, final Object o2) {
        this.zza(3, s, o, o2, null);
    }
    
    public void zzb(final String s, final Object o, final Object o2, final Object o3) {
        this.zza(5, s, o, o2, o3);
    }
    
    public void zzc(final String s, final Object o) {
        this.zza(4, s, o, null, null);
    }
    
    public void zzc(final String s, final Object o, final Object o2) {
        this.zza(5, s, o, o2, null);
    }
    
    public void zzd(final String s, final Object o) {
        this.zza(5, s, o, null, null);
    }
    
    public void zzd(final String s, final Object o, final Object o2) {
        this.zza(6, s, o, o2, null);
    }
    
    public void zze(final String s, final Object o) {
        this.zza(6, s, o, null, null);
    }
    
    public zzf zzhM() {
        return this.zzJy;
    }
    
    protected void zzhN() {
        if (this.zzhR().zziW()) {
            throw new IllegalStateException("Call only supported on the client side");
        }
    }
    
    protected void zzhO() {
        this.zzJy.zzhO();
    }
    
    protected zzlb zzhP() {
        return this.zzJy.zzhP();
    }
    
    protected zzaf zzhQ() {
        return this.zzJy.zzhQ();
    }
    
    protected zzr zzhR() {
        return this.zzJy.zzhR();
    }
    
    protected zzns zzhS() {
        return this.zzJy.zzhS();
    }
    
    protected zzv zzhT() {
        return this.zzJy.zzhT();
    }
    
    protected zzai zzhU() {
        return this.zzJy.zzhU();
    }
    
    protected zzn zzhV() {
        return this.zzJy.zzih();
    }
    
    protected zza zzhW() {
        return this.zzJy.zzig();
    }
    
    protected zzk zzhX() {
        return this.zzJy.zzhX();
    }
    
    protected com.google.android.gms.analytics.internal.zzu zzhY() {
        return this.zzJy.zzhY();
    }
    
    public boolean zzhZ() {
        return Log.isLoggable((String)zzy.zzLb.get(), 2);
    }
    
    public GoogleAnalytics zzhg() {
        return this.zzJy.zzie();
    }
    
    protected zzb zzhl() {
        return this.zzJy.zzhl();
    }
    
    protected zzan zzhm() {
        return this.zzJy.zzhm();
    }
}
