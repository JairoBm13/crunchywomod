// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.settings;

import org.json.JSONException;
import org.json.JSONObject;
import io.fabric.sdk.android.services.common.CurrentTimeProvider;

public interface SettingsJsonTransform
{
    SettingsData buildFromJson(final CurrentTimeProvider p0, final JSONObject p1) throws JSONException;
}
