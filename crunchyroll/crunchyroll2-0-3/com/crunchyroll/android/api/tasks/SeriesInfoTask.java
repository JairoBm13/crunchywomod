// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.tasks;

import com.crunchyroll.android.api.ApiResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.requests.InfoRequest;
import com.google.common.collect.ImmutableSet;
import android.content.Context;
import java.util.Set;
import com.crunchyroll.android.api.models.Series;

public class SeriesInfoTask extends BaseTask<Series>
{
    private final Set<String> mFields;
    private final long mSeriesId;
    
    public SeriesInfoTask(final Context context, final long mSeriesId) {
        super(context);
        this.mFields = ImmutableSet.of("series.series_id", "series.name", "series.in_queue", "series.description", "series.portrait_image", "series.landscape_image", "series.media_count", "series.publisher_name", "series.year", "series.media_type", "series.genres");
        this.mSeriesId = mSeriesId;
    }
    
    @Override
    public Series call() throws Exception {
        final ApiResponse run = this.getApiService().run(new InfoRequest(null, null, this.mSeriesId), this.mFields);
        run.cache();
        return this.parseResponse(run, (TypeReference<Series>)new TypeReference<Series>() {});
    }
}
