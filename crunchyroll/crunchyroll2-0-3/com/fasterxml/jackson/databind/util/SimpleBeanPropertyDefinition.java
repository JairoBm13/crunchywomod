// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.util;

import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import java.io.Serializable;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;

public class SimpleBeanPropertyDefinition extends BeanPropertyDefinition
{
    protected final AnnotationIntrospector _introspector;
    protected final AnnotatedMember _member;
    protected final String _name;
    
    private SimpleBeanPropertyDefinition(final AnnotatedMember member, final String name, final AnnotationIntrospector introspector) {
        this._introspector = introspector;
        this._member = member;
        this._name = name;
    }
    
    public static SimpleBeanPropertyDefinition construct(final MapperConfig<?> mapperConfig, final AnnotatedMember annotatedMember) {
        final String name = annotatedMember.getName();
        AnnotationIntrospector annotationIntrospector;
        if (mapperConfig == null) {
            annotationIntrospector = null;
        }
        else {
            annotationIntrospector = mapperConfig.getAnnotationIntrospector();
        }
        return new SimpleBeanPropertyDefinition(annotatedMember, name, annotationIntrospector);
    }
    
    public static SimpleBeanPropertyDefinition construct(final MapperConfig<?> mapperConfig, final AnnotatedMember annotatedMember, final String s) {
        AnnotationIntrospector annotationIntrospector;
        if (mapperConfig == null) {
            annotationIntrospector = null;
        }
        else {
            annotationIntrospector = mapperConfig.getAnnotationIntrospector();
        }
        return new SimpleBeanPropertyDefinition(annotatedMember, s, annotationIntrospector);
    }
    
    @Override
    public AnnotatedMember getAccessor() {
        Serializable s;
        if ((s = this.getGetter()) == null) {
            s = this.getField();
        }
        return (AnnotatedMember)s;
    }
    
    @Override
    public AnnotatedParameter getConstructorParameter() {
        if (this._member instanceof AnnotatedParameter) {
            return (AnnotatedParameter)this._member;
        }
        return null;
    }
    
    @Override
    public AnnotatedField getField() {
        if (this._member instanceof AnnotatedField) {
            return (AnnotatedField)this._member;
        }
        return null;
    }
    
    @Override
    public AnnotatedMethod getGetter() {
        if (this._member instanceof AnnotatedMethod && ((AnnotatedMethod)this._member).getParameterCount() == 0) {
            return (AnnotatedMethod)this._member;
        }
        return null;
    }
    
    @Override
    public AnnotatedMember getMutator() {
        AnnotatedMember annotatedMember;
        if ((annotatedMember = this.getConstructorParameter()) == null && (annotatedMember = this.getSetter()) == null) {
            annotatedMember = this.getField();
        }
        return annotatedMember;
    }
    
    @Override
    public String getName() {
        return this._name;
    }
    
    @Override
    public AnnotatedMember getPrimaryMember() {
        return this._member;
    }
    
    @Override
    public AnnotatedMethod getSetter() {
        if (this._member instanceof AnnotatedMethod && ((AnnotatedMethod)this._member).getParameterCount() == 1) {
            return (AnnotatedMethod)this._member;
        }
        return null;
    }
    
    @Override
    public PropertyName getWrapperName() {
        if (this._introspector == null) {
            return null;
        }
        return this._introspector.findWrapperName(this._member);
    }
    
    @Override
    public boolean hasConstructorParameter() {
        return this._member instanceof AnnotatedParameter;
    }
    
    @Override
    public boolean hasField() {
        return this._member instanceof AnnotatedField;
    }
    
    @Override
    public boolean hasGetter() {
        return this.getGetter() != null;
    }
    
    @Override
    public boolean hasSetter() {
        return this.getSetter() != null;
    }
    
    @Override
    public boolean isExplicitlyIncluded() {
        return false;
    }
}
