// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.Iterator;
import java.util.List;
import com.google.common.base.Preconditions;

final class SingletonImmutableList<E> extends ImmutableList<E>
{
    final transient E element;
    
    SingletonImmutableList(final E e) {
        this.element = Preconditions.checkNotNull(e);
    }
    
    @Override
    public boolean contains(final Object o) {
        return this.element.equals(o);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o != this) {
            if (!(o instanceof List)) {
                return false;
            }
            final List list = (List)o;
            if (list.size() != 1 || !this.element.equals(list.get(0))) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public E get(final int n) {
        Preconditions.checkElementIndex(n, 1);
        return this.element;
    }
    
    @Override
    public int hashCode() {
        return this.element.hashCode() + 31;
    }
    
    @Override
    public int indexOf(final Object o) {
        if (this.element.equals(o)) {
            return 0;
        }
        return -1;
    }
    
    @Override
    public boolean isEmpty() {
        return false;
    }
    
    @Override
    boolean isPartialView() {
        return false;
    }
    
    @Override
    public UnmodifiableIterator<E> iterator() {
        return Iterators.singletonIterator(this.element);
    }
    
    @Override
    public int lastIndexOf(final Object o) {
        return this.indexOf(o);
    }
    
    @Override
    public int size() {
        return 1;
    }
    
    @Override
    public ImmutableList<E> subList(final int n, final int n2) {
        Preconditions.checkPositionIndexes(n, n2, 1);
        ImmutableList<Object> of = (ImmutableList<Object>)this;
        if (n == n2) {
            of = ImmutableList.of();
        }
        return (ImmutableList<E>)of;
    }
    
    @Override
    public Object[] toArray() {
        return new Object[] { this.element };
    }
    
    @Override
    public <T> T[] toArray(final T[] array) {
        T[] array2;
        if (array.length == 0) {
            array2 = ObjectArrays.newArray(array, 1);
        }
        else {
            array2 = array;
            if (array.length > 1) {
                array[1] = null;
                array2 = array;
            }
        }
        array2[0] = (T)this.element;
        return array2;
    }
    
    @Override
    public String toString() {
        final String string = this.element.toString();
        return new StringBuilder(string.length() + 2).append('[').append(string).append(']').toString();
    }
}
