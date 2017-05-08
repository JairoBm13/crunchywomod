// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

public class AsExternalTypeSerializer extends TypeSerializerBase
{
    protected final String _typePropertyName;
    
    public AsExternalTypeSerializer(final TypeIdResolver typeIdResolver, final BeanProperty beanProperty, final String typePropertyName) {
        super(typeIdResolver, beanProperty);
        this._typePropertyName = typePropertyName;
    }
    
    protected final void _writeArrayPrefix(final Object o, final JsonGenerator jsonGenerator) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartArray();
    }
    
    protected final void _writeArraySuffix(final Object o, final JsonGenerator jsonGenerator, final String s) throws IOException, JsonProcessingException {
        jsonGenerator.writeEndArray();
        jsonGenerator.writeStringField(this._typePropertyName, s);
    }
    
    protected final void _writeObjectPrefix(final Object o, final JsonGenerator jsonGenerator) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();
    }
    
    protected final void _writeObjectSuffix(final Object o, final JsonGenerator jsonGenerator, final String s) throws IOException, JsonProcessingException {
        jsonGenerator.writeEndObject();
        jsonGenerator.writeStringField(this._typePropertyName, s);
    }
    
    protected final void _writeScalarPrefix(final Object o, final JsonGenerator jsonGenerator) throws IOException, JsonProcessingException {
    }
    
    protected final void _writeScalarSuffix(final Object o, final JsonGenerator jsonGenerator, final String s) throws IOException, JsonProcessingException {
        jsonGenerator.writeStringField(this._typePropertyName, s);
    }
    
    @Override
    public AsExternalTypeSerializer forProperty(final BeanProperty beanProperty) {
        if (this._property == beanProperty) {
            return this;
        }
        return new AsExternalTypeSerializer(this._idResolver, beanProperty, this._typePropertyName);
    }
    
    @Override
    public void writeCustomTypePrefixForObject(final Object o, final JsonGenerator jsonGenerator, final String s) throws IOException, JsonProcessingException {
        this._writeObjectPrefix(o, jsonGenerator);
    }
    
    @Override
    public void writeCustomTypeSuffixForObject(final Object o, final JsonGenerator jsonGenerator, final String s) throws IOException, JsonProcessingException {
        this._writeObjectSuffix(o, jsonGenerator, s);
    }
    
    @Override
    public void writeTypePrefixForArray(final Object o, final JsonGenerator jsonGenerator) throws IOException, JsonProcessingException {
        this._writeArrayPrefix(o, jsonGenerator);
    }
    
    @Override
    public void writeTypePrefixForObject(final Object o, final JsonGenerator jsonGenerator) throws IOException, JsonProcessingException {
        this._writeObjectPrefix(o, jsonGenerator);
    }
    
    @Override
    public void writeTypePrefixForScalar(final Object o, final JsonGenerator jsonGenerator) throws IOException, JsonProcessingException {
        this._writeScalarPrefix(o, jsonGenerator);
    }
    
    @Override
    public void writeTypePrefixForScalar(final Object o, final JsonGenerator jsonGenerator, final Class<?> clazz) throws IOException, JsonProcessingException {
        this._writeScalarPrefix(o, jsonGenerator);
    }
    
    @Override
    public void writeTypeSuffixForArray(final Object o, final JsonGenerator jsonGenerator) throws IOException, JsonProcessingException {
        this._writeArraySuffix(o, jsonGenerator, this.idFromValue(o));
    }
    
    @Override
    public void writeTypeSuffixForObject(final Object o, final JsonGenerator jsonGenerator) throws IOException, JsonProcessingException {
        this._writeObjectSuffix(o, jsonGenerator, this.idFromValue(o));
    }
    
    @Override
    public void writeTypeSuffixForScalar(final Object o, final JsonGenerator jsonGenerator) throws IOException, JsonProcessingException {
        this._writeScalarSuffix(o, jsonGenerator, this.idFromValue(o));
    }
}
