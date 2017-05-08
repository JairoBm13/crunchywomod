// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.util;

import com.google.inject.internal.Errors;
import com.google.inject.Key;
import java.lang.annotation.Annotation;

class Node
{
    private Class<? extends Annotation> appliedScopeAnnotation;
    private final Key<?> key;
    
    @Override
    public String toString() {
        if (this.appliedScopeAnnotation != null) {
            return Errors.convert(this.key) + " in @" + this.appliedScopeAnnotation.getSimpleName();
        }
        return Errors.convert(this.key).toString();
    }
}
