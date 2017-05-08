// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.DeserializationContext;

public abstract class DeserializerFactory
{
    protected static final Deserializers[] NO_DESERIALIZERS;
    
    static {
        NO_DESERIALIZERS = new Deserializers[0];
    }
    
    public abstract JsonDeserializer<?> createArrayDeserializer(final DeserializationContext p0, final ArrayType p1, final BeanDescription p2) throws JsonMappingException;
    
    public abstract JsonDeserializer<Object> createBeanDeserializer(final DeserializationContext p0, final JavaType p1, final BeanDescription p2) throws JsonMappingException;
    
    public abstract JsonDeserializer<Object> createBuilderBasedDeserializer(final DeserializationContext p0, final JavaType p1, final BeanDescription p2, final Class<?> p3) throws JsonMappingException;
    
    public abstract JsonDeserializer<?> createCollectionDeserializer(final DeserializationContext p0, final CollectionType p1, final BeanDescription p2) throws JsonMappingException;
    
    public abstract JsonDeserializer<?> createCollectionLikeDeserializer(final DeserializationContext p0, final CollectionLikeType p1, final BeanDescription p2) throws JsonMappingException;
    
    public abstract JsonDeserializer<?> createEnumDeserializer(final DeserializationContext p0, final JavaType p1, final BeanDescription p2) throws JsonMappingException;
    
    public abstract KeyDeserializer createKeyDeserializer(final DeserializationContext p0, final JavaType p1) throws JsonMappingException;
    
    public abstract JsonDeserializer<?> createMapDeserializer(final DeserializationContext p0, final MapType p1, final BeanDescription p2) throws JsonMappingException;
    
    public abstract JsonDeserializer<?> createMapLikeDeserializer(final DeserializationContext p0, final MapLikeType p1, final BeanDescription p2) throws JsonMappingException;
    
    public abstract JsonDeserializer<?> createTreeDeserializer(final DeserializationConfig p0, final JavaType p1, final BeanDescription p2) throws JsonMappingException;
    
    public abstract TypeDeserializer findTypeDeserializer(final DeserializationConfig p0, final JavaType p1) throws JsonMappingException;
    
    public abstract JavaType mapAbstractType(final DeserializationConfig p0, final JavaType p1) throws JsonMappingException;
}
