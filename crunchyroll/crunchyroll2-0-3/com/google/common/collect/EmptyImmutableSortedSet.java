// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Collection;
import java.util.Comparator;

class EmptyImmutableSortedSet<E> extends ImmutableSortedSet<E>
{
    EmptyImmutableSortedSet(final Comparator<? super E> comparator) {
        super(comparator);
    }
    
    @Override
    public ImmutableList<E> asList() {
        return ImmutableList.of();
    }
    
    @Override
    public boolean contains(final Object o) {
        return false;
    }
    
    @Override
    public boolean containsAll(final Collection<?> collection) {
        return collection.isEmpty();
    }
    
    @Override
    ImmutableSortedSet<E> createDescendingSet() {
        return new EmptyImmutableSortedSet(Ordering.from(this.comparator).reverse());
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof Set && ((Set)o).isEmpty();
    }
    
    @Override
    public E first() {
        throw new NoSuchElementException();
    }
    
    @Override
    public int hashCode() {
        return 0;
    }
    
    @Override
    ImmutableSortedSet<E> headSetImpl(final E e, final boolean b) {
        return this;
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
    public UnmodifiableIterator<E> iterator() {
        return Iterators.emptyIterator();
    }
    
    @Override
    public E last() {
        throw new NoSuchElementException();
    }
    
    @Override
    public int size() {
        return 0;
    }
    
    @Override
    ImmutableSortedSet<E> subSetImpl(final E e, final boolean b, final E e2, final boolean b2) {
        return this;
    }
    
    @Override
    ImmutableSortedSet<E> tailSetImpl(final E e, final boolean b) {
        return this;
    }
    
    @Override
    public Object[] toArray() {
        return ObjectArrays.EMPTY_ARRAY;
    }
    
    @Override
    public <T> T[] toArray(final T[] array) {
        return this.asList().toArray(array);
    }
    
    @Override
    public String toString() {
        return "[]";
    }
}
