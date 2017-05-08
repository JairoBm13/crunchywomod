// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.tasks;

import com.crunchyroll.android.api.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.requests.InfoRequest;
import android.content.Context;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import com.crunchyroll.android.api.models.Media;

public class LoadMediaInfoTask extends BaseTask<Media>
{
    private static final Set<String> fields;
    protected final Long mMediaId;
    
    static {
        fields = ImmutableSet.of("media.media_id", "media.media_type", "media.episode_number", "media.name", "media.description", "media.series_id", "media.screenshot_image", "media.premium_available", "media.free_available", "media.series_name", "media.url", "media.collection_name", "media.availability_notes", "media.playhead");
    }
    
    public LoadMediaInfoTask(final Context context, final Long mMediaId) {
        super(context);
        if (mMediaId == 0L) {
            throw new IllegalArgumentException("mediaId cannot be 0");
        }
        this.mMediaId = mMediaId;
    }
    
    @Override
    public Media call() throws Exception {
        final CrunchyrollApplication application = this.getApplication();
        final ObjectMapper objectMapper = application.getObjectMapper();
        final ApiResponse run = application.getApiService().run(new InfoRequest(this.mMediaId, null, null), LoadMediaInfoTask.fields);
        final Media media = objectMapper.readValue(run.body.asParser(objectMapper).readValueAsTree().path("data").traverse(), Media.class);
        run.cache();
        return media;
    }
}
