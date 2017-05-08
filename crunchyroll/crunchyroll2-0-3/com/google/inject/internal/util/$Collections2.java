// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal.util;

import java.util.Collection;
import java.util.Set;

public final class $Collections2
{
    static boolean setEquals(final Set<?> set, @$Nullable final Object o) {
        if (o != set) {
            if (!(o instanceof Set)) {
                return false;
            }
            final Set set2 = (Set)o;
            if (set.size() != set2.size() || !set.containsAll(set2)) {
                return false;
            }
        }
        return true;
    }
    
    static <E> Collection<E> toCollection(final Iterable<E> iterable) {
        if (iterable instanceof Collection) {
            return (Collection<E>)iterable;
        }
        return (Collection<E>)$Lists.newArrayList((Iterable<?>)iterable);
    }
}
