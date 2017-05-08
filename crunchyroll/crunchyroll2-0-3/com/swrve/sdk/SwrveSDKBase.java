// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk;

import android.app.Activity;
import java.util.Map;
import android.util.Log;

public abstract class SwrveSDKBase
{
    protected static ISwrveBase instance;
    
    protected static void checkInstanceCreated() throws RuntimeException {
        if (SwrveSDKBase.instance == null) {
            Log.e("SwrveSDK", "Please call SwrveSDK.createInstance first in your Application class.");
            throw new RuntimeException("Please call SwrveSDK.createInstance first in your Application class.");
        }
    }
    
    public static void event(final String s) {
        checkInstanceCreated();
        SwrveSDKBase.instance.event(s);
    }
    
    public static void event(final String s, final Map<String, String> map) {
        checkInstanceCreated();
        SwrveSDKBase.instance.event(s, map);
    }
    
    public static ISwrveBase getInstance() {
        return SwrveSDKBase.instance;
    }
    
    public static SwrveResourceManager getResourceManager() {
        checkInstanceCreated();
        return SwrveSDKBase.instance.getResourceManager();
    }
    
    public static void onCreate(final Activity activity) {
        checkInstanceCreated();
        SwrveSDKBase.instance.onCreate(activity);
    }
    
    public static void onDestroy(final Activity activity) {
        checkInstanceCreated();
        SwrveSDKBase.instance.onDestroy(activity);
    }
    
    public static void onLowMemory() {
        checkInstanceCreated();
        SwrveSDKBase.instance.onLowMemory();
    }
    
    public static void onPause() {
        checkInstanceCreated();
        SwrveSDKBase.instance.onPause();
    }
    
    public static void onResume(final Activity activity) {
        checkInstanceCreated();
        SwrveSDKBase.instance.onResume(activity);
    }
    
    public static void userUpdate(final Map<String, String> map) {
        checkInstanceCreated();
        SwrveSDKBase.instance.userUpdate(map);
    }
}
