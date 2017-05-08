// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.requests;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.AbstractApiRequest;

public class LogAdServedRequest extends AbstractApiRequest
{
    private static final long serialVersionUID = 6500297813024178526L;
    private final AdIntegration integration;
    
    public LogAdServedRequest(final AdIntegration integration) {
        this.integration = integration;
    }
    
    @Override
    public String getApiMethod() {
        return "log_ad_served";
    }
    
    @Override
    public RequestMethod getMethod() {
        return RequestMethod.POST;
    }
    
    @Override
    public Map<String, String> getParams() {
        return ImmutableMap.of("ad_network", this.integration.toString());
    }
}
