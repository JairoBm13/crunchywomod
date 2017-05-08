// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.concurrency.internal;

public class ExponentialBackoff implements Backoff
{
    private final long baseTimeMillis;
    private final int power;
    
    public ExponentialBackoff(final long baseTimeMillis, final int power) {
        this.baseTimeMillis = baseTimeMillis;
        this.power = power;
    }
    
    @Override
    public long getDelayMillis(final int n) {
        return (long)(this.baseTimeMillis * Math.pow(this.power, n));
    }
}
