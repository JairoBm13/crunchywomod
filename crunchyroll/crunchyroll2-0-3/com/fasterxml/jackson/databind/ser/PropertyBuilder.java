// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ArrayBuilders;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.AnnotationIntrospector;

public class PropertyBuilder
{
    protected final AnnotationIntrospector _annotationIntrospector;
    protected final BeanDescription _beanDesc;
    protected final SerializationConfig _config;
    protected Object _defaultBean;
    protected final JsonInclude.Include _outputProps;
    
    public PropertyBuilder(final SerializationConfig config, final BeanDescription beanDesc) {
        this._config = config;
        this._beanDesc = beanDesc;
        this._outputProps = beanDesc.findSerializationInclusion(config.getSerializationInclusion());
        this._annotationIntrospector = this._config.getAnnotationIntrospector();
    }
    
    protected Object _throwWrapped(Exception cause, final String s, final Object o) {
        while (cause.getCause() != null) {
            cause = (Exception)cause.getCause();
        }
        if (cause instanceof Error) {
            throw (Error)cause;
        }
        if (cause instanceof RuntimeException) {
            throw (RuntimeException)cause;
        }
        throw new IllegalArgumentException("Failed to get property '" + s + "' of default " + o.getClass().getName() + " instance");
    }
    
    protected BeanPropertyWriter buildWriter(final BeanPropertyDefinition beanPropertyDefinition, final JavaType javaType, final JsonSerializer<?> jsonSerializer, final TypeSerializer typeSerializer, final TypeSerializer typeSerializer2, final AnnotatedMember annotatedMember, final boolean b) {
        JavaType javaType2 = this.findSerializationType(annotatedMember, b, javaType);
        if (typeSerializer2 != null) {
            JavaType javaType3;
            if ((javaType3 = javaType2) == null) {
                javaType3 = javaType;
            }
            if (javaType3.getContentType() == null) {
                throw new IllegalStateException("Problem trying to create BeanPropertyWriter for property '" + beanPropertyDefinition.getName() + "' (of type " + this._beanDesc.getType() + "); serialization type " + javaType3 + " has no content");
            }
            javaType2 = javaType3.withContentTypeHandler(typeSerializer2);
            javaType2.getContentType();
        }
        final Object o = null;
        boolean b2 = false;
        final boolean b3 = false;
        final JsonInclude.Include serializationInclusion = this._annotationIntrospector.findSerializationInclusion(annotatedMember, this._outputProps);
        boolean b4 = b3;
        Object o2 = o;
        Label_0200: {
            if (serializationInclusion != null) {
                switch (serializationInclusion) {
                    default: {
                        o2 = o;
                        b4 = b3;
                        break;
                    }
                    case NON_DEFAULT: {
                        final Object defaultValue = this.getDefaultValue(beanPropertyDefinition.getName(), annotatedMember);
                        if (defaultValue == null) {
                            b2 = true;
                            o2 = defaultValue;
                            break Label_0200;
                        }
                        b4 = b3;
                        o2 = defaultValue;
                        if (defaultValue.getClass().isArray()) {
                            o2 = ArrayBuilders.getArrayComparator(defaultValue);
                            b2 = false;
                            break Label_0200;
                        }
                        break;
                    }
                    case NON_EMPTY: {
                        o2 = BeanPropertyWriter.MARKER_FOR_EMPTY;
                        b2 = true;
                        break Label_0200;
                    }
                    case NON_NULL: {
                        b2 = true;
                    }
                    case ALWAYS: {
                        b4 = b2;
                        o2 = o;
                        if (!javaType.isContainerType()) {
                            break;
                        }
                        b4 = b2;
                        o2 = o;
                        if (!this._config.isEnabled(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS)) {
                            o2 = BeanPropertyWriter.MARKER_FOR_EMPTY;
                            break Label_0200;
                        }
                        break;
                    }
                }
            }
            b2 = b4;
        }
        final BeanPropertyWriter beanPropertyWriter = new BeanPropertyWriter(beanPropertyDefinition, annotatedMember, this._beanDesc.getClassAnnotations(), javaType, jsonSerializer, typeSerializer, javaType2, b2, o2);
        final NameTransformer unwrappingNameTransformer = this._annotationIntrospector.findUnwrappingNameTransformer(annotatedMember);
        BeanPropertyWriter unwrappingWriter = beanPropertyWriter;
        if (unwrappingNameTransformer != null) {
            unwrappingWriter = beanPropertyWriter.unwrappingWriter(unwrappingNameTransformer);
        }
        return unwrappingWriter;
    }
    
    protected JavaType findSerializationType(final Annotated annotated, boolean b, JavaType javaType) {
        final boolean b2 = true;
        final Class<?> serializationType = this._annotationIntrospector.findSerializationType(annotated);
        if (serializationType != null) {
            final Class<?> rawClass = javaType.getRawClass();
            if (serializationType.isAssignableFrom(rawClass)) {
                javaType = javaType.widenBy(serializationType);
            }
            else {
                if (!rawClass.isAssignableFrom(serializationType)) {
                    throw new IllegalArgumentException("Illegal concrete-type annotation for method '" + annotated.getName() + "': class " + serializationType.getName() + " not a super-type of (declared) class " + rawClass.getName());
                }
                javaType = this._config.constructSpecializedType(javaType, serializationType);
            }
            b = true;
        }
        final JavaType modifySecondaryTypesByAnnotation = BasicSerializerFactory.modifySecondaryTypesByAnnotation(this._config, annotated, javaType);
        if (modifySecondaryTypesByAnnotation != javaType) {
            javaType = modifySecondaryTypesByAnnotation;
            b = true;
        }
        if (!b) {
            final JsonSerialize.Typing serializationTyping = this._annotationIntrospector.findSerializationTyping(annotated);
            if (serializationTyping != null) {
                if (serializationTyping == JsonSerialize.Typing.STATIC) {
                    b = b2;
                }
                else {
                    b = false;
                }
            }
        }
        while (true) {
            if (b) {
                return javaType;
            }
            return null;
            continue;
        }
    }
    
    public Annotations getClassAnnotations() {
        return this._beanDesc.getClassAnnotations();
    }
    
    protected Object getDefaultBean() {
        if (this._defaultBean == null) {
            this._defaultBean = this._beanDesc.instantiateBean(this._config.canOverrideAccessModifiers());
            if (this._defaultBean == null) {
                throw new IllegalArgumentException("Class " + this._beanDesc.getClassInfo().getAnnotated().getName() + " has no default constructor; can not instantiate default bean value to support 'properties=JsonSerialize.Inclusion.NON_DEFAULT' annotation");
            }
        }
        return this._defaultBean;
    }
    
    protected Object getDefaultValue(final String s, final AnnotatedMember annotatedMember) {
        final Object defaultBean = this.getDefaultBean();
        try {
            return annotatedMember.getValue(defaultBean);
        }
        catch (Exception ex) {
            return this._throwWrapped(ex, s, defaultBean);
        }
    }
}
