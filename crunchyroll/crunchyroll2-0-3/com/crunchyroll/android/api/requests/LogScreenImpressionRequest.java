// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.requests;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.AbstractApiRequest;

public class LogScreenImpressionRequest extends AbstractApiRequest
{
    private static final String PARAM_VIEW = "view";
    private static final long serialVersionUID = 4295378062450429612L;
    private final String viewName;
    
    public LogScreenImpressionRequest(final String viewName) {
        this.viewName = viewName;
    }
    
    @Override
    public String getApiMethod() {
        return "log_impression";
    }
    
    @Override
    public RequestMethod getMethod() {
        return RequestMethod.POST;
    }
    
    @Override
    public Map<String, String> getParams() {
        return ImmutableMap.of("view", this.viewName);
    }
    
    @Override
    public String toString() {
        return String.format("%s: %s", this.getApiMethod(), this.getParams());
    }
}
