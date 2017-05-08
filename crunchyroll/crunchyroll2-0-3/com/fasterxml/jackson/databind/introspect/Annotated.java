// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.introspect;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import java.lang.reflect.Type;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public abstract class Annotated
{
    protected abstract AnnotationMap getAllAnnotations();
    
    public abstract AnnotatedElement getAnnotated();
    
    public abstract <A extends Annotation> A getAnnotation(final Class<A> p0);
    
    public abstract Type getGenericType();
    
    public abstract String getName();
    
    public abstract Class<?> getRawType();
    
    public JavaType getType(final TypeBindings typeBindings) {
        return typeBindings.resolveType(this.getGenericType());
    }
    
    public final <A extends Annotation> boolean hasAnnotation(final Class<A> clazz) {
        return this.getAnnotation(clazz) != null;
    }
}
