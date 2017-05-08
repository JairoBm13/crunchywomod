// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.introspect;

import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.io.Serializable;

public final class AnnotatedField extends AnnotatedMember implements Serializable
{
    protected final transient Field _field;
    
    public AnnotatedField(final Field field, final AnnotationMap annotationMap) {
        super(annotationMap);
        this._field = field;
    }
    
    @Override
    public Field getAnnotated() {
        return this._field;
    }
    
    @Override
    public <A extends Annotation> A getAnnotation(final Class<A> clazz) {
        if (this._annotations == null) {
            return null;
        }
        return this._annotations.get(clazz);
    }
    
    @Override
    public Class<?> getDeclaringClass() {
        return this._field.getDeclaringClass();
    }
    
    public String getFullName() {
        return this.getDeclaringClass().getName() + "#" + this.getName();
    }
    
    @Override
    public Type getGenericType() {
        return this._field.getGenericType();
    }
    
    @Override
    public Member getMember() {
        return this._field;
    }
    
    public int getModifiers() {
        return this._field.getModifiers();
    }
    
    @Override
    public String getName() {
        return this._field.getName();
    }
    
    @Override
    public Class<?> getRawType() {
        return this._field.getType();
    }
    
    @Override
    public Object getValue(Object value) throws IllegalArgumentException {
        try {
            value = this._field.get(value);
            return value;
        }
        catch (IllegalAccessException ex) {
            throw new IllegalArgumentException("Failed to getValue() for field " + this.getFullName() + ": " + ex.getMessage(), ex);
        }
    }
    
    @Override
    public void setValue(final Object o, final Object o2) throws IllegalArgumentException {
        try {
            this._field.set(o, o2);
        }
        catch (IllegalAccessException ex) {
            throw new IllegalArgumentException("Failed to setValue() for field " + this.getFullName() + ": " + ex.getMessage(), ex);
        }
    }
    
    @Override
    public String toString() {
        return "[field " + this.getFullName() + "]";
    }
    
    public AnnotatedField withAnnotations(final AnnotationMap annotationMap) {
        return new AnnotatedField(this._field, annotationMap);
    }
    
    private static final class Serialization implements Serializable
    {
    }
}
