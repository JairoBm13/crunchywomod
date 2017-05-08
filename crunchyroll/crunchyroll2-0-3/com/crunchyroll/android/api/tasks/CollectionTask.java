// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.tasks;

import com.crunchyroll.android.api.ApiResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.crunchyroll.android.api.requests.ListCollectionsRequest;
import android.content.Context;
import com.google.common.collect.ImmutableSet;
import com.crunchyroll.android.api.ApiRequest;
import java.util.Set;
import com.crunchyroll.android.api.models.Collection;
import java.util.List;

public class CollectionTask extends BaseTask<List<Collection>>
{
    public static final Set<String> COLLECTION_FIELDS;
    private ApiRequest mListCollectionsRequest;
    private Long mSeriesId;
    
    static {
        COLLECTION_FIELDS = ImmutableSet.of("collections.collection_id", "collections.series_id", "collections.name", "collections.series_name", "collections.description", "collections.media_type", "collections.season", "collections.complete", "collections.landscape_image", "collections.portrait_image", "collections.availability_notes", "collections.media_count", "collections.premium_only", "collections.created", "collections.mature");
    }
    
    public CollectionTask(final Context context, final Long mSeriesId) {
        super(context);
        this.mSeriesId = mSeriesId;
        this.mListCollectionsRequest = new ListCollectionsRequest(this.mSeriesId, "desc", null, null);
    }
    
    @Override
    public List<Collection> call() throws Exception {
        final ApiResponse run = this.getApiService().run(this.mListCollectionsRequest, CollectionTask.COLLECTION_FIELDS);
        if (!this.isCancelled()) {
            final List<Collection> list = this.parseResponse(run, (TypeReference<List<Collection>>)new TypeReference<List<Collection>>() {});
            run.cache();
            return list;
        }
        return null;
    }
}
