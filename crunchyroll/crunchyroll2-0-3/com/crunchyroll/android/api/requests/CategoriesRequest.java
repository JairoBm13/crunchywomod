// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.requests;

import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.AbstractApiRequest;

public final class CategoriesRequest extends AbstractApiRequest
{
    private static final long serialVersionUID = -3717190103483442635L;
    private final String mediaType;
    
    public CategoriesRequest(final String mediaType) {
        this.mediaType = mediaType;
    }
    
    @Override
    public String getApiMethod() {
        return "categories";
    }
    
    @Override
    public RequestMethod getMethod() {
        return RequestMethod.GET;
    }
    
    @Override
    public ImmutableMap<String, String> getParams() {
        return ImmutableMap.of("media_type", this.mediaType);
    }
    
    @Override
    public String toString() {
        return "CategoriesRequest [getParams()=" + this.getParams() + "]";
    }
}
