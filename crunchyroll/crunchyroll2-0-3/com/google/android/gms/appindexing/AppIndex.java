// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.appindexing;

import com.google.android.gms.internal.zziv;
import com.google.android.gms.appdatasearch.zza;
import com.google.android.gms.common.api.Api;

public final class AppIndex
{
    public static final Api<Api.ApiOptions.NoOptions> API;
    public static final Api<Api.ApiOptions.NoOptions> APP_INDEX_API;
    public static final AppIndexApi AppIndexApi;
    
    static {
        API = zza.zzMQ;
        APP_INDEX_API = zza.zzMQ;
        AppIndexApi = new zziv();
    }
}
