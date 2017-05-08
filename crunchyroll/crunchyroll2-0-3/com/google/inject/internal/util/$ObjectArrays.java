// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal.util;

import java.lang.reflect.Array;

public final class $ObjectArrays
{
    public static <T> T[] newArray(final T[] array, final int n) {
        return (T[])Array.newInstance(array.getClass().getComponentType(), n);
    }
}
