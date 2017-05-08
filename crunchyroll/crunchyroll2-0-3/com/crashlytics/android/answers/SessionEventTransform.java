// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.answers;

import android.annotation.TargetApi;
import org.json.JSONException;
import java.io.IOException;
import android.os.Build$VERSION;
import java.util.Map;
import org.json.JSONObject;
import io.fabric.sdk.android.services.events.EventTransform;

class SessionEventTransform implements EventTransform<SessionEvent>
{
    @TargetApi(9)
    public JSONObject buildJsonForEvent(final SessionEvent sessionEvent) throws IOException {
        try {
            final JSONObject jsonObject = new JSONObject();
            final SessionEventMetadata sessionEventMetadata = sessionEvent.sessionEventMetadata;
            jsonObject.put("appBundleId", (Object)sessionEventMetadata.appBundleId);
            jsonObject.put("executionId", (Object)sessionEventMetadata.executionId);
            jsonObject.put("installationId", (Object)sessionEventMetadata.installationId);
            jsonObject.put("androidId", (Object)sessionEventMetadata.androidId);
            jsonObject.put("advertisingId", (Object)sessionEventMetadata.advertisingId);
            jsonObject.put("limitAdTrackingEnabled", (Object)sessionEventMetadata.limitAdTrackingEnabled);
            jsonObject.put("betaDeviceToken", (Object)sessionEventMetadata.betaDeviceToken);
            jsonObject.put("buildId", (Object)sessionEventMetadata.buildId);
            jsonObject.put("osVersion", (Object)sessionEventMetadata.osVersion);
            jsonObject.put("deviceModel", (Object)sessionEventMetadata.deviceModel);
            jsonObject.put("appVersionCode", (Object)sessionEventMetadata.appVersionCode);
            jsonObject.put("appVersionName", (Object)sessionEventMetadata.appVersionName);
            jsonObject.put("timestamp", sessionEvent.timestamp);
            jsonObject.put("type", (Object)sessionEvent.type.toString());
            jsonObject.put("details", (Object)new JSONObject((Map)sessionEvent.details));
            jsonObject.put("customType", (Object)sessionEvent.customType);
            jsonObject.put("customAttributes", (Object)new JSONObject((Map)sessionEvent.customAttributes));
            jsonObject.put("predefinedType", (Object)sessionEvent.predefinedType);
            jsonObject.put("predefinedAttributes", (Object)new JSONObject((Map)sessionEvent.predefinedAttributes));
            return jsonObject;
        }
        catch (JSONException ex) {
            if (Build$VERSION.SDK_INT >= 9) {
                throw new IOException(ex.getMessage(), (Throwable)ex);
            }
            throw new IOException(ex.getMessage());
        }
    }
    
    @Override
    public byte[] toBytes(final SessionEvent sessionEvent) throws IOException {
        return this.buildJsonForEvent(sessionEvent).toString().getBytes("UTF-8");
    }
}
