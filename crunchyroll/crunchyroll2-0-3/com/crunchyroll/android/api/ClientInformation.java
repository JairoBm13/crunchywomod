// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api;

import android.os.Build$VERSION;
import android.os.Build;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager$NameNotFoundException;
import java.util.UUID;
import android.provider.Settings$Secure;
import android.telephony.TelephonyManager;
import android.app.Application;

public class ClientInformation
{
    private final String mDeviceId;
    private final boolean mIsGoogleTV;
    private final int mVersionCode;
    private final String mVersionName;
    
    public ClientInformation(final Application application) {
        this.mIsGoogleTV = application.getPackageManager().hasSystemFeature("com.google.android.tv");
        try {
            final PackageInfo packageInfo = application.getPackageManager().getPackageInfo(application.getPackageName(), 0);
            this.mVersionCode = packageInfo.versionCode;
            this.mVersionName = packageInfo.versionName;
            final TelephonyManager telephonyManager = (TelephonyManager)application.getSystemService("phone");
            this.mDeviceId = new UUID(("" + Settings$Secure.getString(application.getContentResolver(), "android_id")).hashCode(), ("" + telephonyManager.getDeviceId()).hashCode() << 32 | ("" + telephonyManager.getSimSerialNumber()).hashCode()).toString();
        }
        catch (PackageManager$NameNotFoundException ex) {
            throw new RuntimeException((Throwable)ex);
        }
    }
    
    public int getAndroidApplicationVersionCode() {
        return this.mVersionCode;
    }
    
    public String getAndroidApplicationVersionName() {
        return this.mVersionName;
    }
    
    public boolean getAndroidDeviceIsGoogleTV() {
        return this.mIsGoogleTV;
    }
    
    public String getAndroidDeviceManufacturer() {
        return Build.MANUFACTURER;
    }
    
    public String getAndroidDeviceModel() {
        return Build.MODEL;
    }
    
    public String getAndroidDeviceProduct() {
        return Build.PRODUCT;
    }
    
    public String getAndroidRelease() {
        return Build$VERSION.RELEASE;
    }
    
    public int getAndroidSDK() {
        return Build$VERSION.SDK_INT;
    }
    
    public String getDeviceId() {
        return this.mDeviceId;
    }
    
    public String getDeviceType() {
        return "com.crunchyroll.crunchyroid";
    }
}
