// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.playlog.internal;

import android.os.Parcel;
import java.util.Iterator;
import android.os.Bundle;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class LogEvent implements SafeParcelable
{
    public static final zzc CREATOR;
    public final String tag;
    public final int versionCode;
    public final long zzaGF;
    public final byte[] zzaGG;
    public final Bundle zzaGH;
    
    static {
        CREATOR = new zzc();
    }
    
    LogEvent(final int versionCode, final long zzaGF, final String tag, final byte[] zzaGG, final Bundle zzaGH) {
        this.versionCode = versionCode;
        this.zzaGF = zzaGF;
        this.tag = tag;
        this.zzaGG = zzaGG;
        this.zzaGH = zzaGH;
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("tag=").append(this.tag).append(",");
        sb.append("eventTime=").append(this.zzaGF).append(",");
        if (this.zzaGH != null && !this.zzaGH.isEmpty()) {
            sb.append("keyValues=");
            for (final String s : this.zzaGH.keySet()) {
                sb.append("(").append(s).append(",");
                sb.append(this.zzaGH.getString(s)).append(")");
                sb.append(" ");
            }
        }
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zzc.zza(this, parcel, n);
    }
}
