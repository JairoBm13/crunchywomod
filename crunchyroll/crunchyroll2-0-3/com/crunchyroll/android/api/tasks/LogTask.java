// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.tasks;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import com.crunchyroll.android.api.requests.LogPlaybackProgressRequest;
import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import com.crunchyroll.android.api.AbstractApiRequest;
import android.content.Context;
import android.os.AsyncTask;

public class LogTask extends AsyncTask<Void, Void, Void>
{
    private final Context context;
    private final AbstractApiRequest request;
    
    public LogTask(final Context context, final AbstractApiRequest request) {
        this.context = context;
        this.request = request;
    }
    
    protected Void doInBackground(final Void... array) {
        try {
            ((CrunchyrollApplication)this.context.getApplicationContext()).getApiService().run(this.request);
            return null;
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    protected void onPostExecute(final Void void1) {
        super.onPostExecute((Object)void1);
        if (this.request instanceof LogPlaybackProgressRequest) {
            LocalBroadcastManager.getInstance(this.context).sendBroadcast(new Intent("HISTORY_UPDATED"));
        }
    }
}
