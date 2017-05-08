// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.tagmanager;

import java.util.Collection;
import com.google.android.gms.internal.zzag;
import java.util.Map;
import java.util.Set;

abstract class zzak
{
    private final Set<String> zzaLI;
    
    public abstract zzag.zza zzE(final Map<String, zzag.zza> p0);
    
    boolean zzg(final Set<String> set) {
        return set.containsAll(this.zzaLI);
    }
    
    public Set<String> zzyN() {
        return this.zzaLI;
    }
    
    public abstract boolean zzyh();
}
