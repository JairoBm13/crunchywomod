// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.api;

import android.content.IntentSender$SendIntentException;
import android.app.Activity;
import android.os.Parcelable;
import android.os.Bundle;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Context;
import com.google.android.gms.common.GooglePlayServicesUtil;
import android.content.Intent;
import java.io.PrintWriter;
import java.io.FileDescriptor;
import android.util.Log;
import android.support.v4.app.FragmentManager;
import com.google.android.gms.common.internal.zzu;
import android.support.v4.app.FragmentActivity;
import android.os.Looper;
import android.util.SparseArray;
import android.os.Handler;
import com.google.android.gms.common.ConnectionResult;
import android.content.DialogInterface$OnCancelListener;
import android.support.v4.app.Fragment;

public class zzm extends Fragment implements DialogInterface$OnCancelListener
{
    private boolean mStarted;
    private boolean zzXV;
    private int zzXW;
    private ConnectionResult zzXX;
    private final Handler zzXY;
    private final SparseArray<zza> zzXZ;
    
    public zzm() {
        this.zzXW = -1;
        this.zzXY = new Handler(Looper.getMainLooper());
        this.zzXZ = (SparseArray<zza>)new SparseArray();
    }
    
    public static zzm zza(final FragmentActivity fragmentActivity) {
        zzu.zzbY("Must be called from main thread of process");
        final FragmentManager supportFragmentManager = fragmentActivity.getSupportFragmentManager();
        try {
            final zzm zzm = (zzm)supportFragmentManager.findFragmentByTag("GmsSupportLifecycleFragment");
            if (zzm != null) {
                final zzm zzm2 = zzm;
                if (!zzm.isRemoving()) {
                    return zzm2;
                }
            }
            final zzm zzm2 = new zzm();
            supportFragmentManager.beginTransaction().add(zzm2, "GmsSupportLifecycleFragment").commit();
            supportFragmentManager.executePendingTransactions();
            return zzm2;
        }
        catch (ClassCastException ex) {
            throw new IllegalStateException("Fragment with tag GmsSupportLifecycleFragment is not a SupportLifecycleFragment", ex);
        }
    }
    
    private void zza(final int n, final ConnectionResult connectionResult) {
        Log.w("GmsSupportLifecycleFragment", "Unresolved error while connecting client. Stopping auto-manage.");
        final zza zza = (zza)this.zzXZ.get(n);
        if (zza != null) {
            this.zzbb(n);
            final GoogleApiClient.OnConnectionFailedListener zzYc = zza.zzYc;
            if (zzYc != null) {
                zzYc.onConnectionFailed(connectionResult);
            }
        }
        this.zzmV();
    }
    
    private void zzmV() {
        this.zzXV = false;
        this.zzXW = -1;
        this.zzXX = null;
        for (int i = 0; i < this.zzXZ.size(); ++i) {
            ((zza)this.zzXZ.valueAt(i)).zzYb.connect();
        }
    }
    
    @Override
    public void dump(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
        super.dump(s, fileDescriptor, printWriter, array);
        for (int i = 0; i < this.zzXZ.size(); ++i) {
            ((zza)this.zzXZ.valueAt(i)).dump(s, fileDescriptor, printWriter, array);
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
        this.mStarted = true;
        if (!this.zzXV) {
            for (int i = 0; i < this.zzXZ.size(); ++i) {
                ((zza)this.zzXZ.valueAt(i)).zzYb.connect();
            }
        }
    }
    
    @Override
    public void onStop() {
        super.onStop();
        this.mStarted = false;
        for (int i = 0; i < this.zzXZ.size(); ++i) {
            ((zza)this.zzXZ.valueAt(i)).zzYb.disconnect();
        }
    }
    
    public void zza(final int n, final GoogleApiClient googleApiClient, final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        zzu.zzb(googleApiClient, "GoogleApiClient instance cannot be null");
        zzu.zza(this.zzXZ.indexOfKey(n) < 0, (Object)("Already managing a GoogleApiClient with id " + n));
        this.zzXZ.put(n, (Object)new zza(n, googleApiClient, onConnectionFailedListener));
        if (this.mStarted && !this.zzXV) {
            googleApiClient.connect();
        }
    }
    
    public void zzbb(final int n) {
        final zza zza = (zza)this.zzXZ.get(n);
        this.zzXZ.remove(n);
        if (zza != null) {
            zza.zzmW();
        }
    }
    
    private class zza implements OnConnectionFailedListener
    {
        public final int zzYa;
        public final GoogleApiClient zzYb;
        public final OnConnectionFailedListener zzYc;
        
        public zza(final int zzYa, final GoogleApiClient zzYb, final OnConnectionFailedListener zzYc) {
            this.zzYa = zzYa;
            this.zzYb = zzYb;
            this.zzYc = zzYc;
            zzYb.registerConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener)this);
        }
        
        public void dump(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
            printWriter.append(s).append("GoogleApiClient #").print(this.zzYa);
            printWriter.println(":");
            this.zzYb.dump(s + "  ", fileDescriptor, printWriter, array);
        }
        
        @Override
        public void onConnectionFailed(final ConnectionResult connectionResult) {
            zzm.this.zzXY.post((Runnable)new zzb(this.zzYa, connectionResult));
        }
        
        public void zzmW() {
            this.zzYb.unregisterConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener)this);
            this.zzYb.disconnect();
        }
    }
    
    private class zzb implements Runnable
    {
        private final int zzYe;
        private final ConnectionResult zzYf;
        
        public zzb(final int zzYe, final ConnectionResult zzYf) {
            this.zzYe = zzYe;
            this.zzYf = zzYf;
        }
        
        @Override
        public void run() {
            if (!zzm.this.mStarted || zzm.this.zzXV) {
                return;
            }
            zzm.this.zzXV = true;
            zzm.this.zzXW = this.zzYe;
            zzm.this.zzXX = this.zzYf;
            if (this.zzYf.hasResolution()) {
                try {
                    this.zzYf.startResolutionForResult(zzm.this.getActivity(), (zzm.this.getActivity().getSupportFragmentManager().getFragments().indexOf(zzm.this) + 1 << 16) + 1);
                    return;
                }
                catch (IntentSender$SendIntentException ex) {
                    zzm.this.zzmV();
                    return;
                }
            }
            if (GooglePlayServicesUtil.isUserRecoverableError(this.zzYf.getErrorCode())) {
                GooglePlayServicesUtil.showErrorDialogFragment(this.zzYf.getErrorCode(), zzm.this.getActivity(), zzm.this, 2, (DialogInterface$OnCancelListener)zzm.this);
                return;
            }
            zzm.this.zza(this.zzYe, this.zzYf);
        }
    }
}
