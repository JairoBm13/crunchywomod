// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.answers;

import io.fabric.sdk.android.services.concurrency.internal.RetryState;

class RetryManager
{
    long lastRetry;
    private RetryState retryState;
    
    public RetryManager(final RetryState retryState) {
        if (retryState == null) {
            throw new NullPointerException("retryState must not be null");
        }
        this.retryState = retryState;
    }
    
    public boolean canRetry(final long n) {
        return n - this.lastRetry >= 1000000L * this.retryState.getRetryDelay();
    }
    
    public void recordRetry(final long lastRetry) {
        this.lastRetry = lastRetry;
        this.retryState = this.retryState.nextRetryState();
    }
    
    public void reset() {
        this.lastRetry = 0L;
        this.retryState = this.retryState.initialRetryState();
    }
}
