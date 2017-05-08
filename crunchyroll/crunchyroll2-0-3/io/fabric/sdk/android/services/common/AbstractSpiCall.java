// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.common;

import java.util.Map;
import java.util.Collections;
import io.fabric.sdk.android.services.network.HttpRequest;
import io.fabric.sdk.android.services.network.HttpRequestFactory;
import io.fabric.sdk.android.services.network.HttpMethod;
import io.fabric.sdk.android.Kit;
import java.util.regex.Pattern;

public abstract class AbstractSpiCall
{
    private static final Pattern PROTOCOL_AND_HOST_PATTERN;
    protected final Kit kit;
    private final HttpMethod method;
    private final String protocolAndHostOverride;
    private final HttpRequestFactory requestFactory;
    private final String url;
    
    static {
        PROTOCOL_AND_HOST_PATTERN = Pattern.compile("http(s?)://[^\\/]+", 2);
    }
    
    public AbstractSpiCall(final Kit kit, final String protocolAndHostOverride, final String s, final HttpRequestFactory requestFactory, final HttpMethod method) {
        if (s == null) {
            throw new IllegalArgumentException("url must not be null.");
        }
        if (requestFactory == null) {
            throw new IllegalArgumentException("requestFactory must not be null.");
        }
        this.kit = kit;
        this.protocolAndHostOverride = protocolAndHostOverride;
        this.url = this.overrideProtocolAndHost(s);
        this.requestFactory = requestFactory;
        this.method = method;
    }
    
    private String overrideProtocolAndHost(final String s) {
        String replaceFirst = s;
        if (!CommonUtils.isNullOrEmpty(this.protocolAndHostOverride)) {
            replaceFirst = AbstractSpiCall.PROTOCOL_AND_HOST_PATTERN.matcher(s).replaceFirst(this.protocolAndHostOverride);
        }
        return replaceFirst;
    }
    
    protected HttpRequest getHttpRequest() {
        return this.getHttpRequest(Collections.emptyMap());
    }
    
    protected HttpRequest getHttpRequest(final Map<String, String> map) {
        return this.requestFactory.buildHttpRequest(this.method, this.getUrl(), map).useCaches(false).connectTimeout(10000).header("User-Agent", "Crashlytics Android SDK/" + this.kit.getVersion()).header("X-CRASHLYTICS-DEVELOPER-TOKEN", "470fa2b4ae81cd56ecbcda9735803434cec591fa");
    }
    
    protected String getUrl() {
        return this.url;
    }
}
