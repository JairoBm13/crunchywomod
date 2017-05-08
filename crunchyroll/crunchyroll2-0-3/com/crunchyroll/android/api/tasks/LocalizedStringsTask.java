// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.tasks;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.crunchyroll.android.api.AbstractApiRequest;
import com.crunchyroll.android.api.requests.LocalizedStringsRequest;
import android.app.Application;
import android.content.Context;
import com.crunchyroll.android.api.ClientInformation;
import java.util.Map;

public final class LocalizedStringsTask extends BaseTask<Map<String, String>>
{
    private ClientInformation mClientInformation;
    
    public LocalizedStringsTask(final Context context) {
        super(context);
        this.mClientInformation = null;
        this.mClientInformation = new ClientInformation(this.getApplication());
    }
    
    @Override
    public Map<String, String> call() throws Exception {
        final JsonParser parser = this.getApplication().getApiService().runSessionless(new LocalizedStringsRequest(), this.mClientInformation).body.asParser();
        final JsonNode jsonNode = (JsonNode)parser.readValueAsTree().get("data");
        final ObjectMapper objectMapper = new ObjectMapper();
        parser.nextToken();
        return (Map<String, String>)objectMapper.readValue(jsonNode.toString(), new TypeReference<Map<String, String>>() {});
    }
}
