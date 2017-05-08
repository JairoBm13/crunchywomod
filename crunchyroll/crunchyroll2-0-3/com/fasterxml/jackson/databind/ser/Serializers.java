// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.SerializationConfig;

public interface Serializers
{
    JsonSerializer<?> findArraySerializer(final SerializationConfig p0, final ArrayType p1, final BeanDescription p2, final TypeSerializer p3, final JsonSerializer<Object> p4);
    
    JsonSerializer<?> findCollectionLikeSerializer(final SerializationConfig p0, final CollectionLikeType p1, final BeanDescription p2, final TypeSerializer p3, final JsonSerializer<Object> p4);
    
    JsonSerializer<?> findCollectionSerializer(final SerializationConfig p0, final CollectionType p1, final BeanDescription p2, final TypeSerializer p3, final JsonSerializer<Object> p4);
    
    JsonSerializer<?> findMapLikeSerializer(final SerializationConfig p0, final MapLikeType p1, final BeanDescription p2, final JsonSerializer<Object> p3, final TypeSerializer p4, final JsonSerializer<Object> p5);
    
    JsonSerializer<?> findMapSerializer(final SerializationConfig p0, final MapType p1, final BeanDescription p2, final JsonSerializer<Object> p3, final TypeSerializer p4, final JsonSerializer<Object> p5);
    
    JsonSerializer<?> findSerializer(final SerializationConfig p0, final JavaType p1, final BeanDescription p2);
    
    public static class Base implements Serializers
    {
        @Override
        public JsonSerializer<?> findArraySerializer(final SerializationConfig serializationConfig, final ArrayType arrayType, final BeanDescription beanDescription, final TypeSerializer typeSerializer, final JsonSerializer<Object> jsonSerializer) {
            return null;
        }
        
        @Override
        public JsonSerializer<?> findCollectionLikeSerializer(final SerializationConfig serializationConfig, final CollectionLikeType collectionLikeType, final BeanDescription beanDescription, final TypeSerializer typeSerializer, final JsonSerializer<Object> jsonSerializer) {
            return null;
        }
        
        @Override
        public JsonSerializer<?> findCollectionSerializer(final SerializationConfig serializationConfig, final CollectionType collectionType, final BeanDescription beanDescription, final TypeSerializer typeSerializer, final JsonSerializer<Object> jsonSerializer) {
            return null;
        }
        
        @Override
        public JsonSerializer<?> findMapLikeSerializer(final SerializationConfig serializationConfig, final MapLikeType mapLikeType, final BeanDescription beanDescription, final JsonSerializer<Object> jsonSerializer, final TypeSerializer typeSerializer, final JsonSerializer<Object> jsonSerializer2) {
            return null;
        }
        
        @Override
        public JsonSerializer<?> findMapSerializer(final SerializationConfig serializationConfig, final MapType mapType, final BeanDescription beanDescription, final JsonSerializer<Object> jsonSerializer, final TypeSerializer typeSerializer, final JsonSerializer<Object> jsonSerializer2) {
            return null;
        }
        
        @Override
        public JsonSerializer<?> findSerializer(final SerializationConfig serializationConfig, final JavaType javaType, final BeanDescription beanDescription) {
            return null;
        }
    }
}
