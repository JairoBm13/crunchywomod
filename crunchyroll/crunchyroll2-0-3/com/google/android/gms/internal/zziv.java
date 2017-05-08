// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Result;
import android.os.RemoteException;
import com.google.android.gms.common.api.zza;
import com.google.android.gms.appdatasearch.UsageInfo;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.common.api.GoogleApiClient;
import java.util.List;
import android.net.Uri$Builder;
import android.content.Intent;
import android.net.Uri;
import com.google.android.gms.appindexing.AppIndexApi;
import com.google.android.gms.appdatasearch.zzk;

public final class zziv implements zzk, AppIndexApi
{
    public static Intent zza(final String s, final Uri uri) {
        zzb(s, uri);
        final List pathSegments = uri.getPathSegments();
        final String s2 = pathSegments.get(0);
        final Uri$Builder uri$Builder = new Uri$Builder();
        uri$Builder.scheme(s2);
        if (pathSegments.size() > 1) {
            uri$Builder.authority((String)pathSegments.get(1));
            for (int i = 2; i < pathSegments.size(); ++i) {
                uri$Builder.appendPath((String)pathSegments.get(i));
            }
        }
        uri$Builder.encodedQuery(uri.getEncodedQuery());
        uri$Builder.encodedFragment(uri.getEncodedFragment());
        return new Intent("android.intent.action.VIEW", uri$Builder.build());
    }
    
    private PendingResult<Status> zza(final GoogleApiClient googleApiClient, final Action action, final int n) {
        return this.zza(googleApiClient, zziu.zza(action, System.currentTimeMillis(), googleApiClient.getContext().getPackageName(), n));
    }
    
    private static void zzb(final String s, final Uri uri) {
        if (!"android-app".equals(uri.getScheme())) {
            throw new IllegalArgumentException("AppIndex: The URI scheme must be 'android-app' and follow the format (android-app://<package_name>/<scheme>/[host_path]). Provided URI: " + uri);
        }
        final String host = uri.getHost();
        if (s != null && !s.equals(host)) {
            throw new IllegalArgumentException("AppIndex: The URI host must match the package name and follow the format (android-app://<package_name>/<scheme>/[host_path]). Provided URI: " + uri);
        }
        final List pathSegments = uri.getPathSegments();
        if (pathSegments.isEmpty() || pathSegments.get(0).isEmpty()) {
            throw new IllegalArgumentException("AppIndex: The app URI scheme must exist and follow the format android-app://<package_name>/<scheme>/[host_path]). Provided URI: " + uri);
        }
    }
    
    @Override
    public PendingResult<Status> end(final GoogleApiClient googleApiClient, final Action action) {
        return this.zza(googleApiClient, action, 2);
    }
    
    @Override
    public PendingResult<Status> start(final GoogleApiClient googleApiClient, final Action action) {
        return this.zza(googleApiClient, action, 1);
    }
    
    public PendingResult<Status> zza(final GoogleApiClient googleApiClient, final UsageInfo... array) {
        return googleApiClient.zza((PendingResult<Status>)new zzc<Status>(googleApiClient) {
            final /* synthetic */ String zzNP = googleApiClient.getContext().getPackageName();
            
            @Override
            protected void zza(final zziq zziq) throws RemoteException {
                zziq.zza(new zziv.zzd((com.google.android.gms.common.api.zza.zzb<Status>)this), this.zzNP, array);
            }
        });
    }
    
    private abstract static class zzb<T extends Result> extends zza<T, zzit>
    {
        public zzb(final GoogleApiClient googleApiClient) {
            super(com.google.android.gms.appdatasearch.zza.zzMO, googleApiClient);
        }
        
        protected abstract void zza(final zziq p0) throws RemoteException;
        
        protected final void zza(final zzit zzit) throws RemoteException {
            this.zza(zzit.zzkO());
        }
    }
    
    private abstract static class zzc<T extends Result> extends zzb<Status>
    {
        zzc(final GoogleApiClient googleApiClient) {
            super(googleApiClient);
        }
        
        protected Status zzb(final Status status) {
            return status;
        }
    }
    
    private static final class zzd extends zzis<Status>
    {
        public zzd(final com.google.android.gms.common.api.zza.zzb<Status> zzb) {
            super(zzb);
        }
        
        @Override
        public void zza(final Status status) {
            this.zzNO.zzm((T)status);
        }
    }
}
