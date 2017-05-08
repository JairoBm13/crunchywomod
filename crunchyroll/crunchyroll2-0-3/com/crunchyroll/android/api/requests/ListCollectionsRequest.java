// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.requests;

import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.CacheKey;
import com.crunchyroll.android.api.models.Collection;
import com.google.common.base.Optional;
import com.crunchyroll.android.api.AbstractApiRequest;

public class ListCollectionsRequest extends AbstractApiRequest
{
    private Optional<Integer> limit;
    private Optional<Integer> offset;
    private Long seriesId;
    private Optional<String> sort;
    
    public ListCollectionsRequest(final Long seriesId, final String s, final Integer n, final Integer n2) {
        this.seriesId = seriesId;
        this.sort = Optional.fromNullable(s);
        this.offset = Optional.fromNullable(n);
        this.limit = Optional.fromNullable(n2);
    }
    
    @Override
    public String getApiMethod() {
        return "list_collections";
    }
    
    @Override
    public Object getKey() {
        return new CacheKey(Collection.class, Optional.of(this.seriesId));
    }
    
    @Override
    public RequestMethod getMethod() {
        return RequestMethod.GET;
    }
    
    @Override
    public ImmutableMap<String, String> getParams() {
        final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        builder.put("series_id", this.seriesId.toString());
        if (this.sort.isPresent()) {
            builder.put("sort", this.sort.get());
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
        return "ListCollectionsRequest [getParams()=" + this.getParams() + "]";
    }
}
