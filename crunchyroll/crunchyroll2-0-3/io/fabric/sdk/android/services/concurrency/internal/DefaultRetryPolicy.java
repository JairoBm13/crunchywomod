// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.concurrency.internal;

public class DefaultRetryPolicy implements RetryPolicy
{
    private final int maxRetries;
    
    public DefaultRetryPolicy() {
        this(1);
    }
    
    public DefaultRetryPolicy(final int maxRetries) {
        this.maxRetries = maxRetries;
    }
}
