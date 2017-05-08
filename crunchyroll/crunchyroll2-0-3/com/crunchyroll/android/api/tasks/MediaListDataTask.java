// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.tasks;

import com.crunchyroll.android.api.ApiResponse;
import com.crunchyroll.android.api.models.Media;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import com.crunchyroll.android.api.models.Series;
import com.crunchyroll.android.api.requests.ListMediaRequest;
import com.crunchyroll.android.api.requests.InfoRequest;
import android.content.Context;
import com.google.common.collect.ImmutableSet;
import com.crunchyroll.android.api.ApiRequest;
import java.util.Set;
import com.crunchyroll.android.api.models.SeriesInfoWithMedia;

public class MediaListDataTask extends BaseTask<SeriesInfoWithMedia>
{
    public static final Set<String> MEDIA_FIELDS;
    private final Set<String> SERIES_FIELDS;
    private final ApiRequest mInfoRequest;
    private final ApiRequest mListMediaRequest;
    private Long mSeriesId;
    
    static {
        MEDIA_FIELDS = ImmutableSet.of("media.screenshot_image", "media.free_available", "media.premium_available", "media.media_id", "media.episode_number", "media.name", "media.collection_id", "media.playhead", "media.available_time", "media.duration", "media.url", "media.series_name", "media.series_id", "image.wide_url", "image.fwide_url", "image.widestar_url", "image.fwidestar_url", "image.full_url");
    }
    
    public MediaListDataTask(final Context context, final Long mSeriesId, final Long n) {
        super(context);
        this.SERIES_FIELDS = ImmutableSet.of("series.series_id", "series.name", "series.in_queue", "series.description", "series.portrait_image", "series.landscape_image", "series.media_count", "series.publisher_name", "series.year", "series.rating", "series.url", "series.media_type", "series.genres", "image.wide_url", "image.fwide_url", "image.widestar_url", "image.fwidestar_url", "image.full_url");
        this.mSeriesId = mSeriesId;
        this.mInfoRequest = new InfoRequest(null, null, this.mSeriesId);
        Long mSeriesId2;
        if (n == null) {
            mSeriesId2 = this.mSeriesId;
        }
        else {
            mSeriesId2 = null;
        }
        this.mListMediaRequest = new ListMediaRequest(n, mSeriesId2, "desc", 0, 5000);
    }
    
    @Override
    public SeriesInfoWithMedia call() throws Exception {
        final ApiResponse run = this.getApiService().run(this.mInfoRequest, this.SERIES_FIELDS);
        final ApiResponse run2 = this.getApiService().run(this.mListMediaRequest, MediaListDataTask.MEDIA_FIELDS);
        if (!this.isCancelled()) {
            final SeriesInfoWithMedia seriesInfoWithMedia = new SeriesInfoWithMedia();
            seriesInfoWithMedia.setSeries(this.parseResponse(run, (TypeReference<Series>)new TypeReference<Series>() {}));
            seriesInfoWithMedia.setMedias(this.parseResponse(run2, (TypeReference<List<Media>>)new TypeReference<List<Media>>() {}));
            run2.cache();
            return seriesInfoWithMedia;
        }
        return null;
    }
}
