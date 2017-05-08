// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal.util;

import java.util.AbstractCollection;
import java.util.concurrent.locks.ReentrantLock;
import java.util.NoSuchElementException;
import com.google.inject.internal.util.$CustomConcurrentHashMap$Impl$com.google.inject.internal.util.$CustomConcurrentHashMap$Impl$com.google.inject.internal.util.$CustomConcurrentHashMap$Impl$WriteThroughEntry;
import java.lang.reflect.Field;
import java.util.AbstractSet;
import com.google.inject.internal.util.$CustomConcurrentHashMap$Impl$com.google.inject.internal.util.$CustomConcurrentHashMap$Impl$HashIterator;
import java.util.Iterator;
import java.lang.reflect.Array;
import java.util.Collection;
import com.google.inject.internal.util.$CustomConcurrentHashMap$Impl$com.google.inject.internal.util.$CustomConcurrentHashMap$Impl$Segment;
import java.util.Map;
import java.util.Set;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.ConcurrentMap;

final class $CustomConcurrentHashMap
{
    private static int rehash(int n) {
        n += (n << 15 ^ 0xFFFFCD7D);
        n ^= n >>> 10;
        n += n << 3;
        n ^= n >>> 6;
        n += (n << 2) + (n << 14);
        return n >>> 16 ^ n;
    }
    
    static final class Builder
    {
        int concurrencyLevel;
        int initialCapacity;
        float loadFactor;
        
        Builder() {
            this.loadFactor = 0.75f;
            this.initialCapacity = 16;
            this.concurrencyLevel = 16;
        }
        
        public <K, V, E> ConcurrentMap<K, V> buildComputingMap(final ComputingStrategy<K, V, E> computingStrategy, final $Function<? super K, ? extends V> $Function) {
            if (computingStrategy == null) {
                throw new NullPointerException("strategy");
            }
            if ($Function == null) {
                throw new NullPointerException("computer");
            }
            return new ComputingImpl<K, V, Object>(computingStrategy, this, $Function);
        }
    }
    
    static class ComputingImpl<K, V, E> extends Impl<K, V, E>
    {
        final $Function<? super K, ? extends V> computer;
        final ComputingStrategy<K, V, E> computingStrategy;
        
        ComputingImpl(final ComputingStrategy<K, V, E> computingStrategy, final Builder builder, final $Function<? super K, ? extends V> computer) {
            super(computingStrategy, builder);
            this.computingStrategy = computingStrategy;
            this.computer = computer;
        }
        
        @Override
        public V get(Object o) {
            if (o == null) {
                throw new NullPointerException("key");
            }
            final int hash = this.hash(o);
            final Segment segment = this.segmentFor(hash);
            Object o3;
            Object o2;
            while (true) {
                o2 = (o3 = segment.getEntry(o, hash));
                Label_0257: {
                    if (o2 == null) {
                        boolean b = false;
                        segment.lock();
                        try {
                            o3 = (o2 = segment.getEntry(o, hash));
                            if (o3 == null) {
                                b = true;
                                final int count = segment.count;
                                if (count > segment.threshold) {
                                    segment.expand();
                                }
                                o3 = segment.table;
                                final int n = hash & ((AtomicReferenceArray)o3).length() - 1;
                                o2 = ((AtomicReferenceArray<AtomicReferenceArray<AtomicReferenceArray<AtomicReferenceArray>>>)o3).get(n);
                                ++segment.modCount;
                                o2 = this.computingStrategy.newEntry((K)o, hash, (AtomicReferenceArray<AtomicReferenceArray>)o2);
                                ((AtomicReferenceArray<AtomicReferenceArray<AtomicReferenceArray<AtomicReferenceArray>>>)o3).set(n, (AtomicReferenceArray<AtomicReferenceArray<AtomicReferenceArray>>)o2);
                                segment.count = count + 1;
                            }
                            segment.unlock();
                            o3 = o2;
                            if (!b) {
                                break Label_0257;
                            }
                            try {
                                o3 = this.computingStrategy.compute((K)o, (E)o2, this.computer);
                                if (o3 == null) {
                                    throw new NullPointerException("compute() returned null unexpectedly");
                                }
                                break;
                            }
                            finally {
                                if (!false) {
                                    segment.removeEntry((E)o2, hash);
                                }
                            }
                        }
                        finally {
                            segment.unlock();
                        }
                        break;
                    }
                }
                boolean b2 = false;
                while (true) {
                    try {
                        final V waitForValue = this.computingStrategy.waitForValue((E)o3);
                        if (waitForValue != null) {
                            o = waitForValue;
                            return waitForValue;
                        }
                        segment.removeEntry((E)o3, hash);
                    }
                    catch (InterruptedException ex) {
                        b2 = true;
                        continue;
                    }
                    finally {
                        if (b2) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    break;
                }
            }
            o = o3;
            if (!true) {
                segment.removeEntry((E)o2, hash);
                o = o3;
            }
            return (V)o;
        }
    }
    
    public interface ComputingStrategy<K, V, E> extends Strategy<K, V, E>
    {
        V compute(final K p0, final E p1, final $Function<? super K, ? extends V> p2);
        
        V waitForValue(final E p0) throws InterruptedException;
    }
    
    static class Impl<K, V, E> extends AbstractMap<K, V> implements Serializable, ConcurrentMap<K, V>
    {
        Set<Entry<K, V>> entrySet;
        Set<K> keySet;
        final float loadFactor;
        final int segmentMask;
        final int segmentShift;
        final $CustomConcurrentHashMap$Impl$Segment[] segments;
        final Strategy<K, V, E> strategy;
        Collection<V> values;
        
        Impl(final Strategy<K, V, E> strategy, final Builder builder) {
            this.loadFactor = builder.loadFactor;
            final int concurrencyLevel = builder.concurrencyLevel;
            final int initialCapacity = builder.initialCapacity;
            int n = concurrencyLevel;
            if (concurrencyLevel > 65536) {
                n = 65536;
            }
            int n2 = 0;
            int i;
            for (i = 1; i < n; i <<= 1) {
                ++n2;
            }
            this.segmentShift = 32 - n2;
            this.segmentMask = i - 1;
            this.segments = this.newSegmentArray(i);
            int n3;
            if ((n3 = initialCapacity) > 1073741824) {
                n3 = 1073741824;
            }
            int n5;
            final int n4 = n5 = n3 / i;
            if (n4 * i < n3) {
                n5 = n4 + 1;
            }
            int j;
            for (j = 1; j < n5; j <<= 1) {}
            for (int k = 0; k < this.segments.length; ++k) {
                this.segments[k] = new Segment(j);
            }
            (this.strategy = strategy).setInternals(new InternalsImpl());
        }
        
        @Override
        public void clear() {
            final Segment[] segments = this.segments;
            for (int length = segments.length, i = 0; i < length; ++i) {
                segments[i].clear();
            }
        }
        
        @Override
        public boolean containsKey(final Object o) {
            if (o == null) {
                throw new NullPointerException("key");
            }
            final int hash = this.hash(o);
            return this.segmentFor(hash).containsKey(o, hash);
        }
        
        @Override
        public boolean containsValue(final Object o) {
            if (o == null) {
                throw new NullPointerException("value");
            }
            final Segment[] segments = this.segments;
            final int[] array = new int[segments.length];
            for (int i = 0; i < 2; ++i) {
                int n = 0;
                for (int j = 0; j < segments.length; ++j) {
                    final int count = segments[j].count;
                    final int modCount = segments[j].modCount;
                    array[j] = modCount;
                    n += modCount;
                    if (segments[j].containsValue(o)) {
                        return true;
                    }
                }
                boolean b2 = true;
                if (n != 0) {
                    int n2 = 0;
                    while (true) {
                        b2 = b2;
                        if (n2 >= segments.length) {
                            break;
                        }
                        final int count2 = segments[n2].count;
                        if (array[n2] != segments[n2].modCount) {
                            b2 = false;
                            break;
                        }
                        ++n2;
                    }
                }
                if (b2) {
                    return false;
                }
            }
            for (int length = segments.length, k = 0; k < length; ++k) {
                segments[k].lock();
            }
            boolean b = false;
            try {
                final int length2 = segments.length;
                int n3 = 0;
                boolean b3;
                while (true) {
                    b3 = b;
                    if (n3 >= length2) {
                        break;
                    }
                    if (segments[n3].containsValue(o)) {
                        b3 = true;
                        break;
                    }
                    ++n3;
                }
                final int length3 = segments.length;
                int n4 = 0;
                while (true) {
                    b = b3;
                    if (n4 >= length3) {
                        return b;
                    }
                    segments[n4].unlock();
                    ++n4;
                }
            }
            finally {
                for (int length4 = segments.length, l = 0; l < length4; ++l) {
                    segments[l].unlock();
                }
            }
        }
        
        @Override
        public Set<Entry<K, V>> entrySet() {
            final Set<Entry<K, V>> entrySet = this.entrySet;
            if (entrySet != null) {
                return entrySet;
            }
            return this.entrySet = new EntrySet();
        }
        
        @Override
        public V get(final Object o) {
            if (o == null) {
                throw new NullPointerException("key");
            }
            final int hash = this.hash(o);
            return this.segmentFor(hash).get(o, hash);
        }
        
        int hash(final Object o) {
            return rehash(this.strategy.hashKey(o));
        }
        
        @Override
        public boolean isEmpty() {
            final Segment[] segments = this.segments;
            final int[] array = new int[segments.length];
            int n = 0;
            for (int i = 0; i < segments.length; ++i) {
                if (segments[i].count != 0) {
                    return false;
                }
                final int modCount = segments[i].modCount;
                array[i] = modCount;
                n += modCount;
            }
            if (n != 0) {
                for (int j = 0; j < segments.length; ++j) {
                    if (segments[j].count != 0 || array[j] != segments[j].modCount) {
                        return false;
                    }
                }
            }
            return true;
        }
        
        @Override
        public Set<K> keySet() {
            final Set<K> keySet = this.keySet;
            if (keySet != null) {
                return keySet;
            }
            return this.keySet = new KeySet();
        }
        
        $CustomConcurrentHashMap$Impl$Segment[] newSegmentArray(final int n) {
            return ($CustomConcurrentHashMap$Impl$Segment[])Array.newInstance(Segment.class, n);
        }
        
        @Override
        public V put(final K k, final V v) {
            if (k == null) {
                throw new NullPointerException("key");
            }
            if (v == null) {
                throw new NullPointerException("value");
            }
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
            if (k == null) {
                throw new NullPointerException("key");
            }
            if (v == null) {
                throw new NullPointerException("value");
            }
            final int hash = this.hash(k);
            return this.segmentFor(hash).put(k, hash, v, true);
        }
        
        @Override
        public V remove(final Object o) {
            if (o == null) {
                throw new NullPointerException("key");
            }
            final int hash = this.hash(o);
            return this.segmentFor(hash).remove(o, hash);
        }
        
        @Override
        public boolean remove(final Object o, final Object o2) {
            if (o == null) {
                throw new NullPointerException("key");
            }
            final int hash = this.hash(o);
            return this.segmentFor(hash).remove(o, hash, o2);
        }
        
        @Override
        public V replace(final K k, final V v) {
            if (k == null) {
                throw new NullPointerException("key");
            }
            if (v == null) {
                throw new NullPointerException("value");
            }
            final int hash = this.hash(k);
            return this.segmentFor(hash).replace(k, hash, v);
        }
        
        @Override
        public boolean replace(final K k, final V v, final V v2) {
            if (k == null) {
                throw new NullPointerException("key");
            }
            if (v == null) {
                throw new NullPointerException("oldValue");
            }
            if (v2 == null) {
                throw new NullPointerException("newValue");
            }
            final int hash = this.hash(k);
            return this.segmentFor(hash).replace(k, hash, v, v2);
        }
        
        $CustomConcurrentHashMap$Impl$Segment segmentFor(final int n) {
            return ($CustomConcurrentHashMap$Impl$Segment)this.segments[n >>> this.segmentShift & this.segmentMask];
        }
        
        @Override
        public int size() {
            final Segment[] segments = this.segments;
            long n = 0L;
            long n2 = 0L;
            final int[] array = new int[segments.length];
            for (int i = 0; i < 2; ++i) {
                long n3 = 0L;
                n = 0L;
                int n4 = 0;
                for (int j = 0; j < segments.length; ++j) {
                    n += segments[j].count;
                    final int modCount = segments[j].modCount;
                    array[j] = modCount;
                    n4 += modCount;
                }
                n2 = n3;
                if (n4 != 0) {
                    int n5 = 0;
                    while (true) {
                        n2 = n3;
                        if (n5 >= segments.length) {
                            break;
                        }
                        n3 += segments[n5].count;
                        if (array[n5] != segments[n5].modCount) {
                            n2 = -1L;
                            break;
                        }
                        ++n5;
                    }
                }
                if (n2 == n) {
                    break;
                }
            }
            long n6 = n;
            if (n2 != n) {
                long n7 = 0L;
                for (int length = segments.length, k = 0; k < length; ++k) {
                    segments[k].lock();
                }
                for (int length2 = segments.length, l = 0; l < length2; ++l) {
                    n7 += segments[l].count;
                }
                final int length3 = segments.length;
                int n8 = 0;
                while (true) {
                    n6 = n7;
                    if (n8 >= length3) {
                        break;
                    }
                    segments[n8].unlock();
                    ++n8;
                }
            }
            if (n6 > 2147483647L) {
                return Integer.MAX_VALUE;
            }
            return (int)n6;
        }
        
        @Override
        public Collection<V> values() {
            final Collection<V> values = this.values;
            if (values != null) {
                return values;
            }
            return this.values = new Values();
        }
        
        final class EntryIterator extends $CustomConcurrentHashMap$Impl$HashIterator implements Iterator<Entry<K, V>>
        {
            public Entry<K, V> next() {
                return this.nextEntry();
            }
        }
        
        final class EntrySet extends AbstractSet<Entry<K, V>>
        {
            @Override
            public void clear() {
                Impl.this.clear();
            }
            
            @Override
            public boolean contains(final Object o) {
                if (o instanceof Entry) {
                    final Entry entry = (Entry)o;
                    final Object key = entry.getKey();
                    if (key != null) {
                        final V value = Impl.this.get(key);
                        if (value != null && Impl.this.strategy.equalValues(value, entry.getValue())) {
                            return true;
                        }
                    }
                }
                return false;
            }
            
            @Override
            public boolean isEmpty() {
                return Impl.this.isEmpty();
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
                    if (key != null && Impl.this.remove(key, entry.getValue())) {
                        return true;
                    }
                }
                return false;
            }
            
            @Override
            public int size() {
                return Impl.this.size();
            }
        }
        
        static class Fields
        {
            static final Field loadFactor;
            static final Field segmentMask;
            static final Field segmentShift;
            static final Field segments;
            static final Field strategy;
            
            static {
                loadFactor = findField("loadFactor");
                segmentShift = findField("segmentShift");
                segmentMask = findField("segmentMask");
                segments = findField("segments");
                strategy = findField("strategy");
            }
            
            static Field findField(final String s) {
                try {
                    final Field declaredField = Impl.class.getDeclaredField(s);
                    declaredField.setAccessible(true);
                    return declaredField;
                }
                catch (NoSuchFieldException ex) {
                    throw new AssertionError((Object)ex);
                }
            }
        }
        
        abstract class HashIterator
        {
            AtomicReferenceArray<E> currentTable;
            $CustomConcurrentHashMap$Impl$WriteThroughEntry lastReturned;
            E nextEntry;
            $CustomConcurrentHashMap$Impl$WriteThroughEntry nextExternal;
            int nextSegmentIndex;
            int nextTableIndex;
            
            HashIterator() {
                this.nextSegmentIndex = Impl.this.segments.length - 1;
                this.nextTableIndex = -1;
                this.advance();
            }
            
            final void advance() {
                this.nextExternal = null;
                if (!this.nextInChain() && !this.nextInTable()) {
                    while (this.nextSegmentIndex >= 0) {
                        final Segment segment = Impl.this.segments[this.nextSegmentIndex--];
                        if (segment.count != 0) {
                            this.currentTable = segment.table;
                            this.nextTableIndex = this.currentTable.length() - 1;
                            if (this.nextInTable()) {
                                return;
                            }
                            continue;
                        }
                    }
                }
            }
            
            boolean advanceTo(final E e) {
                final Strategy<K, V, E> strategy = Impl.this.strategy;
                final K key = strategy.getKey(e);
                final V value = strategy.getValue(e);
                if (key != null && value != null) {
                    this.nextExternal = new WriteThroughEntry(key, value);
                    return true;
                }
                return false;
            }
            
            public boolean hasNext() {
                return this.nextExternal != null;
            }
            
            com.google.inject.internal.util.$CustomConcurrentHashMap$Impl$com.google.inject.internal.util.$CustomConcurrentHashMap$Impl$WriteThroughEntry nextEntry() {
                if (this.nextExternal == null) {
                    throw new NoSuchElementException();
                }
                this.lastReturned = this.nextExternal;
                this.advance();
                return (com.google.inject.internal.util.$CustomConcurrentHashMap$Impl$com.google.inject.internal.util.$CustomConcurrentHashMap$Impl$WriteThroughEntry)this.lastReturned;
            }
            
            boolean nextInChain() {
                final Strategy<K, V, E> strategy = Impl.this.strategy;
                if (this.nextEntry != null) {
                    this.nextEntry = strategy.getNext(this.nextEntry);
                    while (this.nextEntry != null) {
                        if (this.advanceTo(this.nextEntry)) {
                            return true;
                        }
                        this.nextEntry = strategy.getNext(this.nextEntry);
                    }
                }
                return false;
            }
            
            boolean nextInTable() {
                while (this.nextTableIndex >= 0) {
                    final E value = this.currentTable.get(this.nextTableIndex--);
                    this.nextEntry = value;
                    if (value != null && (this.advanceTo(this.nextEntry) || this.nextInChain())) {
                        return true;
                    }
                }
                return false;
            }
            
            public void remove() {
                if (this.lastReturned == null) {
                    throw new IllegalStateException();
                }
                Impl.this.remove(this.lastReturned.getKey());
                this.lastReturned = null;
            }
        }
        
        class InternalsImpl implements Internals<K, V, E>, Serializable
        {
            @Override
            public boolean removeEntry(final E e) {
                if (e == null) {
                    throw new NullPointerException("entry");
                }
                final int hash = Impl.this.strategy.getHash(e);
                return Impl.this.segmentFor(hash).removeEntry(e, hash);
            }
            
            @Override
            public boolean removeEntry(final E e, final V v) {
                if (e == null) {
                    throw new NullPointerException("entry");
                }
                final int hash = Impl.this.strategy.getHash(e);
                return Impl.this.segmentFor(hash).removeEntry(e, hash, v);
            }
        }
        
        final class KeyIterator extends $CustomConcurrentHashMap$Impl$HashIterator implements Iterator<K>
        {
            public K next() {
                return super.nextEntry().getKey();
            }
        }
        
        final class KeySet extends AbstractSet<K>
        {
            @Override
            public void clear() {
                Impl.this.clear();
            }
            
            @Override
            public boolean contains(final Object o) {
                return Impl.this.containsKey(o);
            }
            
            @Override
            public boolean isEmpty() {
                return Impl.this.isEmpty();
            }
            
            @Override
            public Iterator<K> iterator() {
                return new KeyIterator();
            }
            
            @Override
            public boolean remove(final Object o) {
                return Impl.this.remove(o) != null;
            }
            
            @Override
            public int size() {
                return Impl.this.size();
            }
        }
        
        final class Segment extends ReentrantLock
        {
            volatile int count;
            int modCount;
            volatile AtomicReferenceArray<E> table;
            int threshold;
            
            Segment(final int n) {
                this.setTable(this.newEntryArray(n));
            }
            
            void clear() {
                if (this.count == 0) {
                    return;
                }
                this.lock();
                try {
                    final AtomicReferenceArray<E> table = this.table;
                    for (int i = 0; i < table.length(); ++i) {
                        table.set(i, null);
                    }
                    ++this.modCount;
                    this.count = 0;
                }
                finally {
                    this.unlock();
                }
            }
            
            boolean containsKey(final Object o, final int n) {
                final boolean b = false;
                final Strategy<K, V, E> strategy = Impl.this.strategy;
                boolean b2 = b;
                if (this.count != 0) {
                    E e = this.getFirst(n);
                    while (true) {
                        b2 = b;
                        if (e == null) {
                            break;
                        }
                        if (strategy.getHash(e) == n) {
                            final K key = strategy.getKey(e);
                            if (key != null && strategy.equalKeys(key, o)) {
                                b2 = b;
                                if (strategy.getValue(e) != null) {
                                    b2 = true;
                                    break;
                                }
                                break;
                            }
                        }
                        e = strategy.getNext(e);
                    }
                }
                return b2;
            }
            
            boolean containsValue(final Object o) {
                final Strategy<K, V, E> strategy = Impl.this.strategy;
                if (this.count != 0) {
                    final AtomicReferenceArray<E> table = this.table;
                    for (int length = table.length(), i = 0; i < length; ++i) {
                        for (E e = table.get(i); e != null; e = strategy.getNext(e)) {
                            final V value = strategy.getValue(e);
                            if (value != null && strategy.equalValues(value, o)) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
            
            void expand() {
                final AtomicReferenceArray<E> table = this.table;
                final int length = table.length();
                if (length >= 1073741824) {
                    return;
                }
                final Strategy<K, V, E> strategy = Impl.this.strategy;
                final AtomicReferenceArray<E> entryArray = this.newEntryArray(length << 1);
                this.threshold = (int)(entryArray.length() * Impl.this.loadFactor);
                final int n = entryArray.length() - 1;
                for (int i = 0; i < length; ++i) {
                    final E value = table.get(i);
                    if (value != null) {
                        E e = strategy.getNext(value);
                        int n2 = strategy.getHash(value) & n;
                        if (e == null) {
                            entryArray.set(n2, value);
                        }
                        else {
                            E e2 = value;
                            while (e != null) {
                                final int n3 = strategy.getHash(e) & n;
                                int n4;
                                if (n3 != (n4 = n2)) {
                                    n4 = n3;
                                    e2 = e;
                                }
                                e = strategy.getNext(e);
                                n2 = n4;
                            }
                            entryArray.set(n2, e2);
                            for (E next = value; next != e2; next = strategy.getNext(next)) {
                                final K key = strategy.getKey(next);
                                if (key != null) {
                                    final int n5 = strategy.getHash(next) & n;
                                    entryArray.set(n5, strategy.copyEntry(key, next, entryArray.get(n5)));
                                }
                            }
                        }
                    }
                }
                this.table = entryArray;
            }
            
            V get(Object entry, final int n) {
                entry = this.getEntry(entry, n);
                if (entry == null) {
                    return null;
                }
                return Impl.this.strategy.getValue((E)entry);
            }
            
            public E getEntry(final Object o, final int n) {
                final Strategy<K, V, E> strategy = Impl.this.strategy;
                if (this.count != 0) {
                    for (E e = this.getFirst(n); e != null; e = strategy.getNext(e)) {
                        if (strategy.getHash(e) == n) {
                            final K key = strategy.getKey(e);
                            if (key != null && strategy.equalKeys(key, o)) {
                                return e;
                            }
                        }
                    }
                }
                return null;
            }
            
            E getFirst(final int n) {
                final AtomicReferenceArray<E> table = this.table;
                return table.get(table.length() - 1 & n);
            }
            
            AtomicReferenceArray<E> newEntryArray(final int n) {
                return new AtomicReferenceArray<E>(n);
            }
            
            V put(final K k, final int n, final V v, final boolean b) {
                final Strategy<K, V, E> strategy = Impl.this.strategy;
                this.lock();
                try {
                    final int count = this.count;
                    if (count > this.threshold) {
                        this.expand();
                    }
                    final AtomicReferenceArray<E> table = this.table;
                    final int n2 = n & table.length() - 1;
                    E e2;
                    final E e = e2 = table.get(n2);
                    while (e2 != null) {
                        final K key = strategy.getKey(e2);
                        if (strategy.getHash(e2) == n && key != null && strategy.equalKeys(k, key)) {
                            final V value = strategy.getValue(e2);
                            if (b && value != null) {
                                return value;
                            }
                            strategy.setValue(e2, v);
                            return value;
                        }
                        else {
                            e2 = strategy.getNext(e2);
                        }
                    }
                    ++this.modCount;
                    final E entry = strategy.newEntry(k, n, e);
                    strategy.setValue(entry, v);
                    table.set(n2, entry);
                    this.count = count + 1;
                    return null;
                }
                finally {
                    this.unlock();
                }
            }
            
            V remove(Object next, final int n) {
                final Strategy<K, V, E> strategy = Impl.this.strategy;
                this.lock();
                try {
                    final int count = this.count;
                    final AtomicReferenceArray<E> table = this.table;
                    final int n2 = n & table.length() - 1;
                    E e;
                    for (Object o = e = table.get(n2); e != null; e = strategy.getNext(e)) {
                        final K key = strategy.getKey(e);
                        if (strategy.getHash(e) == n && key != null && strategy.equalKeys(key, next)) {
                            final V value = Impl.this.strategy.getValue(e);
                            ++this.modCount;
                            final E next2 = strategy.getNext(e);
                            next = o;
                            E e2 = next2;
                            while (next != e) {
                                final K key2 = strategy.getKey((E)next);
                                E copyEntry = e2;
                                if (key2 != null) {
                                    copyEntry = strategy.copyEntry(key2, (E)next, e2);
                                }
                                next = strategy.getNext((E)next);
                                e2 = copyEntry;
                            }
                            table.set(n2, e2);
                            this.count = count - 1;
                            return value;
                        }
                    }
                    return null;
                }
                finally {
                    this.unlock();
                }
            }
            
            boolean remove(Object o, final int n, Object next) {
                final Strategy<K, V, E> strategy = Impl.this.strategy;
                this.lock();
                try {
                    final int count = this.count;
                    final AtomicReferenceArray<E> table = this.table;
                    final int n2 = n & table.length() - 1;
                    E e;
                    final Object o2 = e = table.get(n2);
                    while (e != null) {
                        final K key = strategy.getKey(e);
                        if (strategy.getHash(e) == n && key != null && strategy.equalKeys(key, o)) {
                            o = Impl.this.strategy.getValue(e);
                            if (next == o || (next != null && o != null && strategy.equalValues((V)o, next))) {
                                ++this.modCount;
                                next = strategy.getNext(e);
                                K key2;
                                Object copyEntry;
                                for (o = o2; o != e; o = strategy.getNext((E)o), next = copyEntry) {
                                    key2 = strategy.getKey((E)o);
                                    copyEntry = next;
                                    if (key2 != null) {
                                        copyEntry = strategy.copyEntry(key2, (E)o, (E)next);
                                    }
                                }
                                table.set(n2, (E)next);
                                this.count = count - 1;
                                return true;
                            }
                            return false;
                        }
                        else {
                            e = strategy.getNext(e);
                        }
                    }
                    return false;
                }
                finally {
                    this.unlock();
                }
            }
            
            public boolean removeEntry(final E e, final int n) {
                final Strategy<K, V, E> strategy = Impl.this.strategy;
                this.lock();
                try {
                    final int count = this.count;
                    final AtomicReferenceArray<E> table = this.table;
                    final int n2 = n & table.length() - 1;
                    E e3;
                    for (E e2 = e3 = table.get(n2); e3 != null; e3 = strategy.getNext(e3)) {
                        if (strategy.getHash(e3) == n && e.equals(e3)) {
                            ++this.modCount;
                            final E next = strategy.getNext(e3);
                            E next2 = e2;
                            E e4 = next;
                            while (next2 != e3) {
                                final K key = strategy.getKey(next2);
                                E copyEntry = e4;
                                if (key != null) {
                                    copyEntry = strategy.copyEntry(key, next2, e4);
                                }
                                next2 = strategy.getNext(next2);
                                e4 = copyEntry;
                            }
                            table.set(n2, e4);
                            this.count = count - 1;
                            return true;
                        }
                    }
                    return false;
                }
                finally {
                    this.unlock();
                }
            }
            
            public boolean removeEntry(final E e, final int n, final V v) {
                final Strategy<K, V, E> strategy = Impl.this.strategy;
                this.lock();
                try {
                    final int count = this.count;
                    final AtomicReferenceArray<E> table = this.table;
                    final int n2 = n & table.length() - 1;
                    E e3;
                    final E e2 = e3 = table.get(n2);
                    while (e3 != null) {
                        if (strategy.getHash(e3) == n && e.equals(e3)) {
                            final V value = strategy.getValue(e3);
                            if (value == v || (v != null && strategy.equalValues(value, v))) {
                                ++this.modCount;
                                E next = strategy.getNext(e3);
                                E copyEntry;
                                for (E next2 = e2; next2 != e3; next2 = strategy.getNext(next2), next = copyEntry) {
                                    final K key = strategy.getKey(next2);
                                    copyEntry = next;
                                    if (key != null) {
                                        copyEntry = strategy.copyEntry(key, next2, next);
                                    }
                                }
                                table.set(n2, next);
                                this.count = count - 1;
                                return true;
                            }
                            return false;
                        }
                        else {
                            e3 = strategy.getNext(e3);
                        }
                    }
                    return false;
                }
                finally {
                    this.unlock();
                }
            }
            
            V replace(final K k, final int n, final V v) {
                final Strategy<K, V, E> strategy = Impl.this.strategy;
                this.lock();
                try {
                    E e = this.getFirst(n);
                    while (e != null) {
                        final K key = strategy.getKey(e);
                        if (strategy.getHash(e) == n && key != null && strategy.equalKeys(k, key)) {
                            final V value = strategy.getValue(e);
                            if (value == null) {
                                return null;
                            }
                            strategy.setValue(e, v);
                            return value;
                        }
                        else {
                            e = strategy.getNext(e);
                        }
                    }
                    return null;
                }
                finally {
                    this.unlock();
                }
            }
            
            boolean replace(final K k, final int n, final V v, final V v2) {
                final Strategy<K, V, E> strategy = Impl.this.strategy;
                this.lock();
                try {
                    for (E e = this.getFirst(n); e != null; e = strategy.getNext(e)) {
                        final K key = strategy.getKey(e);
                        if (strategy.getHash(e) == n && key != null && strategy.equalKeys(k, key)) {
                            final V value = strategy.getValue(e);
                            if (value == null) {
                                return false;
                            }
                            if (strategy.equalValues(value, v)) {
                                strategy.setValue(e, v2);
                                return true;
                            }
                        }
                    }
                    return false;
                }
                finally {
                    this.unlock();
                }
            }
            
            void setTable(final AtomicReferenceArray<E> table) {
                this.threshold = (int)(table.length() * Impl.this.loadFactor);
                this.table = table;
            }
        }
        
        final class ValueIterator extends $CustomConcurrentHashMap$Impl$HashIterator implements Iterator<V>
        {
            public V next() {
                return super.nextEntry().getValue();
            }
        }
        
        final class Values extends AbstractCollection<V>
        {
            @Override
            public void clear() {
                Impl.this.clear();
            }
            
            @Override
            public boolean contains(final Object o) {
                return Impl.this.containsValue(o);
            }
            
            @Override
            public boolean isEmpty() {
                return Impl.this.isEmpty();
            }
            
            @Override
            public Iterator<V> iterator() {
                return new ValueIterator();
            }
            
            @Override
            public int size() {
                return Impl.this.size();
            }
        }
        
        final class WriteThroughEntry extends $AbstractMapEntry<K, V>
        {
            final K key;
            V value;
            
            WriteThroughEntry(final K key, final V value) {
                this.key = key;
                this.value = value;
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
            public V setValue(final V value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                final Object put = Impl.this.put(this.getKey(), value);
                this.value = value;
                return (V)put;
            }
        }
    }
    
    public interface Internals<K, V, E>
    {
        boolean removeEntry(final E p0);
        
        boolean removeEntry(final E p0, @$Nullable final V p1);
    }
    
    static class SimpleInternalEntry<K, V>
    {
        final int hash;
        final K key;
        final SimpleInternalEntry<K, V> next;
        volatile V value;
        
        SimpleInternalEntry(final K key, final int hash, @$Nullable final V value, final SimpleInternalEntry<K, V> next) {
            this.key = key;
            this.hash = hash;
            this.value = value;
            this.next = next;
        }
    }
    
    static class SimpleStrategy<K, V> implements Strategy<K, V, SimpleInternalEntry<K, V>>
    {
        public SimpleInternalEntry<K, V> copyEntry(final K k, final SimpleInternalEntry<K, V> simpleInternalEntry, final SimpleInternalEntry<K, V> simpleInternalEntry2) {
            return (SimpleInternalEntry<K, V>)new SimpleInternalEntry(k, simpleInternalEntry.hash, simpleInternalEntry.value, (SimpleInternalEntry<Object, Object>)simpleInternalEntry2);
        }
        
        @Override
        public boolean equalKeys(final K k, final Object o) {
            return k.equals(o);
        }
        
        @Override
        public boolean equalValues(final V v, final Object o) {
            return v.equals(o);
        }
        
        public int getHash(final SimpleInternalEntry<K, V> simpleInternalEntry) {
            return simpleInternalEntry.hash;
        }
        
        public K getKey(final SimpleInternalEntry<K, V> simpleInternalEntry) {
            return simpleInternalEntry.key;
        }
        
        public SimpleInternalEntry<K, V> getNext(final SimpleInternalEntry<K, V> simpleInternalEntry) {
            return simpleInternalEntry.next;
        }
        
        public V getValue(final SimpleInternalEntry<K, V> simpleInternalEntry) {
            return simpleInternalEntry.value;
        }
        
        @Override
        public int hashKey(final Object o) {
            return o.hashCode();
        }
        
        public SimpleInternalEntry<K, V> newEntry(final K k, final int n, final SimpleInternalEntry<K, V> simpleInternalEntry) {
            return (SimpleInternalEntry<K, V>)new SimpleInternalEntry(k, n, null, (SimpleInternalEntry<Object, Object>)simpleInternalEntry);
        }
        
        @Override
        public void setInternals(final Internals<K, V, SimpleInternalEntry<K, V>> internals) {
        }
        
        public void setValue(final SimpleInternalEntry<K, V> simpleInternalEntry, final V value) {
            simpleInternalEntry.value = value;
        }
    }
    
    public interface Strategy<K, V, E>
    {
        E copyEntry(final K p0, final E p1, final E p2);
        
        boolean equalKeys(final K p0, final Object p1);
        
        boolean equalValues(final V p0, final Object p1);
        
        int getHash(final E p0);
        
        K getKey(final E p0);
        
        E getNext(final E p0);
        
        V getValue(final E p0);
        
        int hashKey(final Object p0);
        
        E newEntry(final K p0, final int p1, final E p2);
        
        void setInternals(final Internals<K, V, E> p0);
        
        void setValue(final E p0, final V p1);
    }
}
