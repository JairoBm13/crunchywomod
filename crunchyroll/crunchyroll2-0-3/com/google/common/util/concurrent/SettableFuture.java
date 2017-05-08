// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.util.concurrent;

public final class SettableFuture<V> extends AbstractFuture<V>
{
    public static <V> SettableFuture<V> create() {
        return new SettableFuture<V>();
    }
    
    public boolean set(final V v) {
        return super.set(v);
    }
    
    public boolean setException(final Throwable exception) {
        return super.setException(exception);
    }
}
