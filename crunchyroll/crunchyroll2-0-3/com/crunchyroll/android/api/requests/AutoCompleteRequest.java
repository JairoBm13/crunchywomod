// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.requests;

import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.crunchyroll.android.api.ApiRequest;
import com.google.common.base.Optional;
import java.io.Serializable;
import com.crunchyroll.android.api.AbstractApiRequest;

public final class AutoCompleteRequest extends AbstractApiRequest implements Serializable
{
    private final Optional<Integer> limit;
    private final String mediaType;
    private final Optional<Integer> offset;
    private final String searchString;
    
    public AutoCompleteRequest(final String searchString, final String mediaType, final Integer n, final Integer n2) {
        this.searchString = searchString;
        this.mediaType = mediaType;
        this.offset = Optional.fromNullable(n);
        this.limit = Optional.fromNullable(n2);
    }
    
    @Override
    public String getApiMethod() {
        return "autocomplete";
    }
    
    @Override
    public RequestMethod getMethod() {
        return RequestMethod.GET;
    }
    
    @Override
    public ImmutableMap<String, String> getParams() {
        final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        builder.put("q", this.searchString);
        builder.put("media_types", this.mediaType);
        if (this.offset.isPresent()) {
            builder.put("offset", this.offset.get().toString());
        }
        if (this.limit.isPresent()) {
            builder.put("limit", this.limit.get().toString());
        }
        return builder.build();
    }
    
    @Override
    public String toString() {
        return "AutoCompleteRequest [getParams()=" + this.getParams() + "]";
    }
}
