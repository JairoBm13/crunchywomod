// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.auth.api.credentials;

import android.os.Parcel;
import java.util.Collections;
import com.google.android.gms.common.internal.zzu;
import java.util.List;
import android.net.Uri;
import android.os.Parcelable$Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class Credential implements SafeParcelable
{
    public static final Parcelable$Creator<Credential> CREATOR;
    private final String mName;
    final int zzCY;
    private final String zzKI;
    private final String zzOS;
    private final String zzOT;
    private final Uri zzOU;
    private final List<IdToken> zzOV;
    private final String zzOW;
    private final String zzOX;
    
    static {
        CREATOR = (Parcelable$Creator)new zza();
    }
    
    Credential(final int zzCY, final String zzOS, final String zzOT, final String s, final String mName, final Uri zzOU, final List<IdToken> list, final String zzOW, final String zzOX) {
        this.zzCY = zzCY;
        this.zzOS = zzOS;
        this.zzOT = zzOT;
        this.zzKI = zzu.zzu(s);
        this.mName = mName;
        this.zzOU = zzOU;
        List<IdToken> zzOV;
        if (list == null) {
            zzOV = Collections.emptyList();
        }
        else {
            zzOV = Collections.unmodifiableList((List<? extends IdToken>)list);
        }
        this.zzOV = zzOV;
        this.zzOW = zzOW;
        this.zzOX = zzOX;
    }
    
    public int describeContents() {
        return 0;
    }
    
    public String getAccountType() {
        return this.zzOX;
    }
    
    public String getId() {
        return this.zzKI;
    }
    
    public String getName() {
        return this.mName;
    }
    
    public String getPassword() {
        return this.zzOW;
    }
    
    public Uri getProfilePictureUri() {
        return this.zzOU;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zza.zza(this, parcel, n);
    }
    
    public String zzkZ() {
        return this.zzOS;
    }
    
    public String zzla() {
        return this.zzOT;
    }
    
    public List<IdToken> zzlb() {
        return this.zzOV;
    }
}
