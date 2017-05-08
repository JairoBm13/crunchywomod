// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JavaType;

public abstract class PropertySerializerMap
{
    public static PropertySerializerMap emptyMap() {
        return Empty.instance;
    }
    
    public final SerializerAndMapResult findAndAddSerializer(final JavaType javaType, final SerializerProvider serializerProvider, final BeanProperty beanProperty) throws JsonMappingException {
        final JsonSerializer<Object> valueSerializer = serializerProvider.findValueSerializer(javaType, beanProperty);
        return new SerializerAndMapResult(valueSerializer, this.newWith(javaType.getRawClass(), valueSerializer));
    }
    
    public final SerializerAndMapResult findAndAddSerializer(final Class<?> clazz, final SerializerProvider serializerProvider, final BeanProperty beanProperty) throws JsonMappingException {
        final JsonSerializer<Object> valueSerializer = serializerProvider.findValueSerializer(clazz, beanProperty);
        return new SerializerAndMapResult(valueSerializer, this.newWith(clazz, valueSerializer));
    }
    
    public abstract PropertySerializerMap newWith(final Class<?> p0, final JsonSerializer<Object> p1);
    
    public abstract JsonSerializer<Object> serializerFor(final Class<?> p0);
    
    private static final class Double extends PropertySerializerMap
    {
        private final JsonSerializer<Object> _serializer1;
        private final JsonSerializer<Object> _serializer2;
        private final Class<?> _type1;
        private final Class<?> _type2;
        
        public Double(final Class<?> type1, final JsonSerializer<Object> serializer1, final Class<?> type2, final JsonSerializer<Object> serializer2) {
            this._type1 = type1;
            this._serializer1 = serializer1;
            this._type2 = type2;
            this._serializer2 = serializer2;
        }
        
        @Override
        public PropertySerializerMap newWith(final Class<?> clazz, final JsonSerializer<Object> jsonSerializer) {
            return new Multi(new TypeAndSerializer[] { new TypeAndSerializer(this._type1, this._serializer1), new TypeAndSerializer(this._type2, this._serializer2) });
        }
        
        @Override
        public JsonSerializer<Object> serializerFor(final Class<?> clazz) {
            if (clazz == this._type1) {
                return this._serializer1;
            }
            if (clazz == this._type2) {
                return this._serializer2;
            }
            return null;
        }
    }
    
    private static final class Empty extends PropertySerializerMap
    {
        protected static final Empty instance;
        
        static {
            instance = new Empty();
        }
        
        @Override
        public PropertySerializerMap newWith(final Class<?> clazz, final JsonSerializer<Object> jsonSerializer) {
            return new Single(clazz, jsonSerializer);
        }
        
        @Override
        public JsonSerializer<Object> serializerFor(final Class<?> clazz) {
            return null;
        }
    }
    
    private static final class Multi extends PropertySerializerMap
    {
        private final TypeAndSerializer[] _entries;
        
        public Multi(final TypeAndSerializer[] entries) {
            this._entries = entries;
        }
        
        @Override
        public PropertySerializerMap newWith(final Class<?> clazz, final JsonSerializer<Object> jsonSerializer) {
            final int length = this._entries.length;
            if (length == 8) {
                return this;
            }
            final TypeAndSerializer[] array = new TypeAndSerializer[length + 1];
            System.arraycopy(this._entries, 0, array, 0, length);
            array[length] = new TypeAndSerializer(clazz, jsonSerializer);
            return new Multi(array);
        }
        
        @Override
        public JsonSerializer<Object> serializerFor(final Class<?> clazz) {
            for (int i = 0; i < this._entries.length; ++i) {
                final TypeAndSerializer typeAndSerializer = this._entries[i];
                if (typeAndSerializer.type == clazz) {
                    return typeAndSerializer.serializer;
                }
            }
            return null;
        }
    }
    
    public static final class SerializerAndMapResult
    {
        public final PropertySerializerMap map;
        public final JsonSerializer<Object> serializer;
        
        public SerializerAndMapResult(final JsonSerializer<Object> serializer, final PropertySerializerMap map) {
            this.serializer = serializer;
            this.map = map;
        }
    }
    
    private static final class Single extends PropertySerializerMap
    {
        private final JsonSerializer<Object> _serializer;
        private final Class<?> _type;
        
        public Single(final Class<?> type, final JsonSerializer<Object> serializer) {
            this._type = type;
            this._serializer = serializer;
        }
        
        @Override
        public PropertySerializerMap newWith(final Class<?> clazz, final JsonSerializer<Object> jsonSerializer) {
            return new Double(this._type, this._serializer, clazz, jsonSerializer);
        }
        
        @Override
        public JsonSerializer<Object> serializerFor(final Class<?> clazz) {
            if (clazz == this._type) {
                return this._serializer;
            }
            return null;
        }
    }
    
    private static final class TypeAndSerializer
    {
        public final JsonSerializer<Object> serializer;
        public final Class<?> type;
        
        public TypeAndSerializer(final Class<?> type, final JsonSerializer<Object> serializer) {
            this.type = type;
            this.serializer = serializer;
        }
    }
}
