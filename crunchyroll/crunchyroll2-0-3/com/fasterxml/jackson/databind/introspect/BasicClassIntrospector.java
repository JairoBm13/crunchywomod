// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.introspect;

import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.type.SimpleType;
import java.io.Serializable;

public class BasicClassIntrospector extends ClassIntrospector implements Serializable
{
    protected static final BasicBeanDescription BOOLEAN_DESC;
    protected static final BasicBeanDescription INT_DESC;
    protected static final BasicBeanDescription LONG_DESC;
    protected static final BasicBeanDescription STRING_DESC;
    public static final BasicClassIntrospector instance;
    
    static {
        STRING_DESC = BasicBeanDescription.forOtherUse(null, SimpleType.constructUnsafe(String.class), AnnotatedClass.constructWithoutSuperTypes(String.class, null, null));
        BOOLEAN_DESC = BasicBeanDescription.forOtherUse(null, SimpleType.constructUnsafe(Boolean.TYPE), AnnotatedClass.constructWithoutSuperTypes(Boolean.TYPE, null, null));
        INT_DESC = BasicBeanDescription.forOtherUse(null, SimpleType.constructUnsafe(Integer.TYPE), AnnotatedClass.constructWithoutSuperTypes(Integer.TYPE, null, null));
        LONG_DESC = BasicBeanDescription.forOtherUse(null, SimpleType.constructUnsafe(Long.TYPE), AnnotatedClass.constructWithoutSuperTypes(Long.TYPE, null, null));
        instance = new BasicClassIntrospector();
    }
    
    protected BasicBeanDescription _findCachedDesc(final JavaType javaType) {
        final Class<?> rawClass = javaType.getRawClass();
        if (rawClass == String.class) {
            return BasicClassIntrospector.STRING_DESC;
        }
        if (rawClass == Boolean.TYPE) {
            return BasicClassIntrospector.BOOLEAN_DESC;
        }
        if (rawClass == Integer.TYPE) {
            return BasicClassIntrospector.INT_DESC;
        }
        if (rawClass == Long.TYPE) {
            return BasicClassIntrospector.LONG_DESC;
        }
        return null;
    }
    
    protected POJOPropertiesCollector collectProperties(final MapperConfig<?> mapperConfig, final JavaType javaType, final MixInResolver mixInResolver, final boolean b, final String s) {
        final boolean annotationProcessingEnabled = mapperConfig.isAnnotationProcessingEnabled();
        final Class<?> rawClass = javaType.getRawClass();
        AnnotationIntrospector annotationIntrospector;
        if (annotationProcessingEnabled) {
            annotationIntrospector = mapperConfig.getAnnotationIntrospector();
        }
        else {
            annotationIntrospector = null;
        }
        return this.constructPropertyCollector(mapperConfig, AnnotatedClass.construct(rawClass, annotationIntrospector, mixInResolver), javaType, b, s).collect();
    }
    
    protected POJOPropertiesCollector collectPropertiesWithBuilder(final MapperConfig<?> mapperConfig, final JavaType javaType, final MixInResolver mixInResolver, final boolean b) {
        final JsonPOJOBuilder.Value value = null;
        AnnotationIntrospector annotationIntrospector;
        if (mapperConfig.isAnnotationProcessingEnabled()) {
            annotationIntrospector = mapperConfig.getAnnotationIntrospector();
        }
        else {
            annotationIntrospector = null;
        }
        final AnnotatedClass construct = AnnotatedClass.construct(javaType.getRawClass(), annotationIntrospector, mixInResolver);
        JsonPOJOBuilder.Value pojoBuilderConfig;
        if (annotationIntrospector == null) {
            pojoBuilderConfig = value;
        }
        else {
            pojoBuilderConfig = annotationIntrospector.findPOJOBuilderConfig(construct);
        }
        String withPrefix;
        if (pojoBuilderConfig == null) {
            withPrefix = "with";
        }
        else {
            withPrefix = pojoBuilderConfig.withPrefix;
        }
        return this.constructPropertyCollector(mapperConfig, construct, javaType, b, withPrefix).collect();
    }
    
    protected POJOPropertiesCollector constructPropertyCollector(final MapperConfig<?> mapperConfig, final AnnotatedClass annotatedClass, final JavaType javaType, final boolean b, final String s) {
        return new POJOPropertiesCollector(mapperConfig, b, javaType, annotatedClass, s);
    }
    
    @Override
    public BasicBeanDescription forClassAnnotations(final MapperConfig<?> mapperConfig, final JavaType javaType, final MixInResolver mixInResolver) {
        final boolean annotationProcessingEnabled = mapperConfig.isAnnotationProcessingEnabled();
        final Class<?> rawClass = javaType.getRawClass();
        AnnotationIntrospector annotationIntrospector;
        if (annotationProcessingEnabled) {
            annotationIntrospector = mapperConfig.getAnnotationIntrospector();
        }
        else {
            annotationIntrospector = null;
        }
        return BasicBeanDescription.forOtherUse(mapperConfig, javaType, AnnotatedClass.construct(rawClass, annotationIntrospector, mixInResolver));
    }
    
    @Override
    public BasicBeanDescription forCreation(final DeserializationConfig deserializationConfig, final JavaType javaType, final MixInResolver mixInResolver) {
        BasicBeanDescription basicBeanDescription;
        if ((basicBeanDescription = this._findCachedDesc(javaType)) == null) {
            basicBeanDescription = BasicBeanDescription.forDeserialization(this.collectProperties(deserializationConfig, javaType, mixInResolver, false, "set"));
        }
        return basicBeanDescription;
    }
    
    @Override
    public BasicBeanDescription forDeserialization(final DeserializationConfig deserializationConfig, final JavaType javaType, final MixInResolver mixInResolver) {
        BasicBeanDescription basicBeanDescription;
        if ((basicBeanDescription = this._findCachedDesc(javaType)) == null) {
            basicBeanDescription = BasicBeanDescription.forDeserialization(this.collectProperties(deserializationConfig, javaType, mixInResolver, false, "set"));
        }
        return basicBeanDescription;
    }
    
    @Override
    public BasicBeanDescription forDeserializationWithBuilder(final DeserializationConfig deserializationConfig, final JavaType javaType, final MixInResolver mixInResolver) {
        return BasicBeanDescription.forDeserialization(this.collectPropertiesWithBuilder(deserializationConfig, javaType, mixInResolver, false));
    }
    
    @Override
    public BasicBeanDescription forSerialization(final SerializationConfig serializationConfig, final JavaType javaType, final MixInResolver mixInResolver) {
        BasicBeanDescription basicBeanDescription;
        if ((basicBeanDescription = this._findCachedDesc(javaType)) == null) {
            basicBeanDescription = BasicBeanDescription.forSerialization(this.collectProperties(serializationConfig, javaType, mixInResolver, true, "set"));
        }
        return basicBeanDescription;
    }
}
