// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.lang.reflect.Array;

class Platform
{
    static <T> T[] newArray(final T[] array, final int n) {
        return (T[])Array.newInstance(array.getClass().getComponentType(), n);
    }
    
    static MapMaker tryWeakKeys(final MapMaker mapMaker) {
        return mapMaker.weakKeys();
    }
}
