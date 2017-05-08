// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import com.google.android.gms.common.ConnectionResult;
import android.location.Location;
import android.os.Bundle;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.location.LocationServices;
import android.content.Context;
import com.google.android.gms.common.api.GoogleApiClient;

public class br implements ConnectionCallbacks, OnConnectionFailedListener
{
    Context a;
    private GoogleApiClient b;
    
    public br(final Context a) {
        this.a = a;
        this.b = new GoogleApiClient.Builder(a).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
    }
    
    public void a() {
        if (this.b != null) {
            this.b.connect();
        }
    }
    
    @Override
    public void onConnected(final Bundle bundle) {
        try {
            if (this.b != null) {
                final Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(this.b);
                if (lastLocation != null) {
                    ac.e("TremorLog_info::Location::PlayServices lat=" + lastLocation.getLatitude() + " longi=" + lastLocation.getLongitude());
                }
                bp.a(lastLocation);
                this.b.disconnect();
            }
        }
        catch (Exception ex) {
            ac.e("TremorLog_error::Location::PlayServices onConnected " + ex.getMessage());
        }
    }
    
    @Override
    public void onConnectionFailed(final ConnectionResult connectionResult) {
        if (this.b != null) {
            this.b.disconnect();
        }
        if (this.a != null) {
            bp.a(this.a, false);
        }
    }
    
    @Override
    public void onConnectionSuspended(final int n) {
        if (this.b != null) {
            this.b.disconnect();
        }
        if (this.a != null) {
            bp.a(this.a, false);
        }
    }
}
