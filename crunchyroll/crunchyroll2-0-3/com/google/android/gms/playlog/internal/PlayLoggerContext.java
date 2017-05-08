// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.playlog.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.zzt;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class PlayLoggerContext implements SafeParcelable
{
    public static final zze CREATOR;
    public final String packageName;
    public final int versionCode;
    public final int zzaGP;
    public final int zzaGQ;
    public final String zzaGR;
    public final String zzaGS;
    public final boolean zzaGT;
    public final String zzaGU;
    public final boolean zzaGV;
    
    static {
        CREATOR = new zze();
    }
    
    public PlayLoggerContext(final int versionCode, final String packageName, final int zzaGP, final int zzaGQ, final String zzaGR, final String zzaGS, final boolean zzaGT, final String zzaGU, final boolean zzaGV) {
        this.versionCode = versionCode;
        this.packageName = packageName;
        this.zzaGP = zzaGP;
        this.zzaGQ = zzaGQ;
        this.zzaGR = zzaGR;
        this.zzaGS = zzaGS;
        this.zzaGT = zzaGT;
        this.zzaGU = zzaGU;
        this.zzaGV = zzaGV;
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (!(o instanceof PlayLoggerContext)) {
                return false;
            }
            final PlayLoggerContext playLoggerContext = (PlayLoggerContext)o;
            if (this.versionCode != playLoggerContext.versionCode || !this.packageName.equals(playLoggerContext.packageName) || this.zzaGP != playLoggerContext.zzaGP || this.zzaGQ != playLoggerContext.zzaGQ || !zzt.equal(this.zzaGU, playLoggerContext.zzaGU) || !zzt.equal(this.zzaGR, playLoggerContext.zzaGR) || !zzt.equal(this.zzaGS, playLoggerContext.zzaGS) || this.zzaGT != playLoggerContext.zzaGT || this.zzaGV != playLoggerContext.zzaGV) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        return zzt.hashCode(this.versionCode, this.packageName, this.zzaGP, this.zzaGQ, this.zzaGU, this.zzaGR, this.zzaGS, this.zzaGT, this.zzaGV);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("PlayLoggerContext[");
        sb.append("versionCode=").append(this.versionCode).append(',');
        sb.append("package=").append(this.packageName).append(',');
        sb.append("packageVersionCode=").append(this.zzaGP).append(',');
        sb.append("logSource=").append(this.zzaGQ).append(',');
        sb.append("logSourceName=").append(this.zzaGU).append(',');
        sb.append("uploadAccount=").append(this.zzaGR).append(',');
        sb.append("loggingId=").append(this.zzaGS).append(',');
        sb.append("logAndroidId=").append(this.zzaGT).append(',');
        sb.append("isAnonymous=").append(this.zzaGV);
        sb.append("]");
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zze.zza(this, parcel, n);
    }
}
