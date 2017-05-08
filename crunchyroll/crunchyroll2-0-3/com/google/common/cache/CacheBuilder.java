// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.cache;

import com.google.common.base.Ascii;
import com.google.common.base.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import com.google.common.base.Preconditions;
import com.google.common.base.Suppliers;
import com.google.common.base.Equivalence;
import java.util.logging.Logger;
import com.google.common.base.Ticker;
import com.google.common.base.Supplier;

public final class CacheBuilder<K, V>
{
    static final Supplier<AbstractCache.StatsCounter> CACHE_STATS_COUNTER;
    static final CacheStats EMPTY_STATS;
    static final Supplier<? extends AbstractCache.StatsCounter> NULL_STATS_COUNTER;
    static final Ticker NULL_TICKER;
    private static final Logger logger;
    int concurrencyLevel;
    long expireAfterAccessNanos;
    long expireAfterWriteNanos;
    int initialCapacity;
    Equivalence<Object> keyEquivalence;
    LocalCache.Strength keyStrength;
    long maximumSize;
    long maximumWeight;
    long refreshNanos;
    RemovalListener<? super K, ? super V> removalListener;
    Supplier<? extends AbstractCache.StatsCounter> statsCounterSupplier;
    boolean strictParsing;
    Ticker ticker;
    Equivalence<Object> valueEquivalence;
    LocalCache.Strength valueStrength;
    Weigher<? super K, ? super V> weigher;
    
    static {
        NULL_STATS_COUNTER = Suppliers.ofInstance((AbstractCache.StatsCounter)new AbstractCache.StatsCounter() {
            @Override
            public void recordEviction() {
            }
            
            @Override
            public void recordHits(final int n) {
            }
            
            @Override
            public void recordLoadException(final long n) {
            }
            
            @Override
            public void recordLoadSuccess(final long n) {
            }
            
            @Override
            public void recordMisses(final int n) {
            }
        });
        EMPTY_STATS = new CacheStats(0L, 0L, 0L, 0L, 0L, 0L);
        CACHE_STATS_COUNTER = new Supplier<AbstractCache.StatsCounter>() {
            @Override
            public AbstractCache.StatsCounter get() {
                return new AbstractCache.SimpleStatsCounter();
            }
        };
        NULL_TICKER = new Ticker() {
            @Override
            public long read() {
                return 0L;
            }
        };
        logger = Logger.getLogger(CacheBuilder.class.getName());
    }
    
    CacheBuilder() {
        this.strictParsing = true;
        this.initialCapacity = -1;
        this.concurrencyLevel = -1;
        this.maximumSize = -1L;
        this.maximumWeight = -1L;
        this.expireAfterWriteNanos = -1L;
        this.expireAfterAccessNanos = -1L;
        this.refreshNanos = -1L;
        this.statsCounterSupplier = CacheBuilder.NULL_STATS_COUNTER;
    }
    
    private void checkNonLoadingCache() {
        Preconditions.checkState(this.refreshNanos == -1L, (Object)"refreshAfterWrite requires a LoadingCache");
    }
    
    private void checkWeightWithWeigher() {
        final boolean b = true;
        boolean b2 = true;
        if (this.weigher == null) {
            if (this.maximumWeight != -1L) {
                b2 = false;
            }
            Preconditions.checkState(b2, (Object)"maximumWeight requires weigher");
        }
        else {
            if (this.strictParsing) {
                Preconditions.checkState(this.maximumWeight != -1L && b, (Object)"weigher requires maximumWeight");
                return;
            }
            if (this.maximumWeight == -1L) {
                CacheBuilder.logger.log(Level.WARNING, "ignoring weigher specified without maximumWeight");
            }
        }
    }
    
    public static CacheBuilder<Object, Object> newBuilder() {
        return new CacheBuilder<Object, Object>();
    }
    
    public <K1 extends K, V1 extends V> Cache<K1, V1> build() {
        this.checkWeightWithWeigher();
        this.checkNonLoadingCache();
        return new LocalCache.LocalManualCache<K1, V1>(this);
    }
    
    public CacheBuilder<K, V> concurrencyLevel(final int concurrencyLevel) {
        final boolean b = true;
        Preconditions.checkState(this.concurrencyLevel == -1, "concurrency level was already set to %s", this.concurrencyLevel);
        Preconditions.checkArgument(concurrencyLevel > 0 && b);
        this.concurrencyLevel = concurrencyLevel;
        return this;
    }
    
    public CacheBuilder<K, V> expireAfterWrite(final long n, final TimeUnit timeUnit) {
        Preconditions.checkState(this.expireAfterWriteNanos == -1L, "expireAfterWrite was already set to %s ns", this.expireAfterWriteNanos);
        Preconditions.checkArgument(n >= 0L, "duration cannot be negative: %s %s", n, timeUnit);
        this.expireAfterWriteNanos = timeUnit.toNanos(n);
        return this;
    }
    
    int getConcurrencyLevel() {
        if (this.concurrencyLevel == -1) {
            return 4;
        }
        return this.concurrencyLevel;
    }
    
    long getExpireAfterAccessNanos() {
        if (this.expireAfterAccessNanos == -1L) {
            return 0L;
        }
        return this.expireAfterAccessNanos;
    }
    
    long getExpireAfterWriteNanos() {
        if (this.expireAfterWriteNanos == -1L) {
            return 0L;
        }
        return this.expireAfterWriteNanos;
    }
    
    int getInitialCapacity() {
        if (this.initialCapacity == -1) {
            return 16;
        }
        return this.initialCapacity;
    }
    
    Equivalence<Object> getKeyEquivalence() {
        return Objects.firstNonNull(this.keyEquivalence, this.getKeyStrength().defaultEquivalence());
    }
    
    LocalCache.Strength getKeyStrength() {
        return Objects.firstNonNull(this.keyStrength, LocalCache.Strength.STRONG);
    }
    
    long getMaximumWeight() {
        if (this.expireAfterWriteNanos == 0L || this.expireAfterAccessNanos == 0L) {
            return 0L;
        }
        if (this.weigher == null) {
            return this.maximumSize;
        }
        return this.maximumWeight;
    }
    
    long getRefreshNanos() {
        if (this.refreshNanos == -1L) {
            return 0L;
        }
        return this.refreshNanos;
    }
    
     <K1 extends K, V1 extends V> RemovalListener<K1, V1> getRemovalListener() {
        return Objects.firstNonNull((RemovalListener<K1, V1>)this.removalListener, (RemovalListener<K1, V1>)NullListener.INSTANCE);
    }
    
    Supplier<? extends AbstractCache.StatsCounter> getStatsCounterSupplier() {
        return this.statsCounterSupplier;
    }
    
    Ticker getTicker(final boolean b) {
        if (this.ticker != null) {
            return this.ticker;
        }
        if (b) {
            return Ticker.systemTicker();
        }
        return CacheBuilder.NULL_TICKER;
    }
    
    Equivalence<Object> getValueEquivalence() {
        return Objects.firstNonNull(this.valueEquivalence, this.getValueStrength().defaultEquivalence());
    }
    
    LocalCache.Strength getValueStrength() {
        return Objects.firstNonNull(this.valueStrength, LocalCache.Strength.STRONG);
    }
    
     <K1 extends K, V1 extends V> Weigher<K1, V1> getWeigher() {
        return Objects.firstNonNull((Weigher<K1, V1>)this.weigher, (Weigher<K1, V1>)OneWeigher.INSTANCE);
    }
    
    public CacheBuilder<K, V> maximumSize(final long maximumSize) {
        final boolean b = true;
        Preconditions.checkState(this.maximumSize == -1L, "maximum size was already set to %s", this.maximumSize);
        Preconditions.checkState(this.maximumWeight == -1L, "maximum weight was already set to %s", this.maximumWeight);
        Preconditions.checkState(this.weigher == null, (Object)"maximum size can not be combined with weigher");
        Preconditions.checkArgument(maximumSize >= 0L && b, (Object)"maximum size must not be negative");
        this.maximumSize = maximumSize;
        return this;
    }
    
    @Override
    public String toString() {
        final Objects.ToStringHelper stringHelper = Objects.toStringHelper(this);
        if (this.initialCapacity != -1) {
            stringHelper.add("initialCapacity", this.initialCapacity);
        }
        if (this.concurrencyLevel != -1) {
            stringHelper.add("concurrencyLevel", this.concurrencyLevel);
        }
        if (this.maximumWeight != -1L) {
            if (this.weigher == null) {
                stringHelper.add("maximumSize", this.maximumWeight);
            }
            else {
                stringHelper.add("maximumWeight", this.maximumWeight);
            }
        }
        if (this.expireAfterWriteNanos != -1L) {
            stringHelper.add("expireAfterWrite", this.expireAfterWriteNanos + "ns");
        }
        if (this.expireAfterAccessNanos != -1L) {
            stringHelper.add("expireAfterAccess", this.expireAfterAccessNanos + "ns");
        }
        if (this.keyStrength != null) {
            stringHelper.add("keyStrength", Ascii.toLowerCase(this.keyStrength.toString()));
        }
        if (this.valueStrength != null) {
            stringHelper.add("valueStrength", Ascii.toLowerCase(this.valueStrength.toString()));
        }
        if (this.keyEquivalence != null) {
            stringHelper.addValue("keyEquivalence");
        }
        if (this.valueEquivalence != null) {
            stringHelper.addValue("valueEquivalence");
        }
        if (this.removalListener != null) {
            stringHelper.addValue("removalListener");
        }
        return stringHelper.toString();
    }
    
    enum NullListener implements RemovalListener<Object, Object>
    {
        INSTANCE;
        
        @Override
        public void onRemoval(final RemovalNotification<Object, Object> removalNotification) {
        }
    }
    
    enum OneWeigher implements Weigher<Object, Object>
    {
        INSTANCE;
        
        @Override
        public int weigh(final Object o, final Object o2) {
            return 1;
        }
    }
}
