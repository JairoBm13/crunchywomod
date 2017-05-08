// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.tasks;

import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.requests.ForgotPasswordRequest;
import com.fasterxml.jackson.databind.node.BooleanNode;
import android.app.Application;
import android.content.Context;
import com.crunchyroll.android.api.ClientInformation;

public final class ForgotPasswordTask extends BaseTask<Boolean>
{
    private ClientInformation mClientInformation;
    private String mEmail;
    
    public ForgotPasswordTask(final Context context, final String mEmail) {
        super(context);
        this.mEmail = mEmail;
        this.mClientInformation = new ClientInformation(this.getApplication());
    }
    
    @Override
    public Boolean call() throws Exception {
        return ((BooleanNode)this.getApiService().run(new ForgotPasswordRequest(this.mEmail)).body.asParser().readValueAsTree().get("data")).asBoolean();
    }
}
