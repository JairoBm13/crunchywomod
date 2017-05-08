// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.answers;

import java.io.File;
import java.util.List;
import io.fabric.sdk.android.services.concurrency.internal.RetryPolicy;
import io.fabric.sdk.android.services.concurrency.internal.RetryState;
import io.fabric.sdk.android.services.concurrency.internal.DefaultRetryPolicy;
import io.fabric.sdk.android.services.concurrency.internal.Backoff;
import io.fabric.sdk.android.services.concurrency.internal.ExponentialBackoff;
import io.fabric.sdk.android.services.events.FilesSender;

class AnswersRetryFilesSender implements FilesSender
{
    private final SessionAnalyticsFilesSender filesSender;
    private final RetryManager retryManager;
    
    AnswersRetryFilesSender(final SessionAnalyticsFilesSender filesSender, final RetryManager retryManager) {
        this.filesSender = filesSender;
        this.retryManager = retryManager;
    }
    
    public static AnswersRetryFilesSender build(final SessionAnalyticsFilesSender sessionAnalyticsFilesSender) {
        return new AnswersRetryFilesSender(sessionAnalyticsFilesSender, new RetryManager(new RetryState(new RandomBackoff(new ExponentialBackoff(1000L, 8), 0.1), new DefaultRetryPolicy(5))));
    }
    
    @Override
    public boolean send(final List<File> list) {
        boolean b = false;
        final long nanoTime = System.nanoTime();
        if (this.retryManager.canRetry(nanoTime)) {
            if (!this.filesSender.send(list)) {
                this.retryManager.recordRetry(nanoTime);
                return false;
            }
            this.retryManager.reset();
            b = true;
        }
        return b;
    }
}
