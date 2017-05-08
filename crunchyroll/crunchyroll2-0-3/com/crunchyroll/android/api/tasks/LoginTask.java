// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.tasks;

import com.fasterxml.jackson.core.type.TypeReference;
import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.requests.LoginRequest;
import android.content.Context;
import com.crunchyroll.android.api.models.Login;

public final class LoginTask extends BaseTask<Login>
{
    private final String mPassword;
    private final String mUsername;
    
    public LoginTask(final Context context, final String mUsername, final String mPassword) {
        super(context);
        this.mUsername = mUsername;
        this.mPassword = mPassword;
    }
    
    @Override
    public Login call() throws Exception {
        return this.parseResponse(this.getApiService().run(new LoginRequest(this.mUsername, this.mPassword, null)), (TypeReference<Login>)new TypeReference<Login>() {});
    }
}
