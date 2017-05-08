// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.cast;

import android.os.Parcel;
import com.google.android.gms.common.internal.zzt;
import com.google.android.gms.cast.internal.zzf;
import java.util.ArrayList;
import com.google.android.gms.common.images.WebImage;
import android.net.Uri;
import java.util.List;
import android.os.Parcelable$Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class ApplicationMetadata implements SafeParcelable
{
    public static final Parcelable$Creator<ApplicationMetadata> CREATOR;
    String mName;
    private final int zzCY;
    String zzQv;
    List<String> zzQw;
    String zzQx;
    Uri zzQy;
    List<WebImage> zzvi;
    
    static {
        CREATOR = (Parcelable$Creator)new zza();
    }
    
    private ApplicationMetadata() {
        this.zzCY = 1;
        this.zzvi = new ArrayList<WebImage>();
        this.zzQw = new ArrayList<String>();
    }
    
    ApplicationMetadata(final int zzCY, final String zzQv, final String mName, final List<WebImage> zzvi, final List<String> zzQw, final String zzQx, final Uri zzQy) {
        this.zzCY = zzCY;
        this.zzQv = zzQv;
        this.mName = mName;
        this.zzvi = zzvi;
        this.zzQw = zzQw;
        this.zzQx = zzQx;
        this.zzQy = zzQy;
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o != this) {
            if (!(o instanceof ApplicationMetadata)) {
                return false;
            }
            final ApplicationMetadata applicationMetadata = (ApplicationMetadata)o;
            if (!zzf.zza(this.zzQv, applicationMetadata.zzQv) || !zzf.zza(this.zzvi, applicationMetadata.zzvi) || !zzf.zza(this.mName, applicationMetadata.mName) || !zzf.zza(this.zzQw, applicationMetadata.zzQw) || !zzf.zza(this.zzQx, applicationMetadata.zzQx) || !zzf.zza(this.zzQy, applicationMetadata.zzQy)) {
                return false;
            }
        }
        return true;
    }
    
    public String getApplicationId() {
        return this.zzQv;
    }
    
    public List<WebImage> getImages() {
        return this.zzvi;
    }
    
    public String getName() {
        return this.mName;
    }
    
    public String getSenderAppIdentifier() {
        return this.zzQx;
    }
    
    int getVersionCode() {
        return this.zzCY;
    }
    
    @Override
    public int hashCode() {
        return zzt.hashCode(this.zzCY, this.zzQv, this.mName, this.zzvi, this.zzQw, this.zzQx, this.zzQy);
    }
    
    @Override
    public String toString() {
        final int n = 0;
        final StringBuilder append = new StringBuilder().append("applicationId: ").append(this.zzQv).append(", name: ").append(this.mName).append(", images.count: ");
        int size;
        if (this.zzvi == null) {
            size = 0;
        }
        else {
            size = this.zzvi.size();
        }
        final StringBuilder append2 = append.append(size).append(", namespaces.count: ");
        int size2;
        if (this.zzQw == null) {
            size2 = n;
        }
        else {
            size2 = this.zzQw.size();
        }
        return append2.append(size2).append(", senderAppIdentifier: ").append(this.zzQx).append(", senderAppLaunchUrl: ").append(this.zzQy).toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zza.zza(this, parcel, n);
    }
    
    public Uri zzle() {
        return this.zzQy;
    }
}
