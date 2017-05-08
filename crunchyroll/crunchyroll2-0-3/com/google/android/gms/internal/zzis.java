// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import android.os.ParcelFileDescriptor;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.appdatasearch.GetRecentContextCall;
import com.google.android.gms.common.api.zza;

public abstract class zzis<T> extends zzir.zza
{
    protected zza.zzb<T> zzNO;
    
    public zzis(final zza.zzb<T> zzNO) {
        this.zzNO = zzNO;
    }
    
    public void zza(final GetRecentContextCall.Response response) {
    }
    
    public void zza(final Status status) {
    }
    
    public void zza(final Status status, final ParcelFileDescriptor parcelFileDescriptor) {
    }
    
    public void zza(final Status status, final boolean b) {
    }
}
