// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.internal.util.$Sets;
import com.google.inject.internal.util.$SourceProvider;
import com.google.inject.internal.util.$Maps;
import com.google.inject.Key;
import java.util.Set;
import java.util.Map;

final class WeakKeySet
{
    private Map<String, Set<Object>> backingSet;
    
    public void add(final Key<?> key, final Object o) {
        if (this.backingSet == null) {
            this.backingSet = (Map<String, Set<Object>>)$Maps.newHashMap();
        }
        Object o2 = null;
        if (o instanceof Class || (o2 = o) == $SourceProvider.UNKNOWN_SOURCE) {
            o2 = null;
        }
        final String string = key.toString();
        Set<Object> linkedHashSet;
        if ((linkedHashSet = this.backingSet.get(string)) == null) {
            linkedHashSet = $Sets.newLinkedHashSet();
            this.backingSet.put(string, linkedHashSet);
        }
        linkedHashSet.add(Errors.convert(o2));
    }
    
    public boolean contains(final Key<?> key) {
        return this.backingSet != null && this.backingSet.containsKey(key.toString());
    }
    
    public Set<Object> getSources(final Key<?> key) {
        return this.backingSet.get(key.toString());
    }
}
