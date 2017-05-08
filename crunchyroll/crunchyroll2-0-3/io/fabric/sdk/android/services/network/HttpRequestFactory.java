// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.network;

import java.util.Map;

public interface HttpRequestFactory
{
    HttpRequest buildHttpRequest(final HttpMethod p0, final String p1, final Map<String, String> p2);
    
    void setPinningInfoProvider(final PinningInfoProvider p0);
}
