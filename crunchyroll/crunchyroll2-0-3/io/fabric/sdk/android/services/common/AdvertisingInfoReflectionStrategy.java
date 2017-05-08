// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.common;

import io.fabric.sdk.android.Fabric;
import android.content.Context;

class AdvertisingInfoReflectionStrategy implements AdvertisingInfoStrategy
{
    private final Context context;
    
    public AdvertisingInfoReflectionStrategy(final Context context) {
        this.context = context.getApplicationContext();
    }
    
    private String getAdvertisingId() {
        try {
            return (String)Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient$Info").getMethod("getId", (Class<?>[])new Class[0]).invoke(this.getInfo(), new Object[0]);
        }
        catch (Exception ex) {
            Fabric.getLogger().w("Fabric", "Could not call getId on com.google.android.gms.ads.identifier.AdvertisingIdClient$Info");
            return null;
        }
    }
    
    private Object getInfo() {
        try {
            return Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient").getMethod("getAdvertisingIdInfo", Context.class).invoke(null, this.context);
        }
        catch (Exception ex) {
            Fabric.getLogger().w("Fabric", "Could not call getAdvertisingIdInfo on com.google.android.gms.ads.identifier.AdvertisingIdClient");
            return null;
        }
    }
    
    private boolean isLimitAdTrackingEnabled() {
        try {
            return (boolean)Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient$Info").getMethod("isLimitAdTrackingEnabled", (Class<?>[])new Class[0]).invoke(this.getInfo(), new Object[0]);
        }
        catch (Exception ex) {
            Fabric.getLogger().w("Fabric", "Could not call isLimitAdTrackingEnabled on com.google.android.gms.ads.identifier.AdvertisingIdClient$Info");
            return false;
        }
    }
    
    @Override
    public AdvertisingInfo getAdvertisingInfo() {
        if (this.isGooglePlayServiceAvailable(this.context)) {
            return new AdvertisingInfo(this.getAdvertisingId(), this.isLimitAdTrackingEnabled());
        }
        return null;
    }
    
    boolean isGooglePlayServiceAvailable(final Context context) {
        try {
            return (int)Class.forName("com.google.android.gms.common.GooglePlayServicesUtil").getMethod("isGooglePlayServicesAvailable", Context.class).invoke(null, context) == 0;
        }
        catch (Exception ex) {
            return false;
        }
    }
}
