// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.answers;

import java.io.IOException;
import io.fabric.sdk.android.services.events.EventsStorage;
import io.fabric.sdk.android.services.common.CurrentTimeProvider;
import io.fabric.sdk.android.services.events.GZIPQueueFileEventStorage;
import io.fabric.sdk.android.services.common.SystemCurrentTimeProvider;
import android.os.Looper;
import io.fabric.sdk.android.services.persistence.FileStore;
import android.content.Context;

class AnswersFilesManagerProvider
{
    final Context context;
    final FileStore fileStore;
    
    public AnswersFilesManagerProvider(final Context context, final FileStore fileStore) {
        this.context = context;
        this.fileStore = fileStore;
    }
    
    public SessionAnalyticsFilesManager getAnalyticsFilesManager() throws IOException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalStateException("AnswersFilesManagerProvider cannot be called on the main thread");
        }
        return new SessionAnalyticsFilesManager(this.context, new SessionEventTransform(), new SystemCurrentTimeProvider(), new GZIPQueueFileEventStorage(this.context, this.fileStore.getFilesDir(), "session_analytics.tap", "session_analytics_to_send"));
    }
}
