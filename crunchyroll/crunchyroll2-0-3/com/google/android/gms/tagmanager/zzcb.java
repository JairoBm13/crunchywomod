// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.tagmanager;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import android.net.Uri;

class zzcb
{
    private static zzcb zzaME;
    private volatile String zzaKy;
    private volatile zza zzaMF;
    private volatile String zzaMG;
    private volatile String zzaMH;
    
    zzcb() {
        this.clear();
    }
    
    private String zzeA(final String s) {
        return s.split("&")[0].split("=")[1];
    }
    
    private String zzm(final Uri uri) {
        return uri.getQuery().replace("&gtm_debug=x", "");
    }
    
    static zzcb zzzf() {
        synchronized (zzcb.class) {
            if (zzcb.zzaME == null) {
                zzcb.zzaME = new zzcb();
            }
            return zzcb.zzaME;
        }
    }
    
    void clear() {
        this.zzaMF = zza.zzaMI;
        this.zzaMG = null;
        this.zzaKy = null;
        this.zzaMH = null;
    }
    
    String getContainerId() {
        return this.zzaKy;
    }
    
    boolean zzl(final Uri uri) {
        while (true) {
            boolean b = true;
            String decode;
            synchronized (this) {
                while (true) {
                    while (true) {
                        try {
                            decode = URLDecoder.decode(uri.toString(), "UTF-8");
                            if (!decode.matches("^tagmanager.c.\\S+:\\/\\/preview\\/p\\?id=\\S+&gtm_auth=\\S+&gtm_preview=\\d+(&gtm_debug=x)?$")) {
                                break;
                            }
                            zzbg.zzaB("Container preview url: " + decode);
                            if (decode.matches(".*?&gtm_debug=x$")) {
                                this.zzaMF = zza.zzaMK;
                                this.zzaMH = this.zzm(uri);
                                if (this.zzaMF == zza.zzaMJ || this.zzaMF == zza.zzaMK) {
                                    this.zzaMG = "/r?" + this.zzaMH;
                                }
                                this.zzaKy = this.zzeA(this.zzaMH);
                                return b;
                            }
                        }
                        catch (UnsupportedEncodingException ex) {
                            b = false;
                            return b;
                        }
                        this.zzaMF = zza.zzaMJ;
                        continue;
                    }
                }
            }
            if (!decode.matches("^tagmanager.c.\\S+:\\/\\/preview\\/p\\?id=\\S+&gtm_preview=$")) {
                zzbg.zzaC("Invalid preview uri: " + decode);
                b = false;
                return b;
            }
            final Uri uri2;
            if (this.zzeA(uri2.getQuery()).equals(this.zzaKy)) {
                zzbg.zzaB("Exit preview mode for container: " + this.zzaKy);
                this.zzaMF = zza.zzaMI;
                this.zzaMG = null;
                return b;
            }
            b = false;
            return b;
        }
    }
    
    zza zzzg() {
        return this.zzaMF;
    }
    
    String zzzh() {
        return this.zzaMG;
    }
    
    enum zza
    {
        zzaMI, 
        zzaMJ, 
        zzaMK;
    }
}
