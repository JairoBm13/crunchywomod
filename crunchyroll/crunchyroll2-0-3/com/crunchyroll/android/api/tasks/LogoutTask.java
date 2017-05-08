// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.tasks;

import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.requests.LogoutRequest;
import android.content.Context;
import com.google.common.base.Optional;

public final class LogoutTask extends BaseTask<Void>
{
    private final Optional<String> authOpt;
    
    public LogoutTask(final Context context, final Optional<String> authOpt) {
        super(context);
        this.authOpt = authOpt;
    }
    
    @Override
    public Void call() throws Exception {
        if (this.authOpt.isPresent()) {
            this.getApiService().run(new LogoutRequest(this.authOpt.get()));
        }
        return null;
    }
}
