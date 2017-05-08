// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.cache;

public abstract class AbstractCache<K, V> implements Cache<K, V>
{
    public static final class SimpleStatsCounter implements StatsCounter
    {
        private final LongAdder evictionCount;
        private final LongAdder hitCount;
        private final LongAdder loadExceptionCount;
        private final LongAdder loadSuccessCount;
        private final LongAdder missCount;
        private final LongAdder totalLoadTime;
        
        public SimpleStatsCounter() {
            this.hitCount = new LongAdder();
            this.missCount = new LongAdder();
            this.loadSuccessCount = new LongAdder();
            this.loadExceptionCount = new LongAdder();
            this.totalLoadTime = new LongAdder();
            this.evictionCount = new LongAdder();
        }
        
        @Override
        public void recordEviction() {
            this.evictionCount.increment();
        }
        
        @Override
        public void recordHits(final int n) {
            this.hitCount.add(n);
        }
        
        @Override
        public void recordLoadException(final long n) {
            this.loadExceptionCount.increment();
            this.totalLoadTime.add(n);
        }
        
        @Override
        public void recordLoadSuccess(final long n) {
            this.loadSuccessCount.increment();
            this.totalLoadTime.add(n);
        }
        
        @Override
        public void recordMisses(final int n) {
            this.missCount.add(n);
        }
    }
    
    public interface StatsCounter
    {
        void recordEviction();
        
        void recordHits(final int p0);
        
        void recordLoadException(final long p0);
        
        void recordLoadSuccess(final long p0);
        
        void recordMisses(final int p0);
    }
}
