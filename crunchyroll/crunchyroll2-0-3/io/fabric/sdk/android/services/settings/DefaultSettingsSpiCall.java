// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.settings;

import io.fabric.sdk.android.services.common.CommonUtils;
import java.util.HashMap;
import java.util.Map;
import io.fabric.sdk.android.Fabric;
import org.json.JSONObject;
import io.fabric.sdk.android.services.network.HttpRequest;
import io.fabric.sdk.android.services.network.HttpMethod;
import io.fabric.sdk.android.services.network.HttpRequestFactory;
import io.fabric.sdk.android.Kit;
import io.fabric.sdk.android.services.common.AbstractSpiCall;

class DefaultSettingsSpiCall extends AbstractSpiCall implements SettingsSpiCall
{
    public DefaultSettingsSpiCall(final Kit kit, final String s, final String s2, final HttpRequestFactory httpRequestFactory) {
        this(kit, s, s2, httpRequestFactory, HttpMethod.GET);
    }
    
    DefaultSettingsSpiCall(final Kit kit, final String s, final String s2, final HttpRequestFactory httpRequestFactory, final HttpMethod httpMethod) {
        super(kit, s, s2, httpRequestFactory, httpMethod);
    }
    
    private HttpRequest applyHeadersTo(final HttpRequest httpRequest, final SettingsRequest settingsRequest) {
        return httpRequest.header("X-CRASHLYTICS-API-KEY", settingsRequest.apiKey).header("X-CRASHLYTICS-API-CLIENT-TYPE", "android").header("X-CRASHLYTICS-D", settingsRequest.deviceId).header("X-CRASHLYTICS-API-CLIENT-VERSION", this.kit.getVersion()).header("Accept", "application/json");
    }
    
    private JSONObject getJsonObjectFrom(final String s) {
        try {
            return new JSONObject(s);
        }
        catch (Exception ex) {
            Fabric.getLogger().d("Fabric", "Failed to parse settings JSON from " + this.getUrl(), ex);
            Fabric.getLogger().d("Fabric", "Settings response " + s);
            return null;
        }
    }
    
    private Map<String, String> getQueryParamsFor(final SettingsRequest settingsRequest) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("build_version", settingsRequest.buildVersion);
        hashMap.put("display_version", settingsRequest.displayVersion);
        hashMap.put("source", Integer.toString(settingsRequest.source));
        if (settingsRequest.iconHash != null) {
            hashMap.put("icon_hash", settingsRequest.iconHash);
        }
        final String instanceId = settingsRequest.instanceId;
        if (!CommonUtils.isNullOrEmpty(instanceId)) {
            hashMap.put("instance", instanceId);
        }
        return hashMap;
    }
    
    JSONObject handleResponse(final HttpRequest httpRequest) {
        final int code = httpRequest.code();
        Fabric.getLogger().d("Fabric", "Settings result was: " + code);
        if (this.requestWasSuccessful(code)) {
            return this.getJsonObjectFrom(httpRequest.body());
        }
        Fabric.getLogger().e("Fabric", "Failed to retrieve settings from " + this.getUrl());
        return null;
    }
    
    @Override
    public JSONObject invoke(final SettingsRequest settingsRequest) {
        HttpRequest httpRequest2;
        final HttpRequest httpRequest = httpRequest2 = null;
        try {
            final Map<String, String> queryParams = this.getQueryParamsFor(settingsRequest);
            httpRequest2 = httpRequest;
            final HttpRequest httpRequest3 = httpRequest2 = this.applyHeadersTo(httpRequest2 = this.getHttpRequest(queryParams), settingsRequest);
            Fabric.getLogger().d("Fabric", "Requesting settings from " + this.getUrl());
            httpRequest2 = httpRequest3;
            Fabric.getLogger().d("Fabric", "Settings query params were: " + queryParams);
            httpRequest2 = httpRequest3;
            return this.handleResponse(httpRequest3);
        }
        finally {
            if (httpRequest2 != null) {
                Fabric.getLogger().d("Fabric", "Settings request ID: " + httpRequest2.header("X-REQUEST-ID"));
            }
        }
    }
    
    boolean requestWasSuccessful(final int n) {
        return n == 200 || n == 201 || n == 202 || n == 203;
    }
}
