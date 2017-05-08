// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.ListIterator;
import com.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.List;

class RegularImmutableList<E> extends ImmutableList<E>
{
    private final transient Object[] array;
    private final transient int offset;
    private final transient int size;
    
    RegularImmutableList(final Object[] array) {
        this(array, 0, array.length);
    }
    
    RegularImmutableList(final Object[] array, final int offset, final int size) {
        this.offset = offset;
        this.size = size;
        this.array = array;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o != this) {
            if (!(o instanceof List)) {
                return false;
            }
            final List list = (List)o;
            if (this.size() != list.size()) {
                return false;
            }
            int offset = this.offset;
            if (o instanceof RegularImmutableList) {
                final RegularImmutableList list2 = (RegularImmutableList)o;
                for (int i = list2.offset; i < list2.offset + list2.size; ++i, ++offset) {
                    if (!this.array[offset].equals(list2.array[i])) {
                        return false;
                    }
                }
            }
            else {
                final Iterator<Object> iterator = list.iterator();
                while (iterator.hasNext()) {
                    if (!this.array[offset].equals(iterator.next())) {
                        return false;
                    }
                    ++offset;
                }
            }
        }
        return true;
    }
    
    @Override
    public E get(final int n) {
        Preconditions.checkElementIndex(n, this.size);
        return (E)this.array[this.offset + n];
    }
    
    @Override
    public boolean isEmpty() {
        return false;
    }
    
    @Override
    boolean isPartialView() {
        return this.offset != 0 || this.size != this.array.length;
    }
    
    @Override
    public UnmodifiableListIterator<E> listIterator(final int n) {
        return Iterators.forArray(this.array, this.offset, this.size, n);
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    @Override
    ImmutableList<E> subListUnchecked(final int n, final int n2) {
        return new RegularImmutableList(this.array, this.offset + n, n2 - n);
    }
    
    @Override
    public Object[] toArray() {
        final Object[] array = new Object[this.size()];
        System.arraycopy(this.array, this.offset, array, 0, this.size);
        return array;
    }
    
    @Override
    public <T> T[] toArray(final T[] array) {
        T[] array2;
        if (array.length < this.size) {
            array2 = ObjectArrays.newArray(array, this.size);
        }
        else {
            array2 = array;
            if (array.length > this.size) {
                array[this.size] = null;
                array2 = array;
            }
        }
        System.arraycopy(this.array, this.offset, array2, 0, this.size);
        return array2;
    }
    
    @Override
    public String toString() {
        final StringBuilder append = Collections2.newStringBuilderForCollection(this.size()).append('[').append(this.array[this.offset]);
        for (int i = this.offset + 1; i < this.offset + this.size; ++i) {
            append.append(", ").append(this.array[i]);
        }
        return append.append(']').toString();
    }
}
