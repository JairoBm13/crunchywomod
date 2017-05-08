// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk;

import android.app.Activity;
import java.util.Map;
import com.swrve.sdk.config.SwrveConfigBase;

public interface ISwrveBase<T, C extends SwrveConfigBase>
{
    void event(final String p0);
    
    void event(final String p0, final Map<String, String> p1);
    
    SwrveResourceManager getResourceManager();
    
    T onCreate(final Activity p0) throws IllegalArgumentException;
    
    void onDestroy(final Activity p0);
    
    void onLowMemory();
    
    void onPause();
    
    void onResume(final Activity p0);
    
    void userUpdate(final Map<String, String> p0);
}
