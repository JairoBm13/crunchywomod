// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import com.google.inject.BindingAnnotation;
import java.lang.annotation.Annotation;
import java.util.concurrent.atomic.AtomicInteger;

public class UniqueAnnotations
{
    private static final AtomicInteger nextUniqueValue;
    
    static {
        nextUniqueValue = new AtomicInteger(1);
    }
    
    public static Annotation create() {
        return create(UniqueAnnotations.nextUniqueValue.getAndIncrement());
    }
    
    static Annotation create(final int n) {
        return new Internal() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Internal.class;
            }
            
            @Override
            public boolean equals(final Object o) {
                return o instanceof Internal && ((Internal)o).value() == this.value();
            }
            
            @Override
            public int hashCode() {
                return "value".hashCode() * 127 ^ n;
            }
            
            @Override
            public String toString() {
                return "@" + Internal.class.getName() + "(value=" + n + ")";
            }
            
            @Override
            public int value() {
                return n;
            }
        };
    }
    
    @BindingAnnotation
    @Retention(RetentionPolicy.RUNTIME)
    @interface Internal {
        int value();
    }
}
