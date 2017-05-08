// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Objects;
import java.util.NoSuchElementException;
import java.util.Iterator;

public final class Iterators
{
    static final UnmodifiableListIterator<Object> EMPTY_LIST_ITERATOR;
    private static final Iterator<Object> EMPTY_MODIFIABLE_ITERATOR;
    
    static {
        EMPTY_LIST_ITERATOR = new UnmodifiableListIterator<Object>() {
            @Override
            public boolean hasNext() {
                return false;
            }
            
            @Override
            public boolean hasPrevious() {
                return false;
            }
            
            @Override
            public Object next() {
                throw new NoSuchElementException();
            }
            
            @Override
            public int nextIndex() {
                return 0;
            }
            
            @Override
            public Object previous() {
                throw new NoSuchElementException();
            }
            
            @Override
            public int previousIndex() {
                return -1;
            }
        };
        EMPTY_MODIFIABLE_ITERATOR = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return false;
            }
            
            @Override
            public Object next() {
                throw new NoSuchElementException();
            }
            
            @Override
            public void remove() {
                throw new IllegalStateException();
            }
        };
    }
    
    public static boolean contains(final Iterator<?> iterator, final Object o) {
        if (o == null) {
            while (iterator.hasNext()) {
                if (iterator.next() == null) {
                    return true;
                }
            }
        }
        else {
            while (iterator.hasNext()) {
                if (o.equals(iterator.next())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean elementsEqual(final Iterator<?> iterator, final Iterator<?> iterator2) {
        while (iterator.hasNext()) {
            if (!iterator2.hasNext()) {
                return false;
            }
            if (!Objects.equal(iterator.next(), iterator2.next())) {
                return false;
            }
        }
        if (!iterator2.hasNext()) {
            return true;
        }
        return false;
    }
    
    public static <T> UnmodifiableIterator<T> emptyIterator() {
        return (UnmodifiableIterator<T>)emptyListIterator();
    }
    
    static <T> UnmodifiableListIterator<T> emptyListIterator() {
        return (UnmodifiableListIterator<T>)Iterators.EMPTY_LIST_ITERATOR;
    }
    
    public static <T> UnmodifiableIterator<T> filter(final Iterator<T> iterator, final Predicate<? super T> predicate) {
        Preconditions.checkNotNull(iterator);
        Preconditions.checkNotNull(predicate);
        return new AbstractIterator<T>() {
            @Override
            protected T computeNext() {
                while (iterator.hasNext()) {
                    final T next = iterator.next();
                    if (predicate.apply(next)) {
                        return next;
                    }
                }
                return this.endOfData();
            }
        };
    }
    
    static <T> UnmodifiableListIterator<T> forArray(final T[] array, final int n, final int n2, final int n3) {
        Preconditions.checkArgument(n2 >= 0);
        Preconditions.checkPositionIndexes(n, n + n2, array.length);
        return new AbstractIndexedListIterator<T>(n2, n3) {
            @Override
            protected T get(final int n) {
                return array[n + n];
            }
        };
    }
    
    public static <T> T getNext(final Iterator<? extends T> iterator, T next) {
        if (iterator.hasNext()) {
            next = (T)iterator.next();
        }
        return next;
    }
    
    public static <T> T getOnlyElement(final Iterator<T> iterator) {
        final T next = iterator.next();
        if (!iterator.hasNext()) {
            return next;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("expected one element but was: <" + next);
        for (int n = 0; n < 4 && iterator.hasNext(); ++n) {
            sb.append(", " + iterator.next());
        }
        if (iterator.hasNext()) {
            sb.append(", ...");
        }
        sb.append('>');
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static <T> boolean removeIf(final Iterator<T> iterator, final Predicate<? super T> predicate) {
        Preconditions.checkNotNull(predicate);
        boolean b = false;
        while (iterator.hasNext()) {
            if (predicate.apply(iterator.next())) {
                iterator.remove();
                b = true;
            }
        }
        return b;
    }
    
    public static <T> UnmodifiableIterator<T> singletonIterator(final T t) {
        return new UnmodifiableIterator<T>() {
            boolean done;
            
            @Override
            public boolean hasNext() {
                return !this.done;
            }
            
            @Override
            public T next() {
                if (this.done) {
                    throw new NoSuchElementException();
                }
                this.done = true;
                return t;
            }
        };
    }
    
    public static String toString(final Iterator<?> iterator) {
        return Joiner.on(", ").useForNull("null").appendTo(new StringBuilder().append('['), iterator).append(']').toString();
    }
    
    public static <F, T> Iterator<T> transform(final Iterator<F> iterator, final Function<? super F, ? extends T> function) {
        Preconditions.checkNotNull(function);
        return new TransformedIterator<F, T>(iterator) {
            @Override
            T transform(final F n) {
                return function.apply(n);
            }
        };
    }
}
