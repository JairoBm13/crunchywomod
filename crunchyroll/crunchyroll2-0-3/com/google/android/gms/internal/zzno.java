// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import com.google.android.gms.common.internal.zzu;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class zzno
{
    private final zznr zzaDU;
    private boolean zzaDV;
    private long zzaDW;
    private long zzaDX;
    private long zzaDY;
    private long zzaDZ;
    private long zzaEa;
    private boolean zzaEb;
    private final Map<Class<? extends zznq>, zznq> zzaEc;
    private final List<zznu> zzaEd;
    private final zzlb zzpw;
    
    zzno(final zzno zzno) {
        this.zzaDU = zzno.zzaDU;
        this.zzpw = zzno.zzpw;
        this.zzaDW = zzno.zzaDW;
        this.zzaDX = zzno.zzaDX;
        this.zzaDY = zzno.zzaDY;
        this.zzaDZ = zzno.zzaDZ;
        this.zzaEa = zzno.zzaEa;
        this.zzaEd = new ArrayList<zznu>(zzno.zzaEd);
        this.zzaEc = new HashMap<Class<? extends zznq>, zznq>(zzno.zzaEc.size());
        for (final Map.Entry<Class<? extends zznq>, zznq> entry : zzno.zzaEc.entrySet()) {
            final zznq zzf = zzf((Class<zznq>)entry.getKey());
            entry.getValue().zza(zzf);
            this.zzaEc.put(entry.getKey(), zzf);
        }
    }
    
    zzno(final zznr zzaDU, final zzlb zzpw) {
        zzu.zzu(zzaDU);
        zzu.zzu(zzpw);
        this.zzaDU = zzaDU;
        this.zzpw = zzpw;
        this.zzaDZ = 1800000L;
        this.zzaEa = 3024000000L;
        this.zzaEc = new HashMap<Class<? extends zznq>, zznq>();
        this.zzaEd = new ArrayList<zznu>();
    }
    
    private static <T extends zznq> T zzf(final Class<T> clazz) {
        try {
            return clazz.newInstance();
        }
        catch (InstantiationException ex) {
            throw new IllegalArgumentException("dataType doesn't have default constructor", ex);
        }
        catch (IllegalAccessException ex2) {
            throw new IllegalArgumentException("dataType default constructor is not accessible", ex2);
        }
    }
    
    public void zzL(final long zzaDX) {
        this.zzaDX = zzaDX;
    }
    
    public void zzb(final zznq zznq) {
        zzu.zzu(zznq);
        final Class<? extends zznq> class1 = zznq.getClass();
        if (class1.getSuperclass() != zznq.class) {
            throw new IllegalArgumentException();
        }
        zznq.zza(this.zze(class1));
    }
    
    public <T extends zznq> T zzd(final Class<T> clazz) {
        return (T)this.zzaEc.get(clazz);
    }
    
    public <T extends zznq> T zze(final Class<T> clazz) {
        zznq zzf;
        if ((zzf = this.zzaEc.get(clazz)) == null) {
            zzf = zzf((Class<zznq>)clazz);
            this.zzaEc.put(clazz, zzf);
        }
        return (T)zzf;
    }
    
    public zzno zzvP() {
        return new zzno(this);
    }
    
    public Collection<zznq> zzvQ() {
        return (Collection<zznq>)this.zzaEc.values();
    }
    
    public List<zznu> zzvR() {
        return this.zzaEd;
    }
    
    public long zzvS() {
        return this.zzaDW;
    }
    
    public void zzvT() {
        this.zzvX().zze(this);
    }
    
    public boolean zzvU() {
        return this.zzaDV;
    }
    
    void zzvV() {
        this.zzaDY = this.zzpw.elapsedRealtime();
        if (this.zzaDX != 0L) {
            this.zzaDW = this.zzaDX;
        }
        else {
            this.zzaDW = this.zzpw.currentTimeMillis();
        }
        this.zzaDV = true;
    }
    
    zznr zzvW() {
        return this.zzaDU;
    }
    
    zzns zzvX() {
        return this.zzaDU.zzvX();
    }
    
    boolean zzvY() {
        return this.zzaEb;
    }
    
    void zzvZ() {
        this.zzaEb = true;
    }
}
