// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.introspect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.TypeVariable;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.io.Serializable;

public final class AnnotatedMethod extends AnnotatedWithParams implements Serializable
{
    protected final transient Method _method;
    protected Class<?>[] _paramClasses;
    
    public AnnotatedMethod(final Method method, final AnnotationMap annotationMap, final AnnotationMap[] array) {
        super(annotationMap, array);
        if (method == null) {
            throw new IllegalArgumentException("Can not construct AnnotatedMethod with null Method");
        }
        this._method = method;
    }
    
    @Override
    public final Object call() throws Exception {
        return this._method.invoke(null, new Object[0]);
    }
    
    @Override
    public final Object call(final Object[] array) throws Exception {
        return this._method.invoke(null, array);
    }
    
    @Override
    public final Object call1(final Object o) throws Exception {
        return this._method.invoke(null, o);
    }
    
    @Override
    public Method getAnnotated() {
        return this._method;
    }
    
    @Override
    public Class<?> getDeclaringClass() {
        return this._method.getDeclaringClass();
    }
    
    public String getFullName() {
        return this.getDeclaringClass().getName() + "#" + this.getName() + "(" + this.getParameterCount() + " params)";
    }
    
    @Override
    public Type getGenericParameterType(final int n) {
        final Type[] genericParameterTypes = this._method.getGenericParameterTypes();
        if (n >= genericParameterTypes.length) {
            return null;
        }
        return genericParameterTypes[n];
    }
    
    @Override
    public Type getGenericType() {
        return this._method.getGenericReturnType();
    }
    
    @Override
    public Method getMember() {
        return this._method;
    }
    
    @Override
    public String getName() {
        return this._method.getName();
    }
    
    public int getParameterCount() {
        return this.getRawParameterTypes().length;
    }
    
    public Class<?> getRawParameterType(final int n) {
        final Class<?>[] rawParameterTypes = this.getRawParameterTypes();
        if (n >= rawParameterTypes.length) {
            return null;
        }
        return rawParameterTypes[n];
    }
    
    public Class<?>[] getRawParameterTypes() {
        if (this._paramClasses == null) {
            this._paramClasses = this._method.getParameterTypes();
        }
        return this._paramClasses;
    }
    
    public Class<?> getRawReturnType() {
        return this._method.getReturnType();
    }
    
    @Override
    public Class<?> getRawType() {
        return this._method.getReturnType();
    }
    
    @Override
    public JavaType getType(final TypeBindings typeBindings) {
        return this.getType(typeBindings, this._method.getTypeParameters());
    }
    
    @Override
    public Object getValue(Object invoke) throws IllegalArgumentException {
        try {
            invoke = this._method.invoke(invoke, new Object[0]);
            return invoke;
        }
        catch (IllegalAccessException ex) {
            throw new IllegalArgumentException("Failed to getValue() with method " + this.getFullName() + ": " + ex.getMessage(), ex);
        }
        catch (InvocationTargetException ex2) {
            throw new IllegalArgumentException("Failed to getValue() with method " + this.getFullName() + ": " + ex2.getMessage(), ex2);
        }
    }
    
    @Override
    public void setValue(final Object o, final Object o2) throws IllegalArgumentException {
        try {
            this._method.invoke(o, o2);
        }
        catch (IllegalAccessException ex) {
            throw new IllegalArgumentException("Failed to setValue() with method " + this.getFullName() + ": " + ex.getMessage(), ex);
        }
        catch (InvocationTargetException ex2) {
            throw new IllegalArgumentException("Failed to setValue() with method " + this.getFullName() + ": " + ex2.getMessage(), ex2);
        }
    }
    
    @Override
    public String toString() {
        return "[method " + this.getFullName() + "]";
    }
    
    public AnnotatedMethod withAnnotations(final AnnotationMap annotationMap) {
        return new AnnotatedMethod(this._method, annotationMap, this._paramAnnotations);
    }
    
    public AnnotatedMethod withMethod(final Method method) {
        return new AnnotatedMethod(method, this._annotations, this._paramAnnotations);
    }
    
    private static final class Serialization implements Serializable
    {
    }
}
