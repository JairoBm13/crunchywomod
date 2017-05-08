// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.cache;

public interface Cache<K, V>
{
    V getIfPresent(final Object p0);
    
    void invalidate(final Object p0);
    
    void invalidateAll();
    
    void put(final K p0, final V p1);
}
