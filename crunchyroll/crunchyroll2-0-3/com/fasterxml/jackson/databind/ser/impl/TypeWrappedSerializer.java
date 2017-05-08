// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.JsonSerializer;

public final class TypeWrappedSerializer extends JsonSerializer<Object>
{
    protected final JsonSerializer<Object> _serializer;
    protected final TypeSerializer _typeSerializer;
    
    public TypeWrappedSerializer(final TypeSerializer typeSerializer, final JsonSerializer<?> serializer) {
        this._typeSerializer = typeSerializer;
        this._serializer = (JsonSerializer<Object>)serializer;
    }
    
    @Override
    public Class<Object> handledType() {
        return Object.class;
    }
    
    @Override
    public void serialize(final Object o, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        this._serializer.serializeWithType(o, jsonGenerator, serializerProvider, this._typeSerializer);
    }
    
    @Override
    public void serializeWithType(final Object o, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider, final TypeSerializer typeSerializer) throws IOException, JsonProcessingException {
        this._serializer.serializeWithType(o, jsonGenerator, serializerProvider, typeSerializer);
    }
}
