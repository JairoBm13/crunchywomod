// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.requests;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.AbstractApiRequest;

public class ListLocalesRequest extends AbstractApiRequest
{
    private static final long serialVersionUID = 6190340749787204388L;
    
    @Override
    public String getApiMethod() {
        return "list_locales";
    }
    
    @Override
    public RequestMethod getMethod() {
        return RequestMethod.GET;
    }
    
    @Override
    public Map<String, String> getParams() {
        return (Map<String, String>)ImmutableMap.of();
    }
    
    @Override
    public int getVersion() {
        return 1;
    }
    
    @Override
    public String toString() {
        return "ListLocalesRequest [getParams()=" + this.getParams() + "]";
    }
}
