// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.requests;

import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.crunchyroll.android.api.ApiRequest;
import com.google.common.base.Optional;
import com.crunchyroll.android.api.AbstractApiRequest;

public class RecentlyWatchedRequest extends AbstractApiRequest
{
    private static final long serialVersionUID = 2361491940254006868L;
    private final Optional<Integer> limit;
    private final String mediaTypes;
    private final Optional<Integer> offset;
    
    public RecentlyWatchedRequest(final String s) {
        this(s, null, null);
    }
    
    public RecentlyWatchedRequest(final String mediaTypes, final Integer n, final Integer n2) {
        this.mediaTypes = mediaTypes;
        this.offset = Optional.fromNullable(n);
        this.limit = Optional.fromNullable(n2);
    }
    
    @Override
    public String getApiMethod() {
        return "recently_watched";
    }
    
    @Override
    public RequestMethod getMethod() {
        return RequestMethod.GET;
    }
    
    @Override
    public ImmutableMap<String, String> getParams() {
        final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        builder.put("media_types", this.mediaTypes);
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
        return "RecentlyWatchedRequest [getParams()=" + this.getParams() + "]";
    }
}
