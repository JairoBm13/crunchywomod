// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.cast.internal;

import java.io.IOException;
import android.text.TextUtils;

public abstract class zzd
{
    protected final zzl zzUi;
    private final String zzUj;
    private zzn zzUk;
    
    protected zzd(final String zzUj, final String s, final String sessionLabel) {
        zzf.zzbD(zzUj);
        this.zzUj = zzUj;
        this.zzUi = new zzl(s);
        this.setSessionLabel(sessionLabel);
    }
    
    public final String getNamespace() {
        return this.zzUj;
    }
    
    public void setSessionLabel(final String s) {
        if (!TextUtils.isEmpty((CharSequence)s)) {
            this.zzUi.zzbJ(s);
        }
    }
    
    public final void zza(final zzn zzUk) {
        this.zzUk = zzUk;
        if (this.zzUk == null) {
            this.zzlJ();
        }
    }
    
    protected final void zza(final String s, final long n, final String s2) throws IOException {
        this.zzUi.zza("Sending text message: %s to: %s", s, s2);
        this.zzUk.zza(this.zzUj, s, n, s2);
    }
    
    public void zzb(final long n, final int n2) {
    }
    
    public void zzbB(final String s) {
    }
    
    public void zzlJ() {
    }
    
    protected final long zzlK() {
        return this.zzUk.zzlu();
    }
}
