// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.Iterator;
import java.util.Set;
import com.google.common.base.Preconditions;

final class SingletonImmutableSet<E> extends ImmutableSet<E>
{
    private transient int cachedHashCode;
    final transient E element;
    
    SingletonImmutableSet(final E e) {
        this.element = Preconditions.checkNotNull(e);
    }
    
    SingletonImmutableSet(final E element, final int cachedHashCode) {
        this.element = element;
        this.cachedHashCode = cachedHashCode;
    }
    
    @Override
    public boolean contains(final Object o) {
        return this.element.equals(o);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o != this) {
            if (!(o instanceof Set)) {
                return false;
            }
            final Set set = (Set)o;
            if (set.size() != 1 || !this.element.equals(set.iterator().next())) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public final int hashCode() {
        int cachedHashCode;
        if ((cachedHashCode = this.cachedHashCode) == 0) {
            cachedHashCode = this.element.hashCode();
            this.cachedHashCode = cachedHashCode;
        }
        return cachedHashCode;
    }
    
    @Override
    public boolean isEmpty() {
        return false;
    }
    
    @Override
    boolean isHashCodeFast() {
        return this.cachedHashCode != 0;
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
    public int size() {
        return 1;
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
