// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.requests;

import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.AbstractApiRequest;

public final class ClientOptionsRequest extends AbstractApiRequest
{
    @Override
    public String getApiMethod() {
        return "client_options";
    }
    
    @Override
    public RequestMethod getMethod() {
        return RequestMethod.POST;
    }
    
    @Override
    public ImmutableMap<String, String> getParams() {
        return ImmutableMap.of();
    }
    
    @Override
    public String toString() {
        return "ClientOptionsRequest [getParams()=" + this.getParams() + "]";
    }
}
