// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.lang.ref.WeakReference;
import java.util.AbstractCollection;
import java.util.concurrent.ExecutionException;
import java.lang.ref.SoftReference;
import java.lang.ref.Reference;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.NoSuchElementException;
import com.google.common.collect.MapMakerInternalMap$com.google.common.collect.MapMakerInternalMap$com.google.common.collect.MapMakerInternalMap$WriteThroughEntry;
import java.util.AbstractSet;
import com.google.common.collect.MapMakerInternalMap$com.google.common.collect.MapMakerInternalMap$HashIterator;
import com.google.common.primitives.Ints;
import com.google.common.base.Preconditions;
import java.util.logging.Level;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Iterator;
import java.util.AbstractQueue;
import java.lang.ref.ReferenceQueue;
import java.util.Collection;
import com.google.common.base.Ticker;
import com.google.common.base.Equivalence;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.Queue;
import java.util.concurrent.ConcurrentMap;
import java.io.Serializable;
import java.util.AbstractMap;

class MapMakerInternalMap<K, V> extends AbstractMap<K, V> implements Serializable, ConcurrentMap<K, V>
{
    static final Queue<?> DISCARDING_QUEUE;
    static final ValueReference<Object, Object> UNSET;
    private static final Logger logger;
    final int concurrencyLevel;
    final transient EntryFactory entryFactory;
    transient Set<Entry<K, V>> entrySet;
    final long expireAfterAccessNanos;
    final long expireAfterWriteNanos;
    final Equivalence<Object> keyEquivalence;
    transient Set<K> keySet;
    final Strength keyStrength;
    final int maximumSize;
    final MapMaker.RemovalListener<K, V> removalListener;
    final Queue<MapMaker.RemovalNotification<K, V>> removalNotificationQueue;
    final transient int segmentMask;
    final transient int segmentShift;
    final transient Segment<K, V>[] segments;
    final Ticker ticker;
    final Equivalence<Object> valueEquivalence;
    final Strength valueStrength;
    transient Collection<V> values;
    
    static {
        logger = Logger.getLogger(MapMakerInternalMap.class.getName());
        UNSET = (ValueReference)new ValueReference<Object, Object>() {
            @Override
            public void clear(final ValueReference<Object, Object> valueReference) {
            }
            
            @Override
            public ValueReference<Object, Object> copyFor(final ReferenceQueue<Object> referenceQueue, final Object o, final ReferenceEntry<Object, Object> referenceEntry) {
                return this;
            }
            
            @Override
            public Object get() {
                return null;
            }
            
            @Override
            public ReferenceEntry<Object, Object> getEntry() {
                return null;
            }
            
            @Override
            public boolean isComputingReference() {
                return false;
            }
            
            @Override
            public Object waitForValue() {
                return null;
            }
        };
        DISCARDING_QUEUE = new AbstractQueue<Object>() {
            @Override
            public Iterator<Object> iterator() {
                return Iterators.emptyIterator();
            }
            
            @Override
            public boolean offer(final Object o) {
                return true;
            }
            
            @Override
            public Object peek() {
                return null;
            }
            
            @Override
            public Object poll() {
                return null;
            }
            
            @Override
            public int size() {
                return 0;
            }
        };
    }
    
    MapMakerInternalMap(final MapMaker mapMaker) {
        this.concurrencyLevel = Math.min(mapMaker.getConcurrencyLevel(), 65536);
        this.keyStrength = mapMaker.getKeyStrength();
        this.valueStrength = mapMaker.getValueStrength();
        this.keyEquivalence = mapMaker.getKeyEquivalence();
        this.valueEquivalence = this.valueStrength.defaultEquivalence();
        this.maximumSize = mapMaker.maximumSize;
        this.expireAfterAccessNanos = mapMaker.getExpireAfterAccessNanos();
        this.expireAfterWriteNanos = mapMaker.getExpireAfterWriteNanos();
        this.entryFactory = EntryFactory.getFactory(this.keyStrength, this.expires(), this.evictsBySize());
        this.ticker = mapMaker.getTicker();
        this.removalListener = mapMaker.getRemovalListener();
        Queue<MapMaker.RemovalNotification<K, V>> discardingQueue;
        if (this.removalListener == GenericMapMaker.NullListener.INSTANCE) {
            discardingQueue = discardingQueue();
        }
        else {
            discardingQueue = new ConcurrentLinkedQueue<MapMaker.RemovalNotification<K, V>>();
        }
        this.removalNotificationQueue = discardingQueue;
        int n2;
        final int n = n2 = Math.min(mapMaker.getInitialCapacity(), 1073741824);
        if (this.evictsBySize()) {
            n2 = Math.min(n, this.maximumSize);
        }
        int n3 = 0;
        int n4;
        for (n4 = 1; n4 < this.concurrencyLevel && (!this.evictsBySize() || n4 * 2 <= this.maximumSize); n4 <<= 1) {
            ++n3;
        }
        this.segmentShift = 32 - n3;
        this.segmentMask = n4 - 1;
        this.segments = this.newSegmentArray(n4);
        int n6;
        final int n5 = n6 = n2 / n4;
        if (n5 * n4 < n2) {
            n6 = n5 + 1;
        }
        int i;
        for (i = 1; i < n6; i <<= 1) {}
        if (this.evictsBySize()) {
            int n7 = this.maximumSize / n4 + 1;
            final int maximumSize = this.maximumSize;
            int n8;
            for (int j = 0; j < this.segments.length; ++j, n7 = n8) {
                n8 = n7;
                if (j == maximumSize % n4) {
                    n8 = n7 - 1;
                }
                this.segments[j] = this.createSegment(i, n8);
            }
        }
        else {
            for (int k = 0; k < this.segments.length; ++k) {
                this.segments[k] = this.createSegment(i, -1);
            }
        }
    }
    
    static <K, V> void connectEvictables(final ReferenceEntry<K, V> previousEvictable, final ReferenceEntry<K, V> nextEvictable) {
        previousEvictable.setNextEvictable(nextEvictable);
        nextEvictable.setPreviousEvictable(previousEvictable);
    }
    
    static <K, V> void connectExpirables(final ReferenceEntry<K, V> previousExpirable, final ReferenceEntry<K, V> nextExpirable) {
        previousExpirable.setNextExpirable(nextExpirable);
        nextExpirable.setPreviousExpirable(previousExpirable);
    }
    
    static <E> Queue<E> discardingQueue() {
        return (Queue<E>)MapMakerInternalMap.DISCARDING_QUEUE;
    }
    
    static <K, V> ReferenceEntry<K, V> nullEntry() {
        return (ReferenceEntry<K, V>)NullEntry.INSTANCE;
    }
    
    static <K, V> void nullifyEvictable(final ReferenceEntry<K, V> referenceEntry) {
        final ReferenceEntry<K, V> nullEntry = nullEntry();
        referenceEntry.setNextEvictable(nullEntry);
        referenceEntry.setPreviousEvictable(nullEntry);
    }
    
    static <K, V> void nullifyExpirable(final ReferenceEntry<K, V> referenceEntry) {
        final ReferenceEntry<K, V> nullEntry = nullEntry();
        referenceEntry.setNextExpirable(nullEntry);
        referenceEntry.setPreviousExpirable(nullEntry);
    }
    
    static int rehash(int n) {
        n += (n << 15 ^ 0xFFFFCD7D);
        n ^= n >>> 10;
        n += n << 3;
        n ^= n >>> 6;
        n += (n << 2) + (n << 14);
        return n >>> 16 ^ n;
    }
    
    static <K, V> ValueReference<K, V> unset() {
        return (ValueReference<K, V>)MapMakerInternalMap.UNSET;
    }
    
    @Override
    public void clear() {
        final Segment<K, V>[] segments = this.segments;
        for (int length = segments.length, i = 0; i < length; ++i) {
            segments[i].clear();
        }
    }
    
    @Override
    public boolean containsKey(final Object o) {
        if (o == null) {
            return false;
        }
        final int hash = this.hash(o);
        return this.segmentFor(hash).containsKey(o, hash);
    }
    
    @Override
    public boolean containsValue(final Object o) {
        if (o == null) {
            return false;
        }
        final Segment<K, V>[] segments = this.segments;
        long n = -1L;
        long n2;
        for (int i = 0; i < 3; ++i, n = n2) {
            n2 = 0L;
            for (int length = segments.length, j = 0; j < length; ++j) {
                final Segment<K, V> segment = segments[j];
                final int count = segment.count;
                final AtomicReferenceArray<ReferenceEntry<K, V>> table = segment.table;
                for (int k = 0; k < table.length(); ++k) {
                    for (ReferenceEntry<K, V> next = (ReferenceEntry<K, V>)(ReferenceEntry)table.get(k); next != null; next = next.getNext()) {
                        final V liveValue = segment.getLiveValue(next);
                        if (liveValue != null && this.valueEquivalence.equivalent(o, liveValue)) {
                            return true;
                        }
                    }
                }
                n2 += segment.modCount;
            }
            if (n2 == n) {
                break;
            }
        }
        return false;
    }
    
    Segment<K, V> createSegment(final int n, final int n2) {
        return new Segment<K, V>(this, n, n2);
    }
    
    @Override
    public Set<Entry<K, V>> entrySet() {
        final Set<Entry<K, V>> entrySet = this.entrySet;
        if (entrySet != null) {
            return entrySet;
        }
        return this.entrySet = new EntrySet();
    }
    
    boolean evictsBySize() {
        return this.maximumSize != -1;
    }
    
    boolean expires() {
        return this.expiresAfterWrite() || this.expiresAfterAccess();
    }
    
    boolean expiresAfterAccess() {
        return this.expireAfterAccessNanos > 0L;
    }
    
    boolean expiresAfterWrite() {
        return this.expireAfterWriteNanos > 0L;
    }
    
    @Override
    public V get(final Object o) {
        if (o == null) {
            return null;
        }
        final int hash = this.hash(o);
        return this.segmentFor(hash).get(o, hash);
    }
    
    V getLiveValue(final ReferenceEntry<K, V> referenceEntry) {
        V v;
        if (referenceEntry.getKey() == null) {
            v = null;
        }
        else {
            final V value = referenceEntry.getValueReference().get();
            if (value == null) {
                return null;
            }
            v = value;
            if (this.expires()) {
                v = value;
                if (this.isExpired(referenceEntry)) {
                    return null;
                }
            }
        }
        return v;
    }
    
    int hash(final Object o) {
        return rehash(this.keyEquivalence.hash(o));
    }
    
    @Override
    public boolean isEmpty() {
        long n = 0L;
        final Segment<K, V>[] segments = this.segments;
        for (int i = 0; i < segments.length; ++i) {
            if (segments[i].count != 0) {
                return false;
            }
            n += segments[i].modCount;
        }
        if (n != 0L) {
            for (int j = 0; j < segments.length; ++j) {
                if (segments[j].count != 0) {
                    return false;
                }
                n -= segments[j].modCount;
            }
            if (n != 0L) {
                return false;
            }
        }
        return true;
    }
    
    boolean isExpired(final ReferenceEntry<K, V> referenceEntry) {
        return this.isExpired(referenceEntry, this.ticker.read());
    }
    
    boolean isExpired(final ReferenceEntry<K, V> referenceEntry, final long n) {
        return n - referenceEntry.getExpirationTime() > 0L;
    }
    
    @Override
    public Set<K> keySet() {
        final Set<K> keySet = this.keySet;
        if (keySet != null) {
            return keySet;
        }
        return this.keySet = new KeySet();
    }
    
    final Segment<K, V>[] newSegmentArray(final int n) {
        return (Segment<K, V>[])new Segment[n];
    }
    
    void processPendingNotifications() {
        while (true) {
            final MapMaker.RemovalNotification removalNotification = this.removalNotificationQueue.poll();
            if (removalNotification == null) {
                break;
            }
            try {
                this.removalListener.onRemoval(removalNotification);
            }
            catch (Exception ex) {
                MapMakerInternalMap.logger.log(Level.WARNING, "Exception thrown by removal listener", ex);
            }
        }
    }
    
    @Override
    public V put(final K k, final V v) {
        Preconditions.checkNotNull(k);
        Preconditions.checkNotNull(v);
        final int hash = this.hash(k);
        return this.segmentFor(hash).put(k, hash, v, false);
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends V> map) {
        for (final Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            this.put(entry.getKey(), (V)entry.getValue());
        }
    }
    
    @Override
    public V putIfAbsent(final K k, final V v) {
        Preconditions.checkNotNull(k);
        Preconditions.checkNotNull(v);
        final int hash = this.hash(k);
        return this.segmentFor(hash).put(k, hash, v, true);
    }
    
    void reclaimKey(final ReferenceEntry<K, V> referenceEntry) {
        final int hash = referenceEntry.getHash();
        this.segmentFor(hash).reclaimKey(referenceEntry, hash);
    }
    
    void reclaimValue(final ValueReference<K, V> valueReference) {
        final ReferenceEntry<K, V> entry = valueReference.getEntry();
        final int hash = entry.getHash();
        this.segmentFor(hash).reclaimValue(entry.getKey(), hash, valueReference);
    }
    
    @Override
    public V remove(final Object o) {
        if (o == null) {
            return null;
        }
        final int hash = this.hash(o);
        return this.segmentFor(hash).remove(o, hash);
    }
    
    @Override
    public boolean remove(final Object o, final Object o2) {
        if (o == null || o2 == null) {
            return false;
        }
        final int hash = this.hash(o);
        return this.segmentFor(hash).remove(o, hash, o2);
    }
    
    @Override
    public V replace(final K k, final V v) {
        Preconditions.checkNotNull(k);
        Preconditions.checkNotNull(v);
        final int hash = this.hash(k);
        return this.segmentFor(hash).replace(k, hash, v);
    }
    
    @Override
    public boolean replace(final K k, final V v, final V v2) {
        Preconditions.checkNotNull(k);
        Preconditions.checkNotNull(v2);
        if (v == null) {
            return false;
        }
        final int hash = this.hash(k);
        return this.segmentFor(hash).replace(k, hash, v, v2);
    }
    
    Segment<K, V> segmentFor(final int n) {
        return this.segments[n >>> this.segmentShift & this.segmentMask];
    }
    
    @Override
    public int size() {
        final Segment<K, V>[] segments = this.segments;
        long n = 0L;
        for (int i = 0; i < segments.length; ++i) {
            n += segments[i].count;
        }
        return Ints.saturatedCast(n);
    }
    
    boolean usesKeyReferences() {
        return this.keyStrength != Strength.STRONG;
    }
    
    boolean usesValueReferences() {
        return this.valueStrength != Strength.STRONG;
    }
    
    @Override
    public Collection<V> values() {
        final Collection<V> values = this.values;
        if (values != null) {
            return values;
        }
        return this.values = new Values();
    }
    
    abstract static class AbstractReferenceEntry<K, V> implements ReferenceEntry<K, V>
    {
        @Override
        public long getExpirationTime() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int getHash() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public K getKey() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getNext() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getNextEvictable() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getNextExpirable() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousEvictable() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousExpirable() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ValueReference<K, V> getValueReference() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setExpirationTime(final long n) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setNextEvictable(final ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setNextExpirable(final ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setPreviousEvictable(final ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setPreviousExpirable(final ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setValueReference(final ValueReference<K, V> valueReference) {
            throw new UnsupportedOperationException();
        }
    }
    
    enum EntryFactory
    {
        SOFT {
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> segment, final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
                return new SoftEntry<K, V>(segment.keyReferenceQueue, k, n, referenceEntry);
            }
        }, 
        SOFT_EVICTABLE {
            @Override
             <K, V> ReferenceEntry<K, V> copyEntry(final Segment<K, V> segment, final ReferenceEntry<K, V> referenceEntry, final ReferenceEntry<K, V> referenceEntry2) {
                final ReferenceEntry<K, V> copyEntry = super.copyEntry(segment, referenceEntry, referenceEntry2);
                this.copyEvictableEntry(referenceEntry, copyEntry);
                return copyEntry;
            }
            
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> segment, final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
                return new SoftEvictableEntry<K, V>(segment.keyReferenceQueue, k, n, referenceEntry);
            }
        }, 
        SOFT_EXPIRABLE {
            @Override
             <K, V> ReferenceEntry<K, V> copyEntry(final Segment<K, V> segment, final ReferenceEntry<K, V> referenceEntry, final ReferenceEntry<K, V> referenceEntry2) {
                final ReferenceEntry<K, V> copyEntry = super.copyEntry(segment, referenceEntry, referenceEntry2);
                this.copyExpirableEntry(referenceEntry, copyEntry);
                return copyEntry;
            }
            
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> segment, final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
                return new SoftExpirableEntry<K, V>(segment.keyReferenceQueue, k, n, referenceEntry);
            }
        }, 
        SOFT_EXPIRABLE_EVICTABLE {
            @Override
             <K, V> ReferenceEntry<K, V> copyEntry(final Segment<K, V> segment, final ReferenceEntry<K, V> referenceEntry, final ReferenceEntry<K, V> referenceEntry2) {
                final ReferenceEntry<K, V> copyEntry = super.copyEntry(segment, referenceEntry, referenceEntry2);
                this.copyExpirableEntry(referenceEntry, copyEntry);
                this.copyEvictableEntry(referenceEntry, copyEntry);
                return copyEntry;
            }
            
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> segment, final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
                return new SoftExpirableEvictableEntry<K, V>(segment.keyReferenceQueue, k, n, referenceEntry);
            }
        }, 
        STRONG {
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> segment, final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
                return new StrongEntry<K, V>(k, n, referenceEntry);
            }
        }, 
        STRONG_EVICTABLE {
            @Override
             <K, V> ReferenceEntry<K, V> copyEntry(final Segment<K, V> segment, final ReferenceEntry<K, V> referenceEntry, final ReferenceEntry<K, V> referenceEntry2) {
                final ReferenceEntry<K, V> copyEntry = super.copyEntry(segment, referenceEntry, referenceEntry2);
                this.copyEvictableEntry(referenceEntry, copyEntry);
                return copyEntry;
            }
            
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> segment, final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
                return new StrongEvictableEntry<K, V>(k, n, referenceEntry);
            }
        }, 
        STRONG_EXPIRABLE {
            @Override
             <K, V> ReferenceEntry<K, V> copyEntry(final Segment<K, V> segment, final ReferenceEntry<K, V> referenceEntry, final ReferenceEntry<K, V> referenceEntry2) {
                final ReferenceEntry<K, V> copyEntry = super.copyEntry(segment, referenceEntry, referenceEntry2);
                this.copyExpirableEntry(referenceEntry, copyEntry);
                return copyEntry;
            }
            
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> segment, final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
                return new StrongExpirableEntry<K, V>(k, n, referenceEntry);
            }
        }, 
        STRONG_EXPIRABLE_EVICTABLE {
            @Override
             <K, V> ReferenceEntry<K, V> copyEntry(final Segment<K, V> segment, final ReferenceEntry<K, V> referenceEntry, final ReferenceEntry<K, V> referenceEntry2) {
                final ReferenceEntry<K, V> copyEntry = super.copyEntry(segment, referenceEntry, referenceEntry2);
                this.copyExpirableEntry(referenceEntry, copyEntry);
                this.copyEvictableEntry(referenceEntry, copyEntry);
                return copyEntry;
            }
            
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> segment, final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
                return new StrongExpirableEvictableEntry<K, V>(k, n, referenceEntry);
            }
        }, 
        WEAK {
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> segment, final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
                return new WeakEntry<K, V>(segment.keyReferenceQueue, k, n, referenceEntry);
            }
        }, 
        WEAK_EVICTABLE {
            @Override
             <K, V> ReferenceEntry<K, V> copyEntry(final Segment<K, V> segment, final ReferenceEntry<K, V> referenceEntry, final ReferenceEntry<K, V> referenceEntry2) {
                final ReferenceEntry<K, V> copyEntry = super.copyEntry(segment, referenceEntry, referenceEntry2);
                this.copyEvictableEntry(referenceEntry, copyEntry);
                return copyEntry;
            }
            
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> segment, final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
                return new WeakEvictableEntry<K, V>(segment.keyReferenceQueue, k, n, referenceEntry);
            }
        }, 
        WEAK_EXPIRABLE {
            @Override
             <K, V> ReferenceEntry<K, V> copyEntry(final Segment<K, V> segment, final ReferenceEntry<K, V> referenceEntry, final ReferenceEntry<K, V> referenceEntry2) {
                final ReferenceEntry<K, V> copyEntry = super.copyEntry(segment, referenceEntry, referenceEntry2);
                this.copyExpirableEntry(referenceEntry, copyEntry);
                return copyEntry;
            }
            
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> segment, final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
                return new WeakExpirableEntry<K, V>(segment.keyReferenceQueue, k, n, referenceEntry);
            }
        }, 
        WEAK_EXPIRABLE_EVICTABLE {
            @Override
             <K, V> ReferenceEntry<K, V> copyEntry(final Segment<K, V> segment, final ReferenceEntry<K, V> referenceEntry, final ReferenceEntry<K, V> referenceEntry2) {
                final ReferenceEntry<K, V> copyEntry = super.copyEntry(segment, referenceEntry, referenceEntry2);
                this.copyExpirableEntry(referenceEntry, copyEntry);
                this.copyEvictableEntry(referenceEntry, copyEntry);
                return copyEntry;
            }
            
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> segment, final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
                return new WeakExpirableEvictableEntry<K, V>(segment.keyReferenceQueue, k, n, referenceEntry);
            }
        };
        
        static final EntryFactory[][] factories;
        
        static {
            factories = new EntryFactory[][] { { EntryFactory.STRONG, EntryFactory.STRONG_EXPIRABLE, EntryFactory.STRONG_EVICTABLE, EntryFactory.STRONG_EXPIRABLE_EVICTABLE }, { EntryFactory.SOFT, EntryFactory.SOFT_EXPIRABLE, EntryFactory.SOFT_EVICTABLE, EntryFactory.SOFT_EXPIRABLE_EVICTABLE }, { EntryFactory.WEAK, EntryFactory.WEAK_EXPIRABLE, EntryFactory.WEAK_EVICTABLE, EntryFactory.WEAK_EXPIRABLE_EVICTABLE } };
        }
        
        static EntryFactory getFactory(final Strength strength, final boolean b, final boolean b2) {
            int n = false ? 1 : 0;
            boolean b3;
            if (b) {
                b3 = true;
            }
            else {
                b3 = false;
            }
            if (b2) {
                n = 2;
            }
            return EntryFactory.factories[strength.ordinal()][(b3 ? 1 : 0) | n];
        }
        
         <K, V> ReferenceEntry<K, V> copyEntry(final Segment<K, V> segment, final ReferenceEntry<K, V> referenceEntry, final ReferenceEntry<K, V> referenceEntry2) {
            return this.newEntry(segment, referenceEntry.getKey(), referenceEntry.getHash(), referenceEntry2);
        }
        
         <K, V> void copyEvictableEntry(final ReferenceEntry<K, V> referenceEntry, final ReferenceEntry<K, V> referenceEntry2) {
            MapMakerInternalMap.connectEvictables((ReferenceEntry<K, V>)referenceEntry.getPreviousEvictable(), referenceEntry2);
            MapMakerInternalMap.connectEvictables(referenceEntry2, (ReferenceEntry<K, V>)referenceEntry.getNextEvictable());
            MapMakerInternalMap.nullifyEvictable((ReferenceEntry)referenceEntry);
        }
        
         <K, V> void copyExpirableEntry(final ReferenceEntry<K, V> referenceEntry, final ReferenceEntry<K, V> referenceEntry2) {
            referenceEntry2.setExpirationTime(referenceEntry.getExpirationTime());
            MapMakerInternalMap.connectExpirables(referenceEntry.getPreviousExpirable(), referenceEntry2);
            MapMakerInternalMap.connectExpirables(referenceEntry2, referenceEntry.getNextExpirable());
            MapMakerInternalMap.nullifyExpirable(referenceEntry);
        }
        
        abstract <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> p0, final K p1, final int p2, final ReferenceEntry<K, V> p3);
    }
    
    final class EntryIterator extends MapMakerInternalMap$HashIterator implements Iterator<Entry<K, V>>
    {
        public Entry<K, V> next() {
            return this.nextEntry();
        }
    }
    
    final class EntrySet extends AbstractSet<Entry<K, V>>
    {
        @Override
        public void clear() {
            MapMakerInternalMap.this.clear();
        }
        
        @Override
        public boolean contains(final Object o) {
            if (o instanceof Entry) {
                final Entry entry = (Entry)o;
                final Object key = entry.getKey();
                if (key != null) {
                    final V value = MapMakerInternalMap.this.get(key);
                    if (value != null && MapMakerInternalMap.this.valueEquivalence.equivalent(entry.getValue(), value)) {
                        return true;
                    }
                }
            }
            return false;
        }
        
        @Override
        public boolean isEmpty() {
            return MapMakerInternalMap.this.isEmpty();
        }
        
        @Override
        public Iterator<Entry<K, V>> iterator() {
            return new EntryIterator();
        }
        
        @Override
        public boolean remove(final Object o) {
            if (o instanceof Entry) {
                final Entry entry = (Entry)o;
                final Object key = entry.getKey();
                if (key != null && MapMakerInternalMap.this.remove(key, entry.getValue())) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public int size() {
            return MapMakerInternalMap.this.size();
        }
    }
    
    static final class EvictionQueue<K, V> extends AbstractQueue<ReferenceEntry<K, V>>
    {
        final ReferenceEntry<K, V> head;
        
        EvictionQueue() {
            this.head = new AbstractReferenceEntry<K, V>() {
                ReferenceEntry<K, V> nextEvictable = this;
                ReferenceEntry<K, V> previousEvictable = this;
                
                @Override
                public ReferenceEntry<K, V> getNextEvictable() {
                    return this.nextEvictable;
                }
                
                @Override
                public ReferenceEntry<K, V> getPreviousEvictable() {
                    return this.previousEvictable;
                }
                
                @Override
                public void setNextEvictable(final ReferenceEntry<K, V> nextEvictable) {
                    this.nextEvictable = nextEvictable;
                }
                
                @Override
                public void setPreviousEvictable(final ReferenceEntry<K, V> previousEvictable) {
                    this.previousEvictable = previousEvictable;
                }
            };
        }
        
        @Override
        public void clear() {
            ReferenceEntry<K, V> nextEvictable2;
            for (ReferenceEntry<K, V> nextEvictable = this.head.getNextEvictable(); nextEvictable != this.head; nextEvictable = nextEvictable2) {
                nextEvictable2 = nextEvictable.getNextEvictable();
                MapMakerInternalMap.nullifyEvictable(nextEvictable);
            }
            this.head.setNextEvictable(this.head);
            this.head.setPreviousEvictable(this.head);
        }
        
        @Override
        public boolean contains(final Object o) {
            return ((ReferenceEntry)o).getNextEvictable() != NullEntry.INSTANCE;
        }
        
        @Override
        public boolean isEmpty() {
            return this.head.getNextEvictable() == this.head;
        }
        
        @Override
        public Iterator<ReferenceEntry<K, V>> iterator() {
            return new AbstractSequentialIterator<ReferenceEntry<K, V>>(this.peek()) {
                @Override
                protected ReferenceEntry<K, V> computeNext(final ReferenceEntry<K, V> referenceEntry) {
                    Object nextEvictable;
                    if ((nextEvictable = referenceEntry.getNextEvictable()) == EvictionQueue.this.head) {
                        nextEvictable = null;
                    }
                    return (ReferenceEntry<K, V>)nextEvictable;
                }
            };
        }
        
        @Override
        public boolean offer(final ReferenceEntry<K, V> referenceEntry) {
            MapMakerInternalMap.connectEvictables(referenceEntry.getPreviousEvictable(), referenceEntry.getNextEvictable());
            MapMakerInternalMap.connectEvictables(this.head.getPreviousEvictable(), referenceEntry);
            MapMakerInternalMap.connectEvictables(referenceEntry, this.head);
            return true;
        }
        
        @Override
        public ReferenceEntry<K, V> peek() {
            Object nextEvictable;
            if ((nextEvictable = this.head.getNextEvictable()) == this.head) {
                nextEvictable = null;
            }
            return (ReferenceEntry<K, V>)nextEvictable;
        }
        
        @Override
        public ReferenceEntry<K, V> poll() {
            final ReferenceEntry<K, V> nextEvictable = this.head.getNextEvictable();
            if (nextEvictable == this.head) {
                return null;
            }
            this.remove(nextEvictable);
            return nextEvictable;
        }
        
        @Override
        public boolean remove(final Object o) {
            final ReferenceEntry referenceEntry = (ReferenceEntry)o;
            final ReferenceEntry<K, V> previousEvictable = referenceEntry.getPreviousEvictable();
            final ReferenceEntry<K, V> nextEvictable = referenceEntry.getNextEvictable();
            MapMakerInternalMap.connectEvictables(previousEvictable, nextEvictable);
            MapMakerInternalMap.nullifyEvictable((ReferenceEntry<Object, Object>)referenceEntry);
            return nextEvictable != NullEntry.INSTANCE;
        }
        
        @Override
        public int size() {
            int n = 0;
            for (ReferenceEntry<K, V> referenceEntry = this.head.getNextEvictable(); referenceEntry != this.head; referenceEntry = referenceEntry.getNextEvictable()) {
                ++n;
            }
            return n;
        }
    }
    
    static final class ExpirationQueue<K, V> extends AbstractQueue<ReferenceEntry<K, V>>
    {
        final ReferenceEntry<K, V> head;
        
        ExpirationQueue() {
            this.head = new AbstractReferenceEntry<K, V>() {
                ReferenceEntry<K, V> nextExpirable = this;
                ReferenceEntry<K, V> previousExpirable = this;
                
                @Override
                public long getExpirationTime() {
                    return Long.MAX_VALUE;
                }
                
                @Override
                public ReferenceEntry<K, V> getNextExpirable() {
                    return this.nextExpirable;
                }
                
                @Override
                public ReferenceEntry<K, V> getPreviousExpirable() {
                    return this.previousExpirable;
                }
                
                @Override
                public void setExpirationTime(final long n) {
                }
                
                @Override
                public void setNextExpirable(final ReferenceEntry<K, V> nextExpirable) {
                    this.nextExpirable = nextExpirable;
                }
                
                @Override
                public void setPreviousExpirable(final ReferenceEntry<K, V> previousExpirable) {
                    this.previousExpirable = previousExpirable;
                }
            };
        }
        
        @Override
        public void clear() {
            ReferenceEntry<K, V> nextExpirable2;
            for (ReferenceEntry<K, V> nextExpirable = this.head.getNextExpirable(); nextExpirable != this.head; nextExpirable = nextExpirable2) {
                nextExpirable2 = nextExpirable.getNextExpirable();
                MapMakerInternalMap.nullifyExpirable(nextExpirable);
            }
            this.head.setNextExpirable(this.head);
            this.head.setPreviousExpirable(this.head);
        }
        
        @Override
        public boolean contains(final Object o) {
            return ((ReferenceEntry)o).getNextExpirable() != NullEntry.INSTANCE;
        }
        
        @Override
        public boolean isEmpty() {
            return this.head.getNextExpirable() == this.head;
        }
        
        @Override
        public Iterator<ReferenceEntry<K, V>> iterator() {
            return new AbstractSequentialIterator<ReferenceEntry<K, V>>(this.peek()) {
                @Override
                protected ReferenceEntry<K, V> computeNext(final ReferenceEntry<K, V> referenceEntry) {
                    Object nextExpirable;
                    if ((nextExpirable = referenceEntry.getNextExpirable()) == ExpirationQueue.this.head) {
                        nextExpirable = null;
                    }
                    return (ReferenceEntry<K, V>)nextExpirable;
                }
            };
        }
        
        @Override
        public boolean offer(final ReferenceEntry<K, V> referenceEntry) {
            MapMakerInternalMap.connectExpirables(referenceEntry.getPreviousExpirable(), referenceEntry.getNextExpirable());
            MapMakerInternalMap.connectExpirables(this.head.getPreviousExpirable(), referenceEntry);
            MapMakerInternalMap.connectExpirables(referenceEntry, this.head);
            return true;
        }
        
        @Override
        public ReferenceEntry<K, V> peek() {
            Object nextExpirable;
            if ((nextExpirable = this.head.getNextExpirable()) == this.head) {
                nextExpirable = null;
            }
            return (ReferenceEntry<K, V>)nextExpirable;
        }
        
        @Override
        public ReferenceEntry<K, V> poll() {
            final ReferenceEntry<K, V> nextExpirable = this.head.getNextExpirable();
            if (nextExpirable == this.head) {
                return null;
            }
            this.remove(nextExpirable);
            return nextExpirable;
        }
        
        @Override
        public boolean remove(final Object o) {
            final ReferenceEntry referenceEntry = (ReferenceEntry)o;
            final ReferenceEntry<K, V> previousExpirable = referenceEntry.getPreviousExpirable();
            final ReferenceEntry<K, V> nextExpirable = referenceEntry.getNextExpirable();
            MapMakerInternalMap.connectExpirables(previousExpirable, nextExpirable);
            MapMakerInternalMap.nullifyExpirable((ReferenceEntry<Object, Object>)referenceEntry);
            return nextExpirable != NullEntry.INSTANCE;
        }
        
        @Override
        public int size() {
            int n = 0;
            for (ReferenceEntry<K, V> referenceEntry = this.head.getNextExpirable(); referenceEntry != this.head; referenceEntry = referenceEntry.getNextExpirable()) {
                ++n;
            }
            return n;
        }
    }
    
    abstract class HashIterator
    {
        Segment<K, V> currentSegment;
        AtomicReferenceArray<ReferenceEntry<K, V>> currentTable;
        MapMakerInternalMap$WriteThroughEntry lastReturned;
        ReferenceEntry<K, V> nextEntry;
        MapMakerInternalMap$WriteThroughEntry nextExternal;
        int nextSegmentIndex;
        int nextTableIndex;
        
        HashIterator() {
            this.nextSegmentIndex = MapMakerInternalMap.this.segments.length - 1;
            this.nextTableIndex = -1;
            this.advance();
        }
        
        final void advance() {
            this.nextExternal = null;
            if (!this.nextInChain() && !this.nextInTable()) {
                while (this.nextSegmentIndex >= 0) {
                    this.currentSegment = MapMakerInternalMap.this.segments[this.nextSegmentIndex--];
                    if (this.currentSegment.count != 0) {
                        this.currentTable = this.currentSegment.table;
                        this.nextTableIndex = this.currentTable.length() - 1;
                        if (this.nextInTable()) {
                            return;
                        }
                        continue;
                    }
                }
            }
        }
        
        boolean advanceTo(final ReferenceEntry<K, V> referenceEntry) {
            try {
                final K key = referenceEntry.getKey();
                final Object liveValue = MapMakerInternalMap.this.getLiveValue((ReferenceEntry<K, Object>)referenceEntry);
                if (liveValue != null) {
                    this.nextExternal = new WriteThroughEntry(key, (V)liveValue);
                    return true;
                }
                return false;
            }
            finally {
                this.currentSegment.postReadCleanup();
            }
        }
        
        public boolean hasNext() {
            return this.nextExternal != null;
        }
        
        com.google.common.collect.MapMakerInternalMap$com.google.common.collect.MapMakerInternalMap$WriteThroughEntry nextEntry() {
            if (this.nextExternal == null) {
                throw new NoSuchElementException();
            }
            this.lastReturned = this.nextExternal;
            this.advance();
            return (com.google.common.collect.MapMakerInternalMap$com.google.common.collect.MapMakerInternalMap$WriteThroughEntry)this.lastReturned;
        }
        
        boolean nextInChain() {
            if (this.nextEntry != null) {
                this.nextEntry = this.nextEntry.getNext();
                while (this.nextEntry != null) {
                    if (this.advanceTo(this.nextEntry)) {
                        return true;
                    }
                    this.nextEntry = this.nextEntry.getNext();
                }
            }
            return false;
        }
        
        boolean nextInTable() {
            while (this.nextTableIndex >= 0) {
                final ReferenceEntry nextEntry = (ReferenceEntry)this.currentTable.get(this.nextTableIndex--);
                this.nextEntry = (ReferenceEntry<K, V>)nextEntry;
                if (nextEntry != null && (this.advanceTo(this.nextEntry) || this.nextInChain())) {
                    return true;
                }
            }
            return false;
        }
        
        public void remove() {
            Preconditions.checkState(this.lastReturned != null);
            MapMakerInternalMap.this.remove(this.lastReturned.getKey());
            this.lastReturned = null;
        }
    }
    
    final class KeyIterator extends MapMakerInternalMap$HashIterator implements Iterator<K>
    {
        public K next() {
            return this.nextEntry().getKey();
        }
    }
    
    final class KeySet extends AbstractSet<K>
    {
        @Override
        public void clear() {
            MapMakerInternalMap.this.clear();
        }
        
        @Override
        public boolean contains(final Object o) {
            return MapMakerInternalMap.this.containsKey(o);
        }
        
        @Override
        public boolean isEmpty() {
            return MapMakerInternalMap.this.isEmpty();
        }
        
        @Override
        public Iterator<K> iterator() {
            return new KeyIterator();
        }
        
        @Override
        public boolean remove(final Object o) {
            return MapMakerInternalMap.this.remove(o) != null;
        }
        
        @Override
        public int size() {
            return MapMakerInternalMap.this.size();
        }
    }
    
    private enum NullEntry implements ReferenceEntry<Object, Object>
    {
        INSTANCE;
        
        @Override
        public long getExpirationTime() {
            return 0L;
        }
        
        @Override
        public int getHash() {
            return 0;
        }
        
        @Override
        public Object getKey() {
            return null;
        }
        
        @Override
        public ReferenceEntry<Object, Object> getNext() {
            return null;
        }
        
        @Override
        public ReferenceEntry<Object, Object> getNextEvictable() {
            return this;
        }
        
        @Override
        public ReferenceEntry<Object, Object> getNextExpirable() {
            return this;
        }
        
        @Override
        public ReferenceEntry<Object, Object> getPreviousEvictable() {
            return this;
        }
        
        @Override
        public ReferenceEntry<Object, Object> getPreviousExpirable() {
            return this;
        }
        
        @Override
        public ValueReference<Object, Object> getValueReference() {
            return null;
        }
        
        @Override
        public void setExpirationTime(final long n) {
        }
        
        @Override
        public void setNextEvictable(final ReferenceEntry<Object, Object> referenceEntry) {
        }
        
        @Override
        public void setNextExpirable(final ReferenceEntry<Object, Object> referenceEntry) {
        }
        
        @Override
        public void setPreviousEvictable(final ReferenceEntry<Object, Object> referenceEntry) {
        }
        
        @Override
        public void setPreviousExpirable(final ReferenceEntry<Object, Object> referenceEntry) {
        }
        
        @Override
        public void setValueReference(final ValueReference<Object, Object> valueReference) {
        }
    }
    
    interface ReferenceEntry<K, V>
    {
        long getExpirationTime();
        
        int getHash();
        
        K getKey();
        
        ReferenceEntry<K, V> getNext();
        
        ReferenceEntry<K, V> getNextEvictable();
        
        ReferenceEntry<K, V> getNextExpirable();
        
        ReferenceEntry<K, V> getPreviousEvictable();
        
        ReferenceEntry<K, V> getPreviousExpirable();
        
        ValueReference<K, V> getValueReference();
        
        void setExpirationTime(final long p0);
        
        void setNextEvictable(final ReferenceEntry<K, V> p0);
        
        void setNextExpirable(final ReferenceEntry<K, V> p0);
        
        void setPreviousEvictable(final ReferenceEntry<K, V> p0);
        
        void setPreviousExpirable(final ReferenceEntry<K, V> p0);
        
        void setValueReference(final ValueReference<K, V> p0);
    }
    
    static class Segment<K, V> extends ReentrantLock
    {
        volatile int count;
        final Queue<ReferenceEntry<K, V>> evictionQueue;
        final Queue<ReferenceEntry<K, V>> expirationQueue;
        final ReferenceQueue<K> keyReferenceQueue;
        final MapMakerInternalMap<K, V> map;
        final int maxSegmentSize;
        int modCount;
        final AtomicInteger readCount;
        final Queue<ReferenceEntry<K, V>> recencyQueue;
        volatile AtomicReferenceArray<ReferenceEntry<K, V>> table;
        int threshold;
        final ReferenceQueue<V> valueReferenceQueue;
        
        Segment(final MapMakerInternalMap<K, V> map, final int n, final int maxSegmentSize) {
            final ReferenceQueue<V> referenceQueue = null;
            this.readCount = new AtomicInteger();
            this.map = map;
            this.maxSegmentSize = maxSegmentSize;
            this.initTable(this.newEntryArray(n));
            ReferenceQueue<K> keyReferenceQueue;
            if (map.usesKeyReferences()) {
                keyReferenceQueue = new ReferenceQueue<K>();
            }
            else {
                keyReferenceQueue = null;
            }
            this.keyReferenceQueue = keyReferenceQueue;
            ReferenceQueue<V> valueReferenceQueue = referenceQueue;
            if (map.usesValueReferences()) {
                valueReferenceQueue = new ReferenceQueue<V>();
            }
            this.valueReferenceQueue = valueReferenceQueue;
            Queue<ReferenceEntry<K, V>> discardingQueue;
            if (map.evictsBySize() || map.expiresAfterAccess()) {
                discardingQueue = new ConcurrentLinkedQueue<ReferenceEntry<K, V>>();
            }
            else {
                discardingQueue = MapMakerInternalMap.discardingQueue();
            }
            this.recencyQueue = discardingQueue;
            Queue<ReferenceEntry<K, V>> discardingQueue2;
            if (map.evictsBySize()) {
                discardingQueue2 = (Queue<ReferenceEntry<K, V>>)new EvictionQueue();
            }
            else {
                discardingQueue2 = MapMakerInternalMap.discardingQueue();
            }
            this.evictionQueue = discardingQueue2;
            Queue<ReferenceEntry<K, V>> discardingQueue3;
            if (map.expires()) {
                discardingQueue3 = (Queue<ReferenceEntry<K, V>>)new ExpirationQueue();
            }
            else {
                discardingQueue3 = MapMakerInternalMap.discardingQueue();
            }
            this.expirationQueue = discardingQueue3;
        }
        
        void clear() {
            if (this.count == 0) {
                return;
            }
        Label_0084_Outer:
            while (true) {
                this.lock();
            Label_0084:
                while (true) {
                    Label_0177: {
                        while (true) {
                            int n = 0;
                            Label_0170: {
                                try {
                                    final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                                    if (this.map.removalNotificationQueue == MapMakerInternalMap.DISCARDING_QUEUE) {
                                        break Label_0177;
                                    }
                                    n = 0;
                                    if (n < table.length()) {
                                        for (ReferenceEntry<K, V> next = table.get(n); next != null; next = next.getNext()) {
                                            if (!next.getValueReference().isComputingReference()) {
                                                this.enqueueNotification(next, MapMaker.RemovalCause.EXPLICIT);
                                            }
                                        }
                                        break Label_0170;
                                    }
                                    break Label_0177;
                                    // iftrue(Label_0105:, n >= table.length())
                                    Block_8: {
                                        break Block_8;
                                    }
                                    table.set(n, (ReferenceEntry<K, V>)null);
                                    ++n;
                                    continue Label_0084;
                                    Label_0105: {
                                        this.clearReferenceQueues();
                                    }
                                    this.evictionQueue.clear();
                                    this.expirationQueue.clear();
                                    this.readCount.set(0);
                                    ++this.modCount;
                                    this.count = 0;
                                    return;
                                }
                                finally {
                                    this.unlock();
                                    this.postWriteCleanup();
                                }
                            }
                            ++n;
                            continue Label_0084_Outer;
                        }
                    }
                    int n = 0;
                    continue Label_0084;
                }
            }
        }
        
        void clearKeyReferenceQueue() {
            while (this.keyReferenceQueue.poll() != null) {}
        }
        
        void clearReferenceQueues() {
            if (this.map.usesKeyReferences()) {
                this.clearKeyReferenceQueue();
            }
            if (this.map.usesValueReferences()) {
                this.clearValueReferenceQueue();
            }
        }
        
        boolean clearValue(final K k, final int n, final ValueReference<K, V> valueReference) {
            this.lock();
            try {
                final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                final int n2 = n & table.length() - 1;
                ReferenceEntry<K, V> next;
                final ReferenceEntry referenceEntry = next = (ReferenceEntry<K, V>)(ReferenceEntry)table.get(n2);
                while (next != null) {
                    final K key = next.getKey();
                    if (next.getHash() == n && key != null && this.map.keyEquivalence.equivalent(k, key)) {
                        if (next.getValueReference() == valueReference) {
                            table.set(n2, this.removeFromChain(referenceEntry, next));
                            return true;
                        }
                        return false;
                    }
                    else {
                        next = next.getNext();
                    }
                }
                return false;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
        }
        
        void clearValueReferenceQueue() {
            while (this.valueReferenceQueue.poll() != null) {}
        }
        
        boolean containsKey(Object value, final int n) {
            boolean b = false;
            try {
                if (this.count == 0) {
                    return false;
                }
                final ReferenceEntry<K, Object> liveEntry = this.getLiveEntry(value, n);
                if (liveEntry == null) {
                    return false;
                }
                value = liveEntry.getValueReference().get();
                if (value != null) {
                    b = true;
                }
                return b;
            }
            finally {
                this.postReadCleanup();
            }
        }
        
        ReferenceEntry<K, V> copyEntry(final ReferenceEntry<K, V> referenceEntry, final ReferenceEntry<K, V> referenceEntry2) {
            if (referenceEntry.getKey() != null) {
                final ValueReference<K, V> valueReference = referenceEntry.getValueReference();
                final V value = valueReference.get();
                if (value != null || valueReference.isComputingReference()) {
                    final ReferenceEntry<K, V> copyEntry = this.map.entryFactory.copyEntry(this, referenceEntry, referenceEntry2);
                    copyEntry.setValueReference((ValueReference<K, V>)valueReference.copyFor((ReferenceQueue<V>)this.valueReferenceQueue, value, (ReferenceEntry<K, V>)copyEntry));
                    return copyEntry;
                }
            }
            return null;
        }
        
        void drainKeyReferenceQueue() {
            int n = 0;
            do {
                final Reference<? extends K> poll = this.keyReferenceQueue.poll();
                if (poll == null) {
                    break;
                }
                this.map.reclaimKey((ReferenceEntry<K, V>)poll);
            } while (++n != 16);
        }
        
        void drainRecencyQueue() {
            while (true) {
                final ReferenceEntry referenceEntry = this.recencyQueue.poll();
                if (referenceEntry == null) {
                    break;
                }
                if (this.evictionQueue.contains(referenceEntry)) {
                    this.evictionQueue.add(referenceEntry);
                }
                if (!this.map.expiresAfterAccess() || !this.expirationQueue.contains(referenceEntry)) {
                    continue;
                }
                this.expirationQueue.add(referenceEntry);
            }
        }
        
        void drainReferenceQueues() {
            if (this.map.usesKeyReferences()) {
                this.drainKeyReferenceQueue();
            }
            if (this.map.usesValueReferences()) {
                this.drainValueReferenceQueue();
            }
        }
        
        void drainValueReferenceQueue() {
            int n = 0;
            do {
                final Reference<? extends V> poll = this.valueReferenceQueue.poll();
                if (poll == null) {
                    break;
                }
                this.map.reclaimValue((ValueReference<K, V>)poll);
            } while (++n != 16);
        }
        
        void enqueueNotification(final ReferenceEntry<K, V> referenceEntry, final MapMaker.RemovalCause removalCause) {
            this.enqueueNotification(referenceEntry.getKey(), referenceEntry.getHash(), referenceEntry.getValueReference().get(), removalCause);
        }
        
        void enqueueNotification(final K k, final int n, final V v, final MapMaker.RemovalCause removalCause) {
            if (this.map.removalNotificationQueue != MapMakerInternalMap.DISCARDING_QUEUE) {
                this.map.removalNotificationQueue.offer((MapMaker.RemovalNotification<K, V>)new MapMaker.RemovalNotification(k, v, removalCause));
            }
        }
        
        boolean evictEntries() {
            if (!this.map.evictsBySize() || this.count < this.maxSegmentSize) {
                return false;
            }
            this.drainRecencyQueue();
            final ReferenceEntry referenceEntry = this.evictionQueue.remove();
            if (!this.removeEntry(referenceEntry, referenceEntry.getHash(), MapMaker.RemovalCause.SIZE)) {
                throw new AssertionError();
            }
            return true;
        }
        
        void expand() {
            final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
            final int length = table.length();
            if (length >= 1073741824) {
                return;
            }
            int count = this.count;
            final AtomicReferenceArray<ReferenceEntry<K, V>> entryArray = this.newEntryArray(length << 1);
            this.threshold = entryArray.length() * 3 / 4;
            final int n = entryArray.length() - 1;
            int n2;
            for (int i = 0; i < length; ++i, count = n2) {
                final ReferenceEntry<K, V> referenceEntry = table.get(i);
                n2 = count;
                if (referenceEntry != null) {
                    ReferenceEntry<K, V> referenceEntry2 = referenceEntry.getNext();
                    int n3 = referenceEntry.getHash() & n;
                    if (referenceEntry2 == null) {
                        entryArray.set(n3, referenceEntry);
                        n2 = count;
                    }
                    else {
                        ReferenceEntry<K, V> referenceEntry3 = referenceEntry;
                        while (referenceEntry2 != null) {
                            final int n4 = referenceEntry2.getHash() & n;
                            int n5;
                            if (n4 != (n5 = n3)) {
                                n5 = n4;
                                referenceEntry3 = referenceEntry2;
                            }
                            referenceEntry2 = referenceEntry2.getNext();
                            n3 = n5;
                        }
                        entryArray.set(n3, referenceEntry3);
                        ReferenceEntry<K, V> next = referenceEntry;
                        while (true) {
                            n2 = count;
                            if (next == referenceEntry3) {
                                break;
                            }
                            final int n6 = next.getHash() & n;
                            final ReferenceEntry<K, V> copyEntry = this.copyEntry(next, (ReferenceEntry<K, V>)entryArray.get(n6));
                            if (copyEntry != null) {
                                entryArray.set(n6, copyEntry);
                            }
                            else {
                                this.removeCollectedEntry(next);
                                --count;
                            }
                            next = next.getNext();
                        }
                    }
                }
            }
            this.table = entryArray;
            this.count = count;
        }
        
        void expireEntries() {
            this.drainRecencyQueue();
            if (!this.expirationQueue.isEmpty()) {
                final long read = this.map.ticker.read();
                ReferenceEntry referenceEntry;
                do {
                    referenceEntry = this.expirationQueue.peek();
                    if (referenceEntry != null && this.map.isExpired(referenceEntry, read)) {
                        continue;
                    }
                    return;
                } while (this.removeEntry(referenceEntry, referenceEntry.getHash(), MapMaker.RemovalCause.EXPIRED));
                throw new AssertionError();
            }
        }
        
        V get(final Object o, final int n) {
            try {
                final ReferenceEntry<K, V> liveEntry = this.getLiveEntry(o, n);
                if (liveEntry == null) {
                    return null;
                }
                final V value = liveEntry.getValueReference().get();
                if (value != null) {
                    this.recordRead(liveEntry);
                }
                else {
                    this.tryDrainReferenceQueues();
                }
                return value;
            }
            finally {
                this.postReadCleanup();
            }
        }
        
        ReferenceEntry<K, V> getEntry(final Object o, final int n) {
            if (this.count != 0) {
                for (Object o2 = this.getFirst(n); o2 != null; o2 = ((ReferenceEntry<Object, V>)o2).getNext()) {
                    if (((ReferenceEntry)o2).getHash() == n) {
                        final Object key = ((ReferenceEntry<Object, V>)o2).getKey();
                        if (key == null) {
                            this.tryDrainReferenceQueues();
                        }
                        else if (this.map.keyEquivalence.equivalent(o, key)) {
                            return (ReferenceEntry<K, V>)o2;
                        }
                    }
                }
            }
            return null;
        }
        
        ReferenceEntry<K, V> getFirst(final int n) {
            final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
            return (ReferenceEntry<K, V>)(ReferenceEntry)table.get(table.length() - 1 & n);
        }
        
        ReferenceEntry<K, V> getLiveEntry(final Object o, final int n) {
            final ReferenceEntry<K, V> entry = this.getEntry(o, n);
            ReferenceEntry<K, V> referenceEntry;
            if (entry == null) {
                referenceEntry = null;
            }
            else {
                referenceEntry = entry;
                if (this.map.expires()) {
                    referenceEntry = entry;
                    if (this.map.isExpired(entry)) {
                        this.tryExpireEntries();
                        return null;
                    }
                }
            }
            return referenceEntry;
        }
        
        V getLiveValue(final ReferenceEntry<K, V> referenceEntry) {
            V v;
            if (referenceEntry.getKey() == null) {
                this.tryDrainReferenceQueues();
                v = null;
            }
            else {
                final V value = referenceEntry.getValueReference().get();
                if (value == null) {
                    this.tryDrainReferenceQueues();
                    return null;
                }
                v = value;
                if (this.map.expires()) {
                    v = value;
                    if (this.map.isExpired(referenceEntry)) {
                        this.tryExpireEntries();
                        return null;
                    }
                }
            }
            return v;
        }
        
        void initTable(final AtomicReferenceArray<ReferenceEntry<K, V>> table) {
            this.threshold = table.length() * 3 / 4;
            if (this.threshold == this.maxSegmentSize) {
                ++this.threshold;
            }
            this.table = table;
        }
        
        boolean isCollected(final ValueReference<K, V> valueReference) {
            return !valueReference.isComputingReference() && valueReference.get() == null;
        }
        
        ReferenceEntry<K, V> newEntry(final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
            return this.map.entryFactory.newEntry(this, k, n, referenceEntry);
        }
        
        AtomicReferenceArray<ReferenceEntry<K, V>> newEntryArray(final int n) {
            return new AtomicReferenceArray<ReferenceEntry<K, V>>(n);
        }
        
        void postReadCleanup() {
            if ((this.readCount.incrementAndGet() & 0x3F) == 0x0) {
                this.runCleanup();
            }
        }
        
        void postWriteCleanup() {
            this.runUnlockedCleanup();
        }
        
        void preWriteCleanup() {
            this.runLockedCleanup();
        }
        
        V put(final K k, final int n, final V v, final boolean b) {
            this.lock();
            try {
                this.preWriteCleanup();
                int count;
                if ((count = this.count + 1) > this.threshold) {
                    this.expand();
                    count = this.count + 1;
                }
                final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                final int n2 = n & table.length() - 1;
                Object next;
                final ReferenceEntry<Object, V> referenceEntry = (ReferenceEntry<Object, V>)(next = table.get(n2));
                while (next != null) {
                    final K key = (K)((ReferenceEntry<K, V>)next).getKey();
                    if (((ReferenceEntry)next).getHash() == n && key != null && this.map.keyEquivalence.equivalent(k, key)) {
                        final ValueReference<K, V> valueReference = ((ReferenceEntry<K, V>)next).getValueReference();
                        final V value = valueReference.get();
                        if (value == null) {
                            ++this.modCount;
                            this.setValue((ReferenceEntry<K, V>)next, v);
                            if (!valueReference.isComputingReference()) {
                                this.enqueueNotification(k, n, value, MapMaker.RemovalCause.COLLECTED);
                                count = this.count;
                            }
                            else if (this.evictEntries()) {
                                count = this.count + 1;
                            }
                            this.count = count;
                            return null;
                        }
                        if (b) {
                            this.recordLockedRead((ReferenceEntry<K, V>)next);
                            return value;
                        }
                        ++this.modCount;
                        this.enqueueNotification(k, n, value, MapMaker.RemovalCause.REPLACED);
                        this.setValue((ReferenceEntry<K, V>)next, v);
                        return value;
                    }
                    else {
                        next = ((ReferenceEntry<K, V>)next).getNext();
                    }
                }
                ++this.modCount;
                final ReferenceEntry<Object, V> entry = this.newEntry((Object)k, n, referenceEntry);
                this.setValue((ReferenceEntry<K, V>)entry, v);
                table.set(n2, (ReferenceEntry<K, V>)entry);
                if (this.evictEntries()) {
                    count = this.count + 1;
                }
                this.count = count;
                return null;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
        }
        
        boolean reclaimKey(final ReferenceEntry<K, V> referenceEntry, int count) {
            this.lock();
            try {
                final int count2 = this.count;
                final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                final int n = count & table.length() - 1;
                ReferenceEntry<K, V> next;
                for (ReferenceEntry<K, V> referenceEntry2 = next = table.get(n); next != null; next = next.getNext()) {
                    if (next == referenceEntry) {
                        ++this.modCount;
                        this.enqueueNotification(next.getKey(), count, next.getValueReference().get(), MapMaker.RemovalCause.COLLECTED);
                        final ReferenceEntry<K, V> removeFromChain = this.removeFromChain(referenceEntry2, next);
                        count = this.count;
                        table.set(n, removeFromChain);
                        this.count = count - 1;
                        return true;
                    }
                }
                return false;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
        }
        
        boolean reclaimValue(final K k, int count, final ValueReference<K, V> valueReference) {
            boolean b = false;
            this.lock();
            try {
                final int count2 = this.count;
                final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                final int n = count & table.length() - 1;
                Object next;
                final ReferenceEntry<Object, V> referenceEntry = (ReferenceEntry<Object, V>)(next = table.get(n));
                while (next != null) {
                    final Object key = ((ReferenceEntry<Object, V>)next).getKey();
                    if (((ReferenceEntry)next).getHash() == count && key != null && this.map.keyEquivalence.equivalent(k, key)) {
                        if (((ReferenceEntry<K, V>)next).getValueReference() == valueReference) {
                            ++this.modCount;
                            this.enqueueNotification(k, count, valueReference.get(), MapMaker.RemovalCause.COLLECTED);
                            final ReferenceEntry<Object, V> removeFromChain = this.removeFromChain(referenceEntry, (ReferenceEntry<Object, V>)next);
                            count = this.count;
                            table.set(n, (ReferenceEntry<K, V>)removeFromChain);
                            this.count = count - 1;
                            final boolean b2 = true;
                            this.unlock();
                            b = b2;
                            if (!this.isHeldByCurrentThread()) {
                                this.postWriteCleanup();
                                b = b2;
                            }
                            return b;
                        }
                        return false;
                    }
                    else {
                        next = ((ReferenceEntry<K, V>)next).getNext();
                    }
                }
                return false;
            }
            finally {
                this.unlock();
                if (!this.isHeldByCurrentThread()) {
                    this.postWriteCleanup();
                }
            }
        }
        
        void recordExpirationTime(final ReferenceEntry<K, V> referenceEntry, final long n) {
            referenceEntry.setExpirationTime(this.map.ticker.read() + n);
        }
        
        void recordLockedRead(final ReferenceEntry<K, V> referenceEntry) {
            this.evictionQueue.add(referenceEntry);
            if (this.map.expiresAfterAccess()) {
                this.recordExpirationTime(referenceEntry, this.map.expireAfterAccessNanos);
                this.expirationQueue.add(referenceEntry);
            }
        }
        
        void recordRead(final ReferenceEntry<K, V> referenceEntry) {
            if (this.map.expiresAfterAccess()) {
                this.recordExpirationTime(referenceEntry, this.map.expireAfterAccessNanos);
            }
            this.recencyQueue.add(referenceEntry);
        }
        
        void recordWrite(final ReferenceEntry<K, V> referenceEntry) {
            this.drainRecencyQueue();
            this.evictionQueue.add(referenceEntry);
            if (this.map.expires()) {
                long n;
                if (this.map.expiresAfterAccess()) {
                    n = this.map.expireAfterAccessNanos;
                }
                else {
                    n = this.map.expireAfterWriteNanos;
                }
                this.recordExpirationTime(referenceEntry, n);
                this.expirationQueue.add(referenceEntry);
            }
        }
        
        V remove(final Object o, int count) {
            this.lock();
            try {
                this.preWriteCleanup();
                final int count2 = this.count;
                final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                final int n = count & table.length() - 1;
                Object next;
                for (ReferenceEntry<Object, V> referenceEntry = (ReferenceEntry<Object, V>)(next = table.get(n)); next != null; next = ((ReferenceEntry<Object, V>)next).getNext()) {
                    final K key = ((ReferenceEntry<K, V>)next).getKey();
                    if (((ReferenceEntry)next).getHash() == count && key != null && this.map.keyEquivalence.equivalent(o, key)) {
                        final ValueReference<K, V> valueReference = ((ReferenceEntry<K, V>)next).getValueReference();
                        final V value = valueReference.get();
                        MapMaker.RemovalCause removalCause;
                        if (value != null) {
                            removalCause = MapMaker.RemovalCause.EXPLICIT;
                        }
                        else {
                            if (!this.isCollected(valueReference)) {
                                return null;
                            }
                            removalCause = MapMaker.RemovalCause.COLLECTED;
                        }
                        ++this.modCount;
                        this.enqueueNotification(key, count, value, removalCause);
                        final ReferenceEntry<Object, V> removeFromChain = this.removeFromChain(referenceEntry, (ReferenceEntry<Object, V>)next);
                        count = this.count;
                        table.set(n, (ReferenceEntry<K, V>)removeFromChain);
                        this.count = count - 1;
                        return value;
                    }
                }
                return null;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
        }
        
        boolean remove(final Object o, int count, final Object o2) {
            boolean b = false;
            this.lock();
            try {
                this.preWriteCleanup();
                final int count2 = this.count;
                final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                final int n = count & table.length() - 1;
                Object next;
                for (ReferenceEntry<Object, V> referenceEntry = (ReferenceEntry<Object, V>)(next = table.get(n)); next != null; next = ((ReferenceEntry<Object, V>)next).getNext()) {
                    final K key = ((ReferenceEntry<K, V>)next).getKey();
                    if (((ReferenceEntry)next).getHash() == count && key != null && this.map.keyEquivalence.equivalent(o, key)) {
                        final ValueReference<K, V> valueReference = ((ReferenceEntry<K, V>)next).getValueReference();
                        final V value = valueReference.get();
                        MapMaker.RemovalCause removalCause;
                        if (this.map.valueEquivalence.equivalent(o2, value)) {
                            removalCause = MapMaker.RemovalCause.EXPLICIT;
                        }
                        else {
                            if (!this.isCollected(valueReference)) {
                                return false;
                            }
                            removalCause = MapMaker.RemovalCause.COLLECTED;
                        }
                        ++this.modCount;
                        this.enqueueNotification(key, count, value, removalCause);
                        final ReferenceEntry<Object, V> removeFromChain = this.removeFromChain(referenceEntry, (ReferenceEntry<Object, V>)next);
                        count = this.count;
                        table.set(n, (ReferenceEntry<K, V>)removeFromChain);
                        this.count = count - 1;
                        if (removalCause == MapMaker.RemovalCause.EXPLICIT) {
                            b = true;
                        }
                        return b;
                    }
                }
                return false;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
        }
        
        void removeCollectedEntry(final ReferenceEntry<K, V> referenceEntry) {
            this.enqueueNotification(referenceEntry, MapMaker.RemovalCause.COLLECTED);
            this.evictionQueue.remove(referenceEntry);
            this.expirationQueue.remove(referenceEntry);
        }
        
        boolean removeEntry(final ReferenceEntry<K, V> referenceEntry, int count, final MapMaker.RemovalCause removalCause) {
            final int count2 = this.count;
            final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
            final int n = count & table.length() - 1;
            ReferenceEntry<K, V> next;
            for (ReferenceEntry<K, V> referenceEntry2 = next = table.get(n); next != null; next = next.getNext()) {
                if (next == referenceEntry) {
                    ++this.modCount;
                    this.enqueueNotification(next.getKey(), count, next.getValueReference().get(), removalCause);
                    final ReferenceEntry<K, V> removeFromChain = this.removeFromChain(referenceEntry2, next);
                    count = this.count;
                    table.set(n, removeFromChain);
                    this.count = count - 1;
                    return true;
                }
            }
            return false;
        }
        
        ReferenceEntry<K, V> removeFromChain(ReferenceEntry<K, V> next, final ReferenceEntry<K, V> referenceEntry) {
            this.evictionQueue.remove(referenceEntry);
            this.expirationQueue.remove(referenceEntry);
            int count = this.count;
            Object next2 = referenceEntry.getNext();
            while (next != referenceEntry) {
                final ReferenceEntry<K, V> copyEntry = this.copyEntry(next, (ReferenceEntry<K, V>)next2);
                if (copyEntry != null) {
                    next2 = copyEntry;
                }
                else {
                    this.removeCollectedEntry(next);
                    --count;
                }
                next = next.getNext();
            }
            this.count = count;
            return (ReferenceEntry<K, V>)next2;
        }
        
        V replace(final K k, int count, final V v) {
            this.lock();
            try {
                this.preWriteCleanup();
                final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                final int n = count & table.length() - 1;
                Object next;
                final ReferenceEntry<Object, V> referenceEntry = (ReferenceEntry<Object, V>)(next = table.get(n));
                while (next != null) {
                    final K key = ((ReferenceEntry<K, V>)next).getKey();
                    if (((ReferenceEntry)next).getHash() == count && key != null && this.map.keyEquivalence.equivalent(k, key)) {
                        final ValueReference<K, V> valueReference = ((ReferenceEntry<K, V>)next).getValueReference();
                        final V value = valueReference.get();
                        if (value == null) {
                            if (this.isCollected(valueReference)) {
                                final int count2 = this.count;
                                ++this.modCount;
                                this.enqueueNotification(key, count, value, MapMaker.RemovalCause.COLLECTED);
                                final ReferenceEntry<Object, V> removeFromChain = this.removeFromChain(referenceEntry, (ReferenceEntry<Object, V>)next);
                                count = this.count;
                                table.set(n, (ReferenceEntry<K, V>)removeFromChain);
                                this.count = count - 1;
                            }
                            return null;
                        }
                        ++this.modCount;
                        this.enqueueNotification(k, count, value, MapMaker.RemovalCause.REPLACED);
                        this.setValue((ReferenceEntry<K, V>)next, v);
                        return value;
                    }
                    else {
                        next = ((ReferenceEntry<K, V>)next).getNext();
                    }
                }
                return null;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
        }
        
        boolean replace(final K k, int count, final V v, final V v2) {
            this.lock();
            try {
                this.preWriteCleanup();
                final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                final int n = count & table.length() - 1;
                Object next;
                final ReferenceEntry<Object, V> referenceEntry = (ReferenceEntry<Object, V>)(next = table.get(n));
                while (next != null) {
                    final K key = ((ReferenceEntry<K, V>)next).getKey();
                    if (((ReferenceEntry)next).getHash() == count && key != null && this.map.keyEquivalence.equivalent(k, key)) {
                        final ValueReference<K, V> valueReference = ((ReferenceEntry<K, V>)next).getValueReference();
                        final V value = valueReference.get();
                        if (value == null) {
                            if (this.isCollected(valueReference)) {
                                final int count2 = this.count;
                                ++this.modCount;
                                this.enqueueNotification(key, count, value, MapMaker.RemovalCause.COLLECTED);
                                final ReferenceEntry<Object, V> removeFromChain = this.removeFromChain(referenceEntry, (ReferenceEntry<Object, V>)next);
                                count = this.count;
                                table.set(n, (ReferenceEntry<K, V>)removeFromChain);
                                this.count = count - 1;
                            }
                            return false;
                        }
                        if (this.map.valueEquivalence.equivalent(v, value)) {
                            ++this.modCount;
                            this.enqueueNotification(k, count, value, MapMaker.RemovalCause.REPLACED);
                            this.setValue((ReferenceEntry<K, V>)next, v2);
                            return true;
                        }
                        this.recordLockedRead((ReferenceEntry<K, V>)next);
                        return false;
                    }
                    else {
                        next = ((ReferenceEntry<K, V>)next).getNext();
                    }
                }
                return false;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
        }
        
        void runCleanup() {
            this.runLockedCleanup();
            this.runUnlockedCleanup();
        }
        
        void runLockedCleanup() {
            if (!this.tryLock()) {
                return;
            }
            try {
                this.drainReferenceQueues();
                this.expireEntries();
                this.readCount.set(0);
            }
            finally {
                this.unlock();
            }
        }
        
        void runUnlockedCleanup() {
            if (!this.isHeldByCurrentThread()) {
                this.map.processPendingNotifications();
            }
        }
        
        void setValue(final ReferenceEntry<K, V> referenceEntry, final V v) {
            referenceEntry.setValueReference(this.map.valueStrength.referenceValue(this, referenceEntry, v));
            this.recordWrite(referenceEntry);
        }
        
        void tryDrainReferenceQueues() {
            if (!this.tryLock()) {
                return;
            }
            try {
                this.drainReferenceQueues();
            }
            finally {
                this.unlock();
            }
        }
        
        void tryExpireEntries() {
            if (!this.tryLock()) {
                return;
            }
            try {
                this.expireEntries();
            }
            finally {
                this.unlock();
            }
        }
    }
    
    static class SoftEntry<K, V> extends SoftReference<K> implements ReferenceEntry<K, V>
    {
        final int hash;
        final ReferenceEntry<K, V> next;
        volatile ValueReference<K, V> valueReference;
        
        SoftEntry(final ReferenceQueue<K> referenceQueue, final K k, final int hash, final ReferenceEntry<K, V> next) {
            super(k, referenceQueue);
            this.valueReference = MapMakerInternalMap.unset();
            this.hash = hash;
            this.next = next;
        }
        
        @Override
        public long getExpirationTime() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int getHash() {
            return this.hash;
        }
        
        @Override
        public K getKey() {
            return this.get();
        }
        
        @Override
        public ReferenceEntry<K, V> getNext() {
            return this.next;
        }
        
        @Override
        public ReferenceEntry<K, V> getNextEvictable() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getNextExpirable() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousEvictable() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousExpirable() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }
        
        @Override
        public void setExpirationTime(final long n) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setNextEvictable(final ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setNextExpirable(final ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setPreviousEvictable(final ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setPreviousExpirable(final ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setValueReference(final ValueReference<K, V> valueReference) {
            this.valueReference.clear(this.valueReference = valueReference);
        }
    }
    
    static final class SoftEvictableEntry<K, V> extends SoftEntry<K, V> implements ReferenceEntry<K, V>
    {
        ReferenceEntry<K, V> nextEvictable;
        ReferenceEntry<K, V> previousEvictable;
        
        SoftEvictableEntry(final ReferenceQueue<K> referenceQueue, final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
            super(referenceQueue, k, n, referenceEntry);
            this.nextEvictable = MapMakerInternalMap.nullEntry();
            this.previousEvictable = MapMakerInternalMap.nullEntry();
        }
        
        @Override
        public ReferenceEntry<K, V> getNextEvictable() {
            return this.nextEvictable;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousEvictable() {
            return this.previousEvictable;
        }
        
        @Override
        public void setNextEvictable(final ReferenceEntry<K, V> nextEvictable) {
            this.nextEvictable = nextEvictable;
        }
        
        @Override
        public void setPreviousEvictable(final ReferenceEntry<K, V> previousEvictable) {
            this.previousEvictable = previousEvictable;
        }
    }
    
    static final class SoftExpirableEntry<K, V> extends SoftEntry<K, V> implements ReferenceEntry<K, V>
    {
        ReferenceEntry<K, V> nextExpirable;
        ReferenceEntry<K, V> previousExpirable;
        volatile long time;
        
        SoftExpirableEntry(final ReferenceQueue<K> referenceQueue, final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
            super(referenceQueue, k, n, referenceEntry);
            this.time = Long.MAX_VALUE;
            this.nextExpirable = MapMakerInternalMap.nullEntry();
            this.previousExpirable = MapMakerInternalMap.nullEntry();
        }
        
        @Override
        public long getExpirationTime() {
            return this.time;
        }
        
        @Override
        public ReferenceEntry<K, V> getNextExpirable() {
            return this.nextExpirable;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousExpirable() {
            return this.previousExpirable;
        }
        
        @Override
        public void setExpirationTime(final long time) {
            this.time = time;
        }
        
        @Override
        public void setNextExpirable(final ReferenceEntry<K, V> nextExpirable) {
            this.nextExpirable = nextExpirable;
        }
        
        @Override
        public void setPreviousExpirable(final ReferenceEntry<K, V> previousExpirable) {
            this.previousExpirable = previousExpirable;
        }
    }
    
    static final class SoftExpirableEvictableEntry<K, V> extends SoftEntry<K, V> implements ReferenceEntry<K, V>
    {
        ReferenceEntry<K, V> nextEvictable;
        ReferenceEntry<K, V> nextExpirable;
        ReferenceEntry<K, V> previousEvictable;
        ReferenceEntry<K, V> previousExpirable;
        volatile long time;
        
        SoftExpirableEvictableEntry(final ReferenceQueue<K> referenceQueue, final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
            super(referenceQueue, k, n, referenceEntry);
            this.time = Long.MAX_VALUE;
            this.nextExpirable = MapMakerInternalMap.nullEntry();
            this.previousExpirable = MapMakerInternalMap.nullEntry();
            this.nextEvictable = MapMakerInternalMap.nullEntry();
            this.previousEvictable = MapMakerInternalMap.nullEntry();
        }
        
        @Override
        public long getExpirationTime() {
            return this.time;
        }
        
        @Override
        public ReferenceEntry<K, V> getNextEvictable() {
            return this.nextEvictable;
        }
        
        @Override
        public ReferenceEntry<K, V> getNextExpirable() {
            return this.nextExpirable;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousEvictable() {
            return this.previousEvictable;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousExpirable() {
            return this.previousExpirable;
        }
        
        @Override
        public void setExpirationTime(final long time) {
            this.time = time;
        }
        
        @Override
        public void setNextEvictable(final ReferenceEntry<K, V> nextEvictable) {
            this.nextEvictable = nextEvictable;
        }
        
        @Override
        public void setNextExpirable(final ReferenceEntry<K, V> nextExpirable) {
            this.nextExpirable = nextExpirable;
        }
        
        @Override
        public void setPreviousEvictable(final ReferenceEntry<K, V> previousEvictable) {
            this.previousEvictable = previousEvictable;
        }
        
        @Override
        public void setPreviousExpirable(final ReferenceEntry<K, V> previousExpirable) {
            this.previousExpirable = previousExpirable;
        }
    }
    
    static final class SoftValueReference<K, V> extends SoftReference<V> implements ValueReference<K, V>
    {
        final ReferenceEntry<K, V> entry;
        
        SoftValueReference(final ReferenceQueue<V> referenceQueue, final V v, final ReferenceEntry<K, V> entry) {
            super(v, referenceQueue);
            this.entry = entry;
        }
        
        @Override
        public void clear(final ValueReference<K, V> valueReference) {
            this.clear();
        }
        
        @Override
        public ValueReference<K, V> copyFor(final ReferenceQueue<V> referenceQueue, final V v, final ReferenceEntry<K, V> referenceEntry) {
            return new SoftValueReference((ReferenceQueue<Object>)referenceQueue, v, (ReferenceEntry<Object, Object>)referenceEntry);
        }
        
        @Override
        public ReferenceEntry<K, V> getEntry() {
            return this.entry;
        }
        
        @Override
        public boolean isComputingReference() {
            return false;
        }
        
        @Override
        public V waitForValue() {
            return this.get();
        }
    }
    
    enum Strength
    {
        SOFT {
            @Override
            Equivalence<Object> defaultEquivalence() {
                return Equivalence.identity();
            }
            
            @Override
             <K, V> ValueReference<K, V> referenceValue(final Segment<K, V> segment, final ReferenceEntry<K, V> referenceEntry, final V v) {
                return new SoftValueReference<K, V>(segment.valueReferenceQueue, v, referenceEntry);
            }
        }, 
        STRONG {
            @Override
            Equivalence<Object> defaultEquivalence() {
                return Equivalence.equals();
            }
            
            @Override
             <K, V> ValueReference<K, V> referenceValue(final Segment<K, V> segment, final ReferenceEntry<K, V> referenceEntry, final V v) {
                return new StrongValueReference<K, V>(v);
            }
        }, 
        WEAK {
            @Override
            Equivalence<Object> defaultEquivalence() {
                return Equivalence.identity();
            }
            
            @Override
             <K, V> ValueReference<K, V> referenceValue(final Segment<K, V> segment, final ReferenceEntry<K, V> referenceEntry, final V v) {
                return new WeakValueReference<K, V>(segment.valueReferenceQueue, v, referenceEntry);
            }
        };
        
        abstract Equivalence<Object> defaultEquivalence();
        
        abstract <K, V> ValueReference<K, V> referenceValue(final Segment<K, V> p0, final ReferenceEntry<K, V> p1, final V p2);
    }
    
    static class StrongEntry<K, V> implements ReferenceEntry<K, V>
    {
        final int hash;
        final K key;
        final ReferenceEntry<K, V> next;
        volatile ValueReference<K, V> valueReference;
        
        StrongEntry(final K key, final int hash, final ReferenceEntry<K, V> next) {
            this.valueReference = MapMakerInternalMap.unset();
            this.key = key;
            this.hash = hash;
            this.next = next;
        }
        
        @Override
        public long getExpirationTime() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int getHash() {
            return this.hash;
        }
        
        @Override
        public K getKey() {
            return this.key;
        }
        
        @Override
        public ReferenceEntry<K, V> getNext() {
            return this.next;
        }
        
        @Override
        public ReferenceEntry<K, V> getNextEvictable() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getNextExpirable() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousEvictable() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousExpirable() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }
        
        @Override
        public void setExpirationTime(final long n) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setNextEvictable(final ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setNextExpirable(final ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setPreviousEvictable(final ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setPreviousExpirable(final ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setValueReference(final ValueReference<K, V> valueReference) {
            this.valueReference.clear(this.valueReference = valueReference);
        }
    }
    
    static final class StrongEvictableEntry<K, V> extends StrongEntry<K, V> implements ReferenceEntry<K, V>
    {
        ReferenceEntry<K, V> nextEvictable;
        ReferenceEntry<K, V> previousEvictable;
        
        StrongEvictableEntry(final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
            super(k, n, referenceEntry);
            this.nextEvictable = MapMakerInternalMap.nullEntry();
            this.previousEvictable = MapMakerInternalMap.nullEntry();
        }
        
        @Override
        public ReferenceEntry<K, V> getNextEvictable() {
            return this.nextEvictable;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousEvictable() {
            return this.previousEvictable;
        }
        
        @Override
        public void setNextEvictable(final ReferenceEntry<K, V> nextEvictable) {
            this.nextEvictable = nextEvictable;
        }
        
        @Override
        public void setPreviousEvictable(final ReferenceEntry<K, V> previousEvictable) {
            this.previousEvictable = previousEvictable;
        }
    }
    
    static final class StrongExpirableEntry<K, V> extends StrongEntry<K, V> implements ReferenceEntry<K, V>
    {
        ReferenceEntry<K, V> nextExpirable;
        ReferenceEntry<K, V> previousExpirable;
        volatile long time;
        
        StrongExpirableEntry(final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
            super(k, n, referenceEntry);
            this.time = Long.MAX_VALUE;
            this.nextExpirable = MapMakerInternalMap.nullEntry();
            this.previousExpirable = MapMakerInternalMap.nullEntry();
        }
        
        @Override
        public long getExpirationTime() {
            return this.time;
        }
        
        @Override
        public ReferenceEntry<K, V> getNextExpirable() {
            return this.nextExpirable;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousExpirable() {
            return this.previousExpirable;
        }
        
        @Override
        public void setExpirationTime(final long time) {
            this.time = time;
        }
        
        @Override
        public void setNextExpirable(final ReferenceEntry<K, V> nextExpirable) {
            this.nextExpirable = nextExpirable;
        }
        
        @Override
        public void setPreviousExpirable(final ReferenceEntry<K, V> previousExpirable) {
            this.previousExpirable = previousExpirable;
        }
    }
    
    static final class StrongExpirableEvictableEntry<K, V> extends StrongEntry<K, V> implements ReferenceEntry<K, V>
    {
        ReferenceEntry<K, V> nextEvictable;
        ReferenceEntry<K, V> nextExpirable;
        ReferenceEntry<K, V> previousEvictable;
        ReferenceEntry<K, V> previousExpirable;
        volatile long time;
        
        StrongExpirableEvictableEntry(final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
            super(k, n, referenceEntry);
            this.time = Long.MAX_VALUE;
            this.nextExpirable = MapMakerInternalMap.nullEntry();
            this.previousExpirable = MapMakerInternalMap.nullEntry();
            this.nextEvictable = MapMakerInternalMap.nullEntry();
            this.previousEvictable = MapMakerInternalMap.nullEntry();
        }
        
        @Override
        public long getExpirationTime() {
            return this.time;
        }
        
        @Override
        public ReferenceEntry<K, V> getNextEvictable() {
            return this.nextEvictable;
        }
        
        @Override
        public ReferenceEntry<K, V> getNextExpirable() {
            return this.nextExpirable;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousEvictable() {
            return this.previousEvictable;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousExpirable() {
            return this.previousExpirable;
        }
        
        @Override
        public void setExpirationTime(final long time) {
            this.time = time;
        }
        
        @Override
        public void setNextEvictable(final ReferenceEntry<K, V> nextEvictable) {
            this.nextEvictable = nextEvictable;
        }
        
        @Override
        public void setNextExpirable(final ReferenceEntry<K, V> nextExpirable) {
            this.nextExpirable = nextExpirable;
        }
        
        @Override
        public void setPreviousEvictable(final ReferenceEntry<K, V> previousEvictable) {
            this.previousEvictable = previousEvictable;
        }
        
        @Override
        public void setPreviousExpirable(final ReferenceEntry<K, V> previousExpirable) {
            this.previousExpirable = previousExpirable;
        }
    }
    
    static final class StrongValueReference<K, V> implements ValueReference<K, V>
    {
        final V referent;
        
        StrongValueReference(final V referent) {
            this.referent = referent;
        }
        
        @Override
        public void clear(final ValueReference<K, V> valueReference) {
        }
        
        @Override
        public ValueReference<K, V> copyFor(final ReferenceQueue<V> referenceQueue, final V v, final ReferenceEntry<K, V> referenceEntry) {
            return this;
        }
        
        @Override
        public V get() {
            return this.referent;
        }
        
        @Override
        public ReferenceEntry<K, V> getEntry() {
            return null;
        }
        
        @Override
        public boolean isComputingReference() {
            return false;
        }
        
        @Override
        public V waitForValue() {
            return this.get();
        }
    }
    
    final class ValueIterator extends MapMakerInternalMap$HashIterator implements Iterator<V>
    {
        public V next() {
            return this.nextEntry().getValue();
        }
    }
    
    interface ValueReference<K, V>
    {
        void clear(final ValueReference<K, V> p0);
        
        ValueReference<K, V> copyFor(final ReferenceQueue<V> p0, final V p1, final ReferenceEntry<K, V> p2);
        
        V get();
        
        ReferenceEntry<K, V> getEntry();
        
        boolean isComputingReference();
        
        V waitForValue() throws ExecutionException;
    }
    
    final class Values extends AbstractCollection<V>
    {
        @Override
        public void clear() {
            MapMakerInternalMap.this.clear();
        }
        
        @Override
        public boolean contains(final Object o) {
            return MapMakerInternalMap.this.containsValue(o);
        }
        
        @Override
        public boolean isEmpty() {
            return MapMakerInternalMap.this.isEmpty();
        }
        
        @Override
        public Iterator<V> iterator() {
            return new ValueIterator();
        }
        
        @Override
        public int size() {
            return MapMakerInternalMap.this.size();
        }
    }
    
    static class WeakEntry<K, V> extends WeakReference<K> implements ReferenceEntry<K, V>
    {
        final int hash;
        final ReferenceEntry<K, V> next;
        volatile ValueReference<K, V> valueReference;
        
        WeakEntry(final ReferenceQueue<K> referenceQueue, final K k, final int hash, final ReferenceEntry<K, V> next) {
            super(k, referenceQueue);
            this.valueReference = MapMakerInternalMap.unset();
            this.hash = hash;
            this.next = next;
        }
        
        @Override
        public long getExpirationTime() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int getHash() {
            return this.hash;
        }
        
        @Override
        public K getKey() {
            return this.get();
        }
        
        @Override
        public ReferenceEntry<K, V> getNext() {
            return this.next;
        }
        
        @Override
        public ReferenceEntry<K, V> getNextEvictable() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getNextExpirable() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousEvictable() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousExpirable() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }
        
        @Override
        public void setExpirationTime(final long n) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setNextEvictable(final ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setNextExpirable(final ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setPreviousEvictable(final ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setPreviousExpirable(final ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setValueReference(final ValueReference<K, V> valueReference) {
            this.valueReference.clear(this.valueReference = valueReference);
        }
    }
    
    static final class WeakEvictableEntry<K, V> extends WeakEntry<K, V> implements ReferenceEntry<K, V>
    {
        ReferenceEntry<K, V> nextEvictable;
        ReferenceEntry<K, V> previousEvictable;
        
        WeakEvictableEntry(final ReferenceQueue<K> referenceQueue, final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
            super(referenceQueue, k, n, referenceEntry);
            this.nextEvictable = MapMakerInternalMap.nullEntry();
            this.previousEvictable = MapMakerInternalMap.nullEntry();
        }
        
        @Override
        public ReferenceEntry<K, V> getNextEvictable() {
            return this.nextEvictable;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousEvictable() {
            return this.previousEvictable;
        }
        
        @Override
        public void setNextEvictable(final ReferenceEntry<K, V> nextEvictable) {
            this.nextEvictable = nextEvictable;
        }
        
        @Override
        public void setPreviousEvictable(final ReferenceEntry<K, V> previousEvictable) {
            this.previousEvictable = previousEvictable;
        }
    }
    
    static final class WeakExpirableEntry<K, V> extends WeakEntry<K, V> implements ReferenceEntry<K, V>
    {
        ReferenceEntry<K, V> nextExpirable;
        ReferenceEntry<K, V> previousExpirable;
        volatile long time;
        
        WeakExpirableEntry(final ReferenceQueue<K> referenceQueue, final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
            super(referenceQueue, k, n, referenceEntry);
            this.time = Long.MAX_VALUE;
            this.nextExpirable = MapMakerInternalMap.nullEntry();
            this.previousExpirable = MapMakerInternalMap.nullEntry();
        }
        
        @Override
        public long getExpirationTime() {
            return this.time;
        }
        
        @Override
        public ReferenceEntry<K, V> getNextExpirable() {
            return this.nextExpirable;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousExpirable() {
            return this.previousExpirable;
        }
        
        @Override
        public void setExpirationTime(final long time) {
            this.time = time;
        }
        
        @Override
        public void setNextExpirable(final ReferenceEntry<K, V> nextExpirable) {
            this.nextExpirable = nextExpirable;
        }
        
        @Override
        public void setPreviousExpirable(final ReferenceEntry<K, V> previousExpirable) {
            this.previousExpirable = previousExpirable;
        }
    }
    
    static final class WeakExpirableEvictableEntry<K, V> extends WeakEntry<K, V> implements ReferenceEntry<K, V>
    {
        ReferenceEntry<K, V> nextEvictable;
        ReferenceEntry<K, V> nextExpirable;
        ReferenceEntry<K, V> previousEvictable;
        ReferenceEntry<K, V> previousExpirable;
        volatile long time;
        
        WeakExpirableEvictableEntry(final ReferenceQueue<K> referenceQueue, final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
            super(referenceQueue, k, n, referenceEntry);
            this.time = Long.MAX_VALUE;
            this.nextExpirable = MapMakerInternalMap.nullEntry();
            this.previousExpirable = MapMakerInternalMap.nullEntry();
            this.nextEvictable = MapMakerInternalMap.nullEntry();
            this.previousEvictable = MapMakerInternalMap.nullEntry();
        }
        
        @Override
        public long getExpirationTime() {
            return this.time;
        }
        
        @Override
        public ReferenceEntry<K, V> getNextEvictable() {
            return this.nextEvictable;
        }
        
        @Override
        public ReferenceEntry<K, V> getNextExpirable() {
            return this.nextExpirable;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousEvictable() {
            return this.previousEvictable;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousExpirable() {
            return this.previousExpirable;
        }
        
        @Override
        public void setExpirationTime(final long time) {
            this.time = time;
        }
        
        @Override
        public void setNextEvictable(final ReferenceEntry<K, V> nextEvictable) {
            this.nextEvictable = nextEvictable;
        }
        
        @Override
        public void setNextExpirable(final ReferenceEntry<K, V> nextExpirable) {
            this.nextExpirable = nextExpirable;
        }
        
        @Override
        public void setPreviousEvictable(final ReferenceEntry<K, V> previousEvictable) {
            this.previousEvictable = previousEvictable;
        }
        
        @Override
        public void setPreviousExpirable(final ReferenceEntry<K, V> previousExpirable) {
            this.previousExpirable = previousExpirable;
        }
    }
    
    static final class WeakValueReference<K, V> extends WeakReference<V> implements ValueReference<K, V>
    {
        final ReferenceEntry<K, V> entry;
        
        WeakValueReference(final ReferenceQueue<V> referenceQueue, final V v, final ReferenceEntry<K, V> entry) {
            super(v, referenceQueue);
            this.entry = entry;
        }
        
        @Override
        public void clear(final ValueReference<K, V> valueReference) {
            this.clear();
        }
        
        @Override
        public ValueReference<K, V> copyFor(final ReferenceQueue<V> referenceQueue, final V v, final ReferenceEntry<K, V> referenceEntry) {
            return new WeakValueReference((ReferenceQueue<Object>)referenceQueue, v, (ReferenceEntry<Object, Object>)referenceEntry);
        }
        
        @Override
        public ReferenceEntry<K, V> getEntry() {
            return this.entry;
        }
        
        @Override
        public boolean isComputingReference() {
            return false;
        }
        
        @Override
        public V waitForValue() {
            return this.get();
        }
    }
    
    final class WriteThroughEntry extends AbstractMapEntry<K, V>
    {
        final K key;
        V value;
        
        WriteThroughEntry(final K key, final V value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public boolean equals(final Object o) {
            boolean b2;
            final boolean b = b2 = false;
            if (o instanceof Entry) {
                final Entry entry = (Entry)o;
                b2 = b;
                if (this.key.equals(entry.getKey())) {
                    b2 = b;
                    if (this.value.equals(entry.getValue())) {
                        b2 = true;
                    }
                }
            }
            return b2;
        }
        
        @Override
        public K getKey() {
            return this.key;
        }
        
        @Override
        public V getValue() {
            return this.value;
        }
        
        @Override
        public int hashCode() {
            return this.key.hashCode() ^ this.value.hashCode();
        }
        
        @Override
        public V setValue(final V value) {
            final Object put = MapMakerInternalMap.this.put(this.key, value);
            this.value = value;
            return (V)put;
        }
    }
}
