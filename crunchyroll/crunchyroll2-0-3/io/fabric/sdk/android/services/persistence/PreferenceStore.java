// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.persistence;

import android.content.SharedPreferences;
import android.content.SharedPreferences$Editor;

public interface PreferenceStore
{
    SharedPreferences$Editor edit();
    
    SharedPreferences get();
    
    boolean save(final SharedPreferences$Editor p0);
}
