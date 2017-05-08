// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Collection;
import java.io.Serializable;

public abstract class $ImmutableCollection<E> implements Serializable, Collection<E>
{
    private static final Object[] EMPTY_ARRAY;
    static final $ImmutableCollection<Object> EMPTY_IMMUTABLE_COLLECTION;
    private static final $UnmodifiableIterator<Object> EMPTY_ITERATOR;
    
    static {
        EMPTY_IMMUTABLE_COLLECTION = new EmptyImmutableCollection();
        EMPTY_ARRAY = new Object[0];
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
    }
    
    @Override
    public final boolean add(final E e) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final boolean addAll(final Collection<? extends E> collection) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final void clear() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean contains(@$Nullable final Object o) {
        if (o != null) {
            final Iterator iterator = this.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().equals(o)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public boolean containsAll(final Collection<?> collection) {
        final Iterator<?> iterator = collection.iterator();
        while (iterator.hasNext()) {
            if (!this.contains(iterator.next())) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public abstract $UnmodifiableIterator<E> iterator();
    
    @Override
    public final boolean remove(final Object o) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final boolean removeAll(final Collection<?> collection) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final boolean retainAll(final Collection<?> collection) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Object[] toArray() {
        return this.toArray(new Object[this.size()]);
    }
    
    @Override
    public <T> T[] toArray(final T[] array) {
        final int size = this.size();
        T[] array2;
        if (array.length < size) {
            array2 = $ObjectArrays.newArray(array, size);
        }
        else {
            array2 = array;
            if (array.length > size) {
                array[size] = null;
                array2 = array;
            }
        }
        int n = 0;
        final Iterator iterator = this.iterator();
        while (iterator.hasNext()) {
            array2[n] = iterator.next();
            ++n;
        }
        return array2;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(this.size() * 16);
        sb.append('[');
        final $UnmodifiableIterator<Object> iterator = ($UnmodifiableIterator<Object>)this.iterator();
        if (iterator.hasNext()) {
            sb.append(iterator.next());
        }
        while (iterator.hasNext()) {
            sb.append(", ");
            sb.append(iterator.next());
        }
        return sb.append(']').toString();
    }
    
    private static class ArrayImmutableCollection<E> extends $ImmutableCollection<E>
    {
        private final E[] elements;
        
        @Override
        public boolean isEmpty() {
            return false;
        }
        
        @Override
        public $UnmodifiableIterator<E> iterator() {
            return new $UnmodifiableIterator<E>() {
                int i = 0;
                
                @Override
                public boolean hasNext() {
                    return this.i < ArrayImmutableCollection.this.elements.length;
                }
                
                @Override
                public E next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    return (E)ArrayImmutableCollection.this.elements[this.i++];
                }
            };
        }
        
        @Override
        public int size() {
            return this.elements.length;
        }
    }
    
    private static class EmptyImmutableCollection extends $ImmutableCollection<Object>
    {
        @Override
        public boolean contains(@$Nullable final Object o) {
            return false;
        }
        
        @Override
        public boolean isEmpty() {
            return true;
        }
        
        @Override
        public $UnmodifiableIterator<Object> iterator() {
            return $ImmutableCollection.EMPTY_ITERATOR;
        }
        
        @Override
        public int size() {
            return 0;
        }
        
        @Override
        public Object[] toArray() {
            return $ImmutableCollection.EMPTY_ARRAY;
        }
        
        @Override
        public <T> T[] toArray(final T[] array) {
            if (array.length > 0) {
                array[0] = null;
            }
            return array;
        }
    }
    
    private static class SerializedForm implements Serializable
    {
    }
}
