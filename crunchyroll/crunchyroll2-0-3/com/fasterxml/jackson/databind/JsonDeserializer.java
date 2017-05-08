// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.databind.util.NameTransformer;
import java.util.Collection;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonParser;

public abstract class JsonDeserializer<T>
{
    public abstract T deserialize(final JsonParser p0, final DeserializationContext p1) throws IOException, JsonProcessingException;
    
    public T deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext, final T t) throws IOException, JsonProcessingException {
        throw new UnsupportedOperationException("Can not update object of type " + t.getClass().getName() + " (by deserializer of type " + this.getClass().getName() + ")");
    }
    
    public Object deserializeWithType(final JsonParser jsonParser, final DeserializationContext deserializationContext, final TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        return typeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext);
    }
    
    public T getEmptyValue() {
        return this.getNullValue();
    }
    
    public Collection<Object> getKnownPropertyNames() {
        return null;
    }
    
    public T getNullValue() {
        return null;
    }
    
    public boolean isCachable() {
        return false;
    }
    
    public JsonDeserializer<T> unwrappingDeserializer(final NameTransformer nameTransformer) {
        return this;
    }
    
    public abstract static class None extends JsonDeserializer<Object>
    {
    }
}
