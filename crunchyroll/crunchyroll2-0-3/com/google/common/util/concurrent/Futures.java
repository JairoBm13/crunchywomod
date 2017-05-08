// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.util.concurrent;

import java.util.Arrays;
import com.google.common.base.Function;
import java.lang.reflect.Constructor;
import com.google.common.collect.Ordering;

public final class Futures
{
    private static final AsyncFunction<ListenableFuture<Object>, Object> DEREFERENCER;
    private static final Ordering<Constructor<?>> WITH_STRING_PARAM_FIRST;
    
    static {
        DEREFERENCER = new AsyncFunction<ListenableFuture<Object>, Object>() {};
        WITH_STRING_PARAM_FIRST = Ordering.natural().onResultOf((Function<Object, ? extends Comparable>)new Function<Constructor<?>, Boolean>() {
            @Override
            public Boolean apply(final Constructor<?> constructor) {
                return Arrays.asList((Class<?>[])constructor.getParameterTypes()).contains(String.class);
            }
        }).reverse();
    }
    
    public static <V> ListenableFuture<V> immediateFuture(final V v) {
        final SettableFuture<V> create = SettableFuture.create();
        create.set(v);
        return create;
    }
}
