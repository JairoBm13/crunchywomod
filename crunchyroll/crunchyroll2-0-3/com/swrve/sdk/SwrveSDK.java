// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk;

import com.swrve.sdk.config.SwrveConfig;
import android.content.Context;

public class SwrveSDK extends SwrveSDKBase
{
    public static ISwrve createInstance(final Context context, final int n, final String s) {
        return createInstance(context, n, s, new SwrveConfig());
    }
    
    public static ISwrve createInstance(final Context context, final int n, final String s, final SwrveConfig swrveConfig) {
        if (context == null) {
            SwrveHelper.logAndThrowException("Context not specified");
        }
        else if (SwrveHelper.isNullOrEmpty(s)) {
            SwrveHelper.logAndThrowException("Api key not specified");
        }
        if (!SwrveHelper.sdkAvailable()) {
            return new SwrveEmpty(context, s);
        }
        if (SwrveSDK.instance == null) {
            SwrveSDK.instance = new Swrve(context, n, s, swrveConfig);
        }
        return (ISwrve)SwrveSDK.instance;
    }
}
