// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal.util;

import java.util.NoSuchElementException;
import java.util.ListIterator;
import java.util.Iterator;

public final class $Iterators
{
    static final Iterator<Object> EMPTY_ITERATOR;
    private static final ListIterator<Object> EMPTY_LIST_ITERATOR;
    
    static {
        EMPTY_ITERATOR = new $UnmodifiableIterator<Object>() {
            @Override
            public boolean hasNext() {
                return false;
            }
            
            @Override
            public Object next() {
                throw new NoSuchElementException();
            }
        };
        EMPTY_LIST_ITERATOR = new ListIterator<Object>() {
            @Override
            public void add(final Object o) {
                throw new UnsupportedOperationException();
            }
            
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
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public void set(final Object o) {
                throw new UnsupportedOperationException();
            }
        };
    }
    
    public static <T> Iterator<T> concat(final Iterator<? extends Iterator<? extends T>> iterator) {
        $Preconditions.checkNotNull(iterator);
        return new Iterator<T>() {
            Iterator<? extends T> current = $Iterators.emptyIterator();
            Iterator<? extends T> removeFrom;
            
            @Override
            public boolean hasNext() {
                while (!this.current.hasNext() && iterator.hasNext()) {
                    this.current = iterator.next();
                }
                return this.current.hasNext();
            }
            
            @Override
            public T next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                this.removeFrom = this.current;
                return (T)this.current.next();
            }
            
            @Override
            public void remove() {
                $Preconditions.checkState(this.removeFrom != null, (Object)"no calls to next() since last call to remove()");
                this.removeFrom.remove();
                this.removeFrom = null;
            }
        };
    }
    
    public static <T> $UnmodifiableIterator<T> emptyIterator() {
        return ($UnmodifiableIterator<T>)($UnmodifiableIterator)$Iterators.EMPTY_ITERATOR;
    }
    
    public static <T> ListIterator<T> emptyListIterator() {
        return (ListIterator<T>)$Iterators.EMPTY_LIST_ITERATOR;
    }
    
    public static <T> $UnmodifiableIterator<T> forArray(final T... array) {
        return new $UnmodifiableIterator<T>() {
            int i = 0;
            final int length = array.length;
            
            @Override
            public boolean hasNext() {
                return this.i < this.length;
            }
            
            @Override
            public T next() {
                try {
                    final Object o = array[this.i];
                    ++this.i;
                    return (T)o;
                }
                catch (ArrayIndexOutOfBoundsException ex) {
                    throw new NoSuchElementException();
                }
            }
        };
    }
    
    public static <T> $UnmodifiableIterator<T> forArray(final T[] array, final int n, int n2) {
        $Preconditions.checkArgument(n2 >= 0);
        n2 += n;
        $Preconditions.checkPositionIndexes(n, n2, array.length);
        return new $UnmodifiableIterator<T>() {
            int i = n;
            
            @Override
            public boolean hasNext() {
                return this.i < n2;
            }
            
            @Override
            public T next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                return array[this.i++];
            }
        };
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
        sb.append(">");
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static <T> $UnmodifiableIterator<T> singletonIterator(@$Nullable final T t) {
        return new $UnmodifiableIterator<T>() {
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
        if (!iterator.hasNext()) {
            return "[]";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append('[').append(iterator.next());
        while (iterator.hasNext()) {
            sb.append(", ").append(iterator.next());
        }
        return sb.append(']').toString();
    }
    
    public static <F, T> Iterator<T> transform(final Iterator<F> iterator, final $Function<? super F, ? extends T> $Function) {
        $Preconditions.checkNotNull(iterator);
        $Preconditions.checkNotNull($Function);
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
            
            @Override
            public T next() {
                return $Function.apply(iterator.next());
            }
            
            @Override
            public void remove() {
                iterator.remove();
            }
        };
    }
    
    public static <T> $UnmodifiableIterator<T> unmodifiableIterator(final Iterator<T> iterator) {
        $Preconditions.checkNotNull(iterator);
        return new $UnmodifiableIterator<T>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
            
            @Override
            public T next() {
                return iterator.next();
            }
        };
    }
}
