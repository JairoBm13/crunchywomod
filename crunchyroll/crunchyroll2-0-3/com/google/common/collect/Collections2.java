// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import com.google.common.base.Function;
import java.util.Iterator;
import com.google.common.base.Preconditions;
import java.util.Collection;
import com.google.common.base.Joiner;

public final class Collections2
{
    static final Joiner STANDARD_JOINER;
    
    static {
        STANDARD_JOINER = Joiner.on(", ").useForNull("null");
    }
    
    static <T> Collection<T> cast(final Iterable<T> iterable) {
        return (Collection<T>)iterable;
    }
    
    static boolean containsAllImpl(final Collection<?> collection, final Collection<?> collection2) {
        Preconditions.checkNotNull(collection);
        final Iterator<?> iterator = collection2.iterator();
        while (iterator.hasNext()) {
            if (!collection.contains(iterator.next())) {
                return false;
            }
        }
        return true;
    }
    
    static StringBuilder newStringBuilderForCollection(final int n) {
        Preconditions.checkArgument(n >= 0, (Object)"size must be non-negative");
        return new StringBuilder((int)Math.min(n * 8L, 1073741824L));
    }
    
    static String toStringImpl(final Collection<?> collection) {
        final StringBuilder append = newStringBuilderForCollection(collection.size()).append('[');
        Collections2.STANDARD_JOINER.appendTo(append, Iterables.transform((Iterable<Object>)collection, (Function<? super Object, ?>)new Function<Object, Object>() {
            @Override
            public Object apply(final Object o) {
                Object o2 = o;
                if (o == collection) {
                    o2 = "(this Collection)";
                }
                return o2;
            }
        }));
        return append.append(']').toString();
    }
}
