// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import com.google.common.base.Preconditions;
import java.util.Map;
import java.io.Serializable;

public abstract class ImmutableMap<K, V> implements Serializable, Map<K, V>
{
    private transient ImmutableSet<Entry<K, V>> entrySet;
    private transient ImmutableSet<K> keySet;
    private transient ImmutableCollection<V> values;
    
    public static <K, V> Builder<K, V> builder() {
        return new Builder<K, V>();
    }
    
    public static <K, V> ImmutableMap<K, V> copyOf(final Map<? extends K, ? extends V> map) {
        if (map instanceof ImmutableMap && !(map instanceof ImmutableSortedMap)) {
            final ImmutableMap<K, V> immutableMap = (ImmutableMap<K, V>)map;
            if (!immutableMap.isPartialView()) {
                return immutableMap;
            }
        }
        final Entry[] array = map.entrySet().toArray((Entry[])new Entry[0]);
        switch (array.length) {
            default: {
                for (int i = 0; i < array.length; ++i) {
                    array[i] = (Entry)entryOf(array[i].getKey(), array[i].getValue());
                }
                return new RegularImmutableMap<K, V>((Entry<?, ?>[])array);
            }
            case 0: {
                return of();
            }
            case 1: {
                return new SingletonImmutableMap<K, V>((Entry<Object, Object>)entryOf(array[0].getKey(), array[0].getValue()));
            }
        }
    }
    
    static <K, V> Entry<K, V> entryOf(final K k, final V v) {
        Preconditions.checkNotNull(k, "null key in entry: null=%s", v);
        Preconditions.checkNotNull(v, "null value in entry: %s=null", k);
        return Maps.immutableEntry(k, v);
    }
    
    public static <K, V> ImmutableMap<K, V> of() {
        return (ImmutableMap<K, V>)EmptyImmutableMap.INSTANCE;
    }
    
    public static <K, V> ImmutableMap<K, V> of(final K k, final V v) {
        return new SingletonImmutableMap<K, V>(Preconditions.checkNotNull(k), Preconditions.checkNotNull(v));
    }
    
    public static <K, V> ImmutableMap<K, V> of(final K k, final V v, final K i, final V v2) {
        return new RegularImmutableMap<K, V>((Entry<?, ?>[])new Entry[] { entryOf(k, v), entryOf(i, v2) });
    }
    
    @Override
    public final void clear() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean containsKey(final Object o) {
        return this.get(o) != null;
    }
    
    @Override
    public boolean containsValue(final Object o) {
        return o != null && Maps.containsValueImpl(this, o);
    }
    
    abstract ImmutableSet<Entry<K, V>> createEntrySet();
    
    ImmutableSet<K> createKeySet() {
        return (ImmutableSet<K>)new ImmutableMapKeySet<K, V>(this.entrySet()) {
            @Override
            ImmutableMap<K, V> map() {
                return ImmutableMap.this;
            }
        };
    }
    
    ImmutableCollection<V> createValues() {
        return (ImmutableCollection<V>)new ImmutableMapValues<K, V>() {
            @Override
            ImmutableMap<K, V> map() {
                return ImmutableMap.this;
            }
        };
    }
    
    @Override
    public ImmutableSet<Entry<K, V>> entrySet() {
        ImmutableSet<Entry<K, V>> entrySet;
        if ((entrySet = this.entrySet) == null) {
            entrySet = this.createEntrySet();
            this.entrySet = entrySet;
        }
        return entrySet;
    }
    
    @Override
    public boolean equals(final Object o) {
        return Maps.equalsImpl(this, o);
    }
    
    @Override
    public abstract V get(final Object p0);
    
    @Override
    public int hashCode() {
        return this.entrySet().hashCode();
    }
    
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    abstract boolean isPartialView();
    
    @Override
    public ImmutableSet<K> keySet() {
        ImmutableSet<K> keySet;
        if ((keySet = this.keySet) == null) {
            keySet = this.createKeySet();
            this.keySet = keySet;
        }
        return keySet;
    }
    
    @Override
    public final V put(final K k, final V v) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final void putAll(final Map<? extends K, ? extends V> map) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final V remove(final Object o) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String toString() {
        return Maps.toStringImpl(this);
    }
    
    @Override
    public ImmutableCollection<V> values() {
        ImmutableCollection<V> values;
        if ((values = this.values) == null) {
            values = this.createValues();
            this.values = values;
        }
        return values;
    }
    
    public static class Builder<K, V>
    {
        final ArrayList<Entry<K, V>> entries;
        
        public Builder() {
            this.entries = Lists.newArrayList();
        }
        
        private static <K, V> ImmutableMap<K, V> fromEntryList(final List<Entry<K, V>> list) {
            switch (list.size()) {
                default: {
                    return new RegularImmutableMap<K, V>((Entry<?, ?>[])list.toArray((Entry<?, ?>[])new Entry[list.size()]));
                }
                case 0: {
                    return ImmutableMap.of();
                }
                case 1: {
                    return new SingletonImmutableMap<K, V>(Iterables.getOnlyElement((Iterable<Entry>)list));
                }
            }
        }
        
        public ImmutableMap<K, V> build() {
            return fromEntryList(this.entries);
        }
        
        public Builder<K, V> put(final K k, final V v) {
            this.entries.add(ImmutableMap.entryOf(k, v));
            return this;
        }
        
        public Builder<K, V> putAll(final Map<? extends K, ? extends V> map) {
            this.entries.ensureCapacity(this.entries.size() + map.size());
            for (final Entry<? extends K, ? extends V> entry : map.entrySet()) {
                this.put(entry.getKey(), (V)entry.getValue());
            }
            return this;
        }
    }
}
