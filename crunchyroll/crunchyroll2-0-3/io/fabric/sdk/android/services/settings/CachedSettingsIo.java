// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.settings;

import org.json.JSONObject;

public interface CachedSettingsIo
{
    JSONObject readCachedSettings();
    
    void writeCachedSettings(final long p0, final JSONObject p1);
}
