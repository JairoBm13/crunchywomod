// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.requests;

import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.AbstractApiRequest;

public final class AddToQueueRequest extends AbstractApiRequest
{
    private static final long serialVersionUID = -916710166179172283L;
    private final Long seriesId;
    
    public AddToQueueRequest(final Long seriesId) {
        this.seriesId = seriesId;
    }
    
    @Override
    public String getApiMethod() {
        return "add_to_queue";
    }
    
    @Override
    public RequestMethod getMethod() {
        return RequestMethod.POST;
    }
    
    @Override
    public ImmutableMap<String, String> getParams() {
        return ImmutableMap.of("series_id", this.seriesId.toString());
    }
    
    public Long getSeriesId() {
        return this.seriesId;
    }
    
    @Override
    public String toString() {
        return "AddToQueueRequest [getParams()=" + this.getParams() + "]";
    }
}
