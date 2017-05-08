// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.JavaType;

public abstract class TypeBase extends JavaType implements JsonSerializable
{
    transient volatile String _canonicalName;
    
    protected TypeBase(final Class<?> clazz, final int n, final Object o, final Object o2, final boolean b) {
        super(clazz, n, o, o2, b);
    }
    
    protected abstract String buildCanonicalName();
    
    @Override
    public <T> T getTypeHandler() {
        return (T)this._typeHandler;
    }
    
    @Override
    public <T> T getValueHandler() {
        return (T)this._valueHandler;
    }
    
    @Override
    public void serialize(final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeString(this.toCanonical());
    }
    
    @Override
    public void serializeWithType(final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider, final TypeSerializer typeSerializer) throws IOException, JsonProcessingException {
        typeSerializer.writeTypePrefixForScalar(this, jsonGenerator);
        this.serialize(jsonGenerator, serializerProvider);
        typeSerializer.writeTypeSuffixForScalar(this, jsonGenerator);
    }
    
    @Override
    public String toCanonical() {
        String s;
        if ((s = this._canonicalName) == null) {
            s = this.buildCanonicalName();
        }
        return s;
    }
}
