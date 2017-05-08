// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics.internal;

import java.util.Collections;
import java.util.HashMap;
import com.google.android.gms.common.internal.zzu;
import java.util.Map;

public class zzh
{
    private final long zzJS;
    private final String zzJT;
    private final boolean zzJU;
    private long zzJV;
    private final String zzJd;
    private final Map<String, String> zzyn;
    
    public zzh(final long zzJS, final String zzJd, final String zzJT, final boolean zzJU, final long zzJV, final Map<String, String> map) {
        zzu.zzcj(zzJd);
        zzu.zzcj(zzJT);
        this.zzJS = zzJS;
        this.zzJd = zzJd;
        this.zzJT = zzJT;
        this.zzJU = zzJU;
        this.zzJV = zzJV;
        if (map != null) {
            this.zzyn = new HashMap<String, String>(map);
            return;
        }
        this.zzyn = Collections.emptyMap();
    }
    
    public String getClientId() {
        return this.zzJd;
    }
    
    public long zzii() {
        return this.zzJS;
    }
    
    public String zzij() {
        return this.zzJT;
    }
    
    public boolean zzik() {
        return this.zzJU;
    }
    
    public long zzil() {
        return this.zzJV;
    }
    
    public Map<String, String> zzn() {
        return this.zzyn;
    }
    
    public void zzn(final long zzJV) {
        this.zzJV = zzJV;
    }
}
