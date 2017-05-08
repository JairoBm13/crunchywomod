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

public class AsPropertyTypeSerializer extends AsArrayTypeSerializer
{
    protected final String _typePropertyName;
    
    public AsPropertyTypeSerializer(final TypeIdResolver typeIdResolver, final BeanProperty beanProperty, final String typePropertyName) {
        super(typeIdResolver, beanProperty);
        this._typePropertyName = typePropertyName;
    }
    
    @Override
    public AsPropertyTypeSerializer forProperty(final BeanProperty beanProperty) {
        if (this._property == beanProperty) {
            return this;
        }
        return new AsPropertyTypeSerializer(this._idResolver, beanProperty, this._typePropertyName);
    }
    
    @Override
    public void writeCustomTypePrefixForObject(final Object o, final JsonGenerator jsonGenerator, final String s) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField(this._typePropertyName, s);
    }
    
    @Override
    public void writeCustomTypeSuffixForObject(final Object o, final JsonGenerator jsonGenerator, final String s) throws IOException, JsonProcessingException {
        jsonGenerator.writeEndObject();
    }
    
    @Override
    public void writeTypePrefixForObject(final Object o, final JsonGenerator jsonGenerator) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField(this._typePropertyName, this.idFromValue(o));
    }
    
    @Override
    public void writeTypeSuffixForObject(final Object o, final JsonGenerator jsonGenerator) throws IOException, JsonProcessingException {
        jsonGenerator.writeEndObject();
    }
}
