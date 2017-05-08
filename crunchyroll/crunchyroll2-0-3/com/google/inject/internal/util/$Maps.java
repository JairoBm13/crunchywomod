// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal.util;

import java.util.LinkedHashMap;
import java.util.IdentityHashMap;
import java.util.HashMap;
import java.util.Map;

public final class $Maps
{
    public static <K, V> Map.Entry<K, V> immutableEntry(@$Nullable final K k, @$Nullable final V v) {
        return new $ImmutableEntry<K, V>(k, v);
    }
    
    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<K, V>();
    }
    
    public static <K, V> IdentityHashMap<K, V> newIdentityHashMap() {
        return new IdentityHashMap<K, V>();
    }
    
    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
        return new LinkedHashMap<K, V>();
    }
}
