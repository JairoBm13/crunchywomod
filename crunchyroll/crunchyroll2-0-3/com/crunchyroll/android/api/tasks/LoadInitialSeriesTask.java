// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.tasks;

import com.crunchyroll.android.api.ApiResponse;
import java.util.ArrayList;
import com.fasterxml.jackson.core.type.TypeReference;
import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.requests.ListSeriesRequest;
import com.google.common.collect.ImmutableSet;
import android.content.Context;
import java.util.Set;
import com.crunchyroll.android.api.models.Series;
import java.util.List;

public class LoadInitialSeriesTask extends BaseTask<List<Series>>
{
    final Set<String> fields;
    private final String mFilter;
    private final String mMediaType;
    
    public LoadInitialSeriesTask(final Context context, final String mMediaType, final String mFilter) {
        super(context);
        this.fields = ImmutableSet.of("series.series_id", "series.name", "series.in_queue", "series.portrait_image", "series.url", "series.media_count", "media.media_id", "media.name", "media.episode_number", "image.medium_url", "image.large_url", "image.full_url");
        this.mMediaType = mMediaType;
        this.mFilter = mFilter;
    }
    
    @Override
    public List<Series> call() throws Exception {
        final ApiResponse run = this.getApiService().run(new ListSeriesRequest(this.mMediaType, this.mFilter), this.fields);
        run.cache();
        return this.parseResponse(run, (TypeReference<List<Series>>)new TypeReference<ArrayList<Series>>() {});
    }
}
