// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.tasks;

import com.crunchyroll.android.api.ApiResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.crunchyroll.android.api.AbstractApiRequest;
import com.crunchyroll.android.api.requests.ListLocalesRequest;
import android.app.Application;
import android.content.Context;
import com.crunchyroll.android.api.ClientInformation;
import com.crunchyroll.android.api.models.LocaleData;
import java.util.List;

public class ListLocalesTask extends BaseTask<List<LocaleData>>
{
    private ClientInformation mClientInformation;
    
    public ListLocalesTask(final Context context) {
        super(context);
        this.mClientInformation = null;
        this.mClientInformation = new ClientInformation(this.getApplication());
    }
    
    @Override
    public List<LocaleData> call() throws Exception {
        final ApiResponse runSessionless = this.getApplication().getApiService().runSessionless(new ListLocalesRequest(), this.mClientInformation);
        final ObjectMapper objectMapper = new ObjectMapper();
        final List<LocaleData> list = objectMapper.readValue(runSessionless.body.asParser(objectMapper).readValueAsTree().path("data").path("locales").traverse(), new TypeReference<List<LocaleData>>() {});
        this.filterLocales(list);
        return list;
    }
    
    protected void filterLocales(final List<LocaleData> list) {
    }
}
