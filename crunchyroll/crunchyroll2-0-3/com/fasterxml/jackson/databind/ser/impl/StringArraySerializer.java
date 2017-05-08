// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.ser.ContainerSerializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.ArraySerializerBase;

@JacksonStdImpl
public class StringArraySerializer extends ArraySerializerBase<String[]> implements ContextualSerializer
{
    private static final JavaType VALUE_TYPE;
    public static final StringArraySerializer instance;
    protected final JsonSerializer<Object> _elementSerializer;
    
    static {
        VALUE_TYPE = TypeFactory.defaultInstance().uncheckedSimpleType(String.class);
        instance = new StringArraySerializer();
    }
    
    protected StringArraySerializer() {
        super(String[].class, null);
        this._elementSerializer = null;
    }
    
    public StringArraySerializer(final StringArraySerializer stringArraySerializer, final BeanProperty beanProperty, final JsonSerializer<?> elementSerializer) {
        super(stringArraySerializer, beanProperty);
        this._elementSerializer = (JsonSerializer<Object>)elementSerializer;
    }
    
    private void serializeContentsSlow(final String[] array, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider, final JsonSerializer<Object> jsonSerializer) throws IOException, JsonGenerationException {
        for (int i = 0; i < array.length; ++i) {
            if (array[i] == null) {
                serializerProvider.defaultSerializeNull(jsonGenerator);
            }
            else {
                jsonSerializer.serialize(array[i], jsonGenerator, serializerProvider);
            }
        }
    }
    
    public ContainerSerializer<?> _withValueTypeSerializer(final TypeSerializer typeSerializer) {
        return this;
    }
    
    @Override
    public JsonSerializer<?> createContextual(final SerializerProvider serializerProvider, final BeanProperty beanProperty) throws JsonMappingException {
        while (true) {
            Label_0134: {
                if (beanProperty == null) {
                    break Label_0134;
                }
                final AnnotatedMember member = beanProperty.getMember();
                if (member == null) {
                    break Label_0134;
                }
                final Object contentSerializer = serializerProvider.getAnnotationIntrospector().findContentSerializer(member);
                if (contentSerializer == null) {
                    break Label_0134;
                }
                final JsonSerializer<?> serializerInstance = serializerProvider.serializerInstance(member, contentSerializer);
                JsonSerializer<?> elementSerializer = serializerInstance;
                if (serializerInstance == null) {
                    elementSerializer = this._elementSerializer;
                }
                final JsonSerializer<?> convertingContentSerializer = this.findConvertingContentSerializer(serializerProvider, beanProperty, elementSerializer);
                JsonSerializer<?> jsonSerializer;
                if (convertingContentSerializer == null) {
                    jsonSerializer = serializerProvider.findValueSerializer(String.class, beanProperty);
                }
                else {
                    jsonSerializer = convertingContentSerializer;
                    if (convertingContentSerializer instanceof ContextualSerializer) {
                        jsonSerializer = ((ContextualSerializer)convertingContentSerializer).createContextual(serializerProvider, beanProperty);
                    }
                }
                JsonSerializer<?> jsonSerializer2 = jsonSerializer;
                if (this.isDefaultSerializer(jsonSerializer)) {
                    jsonSerializer2 = null;
                }
                if (jsonSerializer2 == this._elementSerializer) {
                    return this;
                }
                return new StringArraySerializer(this, beanProperty, jsonSerializer2);
            }
            final JsonSerializer<?> serializerInstance = null;
            continue;
        }
    }
    
    @Override
    public boolean hasSingleElement(final String[] array) {
        return array.length == 1;
    }
    
    @Override
    public boolean isEmpty(final String[] array) {
        return array == null || array.length == 0;
    }
    
    public void serializeContents(final String[] array, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        final int length = array.length;
        if (length != 0) {
            if (this._elementSerializer != null) {
                this.serializeContentsSlow(array, jsonGenerator, serializerProvider, this._elementSerializer);
                return;
            }
            for (int i = 0; i < length; ++i) {
                if (array[i] == null) {
                    jsonGenerator.writeNull();
                }
                else {
                    jsonGenerator.writeString(array[i]);
                }
            }
        }
    }
}
