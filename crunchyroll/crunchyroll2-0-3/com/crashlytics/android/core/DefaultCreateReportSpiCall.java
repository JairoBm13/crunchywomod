// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.core;

import io.fabric.sdk.android.services.common.ResponseParser;
import io.fabric.sdk.android.Fabric;
import java.util.Iterator;
import java.util.Map;
import io.fabric.sdk.android.services.network.HttpRequest;
import io.fabric.sdk.android.services.network.HttpMethod;
import io.fabric.sdk.android.services.network.HttpRequestFactory;
import io.fabric.sdk.android.Kit;
import io.fabric.sdk.android.services.common.AbstractSpiCall;

class DefaultCreateReportSpiCall extends AbstractSpiCall implements CreateReportSpiCall
{
    public DefaultCreateReportSpiCall(final Kit kit, final String s, final String s2, final HttpRequestFactory httpRequestFactory) {
        super(kit, s, s2, httpRequestFactory, HttpMethod.POST);
    }
    
    private HttpRequest applyHeadersTo(HttpRequest httpRequest, final CreateReportRequest createReportRequest) {
        httpRequest = httpRequest.header("X-CRASHLYTICS-API-KEY", createReportRequest.apiKey).header("X-CRASHLYTICS-API-CLIENT-TYPE", "android").header("X-CRASHLYTICS-API-CLIENT-VERSION", CrashlyticsCore.getInstance().getVersion());
        final Iterator<Map.Entry<String, String>> iterator = createReportRequest.report.getCustomHeaders().entrySet().iterator();
        while (iterator.hasNext()) {
            httpRequest = httpRequest.header((Map.Entry<String, String>)iterator.next());
        }
        return httpRequest;
    }
    
    private HttpRequest applyMultipartDataTo(final HttpRequest httpRequest, final CreateReportRequest createReportRequest) {
        final Report report = createReportRequest.report;
        return httpRequest.part("report[file]", report.getFileName(), "application/octet-stream", report.getFile()).part("report[identifier]", report.getIdentifier());
    }
    
    @Override
    public boolean invoke(final CreateReportRequest createReportRequest) {
        final HttpRequest applyMultipartDataTo = this.applyMultipartDataTo(this.applyHeadersTo(this.getHttpRequest(), createReportRequest), createReportRequest);
        Fabric.getLogger().d("CrashlyticsCore", "Sending report to: " + this.getUrl());
        final int code = applyMultipartDataTo.code();
        Fabric.getLogger().d("CrashlyticsCore", "Create report request ID: " + applyMultipartDataTo.header("X-REQUEST-ID"));
        Fabric.getLogger().d("CrashlyticsCore", "Result was: " + code);
        return ResponseParser.parse(code) == 0;
    }
}
