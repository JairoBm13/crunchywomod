// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.type.ArrayType;
import java.util.List;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;

public abstract class BeanSerializerModifier
{
    public List<BeanPropertyWriter> changeProperties(final SerializationConfig serializationConfig, final BeanDescription beanDescription, final List<BeanPropertyWriter> list) {
        return list;
    }
    
    public JsonSerializer<?> modifyArraySerializer(final SerializationConfig serializationConfig, final ArrayType arrayType, final BeanDescription beanDescription, final JsonSerializer<?> jsonSerializer) {
        return jsonSerializer;
    }
    
    public JsonSerializer<?> modifyCollectionLikeSerializer(final SerializationConfig serializationConfig, final CollectionLikeType collectionLikeType, final BeanDescription beanDescription, final JsonSerializer<?> jsonSerializer) {
        return jsonSerializer;
    }
    
    public JsonSerializer<?> modifyCollectionSerializer(final SerializationConfig serializationConfig, final CollectionType collectionType, final BeanDescription beanDescription, final JsonSerializer<?> jsonSerializer) {
        return jsonSerializer;
    }
    
    public JsonSerializer<?> modifyEnumSerializer(final SerializationConfig serializationConfig, final JavaType javaType, final BeanDescription beanDescription, final JsonSerializer<?> jsonSerializer) {
        return jsonSerializer;
    }
    
    public JsonSerializer<?> modifyKeySerializer(final SerializationConfig serializationConfig, final JavaType javaType, final BeanDescription beanDescription, final JsonSerializer<?> jsonSerializer) {
        return jsonSerializer;
    }
    
    public JsonSerializer<?> modifyMapLikeSerializer(final SerializationConfig serializationConfig, final MapLikeType mapLikeType, final BeanDescription beanDescription, final JsonSerializer<?> jsonSerializer) {
        return jsonSerializer;
    }
    
    public JsonSerializer<?> modifyMapSerializer(final SerializationConfig serializationConfig, final MapType mapType, final BeanDescription beanDescription, final JsonSerializer<?> jsonSerializer) {
        return jsonSerializer;
    }
    
    public JsonSerializer<?> modifySerializer(final SerializationConfig serializationConfig, final BeanDescription beanDescription, final JsonSerializer<?> jsonSerializer) {
        return jsonSerializer;
    }
    
    public List<BeanPropertyWriter> orderProperties(final SerializationConfig serializationConfig, final BeanDescription beanDescription, final List<BeanPropertyWriter> list) {
        return list;
    }
    
    public BeanSerializerBuilder updateBuilder(final SerializationConfig serializationConfig, final BeanDescription beanDescription, final BeanSerializerBuilder beanSerializerBuilder) {
        return beanSerializerBuilder;
    }
}
