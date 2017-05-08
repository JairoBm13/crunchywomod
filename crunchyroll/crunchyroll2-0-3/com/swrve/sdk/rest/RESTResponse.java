// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.rest;

import java.util.List;
import java.util.Map;

public class RESTResponse
{
    public String responseBody;
    public int responseCode;
    public Map<String, List<String>> responseHeaders;
    
    public RESTResponse(final int responseCode, final String responseBody, final Map<String, List<String>> responseHeaders) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
        this.responseHeaders = responseHeaders;
    }
    
    public String getHeaderValue(final String s) {
        if (this.responseHeaders != null) {
            final List<String> list = this.responseHeaders.get(s);
            if (list != null && list.size() > 0) {
                return list.get(0);
            }
        }
        return null;
    }
}
