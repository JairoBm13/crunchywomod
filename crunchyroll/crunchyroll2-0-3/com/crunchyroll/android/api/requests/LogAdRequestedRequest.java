// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.requests;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.AbstractApiRequest;

public class LogAdRequestedRequest extends AbstractApiRequest
{
    private static final long serialVersionUID = 7781180389393289219L;
    private final AdIntegration integration;
    
    public LogAdRequestedRequest(final AdIntegration integration) {
        this.integration = integration;
    }
    
    @Override
    public String getApiMethod() {
        return "log_ad_requested";
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
