// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationConfig;

public abstract class SerializerFactory
{
    public abstract JsonSerializer<Object> createKeySerializer(final SerializationConfig p0, final JavaType p1, final JsonSerializer<Object> p2) throws JsonMappingException;
    
    public abstract JsonSerializer<Object> createSerializer(final SerializerProvider p0, final JavaType p1) throws JsonMappingException;
    
    public abstract TypeSerializer createTypeSerializer(final SerializationConfig p0, final JavaType p1) throws JsonMappingException;
}
