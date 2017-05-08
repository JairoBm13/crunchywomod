// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.server.converter;

import android.os.Parcel;
import com.google.android.gms.common.server.response.FastJsonResponse;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class ConverterWrapper implements SafeParcelable
{
    public static final zza CREATOR;
    private final int zzCY;
    private final StringToIntConverter zzabA;
    
    static {
        CREATOR = new zza();
    }
    
    ConverterWrapper(final int zzCY, final StringToIntConverter zzabA) {
        this.zzCY = zzCY;
        this.zzabA = zzabA;
    }
    
    private ConverterWrapper(final StringToIntConverter zzabA) {
        this.zzCY = 1;
        this.zzabA = zzabA;
    }
    
    public static ConverterWrapper zza(final FastJsonResponse.zza<?, ?> zza) {
        if (zza instanceof StringToIntConverter) {
            return new ConverterWrapper((StringToIntConverter)zza);
        }
        throw new IllegalArgumentException("Unsupported safe parcelable field converter class.");
    }
    
    public int describeContents() {
        final zza creator = ConverterWrapper.CREATOR;
        return 0;
    }
    
    int getVersionCode() {
        return this.zzCY;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        final zza creator = ConverterWrapper.CREATOR;
        zza.zza(this, parcel, n);
    }
    
    StringToIntConverter zzoh() {
        return this.zzabA;
    }
    
    public FastJsonResponse.zza<?, ?> zzoi() {
        if (this.zzabA != null) {
            return this.zzabA;
        }
        throw new IllegalStateException("There was no converter wrapped in this ConverterWrapper.");
    }
}
