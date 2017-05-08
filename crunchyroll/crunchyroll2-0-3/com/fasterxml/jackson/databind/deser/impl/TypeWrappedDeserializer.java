// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.JsonDeserializer;

public final class TypeWrappedDeserializer extends JsonDeserializer<Object>
{
    final JsonDeserializer<Object> _deserializer;
    final TypeDeserializer _typeDeserializer;
    
    public TypeWrappedDeserializer(final TypeDeserializer typeDeserializer, final JsonDeserializer<Object> deserializer) {
        this._typeDeserializer = typeDeserializer;
        this._deserializer = deserializer;
    }
    
    @Override
    public Object deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        return this._deserializer.deserializeWithType(jsonParser, deserializationContext, this._typeDeserializer);
    }
    
    @Override
    public Object deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext, final Object o) throws IOException, JsonProcessingException {
        return this._deserializer.deserialize(jsonParser, deserializationContext, o);
    }
    
    @Override
    public Object deserializeWithType(final JsonParser jsonParser, final DeserializationContext deserializationContext, final TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        throw new IllegalStateException("Type-wrapped deserializer's deserializeWithType should never get called");
    }
}
