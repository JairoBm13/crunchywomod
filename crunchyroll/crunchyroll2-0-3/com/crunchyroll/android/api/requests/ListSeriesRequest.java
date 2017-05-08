// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.requests;

import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.crunchyroll.android.api.ApiRequest;
import com.google.common.base.Optional;
import com.crunchyroll.android.api.AbstractApiRequest;

public final class ListSeriesRequest extends AbstractApiRequest
{
    private static final long serialVersionUID = -2730403602408131600L;
    private final Optional<String> filter;
    private final Optional<Integer> limit;
    private final String mediaType;
    private final Optional<Integer> offset;
    
    public ListSeriesRequest(final String s, final String s2) {
        this(s, s2, null, null);
    }
    
    public ListSeriesRequest(final String mediaType, final String s, final Integer n, final Integer n2) {
        this.mediaType = mediaType;
        this.filter = Optional.fromNullable(s);
        this.offset = Optional.fromNullable(n);
        this.limit = Optional.fromNullable(n2);
    }
    
    @Override
    public String getApiMethod() {
        return "list_series";
    }
    
    @Override
    public RequestMethod getMethod() {
        return RequestMethod.GET;
    }
    
    @Override
    public ImmutableMap<String, String> getParams() {
        final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        builder.put("media_type", this.mediaType);
        if (this.filter.isPresent()) {
            builder.put("filter", this.filter.get());
        }
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
        return "ListSeriesRequest [getParams()=" + this.getParams() + "]";
    }
}
