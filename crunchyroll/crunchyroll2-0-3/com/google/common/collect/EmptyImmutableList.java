// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.ListIterator;
import java.util.Iterator;
import com.google.common.base.Preconditions;
import java.util.List;
import java.util.Collection;

final class EmptyImmutableList extends ImmutableList<Object>
{
    static final EmptyImmutableList INSTANCE;
    
    static {
        INSTANCE = new EmptyImmutableList();
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
    public boolean equals(final Object o) {
        return o instanceof List && ((List)o).isEmpty();
    }
    
    @Override
    public Object get(final int n) {
        Preconditions.checkElementIndex(n, 0);
        throw new AssertionError((Object)"unreachable");
    }
    
    @Override
    public int hashCode() {
        return 1;
    }
    
    @Override
    public int indexOf(final Object o) {
        return -1;
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
        return this.listIterator();
    }
    
    @Override
    public int lastIndexOf(final Object o) {
        return -1;
    }
    
    @Override
    public UnmodifiableListIterator<Object> listIterator() {
        return Iterators.EMPTY_LIST_ITERATOR;
    }
    
    @Override
    public UnmodifiableListIterator<Object> listIterator(final int n) {
        Preconditions.checkPositionIndex(n, 0);
        return Iterators.EMPTY_LIST_ITERATOR;
    }
    
    @Override
    public int size() {
        return 0;
    }
    
    @Override
    public ImmutableList<Object> subList(final int n, final int n2) {
        Preconditions.checkPositionIndexes(n, n2, 0);
        return this;
    }
    
    @Override
    public Object[] toArray() {
        return ObjectArrays.EMPTY_ARRAY;
    }
    
    @Override
    public <T> T[] toArray(final T[] array) {
        if (array.length > 0) {
            array[0] = null;
        }
        return array;
    }
    
    @Override
    public String toString() {
        return "[]";
    }
}
