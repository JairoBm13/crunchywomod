// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics.internal;

public abstract class zzd extends zzc
{
    private boolean zzJA;
    private boolean zzJz;
    
    protected zzd(final zzf zzf) {
        super(zzf);
    }
    
    public boolean isInitialized() {
        return this.zzJz && !this.zzJA;
    }
    
    public void zza() {
        this.zzhn();
        this.zzJz = true;
    }
    
    protected abstract void zzhn();
    
    protected void zzia() {
        if (!this.isInitialized()) {
            throw new IllegalStateException("Not initialized");
        }
    }
}
