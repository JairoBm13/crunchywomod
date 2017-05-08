// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.internal.util.$Nullable;
import java.lang.annotation.Annotation;

public class Nullability
{
    public static boolean allowsNull(final Annotation[] array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            final Class<? extends Annotation> annotationType = array[i].annotationType();
            if ("Nullable".equals(annotationType.getSimpleName()) || annotationType == $Nullable.class) {
                return true;
            }
        }
        return false;
    }
}
