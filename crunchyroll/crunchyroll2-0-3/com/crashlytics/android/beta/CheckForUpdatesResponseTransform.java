// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.beta;

import java.io.IOException;
import org.json.JSONObject;

class CheckForUpdatesResponseTransform
{
    public CheckForUpdatesResponse fromJson(final JSONObject jsonObject) throws IOException {
        if (jsonObject == null) {
            return null;
        }
        return new CheckForUpdatesResponse(jsonObject.optString("url", (String)null), jsonObject.optString("version_string", (String)null), jsonObject.optString("display_version", (String)null), jsonObject.optString("build_version", (String)null), jsonObject.optString("identifier", (String)null), jsonObject.optString("instance_identifier", (String)null));
    }
}
