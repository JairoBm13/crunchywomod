// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.requests;

import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.crunchyroll.android.api.ApiRequest;
import com.google.common.base.Optional;
import com.crunchyroll.android.api.AbstractApiRequest;

public final class InfoRequest extends AbstractApiRequest
{
    private static final long serialVersionUID = -8249046434151175556L;
    private final Optional<Long> collectionId;
    private final Optional<Long> mediaId;
    private final Optional<Long> seriesId;
    
    public InfoRequest(final Long n, final Long n2, final Long n3) {
        if (n != null) {
            if (n2 != null || n3 != null) {
                throw new IllegalArgumentException("Not allowed to have more than one ID specified!");
            }
        }
        else {
            if (n2 == null && n3 == null) {
                throw new IllegalArgumentException("Not allowed to have zero IDs specified!");
            }
            if (n2 != null && n3 != null) {
                throw new IllegalArgumentException("Not allowed to have more than one ID specified!");
            }
        }
        this.mediaId = Optional.fromNullable(n);
        this.collectionId = Optional.fromNullable(n2);
        this.seriesId = Optional.fromNullable(n3);
    }
    
    @Override
    public String getApiMethod() {
        return "info";
    }
    
    @Override
    public RequestMethod getMethod() {
        return RequestMethod.GET;
    }
    
    @Override
    public ImmutableMap<String, String> getParams() {
        final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        if (this.mediaId.isPresent()) {
            builder.put("media_id", this.mediaId.get().toString());
        }
        else if (this.collectionId.isPresent()) {
            builder.put("collection_id", this.collectionId.get().toString());
        }
        else if (this.seriesId.isPresent()) {
            builder.put("series_id", this.seriesId.get().toString());
        }
        return builder.build();
    }
    
    @Override
    public String toString() {
        return "InfoRequest [getParams()=" + this.getParams() + "]";
    }
}
