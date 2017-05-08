// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.Collection;
import java.util.Iterator;

public final class ObjectArrays
{
    static final Object[] EMPTY_ARRAY;
    
    static {
        EMPTY_ARRAY = new Object[0];
    }
    
    static <T> T[] arraysCopyOf(final T[] array, final int n) {
        final Object[] array2 = newArray((Object[])array, n);
        System.arraycopy(array, 0, array2, 0, Math.min(array.length, n));
        return (T[])array2;
    }
    
    static Object checkElementNotNull(final Object o, final int n) {
        if (o == null) {
            throw new NullPointerException("at index " + n);
        }
        return o;
    }
    
    private static Object[] fillArray(final Iterable<?> iterable, final Object[] array) {
        int n = 0;
        final Iterator<?> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            array[n] = iterator.next();
            ++n;
        }
        return array;
    }
    
    public static <T> T[] newArray(final T[] array, final int n) {
        return Platform.newArray(array, n);
    }
    
    static void swap(final Object[] array, final int n, final int n2) {
        final Object o = array[n];
        array[n] = array[n2];
        array[n2] = o;
    }
    
    static Object[] toArrayImpl(final Collection<?> collection) {
        return fillArray(collection, new Object[collection.size()]);
    }
    
    static <T> T[] toArrayImpl(final Collection<?> collection, final T[] array) {
        final int size = collection.size();
        T[] array2 = array;
        if (array.length < size) {
            array2 = newArray(array, size);
        }
        fillArray(collection, array2);
        if (array2.length > size) {
            array2[size] = null;
        }
        return array2;
    }
}
