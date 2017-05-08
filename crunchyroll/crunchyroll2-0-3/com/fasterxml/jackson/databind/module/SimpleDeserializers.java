// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.module;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.type.ClassKey;
import java.util.HashMap;
import java.io.Serializable;
import com.fasterxml.jackson.databind.deser.Deserializers;

public class SimpleDeserializers implements Deserializers, Serializable
{
    protected HashMap<ClassKey, JsonDeserializer<?>> _classMappings;
    
    public SimpleDeserializers() {
        this._classMappings = null;
    }
    
    @Override
    public JsonDeserializer<?> findArrayDeserializer(final ArrayType arrayType, final DeserializationConfig deserializationConfig, final BeanDescription beanDescription, final TypeDeserializer typeDeserializer, final JsonDeserializer<?> jsonDeserializer) throws JsonMappingException {
        if (this._classMappings == null) {
            return null;
        }
        return this._classMappings.get(new ClassKey(arrayType.getRawClass()));
    }
    
    @Override
    public JsonDeserializer<?> findBeanDeserializer(final JavaType javaType, final DeserializationConfig deserializationConfig, final BeanDescription beanDescription) throws JsonMappingException {
        if (this._classMappings == null) {
            return null;
        }
        return this._classMappings.get(new ClassKey(javaType.getRawClass()));
    }
    
    @Override
    public JsonDeserializer<?> findCollectionDeserializer(final CollectionType collectionType, final DeserializationConfig deserializationConfig, final BeanDescription beanDescription, final TypeDeserializer typeDeserializer, final JsonDeserializer<?> jsonDeserializer) throws JsonMappingException {
        if (this._classMappings == null) {
            return null;
        }
        return this._classMappings.get(new ClassKey(collectionType.getRawClass()));
    }
    
    @Override
    public JsonDeserializer<?> findCollectionLikeDeserializer(final CollectionLikeType collectionLikeType, final DeserializationConfig deserializationConfig, final BeanDescription beanDescription, final TypeDeserializer typeDeserializer, final JsonDeserializer<?> jsonDeserializer) throws JsonMappingException {
        if (this._classMappings == null) {
            return null;
        }
        return this._classMappings.get(new ClassKey(collectionLikeType.getRawClass()));
    }
    
    @Override
    public JsonDeserializer<?> findEnumDeserializer(final Class<?> clazz, final DeserializationConfig deserializationConfig, final BeanDescription beanDescription) throws JsonMappingException {
        if (this._classMappings == null) {
            return null;
        }
        return this._classMappings.get(new ClassKey(clazz));
    }
    
    @Override
    public JsonDeserializer<?> findMapDeserializer(final MapType mapType, final DeserializationConfig deserializationConfig, final BeanDescription beanDescription, final KeyDeserializer keyDeserializer, final TypeDeserializer typeDeserializer, final JsonDeserializer<?> jsonDeserializer) throws JsonMappingException {
        if (this._classMappings == null) {
            return null;
        }
        return this._classMappings.get(new ClassKey(mapType.getRawClass()));
    }
    
    @Override
    public JsonDeserializer<?> findMapLikeDeserializer(final MapLikeType mapLikeType, final DeserializationConfig deserializationConfig, final BeanDescription beanDescription, final KeyDeserializer keyDeserializer, final TypeDeserializer typeDeserializer, final JsonDeserializer<?> jsonDeserializer) throws JsonMappingException {
        if (this._classMappings == null) {
            return null;
        }
        return this._classMappings.get(new ClassKey(mapLikeType.getRawClass()));
    }
    
    @Override
    public JsonDeserializer<?> findTreeNodeDeserializer(final Class<? extends JsonNode> clazz, final DeserializationConfig deserializationConfig, final BeanDescription beanDescription) throws JsonMappingException {
        if (this._classMappings == null) {
            return null;
        }
        return this._classMappings.get(new ClassKey(clazz));
    }
}
