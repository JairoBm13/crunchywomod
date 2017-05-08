// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.Collection;
import java.util.NavigableMap;
import com.google.common.base.Preconditions;
import java.util.Set;
import java.util.Map;
import java.util.Comparator;

final class EmptyImmutableSortedMap<K, V> extends ImmutableSortedMap<K, V>
{
    private final transient ImmutableSortedSet<K> keySet;
    
    EmptyImmutableSortedMap(final Comparator<? super K> comparator) {
        this.keySet = ImmutableSortedSet.emptySet(comparator);
    }
    
    EmptyImmutableSortedMap(final Comparator<? super K> comparator, final ImmutableSortedMap<K, V> immutableSortedMap) {
        super(immutableSortedMap);
        this.keySet = ImmutableSortedSet.emptySet(comparator);
    }
    
    @Override
    ImmutableSortedMap<K, V> createDescendingMap() {
        return new EmptyImmutableSortedMap(Ordering.from(this.comparator()).reverse(), (ImmutableSortedMap<Object, Object>)this);
    }
    
    @Override
    ImmutableSet<Entry<K, V>> createEntrySet() {
        throw new AssertionError((Object)"should never be called");
    }
    
    @Override
    public ImmutableSet<Entry<K, V>> entrySet() {
        return ImmutableSet.of();
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof Map && ((Map)o).isEmpty();
    }
    
    @Override
    public V get(final Object o) {
        return null;
    }
    
    @Override
    public int hashCode() {
        return 0;
    }
    
    @Override
    public ImmutableSortedMap<K, V> headMap(final K k, final boolean b) {
        Preconditions.checkNotNull(k);
        return this;
    }
    
    @Override
    public boolean isEmpty() {
        return true;
    }
    
    @Override
    boolean isPartialView() {
        return false;
    }
    
    @Override
    public ImmutableSortedSet<K> keySet() {
        return this.keySet;
    }
    
    @Override
    public int size() {
        return 0;
    }
    
    @Override
    public ImmutableSortedMap<K, V> tailMap(final K k, final boolean b) {
        Preconditions.checkNotNull(k);
        return this;
    }
    
    @Override
    public String toString() {
        return "{}";
    }
    
    @Override
    public ImmutableCollection<V> values() {
        return (ImmutableCollection<V>)ImmutableList.of();
    }
}
