// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.ArrayList;
import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.List;
import java.io.Serializable;

final class NaturalOrdering extends Ordering<Comparable> implements Serializable
{
    static final NaturalOrdering INSTANCE;
    
    static {
        INSTANCE = new NaturalOrdering();
    }
    
    @Override
    public int binarySearch(final List<? extends Comparable> list, final Comparable comparable) {
        return Collections.binarySearch((List<? extends Comparable<? super Comparable>>)list, comparable);
    }
    
    @Override
    public int compare(final Comparable comparable, final Comparable comparable2) {
        Preconditions.checkNotNull(comparable);
        Preconditions.checkNotNull(comparable2);
        if (comparable == comparable2) {
            return 0;
        }
        return comparable.compareTo(comparable2);
    }
    
    @Override
    public <S extends Comparable> Ordering<S> reverse() {
        return (Ordering<S>)ReverseNaturalOrdering.INSTANCE;
    }
    
    @Override
    public <E extends Comparable> List<E> sortedCopy(final Iterable<E> iterable) {
        final ArrayList<Comparable> arrayList = (ArrayList<Comparable>)Lists.newArrayList((Iterable<?>)iterable);
        Collections.sort(arrayList);
        return (List<E>)arrayList;
    }
    
    @Override
    public String toString() {
        return "Ordering.natural()";
    }
}
