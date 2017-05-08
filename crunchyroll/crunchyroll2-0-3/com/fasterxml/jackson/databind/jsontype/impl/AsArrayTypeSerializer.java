// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

public class AsArrayTypeSerializer extends TypeSerializerBase
{
    public AsArrayTypeSerializer(final TypeIdResolver typeIdResolver, final BeanProperty beanProperty) {
        super(typeIdResolver, beanProperty);
    }
    
    @Override
    public AsArrayTypeSerializer forProperty(final BeanProperty beanProperty) {
        if (this._property == beanProperty) {
            return this;
        }
        return new AsArrayTypeSerializer(this._idResolver, beanProperty);
    }
    
    @Override
    public void writeCustomTypePrefixForObject(final Object o, final JsonGenerator jsonGenerator, final String s) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartArray();
        jsonGenerator.writeString(s);
        jsonGenerator.writeStartObject();
    }
    
    @Override
    public void writeCustomTypeSuffixForObject(final Object o, final JsonGenerator jsonGenerator, final String s) throws IOException, JsonProcessingException {
        this.writeTypeSuffixForObject(o, jsonGenerator);
    }
    
    @Override
    public void writeTypePrefixForArray(final Object o, final JsonGenerator jsonGenerator) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartArray();
        jsonGenerator.writeString(this.idFromValue(o));
        jsonGenerator.writeStartArray();
    }
    
    @Override
    public void writeTypePrefixForObject(final Object o, final JsonGenerator jsonGenerator) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartArray();
        jsonGenerator.writeString(this.idFromValue(o));
        jsonGenerator.writeStartObject();
    }
    
    @Override
    public void writeTypePrefixForScalar(final Object o, final JsonGenerator jsonGenerator) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartArray();
        jsonGenerator.writeString(this.idFromValue(o));
    }
    
    @Override
    public void writeTypePrefixForScalar(final Object o, final JsonGenerator jsonGenerator, final Class<?> clazz) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartArray();
        jsonGenerator.writeString(this.idFromValueAndType(o, clazz));
    }
    
    @Override
    public void writeTypeSuffixForArray(final Object o, final JsonGenerator jsonGenerator) throws IOException, JsonProcessingException {
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndArray();
    }
    
    @Override
    public void writeTypeSuffixForObject(final Object o, final JsonGenerator jsonGenerator) throws IOException, JsonProcessingException {
        jsonGenerator.writeEndObject();
        jsonGenerator.writeEndArray();
    }
    
    @Override
    public void writeTypeSuffixForScalar(final Object o, final JsonGenerator jsonGenerator) throws IOException, JsonProcessingException {
        jsonGenerator.writeEndArray();
    }
}
