// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.SerializationFeature;
import java.lang.reflect.InvocationTargetException;
import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.JsonSerializer;

public abstract class StdSerializer<T> extends JsonSerializer<T> implements JsonFormatVisitable, SchemaAware
{
    protected final Class<T> _handledType;
    
    protected StdSerializer(final JavaType javaType) {
        this._handledType = (Class<T>)javaType.getRawClass();
    }
    
    protected StdSerializer(final Class<T> handledType) {
        this._handledType = handledType;
    }
    
    protected StdSerializer(final Class<?> handledType, final boolean b) {
        this._handledType = (Class<T>)handledType;
    }
    
    protected JsonSerializer<?> findConvertingContentSerializer(final SerializerProvider serializerProvider, final BeanProperty beanProperty, final JsonSerializer<?> jsonSerializer) throws JsonMappingException {
        final AnnotationIntrospector annotationIntrospector = serializerProvider.getAnnotationIntrospector();
        JsonSerializer<?> jsonSerializer2 = jsonSerializer;
        if (annotationIntrospector != null) {
            jsonSerializer2 = jsonSerializer;
            if (beanProperty != null) {
                final Object serializationContentConverter = annotationIntrospector.findSerializationContentConverter(beanProperty.getMember());
                jsonSerializer2 = jsonSerializer;
                if (serializationContentConverter != null) {
                    final Converter<Object, Object> converterInstance = serializerProvider.converterInstance(beanProperty.getMember(), serializationContentConverter);
                    final JavaType outputType = converterInstance.getOutputType(serializerProvider.getTypeFactory());
                    JsonSerializer<?> valueSerializer;
                    if ((valueSerializer = jsonSerializer) == null) {
                        valueSerializer = serializerProvider.findValueSerializer(outputType, beanProperty);
                    }
                    jsonSerializer2 = new StdDelegatingSerializer(converterInstance, outputType, valueSerializer);
                }
            }
        }
        return jsonSerializer2;
    }
    
    @Override
    public Class<T> handledType() {
        return this._handledType;
    }
    
    protected boolean isDefaultSerializer(final JsonSerializer<?> jsonSerializer) {
        return jsonSerializer != null && jsonSerializer.getClass().getAnnotation(JacksonStdImpl.class) != null;
    }
    
    @Override
    public abstract void serialize(final T p0, final JsonGenerator p1, final SerializerProvider p2) throws IOException, JsonGenerationException;
    
    public void wrapAndThrow(final SerializerProvider serializerProvider, Throwable cause, final Object o, final int n) throws IOException {
        while (cause instanceof InvocationTargetException && cause.getCause() != null) {
            cause = cause.getCause();
        }
        if (cause instanceof Error) {
            throw (Error)cause;
        }
        boolean b;
        if (serializerProvider == null || serializerProvider.isEnabled(SerializationFeature.WRAP_EXCEPTIONS)) {
            b = true;
        }
        else {
            b = false;
        }
        if (cause instanceof IOException) {
            if (!b || !(cause instanceof JsonMappingException)) {
                throw (IOException)cause;
            }
        }
        else if (!b && cause instanceof RuntimeException) {
            throw (RuntimeException)cause;
        }
        throw JsonMappingException.wrapWithPath(cause, o, n);
    }
    
    public void wrapAndThrow(final SerializerProvider serializerProvider, Throwable cause, final Object o, final String s) throws IOException {
        while (cause instanceof InvocationTargetException && cause.getCause() != null) {
            cause = cause.getCause();
        }
        if (cause instanceof Error) {
            throw (Error)cause;
        }
        boolean b;
        if (serializerProvider == null || serializerProvider.isEnabled(SerializationFeature.WRAP_EXCEPTIONS)) {
            b = true;
        }
        else {
            b = false;
        }
        if (cause instanceof IOException) {
            if (!b || !(cause instanceof JsonMappingException)) {
                throw (IOException)cause;
            }
        }
        else if (!b && cause instanceof RuntimeException) {
            throw (RuntimeException)cause;
        }
        throw JsonMappingException.wrapWithPath(cause, o, s);
    }
}
