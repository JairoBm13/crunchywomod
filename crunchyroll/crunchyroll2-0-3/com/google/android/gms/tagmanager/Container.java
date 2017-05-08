// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.tagmanager;

public class Container
{
    private zzcp zzaKA;
    private final String zzaKy;
    
    private zzcp zzyn() {
        synchronized (this) {
            return this.zzaKA;
        }
    }
    
    public String getContainerId() {
        return this.zzaKy;
    }
    
    void release() {
        this.zzaKA = null;
    }
    
    void zzeh(final String s) {
        this.zzyn().zzeh(s);
    }
}
