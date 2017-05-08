// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.answers;

import java.util.Iterator;
import io.fabric.sdk.android.services.network.HttpRequest;
import io.fabric.sdk.android.services.common.ResponseParser;
import io.fabric.sdk.android.Fabric;
import java.io.File;
import java.util.List;
import io.fabric.sdk.android.services.network.HttpMethod;
import io.fabric.sdk.android.services.network.HttpRequestFactory;
import io.fabric.sdk.android.Kit;
import io.fabric.sdk.android.services.events.FilesSender;
import io.fabric.sdk.android.services.common.AbstractSpiCall;

class SessionAnalyticsFilesSender extends AbstractSpiCall implements FilesSender
{
    private final String apiKey;
    
    public SessionAnalyticsFilesSender(final Kit kit, final String s, final String s2, final HttpRequestFactory httpRequestFactory, final String apiKey) {
        super(kit, s, s2, httpRequestFactory, HttpMethod.POST);
        this.apiKey = apiKey;
    }
    
    @Override
    public boolean send(final List<File> list) {
        final HttpRequest header = this.getHttpRequest().header("X-CRASHLYTICS-API-CLIENT-TYPE", "android").header("X-CRASHLYTICS-API-CLIENT-VERSION", this.kit.getVersion()).header("X-CRASHLYTICS-API-KEY", this.apiKey);
        int n = 0;
        for (final File file : list) {
            header.part("session_analytics_file_" + n, file.getName(), "application/vnd.crashlytics.android.events", file);
            ++n;
        }
        Fabric.getLogger().d("Answers", "Sending " + list.size() + " analytics files to " + this.getUrl());
        final int code = header.code();
        Fabric.getLogger().d("Answers", "Response code for analytics file send is " + code);
        return ResponseParser.parse(code) == 0;
    }
}
