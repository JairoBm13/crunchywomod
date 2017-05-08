// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.io.Serializable;
import java.util.AbstractMap;
import com.google.common.base.Ascii;
import com.google.common.base.Preconditions;
import java.util.concurrent.ConcurrentMap;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Ticker;
import com.google.common.base.Equivalence;

public final class MapMaker extends GenericMapMaker<Object, Object>
{
    int concurrencyLevel;
    long expireAfterAccessNanos;
    long expireAfterWriteNanos;
    int initialCapacity;
    Equivalence<Object> keyEquivalence;
    MapMakerInternalMap.Strength keyStrength;
    int maximumSize;
    RemovalCause nullRemovalCause;
    Ticker ticker;
    boolean useCustomMap;
    MapMakerInternalMap.Strength valueStrength;
    
    public MapMaker() {
        this.initialCapacity = -1;
        this.concurrencyLevel = -1;
        this.maximumSize = -1;
        this.expireAfterWriteNanos = -1L;
        this.expireAfterAccessNanos = -1L;
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
    
    MapMakerInternalMap.Strength getKeyStrength() {
        return Objects.firstNonNull(this.keyStrength, MapMakerInternalMap.Strength.STRONG);
    }
    
    Ticker getTicker() {
        return Objects.firstNonNull(this.ticker, Ticker.systemTicker());
    }
    
    MapMakerInternalMap.Strength getValueStrength() {
        return Objects.firstNonNull(this.valueStrength, MapMakerInternalMap.Strength.STRONG);
    }
    
    @Deprecated
    public <K, V> ConcurrentMap<K, V> makeComputingMap(final Function<? super K, ? extends V> function) {
        if (this.nullRemovalCause == null) {
            return new ComputingConcurrentHashMap.ComputingMapAdapter<K, V>(this, function);
        }
        return new NullComputingConcurrentMap<K, V>(this, function);
    }
    
    MapMaker setKeyStrength(final MapMakerInternalMap.Strength strength) {
        Preconditions.checkState(this.keyStrength == null, "Key strength was already set to %s", this.keyStrength);
        this.keyStrength = Preconditions.checkNotNull(strength);
        if (strength != MapMakerInternalMap.Strength.STRONG) {
            this.useCustomMap = true;
        }
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
        if (this.maximumSize != -1) {
            stringHelper.add("maximumSize", this.maximumSize);
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
        if (this.removalListener != null) {
            stringHelper.addValue("removalListener");
        }
        return stringHelper.toString();
    }
    
    public MapMaker weakKeys() {
        return this.setKeyStrength(MapMakerInternalMap.Strength.WEAK);
    }
    
    static final class NullComputingConcurrentMap<K, V> extends NullConcurrentMap<K, V>
    {
        final Function<? super K, ? extends V> computingFunction;
        
        NullComputingConcurrentMap(final MapMaker mapMaker, final Function<? super K, ? extends V> function) {
            super(mapMaker);
            this.computingFunction = Preconditions.checkNotNull(function);
        }
        
        private V compute(final K k) {
            Preconditions.checkNotNull(k);
            try {
                return (V)this.computingFunction.apply((Object)k);
            }
            catch (ComputationException ex) {
                throw ex;
            }
            catch (Throwable t) {
                throw new ComputationException(t);
            }
        }
        
        @Override
        public V get(final Object o) {
            final V compute = this.compute(o);
            Preconditions.checkNotNull(compute, (Object)(this.computingFunction + " returned null for key " + o + "."));
            this.notifyRemoval((K)o, compute);
            return compute;
        }
    }
    
    static class NullConcurrentMap<K, V> extends AbstractMap<K, V> implements Serializable, ConcurrentMap<K, V>
    {
        private final RemovalCause removalCause;
        private final RemovalListener<K, V> removalListener;
        
        NullConcurrentMap(final MapMaker mapMaker) {
            this.removalListener = mapMaker.getRemovalListener();
            this.removalCause = mapMaker.nullRemovalCause;
        }
        
        @Override
        public boolean containsKey(final Object o) {
            return false;
        }
        
        @Override
        public boolean containsValue(final Object o) {
            return false;
        }
        
        @Override
        public Set<Entry<K, V>> entrySet() {
            return Collections.emptySet();
        }
        
        @Override
        public V get(final Object o) {
            return null;
        }
        
        void notifyRemoval(final K k, final V v) {
            this.removalListener.onRemoval((RemovalNotification<K, V>)new RemovalNotification(k, v, this.removalCause));
        }
        
        @Override
        public V put(final K k, final V v) {
            Preconditions.checkNotNull(k);
            Preconditions.checkNotNull(v);
            this.notifyRemoval(k, v);
            return null;
        }
        
        @Override
        public V putIfAbsent(final K k, final V v) {
            return this.put(k, v);
        }
        
        @Override
        public V remove(final Object o) {
            return null;
        }
        
        @Override
        public boolean remove(final Object o, final Object o2) {
            return false;
        }
        
        @Override
        public V replace(final K k, final V v) {
            Preconditions.checkNotNull(k);
            Preconditions.checkNotNull(v);
            return null;
        }
        
        @Override
        public boolean replace(final K k, final V v, final V v2) {
            Preconditions.checkNotNull(k);
            Preconditions.checkNotNull(v2);
            return false;
        }
    }
    
    enum RemovalCause
    {
        COLLECTED {
        }, 
        EXPIRED {
        }, 
        EXPLICIT {
        }, 
        REPLACED {
        }, 
        SIZE {
        };
    }
    
    interface RemovalListener<K, V>
    {
        void onRemoval(final RemovalNotification<K, V> p0);
    }
    
    static final class RemovalNotification<K, V> extends ImmutableEntry<K, V>
    {
        private final RemovalCause cause;
        
        RemovalNotification(final K k, final V v, final RemovalCause cause) {
            super(k, v);
            this.cause = cause;
        }
    }
}
