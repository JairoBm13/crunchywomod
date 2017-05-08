// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.jsontype;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;

public abstract class TypeSerializer
{
    public abstract TypeSerializer forProperty(final BeanProperty p0);
    
    public abstract void writeCustomTypePrefixForObject(final Object p0, final JsonGenerator p1, final String p2) throws IOException, JsonProcessingException;
    
    public abstract void writeCustomTypeSuffixForObject(final Object p0, final JsonGenerator p1, final String p2) throws IOException, JsonProcessingException;
    
    public abstract void writeTypePrefixForArray(final Object p0, final JsonGenerator p1) throws IOException, JsonProcessingException;
    
    public abstract void writeTypePrefixForObject(final Object p0, final JsonGenerator p1) throws IOException, JsonProcessingException;
    
    public abstract void writeTypePrefixForScalar(final Object p0, final JsonGenerator p1) throws IOException, JsonProcessingException;
    
    public void writeTypePrefixForScalar(final Object o, final JsonGenerator jsonGenerator, final Class<?> clazz) throws IOException, JsonProcessingException {
        this.writeTypePrefixForScalar(o, jsonGenerator);
    }
    
    public abstract void writeTypeSuffixForArray(final Object p0, final JsonGenerator p1) throws IOException, JsonProcessingException;
    
    public abstract void writeTypeSuffixForObject(final Object p0, final JsonGenerator p1) throws IOException, JsonProcessingException;
    
    public abstract void writeTypeSuffixForScalar(final Object p0, final JsonGenerator p1) throws IOException, JsonProcessingException;
}
