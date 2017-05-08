// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import com.google.common.base.Function;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;
import java.util.Iterator;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;

public final class Iterables
{
    public static <T> Iterable<T> filter(final Iterable<T> iterable, final Predicate<? super T> predicate) {
        Preconditions.checkNotNull(iterable);
        Preconditions.checkNotNull(predicate);
        return new FluentIterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return (Iterator<T>)Iterators.filter(iterable.iterator(), predicate);
            }
        };
    }
    
    public static <T> T getFirst(final Iterable<? extends T> iterable, final T t) {
        return Iterators.getNext(iterable.iterator(), t);
    }
    
    public static <T> T getOnlyElement(final Iterable<T> iterable) {
        return Iterators.getOnlyElement(iterable.iterator());
    }
    
    public static <T> boolean removeIf(final Iterable<T> iterable, final Predicate<? super T> predicate) {
        if (iterable instanceof RandomAccess && iterable instanceof List) {
            return removeIfFromRandomAccessList((List<Object>)iterable, Preconditions.checkNotNull(predicate));
        }
        return Iterators.removeIf((Iterator<T>)iterable.iterator(), (Predicate<? super T>)predicate);
    }
    
    private static <T> boolean removeIfFromRandomAccessList(final List<T> list, final Predicate<? super T> predicate) {
        int i = 0;
        int n = 0;
        while (i < list.size()) {
            final T value = list.get(i);
            int n2 = n;
            Label_0057: {
                if (predicate.apply(value)) {
                    break Label_0057;
                }
                Label_0052: {
                    if (i <= n) {
                        break Label_0052;
                    }
                    try {
                        list.set(n, value);
                        n2 = n + 1;
                        ++i;
                        n = n2;
                        continue;
                    }
                    catch (UnsupportedOperationException ex) {
                        slowRemoveIfForRemainingElements(list, predicate, n, i);
                    }
                }
            }
            return true;
        }
        list.subList(n, list.size()).clear();
        if (i == n) {
            return false;
        }
        return true;
    }
    
    private static <T> void slowRemoveIfForRemainingElements(final List<T> list, final Predicate<? super T> predicate, final int n, int i) {
        for (int j = list.size() - 1; j > i; --j) {
            if (predicate.apply(list.get(j))) {
                list.remove(j);
            }
        }
        for (--i; i >= n; --i) {
            list.remove(i);
        }
    }
    
    static Object[] toArray(final Iterable<?> iterable) {
        return toCollection(iterable).toArray();
    }
    
    private static <E> Collection<E> toCollection(final Iterable<E> iterable) {
        if (iterable instanceof Collection) {
            return (Collection<E>)iterable;
        }
        return (Collection<E>)Lists.newArrayList(iterable.iterator());
    }
    
    public static String toString(final Iterable<?> iterable) {
        return Iterators.toString(iterable.iterator());
    }
    
    public static <F, T> Iterable<T> transform(final Iterable<F> iterable, final Function<? super F, ? extends T> function) {
        Preconditions.checkNotNull(iterable);
        Preconditions.checkNotNull(function);
        return new FluentIterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return Iterators.transform(iterable.iterator(), (Function<? super Object, ? extends T>)function);
            }
        };
    }
}
