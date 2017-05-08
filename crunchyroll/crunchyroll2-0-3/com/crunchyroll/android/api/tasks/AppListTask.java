// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.tasks;

import com.crunchyroll.android.api.ApiResponse;
import android.util.Log;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.requests.AppListRequest;
import android.content.Context;
import com.crunchyroll.android.api.models.RelatedApp;
import java.util.List;

public class AppListTask extends BaseTask<List<RelatedApp>>
{
    public AppListTask(final Context context) {
        super(context);
    }
    
    @Override
    public List<RelatedApp> call() throws Exception {
        final ApiResponse run = this.getApiService().run(new AppListRequest());
        run.cache();
        final ObjectMapper objectMapper = new ObjectMapper();
        final JsonNode path = run.body.asParser(objectMapper).readValueAsTree().path("items");
        Log.i("RelatedApps", objectMapper.readValue(path.traverse(), new TypeReference<List<RelatedApp>>() {}).toString());
        return (List<RelatedApp>)objectMapper.readValue(path.traverse(), new TypeReference<List<RelatedApp>>() {});
    }
}
