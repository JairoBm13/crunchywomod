// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics;

import com.google.android.gms.internal.zznq;
import java.util.ListIterator;
import android.net.Uri;
import com.google.android.gms.internal.zznu;
import com.google.android.gms.common.internal.zzu;
import android.text.TextUtils;
import com.google.android.gms.internal.zzip;
import com.google.android.gms.internal.zzno;
import com.google.android.gms.analytics.internal.zzf;
import com.google.android.gms.internal.zznr;

public class zza extends zznr<zza>
{
    private final zzf zzIa;
    private boolean zzIb;
    
    public zza(final zzf zzIa) {
        super(zzIa.zzhS(), zzIa.zzhP());
        this.zzIa = zzIa;
    }
    
    public void enableAdvertisingIdCollection(final boolean zzIb) {
        this.zzIb = zzIb;
    }
    
    @Override
    protected void zza(final zzno zzno) {
        final zzip zzip = zzno.zze(zzip.class);
        if (TextUtils.isEmpty((CharSequence)zzip.getClientId())) {
            zzip.setClientId(this.zzIa.zzih().zziP());
        }
        if (this.zzIb && TextUtils.isEmpty((CharSequence)zzip.zzhx())) {
            final com.google.android.gms.analytics.internal.zza zzig = this.zzIa.zzig();
            zzip.zzaO(zzig.zzhC());
            zzip.zzE(zzig.zzhy());
        }
    }
    
    public void zzaI(final String s) {
        zzu.zzcj(s);
        this.zzaJ(s);
        this.zzwb().add(new zzb(this.zzIa, s));
    }
    
    public void zzaJ(final String s) {
        final Uri zzaK = zzb.zzaK(s);
        final ListIterator<zznu> listIterator = this.zzwb().listIterator();
        while (listIterator.hasNext()) {
            if (zzaK.equals((Object)listIterator.next().zzhe())) {
                listIterator.remove();
            }
        }
    }
    
    zzf zzhb() {
        return this.zzIa;
    }
    
    @Override
    public zzno zzhc() {
        final zzno zzvP = this.zzwa().zzvP();
        zzvP.zzb(this.zzIa.zzhX().zzix());
        zzvP.zzb(this.zzIa.zzhY().zzjE());
        this.zzd(zzvP);
        return zzvP;
    }
}
