// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.cache;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

public abstract class CacheLoader<K, V>
{
    public abstract V load(final K p0) throws Exception;
    
    public ListenableFuture<V> reload(final K k, final V v) throws Exception {
        return Futures.immediateFuture(this.load(k));
    }
    
    public static final class InvalidCacheLoadException extends RuntimeException
    {
        public InvalidCacheLoadException(final String s) {
            super(s);
        }
    }
}
