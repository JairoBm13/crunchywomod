// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.ListIterator;

class RegularImmutableAsList<E> extends ImmutableAsList<E>
{
    private final ImmutableCollection<E> delegate;
    private final ImmutableList<? extends E> delegateList;
    
    RegularImmutableAsList(final ImmutableCollection<E> delegate, final ImmutableList<? extends E> delegateList) {
        this.delegate = delegate;
        this.delegateList = delegateList;
    }
    
    RegularImmutableAsList(final ImmutableCollection<E> collection, final Object[] array) {
        this(collection, ImmutableList.asImmutableList(array));
    }
    
    @Override
    ImmutableCollection<E> delegateCollection() {
        return this.delegate;
    }
    
    @Override
    public boolean equals(final Object o) {
        return this.delegateList.equals(o);
    }
    
    @Override
    public E get(final int n) {
        return (E)this.delegateList.get(n);
    }
    
    @Override
    public int hashCode() {
        return this.delegateList.hashCode();
    }
    
    @Override
    public int indexOf(final Object o) {
        return this.delegateList.indexOf(o);
    }
    
    @Override
    public int lastIndexOf(final Object o) {
        return this.delegateList.lastIndexOf(o);
    }
    
    @Override
    public UnmodifiableListIterator<E> listIterator(final int n) {
        return (UnmodifiableListIterator<E>)this.delegateList.listIterator(n);
    }
    
    @Override
    public Object[] toArray() {
        return this.delegateList.toArray();
    }
    
    @Override
    public <T> T[] toArray(final T[] array) {
        return this.delegateList.toArray(array);
    }
}
