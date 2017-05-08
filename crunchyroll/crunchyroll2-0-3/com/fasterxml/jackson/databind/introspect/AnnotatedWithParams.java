// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.introspect;

import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;
import java.lang.reflect.TypeVariable;
import com.fasterxml.jackson.databind.type.TypeBindings;
import java.lang.reflect.Type;
import java.lang.annotation.Annotation;

public abstract class AnnotatedWithParams extends AnnotatedMember
{
    protected final AnnotationMap[] _paramAnnotations;
    
    protected AnnotatedWithParams(final AnnotationMap annotationMap, final AnnotationMap[] paramAnnotations) {
        super(annotationMap);
        this._paramAnnotations = paramAnnotations;
    }
    
    public final void addOrOverrideParam(final int n, final Annotation annotation) {
        AnnotationMap annotationMap;
        if ((annotationMap = this._paramAnnotations[n]) == null) {
            annotationMap = new AnnotationMap();
            this._paramAnnotations[n] = annotationMap;
        }
        annotationMap.add(annotation);
    }
    
    public abstract Object call() throws Exception;
    
    public abstract Object call(final Object[] p0) throws Exception;
    
    public abstract Object call1(final Object p0) throws Exception;
    
    @Override
    public final <A extends Annotation> A getAnnotation(final Class<A> clazz) {
        return this._annotations.get(clazz);
    }
    
    public abstract Type getGenericParameterType(final int p0);
    
    public final AnnotatedParameter getParameter(final int n) {
        return new AnnotatedParameter(this, this.getGenericParameterType(n), this.getParameterAnnotations(n), n);
    }
    
    public final AnnotationMap getParameterAnnotations(final int n) {
        if (this._paramAnnotations != null && n >= 0 && n <= this._paramAnnotations.length) {
            return this._paramAnnotations[n];
        }
        return null;
    }
    
    protected JavaType getType(final TypeBindings typeBindings, final TypeVariable<?>[] array) {
        TypeBindings typeBindings2 = typeBindings;
        if (array != null) {
            typeBindings2 = typeBindings;
            if (array.length > 0) {
                final TypeBindings childInstance = typeBindings.childInstance();
                final int length = array.length;
                int n = 0;
                while (true) {
                    typeBindings2 = childInstance;
                    if (n >= length) {
                        break;
                    }
                    final TypeVariable<?> typeVariable = array[n];
                    childInstance._addPlaceholder(typeVariable.getName());
                    final Type type = typeVariable.getBounds()[0];
                    JavaType javaType;
                    if (type == null) {
                        javaType = TypeFactory.unknownType();
                    }
                    else {
                        javaType = childInstance.resolveType(type);
                    }
                    childInstance.addBinding(typeVariable.getName(), javaType);
                    ++n;
                }
            }
        }
        return typeBindings2.resolveType(this.getGenericType());
    }
    
    protected AnnotatedParameter replaceParameterAnnotations(final int n, final AnnotationMap annotationMap) {
        this._paramAnnotations[n] = annotationMap;
        return this.getParameter(n);
    }
}
