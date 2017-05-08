// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import com.google.common.base.Preconditions;
import java.util.Comparator;
import java.io.Serializable;

final class ComparatorOrdering<T> extends Ordering<T> implements Serializable
{
    final Comparator<T> comparator;
    
    ComparatorOrdering(final Comparator<T> comparator) {
        this.comparator = Preconditions.checkNotNull(comparator);
    }
    
    @Override
    public int binarySearch(final List<? extends T> list, final T t) {
        return Collections.binarySearch(list, t, this.comparator);
    }
    
    @Override
    public int compare(final T t, final T t2) {
        return this.comparator.compare(t, t2);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof ComparatorOrdering && this.comparator.equals(((ComparatorOrdering)o).comparator));
    }
    
    @Override
    public int hashCode() {
        return this.comparator.hashCode();
    }
    
    @Override
    public <E extends T> ImmutableList<E> immutableSortedCopy(final Iterable<E> iterable) {
        final Object[] array = Iterables.toArray(iterable);
        for (int length = array.length, i = 0; i < length; ++i) {
            Preconditions.checkNotNull(array[i]);
        }
        Arrays.sort(array, (Comparator<? super Object>)this.comparator);
        return (ImmutableList<E>)ImmutableList.asImmutableList(array);
    }
    
    @Override
    public <E extends T> List<E> sortedCopy(final Iterable<E> iterable) {
        final ArrayList<Object> arrayList = Lists.newArrayList((Iterable<?>)iterable);
        Collections.sort(arrayList, (Comparator<? super Object>)this.comparator);
        return (List<E>)arrayList;
    }
    
    @Override
    public String toString() {
        return this.comparator.toString();
    }
}
