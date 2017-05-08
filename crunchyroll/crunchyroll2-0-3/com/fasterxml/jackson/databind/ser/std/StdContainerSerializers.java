// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.ser.impl.IteratorSerializer;
import com.fasterxml.jackson.databind.ser.impl.IndexedListSerializer;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.ser.ContainerSerializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.JavaType;

public class StdContainerSerializers
{
    public static ContainerSerializer<?> collectionSerializer(final JavaType javaType, final boolean b, final TypeSerializer typeSerializer, final JsonSerializer<Object> jsonSerializer) {
        return new CollectionSerializer(javaType, b, typeSerializer, null, jsonSerializer);
    }
    
    public static JsonSerializer<?> enumSetSerializer(final JavaType javaType) {
        return new EnumSetSerializer(javaType, null);
    }
    
    public static ContainerSerializer<?> indexedListSerializer(final JavaType javaType, final boolean b, final TypeSerializer typeSerializer, final JsonSerializer<Object> jsonSerializer) {
        return new IndexedListSerializer(javaType, b, typeSerializer, null, jsonSerializer);
    }
    
    public static ContainerSerializer<?> iterableSerializer(final JavaType javaType, final boolean b, final TypeSerializer typeSerializer) {
        return new IterableSerializer(javaType, b, typeSerializer, null);
    }
    
    public static ContainerSerializer<?> iteratorSerializer(final JavaType javaType, final boolean b, final TypeSerializer typeSerializer) {
        return new IteratorSerializer(javaType, b, typeSerializer, null);
    }
}
