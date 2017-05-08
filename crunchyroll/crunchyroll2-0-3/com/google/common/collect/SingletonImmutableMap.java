// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.Map;

final class SingletonImmutableMap<K, V> extends ImmutableMap<K, V>
{
    final transient K singleKey;
    final transient V singleValue;
    
    SingletonImmutableMap(final K singleKey, final V singleValue) {
        this.singleKey = singleKey;
        this.singleValue = singleValue;
    }
    
    SingletonImmutableMap(final Entry<K, V> entry) {
        this(entry.getKey(), entry.getValue());
    }
    
    @Override
    public boolean containsKey(final Object o) {
        return this.singleKey.equals(o);
    }
    
    @Override
    public boolean containsValue(final Object o) {
        return this.singleValue.equals(o);
    }
    
    @Override
    ImmutableSet<Entry<K, V>> createEntrySet() {
        return ImmutableSet.of(Maps.immutableEntry(this.singleKey, this.singleValue));
    }
    
    @Override
    ImmutableSet<K> createKeySet() {
        return ImmutableSet.of(this.singleKey);
    }
    
    @Override
    ImmutableCollection<V> createValues() {
        return ImmutableList.of(this.singleValue);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o != this) {
            if (o instanceof Map) {
                final Map map = (Map)o;
                if (map.size() == 1) {
                    final Map.Entry<Object, V> entry = map.entrySet().iterator().next();
                    if (!this.singleKey.equals(entry.getKey()) || !this.singleValue.equals(entry.getValue())) {
                        return false;
                    }
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    @Override
    public V get(final Object o) {
        if (this.singleKey.equals(o)) {
            return this.singleValue;
        }
        return null;
    }
    
    @Override
    public int hashCode() {
        return this.singleKey.hashCode() ^ this.singleValue.hashCode();
    }
    
    @Override
    public boolean isEmpty() {
        return false;
    }
    
    @Override
    boolean isPartialView() {
        return false;
    }
    
    @Override
    public int size() {
        return 1;
    }
}
