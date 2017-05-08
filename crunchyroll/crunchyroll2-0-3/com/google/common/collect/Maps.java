// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.HashMap;
import java.util.Iterator;
import com.google.common.base.Objects;
import java.util.Map;
import com.google.common.base.Joiner;

public final class Maps
{
    static final Joiner.MapJoiner STANDARD_JOINER;
    
    static {
        STANDARD_JOINER = Collections2.STANDARD_JOINER.withKeyValueSeparator("=");
    }
    
    static boolean containsValueImpl(final Map<?, ?> map, final Object o) {
        final Iterator<Map.Entry<?, Object>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            if (Objects.equal(iterator.next().getValue(), o)) {
                return true;
            }
        }
        return false;
    }
    
    static boolean equalsImpl(final Map<?, ?> map, final Object o) {
        return map == o || (o instanceof Map && map.entrySet().equals(((Map)o).entrySet()));
    }
    
    public static <K, V> Map.Entry<K, V> immutableEntry(final K k, final V v) {
        return new ImmutableEntry<K, V>(k, v);
    }
    
    static <K> K keyOrNull(final Map.Entry<K, ?> entry) {
        if (entry == null) {
            return null;
        }
        return entry.getKey();
    }
    
    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<K, V>();
    }
    
    static String toStringImpl(final Map<?, ?> map) {
        final StringBuilder append = Collections2.newStringBuilderForCollection(map.size()).append('{');
        Maps.STANDARD_JOINER.appendTo(append, map);
        return append.append('}').toString();
    }
    
    static <K, V> UnmodifiableIterator<V> valueIterator(final UnmodifiableIterator<Map.Entry<K, V>> unmodifiableIterator) {
        return new UnmodifiableIterator<V>() {
            @Override
            public boolean hasNext() {
                return unmodifiableIterator.hasNext();
            }
            
            @Override
            public V next() {
                return ((Map.Entry)unmodifiableIterator.next()).getValue();
            }
        };
    }
}
