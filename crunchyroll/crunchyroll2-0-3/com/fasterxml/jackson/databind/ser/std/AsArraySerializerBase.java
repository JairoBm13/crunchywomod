// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ContainerSerializer;

public abstract class AsArraySerializerBase<T> extends ContainerSerializer<T> implements ContextualSerializer
{
    protected PropertySerializerMap _dynamicSerializers;
    protected final JsonSerializer<Object> _elementSerializer;
    protected final JavaType _elementType;
    protected final BeanProperty _property;
    protected final boolean _staticTyping;
    protected final TypeSerializer _valueTypeSerializer;
    
    protected AsArraySerializerBase(final AsArraySerializerBase<?> asArraySerializerBase, final BeanProperty property, final TypeSerializer valueTypeSerializer, final JsonSerializer<?> elementSerializer) {
        super(asArraySerializerBase);
        this._elementType = asArraySerializerBase._elementType;
        this._staticTyping = asArraySerializerBase._staticTyping;
        this._valueTypeSerializer = valueTypeSerializer;
        this._property = property;
        this._elementSerializer = (JsonSerializer<Object>)elementSerializer;
        this._dynamicSerializers = asArraySerializerBase._dynamicSerializers;
    }
    
    protected AsArraySerializerBase(final Class<?> clazz, final JavaType elementType, final boolean b, final TypeSerializer valueTypeSerializer, final BeanProperty property, final JsonSerializer<Object> elementSerializer) {
        final boolean b2 = false;
        super(clazz, false);
        this._elementType = elementType;
        boolean staticTyping = false;
        Label_0037: {
            if (!b) {
                staticTyping = b2;
                if (elementType == null) {
                    break Label_0037;
                }
                staticTyping = b2;
                if (!elementType.isFinal()) {
                    break Label_0037;
                }
            }
            staticTyping = true;
        }
        this._staticTyping = staticTyping;
        this._valueTypeSerializer = valueTypeSerializer;
        this._property = property;
        this._elementSerializer = elementSerializer;
        this._dynamicSerializers = PropertySerializerMap.emptyMap();
    }
    
    protected final JsonSerializer<Object> _findAndAddDynamic(final PropertySerializerMap propertySerializerMap, final JavaType javaType, final SerializerProvider serializerProvider) throws JsonMappingException {
        final PropertySerializerMap.SerializerAndMapResult andAddSerializer = propertySerializerMap.findAndAddSerializer(javaType, serializerProvider, this._property);
        if (propertySerializerMap != andAddSerializer.map) {
            this._dynamicSerializers = andAddSerializer.map;
        }
        return andAddSerializer.serializer;
    }
    
    protected final JsonSerializer<Object> _findAndAddDynamic(final PropertySerializerMap propertySerializerMap, final Class<?> clazz, final SerializerProvider serializerProvider) throws JsonMappingException {
        final PropertySerializerMap.SerializerAndMapResult andAddSerializer = propertySerializerMap.findAndAddSerializer(clazz, serializerProvider, this._property);
        if (propertySerializerMap != andAddSerializer.map) {
            this._dynamicSerializers = andAddSerializer.map;
        }
        return andAddSerializer.serializer;
    }
    
    @Override
    public JsonSerializer<?> createContextual(final SerializerProvider serializerProvider, final BeanProperty beanProperty) throws JsonMappingException {
        TypeSerializer typeSerializer = this._valueTypeSerializer;
        if (typeSerializer != null) {
            typeSerializer = typeSerializer.forProperty(beanProperty);
        }
        JsonSerializer<?> serializerInstance;
        final JsonSerializer<?> jsonSerializer = serializerInstance = null;
        if (beanProperty != null) {
            final AnnotatedMember member = beanProperty.getMember();
            serializerInstance = jsonSerializer;
            if (member != null) {
                final Object contentSerializer = serializerProvider.getAnnotationIntrospector().findContentSerializer(member);
                serializerInstance = jsonSerializer;
                if (contentSerializer != null) {
                    serializerInstance = serializerProvider.serializerInstance(member, contentSerializer);
                }
            }
        }
        JsonSerializer<?> elementSerializer;
        if ((elementSerializer = serializerInstance) == null) {
            elementSerializer = this._elementSerializer;
        }
        final JsonSerializer<?> convertingContentSerializer = this.findConvertingContentSerializer(serializerProvider, beanProperty, elementSerializer);
        JsonSerializer<?> jsonSerializer2 = null;
        Label_0148: {
            if (convertingContentSerializer == null) {
                if ((jsonSerializer2 = convertingContentSerializer) == null) {
                    jsonSerializer2 = convertingContentSerializer;
                    if (this._elementType != null) {
                        if (!this._staticTyping) {
                            jsonSerializer2 = convertingContentSerializer;
                            if (!this.hasContentTypeAnnotation(serializerProvider, beanProperty)) {
                                break Label_0148;
                            }
                        }
                        jsonSerializer2 = serializerProvider.findValueSerializer(this._elementType, beanProperty);
                    }
                }
            }
            else {
                jsonSerializer2 = convertingContentSerializer;
                if (convertingContentSerializer instanceof ContextualSerializer) {
                    jsonSerializer2 = ((ContextualSerializer)convertingContentSerializer).createContextual(serializerProvider, beanProperty);
                }
            }
        }
        if (jsonSerializer2 == this._elementSerializer && beanProperty == this._property) {
            final AsArraySerializerBase withResolved = this;
            if (this._valueTypeSerializer == typeSerializer) {
                return withResolved;
            }
        }
        return this.withResolved(beanProperty, typeSerializer, jsonSerializer2);
    }
    
    @Override
    public final void serialize(final T t, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        if (serializerProvider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED) && this.hasSingleElement(t)) {
            this.serializeContents(t, jsonGenerator, serializerProvider);
            return;
        }
        jsonGenerator.writeStartArray();
        this.serializeContents(t, jsonGenerator, serializerProvider);
        jsonGenerator.writeEndArray();
    }
    
    protected abstract void serializeContents(final T p0, final JsonGenerator p1, final SerializerProvider p2) throws IOException, JsonGenerationException;
    
    @Override
    public final void serializeWithType(final T t, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider, final TypeSerializer typeSerializer) throws IOException, JsonGenerationException {
        typeSerializer.writeTypePrefixForArray(t, jsonGenerator);
        this.serializeContents(t, jsonGenerator, serializerProvider);
        typeSerializer.writeTypeSuffixForArray(t, jsonGenerator);
    }
    
    public abstract AsArraySerializerBase<T> withResolved(final BeanProperty p0, final TypeSerializer p1, final JsonSerializer<?> p2);
}
