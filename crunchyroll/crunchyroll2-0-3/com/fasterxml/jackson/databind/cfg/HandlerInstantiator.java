// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.cfg;

import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.introspect.Annotated;

public abstract class HandlerInstantiator
{
    public Converter<?, ?> converterInstance(final MapperConfig<?> mapperConfig, final Annotated annotated, final Class<?> clazz) {
        return null;
    }
    
    public abstract JsonDeserializer<?> deserializerInstance(final DeserializationConfig p0, final Annotated p1, final Class<?> p2);
    
    public abstract KeyDeserializer keyDeserializerInstance(final DeserializationConfig p0, final Annotated p1, final Class<?> p2);
    
    public PropertyNamingStrategy namingStrategyInstance(final MapperConfig<?> mapperConfig, final Annotated annotated, final Class<?> clazz) {
        return null;
    }
    
    public ObjectIdGenerator<?> objectIdGeneratorInstance(final MapperConfig<?> mapperConfig, final Annotated annotated, final Class<?> clazz) {
        return null;
    }
    
    public abstract JsonSerializer<?> serializerInstance(final SerializationConfig p0, final Annotated p1, final Class<?> p2);
    
    public abstract TypeIdResolver typeIdResolverInstance(final MapperConfig<?> p0, final Annotated p1, final Class<?> p2);
    
    public abstract TypeResolverBuilder<?> typeResolverBuilderInstance(final MapperConfig<?> p0, final Annotated p1, final Class<?> p2);
    
    public ValueInstantiator valueInstantiatorInstance(final MapperConfig<?> mapperConfig, final Annotated annotated, final Class<?> clazz) {
        return null;
    }
}
