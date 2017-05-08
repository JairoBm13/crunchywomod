// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.concurrency.internal;

public class RetryState
{
    private final Backoff backoff;
    private final int retryCount;
    private final RetryPolicy retryPolicy;
    
    public RetryState(final int retryCount, final Backoff backoff, final RetryPolicy retryPolicy) {
        this.retryCount = retryCount;
        this.backoff = backoff;
        this.retryPolicy = retryPolicy;
    }
    
    public RetryState(final Backoff backoff, final RetryPolicy retryPolicy) {
        this(0, backoff, retryPolicy);
    }
    
    public long getRetryDelay() {
        return this.backoff.getDelayMillis(this.retryCount);
    }
    
    public RetryState initialRetryState() {
        return new RetryState(this.backoff, this.retryPolicy);
    }
    
    public RetryState nextRetryState() {
        return new RetryState(this.retryCount + 1, this.backoff, this.retryPolicy);
    }
}
