// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.answers;

import java.util.Random;
import io.fabric.sdk.android.services.concurrency.internal.Backoff;

class RandomBackoff implements Backoff
{
    final Backoff backoff;
    final double jitterPercent;
    final Random random;
    
    public RandomBackoff(final Backoff backoff, final double n) {
        this(backoff, n, new Random());
    }
    
    public RandomBackoff(final Backoff backoff, final double jitterPercent, final Random random) {
        if (jitterPercent < 0.0 || jitterPercent > 1.0) {
            throw new IllegalArgumentException("jitterPercent must be between 0.0 and 1.0");
        }
        if (backoff == null) {
            throw new NullPointerException("backoff must not be null");
        }
        if (random == null) {
            throw new NullPointerException("random must not be null");
        }
        this.backoff = backoff;
        this.jitterPercent = jitterPercent;
        this.random = random;
    }
    
    @Override
    public long getDelayMillis(final int n) {
        return (long)(this.randomJitter() * this.backoff.getDelayMillis(n));
    }
    
    double randomJitter() {
        final double n = 1.0 - this.jitterPercent;
        return (1.0 + this.jitterPercent - n) * this.random.nextDouble() + n;
    }
}
