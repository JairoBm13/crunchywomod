// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.cache;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public final class CacheStats
{
    private final long evictionCount;
    private final long hitCount;
    private final long loadExceptionCount;
    private final long loadSuccessCount;
    private final long missCount;
    private final long totalLoadTime;
    
    public CacheStats(final long hitCount, final long missCount, final long loadSuccessCount, final long loadExceptionCount, final long totalLoadTime, final long evictionCount) {
        Preconditions.checkArgument(hitCount >= 0L);
        Preconditions.checkArgument(missCount >= 0L);
        Preconditions.checkArgument(loadSuccessCount >= 0L);
        Preconditions.checkArgument(loadExceptionCount >= 0L);
        Preconditions.checkArgument(totalLoadTime >= 0L);
        Preconditions.checkArgument(evictionCount >= 0L);
        this.hitCount = hitCount;
        this.missCount = missCount;
        this.loadSuccessCount = loadSuccessCount;
        this.loadExceptionCount = loadExceptionCount;
        this.totalLoadTime = totalLoadTime;
        this.evictionCount = evictionCount;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b2;
        final boolean b = b2 = false;
        if (o instanceof CacheStats) {
            final CacheStats cacheStats = (CacheStats)o;
            b2 = b;
            if (this.hitCount == cacheStats.hitCount) {
                b2 = b;
                if (this.missCount == cacheStats.missCount) {
                    b2 = b;
                    if (this.loadSuccessCount == cacheStats.loadSuccessCount) {
                        b2 = b;
                        if (this.loadExceptionCount == cacheStats.loadExceptionCount) {
                            b2 = b;
                            if (this.totalLoadTime == cacheStats.totalLoadTime) {
                                b2 = b;
                                if (this.evictionCount == cacheStats.evictionCount) {
                                    b2 = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return b2;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.hitCount, this.missCount, this.loadSuccessCount, this.loadExceptionCount, this.totalLoadTime, this.evictionCount);
    }
    
    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("hitCount", this.hitCount).add("missCount", this.missCount).add("loadSuccessCount", this.loadSuccessCount).add("loadExceptionCount", this.loadExceptionCount).add("totalLoadTime", this.totalLoadTime).add("evictionCount", this.evictionCount).toString();
    }
}
