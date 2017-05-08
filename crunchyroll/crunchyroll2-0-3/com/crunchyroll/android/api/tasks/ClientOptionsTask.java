// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.tasks;

import com.crunchyroll.android.api.ApiResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import com.crunchyroll.android.api.models.NameValuePair;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.crunchyroll.android.api.AbstractApiRequest;
import com.crunchyroll.android.api.requests.ClientOptionsRequest;
import android.app.Application;
import android.content.Context;
import com.crunchyroll.android.api.ClientInformation;
import com.crunchyroll.android.api.models.ClientOptions;

public class ClientOptionsTask extends BaseTask<ClientOptions>
{
    private ClientInformation mClientInformation;
    
    public ClientOptionsTask(final Context context) {
        super(context);
        this.mClientInformation = new ClientInformation(this.getApplication());
    }
    
    @Override
    public ClientOptions call() throws Exception {
        final ApiResponse runSessionless = this.getApiService().runSessionless(new ClientOptionsRequest(), this.mClientInformation);
        final ObjectMapper objectMapper = new ObjectMapper();
        final List<NameValuePair> nameValuePairList = objectMapper.readValue(runSessionless.body.asParser(objectMapper).readValueAsTree().path("data").traverse(), new TypeReference<List<NameValuePair>>() {});
        final ClientOptions clientOptions = new ClientOptions();
        clientOptions.setNameValuePairList(nameValuePairList);
        return clientOptions;
    }
}
