// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.databind.util.NameTransformer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;

public abstract class JsonSerializer<T> implements JsonFormatVisitable
{
    public Class<T> handledType() {
        return null;
    }
    
    public boolean isEmpty(final T t) {
        return t == null;
    }
    
    public boolean isUnwrappingSerializer() {
        return false;
    }
    
    public abstract void serialize(final T p0, final JsonGenerator p1, final SerializerProvider p2) throws IOException, JsonProcessingException;
    
    public void serializeWithType(final T t, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider, final TypeSerializer typeSerializer) throws IOException, JsonProcessingException {
        Class<?> clazz;
        if ((clazz = this.handledType()) == null) {
            clazz = t.getClass();
        }
        throw new UnsupportedOperationException("Type id handling not implemented for type " + clazz.getName());
    }
    
    public JsonSerializer<T> unwrappingSerializer(final NameTransformer nameTransformer) {
        return this;
    }
    
    public boolean usesObjectId() {
        return false;
    }
    
    public abstract static class None extends JsonSerializer<Object>
    {
    }
}
