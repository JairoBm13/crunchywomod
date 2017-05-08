// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.base;

import java.io.Serializable;

public abstract class Optional<T> implements Serializable
{
    public static <T> Optional<T> absent() {
        return (Optional<T>)Absent.INSTANCE;
    }
    
    public static <T> Optional<T> fromNullable(final T t) {
        if (t == null) {
            return absent();
        }
        return new Present<T>(t);
    }
    
    public static <T> Optional<T> of(final T t) {
        return new Present<T>(Preconditions.checkNotNull(t));
    }
    
    @Override
    public abstract boolean equals(final Object p0);
    
    public abstract T get();
    
    public abstract boolean isPresent();
    
    public abstract Optional<T> or(final Optional<? extends T> p0);
    
    public abstract T or(final T p0);
    
    public abstract T orNull();
    
    public abstract <V> Optional<V> transform(final Function<? super T, V> p0);
}
