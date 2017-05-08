// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.requests;

import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.crunchyroll.android.api.ApiRequest;
import com.google.common.base.Optional;
import com.crunchyroll.android.api.AbstractApiRequest;

public class QueueRequest extends AbstractApiRequest
{
    private static final long serialVersionUID = 4547401178315012063L;
    private final Optional<String> mediaTypes;
    
    public QueueRequest() {
        this(null);
    }
    
    public QueueRequest(final String s) {
        this.mediaTypes = Optional.fromNullable(s);
    }
    
    @Override
    public String getApiMethod() {
        return "queue";
    }
    
    @Override
    public RequestMethod getMethod() {
        return RequestMethod.GET;
    }
    
    @Override
    public ImmutableMap<String, String> getParams() {
        if (this.mediaTypes.isPresent()) {
            return ImmutableMap.of("media_types", this.mediaTypes.get());
        }
        return ImmutableMap.of();
    }
    
    @Override
    public String toString() {
        return "QueueRequest [getParams()=" + this.getParams() + "]";
    }
}
