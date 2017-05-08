// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.Collection;
import java.util.Set;
import java.util.Map;

final class EmptyImmutableMap extends ImmutableMap<Object, Object>
{
    static final EmptyImmutableMap INSTANCE;
    
    static {
        INSTANCE = new EmptyImmutableMap();
    }
    
    @Override
    public boolean containsKey(final Object o) {
        return false;
    }
    
    @Override
    public boolean containsValue(final Object o) {
        return false;
    }
    
    @Override
    ImmutableSet<Entry<Object, Object>> createEntrySet() {
        throw new AssertionError((Object)"should never be called");
    }
    
    @Override
    public ImmutableSet<Entry<Object, Object>> entrySet() {
        return ImmutableSet.of();
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof Map && ((Map)o).isEmpty();
    }
    
    @Override
    public Object get(final Object o) {
        return null;
    }
    
    @Override
    public int hashCode() {
        return 0;
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
    public ImmutableSet<Object> keySet() {
        return ImmutableSet.of();
    }
    
    @Override
    public int size() {
        return 0;
    }
    
    @Override
    public String toString() {
        return "{}";
    }
    
    @Override
    public ImmutableCollection<Object> values() {
        return ImmutableCollection.EMPTY_IMMUTABLE_COLLECTION;
    }
}
