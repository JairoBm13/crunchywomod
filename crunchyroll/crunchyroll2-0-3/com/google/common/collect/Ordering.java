// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;
import com.google.common.base.Function;
import java.util.Iterator;
import java.util.Arrays;
import java.util.Collections;
import com.google.common.base.Preconditions;
import java.util.List;
import java.util.Comparator;

public abstract class Ordering<T> implements Comparator<T>
{
    static final int LEFT_IS_GREATER = 1;
    static final int RIGHT_IS_GREATER = -1;
    
    public static Ordering<Object> allEqual() {
        return AllEqualOrdering.INSTANCE;
    }
    
    public static Ordering<Object> arbitrary() {
        return ArbitraryOrderingHolder.ARBITRARY_ORDERING;
    }
    
    public static <T> Ordering<T> compound(final Iterable<? extends Comparator<? super T>> iterable) {
        return new CompoundOrdering<T>(iterable);
    }
    
    public static <T> Ordering<T> explicit(final T t, final T... array) {
        return explicit((List<T>)Lists.asList(t, (T[])array));
    }
    
    public static <T> Ordering<T> explicit(final List<T> list) {
        return new ExplicitOrdering<T>(list);
    }
    
    @Deprecated
    public static <T> Ordering<T> from(final Ordering<T> ordering) {
        return Preconditions.checkNotNull(ordering);
    }
    
    public static <T> Ordering<T> from(final Comparator<T> comparator) {
        if (comparator instanceof Ordering) {
            return (Ordering<T>)comparator;
        }
        return new ComparatorOrdering<T>((Comparator<Object>)comparator);
    }
    
    public static <C extends Comparable> Ordering<C> natural() {
        return (Ordering<C>)NaturalOrdering.INSTANCE;
    }
    
    private <E extends T> int partition(final E[] array, int i, final int n, int n2) {
        final T t = array[n2];
        array[n2] = array[n];
        array[n] = (E)t;
        n2 = i;
        while (i < n) {
            int n3 = n2;
            if (this.compare(array[i], t) < 0) {
                ObjectArrays.swap(array, n2, i);
                n3 = n2 + 1;
            }
            ++i;
            n2 = n3;
        }
        ObjectArrays.swap(array, n, n2);
        return n2;
    }
    
    private <E extends T> void quicksortLeastK(final E[] array, final int n, final int n2, final int n3) {
        if (n2 > n) {
            final int partition = this.partition(array, n, n2, n + n2 >>> 1);
            this.quicksortLeastK((Object[])array, n, partition - 1, n3);
            if (partition < n3) {
                this.quicksortLeastK((Object[])array, partition + 1, n2, n3);
            }
        }
    }
    
    public static Ordering<Object> usingToString() {
        return UsingToStringOrdering.INSTANCE;
    }
    
    public int binarySearch(final List<? extends T> list, final T t) {
        return Collections.binarySearch(list, t, this);
    }
    
    @Override
    public abstract int compare(final T p0, final T p1);
    
    public <U extends T> Ordering<U> compound(final Comparator<? super U> comparator) {
        return new CompoundOrdering<U>(this, Preconditions.checkNotNull(comparator));
    }
    
    public <E extends T> List<E> greatestOf(final Iterable<E> iterable, final int n) {
        return (List<E>)this.reverse().leastOf((Iterable<Object>)iterable, n);
    }
    
    public <E extends T> ImmutableList<E> immutableSortedCopy(final Iterable<E> iterable) {
        final Object[] array = Iterables.toArray(iterable);
        for (int length = array.length, i = 0; i < length; ++i) {
            Preconditions.checkNotNull(array[i]);
        }
        Arrays.sort(array, (Comparator<? super Object>)this);
        return (ImmutableList<E>)ImmutableList.asImmutableList(array);
    }
    
    public boolean isOrdered(final Iterable<? extends T> iterable) {
        final Iterator<? extends T> iterator = iterable.iterator();
        if (iterator.hasNext()) {
            T next = (T)iterator.next();
            while (iterator.hasNext()) {
                final T next2 = (T)iterator.next();
                if (this.compare(next, next2) > 0) {
                    return false;
                }
                next = next2;
            }
        }
        return true;
    }
    
    public boolean isStrictlyOrdered(final Iterable<? extends T> iterable) {
        final Iterator<? extends T> iterator = iterable.iterator();
        if (iterator.hasNext()) {
            T next = (T)iterator.next();
            while (iterator.hasNext()) {
                final T next2 = (T)iterator.next();
                if (this.compare(next, next2) >= 0) {
                    return false;
                }
                next = next2;
            }
        }
        return true;
    }
    
    public <E extends T> List<E> leastOf(final Iterable<E> iterable, final int n) {
        Preconditions.checkArgument(n >= 0, "%d is negative", n);
        Object[] array = Iterables.toArray(iterable);
        if (array.length <= n) {
            Arrays.sort(array, (Comparator<? super Object>)this);
        }
        else {
            this.quicksortLeastK(array, 0, array.length - 1, n);
            final Object[] array2 = new Object[n];
            System.arraycopy(array, 0, array2, 0, n);
            array = array2;
        }
        return (List<E>)Collections.unmodifiableList((List<?>)Arrays.asList((T[])array));
    }
    
    public <S extends T> Ordering<Iterable<S>> lexicographical() {
        return (Ordering<Iterable<S>>)new LexicographicalOrdering((Ordering<? super Object>)this);
    }
    
    public <E extends T> E max(final Iterable<E> iterable) {
        return this.max(iterable.iterator());
    }
    
    public <E extends T> E max(final E e, final E e2) {
        if (this.compare(e, e2) >= 0) {
            return e;
        }
        return e2;
    }
    
    public <E extends T> E max(final E e, final E e2, final E e3, final E... array) {
        T t = this.max(this.max(e, e2), e3);
        for (int length = array.length, i = 0; i < length; ++i) {
            t = this.max(t, array[i]);
        }
        return (E)t;
    }
    
    public <E extends T> E max(final Iterator<E> iterator) {
        T t = iterator.next();
        while (iterator.hasNext()) {
            t = this.max(t, iterator.next());
        }
        return (E)t;
    }
    
    public <E extends T> E min(final Iterable<E> iterable) {
        return this.min(iterable.iterator());
    }
    
    public <E extends T> E min(final E e, final E e2) {
        if (this.compare(e, e2) <= 0) {
            return e;
        }
        return e2;
    }
    
    public <E extends T> E min(final E e, final E e2, final E e3, final E... array) {
        T t = this.min(this.min(e, e2), e3);
        for (int length = array.length, i = 0; i < length; ++i) {
            t = this.min(t, array[i]);
        }
        return (E)t;
    }
    
    public <E extends T> E min(final Iterator<E> iterator) {
        T t = iterator.next();
        while (iterator.hasNext()) {
            t = this.min(t, iterator.next());
        }
        return (E)t;
    }
    
    public <S extends T> Ordering<S> nullsFirst() {
        return new NullsFirstOrdering<S>(this);
    }
    
    public <S extends T> Ordering<S> nullsLast() {
        return new NullsLastOrdering<S>(this);
    }
    
    public <F> Ordering<F> onResultOf(final Function<F, ? extends T> function) {
        return (Ordering<F>)new ByFunctionOrdering((Function<Object, ?>)function, (Ordering<Object>)this);
    }
    
    public <S extends T> Ordering<S> reverse() {
        return new ReverseOrdering<S>(this);
    }
    
    public <E extends T> List<E> sortedCopy(final Iterable<E> iterable) {
        final Object[] array = Iterables.toArray(iterable);
        Arrays.sort(array, (Comparator<? super Object>)this);
        return (List<E>)Lists.newArrayList((Iterable<?>)Arrays.asList(array));
    }
    
    static class ArbitraryOrdering extends Ordering<Object>
    {
        private Map<Object, Integer> uids;
        
        ArbitraryOrdering() {
            this.uids = (Map<Object, Integer>)Platform.tryWeakKeys(new MapMaker()).makeComputingMap((Function<? super Object, ?>)new Function<Object, Integer>() {
                final AtomicInteger counter = new AtomicInteger(0);
                
                @Override
                public Integer apply(final Object o) {
                    return this.counter.getAndIncrement();
                }
            });
        }
        
        @Override
        public int compare(final Object o, final Object o2) {
            final int n = -1;
            int n2;
            if (o == o2) {
                n2 = 0;
            }
            else {
                n2 = n;
                if (o != null) {
                    if (o2 == null) {
                        return 1;
                    }
                    final int identityHashCode = this.identityHashCode(o);
                    final int identityHashCode2 = this.identityHashCode(o2);
                    if (identityHashCode != identityHashCode2) {
                        n2 = n;
                        if (identityHashCode >= identityHashCode2) {
                            return 1;
                        }
                    }
                    else {
                        final int compareTo = this.uids.get(o).compareTo(this.uids.get(o2));
                        if (compareTo == 0) {
                            throw new AssertionError();
                        }
                        return compareTo;
                    }
                }
            }
            return n2;
        }
        
        int identityHashCode(final Object o) {
            return System.identityHashCode(o);
        }
        
        @Override
        public String toString() {
            return "Ordering.arbitrary()";
        }
    }
    
    private static class ArbitraryOrderingHolder
    {
        static final Ordering<Object> ARBITRARY_ORDERING;
        
        static {
            ARBITRARY_ORDERING = new ArbitraryOrdering();
        }
    }
    
    static class IncomparableValueException extends ClassCastException
    {
        final Object value;
        
        IncomparableValueException(final Object value) {
            super("Cannot compare value: " + value);
            this.value = value;
        }
    }
}
