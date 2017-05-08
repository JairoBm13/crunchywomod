// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind;

import java.lang.reflect.Type;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import java.util.Set;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.util.Annotations;
import java.lang.reflect.Constructor;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import java.util.List;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.lang.reflect.Method;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import java.util.Map;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.type.TypeBindings;

public abstract class BeanDescription
{
    protected final JavaType _type;
    
    protected BeanDescription(final JavaType type) {
        this._type = type;
    }
    
    public abstract TypeBindings bindingsForBeanType();
    
    public abstract AnnotatedMember findAnyGetter();
    
    public abstract AnnotatedMethod findAnySetter();
    
    public abstract Map<String, AnnotatedMember> findBackReferenceProperties();
    
    public abstract AnnotatedConstructor findDefaultConstructor();
    
    public abstract Converter<Object, Object> findDeserializationConverter();
    
    public abstract JsonFormat.Value findExpectedFormat(final JsonFormat.Value p0);
    
    public abstract Method findFactoryMethod(final Class<?>... p0);
    
    public abstract Map<Object, AnnotatedMember> findInjectables();
    
    public abstract AnnotatedMethod findJsonValueMethod();
    
    public abstract AnnotatedMethod findMethod(final String p0, final Class<?>[] p1);
    
    public abstract Class<?> findPOJOBuilder();
    
    public abstract JsonPOJOBuilder.Value findPOJOBuilderConfig();
    
    public abstract List<BeanPropertyDefinition> findProperties();
    
    public abstract Converter<Object, Object> findSerializationConverter();
    
    public abstract JsonInclude.Include findSerializationInclusion(final JsonInclude.Include p0);
    
    public abstract Constructor<?> findSingleArgConstructor(final Class<?>... p0);
    
    public Class<?> getBeanClass() {
        return this._type.getRawClass();
    }
    
    public abstract Annotations getClassAnnotations();
    
    public abstract AnnotatedClass getClassInfo();
    
    public abstract List<AnnotatedConstructor> getConstructors();
    
    public abstract List<AnnotatedMethod> getFactoryMethods();
    
    public abstract Set<String> getIgnoredPropertyNames();
    
    public abstract ObjectIdInfo getObjectIdInfo();
    
    public JavaType getType() {
        return this._type;
    }
    
    public abstract boolean hasKnownClassAnnotations();
    
    public abstract Object instantiateBean(final boolean p0);
    
    public abstract JavaType resolveType(final Type p0);
}
