// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.beta;

import org.json.JSONObject;
import io.fabric.sdk.android.Fabric;
import java.util.HashMap;
import java.util.Map;
import io.fabric.sdk.android.services.network.HttpRequest;
import io.fabric.sdk.android.services.network.HttpMethod;
import io.fabric.sdk.android.services.network.HttpRequestFactory;
import io.fabric.sdk.android.Kit;
import io.fabric.sdk.android.services.common.AbstractSpiCall;

class CheckForUpdatesRequest extends AbstractSpiCall
{
    private final CheckForUpdatesResponseTransform responseTransform;
    
    public CheckForUpdatesRequest(final Kit kit, final String s, final String s2, final HttpRequestFactory httpRequestFactory, final CheckForUpdatesResponseTransform responseTransform) {
        super(kit, s, s2, httpRequestFactory, HttpMethod.GET);
        this.responseTransform = responseTransform;
    }
    
    private HttpRequest applyHeadersTo(final HttpRequest httpRequest, final String s, final String s2) {
        return httpRequest.header("Accept", "application/json").header("User-Agent", "Crashlytics Android SDK/" + this.kit.getVersion()).header("X-CRASHLYTICS-DEVELOPER-TOKEN", "470fa2b4ae81cd56ecbcda9735803434cec591fa").header("X-CRASHLYTICS-API-CLIENT-TYPE", "android").header("X-CRASHLYTICS-API-CLIENT-VERSION", this.kit.getVersion()).header("X-CRASHLYTICS-API-KEY", s).header("X-CRASHLYTICS-D", s2);
    }
    
    private Map<String, String> getQueryParamsFor(final BuildProperties buildProperties) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("build_version", buildProperties.versionCode);
        hashMap.put("display_version", buildProperties.versionName);
        hashMap.put("instance", buildProperties.buildId);
        hashMap.put("source", "3");
        return hashMap;
    }
    
    public CheckForUpdatesResponse invoke(String header, String header2, final BuildProperties buildProperties) {
        final HttpRequest httpRequest = null;
        HttpRequest httpRequest3;
        final HttpRequest httpRequest2 = httpRequest3 = null;
        HttpRequest httpRequest4 = httpRequest;
        try {
            final Map<String, String> queryParams = this.getQueryParamsFor(buildProperties);
            httpRequest3 = httpRequest2;
            httpRequest4 = httpRequest;
            final HttpRequest httpRequest5 = httpRequest4 = (httpRequest3 = this.applyHeadersTo(httpRequest4 = (httpRequest3 = this.getHttpRequest(queryParams)), header, header2));
            Fabric.getLogger().d("Beta", "Checking for updates from " + this.getUrl());
            httpRequest3 = httpRequest5;
            httpRequest4 = httpRequest5;
            Fabric.getLogger().d("Beta", "Checking for updates query params are: " + queryParams);
            httpRequest3 = httpRequest5;
            httpRequest4 = httpRequest5;
            if (httpRequest5.ok()) {
                httpRequest3 = httpRequest5;
                httpRequest4 = httpRequest5;
                Fabric.getLogger().d("Beta", "Checking for updates was successful");
                httpRequest3 = httpRequest5;
                httpRequest4 = httpRequest5;
                final JSONObject jsonObject = new JSONObject(httpRequest5.body());
                httpRequest3 = httpRequest5;
                httpRequest4 = httpRequest5;
                return this.responseTransform.fromJson(jsonObject);
            }
            httpRequest3 = httpRequest5;
            httpRequest4 = httpRequest5;
            Fabric.getLogger().e("Beta", "Checking for updates failed. Response code: " + httpRequest5.code());
            return null;
        }
        catch (Exception ex) {
            httpRequest4 = httpRequest3;
            Fabric.getLogger().e("Beta", "Error while checking for updates from " + this.getUrl(), ex);
            if (httpRequest3 != null) {
                header = httpRequest3.header("X-REQUEST-ID");
                Fabric.getLogger().d("Fabric", "Checking for updates request ID: " + header);
                return null;
            }
            return null;
        }
        finally {
            if (httpRequest4 != null) {
                header2 = httpRequest4.header("X-REQUEST-ID");
                Fabric.getLogger().d("Fabric", "Checking for updates request ID: " + header2);
            }
        }
    }
}
