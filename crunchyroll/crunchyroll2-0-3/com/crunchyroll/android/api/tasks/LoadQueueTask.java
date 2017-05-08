// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.tasks;

import com.fasterxml.jackson.core.type.TypeReference;
import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.requests.QueueRequest;
import android.content.Context;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import com.crunchyroll.android.api.models.QueueEntry;
import java.util.List;

public final class LoadQueueTask extends BaseTask<List<QueueEntry>>
{
    public static final Set<String> fields;
    private final String mMediaTypes;
    
    static {
        fields = ImmutableSet.of("most_likely_media", "series", "series.media_type", "series.series_id", "series.name", "media.media_id", "media.name", "media.episode_number", "series.url", "media.screenshot_image", "media.free_available", "media.duration", "media.playhead", "media.series_id", "media.premium_available", "media.free_available", "image.wide_url", "image.fwide_url", "image.fwidestar_url", "image.widestar_url", "ordering");
    }
    
    public LoadQueueTask(final Context context, final String mMediaTypes) {
        super(context);
        this.mMediaTypes = mMediaTypes;
    }
    
    @Override
    public List<QueueEntry> call() throws Exception {
        return this.parseResponse(this.getApiService().run(new QueueRequest(this.mMediaTypes), LoadQueueTask.fields), (TypeReference<List<QueueEntry>>)new TypeReference<List<QueueEntry>>() {});
    }
}
