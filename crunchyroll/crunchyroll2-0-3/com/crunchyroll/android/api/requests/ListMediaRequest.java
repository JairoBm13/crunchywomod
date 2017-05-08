// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.requests;

import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.CacheKey;
import com.crunchyroll.android.api.models.Media;
import com.google.common.base.Optional;
import com.crunchyroll.android.api.AbstractApiRequest;

public final class ListMediaRequest extends AbstractApiRequest
{
    private static final long serialVersionUID = 990628066147263495L;
    private final Optional<Long> collectionId;
    private final Optional<Integer> limit;
    private final Optional<String> locale;
    private final Optional<Integer> offset;
    private final Optional<Long> seriesId;
    private final Optional<String> sort;
    
    public ListMediaRequest(final Long n, final Long n2, final String s) {
        this(n, n2, s, null, null, null);
    }
    
    public ListMediaRequest(final Long n, final Long n2, final String s, final Integer n3, final Integer n4) {
        this(n, n2, s, n3, n4, null);
    }
    
    public ListMediaRequest(final Long n, final Long n2, final String s, final Integer n3, final Integer n4, final String s2) {
        this.collectionId = Optional.fromNullable(n);
        this.seriesId = Optional.fromNullable(n2);
        this.sort = Optional.fromNullable(s);
        this.offset = Optional.fromNullable(n3);
        this.limit = Optional.fromNullable(n4);
        this.locale = Optional.fromNullable(s2);
    }
    
    @Override
    public String getApiMethod() {
        return "list_media";
    }
    
    public Optional<Long> getCollectionId() {
        return this.collectionId;
    }
    
    @Override
    public Object getKey() {
        if (this.seriesId.isPresent()) {
            return new CacheKey(Media.class, this.getSeriesId());
        }
        return new CacheKey(Media.class, this.getCollectionId());
    }
    
    public Optional<Integer> getLimit() {
        return this.limit;
    }
    
    public Optional<String> getLocale() {
        return this.locale;
    }
    
    @Override
    public RequestMethod getMethod() {
        return RequestMethod.GET;
    }
    
    public Optional<Integer> getOffset() {
        return this.offset;
    }
    
    @Override
    public ImmutableMap<String, String> getParams() {
        final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        if (this.collectionId.isPresent()) {
            builder.put("collection_id", this.collectionId.get().toString());
        }
        if (this.seriesId.isPresent()) {
            builder.put("series_id", this.seriesId.get().toString());
        }
        if (this.sort.isPresent()) {
            builder.put("sort", this.sort.get());
        }
        if (this.offset.isPresent()) {
            builder.put("offset", this.offset.get().toString());
        }
        if (this.limit.isPresent()) {
            builder.put("limit", this.limit.get().toString());
        }
        if (this.locale.isPresent()) {
            builder.put("locale", this.locale.get());
        }
        return builder.build();
    }
    
    public Optional<Long> getSeriesId() {
        return this.seriesId;
    }
    
    public Optional<String> getSort() {
        return this.sort;
    }
    
    @Override
    public String toString() {
        return "ListMediaRequest [getParams()=" + this.getParams() + "]";
    }
}
