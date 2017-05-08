// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import java.util.Iterator;
import java.util.ArrayList;
import com.google.android.gms.common.internal.zzu;
import java.util.List;

public abstract class zznr<T extends zznr>
{
    private final zzns zzaEe;
    protected final zzno zzaEf;
    private final List<zznp> zzaEg;
    
    protected zznr(final zzns zzaEe, final zzlb zzlb) {
        zzu.zzu(zzaEe);
        this.zzaEe = zzaEe;
        this.zzaEg = new ArrayList<zznp>();
        final zzno zzaEf = new zzno(this, zzlb);
        zzaEf.zzvZ();
        this.zzaEf = zzaEf;
    }
    
    protected void zza(final zzno zzno) {
    }
    
    protected void zzd(final zzno zzno) {
        final Iterator<zznp> iterator = this.zzaEg.iterator();
        while (iterator.hasNext()) {
            iterator.next().zza(this, zzno);
        }
    }
    
    public zzno zzhc() {
        final zzno zzvP = this.zzaEf.zzvP();
        this.zzd(zzvP);
        return zzvP;
    }
    
    protected zzns zzvX() {
        return this.zzaEe;
    }
    
    public zzno zzwa() {
        return this.zzaEf;
    }
    
    public List<zznu> zzwb() {
        return this.zzaEf.zzvR();
    }
}
