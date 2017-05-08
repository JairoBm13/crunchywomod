// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.Collection;
import com.google.common.base.Preconditions;
import java.util.SortedMap;
import java.util.Set;
import java.util.NavigableSet;
import java.util.Map;
import java.util.Comparator;
import java.util.NavigableMap;

public abstract class ImmutableSortedMap<K, V> extends ImmutableSortedMapFauxverideShim<K, V> implements NavigableMap<K, V>
{
    private static final ImmutableSortedMap<Comparable, Object> NATURAL_EMPTY_MAP;
    private static final Comparator<Comparable> NATURAL_ORDER;
    private transient ImmutableSortedMap<K, V> descendingMap;
    
    static {
        NATURAL_ORDER = Ordering.natural();
        NATURAL_EMPTY_MAP = new EmptyImmutableSortedMap<Comparable, Object>(ImmutableSortedMap.NATURAL_ORDER);
    }
    
    ImmutableSortedMap() {
    }
    
    ImmutableSortedMap(final ImmutableSortedMap<K, V> descendingMap) {
        this.descendingMap = descendingMap;
    }
    
    @Override
    public Entry<K, V> ceilingEntry(final K k) {
        return this.tailMap(k, true).firstEntry();
    }
    
    @Override
    public K ceilingKey(final K k) {
        return Maps.keyOrNull((Entry<K, ?>)this.ceilingEntry((K)k));
    }
    
    @Override
    public Comparator<? super K> comparator() {
        return this.keySet().comparator();
    }
    
    @Override
    public boolean containsValue(final Object o) {
        return this.values().contains(o);
    }
    
    abstract ImmutableSortedMap<K, V> createDescendingMap();
    
    @Override
    public ImmutableSortedSet<K> descendingKeySet() {
        return this.keySet().descendingSet();
    }
    
    @Override
    public ImmutableSortedMap<K, V> descendingMap() {
        ImmutableSortedMap<K, V> descendingMap;
        if ((descendingMap = this.descendingMap) == null) {
            descendingMap = this.createDescendingMap();
            this.descendingMap = descendingMap;
        }
        return descendingMap;
    }
    
    @Override
    public ImmutableSet<Entry<K, V>> entrySet() {
        return super.entrySet();
    }
    
    @Override
    public Entry<K, V> firstEntry() {
        if (this.isEmpty()) {
            return null;
        }
        return (Entry<K, V>)this.entrySet().asList().get(0);
    }
    
    @Override
    public K firstKey() {
        return this.keySet().first();
    }
    
    @Override
    public Entry<K, V> floorEntry(final K k) {
        return this.headMap(k, true).lastEntry();
    }
    
    @Override
    public K floorKey(final K k) {
        return Maps.keyOrNull((Entry<K, ?>)this.floorEntry((K)k));
    }
    
    @Override
    public ImmutableSortedMap<K, V> headMap(final K k) {
        return this.headMap(k, false);
    }
    
    @Override
    public abstract ImmutableSortedMap<K, V> headMap(final K p0, final boolean p1);
    
    @Override
    public Entry<K, V> higherEntry(final K k) {
        return this.tailMap(k, false).firstEntry();
    }
    
    @Override
    public K higherKey(final K k) {
        return Maps.keyOrNull((Entry<K, ?>)this.higherEntry((K)k));
    }
    
    @Override
    boolean isPartialView() {
        return this.keySet().isPartialView() || this.values().isPartialView();
    }
    
    @Override
    public abstract ImmutableSortedSet<K> keySet();
    
    @Override
    public Entry<K, V> lastEntry() {
        if (this.isEmpty()) {
            return null;
        }
        return (Entry<K, V>)this.entrySet().asList().get(this.size() - 1);
    }
    
    @Override
    public K lastKey() {
        return this.keySet().last();
    }
    
    @Override
    public Entry<K, V> lowerEntry(final K k) {
        return this.headMap(k, false).lastEntry();
    }
    
    @Override
    public K lowerKey(final K k) {
        return Maps.keyOrNull((Entry<K, ?>)this.lowerEntry((K)k));
    }
    
    @Override
    public ImmutableSortedSet<K> navigableKeySet() {
        return this.keySet();
    }
    
    @Override
    public final Entry<K, V> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final Entry<K, V> pollLastEntry() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public int size() {
        return this.values().size();
    }
    
    @Override
    public ImmutableSortedMap<K, V> subMap(final K k, final K i) {
        return this.subMap(k, true, i, false);
    }
    
    @Override
    public ImmutableSortedMap<K, V> subMap(final K k, final boolean b, final K i, final boolean b2) {
        Preconditions.checkNotNull(k);
        Preconditions.checkNotNull(i);
        Preconditions.checkArgument(this.comparator().compare((Object)k, (Object)i) <= 0, "expected fromKey <= toKey but %s > %s", k, i);
        return this.headMap(i, b2).tailMap(k, b);
    }
    
    @Override
    public ImmutableSortedMap<K, V> tailMap(final K k) {
        return this.tailMap(k, true);
    }
    
    @Override
    public abstract ImmutableSortedMap<K, V> tailMap(final K p0, final boolean p1);
    
    @Override
    public abstract ImmutableCollection<V> values();
}
