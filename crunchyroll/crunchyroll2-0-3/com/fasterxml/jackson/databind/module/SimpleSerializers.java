// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.module;

import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.type.ClassKey;
import java.util.HashMap;
import java.io.Serializable;
import com.fasterxml.jackson.databind.ser.Serializers;

public class SimpleSerializers extends Base implements Serializable
{
    protected HashMap<ClassKey, JsonSerializer<?>> _classMappings;
    protected HashMap<ClassKey, JsonSerializer<?>> _interfaceMappings;
    
    public SimpleSerializers() {
        this._classMappings = null;
        this._interfaceMappings = null;
    }
    
    protected JsonSerializer<?> _findInterfaceMapping(final Class<?> clazz, final ClassKey classKey) {
        final Class[] interfaces = clazz.getInterfaces();
        for (int length = interfaces.length, i = 0; i < length; ++i) {
            final Class clazz2 = interfaces[i];
            classKey.reset(clazz2);
            JsonSerializer<?> findInterfaceMapping = this._interfaceMappings.get(classKey);
            if (findInterfaceMapping != null || (findInterfaceMapping = this._findInterfaceMapping(clazz2, classKey)) != null) {
                return findInterfaceMapping;
            }
        }
        return null;
    }
    
    @Override
    public JsonSerializer<?> findArraySerializer(final SerializationConfig serializationConfig, final ArrayType arrayType, final BeanDescription beanDescription, final TypeSerializer typeSerializer, final JsonSerializer<Object> jsonSerializer) {
        return this.findSerializer(serializationConfig, arrayType, beanDescription);
    }
    
    @Override
    public JsonSerializer<?> findCollectionLikeSerializer(final SerializationConfig serializationConfig, final CollectionLikeType collectionLikeType, final BeanDescription beanDescription, final TypeSerializer typeSerializer, final JsonSerializer<Object> jsonSerializer) {
        return this.findSerializer(serializationConfig, collectionLikeType, beanDescription);
    }
    
    @Override
    public JsonSerializer<?> findCollectionSerializer(final SerializationConfig serializationConfig, final CollectionType collectionType, final BeanDescription beanDescription, final TypeSerializer typeSerializer, final JsonSerializer<Object> jsonSerializer) {
        return this.findSerializer(serializationConfig, collectionType, beanDescription);
    }
    
    @Override
    public JsonSerializer<?> findMapLikeSerializer(final SerializationConfig serializationConfig, final MapLikeType mapLikeType, final BeanDescription beanDescription, final JsonSerializer<Object> jsonSerializer, final TypeSerializer typeSerializer, final JsonSerializer<Object> jsonSerializer2) {
        return this.findSerializer(serializationConfig, mapLikeType, beanDescription);
    }
    
    @Override
    public JsonSerializer<?> findMapSerializer(final SerializationConfig serializationConfig, final MapType mapType, final BeanDescription beanDescription, final JsonSerializer<Object> jsonSerializer, final TypeSerializer typeSerializer, final JsonSerializer<Object> jsonSerializer2) {
        return this.findSerializer(serializationConfig, mapType, beanDescription);
    }
    
    @Override
    public JsonSerializer<?> findSerializer(final SerializationConfig serializationConfig, final JavaType javaType, final BeanDescription beanDescription) {
        final Class<?> rawClass = javaType.getRawClass();
        final ClassKey classKey = new ClassKey(rawClass);
        Label_0116: {
            JsonSerializer<?> findInterfaceMapping;
            if (rawClass.isInterface()) {
                if (this._interfaceMappings == null) {
                    break Label_0116;
                }
                findInterfaceMapping = this._interfaceMappings.get(classKey);
                if (findInterfaceMapping == null) {
                    break Label_0116;
                }
            }
            else {
                if (this._classMappings == null) {
                    break Label_0116;
                }
                if ((findInterfaceMapping = this._classMappings.get(classKey)) == null) {
                    for (Class<?> superclass = rawClass; superclass != null; superclass = superclass.getSuperclass()) {
                        classKey.reset(superclass);
                        if ((findInterfaceMapping = this._classMappings.get(classKey)) != null) {
                            return findInterfaceMapping;
                        }
                    }
                    break Label_0116;
                }
            }
            return findInterfaceMapping;
        }
        if (this._interfaceMappings != null) {
            final JsonSerializer<?> findInterfaceMapping;
            if ((findInterfaceMapping = this._findInterfaceMapping(rawClass, classKey)) != null) {
                return findInterfaceMapping;
            }
            if (!rawClass.isInterface()) {
                Class<?> superclass2 = rawClass;
                JsonSerializer<?> findInterfaceMapping2;
                do {
                    superclass2 = superclass2.getSuperclass();
                    if (superclass2 == null) {
                        return null;
                    }
                    findInterfaceMapping2 = this._findInterfaceMapping(superclass2, classKey);
                } while (findInterfaceMapping2 == null);
                return findInterfaceMapping2;
            }
        }
        return null;
    }
}
