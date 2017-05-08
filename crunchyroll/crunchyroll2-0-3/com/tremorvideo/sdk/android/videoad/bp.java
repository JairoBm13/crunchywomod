// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import com.google.android.gms.common.GooglePlayServicesUtil;
import android.os.Build$VERSION;
import android.os.SystemClock;
import android.os.Bundle;
import android.location.Location;
import android.location.LocationListener;
import android.content.Context;
import android.location.LocationManager;

public final class bp
{
    public static double a;
    public static double b;
    private static long c;
    
    static {
        bp.a = 0.0;
        bp.b = 0.0;
        bp.c = 0L;
    }
    
    public static int a() {
        if (!b() && !c()) {
            return 2;
        }
        final LocationManager locationManager = (LocationManager)ac.x().getSystemService("location");
        if (b() && locationManager.isProviderEnabled("gps")) {
            return 3;
        }
        if (c() && locationManager.isProviderEnabled("network")) {
            return 3;
        }
        return 2;
    }
    
    public static void a(final Context context) {
        if (b() || c()) {
            final LocationManager locationManager = (LocationManager)context.getSystemService("location");
            final LocationListener locationListener = (LocationListener)new LocationListener() {
                public void onLocationChanged(final Location location) {
                    bp.a(location);
                    locationManager.removeUpdates((LocationListener)this);
                }
                
                public void onProviderDisabled(final String s) {
                    locationManager.removeUpdates((LocationListener)this);
                }
                
                public void onProviderEnabled(final String s) {
                }
                
                public void onStatusChanged(final String s, final int n, final Bundle bundle) {
                    if (n != 2) {
                        locationManager.removeUpdates((LocationListener)this);
                    }
                }
            };
            if (b()) {
                if (!a("gps", locationManager, (LocationListener)locationListener) && !a("network", locationManager, (LocationListener)locationListener)) {
                    a(locationManager.getLastKnownLocation("gps"));
                }
            }
            else if (!a("network", locationManager, (LocationListener)locationListener)) {
                a(locationManager.getLastKnownLocation("network"));
            }
        }
    }
    
    public static void a(final Context context, final boolean b) {
        if ((b() || c()) && (bp.c == 0L || SystemClock.elapsedRealtime() - bp.c > 300000L || (bp.a == 0.0 && bp.b == 0.0))) {
            if (Build$VERSION.SDK_INT > 8 && b) {
                try {
                    Class.forName("com.google.android.gms.common.GooglePlayServicesUtil");
                    Class.forName("com.google.android.gms.location.LocationServices");
                    if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) == 0) {
                        new br(context).a();
                        return;
                    }
                    a(context);
                    return;
                }
                catch (ClassNotFoundException ex) {
                    ac.e("TremorLog_error::Location::PlayServices ClassNotFoundException " + ex.getMessage());
                    a(context);
                    return;
                }
                catch (Exception ex2) {
                    ac.e("TremorLog_error::Location::PlayServices " + ex2.getMessage());
                    a(context);
                    return;
                }
            }
            a(context);
        }
    }
    
    static void a(final Location location) {
        if (location != null) {
            bp.c = SystemClock.elapsedRealtime();
            bp.a = location.getLatitude();
            bp.b = location.getLongitude();
        }
    }
    
    private static boolean a(final String s, final LocationManager locationManager, final LocationListener locationListener) {
        if (!locationManager.isProviderEnabled(s)) {
            return false;
        }
        a(locationManager.getLastKnownLocation(s));
        locationManager.requestLocationUpdates(s, 0L, 0.0f, locationListener);
        return true;
    }
    
    private static boolean b() {
        return ac.b("android.permission.ACCESS_FINE_LOCATION");
    }
    
    private static boolean c() {
        return ac.b("android.permission.ACCESS_COARSE_LOCATION");
    }
}
