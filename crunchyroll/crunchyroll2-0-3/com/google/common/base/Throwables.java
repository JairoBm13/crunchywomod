// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.base;

public final class Throwables
{
    public static <X extends Throwable> void propagateIfInstanceOf(final Throwable t, final Class<X> clazz) throws X, Throwable {
        if (t != null && clazz.isInstance(t)) {
            throw clazz.cast(t);
        }
    }
}
