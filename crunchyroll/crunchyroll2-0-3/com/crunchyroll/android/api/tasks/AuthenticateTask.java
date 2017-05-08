// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.tasks;

import com.fasterxml.jackson.core.type.TypeReference;
import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.requests.AuthenticateRequest;
import android.content.Context;
import com.crunchyroll.android.api.models.AuthenticateItem;

public class AuthenticateTask extends BaseTask<AuthenticateItem>
{
    private String mAuth;
    private Integer mDuration;
    
    public AuthenticateTask(final Context context, final String mAuth, final Integer mDuration) {
        super(context);
        this.mAuth = mAuth;
        this.mDuration = mDuration;
    }
    
    @Override
    public AuthenticateItem call() throws Exception {
        return this.parseResponse(this.getApiService().run(new AuthenticateRequest(this.mAuth, this.mDuration)), (TypeReference<AuthenticateItem>)new TypeReference<AuthenticateItem>() {});
    }
}
