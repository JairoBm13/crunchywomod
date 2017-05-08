// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.tasks;

import com.crunchyroll.android.api.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import java.util.ArrayList;
import com.crunchyroll.android.api.requests.QueueRequest;
import com.crunchyroll.android.api.models.QueueEntry;
import com.crunchyroll.android.api.models.Categories;
import com.crunchyroll.android.api.models.Series;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.crunchyroll.android.api.ApiRequest;
import java.util.List;
import com.crunchyroll.android.api.requests.CategoriesRequest;
import com.crunchyroll.android.api.requests.ListSeriesRequest;
import com.google.common.collect.Lists;
import android.content.Context;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import com.crunchyroll.android.api.models.InitialBrowseData;

public class InitialBrowseDataTask extends BaseTask<InitialBrowseData>
{
    private static final Set<String> fields;
    private boolean mIsUserLoggedIn;
    private String mMediaType;
    
    static {
        fields = ImmutableSet.of("series.series_id", "series.name", "series.portrait_image", "series.media_count", "series.media_type", "image.medium_url", "image.large_url", "series.url", "image.full_url");
    }
    
    public InitialBrowseDataTask(final Context context, final String mMediaType, final boolean mIsUserLoggedIn) {
        super(context);
        this.mMediaType = mMediaType;
        this.mIsUserLoggedIn = mIsUserLoggedIn;
    }
    
    @Override
    public InitialBrowseData call() throws Exception {
        final ArrayList<Object> arrayList = Lists.newArrayList();
        arrayList.add(new ListSeriesRequest(this.mMediaType, "popular"));
        arrayList.add(new ListSeriesRequest(this.mMediaType, "simulcast"));
        arrayList.add(new ListSeriesRequest(this.mMediaType, "updated"));
        arrayList.add(new ListSeriesRequest(this.mMediaType, "alpha"));
        arrayList.add(new CategoriesRequest(this.mMediaType));
        final CrunchyrollApplication application = this.getApplication();
        final ObjectMapper objectMapper = application.getObjectMapper();
        final ApiResponse postBatch = application.getApiService().postBatch((List<ApiRequest>)arrayList, InitialBrowseDataTask.fields);
        if (this.isCancelled()) {
            return null;
        }
        final JsonNode path = postBatch.body.asParser().readValueAsTree().path("data");
        final JsonNode path2 = path.path(0).path("body").path("data");
        final JsonNode path3 = path.path(1).path("body").path("data");
        final JsonNode path4 = path.path(2).path("body").path("data");
        final JsonNode path5 = path.path(3).path("body").path("data");
        final JsonNode path6 = path.path(4).path("body").path("data");
        final TypeReference<List<Series>> typeReference = new TypeReference<List<Series>>() {};
        final InitialBrowseData initialBrowseData = new InitialBrowseData();
        initialBrowseData.setPopularSeries(objectMapper.readValue(path2.traverse(), typeReference));
        initialBrowseData.setSimulcastSeries(objectMapper.readValue(path3.traverse(), typeReference));
        initialBrowseData.setUpdatedSeries(objectMapper.readValue(path4.traverse(), typeReference));
        initialBrowseData.setAlphaSeries(objectMapper.readValue(path5.traverse(), typeReference));
        initialBrowseData.setCategories(objectMapper.readValue(path6.traverse(), Categories.class));
        if (this.mIsUserLoggedIn) {
            initialBrowseData.setQueueList(this.parseResponse(application.getApiService().run(new QueueRequest("anime|drama"), LoadQueueTask.fields), (TypeReference<List<QueueEntry>>)new TypeReference<List<QueueEntry>>() {}));
        }
        postBatch.cache();
        return initialBrowseData;
    }
}
