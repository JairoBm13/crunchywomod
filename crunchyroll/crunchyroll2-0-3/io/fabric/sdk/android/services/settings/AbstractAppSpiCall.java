// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.settings;

import java.util.Iterator;
import io.fabric.sdk.android.services.common.ResponseParser;
import java.util.Locale;
import android.content.res.Resources$NotFoundException;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.KitInfo;
import java.io.Closeable;
import java.io.InputStream;
import io.fabric.sdk.android.services.common.CommonUtils;
import io.fabric.sdk.android.services.network.HttpRequest;
import io.fabric.sdk.android.services.network.HttpMethod;
import io.fabric.sdk.android.services.network.HttpRequestFactory;
import io.fabric.sdk.android.Kit;
import io.fabric.sdk.android.services.common.AbstractSpiCall;

abstract class AbstractAppSpiCall extends AbstractSpiCall
{
    public AbstractAppSpiCall(final Kit kit, final String s, final String s2, final HttpRequestFactory httpRequestFactory, final HttpMethod httpMethod) {
        super(kit, s, s2, httpRequestFactory, httpMethod);
    }
    
    private HttpRequest applyHeadersTo(final HttpRequest httpRequest, final AppRequestData appRequestData) {
        return httpRequest.header("X-CRASHLYTICS-API-KEY", appRequestData.apiKey).header("X-CRASHLYTICS-API-CLIENT-TYPE", "android").header("X-CRASHLYTICS-API-CLIENT-VERSION", this.kit.getVersion());
    }
    
    private HttpRequest applyMultipartDataTo(HttpRequest httpRequest, AppRequestData appRequestData) {
        final HttpRequest part = httpRequest.part("app[identifier]", appRequestData.appId).part("app[name]", appRequestData.name).part("app[display_version]", appRequestData.displayVersion).part("app[build_version]", appRequestData.buildVersion).part("app[source]", appRequestData.source).part("app[minimum_sdk_version]", appRequestData.minSdkVersion).part("app[built_sdk_version]", appRequestData.builtSdkVersion);
        if (!CommonUtils.isNullOrEmpty(appRequestData.instanceIdentifier)) {
            part.part("app[instance_identifier]", appRequestData.instanceIdentifier);
        }
        while (true) {
            if (appRequestData.icon != null) {
                Object o = null;
                httpRequest = null;
                try {
                    final HttpRequest httpRequest2 = (HttpRequest)(o = (httpRequest = (HttpRequest)this.kit.getContext().getResources().openRawResource(appRequestData.icon.iconResourceId)));
                    part.part("app[icon][hash]", appRequestData.icon.hash).part("app[icon][data]", "icon.png", "application/octet-stream", (InputStream)httpRequest2).part("app[icon][width]", appRequestData.icon.width).part("app[icon][height]", appRequestData.icon.height);
                    CommonUtils.closeOrLog((Closeable)httpRequest2, "Failed to close app icon InputStream.");
                    if (appRequestData.sdkKits != null) {
                        httpRequest = (HttpRequest)appRequestData.sdkKits.iterator();
                        while (((Iterator)httpRequest).hasNext()) {
                            appRequestData = (AppRequestData)((Iterator<KitInfo>)httpRequest).next();
                            part.part(this.getKitVersionKey((KitInfo)appRequestData), ((KitInfo)appRequestData).getVersion());
                            part.part(this.getKitBuildTypeKey((KitInfo)appRequestData), ((KitInfo)appRequestData).getBuildType());
                        }
                    }
                }
                catch (Resources$NotFoundException ex) {
                    o = httpRequest;
                    Fabric.getLogger().e("Fabric", "Failed to find app icon with resource ID: " + appRequestData.icon.iconResourceId, (Throwable)ex);
                    CommonUtils.closeOrLog((Closeable)httpRequest, "Failed to close app icon InputStream.");
                    continue;
                }
                finally {
                    CommonUtils.closeOrLog((Closeable)o, "Failed to close app icon InputStream.");
                }
                return part;
            }
            continue;
        }
    }
    
    String getKitBuildTypeKey(final KitInfo kitInfo) {
        return String.format(Locale.US, "app[build][libraries][%s][type]", kitInfo.getIdentifier());
    }
    
    String getKitVersionKey(final KitInfo kitInfo) {
        return String.format(Locale.US, "app[build][libraries][%s][version]", kitInfo.getIdentifier());
    }
    
    public boolean invoke(final AppRequestData appRequestData) {
        final HttpRequest applyMultipartDataTo = this.applyMultipartDataTo(this.applyHeadersTo(this.getHttpRequest(), appRequestData), appRequestData);
        Fabric.getLogger().d("Fabric", "Sending app info to " + this.getUrl());
        if (appRequestData.icon != null) {
            Fabric.getLogger().d("Fabric", "App icon hash is " + appRequestData.icon.hash);
            Fabric.getLogger().d("Fabric", "App icon size is " + appRequestData.icon.width + "x" + appRequestData.icon.height);
        }
        final int code = applyMultipartDataTo.code();
        String s;
        if ("POST".equals(applyMultipartDataTo.method())) {
            s = "Create";
        }
        else {
            s = "Update";
        }
        Fabric.getLogger().d("Fabric", s + " app request ID: " + applyMultipartDataTo.header("X-REQUEST-ID"));
        Fabric.getLogger().d("Fabric", "Result was " + code);
        return ResponseParser.parse(code) == 0;
    }
}
