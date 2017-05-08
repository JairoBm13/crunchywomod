// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Api;

public final class zzpt implements Optional
{
    public static final zzpt zzaJQ;
    private final boolean zzaJR;
    private final boolean zzaJS;
    private final String zzaJT;
    private final GoogleApiClient.ServerAuthCodeCallbacks zzaJU;
    
    static {
        zzaJQ = new zza().zzyc();
    }
    
    private zzpt(final boolean zzaJR, final boolean zzaJS, final String zzaJT, final GoogleApiClient.ServerAuthCodeCallbacks zzaJU) {
        this.zzaJR = zzaJR;
        this.zzaJS = zzaJS;
        this.zzaJT = zzaJT;
        this.zzaJU = zzaJU;
    }
    
    public boolean zzxZ() {
        return this.zzaJR;
    }
    
    public String zzxt() {
        return this.zzaJT;
    }
    
    public boolean zzya() {
        return this.zzaJS;
    }
    
    public GoogleApiClient.ServerAuthCodeCallbacks zzyb() {
        return this.zzaJU;
    }
    
    public static final class zza
    {
        private String zzaHb;
        private boolean zzaJV;
        private boolean zzaJW;
        private GoogleApiClient.ServerAuthCodeCallbacks zzaJX;
        
        public zzpt zzyc() {
            return new zzpt(this.zzaJV, this.zzaJW, this.zzaHb, this.zzaJX, null);
        }
    }
}
