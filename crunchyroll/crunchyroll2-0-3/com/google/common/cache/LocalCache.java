// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.cache;

import java.lang.ref.WeakReference;
import java.util.AbstractCollection;
import java.lang.ref.SoftReference;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import com.google.common.util.concurrent.Uninterruptibles;
import java.lang.ref.Reference;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.io.Serializable;
import com.google.common.util.concurrent.Futures;
import java.util.concurrent.TimeUnit;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.SettableFuture;
import java.util.NoSuchElementException;
import com.google.common.cache.LocalCache$com.google.common.cache.LocalCache$com.google.common.cache.LocalCache$WriteThroughEntry;
import java.util.AbstractSet;
import com.google.common.cache.LocalCache$com.google.common.cache.LocalCache$HashIterator;
import com.google.common.collect.AbstractSequentialIterator;
import com.google.common.primitives.Ints;
import java.util.logging.Level;
import com.google.common.base.Preconditions;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.ConcurrentLinkedQueue;
import com.google.common.collect.Iterators;
import java.util.Iterator;
import java.util.AbstractQueue;
import java.lang.ref.ReferenceQueue;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.Collection;
import com.google.common.base.Ticker;
import com.google.common.base.Equivalence;
import java.util.Map;
import java.util.Set;
import com.google.common.util.concurrent.ListeningExecutorService;
import java.util.logging.Logger;
import java.util.Queue;
import java.util.concurrent.ConcurrentMap;
import java.util.AbstractMap;

class LocalCache<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V>
{
    static final Queue<?> DISCARDING_QUEUE;
    static final ValueReference<Object, Object> UNSET;
    static final Logger logger;
    static final ListeningExecutorService sameThreadExecutor;
    final int concurrencyLevel;
    final CacheLoader<? super K, V> defaultLoader;
    final EntryFactory entryFactory;
    Set<Entry<K, V>> entrySet;
    final long expireAfterAccessNanos;
    final long expireAfterWriteNanos;
    final AbstractCache.StatsCounter globalStatsCounter;
    final Equivalence<Object> keyEquivalence;
    Set<K> keySet;
    final Strength keyStrength;
    final long maxWeight;
    final long refreshNanos;
    final RemovalListener<K, V> removalListener;
    final Queue<RemovalNotification<K, V>> removalNotificationQueue;
    final int segmentMask;
    final int segmentShift;
    final Segment<K, V>[] segments;
    final Ticker ticker;
    final Equivalence<Object> valueEquivalence;
    final Strength valueStrength;
    Collection<V> values;
    final Weigher<K, V> weigher;
    
    static {
        logger = Logger.getLogger(LocalCache.class.getName());
        sameThreadExecutor = MoreExecutors.sameThreadExecutor();
        UNSET = (ValueReference)new ValueReference<Object, Object>() {
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
            public int getWeight() {
                return 0;
            }
            
            @Override
            public boolean isActive() {
                return false;
            }
            
            @Override
            public boolean isLoading() {
                return false;
            }
            
            @Override
            public void notifyNewValue(final Object o) {
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
    
    LocalCache(final CacheBuilder<? super K, ? super V> cacheBuilder, final CacheLoader<? super K, V> defaultLoader) {
        this.concurrencyLevel = Math.min(cacheBuilder.getConcurrencyLevel(), 65536);
        this.keyStrength = cacheBuilder.getKeyStrength();
        this.valueStrength = cacheBuilder.getValueStrength();
        this.keyEquivalence = cacheBuilder.getKeyEquivalence();
        this.valueEquivalence = cacheBuilder.getValueEquivalence();
        this.maxWeight = cacheBuilder.getMaximumWeight();
        this.weigher = cacheBuilder.getWeigher();
        this.expireAfterAccessNanos = cacheBuilder.getExpireAfterAccessNanos();
        this.expireAfterWriteNanos = cacheBuilder.getExpireAfterWriteNanos();
        this.refreshNanos = cacheBuilder.getRefreshNanos();
        this.removalListener = cacheBuilder.getRemovalListener();
        Queue<RemovalNotification<K, V>> discardingQueue;
        if (this.removalListener == CacheBuilder.NullListener.INSTANCE) {
            discardingQueue = discardingQueue();
        }
        else {
            discardingQueue = new ConcurrentLinkedQueue<RemovalNotification<K, V>>();
        }
        this.removalNotificationQueue = discardingQueue;
        this.ticker = cacheBuilder.getTicker(this.recordsTime());
        this.entryFactory = EntryFactory.getFactory(this.keyStrength, this.usesAccessEntries(), this.usesWriteEntries());
        this.globalStatsCounter = (AbstractCache.StatsCounter)cacheBuilder.getStatsCounterSupplier().get();
        this.defaultLoader = defaultLoader;
        int n2;
        final int n = n2 = Math.min(cacheBuilder.getInitialCapacity(), 1073741824);
        if (this.evictsBySize()) {
            n2 = n;
            if (!this.customWeigher()) {
                n2 = Math.min(n, (int)this.maxWeight);
            }
        }
        int n3 = 0;
        int n4;
        for (n4 = 1; n4 < this.concurrencyLevel && (!this.evictsBySize() || n4 * 20 <= this.maxWeight); n4 <<= 1) {
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
            long n7 = this.maxWeight / n4 + 1L;
            final long maxWeight = this.maxWeight;
            final long n8 = n4;
            long n9;
            for (int j = 0; j < this.segments.length; ++j, n7 = n9) {
                n9 = n7;
                if (j == maxWeight % n8) {
                    n9 = n7 - 1L;
                }
                this.segments[j] = this.createSegment(i, n9, (AbstractCache.StatsCounter)cacheBuilder.getStatsCounterSupplier().get());
            }
        }
        else {
            for (int k = 0; k < this.segments.length; ++k) {
                this.segments[k] = this.createSegment(i, -1L, (AbstractCache.StatsCounter)cacheBuilder.getStatsCounterSupplier().get());
            }
        }
    }
    
    static <K, V> void connectAccessOrder(final ReferenceEntry<K, V> previousInAccessQueue, final ReferenceEntry<K, V> nextInAccessQueue) {
        previousInAccessQueue.setNextInAccessQueue(nextInAccessQueue);
        nextInAccessQueue.setPreviousInAccessQueue(previousInAccessQueue);
    }
    
    static <K, V> void connectWriteOrder(final ReferenceEntry<K, V> previousInWriteQueue, final ReferenceEntry<K, V> nextInWriteQueue) {
        previousInWriteQueue.setNextInWriteQueue(nextInWriteQueue);
        nextInWriteQueue.setPreviousInWriteQueue(previousInWriteQueue);
    }
    
    static <E> Queue<E> discardingQueue() {
        return (Queue<E>)LocalCache.DISCARDING_QUEUE;
    }
    
    static <K, V> ReferenceEntry<K, V> nullEntry() {
        return (ReferenceEntry<K, V>)NullEntry.INSTANCE;
    }
    
    static <K, V> void nullifyAccessOrder(final ReferenceEntry<K, V> referenceEntry) {
        final ReferenceEntry<K, V> nullEntry = nullEntry();
        referenceEntry.setNextInAccessQueue(nullEntry);
        referenceEntry.setPreviousInAccessQueue(nullEntry);
    }
    
    static <K, V> void nullifyWriteOrder(final ReferenceEntry<K, V> referenceEntry) {
        final ReferenceEntry<K, V> nullEntry = nullEntry();
        referenceEntry.setNextInWriteQueue(nullEntry);
        referenceEntry.setPreviousInWriteQueue(nullEntry);
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
        return (ValueReference<K, V>)LocalCache.UNSET;
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
        final long read = this.ticker.read();
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
                        final V liveValue = segment.getLiveValue(next, read);
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
    
    Segment<K, V> createSegment(final int n, final long n2, final AbstractCache.StatsCounter statsCounter) {
        return new Segment<K, V>(this, n, n2, statsCounter);
    }
    
    boolean customWeigher() {
        return this.weigher != CacheBuilder.OneWeigher.INSTANCE;
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
        return this.maxWeight >= 0L;
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
    
    public V getIfPresent(Object value) {
        final int hash = this.hash(Preconditions.checkNotNull(value));
        value = this.segmentFor(hash).get(value, hash);
        if (value == null) {
            this.globalStatsCounter.recordMisses(1);
            return (V)value;
        }
        this.globalStatsCounter.recordHits(1);
        return (V)value;
    }
    
    V getLiveValue(final ReferenceEntry<K, V> referenceEntry, final long n) {
        V value;
        if (referenceEntry.getKey() == null) {
            value = null;
        }
        else {
            value = referenceEntry.getValueReference().get();
            if (value == null) {
                return null;
            }
            if (this.isExpired(referenceEntry, n)) {
                return null;
            }
        }
        return value;
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
    
    boolean isExpired(final ReferenceEntry<K, V> referenceEntry, final long n) {
        return (this.expiresAfterAccess() && n - referenceEntry.getAccessTime() > this.expireAfterAccessNanos) || (this.expiresAfterWrite() && n - referenceEntry.getWriteTime() > this.expireAfterWriteNanos);
    }
    
    @Override
    public Set<K> keySet() {
        final Set<K> keySet = this.keySet;
        if (keySet != null) {
            return keySet;
        }
        return this.keySet = new KeySet();
    }
    
    long longSize() {
        final Segment<K, V>[] segments = this.segments;
        long n = 0L;
        for (int i = 0; i < segments.length; ++i) {
            n += segments[i].count;
        }
        return n;
    }
    
    final Segment<K, V>[] newSegmentArray(final int n) {
        return (Segment<K, V>[])new Segment[n];
    }
    
    void processPendingNotifications() {
        while (true) {
            final RemovalNotification<K, V> removalNotification = this.removalNotificationQueue.poll();
            if (removalNotification == null) {
                break;
            }
            try {
                this.removalListener.onRemoval(removalNotification);
            }
            catch (Throwable t) {
                LocalCache.logger.log(Level.WARNING, "Exception thrown by removal listener", t);
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
    
    boolean recordsAccess() {
        return this.expiresAfterAccess();
    }
    
    boolean recordsTime() {
        return this.recordsWrite() || this.recordsAccess();
    }
    
    boolean recordsWrite() {
        return this.expiresAfterWrite() || this.refreshes();
    }
    
    boolean refreshes() {
        return this.refreshNanos > 0L;
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
        return Ints.saturatedCast(this.longSize());
    }
    
    boolean usesAccessEntries() {
        return this.usesAccessQueue() || this.recordsAccess();
    }
    
    boolean usesAccessQueue() {
        return this.expiresAfterAccess() || this.evictsBySize();
    }
    
    boolean usesKeyReferences() {
        return this.keyStrength != Strength.STRONG;
    }
    
    boolean usesValueReferences() {
        return this.valueStrength != Strength.STRONG;
    }
    
    boolean usesWriteEntries() {
        return this.usesWriteQueue() || this.recordsWrite();
    }
    
    boolean usesWriteQueue() {
        return this.expiresAfterWrite();
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
        public long getAccessTime() {
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
        public ReferenceEntry<K, V> getNextInAccessQueue() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getNextInWriteQueue() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ValueReference<K, V> getValueReference() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long getWriteTime() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setAccessTime(final long n) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setNextInAccessQueue(final ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setNextInWriteQueue(final ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setPreviousInAccessQueue(final ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setPreviousInWriteQueue(final ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setValueReference(final ValueReference<K, V> valueReference) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setWriteTime(final long n) {
            throw new UnsupportedOperationException();
        }
    }
    
    static final class AccessQueue<K, V> extends AbstractQueue<ReferenceEntry<K, V>>
    {
        final ReferenceEntry<K, V> head;
        
        AccessQueue() {
            this.head = new AbstractReferenceEntry<K, V>() {
                ReferenceEntry<K, V> nextAccess = this;
                ReferenceEntry<K, V> previousAccess = this;
                
                @Override
                public long getAccessTime() {
                    return Long.MAX_VALUE;
                }
                
                @Override
                public ReferenceEntry<K, V> getNextInAccessQueue() {
                    return this.nextAccess;
                }
                
                @Override
                public ReferenceEntry<K, V> getPreviousInAccessQueue() {
                    return this.previousAccess;
                }
                
                @Override
                public void setAccessTime(final long n) {
                }
                
                @Override
                public void setNextInAccessQueue(final ReferenceEntry<K, V> nextAccess) {
                    this.nextAccess = nextAccess;
                }
                
                @Override
                public void setPreviousInAccessQueue(final ReferenceEntry<K, V> previousAccess) {
                    this.previousAccess = previousAccess;
                }
            };
        }
        
        @Override
        public void clear() {
            ReferenceEntry<K, V> nextInAccessQueue2;
            for (ReferenceEntry<K, V> nextInAccessQueue = this.head.getNextInAccessQueue(); nextInAccessQueue != this.head; nextInAccessQueue = nextInAccessQueue2) {
                nextInAccessQueue2 = nextInAccessQueue.getNextInAccessQueue();
                LocalCache.nullifyAccessOrder(nextInAccessQueue);
            }
            this.head.setNextInAccessQueue(this.head);
            this.head.setPreviousInAccessQueue(this.head);
        }
        
        @Override
        public boolean contains(final Object o) {
            return ((ReferenceEntry)o).getNextInAccessQueue() != NullEntry.INSTANCE;
        }
        
        @Override
        public boolean isEmpty() {
            return this.head.getNextInAccessQueue() == this.head;
        }
        
        @Override
        public Iterator<ReferenceEntry<K, V>> iterator() {
            return new AbstractSequentialIterator<ReferenceEntry<K, V>>(this.peek()) {
                @Override
                protected ReferenceEntry<K, V> computeNext(final ReferenceEntry<K, V> referenceEntry) {
                    Object nextInAccessQueue;
                    if ((nextInAccessQueue = referenceEntry.getNextInAccessQueue()) == AccessQueue.this.head) {
                        nextInAccessQueue = null;
                    }
                    return (ReferenceEntry<K, V>)nextInAccessQueue;
                }
            };
        }
        
        @Override
        public boolean offer(final ReferenceEntry<K, V> referenceEntry) {
            LocalCache.connectAccessOrder(referenceEntry.getPreviousInAccessQueue(), referenceEntry.getNextInAccessQueue());
            LocalCache.connectAccessOrder(this.head.getPreviousInAccessQueue(), referenceEntry);
            LocalCache.connectAccessOrder(referenceEntry, this.head);
            return true;
        }
        
        @Override
        public ReferenceEntry<K, V> peek() {
            Object nextInAccessQueue;
            if ((nextInAccessQueue = this.head.getNextInAccessQueue()) == this.head) {
                nextInAccessQueue = null;
            }
            return (ReferenceEntry<K, V>)nextInAccessQueue;
        }
        
        @Override
        public ReferenceEntry<K, V> poll() {
            final ReferenceEntry<K, V> nextInAccessQueue = this.head.getNextInAccessQueue();
            if (nextInAccessQueue == this.head) {
                return null;
            }
            this.remove(nextInAccessQueue);
            return nextInAccessQueue;
        }
        
        @Override
        public boolean remove(final Object o) {
            final ReferenceEntry referenceEntry = (ReferenceEntry)o;
            final ReferenceEntry<K, V> previousInAccessQueue = referenceEntry.getPreviousInAccessQueue();
            final ReferenceEntry<K, V> nextInAccessQueue = referenceEntry.getNextInAccessQueue();
            LocalCache.connectAccessOrder(previousInAccessQueue, nextInAccessQueue);
            LocalCache.nullifyAccessOrder((ReferenceEntry<Object, Object>)referenceEntry);
            return nextInAccessQueue != NullEntry.INSTANCE;
        }
        
        @Override
        public int size() {
            int n = 0;
            for (ReferenceEntry<K, V> referenceEntry = this.head.getNextInAccessQueue(); referenceEntry != this.head; referenceEntry = referenceEntry.getNextInAccessQueue()) {
                ++n;
            }
            return n;
        }
    }
    
    enum EntryFactory
    {
        STRONG {
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> segment, final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
                return new StrongEntry<K, V>(k, n, referenceEntry);
            }
        }, 
        STRONG_ACCESS {
            @Override
             <K, V> ReferenceEntry<K, V> copyEntry(final Segment<K, V> segment, final ReferenceEntry<K, V> referenceEntry, final ReferenceEntry<K, V> referenceEntry2) {
                final ReferenceEntry<K, V> copyEntry = super.copyEntry(segment, referenceEntry, referenceEntry2);
                this.copyAccessEntry(referenceEntry, copyEntry);
                return copyEntry;
            }
            
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> segment, final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
                return new StrongAccessEntry<K, V>(k, n, referenceEntry);
            }
        }, 
        STRONG_ACCESS_WRITE {
            @Override
             <K, V> ReferenceEntry<K, V> copyEntry(final Segment<K, V> segment, final ReferenceEntry<K, V> referenceEntry, final ReferenceEntry<K, V> referenceEntry2) {
                final ReferenceEntry<K, V> copyEntry = super.copyEntry(segment, referenceEntry, referenceEntry2);
                this.copyAccessEntry(referenceEntry, copyEntry);
                this.copyWriteEntry(referenceEntry, copyEntry);
                return copyEntry;
            }
            
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> segment, final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
                return new StrongAccessWriteEntry<K, V>(k, n, referenceEntry);
            }
        }, 
        STRONG_WRITE {
            @Override
             <K, V> ReferenceEntry<K, V> copyEntry(final Segment<K, V> segment, final ReferenceEntry<K, V> referenceEntry, final ReferenceEntry<K, V> referenceEntry2) {
                final ReferenceEntry<K, V> copyEntry = super.copyEntry(segment, referenceEntry, referenceEntry2);
                this.copyWriteEntry(referenceEntry, copyEntry);
                return copyEntry;
            }
            
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> segment, final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
                return new StrongWriteEntry<K, V>(k, n, referenceEntry);
            }
        }, 
        WEAK {
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> segment, final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
                return new WeakEntry<K, V>(segment.keyReferenceQueue, k, n, referenceEntry);
            }
        }, 
        WEAK_ACCESS {
            @Override
             <K, V> ReferenceEntry<K, V> copyEntry(final Segment<K, V> segment, final ReferenceEntry<K, V> referenceEntry, final ReferenceEntry<K, V> referenceEntry2) {
                final ReferenceEntry<K, V> copyEntry = super.copyEntry(segment, referenceEntry, referenceEntry2);
                this.copyAccessEntry(referenceEntry, copyEntry);
                return copyEntry;
            }
            
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> segment, final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
                return new WeakAccessEntry<K, V>(segment.keyReferenceQueue, k, n, referenceEntry);
            }
        }, 
        WEAK_ACCESS_WRITE {
            @Override
             <K, V> ReferenceEntry<K, V> copyEntry(final Segment<K, V> segment, final ReferenceEntry<K, V> referenceEntry, final ReferenceEntry<K, V> referenceEntry2) {
                final ReferenceEntry<K, V> copyEntry = super.copyEntry(segment, referenceEntry, referenceEntry2);
                this.copyAccessEntry(referenceEntry, copyEntry);
                this.copyWriteEntry(referenceEntry, copyEntry);
                return copyEntry;
            }
            
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> segment, final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
                return new WeakAccessWriteEntry<K, V>(segment.keyReferenceQueue, k, n, referenceEntry);
            }
        }, 
        WEAK_WRITE {
            @Override
             <K, V> ReferenceEntry<K, V> copyEntry(final Segment<K, V> segment, final ReferenceEntry<K, V> referenceEntry, final ReferenceEntry<K, V> referenceEntry2) {
                final ReferenceEntry<K, V> copyEntry = super.copyEntry(segment, referenceEntry, referenceEntry2);
                this.copyWriteEntry(referenceEntry, copyEntry);
                return copyEntry;
            }
            
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> segment, final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
                return new WeakWriteEntry<K, V>(segment.keyReferenceQueue, k, n, referenceEntry);
            }
        };
        
        static final EntryFactory[] factories;
        
        static {
            factories = new EntryFactory[] { EntryFactory.STRONG, EntryFactory.STRONG_ACCESS, EntryFactory.STRONG_WRITE, EntryFactory.STRONG_ACCESS_WRITE, EntryFactory.WEAK, EntryFactory.WEAK_ACCESS, EntryFactory.WEAK_WRITE, EntryFactory.WEAK_ACCESS_WRITE };
        }
        
        static EntryFactory getFactory(final Strength strength, final boolean b, final boolean b2) {
            int n = false ? 1 : 0;
            int n2;
            if (strength == Strength.WEAK) {
                n2 = 4;
            }
            else {
                n2 = (false ? 1 : 0);
            }
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
            return EntryFactory.factories[(b3 ? 1 : 0) | n2 | n];
        }
        
         <K, V> void copyAccessEntry(final ReferenceEntry<K, V> referenceEntry, final ReferenceEntry<K, V> referenceEntry2) {
            referenceEntry2.setAccessTime(referenceEntry.getAccessTime());
            LocalCache.connectAccessOrder(referenceEntry.getPreviousInAccessQueue(), referenceEntry2);
            LocalCache.connectAccessOrder(referenceEntry2, referenceEntry.getNextInAccessQueue());
            LocalCache.nullifyAccessOrder(referenceEntry);
        }
        
         <K, V> ReferenceEntry<K, V> copyEntry(final Segment<K, V> segment, final ReferenceEntry<K, V> referenceEntry, final ReferenceEntry<K, V> referenceEntry2) {
            return this.newEntry(segment, referenceEntry.getKey(), referenceEntry.getHash(), referenceEntry2);
        }
        
         <K, V> void copyWriteEntry(final ReferenceEntry<K, V> referenceEntry, final ReferenceEntry<K, V> referenceEntry2) {
            referenceEntry2.setWriteTime(referenceEntry.getWriteTime());
            LocalCache.connectWriteOrder(referenceEntry.getPreviousInWriteQueue(), referenceEntry2);
            LocalCache.connectWriteOrder(referenceEntry2, referenceEntry.getNextInWriteQueue());
            LocalCache.nullifyWriteOrder(referenceEntry);
        }
        
        abstract <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> p0, final K p1, final int p2, final ReferenceEntry<K, V> p3);
    }
    
    final class EntryIterator extends LocalCache$HashIterator implements Iterator<Entry<K, V>>
    {
        public Entry<K, V> next() {
            return this.nextEntry();
        }
    }
    
    final class EntrySet extends AbstractSet<Entry<K, V>>
    {
        @Override
        public void clear() {
            LocalCache.this.clear();
        }
        
        @Override
        public boolean contains(final Object o) {
            if (o instanceof Entry) {
                final Entry entry = (Entry)o;
                final Object key = entry.getKey();
                if (key != null) {
                    final V value = LocalCache.this.get(key);
                    if (value != null && LocalCache.this.valueEquivalence.equivalent(entry.getValue(), value)) {
                        return true;
                    }
                }
            }
            return false;
        }
        
        @Override
        public boolean isEmpty() {
            return LocalCache.this.isEmpty();
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
                if (key != null && LocalCache.this.remove(key, entry.getValue())) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public int size() {
            return LocalCache.this.size();
        }
    }
    
    abstract class HashIterator
    {
        Segment<K, V> currentSegment;
        AtomicReferenceArray<ReferenceEntry<K, V>> currentTable;
        LocalCache$WriteThroughEntry lastReturned;
        ReferenceEntry<K, V> nextEntry;
        LocalCache$WriteThroughEntry nextExternal;
        int nextSegmentIndex;
        int nextTableIndex;
        
        HashIterator() {
            this.nextSegmentIndex = LocalCache.this.segments.length - 1;
            this.nextTableIndex = -1;
            this.advance();
        }
        
        final void advance() {
            this.nextExternal = null;
            if (!this.nextInChain() && !this.nextInTable()) {
                while (this.nextSegmentIndex >= 0) {
                    this.currentSegment = LocalCache.this.segments[this.nextSegmentIndex--];
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
                final long read = LocalCache.this.ticker.read();
                final K key = referenceEntry.getKey();
                final Object liveValue = LocalCache.this.getLiveValue((ReferenceEntry<K, Object>)referenceEntry, read);
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
        
        com.google.common.cache.LocalCache$com.google.common.cache.LocalCache$WriteThroughEntry nextEntry() {
            if (this.nextExternal == null) {
                throw new NoSuchElementException();
            }
            this.lastReturned = this.nextExternal;
            this.advance();
            return (com.google.common.cache.LocalCache$com.google.common.cache.LocalCache$WriteThroughEntry)this.lastReturned;
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
            LocalCache.this.remove(this.lastReturned.getKey());
            this.lastReturned = null;
        }
    }
    
    final class KeyIterator extends LocalCache$HashIterator implements Iterator<K>
    {
        public K next() {
            return this.nextEntry().getKey();
        }
    }
    
    final class KeySet extends AbstractSet<K>
    {
        @Override
        public void clear() {
            LocalCache.this.clear();
        }
        
        @Override
        public boolean contains(final Object o) {
            return LocalCache.this.containsKey(o);
        }
        
        @Override
        public boolean isEmpty() {
            return LocalCache.this.isEmpty();
        }
        
        @Override
        public Iterator<K> iterator() {
            return new KeyIterator();
        }
        
        @Override
        public boolean remove(final Object o) {
            return LocalCache.this.remove(o) != null;
        }
        
        @Override
        public int size() {
            return LocalCache.this.size();
        }
    }
    
    static class LoadingValueReference<K, V> implements ValueReference<K, V>
    {
        final SettableFuture<V> futureValue;
        volatile ValueReference<K, V> oldValue;
        final Stopwatch stopwatch;
        
        public LoadingValueReference() {
            this(LocalCache.unset());
        }
        
        public LoadingValueReference(final ValueReference<K, V> oldValue) {
            this.futureValue = SettableFuture.create();
            this.stopwatch = new Stopwatch();
            this.oldValue = oldValue;
        }
        
        private ListenableFuture<V> fullyFailedFuture(final Throwable t) {
            final SettableFuture<?> create = SettableFuture.create();
            setException(create, t);
            return (ListenableFuture<V>)create;
        }
        
        private static boolean setException(final SettableFuture<?> settableFuture, final Throwable exception) {
            try {
                return settableFuture.setException(exception);
            }
            catch (Error error) {
                return false;
            }
        }
        
        @Override
        public ValueReference<K, V> copyFor(final ReferenceQueue<V> referenceQueue, final V v, final ReferenceEntry<K, V> referenceEntry) {
            return this;
        }
        
        public long elapsedNanos() {
            return this.stopwatch.elapsedTime(TimeUnit.NANOSECONDS);
        }
        
        @Override
        public V get() {
            return this.oldValue.get();
        }
        
        @Override
        public ReferenceEntry<K, V> getEntry() {
            return null;
        }
        
        public ValueReference<K, V> getOldValue() {
            return this.oldValue;
        }
        
        @Override
        public int getWeight() {
            return this.oldValue.getWeight();
        }
        
        @Override
        public boolean isActive() {
            return this.oldValue.isActive();
        }
        
        @Override
        public boolean isLoading() {
            return true;
        }
        
        public ListenableFuture<V> loadFuture(final K k, final CacheLoader<? super K, V> cacheLoader) {
            this.stopwatch.start();
            final V value = this.oldValue.get();
            while (true) {
                if (value == null) {
                    ListenableFuture<V> listenableFuture;
                    try {
                        final V load = cacheLoader.load(k);
                        if (this.set(load)) {
                            return this.futureValue;
                        }
                        return Futures.immediateFuture(load);
                        listenableFuture = cacheLoader.reload(k, value);
                        // iftrue(Label_0060:, listenableFuture == null)
                        return listenableFuture;
                        Label_0060: {
                            listenableFuture = Futures.immediateFuture((V)null);
                        }
                    }
                    catch (Throwable exception) {
                        if (exception instanceof InterruptedException) {
                            Thread.currentThread().interrupt();
                        }
                        if (this.setException(exception)) {
                            return this.futureValue;
                        }
                        return this.fullyFailedFuture(exception);
                    }
                    return listenableFuture;
                }
                continue;
            }
        }
        
        @Override
        public void notifyNewValue(final V v) {
            if (v != null) {
                this.set(v);
                return;
            }
            this.oldValue = LocalCache.unset();
        }
        
        public boolean set(final V v) {
            return this.futureValue.set(v);
        }
        
        public boolean setException(final Throwable t) {
            return setException(this.futureValue, t);
        }
    }
    
    static class LocalManualCache<K, V> implements Cache<K, V>, Serializable
    {
        final LocalCache<K, V> localCache;
        
        LocalManualCache(final CacheBuilder<? super K, ? super V> cacheBuilder) {
            this(new LocalCache<Object, Object>((CacheBuilder<? super Object, ? super Object>)cacheBuilder, null));
        }
        
        private LocalManualCache(final LocalCache<K, V> localCache) {
            this.localCache = localCache;
        }
        
        @Override
        public V getIfPresent(final Object o) {
            return this.localCache.getIfPresent(o);
        }
        
        @Override
        public void invalidate(final Object o) {
            Preconditions.checkNotNull(o);
            this.localCache.remove(o);
        }
        
        @Override
        public void invalidateAll() {
            this.localCache.clear();
        }
        
        @Override
        public void put(final K k, final V v) {
            this.localCache.put(k, v);
        }
    }
    
    private enum NullEntry implements ReferenceEntry<Object, Object>
    {
        INSTANCE;
        
        @Override
        public long getAccessTime() {
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
        public ReferenceEntry<Object, Object> getNextInAccessQueue() {
            return this;
        }
        
        @Override
        public ReferenceEntry<Object, Object> getNextInWriteQueue() {
            return this;
        }
        
        @Override
        public ReferenceEntry<Object, Object> getPreviousInAccessQueue() {
            return this;
        }
        
        @Override
        public ReferenceEntry<Object, Object> getPreviousInWriteQueue() {
            return this;
        }
        
        @Override
        public ValueReference<Object, Object> getValueReference() {
            return null;
        }
        
        @Override
        public long getWriteTime() {
            return 0L;
        }
        
        @Override
        public void setAccessTime(final long n) {
        }
        
        @Override
        public void setNextInAccessQueue(final ReferenceEntry<Object, Object> referenceEntry) {
        }
        
        @Override
        public void setNextInWriteQueue(final ReferenceEntry<Object, Object> referenceEntry) {
        }
        
        @Override
        public void setPreviousInAccessQueue(final ReferenceEntry<Object, Object> referenceEntry) {
        }
        
        @Override
        public void setPreviousInWriteQueue(final ReferenceEntry<Object, Object> referenceEntry) {
        }
        
        @Override
        public void setValueReference(final ValueReference<Object, Object> valueReference) {
        }
        
        @Override
        public void setWriteTime(final long n) {
        }
    }
    
    interface ReferenceEntry<K, V>
    {
        long getAccessTime();
        
        int getHash();
        
        K getKey();
        
        ReferenceEntry<K, V> getNext();
        
        ReferenceEntry<K, V> getNextInAccessQueue();
        
        ReferenceEntry<K, V> getNextInWriteQueue();
        
        ReferenceEntry<K, V> getPreviousInAccessQueue();
        
        ReferenceEntry<K, V> getPreviousInWriteQueue();
        
        ValueReference<K, V> getValueReference();
        
        long getWriteTime();
        
        void setAccessTime(final long p0);
        
        void setNextInAccessQueue(final ReferenceEntry<K, V> p0);
        
        void setNextInWriteQueue(final ReferenceEntry<K, V> p0);
        
        void setPreviousInAccessQueue(final ReferenceEntry<K, V> p0);
        
        void setPreviousInWriteQueue(final ReferenceEntry<K, V> p0);
        
        void setValueReference(final ValueReference<K, V> p0);
        
        void setWriteTime(final long p0);
    }
    
    static class Segment<K, V> extends ReentrantLock
    {
        final Queue<ReferenceEntry<K, V>> accessQueue;
        volatile int count;
        final ReferenceQueue<K> keyReferenceQueue;
        final LocalCache<K, V> map;
        final long maxSegmentWeight;
        int modCount;
        final AtomicInteger readCount;
        final Queue<ReferenceEntry<K, V>> recencyQueue;
        final AbstractCache.StatsCounter statsCounter;
        volatile AtomicReferenceArray<ReferenceEntry<K, V>> table;
        int threshold;
        int totalWeight;
        final ReferenceQueue<V> valueReferenceQueue;
        final Queue<ReferenceEntry<K, V>> writeQueue;
        
        Segment(final LocalCache<K, V> map, final int n, final long maxSegmentWeight, final AbstractCache.StatsCounter statsCounter) {
            final ReferenceQueue<V> referenceQueue = null;
            this.readCount = new AtomicInteger();
            this.map = map;
            this.maxSegmentWeight = maxSegmentWeight;
            this.statsCounter = statsCounter;
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
            if (map.usesAccessQueue()) {
                discardingQueue = new ConcurrentLinkedQueue<ReferenceEntry<K, V>>();
            }
            else {
                discardingQueue = LocalCache.discardingQueue();
            }
            this.recencyQueue = discardingQueue;
            Queue<ReferenceEntry<K, V>> discardingQueue2;
            if (map.usesWriteQueue()) {
                discardingQueue2 = (Queue<ReferenceEntry<K, V>>)new WriteQueue();
            }
            else {
                discardingQueue2 = LocalCache.discardingQueue();
            }
            this.writeQueue = discardingQueue2;
            Queue<ReferenceEntry<K, V>> discardingQueue3;
            if (map.usesAccessQueue()) {
                discardingQueue3 = (Queue<ReferenceEntry<K, V>>)new AccessQueue();
            }
            else {
                discardingQueue3 = LocalCache.discardingQueue();
            }
            this.accessQueue = discardingQueue3;
        }
        
        void cleanUp() {
            this.runLockedCleanup(this.map.ticker.read());
            this.runUnlockedCleanup();
        }
        
        void clear() {
            if (this.count == 0) {
                return;
            }
        Label_0071_Outer:
            while (true) {
                this.lock();
                while (true) {
                Label_0164:
                    while (true) {
                        int n = 0;
                        Label_0157: {
                            try {
                                final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                                n = 0;
                                if (n < table.length()) {
                                    for (ReferenceEntry<K, V> next = table.get(n); next != null; next = next.getNext()) {
                                        if (next.getValueReference().isActive()) {
                                            this.enqueueNotification(next, RemovalCause.EXPLICIT);
                                        }
                                    }
                                    break Label_0157;
                                }
                                break Label_0164;
                                // iftrue(Label_0092:, n >= table.length())
                                while (true) {
                                    table.set(n, (ReferenceEntry<K, V>)null);
                                    ++n;
                                    continue Label_0071_Outer;
                                }
                                Label_0092: {
                                    this.clearReferenceQueues();
                                }
                                this.writeQueue.clear();
                                this.accessQueue.clear();
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
                        continue Label_0071_Outer;
                    }
                    int n = 0;
                    continue;
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
        
        void clearValueReferenceQueue() {
            while (this.valueReferenceQueue.poll() != null) {}
        }
        
        boolean containsKey(Object value, final int n) {
            boolean b = false;
            try {
                if (this.count == 0) {
                    return false;
                }
                final ReferenceEntry<K, Object> liveEntry = this.getLiveEntry(value, n, this.map.ticker.read());
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
                if (value != null || !valueReference.isActive()) {
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
                if (!this.accessQueue.contains(referenceEntry)) {
                    continue;
                }
                this.accessQueue.add(referenceEntry);
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
        
        void enqueueNotification(final ReferenceEntry<K, V> referenceEntry, final RemovalCause removalCause) {
            this.enqueueNotification(referenceEntry.getKey(), referenceEntry.getHash(), referenceEntry.getValueReference(), removalCause);
        }
        
        void enqueueNotification(final K k, final int n, final ValueReference<K, V> valueReference, final RemovalCause removalCause) {
            this.totalWeight -= valueReference.getWeight();
            if (removalCause.wasEvicted()) {
                this.statsCounter.recordEviction();
            }
            if (this.map.removalNotificationQueue != LocalCache.DISCARDING_QUEUE) {
                this.map.removalNotificationQueue.offer(new RemovalNotification<K, V>(k, valueReference.get(), removalCause));
            }
        }
        
        void evictEntries() {
            if (this.map.evictsBySize()) {
                this.drainRecencyQueue();
                while (this.totalWeight > this.maxSegmentWeight) {
                    final ReferenceEntry<K, V> nextEvictable = this.getNextEvictable();
                    if (!this.removeEntry(nextEvictable, nextEvictable.getHash(), RemovalCause.SIZE)) {
                        throw new AssertionError();
                    }
                }
            }
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
        
        void expireEntries(final long n) {
            this.drainRecencyQueue();
            ReferenceEntry referenceEntry;
            do {
                referenceEntry = this.writeQueue.peek();
                if (referenceEntry != null && this.map.isExpired(referenceEntry, n)) {
                    continue;
                }
                ReferenceEntry referenceEntry2;
                do {
                    referenceEntry2 = this.accessQueue.peek();
                    if (referenceEntry2 != null && this.map.isExpired(referenceEntry2, n)) {
                        continue;
                    }
                    return;
                } while (this.removeEntry(referenceEntry2, referenceEntry2.getHash(), RemovalCause.EXPIRED));
                throw new AssertionError();
            } while (this.removeEntry(referenceEntry, referenceEntry.getHash(), RemovalCause.EXPIRED));
            throw new AssertionError();
        }
        
        V get(Object scheduleRefresh, final int n) {
            try {
                if (this.count != 0) {
                    final long read = this.map.ticker.read();
                    final ReferenceEntry<K, V> liveEntry = this.getLiveEntry(scheduleRefresh, n, read);
                    if (liveEntry == null) {
                        return null;
                    }
                    final V value = liveEntry.getValueReference().get();
                    if (value != null) {
                        this.recordRead(liveEntry, read);
                        scheduleRefresh = this.scheduleRefresh((ReferenceEntry<K, Object>)liveEntry, (K)liveEntry.getKey(), n, (Object)value, read, (CacheLoader<? super K, Object>)this.map.defaultLoader);
                        return (V)scheduleRefresh;
                    }
                    this.tryDrainReferenceQueues();
                }
                return null;
            }
            finally {
                this.postReadCleanup();
            }
        }
        
        V getAndRecordStats(final K k, final int n, final LoadingValueReference<K, V> loadingValueReference, final ListenableFuture<V> listenableFuture) throws ExecutionException {
            V v = null;
            try {
                final V uninterruptibly = Uninterruptibles.getUninterruptibly(listenableFuture);
                if (uninterruptibly == null) {
                    v = uninterruptibly;
                    throw new CacheLoader.InvalidCacheLoadException("CacheLoader returned null for key " + k + ".");
                }
            }
            finally {
                if (v == null) {
                    this.statsCounter.recordLoadException(loadingValueReference.elapsedNanos());
                    this.removeLoadingValue(k, n, loadingValueReference);
                }
            }
            this.statsCounter.recordLoadSuccess(loadingValueReference.elapsedNanos());
            final V v2;
            this.storeLoadedValue(k, n, loadingValueReference, v2);
            if (v2 == null) {
                this.statsCounter.recordLoadException(loadingValueReference.elapsedNanos());
                this.removeLoadingValue(k, n, loadingValueReference);
            }
            return v2;
        }
        
        ReferenceEntry<K, V> getEntry(final Object o, final int n) {
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
            return null;
        }
        
        ReferenceEntry<K, V> getFirst(final int n) {
            final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
            return (ReferenceEntry<K, V>)(ReferenceEntry)table.get(table.length() - 1 & n);
        }
        
        ReferenceEntry<K, V> getLiveEntry(final Object o, final int n, final long n2) {
            final ReferenceEntry<K, V> entry = this.getEntry(o, n);
            ReferenceEntry<K, V> referenceEntry;
            if (entry == null) {
                referenceEntry = null;
            }
            else {
                referenceEntry = entry;
                if (this.map.isExpired(entry, n2)) {
                    this.tryExpireEntries(n2);
                    return null;
                }
            }
            return referenceEntry;
        }
        
        V getLiveValue(final ReferenceEntry<K, V> referenceEntry, final long n) {
            V value;
            if (referenceEntry.getKey() == null) {
                this.tryDrainReferenceQueues();
                value = null;
            }
            else {
                value = referenceEntry.getValueReference().get();
                if (value == null) {
                    this.tryDrainReferenceQueues();
                    return null;
                }
                if (this.map.isExpired(referenceEntry, n)) {
                    this.tryExpireEntries(n);
                    return null;
                }
            }
            return value;
        }
        
        ReferenceEntry<K, V> getNextEvictable() {
            for (final ReferenceEntry<K, V> referenceEntry : this.accessQueue) {
                if (referenceEntry.getValueReference().getWeight() > 0) {
                    return referenceEntry;
                }
            }
            throw new AssertionError();
        }
        
        void initTable(final AtomicReferenceArray<ReferenceEntry<K, V>> table) {
            this.threshold = table.length() * 3 / 4;
            if (!this.map.customWeigher() && this.threshold == this.maxSegmentWeight) {
                ++this.threshold;
            }
            this.table = table;
        }
        
        LoadingValueReference<K, V> insertLoadingValueReference(final K k, final int n) {
            this.lock();
            try {
                this.preWriteCleanup(this.map.ticker.read());
                final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                final int n2 = n & table.length() - 1;
                Object next;
                final ReferenceEntry<Object, V> referenceEntry = (ReferenceEntry<Object, V>)(next = table.get(n2));
                while (next != null) {
                    final Object key = ((ReferenceEntry<Object, V>)next).getKey();
                    if (((ReferenceEntry)next).getHash() == n && key != null && this.map.keyEquivalence.equivalent(k, key)) {
                        final ValueReference<Object, V> valueReference = ((ReferenceEntry<Object, V>)next).getValueReference();
                        if (valueReference.isLoading()) {
                            return null;
                        }
                        ++this.modCount;
                        final LoadingValueReference valueReference2 = new LoadingValueReference<Object, Object>((ValueReference<Object, V>)valueReference);
                        ((ReferenceEntry<Object, V>)next).setValueReference((ValueReference<Object, V>)valueReference2);
                        return (LoadingValueReference<K, V>)valueReference2;
                    }
                    else {
                        next = ((ReferenceEntry<Object, V>)next).getNext();
                    }
                }
                ++this.modCount;
                final LoadingValueReference<K, V> valueReference3 = (LoadingValueReference<K, V>)new LoadingValueReference<Object, V>();
                final ReferenceEntry<Object, V> entry = this.newEntry((Object)k, n, referenceEntry);
                entry.setValueReference((ValueReference<Object, V>)valueReference3);
                table.set(n2, (ReferenceEntry<K, V>)entry);
                return valueReference3;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
        }
        
        ListenableFuture<V> loadAsync(final K k, final int n, final LoadingValueReference<K, V> loadingValueReference, final CacheLoader<? super K, V> cacheLoader) {
            final ListenableFuture<V> loadFuture = loadingValueReference.loadFuture(k, cacheLoader);
            loadFuture.addListener(new Runnable() {
                @Override
                public void run() {
                    try {
                        loadingValueReference.set(Segment.this.getAndRecordStats(k, n, loadingValueReference, loadFuture));
                    }
                    catch (Throwable exception) {
                        LocalCache.logger.log(Level.WARNING, "Exception thrown during refresh", exception);
                        loadingValueReference.setException(exception);
                    }
                }
            }, LocalCache.sameThreadExecutor);
            return loadFuture;
        }
        
        ReferenceEntry<K, V> newEntry(final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
            return this.map.entryFactory.newEntry(this, k, n, referenceEntry);
        }
        
        AtomicReferenceArray<ReferenceEntry<K, V>> newEntryArray(final int n) {
            return new AtomicReferenceArray<ReferenceEntry<K, V>>(n);
        }
        
        void postReadCleanup() {
            if ((this.readCount.incrementAndGet() & 0x3F) == 0x0) {
                this.cleanUp();
            }
        }
        
        void postWriteCleanup() {
            this.runUnlockedCleanup();
        }
        
        void preWriteCleanup(final long n) {
            this.runLockedCleanup(n);
        }
        
        V put(final K k, int count, final V v, final boolean b) {
            this.lock();
            try {
                final long read = this.map.ticker.read();
                this.preWriteCleanup(read);
                if (this.count + 1 > this.threshold) {
                    this.expand();
                    final int count2 = this.count;
                }
                final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                final int n = count & table.length() - 1;
                Object next;
                final ReferenceEntry<Object, V> referenceEntry = (ReferenceEntry<Object, V>)(next = table.get(n));
                while (next != null) {
                    final K key = (K)((ReferenceEntry<K, V>)next).getKey();
                    if (((ReferenceEntry)next).getHash() == count && key != null && this.map.keyEquivalence.equivalent(k, key)) {
                        final ValueReference<K, V> valueReference = ((ReferenceEntry<K, V>)next).getValueReference();
                        final V value = valueReference.get();
                        if (value == null) {
                            ++this.modCount;
                            if (valueReference.isActive()) {
                                this.enqueueNotification(k, count, valueReference, RemovalCause.COLLECTED);
                                this.setValue((ReferenceEntry<K, V>)next, k, v, read);
                                count = this.count;
                            }
                            else {
                                this.setValue((ReferenceEntry<K, V>)next, k, v, read);
                                count = this.count + 1;
                            }
                            this.count = count;
                            this.evictEntries();
                            return null;
                        }
                        if (b) {
                            this.recordLockedRead((ReferenceEntry<K, V>)next, read);
                            return value;
                        }
                        ++this.modCount;
                        this.enqueueNotification(k, count, valueReference, RemovalCause.REPLACED);
                        this.setValue((ReferenceEntry<K, V>)next, k, v, read);
                        this.evictEntries();
                        return value;
                    }
                    else {
                        next = ((ReferenceEntry<K, V>)next).getNext();
                    }
                }
                ++this.modCount;
                final ReferenceEntry<Object, V> entry = this.newEntry((Object)k, count, referenceEntry);
                this.setValue((ReferenceEntry<K, V>)entry, k, v, read);
                table.set(n, (ReferenceEntry<K, V>)entry);
                ++this.count;
                this.evictEntries();
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
                        final ReferenceEntry<K, V> removeValueFromChain = this.removeValueFromChain(referenceEntry2, next, next.getKey(), count, next.getValueReference(), RemovalCause.COLLECTED);
                        count = this.count;
                        table.set(n, removeValueFromChain);
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
                    final K key = ((ReferenceEntry<K, V>)next).getKey();
                    if (((ReferenceEntry)next).getHash() == count && key != null && this.map.keyEquivalence.equivalent(k, key)) {
                        if (((ReferenceEntry<K, V>)next).getValueReference() == valueReference) {
                            ++this.modCount;
                            final ReferenceEntry<Object, V> removeValueFromChain = this.removeValueFromChain(referenceEntry, (ReferenceEntry<Object, V>)next, (Object)key, count, (ValueReference<Object, V>)valueReference, RemovalCause.COLLECTED);
                            count = this.count;
                            table.set(n, (ReferenceEntry<K, V>)removeValueFromChain);
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
                        next = ((ReferenceEntry<Object, V>)next).getNext();
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
        
        void recordLockedRead(final ReferenceEntry<K, V> referenceEntry, final long accessTime) {
            if (this.map.recordsAccess()) {
                referenceEntry.setAccessTime(accessTime);
            }
            this.accessQueue.add(referenceEntry);
        }
        
        void recordRead(final ReferenceEntry<K, V> referenceEntry, final long accessTime) {
            if (this.map.recordsAccess()) {
                referenceEntry.setAccessTime(accessTime);
            }
            this.recencyQueue.add(referenceEntry);
        }
        
        void recordWrite(final ReferenceEntry<K, V> referenceEntry, final int n, final long n2) {
            this.drainRecencyQueue();
            this.totalWeight += n;
            if (this.map.recordsAccess()) {
                referenceEntry.setAccessTime(n2);
            }
            if (this.map.recordsWrite()) {
                referenceEntry.setWriteTime(n2);
            }
            this.accessQueue.add(referenceEntry);
            this.writeQueue.add(referenceEntry);
        }
        
        V refresh(final K k, final int n, final CacheLoader<? super K, V> cacheLoader) {
            final LoadingValueReference<K, V> insertLoadingValueReference = this.insertLoadingValueReference(k, n);
            if (insertLoadingValueReference != null) {
                final ListenableFuture<V> loadAsync = this.loadAsync(k, n, (LoadingValueReference<K, V>)insertLoadingValueReference, (CacheLoader<? super K, V>)cacheLoader);
                if (loadAsync.isDone()) {
                    try {
                        return Uninterruptibles.getUninterruptibly((Future<V>)loadAsync);
                    }
                    catch (Throwable t) {
                        return null;
                    }
                }
            }
            return null;
        }
        
        V remove(final Object o, int count) {
            this.lock();
            try {
                this.preWriteCleanup(this.map.ticker.read());
                final int count2 = this.count;
                final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                final int n = count & table.length() - 1;
                Object next;
                for (ReferenceEntry<Object, V> referenceEntry = (ReferenceEntry<Object, V>)(next = table.get(n)); next != null; next = ((ReferenceEntry<Object, V>)next).getNext()) {
                    final K key = ((ReferenceEntry<K, V>)next).getKey();
                    if (((ReferenceEntry)next).getHash() == count && key != null && this.map.keyEquivalence.equivalent(o, key)) {
                        final ValueReference<K, V> valueReference = ((ReferenceEntry<K, V>)next).getValueReference();
                        final V value = valueReference.get();
                        RemovalCause removalCause;
                        if (value != null) {
                            removalCause = RemovalCause.EXPLICIT;
                        }
                        else {
                            if (!valueReference.isActive()) {
                                return null;
                            }
                            removalCause = RemovalCause.COLLECTED;
                        }
                        ++this.modCount;
                        final ReferenceEntry<Object, V> removeValueFromChain = this.removeValueFromChain(referenceEntry, (ReferenceEntry<Object, V>)next, (Object)key, count, (ValueReference<Object, V>)valueReference, removalCause);
                        count = this.count;
                        table.set(n, (ReferenceEntry<K, V>)removeValueFromChain);
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
        
        boolean remove(Object value, int count, final Object o) {
            this.lock();
            try {
                this.preWriteCleanup(this.map.ticker.read());
                final int count2 = this.count;
                final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                final int n = count & table.length() - 1;
                Object next;
                for (ReferenceEntry<Object, V> referenceEntry = (ReferenceEntry<Object, V>)(next = table.get(n)); next != null; next = ((ReferenceEntry<Object, V>)next).getNext()) {
                    final K key = ((ReferenceEntry<K, V>)next).getKey();
                    if (((ReferenceEntry)next).getHash() == count && key != null && this.map.keyEquivalence.equivalent(value, key)) {
                        final ValueReference<K, V> valueReference = ((ReferenceEntry<K, V>)next).getValueReference();
                        value = valueReference.get();
                        RemovalCause removalCause;
                        if (this.map.valueEquivalence.equivalent(o, value)) {
                            removalCause = RemovalCause.EXPLICIT;
                        }
                        else {
                            if (value != null || !valueReference.isActive()) {
                                return false;
                            }
                            removalCause = RemovalCause.COLLECTED;
                        }
                        ++this.modCount;
                        final ReferenceEntry<Object, V> removeValueFromChain = this.removeValueFromChain(referenceEntry, (ReferenceEntry<Object, V>)next, (Object)key, count, (ValueReference<Object, V>)valueReference, removalCause);
                        count = this.count;
                        table.set(n, (ReferenceEntry<K, V>)removeValueFromChain);
                        this.count = count - 1;
                        return removalCause == RemovalCause.EXPLICIT;
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
            this.enqueueNotification(referenceEntry, RemovalCause.COLLECTED);
            this.writeQueue.remove(referenceEntry);
            this.accessQueue.remove(referenceEntry);
        }
        
        boolean removeEntry(final ReferenceEntry<K, V> referenceEntry, int count, final RemovalCause removalCause) {
            final int count2 = this.count;
            final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
            final int n = count & table.length() - 1;
            ReferenceEntry<K, V> next;
            for (ReferenceEntry<K, V> referenceEntry2 = next = table.get(n); next != null; next = next.getNext()) {
                if (next == referenceEntry) {
                    ++this.modCount;
                    final ReferenceEntry<K, V> removeValueFromChain = this.removeValueFromChain(referenceEntry2, next, next.getKey(), count, next.getValueReference(), removalCause);
                    count = this.count;
                    table.set(n, removeValueFromChain);
                    this.count = count - 1;
                    return true;
                }
            }
            return false;
        }
        
        ReferenceEntry<K, V> removeEntryFromChain(ReferenceEntry<K, V> next, final ReferenceEntry<K, V> referenceEntry) {
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
        
        boolean removeLoadingValue(final K k, final int n, final LoadingValueReference<K, V> loadingValueReference) {
            while (true) {
                this.lock();
                Label_0173: {
                Label_0151:
                    while (true) {
                        ReferenceEntry<K, V> next = null;
                        Label_0161: {
                            try {
                                final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                                final int n2 = n & table.length() - 1;
                                final ReferenceEntry referenceEntry = next = (ReferenceEntry<K, V>)(ReferenceEntry)table.get(n2);
                                if (next == null) {
                                    break Label_0173;
                                }
                                final K key = next.getKey();
                                if (next.getHash() != n || key == null || !this.map.keyEquivalence.equivalent(k, key)) {
                                    break Label_0161;
                                }
                                if (next.getValueReference() == loadingValueReference) {
                                    if (loadingValueReference.isActive()) {
                                        next.setValueReference(loadingValueReference.getOldValue());
                                    }
                                    else {
                                        table.set(n2, this.removeEntryFromChain(referenceEntry, next));
                                    }
                                    return true;
                                }
                            }
                            finally {
                                this.unlock();
                                this.postWriteCleanup();
                            }
                            break Label_0151;
                        }
                        next = next.getNext();
                        continue;
                    }
                    this.unlock();
                    this.postWriteCleanup();
                    return false;
                }
                this.unlock();
                this.postWriteCleanup();
                return false;
            }
        }
        
        ReferenceEntry<K, V> removeValueFromChain(final ReferenceEntry<K, V> referenceEntry, final ReferenceEntry<K, V> referenceEntry2, final K k, final int n, final ValueReference<K, V> valueReference, final RemovalCause removalCause) {
            this.enqueueNotification(k, n, valueReference, removalCause);
            this.writeQueue.remove(referenceEntry2);
            this.accessQueue.remove(referenceEntry2);
            if (valueReference.isLoading()) {
                valueReference.notifyNewValue(null);
                return referenceEntry;
            }
            return this.removeEntryFromChain(referenceEntry, referenceEntry2);
        }
        
        V replace(final K k, int count, final V v) {
            this.lock();
            try {
                final long read = this.map.ticker.read();
                this.preWriteCleanup(read);
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
                            if (valueReference.isActive()) {
                                final int count2 = this.count;
                                ++this.modCount;
                                final ReferenceEntry<Object, V> removeValueFromChain = this.removeValueFromChain(referenceEntry, (ReferenceEntry<Object, V>)next, (Object)key, count, (ValueReference<Object, V>)valueReference, RemovalCause.COLLECTED);
                                count = this.count;
                                table.set(n, (ReferenceEntry<K, V>)removeValueFromChain);
                                this.count = count - 1;
                            }
                            return null;
                        }
                        ++this.modCount;
                        this.enqueueNotification(k, count, valueReference, RemovalCause.REPLACED);
                        this.setValue((ReferenceEntry<K, V>)next, k, v, read);
                        this.evictEntries();
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
                final long read = this.map.ticker.read();
                this.preWriteCleanup(read);
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
                            if (valueReference.isActive()) {
                                final int count2 = this.count;
                                ++this.modCount;
                                final ReferenceEntry<Object, V> removeValueFromChain = this.removeValueFromChain(referenceEntry, (ReferenceEntry<Object, V>)next, (Object)key, count, (ValueReference<Object, V>)valueReference, RemovalCause.COLLECTED);
                                count = this.count;
                                table.set(n, (ReferenceEntry<K, V>)removeValueFromChain);
                                this.count = count - 1;
                            }
                            return false;
                        }
                        if (this.map.valueEquivalence.equivalent(v, value)) {
                            ++this.modCount;
                            this.enqueueNotification(k, count, valueReference, RemovalCause.REPLACED);
                            this.setValue((ReferenceEntry<K, V>)next, k, v2, read);
                            this.evictEntries();
                            return true;
                        }
                        this.recordLockedRead((ReferenceEntry<K, V>)next, read);
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
        
        void runLockedCleanup(final long n) {
            if (!this.tryLock()) {
                return;
            }
            try {
                this.drainReferenceQueues();
                this.expireEntries(n);
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
        
        V scheduleRefresh(final ReferenceEntry<K, V> referenceEntry, final K k, final int n, final V v, final long n2, final CacheLoader<? super K, V> cacheLoader) {
            if (this.map.refreshes() && n2 - referenceEntry.getWriteTime() > this.map.refreshNanos) {
                final Object refresh = this.refresh(k, n, (CacheLoader<? super K, Object>)cacheLoader);
                if (refresh != null) {
                    return (V)refresh;
                }
            }
            return v;
        }
        
        void setValue(final ReferenceEntry<K, V> referenceEntry, final K k, final V v, final long n) {
            final ValueReference<K, V> valueReference = referenceEntry.getValueReference();
            final int weigh = this.map.weigher.weigh(k, v);
            Preconditions.checkState(weigh >= 0, (Object)"Weights must be non-negative");
            referenceEntry.setValueReference(this.map.valueStrength.referenceValue(this, referenceEntry, v, weigh));
            this.recordWrite(referenceEntry, weigh, n);
            valueReference.notifyNewValue(v);
        }
        
        boolean storeLoadedValue(final K k, final int n, final LoadingValueReference<K, V> loadingValueReference, final V v) {
            this.lock();
            try {
                final long read = this.map.ticker.read();
                this.preWriteCleanup(read);
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
                    final K key = ((ReferenceEntry<K, V>)next).getKey();
                    if (((ReferenceEntry)next).getHash() == n && key != null && this.map.keyEquivalence.equivalent(k, key)) {
                        final ValueReference<K, V> valueReference = ((ReferenceEntry<K, V>)next).getValueReference();
                        final V value = valueReference.get();
                        if (loadingValueReference == valueReference || (value == null && valueReference != LocalCache.UNSET)) {
                            ++this.modCount;
                            int count2 = count;
                            if (loadingValueReference.isActive()) {
                                RemovalCause removalCause;
                                if (value == null) {
                                    removalCause = RemovalCause.COLLECTED;
                                }
                                else {
                                    removalCause = RemovalCause.REPLACED;
                                }
                                this.enqueueNotification(k, n, loadingValueReference, removalCause);
                                count2 = count - 1;
                            }
                            this.setValue((ReferenceEntry<K, V>)next, k, v, read);
                            this.count = count2;
                            this.evictEntries();
                            return true;
                        }
                        this.enqueueNotification(k, n, new WeightedStrongValueReference<K, V>(v, 0), RemovalCause.REPLACED);
                        return false;
                    }
                    else {
                        next = ((ReferenceEntry<K, V>)next).getNext();
                    }
                }
                ++this.modCount;
                final ReferenceEntry<Object, V> entry = this.newEntry((Object)k, n, referenceEntry);
                this.setValue((ReferenceEntry<K, V>)entry, k, v, read);
                table.set(n2, (ReferenceEntry<K, V>)entry);
                this.count = count;
                this.evictEntries();
                return true;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
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
        
        void tryExpireEntries(final long n) {
            if (!this.tryLock()) {
                return;
            }
            try {
                this.expireEntries(n);
            }
            finally {
                this.unlock();
            }
        }
    }
    
    static class SoftValueReference<K, V> extends SoftReference<V> implements ValueReference<K, V>
    {
        final ReferenceEntry<K, V> entry;
        
        SoftValueReference(final ReferenceQueue<V> referenceQueue, final V v, final ReferenceEntry<K, V> entry) {
            super(v, referenceQueue);
            this.entry = entry;
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
        public int getWeight() {
            return 1;
        }
        
        @Override
        public boolean isActive() {
            return true;
        }
        
        @Override
        public boolean isLoading() {
            return false;
        }
        
        @Override
        public void notifyNewValue(final V v) {
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
             <K, V> ValueReference<K, V> referenceValue(final Segment<K, V> segment, final ReferenceEntry<K, V> referenceEntry, final V v, final int n) {
                if (n == 1) {
                    return new SoftValueReference<K, V>(segment.valueReferenceQueue, v, referenceEntry);
                }
                return new WeightedSoftValueReference<K, V>(segment.valueReferenceQueue, v, referenceEntry, n);
            }
        }, 
        STRONG {
            @Override
            Equivalence<Object> defaultEquivalence() {
                return Equivalence.equals();
            }
            
            @Override
             <K, V> ValueReference<K, V> referenceValue(final Segment<K, V> segment, final ReferenceEntry<K, V> referenceEntry, final V v, final int n) {
                if (n == 1) {
                    return new StrongValueReference<K, V>(v);
                }
                return new WeightedStrongValueReference<K, V>(v, n);
            }
        }, 
        WEAK {
            @Override
            Equivalence<Object> defaultEquivalence() {
                return Equivalence.identity();
            }
            
            @Override
             <K, V> ValueReference<K, V> referenceValue(final Segment<K, V> segment, final ReferenceEntry<K, V> referenceEntry, final V v, final int n) {
                if (n == 1) {
                    return new WeakValueReference<K, V>(segment.valueReferenceQueue, v, referenceEntry);
                }
                return new WeightedWeakValueReference<K, V>(segment.valueReferenceQueue, v, referenceEntry, n);
            }
        };
        
        abstract Equivalence<Object> defaultEquivalence();
        
        abstract <K, V> ValueReference<K, V> referenceValue(final Segment<K, V> p0, final ReferenceEntry<K, V> p1, final V p2, final int p3);
    }
    
    static final class StrongAccessEntry<K, V> extends StrongEntry<K, V> implements ReferenceEntry<K, V>
    {
        volatile long accessTime;
        ReferenceEntry<K, V> nextAccess;
        ReferenceEntry<K, V> previousAccess;
        
        StrongAccessEntry(final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
            super(k, n, referenceEntry);
            this.accessTime = Long.MAX_VALUE;
            this.nextAccess = LocalCache.nullEntry();
            this.previousAccess = LocalCache.nullEntry();
        }
        
        @Override
        public long getAccessTime() {
            return this.accessTime;
        }
        
        @Override
        public ReferenceEntry<K, V> getNextInAccessQueue() {
            return this.nextAccess;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            return this.previousAccess;
        }
        
        @Override
        public void setAccessTime(final long accessTime) {
            this.accessTime = accessTime;
        }
        
        @Override
        public void setNextInAccessQueue(final ReferenceEntry<K, V> nextAccess) {
            this.nextAccess = nextAccess;
        }
        
        @Override
        public void setPreviousInAccessQueue(final ReferenceEntry<K, V> previousAccess) {
            this.previousAccess = previousAccess;
        }
    }
    
    static final class StrongAccessWriteEntry<K, V> extends StrongEntry<K, V> implements ReferenceEntry<K, V>
    {
        volatile long accessTime;
        ReferenceEntry<K, V> nextAccess;
        ReferenceEntry<K, V> nextWrite;
        ReferenceEntry<K, V> previousAccess;
        ReferenceEntry<K, V> previousWrite;
        volatile long writeTime;
        
        StrongAccessWriteEntry(final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
            super(k, n, referenceEntry);
            this.accessTime = Long.MAX_VALUE;
            this.nextAccess = LocalCache.nullEntry();
            this.previousAccess = LocalCache.nullEntry();
            this.writeTime = Long.MAX_VALUE;
            this.nextWrite = LocalCache.nullEntry();
            this.previousWrite = LocalCache.nullEntry();
        }
        
        @Override
        public long getAccessTime() {
            return this.accessTime;
        }
        
        @Override
        public ReferenceEntry<K, V> getNextInAccessQueue() {
            return this.nextAccess;
        }
        
        @Override
        public ReferenceEntry<K, V> getNextInWriteQueue() {
            return this.nextWrite;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            return this.previousAccess;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            return this.previousWrite;
        }
        
        @Override
        public long getWriteTime() {
            return this.writeTime;
        }
        
        @Override
        public void setAccessTime(final long accessTime) {
            this.accessTime = accessTime;
        }
        
        @Override
        public void setNextInAccessQueue(final ReferenceEntry<K, V> nextAccess) {
            this.nextAccess = nextAccess;
        }
        
        @Override
        public void setNextInWriteQueue(final ReferenceEntry<K, V> nextWrite) {
            this.nextWrite = nextWrite;
        }
        
        @Override
        public void setPreviousInAccessQueue(final ReferenceEntry<K, V> previousAccess) {
            this.previousAccess = previousAccess;
        }
        
        @Override
        public void setPreviousInWriteQueue(final ReferenceEntry<K, V> previousWrite) {
            this.previousWrite = previousWrite;
        }
        
        @Override
        public void setWriteTime(final long writeTime) {
            this.writeTime = writeTime;
        }
    }
    
    static class StrongEntry<K, V> implements ReferenceEntry<K, V>
    {
        final int hash;
        final K key;
        final ReferenceEntry<K, V> next;
        volatile ValueReference<K, V> valueReference;
        
        StrongEntry(final K key, final int hash, final ReferenceEntry<K, V> next) {
            this.valueReference = LocalCache.unset();
            this.key = key;
            this.hash = hash;
            this.next = next;
        }
        
        @Override
        public long getAccessTime() {
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
        public ReferenceEntry<K, V> getNextInAccessQueue() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getNextInWriteQueue() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }
        
        @Override
        public long getWriteTime() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setAccessTime(final long n) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setNextInAccessQueue(final ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setNextInWriteQueue(final ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setPreviousInAccessQueue(final ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setPreviousInWriteQueue(final ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setValueReference(final ValueReference<K, V> valueReference) {
            this.valueReference = valueReference;
        }
        
        @Override
        public void setWriteTime(final long n) {
            throw new UnsupportedOperationException();
        }
    }
    
    static class StrongValueReference<K, V> implements ValueReference<K, V>
    {
        final V referent;
        
        StrongValueReference(final V referent) {
            this.referent = referent;
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
        public int getWeight() {
            return 1;
        }
        
        @Override
        public boolean isActive() {
            return true;
        }
        
        @Override
        public boolean isLoading() {
            return false;
        }
        
        @Override
        public void notifyNewValue(final V v) {
        }
    }
    
    static final class StrongWriteEntry<K, V> extends StrongEntry<K, V> implements ReferenceEntry<K, V>
    {
        ReferenceEntry<K, V> nextWrite;
        ReferenceEntry<K, V> previousWrite;
        volatile long writeTime;
        
        StrongWriteEntry(final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
            super(k, n, referenceEntry);
            this.writeTime = Long.MAX_VALUE;
            this.nextWrite = LocalCache.nullEntry();
            this.previousWrite = LocalCache.nullEntry();
        }
        
        @Override
        public ReferenceEntry<K, V> getNextInWriteQueue() {
            return this.nextWrite;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            return this.previousWrite;
        }
        
        @Override
        public long getWriteTime() {
            return this.writeTime;
        }
        
        @Override
        public void setNextInWriteQueue(final ReferenceEntry<K, V> nextWrite) {
            this.nextWrite = nextWrite;
        }
        
        @Override
        public void setPreviousInWriteQueue(final ReferenceEntry<K, V> previousWrite) {
            this.previousWrite = previousWrite;
        }
        
        @Override
        public void setWriteTime(final long writeTime) {
            this.writeTime = writeTime;
        }
    }
    
    final class ValueIterator extends LocalCache$HashIterator implements Iterator<V>
    {
        public V next() {
            return this.nextEntry().getValue();
        }
    }
    
    interface ValueReference<K, V>
    {
        ValueReference<K, V> copyFor(final ReferenceQueue<V> p0, final V p1, final ReferenceEntry<K, V> p2);
        
        V get();
        
        ReferenceEntry<K, V> getEntry();
        
        int getWeight();
        
        boolean isActive();
        
        boolean isLoading();
        
        void notifyNewValue(final V p0);
    }
    
    final class Values extends AbstractCollection<V>
    {
        @Override
        public void clear() {
            LocalCache.this.clear();
        }
        
        @Override
        public boolean contains(final Object o) {
            return LocalCache.this.containsValue(o);
        }
        
        @Override
        public boolean isEmpty() {
            return LocalCache.this.isEmpty();
        }
        
        @Override
        public Iterator<V> iterator() {
            return new ValueIterator();
        }
        
        @Override
        public int size() {
            return LocalCache.this.size();
        }
    }
    
    static final class WeakAccessEntry<K, V> extends WeakEntry<K, V> implements ReferenceEntry<K, V>
    {
        volatile long accessTime;
        ReferenceEntry<K, V> nextAccess;
        ReferenceEntry<K, V> previousAccess;
        
        WeakAccessEntry(final ReferenceQueue<K> referenceQueue, final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
            super(referenceQueue, k, n, referenceEntry);
            this.accessTime = Long.MAX_VALUE;
            this.nextAccess = LocalCache.nullEntry();
            this.previousAccess = LocalCache.nullEntry();
        }
        
        @Override
        public long getAccessTime() {
            return this.accessTime;
        }
        
        @Override
        public ReferenceEntry<K, V> getNextInAccessQueue() {
            return this.nextAccess;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            return this.previousAccess;
        }
        
        @Override
        public void setAccessTime(final long accessTime) {
            this.accessTime = accessTime;
        }
        
        @Override
        public void setNextInAccessQueue(final ReferenceEntry<K, V> nextAccess) {
            this.nextAccess = nextAccess;
        }
        
        @Override
        public void setPreviousInAccessQueue(final ReferenceEntry<K, V> previousAccess) {
            this.previousAccess = previousAccess;
        }
    }
    
    static final class WeakAccessWriteEntry<K, V> extends WeakEntry<K, V> implements ReferenceEntry<K, V>
    {
        volatile long accessTime;
        ReferenceEntry<K, V> nextAccess;
        ReferenceEntry<K, V> nextWrite;
        ReferenceEntry<K, V> previousAccess;
        ReferenceEntry<K, V> previousWrite;
        volatile long writeTime;
        
        WeakAccessWriteEntry(final ReferenceQueue<K> referenceQueue, final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
            super(referenceQueue, k, n, referenceEntry);
            this.accessTime = Long.MAX_VALUE;
            this.nextAccess = LocalCache.nullEntry();
            this.previousAccess = LocalCache.nullEntry();
            this.writeTime = Long.MAX_VALUE;
            this.nextWrite = LocalCache.nullEntry();
            this.previousWrite = LocalCache.nullEntry();
        }
        
        @Override
        public long getAccessTime() {
            return this.accessTime;
        }
        
        @Override
        public ReferenceEntry<K, V> getNextInAccessQueue() {
            return this.nextAccess;
        }
        
        @Override
        public ReferenceEntry<K, V> getNextInWriteQueue() {
            return this.nextWrite;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            return this.previousAccess;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            return this.previousWrite;
        }
        
        @Override
        public long getWriteTime() {
            return this.writeTime;
        }
        
        @Override
        public void setAccessTime(final long accessTime) {
            this.accessTime = accessTime;
        }
        
        @Override
        public void setNextInAccessQueue(final ReferenceEntry<K, V> nextAccess) {
            this.nextAccess = nextAccess;
        }
        
        @Override
        public void setNextInWriteQueue(final ReferenceEntry<K, V> nextWrite) {
            this.nextWrite = nextWrite;
        }
        
        @Override
        public void setPreviousInAccessQueue(final ReferenceEntry<K, V> previousAccess) {
            this.previousAccess = previousAccess;
        }
        
        @Override
        public void setPreviousInWriteQueue(final ReferenceEntry<K, V> previousWrite) {
            this.previousWrite = previousWrite;
        }
        
        @Override
        public void setWriteTime(final long writeTime) {
            this.writeTime = writeTime;
        }
    }
    
    static class WeakEntry<K, V> extends WeakReference<K> implements ReferenceEntry<K, V>
    {
        final int hash;
        final ReferenceEntry<K, V> next;
        volatile ValueReference<K, V> valueReference;
        
        WeakEntry(final ReferenceQueue<K> referenceQueue, final K k, final int hash, final ReferenceEntry<K, V> next) {
            super(k, referenceQueue);
            this.valueReference = LocalCache.unset();
            this.hash = hash;
            this.next = next;
        }
        
        @Override
        public long getAccessTime() {
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
        public ReferenceEntry<K, V> getNextInAccessQueue() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getNextInWriteQueue() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }
        
        @Override
        public long getWriteTime() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setAccessTime(final long n) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setNextInAccessQueue(final ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setNextInWriteQueue(final ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setPreviousInAccessQueue(final ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setPreviousInWriteQueue(final ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setValueReference(final ValueReference<K, V> valueReference) {
            this.valueReference = valueReference;
        }
        
        @Override
        public void setWriteTime(final long n) {
            throw new UnsupportedOperationException();
        }
    }
    
    static class WeakValueReference<K, V> extends WeakReference<V> implements ValueReference<K, V>
    {
        final ReferenceEntry<K, V> entry;
        
        WeakValueReference(final ReferenceQueue<V> referenceQueue, final V v, final ReferenceEntry<K, V> entry) {
            super(v, referenceQueue);
            this.entry = entry;
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
        public int getWeight() {
            return 1;
        }
        
        @Override
        public boolean isActive() {
            return true;
        }
        
        @Override
        public boolean isLoading() {
            return false;
        }
        
        @Override
        public void notifyNewValue(final V v) {
        }
    }
    
    static final class WeakWriteEntry<K, V> extends WeakEntry<K, V> implements ReferenceEntry<K, V>
    {
        ReferenceEntry<K, V> nextWrite;
        ReferenceEntry<K, V> previousWrite;
        volatile long writeTime;
        
        WeakWriteEntry(final ReferenceQueue<K> referenceQueue, final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
            super(referenceQueue, k, n, referenceEntry);
            this.writeTime = Long.MAX_VALUE;
            this.nextWrite = LocalCache.nullEntry();
            this.previousWrite = LocalCache.nullEntry();
        }
        
        @Override
        public ReferenceEntry<K, V> getNextInWriteQueue() {
            return this.nextWrite;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            return this.previousWrite;
        }
        
        @Override
        public long getWriteTime() {
            return this.writeTime;
        }
        
        @Override
        public void setNextInWriteQueue(final ReferenceEntry<K, V> nextWrite) {
            this.nextWrite = nextWrite;
        }
        
        @Override
        public void setPreviousInWriteQueue(final ReferenceEntry<K, V> previousWrite) {
            this.previousWrite = previousWrite;
        }
        
        @Override
        public void setWriteTime(final long writeTime) {
            this.writeTime = writeTime;
        }
    }
    
    static final class WeightedSoftValueReference<K, V> extends SoftValueReference<K, V>
    {
        final int weight;
        
        WeightedSoftValueReference(final ReferenceQueue<V> referenceQueue, final V v, final ReferenceEntry<K, V> referenceEntry, final int weight) {
            super(referenceQueue, v, referenceEntry);
            this.weight = weight;
        }
        
        @Override
        public ValueReference<K, V> copyFor(final ReferenceQueue<V> referenceQueue, final V v, final ReferenceEntry<K, V> referenceEntry) {
            return new WeightedSoftValueReference((ReferenceQueue<Object>)referenceQueue, v, (ReferenceEntry<Object, Object>)referenceEntry, this.weight);
        }
        
        @Override
        public int getWeight() {
            return this.weight;
        }
    }
    
    static final class WeightedStrongValueReference<K, V> extends StrongValueReference<K, V>
    {
        final int weight;
        
        WeightedStrongValueReference(final V v, final int weight) {
            super(v);
            this.weight = weight;
        }
        
        @Override
        public int getWeight() {
            return this.weight;
        }
    }
    
    static final class WeightedWeakValueReference<K, V> extends WeakValueReference<K, V>
    {
        final int weight;
        
        WeightedWeakValueReference(final ReferenceQueue<V> referenceQueue, final V v, final ReferenceEntry<K, V> referenceEntry, final int weight) {
            super(referenceQueue, v, referenceEntry);
            this.weight = weight;
        }
        
        @Override
        public ValueReference<K, V> copyFor(final ReferenceQueue<V> referenceQueue, final V v, final ReferenceEntry<K, V> referenceEntry) {
            return new WeightedWeakValueReference((ReferenceQueue<Object>)referenceQueue, v, (ReferenceEntry<Object, Object>)referenceEntry, this.weight);
        }
        
        @Override
        public int getWeight() {
            return this.weight;
        }
    }
    
    static final class WriteQueue<K, V> extends AbstractQueue<ReferenceEntry<K, V>>
    {
        final ReferenceEntry<K, V> head;
        
        WriteQueue() {
            this.head = new AbstractReferenceEntry<K, V>() {
                ReferenceEntry<K, V> nextWrite = this;
                ReferenceEntry<K, V> previousWrite = this;
                
                @Override
                public ReferenceEntry<K, V> getNextInWriteQueue() {
                    return this.nextWrite;
                }
                
                @Override
                public ReferenceEntry<K, V> getPreviousInWriteQueue() {
                    return this.previousWrite;
                }
                
                @Override
                public long getWriteTime() {
                    return Long.MAX_VALUE;
                }
                
                @Override
                public void setNextInWriteQueue(final ReferenceEntry<K, V> nextWrite) {
                    this.nextWrite = nextWrite;
                }
                
                @Override
                public void setPreviousInWriteQueue(final ReferenceEntry<K, V> previousWrite) {
                    this.previousWrite = previousWrite;
                }
                
                @Override
                public void setWriteTime(final long n) {
                }
            };
        }
        
        @Override
        public void clear() {
            ReferenceEntry<K, V> nextInWriteQueue2;
            for (ReferenceEntry<K, V> nextInWriteQueue = this.head.getNextInWriteQueue(); nextInWriteQueue != this.head; nextInWriteQueue = nextInWriteQueue2) {
                nextInWriteQueue2 = nextInWriteQueue.getNextInWriteQueue();
                LocalCache.nullifyWriteOrder(nextInWriteQueue);
            }
            this.head.setNextInWriteQueue(this.head);
            this.head.setPreviousInWriteQueue(this.head);
        }
        
        @Override
        public boolean contains(final Object o) {
            return ((ReferenceEntry)o).getNextInWriteQueue() != NullEntry.INSTANCE;
        }
        
        @Override
        public boolean isEmpty() {
            return this.head.getNextInWriteQueue() == this.head;
        }
        
        @Override
        public Iterator<ReferenceEntry<K, V>> iterator() {
            return new AbstractSequentialIterator<ReferenceEntry<K, V>>(this.peek()) {
                @Override
                protected ReferenceEntry<K, V> computeNext(final ReferenceEntry<K, V> referenceEntry) {
                    Object nextInWriteQueue;
                    if ((nextInWriteQueue = referenceEntry.getNextInWriteQueue()) == WriteQueue.this.head) {
                        nextInWriteQueue = null;
                    }
                    return (ReferenceEntry<K, V>)nextInWriteQueue;
                }
            };
        }
        
        @Override
        public boolean offer(final ReferenceEntry<K, V> referenceEntry) {
            LocalCache.connectWriteOrder(referenceEntry.getPreviousInWriteQueue(), referenceEntry.getNextInWriteQueue());
            LocalCache.connectWriteOrder(this.head.getPreviousInWriteQueue(), referenceEntry);
            LocalCache.connectWriteOrder(referenceEntry, this.head);
            return true;
        }
        
        @Override
        public ReferenceEntry<K, V> peek() {
            Object nextInWriteQueue;
            if ((nextInWriteQueue = this.head.getNextInWriteQueue()) == this.head) {
                nextInWriteQueue = null;
            }
            return (ReferenceEntry<K, V>)nextInWriteQueue;
        }
        
        @Override
        public ReferenceEntry<K, V> poll() {
            final ReferenceEntry<K, V> nextInWriteQueue = this.head.getNextInWriteQueue();
            if (nextInWriteQueue == this.head) {
                return null;
            }
            this.remove(nextInWriteQueue);
            return nextInWriteQueue;
        }
        
        @Override
        public boolean remove(final Object o) {
            final ReferenceEntry referenceEntry = (ReferenceEntry)o;
            final ReferenceEntry<K, V> previousInWriteQueue = referenceEntry.getPreviousInWriteQueue();
            final ReferenceEntry<K, V> nextInWriteQueue = referenceEntry.getNextInWriteQueue();
            LocalCache.connectWriteOrder(previousInWriteQueue, nextInWriteQueue);
            LocalCache.nullifyWriteOrder((ReferenceEntry<Object, Object>)referenceEntry);
            return nextInWriteQueue != NullEntry.INSTANCE;
        }
        
        @Override
        public int size() {
            int n = 0;
            for (ReferenceEntry<K, V> referenceEntry = this.head.getNextInWriteQueue(); referenceEntry != this.head; referenceEntry = referenceEntry.getNextInWriteQueue()) {
                ++n;
            }
            return n;
        }
    }
    
    final class WriteThroughEntry implements Entry<K, V>
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
        public V setValue(final V v) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public String toString() {
            return this.getKey() + "=" + this.getValue();
        }
    }
}
