// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.Iterator;
import java.util.Collection;
import java.io.Serializable;

public abstract class ImmutableCollection<E> implements Serializable, Collection<E>
{
    static final ImmutableCollection<Object> EMPTY_IMMUTABLE_COLLECTION;
    private transient ImmutableList<E> asList;
    
    static {
        EMPTY_IMMUTABLE_COLLECTION = new EmptyImmutableCollection();
    }
    
    @Override
    public final boolean add(final E e) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final boolean addAll(final Collection<? extends E> collection) {
        throw new UnsupportedOperationException();
    }
    
    public ImmutableList<E> asList() {
        ImmutableList<E> asList;
        if ((asList = this.asList) == null) {
            asList = this.createAsList();
            this.asList = asList;
        }
        return asList;
    }
    
    @Override
    public final void clear() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean contains(final Object o) {
        return o != null && Iterators.contains(this.iterator(), o);
    }
    
    @Override
    public boolean containsAll(final Collection<?> collection) {
        return Collections2.containsAllImpl(this, collection);
    }
    
    ImmutableList<E> createAsList() {
        switch (this.size()) {
            default: {
                return new RegularImmutableAsList<E>(this, this.toArray());
            }
            case 0: {
                return ImmutableList.of();
            }
            case 1: {
                return ImmutableList.of(this.iterator().next());
            }
        }
    }
    
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    abstract boolean isPartialView();
    
    @Override
    public abstract UnmodifiableIterator<E> iterator();
    
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
        return ObjectArrays.toArrayImpl(this);
    }
    
    @Override
    public <T> T[] toArray(final T[] array) {
        return ObjectArrays.toArrayImpl(this, array);
    }
    
    @Override
    public String toString() {
        return Collections2.toStringImpl(this);
    }
    
    public abstract static class Builder<E>
    {
        static int expandedCapacity(int n, int n2) {
            if (n2 < 0) {
                throw new AssertionError((Object)"cannot store more than MAX_VALUE elements");
            }
            if ((n = (n >> 1) + n + 1) < n2) {
                n = Integer.highestOneBit(n2 - 1) << 1;
            }
            if ((n2 = n) < 0) {
                n2 = Integer.MAX_VALUE;
            }
            return n2;
        }
        
        public abstract Builder<E> add(final E p0);
        
        public Builder<E> addAll(final Iterable<? extends E> iterable) {
            final Iterator<? extends E> iterator = iterable.iterator();
            while (iterator.hasNext()) {
                this.add(iterator.next());
            }
            return this;
        }
        
        public Builder<E> addAll(final Iterator<? extends E> iterator) {
            while (iterator.hasNext()) {
                this.add(iterator.next());
            }
            return this;
        }
    }
    
    private static class EmptyImmutableCollection extends ImmutableCollection<Object>
    {
        private static final Object[] EMPTY_ARRAY;
        
        static {
            EMPTY_ARRAY = new Object[0];
        }
        
        @Override
        public boolean contains(final Object o) {
            return false;
        }
        
        @Override
        ImmutableList<Object> createAsList() {
            return ImmutableList.of();
        }
        
        @Override
        public boolean isEmpty() {
            return true;
        }
        
        @Override
        boolean isPartialView() {
            return false;
        }
        
        @Override
        public UnmodifiableIterator<Object> iterator() {
            return Iterators.EMPTY_LIST_ITERATOR;
        }
        
        @Override
        public int size() {
            return 0;
        }
        
        @Override
        public Object[] toArray() {
            return EmptyImmutableCollection.EMPTY_ARRAY;
        }
        
        @Override
        public <T> T[] toArray(final T[] array) {
            if (array.length > 0) {
                array[0] = null;
            }
            return array;
        }
    }
}
