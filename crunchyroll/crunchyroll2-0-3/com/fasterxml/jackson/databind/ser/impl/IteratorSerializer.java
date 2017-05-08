// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.impl;

import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ser.ContainerSerializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import java.util.Iterator;
import com.fasterxml.jackson.databind.ser.std.AsArraySerializerBase;

@JacksonStdImpl
public class IteratorSerializer extends AsArraySerializerBase<Iterator<?>>
{
    public IteratorSerializer(final JavaType javaType, final boolean b, final TypeSerializer typeSerializer, final BeanProperty beanProperty) {
        super(Iterator.class, javaType, b, typeSerializer, beanProperty, null);
    }
    
    public IteratorSerializer(final IteratorSerializer iteratorSerializer, final BeanProperty beanProperty, final TypeSerializer typeSerializer, final JsonSerializer<?> jsonSerializer) {
        super(iteratorSerializer, beanProperty, typeSerializer, jsonSerializer);
    }
    
    public ContainerSerializer<?> _withValueTypeSerializer(final TypeSerializer typeSerializer) {
        return new IteratorSerializer(this._elementType, this._staticTyping, typeSerializer, this._property);
    }
    
    @Override
    public boolean hasSingleElement(final Iterator<?> iterator) {
        return false;
    }
    
    @Override
    public boolean isEmpty(final Iterator<?> iterator) {
        return iterator == null || !iterator.hasNext();
    }
    
    public void serializeContents(final Iterator<?> iterator, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        Class<?> clazz = null;
        if (iterator.hasNext()) {
            final TypeSerializer valueTypeSerializer = this._valueTypeSerializer;
            JsonSerializer<Object> valueSerializer = null;
            do {
                final Object next = iterator.next();
                if (next == null) {
                    serializerProvider.defaultSerializeNull(jsonGenerator);
                }
                else {
                    final Class<?> class1 = next.getClass();
                    JsonSerializer<Object> jsonSerializer;
                    if (class1 == clazz) {
                        jsonSerializer = valueSerializer;
                    }
                    else {
                        valueSerializer = serializerProvider.findValueSerializer(class1, this._property);
                        clazz = class1;
                        jsonSerializer = valueSerializer;
                    }
                    if (valueTypeSerializer == null) {
                        jsonSerializer.serialize(next, jsonGenerator, serializerProvider);
                    }
                    else {
                        jsonSerializer.serializeWithType(next, jsonGenerator, serializerProvider, valueTypeSerializer);
                    }
                }
            } while (iterator.hasNext());
        }
    }
    
    @Override
    public IteratorSerializer withResolved(final BeanProperty beanProperty, final TypeSerializer typeSerializer, final JsonSerializer<?> jsonSerializer) {
        return new IteratorSerializer(this, beanProperty, typeSerializer, jsonSerializer);
    }
}
