// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.api;

import android.content.IntentSender$SendIntentException;
import java.io.PrintWriter;
import java.io.FileDescriptor;
import android.os.Parcelable;
import android.support.v4.content.Loader;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.app.Activity;
import android.content.Context;
import com.google.android.gms.common.GooglePlayServicesUtil;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import com.google.android.gms.common.internal.zzu;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.os.Looper;
import android.util.SparseArray;
import android.os.Handler;
import com.google.android.gms.common.ConnectionResult;
import android.support.v4.app.LoaderManager;
import android.content.DialogInterface$OnCancelListener;
import android.support.v4.app.Fragment;

public class zzn extends Fragment implements DialogInterface$OnCancelListener, LoaderCallbacks<ConnectionResult>
{
    private boolean zzXV;
    private int zzXW;
    private ConnectionResult zzXX;
    private final Handler zzXY;
    private final SparseArray<zzb> zzXZ;
    
    public zzn() {
        this.zzXW = -1;
        this.zzXY = new Handler(Looper.getMainLooper());
        this.zzXZ = (SparseArray<zzb>)new SparseArray();
    }
    
    private void zza(final int n, final ConnectionResult connectionResult) {
        Log.w("GmsSupportLoaderLifecycleFragment", "Unresolved error while connecting client. Stopping auto-manage.");
        final zzb zzb = (zzb)this.zzXZ.get(n);
        if (zzb != null) {
            this.zzbb(n);
            final GoogleApiClient.OnConnectionFailedListener zzYc = zzb.zzYc;
            if (zzYc != null) {
                zzYc.onConnectionFailed(connectionResult);
            }
        }
        this.zzmV();
    }
    
    public static zzn zzb(final FragmentActivity fragmentActivity) {
        zzu.zzbY("Must be called from main thread of process");
        final FragmentManager supportFragmentManager = fragmentActivity.getSupportFragmentManager();
        try {
            final zzn zzn = (zzn)supportFragmentManager.findFragmentByTag("GmsSupportLoaderLifecycleFragment");
            if (zzn != null) {
                final zzn zzn2 = zzn;
                if (!zzn.isRemoving()) {
                    return zzn2;
                }
            }
            final zzn zzn2 = new zzn();
            supportFragmentManager.beginTransaction().add(zzn2, "GmsSupportLoaderLifecycleFragment").commit();
            supportFragmentManager.executePendingTransactions();
            return zzn2;
        }
        catch (ClassCastException ex) {
            throw new IllegalStateException("Fragment with tag GmsSupportLoaderLifecycleFragment is not a SupportLoaderLifecycleFragment", ex);
        }
    }
    
    private void zzb(final int zzXW, final ConnectionResult zzXX) {
        if (!this.zzXV) {
            this.zzXV = true;
            this.zzXW = zzXW;
            this.zzXX = zzXX;
            this.zzXY.post((Runnable)new zzc(zzXW, zzXX));
        }
    }
    
    private void zzmV() {
        int i = 0;
        this.zzXV = false;
        this.zzXW = -1;
        this.zzXX = null;
        final LoaderManager loaderManager = this.getLoaderManager();
        while (i < this.zzXZ.size()) {
            final int key = this.zzXZ.keyAt(i);
            final zza zzbd = this.zzbd(key);
            if (zzbd != null && zzbd.zzmX()) {
                loaderManager.destroyLoader(key);
                loaderManager.initLoader(key, null, (LoaderManager.LoaderCallbacks<Object>)this);
            }
            ++i;
        }
    }
    
    @Override
    public void onActivityResult(int n, final int n2, final Intent intent) {
        final int n3 = 1;
        Label_0030: {
            switch (n) {
                case 2: {
                    if (GooglePlayServicesUtil.isGooglePlayServicesAvailable((Context)this.getActivity()) == 0) {
                        n = n3;
                        break Label_0030;
                    }
                    break;
                }
                case 1: {
                    if (n2 == -1) {
                        n = n3;
                        break Label_0030;
                    }
                    break;
                }
            }
            n = 0;
        }
        if (n != 0) {
            this.zzmV();
            return;
        }
        this.zza(this.zzXW, this.zzXX);
    }
    
    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        for (int i = 0; i < this.zzXZ.size(); ++i) {
            final int key = this.zzXZ.keyAt(i);
            final zza zzbd = this.zzbd(key);
            if (zzbd != null && ((zzb)this.zzXZ.valueAt(i)).zzYb != zzbd.zzYb) {
                this.getLoaderManager().restartLoader(key, null, (LoaderManager.LoaderCallbacks<Object>)this);
            }
            else {
                this.getLoaderManager().initLoader(key, null, (LoaderManager.LoaderCallbacks<Object>)this);
            }
        }
    }
    
    public void onCancel(final DialogInterface dialogInterface) {
        this.zza(this.zzXW, new ConnectionResult(13, null));
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            this.zzXV = bundle.getBoolean("resolving_error", false);
            this.zzXW = bundle.getInt("failed_client_id", -1);
            if (this.zzXW >= 0) {
                this.zzXX = new ConnectionResult(bundle.getInt("failed_status"), (PendingIntent)bundle.getParcelable("failed_resolution"));
            }
        }
    }
    
    public Loader<ConnectionResult> onCreateLoader(final int n, final Bundle bundle) {
        return new zza((Context)this.getActivity(), ((zzb)this.zzXZ.get(n)).zzYb);
    }
    
    public void onLoaderReset(final Loader<ConnectionResult> loader) {
    }
    
    @Override
    public void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("resolving_error", this.zzXV);
        if (this.zzXW >= 0) {
            bundle.putInt("failed_client_id", this.zzXW);
            bundle.putInt("failed_status", this.zzXX.getErrorCode());
            bundle.putParcelable("failed_resolution", (Parcelable)this.zzXX.getResolution());
        }
    }
    
    @Override
    public void onStart() {
        super.onStart();
        if (!this.zzXV) {
            for (int i = 0; i < this.zzXZ.size(); ++i) {
                this.getLoaderManager().initLoader(this.zzXZ.keyAt(i), null, (LoaderManager.LoaderCallbacks<Object>)this);
            }
        }
    }
    
    public void zza(final int n, final GoogleApiClient googleApiClient, final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        zzu.zzb(googleApiClient, "GoogleApiClient instance cannot be null");
        zzu.zza(this.zzXZ.indexOfKey(n) < 0, (Object)("Already managing a GoogleApiClient with id " + n));
        this.zzXZ.put(n, (Object)new zzb(googleApiClient, onConnectionFailedListener));
        if (this.getActivity() != null) {
            LoaderManager.enableDebugLogging(false);
            this.getLoaderManager().initLoader(n, null, (LoaderManager.LoaderCallbacks<Object>)this);
        }
    }
    
    public void zza(final Loader<ConnectionResult> loader, final ConnectionResult connectionResult) {
        if (!connectionResult.isSuccess()) {
            this.zzb(loader.getId(), connectionResult);
        }
    }
    
    public void zzbb(final int n) {
        this.zzXZ.remove(n);
        this.getLoaderManager().destroyLoader(n);
    }
    
    public GoogleApiClient zzbc(final int n) {
        if (this.getActivity() != null) {
            final zza zzbd = this.zzbd(n);
            if (zzbd != null) {
                return zzbd.zzYb;
            }
        }
        return null;
    }
    
    zza zzbd(final int n) {
        try {
            return (zza)this.getLoaderManager().getLoader(n);
        }
        catch (ClassCastException ex) {
            throw new IllegalStateException("Unknown loader in SupportLoaderLifecycleFragment", ex);
        }
    }
    
    static class zza extends Loader<ConnectionResult> implements ConnectionCallbacks, OnConnectionFailedListener
    {
        public final GoogleApiClient zzYb;
        private boolean zzYg;
        private ConnectionResult zzYh;
        
        public zza(final Context context, final GoogleApiClient zzYb) {
            super(context);
            this.zzYb = zzYb;
        }
        
        private void zzf(final ConnectionResult zzYh) {
            this.zzYh = zzYh;
            if (this.isStarted() && !this.isAbandoned()) {
                this.deliverResult(zzYh);
            }
        }
        
        @Override
        public void dump(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
            super.dump(s, fileDescriptor, printWriter, array);
            this.zzYb.dump(s, fileDescriptor, printWriter, array);
        }
        
        @Override
        public void onConnected(final Bundle bundle) {
            this.zzYg = false;
            this.zzf(ConnectionResult.zzVG);
        }
        
        @Override
        public void onConnectionFailed(final ConnectionResult connectionResult) {
            this.zzYg = true;
            this.zzf(connectionResult);
        }
        
        @Override
        public void onConnectionSuspended(final int n) {
        }
        
        @Override
        protected void onReset() {
            this.zzYh = null;
            this.zzYg = false;
            this.zzYb.unregisterConnectionCallbacks((GoogleApiClient.ConnectionCallbacks)this);
            this.zzYb.unregisterConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener)this);
            this.zzYb.disconnect();
        }
        
        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            this.zzYb.registerConnectionCallbacks((GoogleApiClient.ConnectionCallbacks)this);
            this.zzYb.registerConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener)this);
            if (this.zzYh != null) {
                this.deliverResult(this.zzYh);
            }
            if (!this.zzYb.isConnected() && !this.zzYb.isConnecting() && !this.zzYg) {
                this.zzYb.connect();
            }
        }
        
        @Override
        protected void onStopLoading() {
            this.zzYb.disconnect();
        }
        
        public boolean zzmX() {
            return this.zzYg;
        }
    }
    
    private static class zzb
    {
        public final GoogleApiClient zzYb;
        public final GoogleApiClient.OnConnectionFailedListener zzYc;
        
        private zzb(final GoogleApiClient zzYb, final GoogleApiClient.OnConnectionFailedListener zzYc) {
            this.zzYb = zzYb;
            this.zzYc = zzYc;
        }
    }
    
    private class zzc implements Runnable
    {
        private final int zzYe;
        private final ConnectionResult zzYf;
        
        public zzc(final int zzYe, final ConnectionResult zzYf) {
            this.zzYe = zzYe;
            this.zzYf = zzYf;
        }
        
        @Override
        public void run() {
            if (this.zzYf.hasResolution()) {
                try {
                    this.zzYf.startResolutionForResult(zzn.this.getActivity(), (zzn.this.getActivity().getSupportFragmentManager().getFragments().indexOf(zzn.this) + 1 << 16) + 1);
                    return;
                }
                catch (IntentSender$SendIntentException ex) {
                    zzn.this.zzmV();
                    return;
                }
            }
            if (GooglePlayServicesUtil.isUserRecoverableError(this.zzYf.getErrorCode())) {
                GooglePlayServicesUtil.showErrorDialogFragment(this.zzYf.getErrorCode(), zzn.this.getActivity(), zzn.this, 2, (DialogInterface$OnCancelListener)zzn.this);
                return;
            }
            zzn.this.zza(this.zzYe, this.zzYf);
        }
    }
}
