// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.Iterator;
import java.util.Set;
import java.util.Collection;

final class EmptyImmutableSet extends ImmutableSet<Object>
{
    static final EmptyImmutableSet INSTANCE;
    
    static {
        INSTANCE = new EmptyImmutableSet();
    }
    
    @Override
    public ImmutableList<Object> asList() {
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
    public boolean equals(final Object o) {
        return o instanceof Set && ((Set)o).isEmpty();
    }
    
    @Override
    public final int hashCode() {
        return 0;
    }
    
    @Override
    public boolean isEmpty() {
        return true;
    }
    
    @Override
    boolean isHashCodeFast() {
        return true;
    }
    
    @Override
    boolean isPartialView() {
        return false;
    }
    
    @Override
    public UnmodifiableIterator<Object> iterator() {
        return Iterators.emptyIterator();
    }
    
    @Override
    public int size() {
        return 0;
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
