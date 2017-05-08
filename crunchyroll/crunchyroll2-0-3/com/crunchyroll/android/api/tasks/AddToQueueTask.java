// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.tasks;

import com.crunchyroll.crunchyroid.tracking.Tracker;
import com.crunchyroll.android.api.ApiResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.crunchyroll.android.api.requests.AddToQueueRequest;
import com.crunchyroll.android.api.ApiRequest;
import android.content.Context;
import com.crunchyroll.android.api.models.QueueEntry;

public class AddToQueueTask extends BaseTask<QueueEntry>
{
    private Context mContext;
    private ApiRequest mRequest;
    protected final Long mSeriesId;
    
    public AddToQueueTask(final Context mContext, final Long mSeriesId) {
        super(mContext);
        this.mContext = mContext;
        this.mSeriesId = mSeriesId;
        this.mRequest = new AddToQueueRequest(this.mSeriesId);
    }
    
    @Override
    public QueueEntry call() throws Exception {
        final ApiResponse run = this.getApiService().run(this.mRequest);
        if (!this.isCancelled()) {
            return this.parseResponse(run, (TypeReference<QueueEntry>)new TypeReference<QueueEntry>() {});
        }
        return null;
    }
    
    @Override
    protected void onSuccess(final QueueEntry queueEntry) throws Exception {
        if (!this.isCancelled()) {
            this.broadcastIntent("QUEUE_UPDATED");
            this.broadcastIntent("QUEUE_ADD");
            Tracker.addToQueue(this.mContext, queueEntry.getSeries());
            super.onSuccess(queueEntry);
        }
    }
}
