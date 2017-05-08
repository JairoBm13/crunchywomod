// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.tasks;

import com.crunchyroll.android.api.ApiResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.requests.ListSeriesRequest;
import com.google.common.collect.ImmutableSet;
import android.content.Context;
import java.util.Set;
import com.crunchyroll.android.api.models.UpdatedEpisode;
import java.util.List;

public class UpdatedEpisodesTask extends BaseTask<List<UpdatedEpisode>>
{
    final Set<String> fields;
    private final String mFilter;
    private final int mLimit;
    private final String mMediaType;
    private final int mOffset;
    
    public UpdatedEpisodesTask(final Context context, final String mMediaType, final String mFilter, final Integer n, final Integer n2) {
        super(context);
        this.fields = ImmutableSet.of("series.series_id", "series.name", "series.in_queue", "series.url", "series.most_recent_media", "media.media_id", "media.collection_id", "media.series_id", "media.episode_number", "media.name", "media.screenshot_image", "media.url", "media.available_time", "media.duration", "media.series_name", "media.playhead", "media.premium_available", "media.free_available");
        this.mMediaType = mMediaType;
        this.mFilter = mFilter;
        this.mOffset = n;
        this.mLimit = n2;
    }
    
    @Override
    public List<UpdatedEpisode> call() throws Exception {
        final ApiResponse run = this.getApiService().run(new ListSeriesRequest(this.mMediaType, this.mFilter, this.mOffset, this.mLimit), this.fields);
        run.cache();
        return this.parseResponse(run, (TypeReference<List<UpdatedEpisode>>)new TypeReference<List<UpdatedEpisode>>() {});
    }
}
