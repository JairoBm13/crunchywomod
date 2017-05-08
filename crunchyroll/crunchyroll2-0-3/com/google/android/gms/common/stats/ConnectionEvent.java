// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.stats;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class ConnectionEvent implements SafeParcelable
{
    public static final Parcelable$Creator<ConnectionEvent> CREATOR;
    final int zzCY;
    private final long zzabZ;
    private int zzaca;
    private final String zzacb;
    private final String zzacc;
    private final String zzacd;
    private final String zzace;
    private final String zzacf;
    private final String zzacg;
    private final long zzach;
    private final long zzaci;
    private long zzacj;
    
    static {
        CREATOR = (Parcelable$Creator)new zza();
    }
    
    ConnectionEvent(final int zzCY, final long zzabZ, final int zzaca, final String zzacb, final String zzacc, final String zzacd, final String zzace, final String zzacf, final String zzacg, final long zzach, final long zzaci) {
        this.zzCY = zzCY;
        this.zzabZ = zzabZ;
        this.zzaca = zzaca;
        this.zzacb = zzacb;
        this.zzacc = zzacc;
        this.zzacd = zzacd;
        this.zzace = zzace;
        this.zzacj = -1L;
        this.zzacf = zzacf;
        this.zzacg = zzacg;
        this.zzach = zzach;
        this.zzaci = zzaci;
    }
    
    public ConnectionEvent(final long n, final int n2, final String s, final String s2, final String s3, final String s4, final String s5, final String s6, final long n3, final long n4) {
        this(1, n, n2, s, s2, s3, s4, s5, s6, n3, n4);
    }
    
    public int describeContents() {
        return 0;
    }
    
    public int getEventType() {
        return this.zzaca;
    }
    
    public long getTimeMillis() {
        return this.zzabZ;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zza.zza(this, parcel, n);
    }
    
    public String zzoG() {
        return this.zzacb;
    }
    
    public String zzoH() {
        return this.zzacc;
    }
    
    public String zzoI() {
        return this.zzacd;
    }
    
    public String zzoJ() {
        return this.zzace;
    }
    
    public String zzoK() {
        return this.zzacf;
    }
    
    public String zzoL() {
        return this.zzacg;
    }
    
    public long zzoM() {
        return this.zzaci;
    }
    
    public long zzoN() {
        return this.zzach;
    }
}
