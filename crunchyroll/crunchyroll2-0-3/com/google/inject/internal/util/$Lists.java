// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal.util;

import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;

public final class $Lists
{
    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<E>();
    }
    
    public static <E> ArrayList<E> newArrayList(final Iterable<? extends E> iterable) {
        if (iterable instanceof Collection) {
            return new ArrayList<E>((Collection<? extends E>)iterable);
        }
        return newArrayList((Iterator<? extends E>)iterable.iterator());
    }
    
    public static <E> ArrayList<E> newArrayList(final Iterator<? extends E> iterator) {
        final ArrayList<E> arrayList = newArrayList();
        while (iterator.hasNext()) {
            arrayList.add((E)iterator.next());
        }
        return arrayList;
    }
}
