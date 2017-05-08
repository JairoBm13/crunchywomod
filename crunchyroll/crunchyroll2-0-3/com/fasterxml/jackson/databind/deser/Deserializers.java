// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.type.ArrayType;

public interface Deserializers
{
    JsonDeserializer<?> findArrayDeserializer(final ArrayType p0, final DeserializationConfig p1, final BeanDescription p2, final TypeDeserializer p3, final JsonDeserializer<?> p4) throws JsonMappingException;
    
    JsonDeserializer<?> findBeanDeserializer(final JavaType p0, final DeserializationConfig p1, final BeanDescription p2) throws JsonMappingException;
    
    JsonDeserializer<?> findCollectionDeserializer(final CollectionType p0, final DeserializationConfig p1, final BeanDescription p2, final TypeDeserializer p3, final JsonDeserializer<?> p4) throws JsonMappingException;
    
    JsonDeserializer<?> findCollectionLikeDeserializer(final CollectionLikeType p0, final DeserializationConfig p1, final BeanDescription p2, final TypeDeserializer p3, final JsonDeserializer<?> p4) throws JsonMappingException;
    
    JsonDeserializer<?> findEnumDeserializer(final Class<?> p0, final DeserializationConfig p1, final BeanDescription p2) throws JsonMappingException;
    
    JsonDeserializer<?> findMapDeserializer(final MapType p0, final DeserializationConfig p1, final BeanDescription p2, final KeyDeserializer p3, final TypeDeserializer p4, final JsonDeserializer<?> p5) throws JsonMappingException;
    
    JsonDeserializer<?> findMapLikeDeserializer(final MapLikeType p0, final DeserializationConfig p1, final BeanDescription p2, final KeyDeserializer p3, final TypeDeserializer p4, final JsonDeserializer<?> p5) throws JsonMappingException;
    
    JsonDeserializer<?> findTreeNodeDeserializer(final Class<? extends JsonNode> p0, final DeserializationConfig p1, final BeanDescription p2) throws JsonMappingException;
    
    public static class Base implements Deserializers
    {
        @Override
        public JsonDeserializer<?> findArrayDeserializer(final ArrayType arrayType, final DeserializationConfig deserializationConfig, final BeanDescription beanDescription, final TypeDeserializer typeDeserializer, final JsonDeserializer<?> jsonDeserializer) throws JsonMappingException {
            return null;
        }
        
        @Override
        public JsonDeserializer<?> findBeanDeserializer(final JavaType javaType, final DeserializationConfig deserializationConfig, final BeanDescription beanDescription) throws JsonMappingException {
            return null;
        }
        
        @Override
        public JsonDeserializer<?> findCollectionDeserializer(final CollectionType collectionType, final DeserializationConfig deserializationConfig, final BeanDescription beanDescription, final TypeDeserializer typeDeserializer, final JsonDeserializer<?> jsonDeserializer) throws JsonMappingException {
            return null;
        }
        
        @Override
        public JsonDeserializer<?> findCollectionLikeDeserializer(final CollectionLikeType collectionLikeType, final DeserializationConfig deserializationConfig, final BeanDescription beanDescription, final TypeDeserializer typeDeserializer, final JsonDeserializer<?> jsonDeserializer) throws JsonMappingException {
            return null;
        }
        
        @Override
        public JsonDeserializer<?> findEnumDeserializer(final Class<?> clazz, final DeserializationConfig deserializationConfig, final BeanDescription beanDescription) throws JsonMappingException {
            return null;
        }
        
        @Override
        public JsonDeserializer<?> findMapDeserializer(final MapType mapType, final DeserializationConfig deserializationConfig, final BeanDescription beanDescription, final KeyDeserializer keyDeserializer, final TypeDeserializer typeDeserializer, final JsonDeserializer<?> jsonDeserializer) throws JsonMappingException {
            return null;
        }
        
        @Override
        public JsonDeserializer<?> findMapLikeDeserializer(final MapLikeType mapLikeType, final DeserializationConfig deserializationConfig, final BeanDescription beanDescription, final KeyDeserializer keyDeserializer, final TypeDeserializer typeDeserializer, final JsonDeserializer<?> jsonDeserializer) throws JsonMappingException {
            return null;
        }
        
        @Override
        public JsonDeserializer<?> findTreeNodeDeserializer(final Class<? extends JsonNode> clazz, final DeserializationConfig deserializationConfig, final BeanDescription beanDescription) throws JsonMappingException {
            return null;
        }
    }
}
