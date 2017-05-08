// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.requests;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.AbstractApiRequest;

public class LogFirstLaunchRequest extends AbstractApiRequest
{
    private static final long serialVersionUID = -4648771967881981380L;
    
    @Override
    public String getApiMethod() {
        return "log_first_launch";
    }
    
    @Override
    public RequestMethod getMethod() {
        return RequestMethod.POST;
    }
    
    @Override
    public Map<String, String> getParams() {
        return (Map<String, String>)ImmutableMap.of();
    }
}
