// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.tasks;

import com.crunchyroll.crunchyroid.tracking.Tracker;
import com.crunchyroll.android.api.requests.RemoveFromQueueRequest;
import com.crunchyroll.android.api.models.Series;
import com.crunchyroll.android.api.ApiRequest;
import android.content.Context;

public class RemoveFromQueueTask extends BaseTask<Void>
{
    protected final Context mContext;
    private ApiRequest mRequest;
    protected final Series mSeries;
    
    public RemoveFromQueueTask(final Context mContext, final Series mSeries) {
        super(mContext);
        this.mContext = mContext;
        this.mSeries = mSeries;
        this.mRequest = new RemoveFromQueueRequest(this.mSeries.getSeriesId());
    }
    
    @Override
    public Void call() throws Exception {
        this.getApiService().run(this.mRequest);
        return null;
    }
    
    @Override
    protected void onSuccess(final Void void1) throws Exception {
        if (!this.isCancelled()) {
            this.broadcastIntent("QUEUE_UPDATED");
            Tracker.removeFromQueue(this.mContext, this.mSeries);
            super.onSuccess(void1);
        }
    }
}
