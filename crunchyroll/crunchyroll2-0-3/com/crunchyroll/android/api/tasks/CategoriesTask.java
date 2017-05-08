// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.tasks;

import com.fasterxml.jackson.core.type.TypeReference;
import com.crunchyroll.android.api.AbstractApiRequest;
import com.crunchyroll.android.api.requests.CategoriesRequest;
import android.app.Application;
import android.content.Context;
import com.crunchyroll.android.api.ClientInformation;
import com.crunchyroll.android.api.models.Categories;

public class CategoriesTask extends BaseTask<Categories>
{
    private ClientInformation mClientInformation;
    private String mMediaType;
    
    public CategoriesTask(final Context context, final String mMediaType) {
        super(context);
        this.mMediaType = mMediaType;
        this.mClientInformation = new ClientInformation(this.getApplication());
    }
    
    @Override
    public Categories call() throws Exception {
        return this.parseResponse(this.getApiService().runSessionless(new CategoriesRequest(this.mMediaType), this.mClientInformation), (TypeReference<Categories>)new TypeReference<Categories>() {});
    }
}
