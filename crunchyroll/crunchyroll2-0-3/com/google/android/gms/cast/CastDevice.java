// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.cast;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Collections;
import com.google.android.gms.cast.internal.zzf;
import android.os.Bundle;
import java.net.UnknownHostException;
import java.net.InetAddress;
import java.util.ArrayList;
import com.google.android.gms.common.images.WebImage;
import java.util.List;
import java.net.Inet4Address;
import android.os.Parcelable$Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class CastDevice implements SafeParcelable
{
    public static final Parcelable$Creator<CastDevice> CREATOR;
    private final int zzCY;
    private String zzQL;
    String zzQM;
    private Inet4Address zzQN;
    private String zzQO;
    private String zzQP;
    private String zzQQ;
    private int zzQR;
    private List<WebImage> zzQS;
    private int zzQT;
    private int zzwS;
    
    static {
        CREATOR = (Parcelable$Creator)new zzb();
    }
    
    private CastDevice() {
        this(3, null, null, null, null, null, -1, new ArrayList<WebImage>(), 0, -1);
    }
    
    CastDevice(final int zzCY, final String zzQL, final String zzQM, final String zzQO, final String zzQP, final String zzQQ, final int zzQR, final List<WebImage> zzQS, final int zzQT, final int zzwS) {
        this.zzCY = zzCY;
        this.zzQL = zzQL;
        this.zzQM = zzQM;
        while (true) {
            if (this.zzQM == null) {
                break Label_0049;
            }
            try {
                final InetAddress byName = InetAddress.getByName(this.zzQM);
                if (byName instanceof Inet4Address) {
                    this.zzQN = (Inet4Address)byName;
                }
                this.zzQO = zzQO;
                this.zzQP = zzQP;
                this.zzQQ = zzQQ;
                this.zzQR = zzQR;
                this.zzQS = zzQS;
                this.zzQT = zzQT;
                this.zzwS = zzwS;
            }
            catch (UnknownHostException ex) {
                this.zzQN = null;
                continue;
            }
            break;
        }
    }
    
    public static CastDevice getFromBundle(final Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        bundle.setClassLoader(CastDevice.class.getClassLoader());
        return (CastDevice)bundle.getParcelable("com.google.android.gms.cast.EXTRA_CAST_DEVICE");
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o != this) {
            if (!(o instanceof CastDevice)) {
                return false;
            }
            final CastDevice castDevice = (CastDevice)o;
            if (this.getDeviceId() == null) {
                if (castDevice.getDeviceId() != null) {
                    return false;
                }
            }
            else if (!zzf.zza(this.zzQL, castDevice.zzQL) || !zzf.zza(this.zzQN, castDevice.zzQN) || !zzf.zza(this.zzQP, castDevice.zzQP) || !zzf.zza(this.zzQO, castDevice.zzQO) || !zzf.zza(this.zzQQ, castDevice.zzQQ) || this.zzQR != castDevice.zzQR || !zzf.zza(this.zzQS, castDevice.zzQS) || this.zzQT != castDevice.zzQT || this.zzwS != castDevice.zzwS) {
                return false;
            }
        }
        return true;
    }
    
    public int getCapabilities() {
        return this.zzQT;
    }
    
    public String getDeviceId() {
        return this.zzQL;
    }
    
    public String getDeviceVersion() {
        return this.zzQQ;
    }
    
    public String getFriendlyName() {
        return this.zzQO;
    }
    
    public List<WebImage> getIcons() {
        return Collections.unmodifiableList((List<? extends WebImage>)this.zzQS);
    }
    
    public String getModelName() {
        return this.zzQP;
    }
    
    public int getServicePort() {
        return this.zzQR;
    }
    
    public int getStatus() {
        return this.zzwS;
    }
    
    int getVersionCode() {
        return this.zzCY;
    }
    
    @Override
    public int hashCode() {
        if (this.zzQL == null) {
            return 0;
        }
        return this.zzQL.hashCode();
    }
    
    public void putInBundle(final Bundle bundle) {
        if (bundle == null) {
            return;
        }
        bundle.putParcelable("com.google.android.gms.cast.EXTRA_CAST_DEVICE", (Parcelable)this);
    }
    
    @Override
    public String toString() {
        return String.format("\"%s\" (%s)", this.zzQO, this.zzQL);
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zzb.zza(this, parcel, n);
    }
}
