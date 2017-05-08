// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import java.util.HashMap;
import com.fasterxml.jackson.databind.ser.impl.ReadOnlyClassToSerializerMap;

public final class SerializerCache
{
    private ReadOnlyClassToSerializerMap _readOnlyMap;
    private HashMap<TypeKey, JsonSerializer<Object>> _sharedMap;
    
    public SerializerCache() {
        this._sharedMap = new HashMap<TypeKey, JsonSerializer<Object>>(64);
        this._readOnlyMap = null;
    }
    
    public void addAndResolveNonTypedSerializer(final JavaType javaType, final JsonSerializer<Object> jsonSerializer, final SerializerProvider serializerProvider) throws JsonMappingException {
        synchronized (this) {
            if (this._sharedMap.put(new TypeKey(javaType, false), jsonSerializer) == null) {
                this._readOnlyMap = null;
            }
            if (jsonSerializer instanceof ResolvableSerializer) {
                ((ResolvableSerializer)jsonSerializer).resolve(serializerProvider);
            }
        }
    }
    
    public void addAndResolveNonTypedSerializer(final Class<?> clazz, final JsonSerializer<Object> jsonSerializer, final SerializerProvider serializerProvider) throws JsonMappingException {
        synchronized (this) {
            if (this._sharedMap.put(new TypeKey(clazz, false), jsonSerializer) == null) {
                this._readOnlyMap = null;
            }
            if (jsonSerializer instanceof ResolvableSerializer) {
                ((ResolvableSerializer)jsonSerializer).resolve(serializerProvider);
            }
        }
    }
    
    public void addTypedSerializer(final JavaType javaType, final JsonSerializer<Object> jsonSerializer) {
        synchronized (this) {
            if (this._sharedMap.put(new TypeKey(javaType, true), jsonSerializer) == null) {
                this._readOnlyMap = null;
            }
        }
    }
    
    public void addTypedSerializer(final Class<?> clazz, final JsonSerializer<Object> jsonSerializer) {
        synchronized (this) {
            if (this._sharedMap.put(new TypeKey(clazz, true), jsonSerializer) == null) {
                this._readOnlyMap = null;
            }
        }
    }
    
    public ReadOnlyClassToSerializerMap getReadOnlyLookupMap() {
        synchronized (this) {
            ReadOnlyClassToSerializerMap readOnlyMap;
            if ((readOnlyMap = this._readOnlyMap) == null) {
                readOnlyMap = ReadOnlyClassToSerializerMap.from(this._sharedMap);
                this._readOnlyMap = readOnlyMap;
            }
            return readOnlyMap.instance();
        }
    }
    
    public JsonSerializer<Object> typedValueSerializer(final JavaType javaType) {
        synchronized (this) {
            return this._sharedMap.get(new TypeKey(javaType, true));
        }
    }
    
    public JsonSerializer<Object> typedValueSerializer(final Class<?> clazz) {
        synchronized (this) {
            return this._sharedMap.get(new TypeKey(clazz, true));
        }
    }
    
    public JsonSerializer<Object> untypedValueSerializer(final JavaType javaType) {
        synchronized (this) {
            return this._sharedMap.get(new TypeKey(javaType, false));
        }
    }
    
    public JsonSerializer<Object> untypedValueSerializer(final Class<?> clazz) {
        synchronized (this) {
            return this._sharedMap.get(new TypeKey(clazz, false));
        }
    }
    
    public static final class TypeKey
    {
        protected Class<?> _class;
        protected int _hashCode;
        protected boolean _isTyped;
        protected JavaType _type;
        
        public TypeKey(final JavaType type, final boolean isTyped) {
            this._type = type;
            this._class = null;
            this._isTyped = isTyped;
            this._hashCode = hash(type, isTyped);
        }
        
        public TypeKey(final Class<?> class1, final boolean isTyped) {
            this._class = class1;
            this._type = null;
            this._isTyped = isTyped;
            this._hashCode = hash(class1, isTyped);
        }
        
        private static final int hash(final JavaType javaType, final boolean b) {
            int n = javaType.hashCode() - 1;
            if (b) {
                --n;
            }
            return n;
        }
        
        private static final int hash(final Class<?> clazz, final boolean b) {
            int hashCode = clazz.getName().hashCode();
            if (b) {
                ++hashCode;
            }
            return hashCode;
        }
        
        @Override
        public final boolean equals(final Object o) {
            boolean b = true;
            if (o != null) {
                if (o == this) {
                    return true;
                }
                if (o.getClass() == this.getClass()) {
                    final TypeKey typeKey = (TypeKey)o;
                    if (typeKey._isTyped == this._isTyped) {
                        if (this._class != null) {
                            if (typeKey._class != this._class) {
                                b = false;
                            }
                            return b;
                        }
                        return this._type.equals(typeKey._type);
                    }
                }
            }
            return false;
        }
        
        @Override
        public final int hashCode() {
            return this._hashCode;
        }
        
        public void resetTyped(final JavaType type) {
            this._type = type;
            this._class = null;
            this._isTyped = true;
            this._hashCode = hash(type, true);
        }
        
        public void resetTyped(final Class<?> class1) {
            this._type = null;
            this._class = class1;
            this._isTyped = true;
            this._hashCode = hash(class1, true);
        }
        
        public void resetUntyped(final JavaType type) {
            this._type = type;
            this._class = null;
            this._isTyped = false;
            this._hashCode = hash(type, false);
        }
        
        public void resetUntyped(final Class<?> class1) {
            this._type = null;
            this._class = class1;
            this._isTyped = false;
            this._hashCode = hash(class1, false);
        }
        
        @Override
        public final String toString() {
            if (this._class != null) {
                return "{class: " + this._class.getName() + ", typed? " + this._isTyped + "}";
            }
            return "{type: " + this._type + ", typed? " + this._isTyped + "}";
        }
    }
}
