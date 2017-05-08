// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.tasks;

import com.fasterxml.jackson.core.type.TypeReference;
import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.requests.RecentlyWatchedRequest;
import com.google.common.collect.ImmutableSet;
import android.content.Context;
import java.util.Set;
import com.crunchyroll.android.api.models.RecentlyWatchedItem;
import java.util.List;

public class LoadHistoryTask extends BaseTask<List<RecentlyWatchedItem>>
{
    private final Set<String> fields;
    protected final Integer mLimit;
    protected String mMediaTypes;
    protected final Integer mOffset;
    
    public LoadHistoryTask(final Context context, final String mMediaTypes, final Integer mOffset, final Integer mLimit) {
        super(context);
        this.fields = ImmutableSet.of("series.series_id", "series.name", "media.url", "media.media_id", "media.name", "media.episode_number", "media.duration", "media.playhead", "media.screenshot_image", "media.premium_available", "media.free_available", "image.wide_url", "image.fwide_url", "image.widestar_url", "image.fwidestar_url", "media.series_id");
        this.mMediaTypes = mMediaTypes;
        this.mOffset = mOffset;
        this.mLimit = mLimit;
    }
    
    @Override
    public List<RecentlyWatchedItem> call() throws Exception {
        return this.parseResponse(this.getApiService().run(new RecentlyWatchedRequest(this.mMediaTypes, this.mOffset, this.mLimit), this.fields), (TypeReference<List<RecentlyWatchedItem>>)new TypeReference<List<RecentlyWatchedItem>>() {});
    }
    
    public int getLimit() {
        return this.mLimit;
    }
    
    public int getOffset() {
        return this.mOffset;
    }
}
