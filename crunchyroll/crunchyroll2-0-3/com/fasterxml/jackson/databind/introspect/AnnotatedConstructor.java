// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.introspect;

import java.io.Serializable;
import java.lang.reflect.TypeVariable;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;

public final class AnnotatedConstructor extends AnnotatedWithParams
{
    protected final Constructor<?> _constructor;
    
    public AnnotatedConstructor(final Constructor<?> constructor, final AnnotationMap annotationMap, final AnnotationMap[] array) {
        super(annotationMap, array);
        if (constructor == null) {
            throw new IllegalArgumentException("Null constructor not allowed");
        }
        this._constructor = constructor;
    }
    
    @Override
    public final Object call() throws Exception {
        return this._constructor.newInstance(new Object[0]);
    }
    
    @Override
    public final Object call(final Object[] array) throws Exception {
        return this._constructor.newInstance(array);
    }
    
    @Override
    public final Object call1(final Object o) throws Exception {
        return this._constructor.newInstance(o);
    }
    
    @Override
    public Constructor<?> getAnnotated() {
        return this._constructor;
    }
    
    @Override
    public Class<?> getDeclaringClass() {
        return this._constructor.getDeclaringClass();
    }
    
    @Override
    public Type getGenericParameterType(final int n) {
        final Type[] genericParameterTypes = this._constructor.getGenericParameterTypes();
        if (n >= genericParameterTypes.length) {
            return null;
        }
        return genericParameterTypes[n];
    }
    
    @Override
    public Type getGenericType() {
        return this.getRawType();
    }
    
    @Override
    public Member getMember() {
        return this._constructor;
    }
    
    @Override
    public String getName() {
        return this._constructor.getName();
    }
    
    public int getParameterCount() {
        return this._constructor.getParameterTypes().length;
    }
    
    public Class<?> getRawParameterType(final int n) {
        final Class<?>[] parameterTypes = this._constructor.getParameterTypes();
        if (n >= parameterTypes.length) {
            return null;
        }
        return parameterTypes[n];
    }
    
    @Override
    public Class<?> getRawType() {
        return this._constructor.getDeclaringClass();
    }
    
    @Override
    public JavaType getType(final TypeBindings typeBindings) {
        return this.getType(typeBindings, this._constructor.getTypeParameters());
    }
    
    @Override
    public Object getValue(final Object o) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Cannot call getValue() on constructor of " + this.getDeclaringClass().getName());
    }
    
    @Override
    public void setValue(final Object o, final Object o2) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Cannot call setValue() on constructor of " + this.getDeclaringClass().getName());
    }
    
    @Override
    public String toString() {
        return "[constructor for " + this.getName() + ", annotations: " + this._annotations + "]";
    }
    
    private static final class Serialization implements Serializable
    {
    }
}
