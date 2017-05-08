// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.cache;

public interface RemovalListener<K, V>
{
    void onRemoval(final RemovalNotification<K, V> p0);
}
