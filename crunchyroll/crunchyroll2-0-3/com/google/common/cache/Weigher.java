// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.cache;

public interface Weigher<K, V>
{
    int weigh(final K p0, final V p1);
}
