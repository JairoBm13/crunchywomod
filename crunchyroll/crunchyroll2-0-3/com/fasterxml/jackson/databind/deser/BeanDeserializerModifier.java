// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import java.util.List;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.DeserializationConfig;

public abstract class BeanDeserializerModifier
{
    public JsonDeserializer<?> modifyArrayDeserializer(final DeserializationConfig deserializationConfig, final ArrayType arrayType, final BeanDescription beanDescription, final JsonDeserializer<?> jsonDeserializer) {
        return jsonDeserializer;
    }
    
    public JsonDeserializer<?> modifyCollectionDeserializer(final DeserializationConfig deserializationConfig, final CollectionType collectionType, final BeanDescription beanDescription, final JsonDeserializer<?> jsonDeserializer) {
        return jsonDeserializer;
    }
    
    public JsonDeserializer<?> modifyCollectionLikeDeserializer(final DeserializationConfig deserializationConfig, final CollectionLikeType collectionLikeType, final BeanDescription beanDescription, final JsonDeserializer<?> jsonDeserializer) {
        return jsonDeserializer;
    }
    
    public JsonDeserializer<?> modifyDeserializer(final DeserializationConfig deserializationConfig, final BeanDescription beanDescription, final JsonDeserializer<?> jsonDeserializer) {
        return jsonDeserializer;
    }
    
    public JsonDeserializer<?> modifyEnumDeserializer(final DeserializationConfig deserializationConfig, final JavaType javaType, final BeanDescription beanDescription, final JsonDeserializer<?> jsonDeserializer) {
        return jsonDeserializer;
    }
    
    public KeyDeserializer modifyKeyDeserializer(final DeserializationConfig deserializationConfig, final JavaType javaType, final KeyDeserializer keyDeserializer) {
        return keyDeserializer;
    }
    
    public JsonDeserializer<?> modifyMapDeserializer(final DeserializationConfig deserializationConfig, final MapType mapType, final BeanDescription beanDescription, final JsonDeserializer<?> jsonDeserializer) {
        return jsonDeserializer;
    }
    
    public JsonDeserializer<?> modifyMapLikeDeserializer(final DeserializationConfig deserializationConfig, final MapLikeType mapLikeType, final BeanDescription beanDescription, final JsonDeserializer<?> jsonDeserializer) {
        return jsonDeserializer;
    }
    
    public BeanDeserializerBuilder updateBuilder(final DeserializationConfig deserializationConfig, final BeanDescription beanDescription, final BeanDeserializerBuilder beanDeserializerBuilder) {
        return beanDeserializerBuilder;
    }
    
    public List<BeanPropertyDefinition> updateProperties(final DeserializationConfig deserializationConfig, final BeanDescription beanDescription, final List<BeanPropertyDefinition> list) {
        return list;
    }
}
