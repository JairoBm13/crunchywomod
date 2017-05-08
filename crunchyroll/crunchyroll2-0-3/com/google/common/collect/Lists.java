// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.RandomAccess;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.LinkedList;
import java.util.Collection;
import java.util.ArrayList;
import java.util.ListIterator;
import com.google.common.base.Objects;
import java.util.Iterator;
import com.google.common.base.Preconditions;
import java.util.List;

public final class Lists
{
    public static <E> List<E> asList(final E e, final E[] array) {
        return new OnePlusArrayList<E>(e, array);
    }
    
    static boolean equalsImpl(final List<?> list, final Object o) {
        if (o != Preconditions.checkNotNull(list)) {
            if (!(o instanceof List)) {
                return false;
            }
            final List list2 = (List)o;
            if (list.size() != list2.size() || !Iterators.elementsEqual(list.iterator(), list2.iterator())) {
                return false;
            }
        }
        return true;
    }
    
    static int hashCodeImpl(final List<?> list) {
        int n = 1;
        for (final Object next : list) {
            int hashCode;
            if (next == null) {
                hashCode = 0;
            }
            else {
                hashCode = next.hashCode();
            }
            n = n * 31 + hashCode;
        }
        return n;
    }
    
    static int indexOfImpl(final List<?> list, final Object o) {
        final ListIterator<?> listIterator = list.listIterator();
        while (listIterator.hasNext()) {
            if (Objects.equal(o, listIterator.next())) {
                return listIterator.previousIndex();
            }
        }
        return -1;
    }
    
    static int lastIndexOfImpl(final List<?> list, final Object o) {
        final ListIterator<?> listIterator = list.listIterator(list.size());
        while (listIterator.hasPrevious()) {
            if (Objects.equal(o, listIterator.previous())) {
                return listIterator.nextIndex();
            }
        }
        return -1;
    }
    
    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<E>();
    }
    
    public static <E> ArrayList<E> newArrayList(final Iterable<? extends E> iterable) {
        Preconditions.checkNotNull(iterable);
        if (iterable instanceof Collection) {
            return new ArrayList<E>((Collection<? extends E>)Collections2.cast((Iterable<? extends E>)iterable));
        }
        return newArrayList((Iterator<? extends E>)iterable.iterator());
    }
    
    public static <E> ArrayList<E> newArrayList(final Iterator<? extends E> iterator) {
        Preconditions.checkNotNull(iterator);
        final ArrayList<E> arrayList = newArrayList();
        while (iterator.hasNext()) {
            arrayList.add((E)iterator.next());
        }
        return arrayList;
    }
    
    public static <E> ArrayList<E> newArrayListWithCapacity(final int n) {
        Preconditions.checkArgument(n >= 0);
        return new ArrayList<E>(n);
    }
    
    public static <E> LinkedList<E> newLinkedList() {
        return new LinkedList<E>();
    }
    
    private static class OnePlusArrayList<E> extends AbstractList<E> implements Serializable, RandomAccess
    {
        final E first;
        final E[] rest;
        
        OnePlusArrayList(final E first, final E[] array) {
            this.first = first;
            this.rest = Preconditions.checkNotNull(array);
        }
        
        @Override
        public E get(final int n) {
            Preconditions.checkElementIndex(n, this.size());
            if (n == 0) {
                return this.first;
            }
            return (E)this.rest[n - 1];
        }
        
        @Override
        public int size() {
            return this.rest.length + 1;
        }
    }
}
