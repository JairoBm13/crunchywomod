// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import java.util.HashMap;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import java.io.Serializable;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public abstract class TypeDeserializerBase extends TypeDeserializer implements Serializable
{
    protected final JavaType _baseType;
    protected final JavaType _defaultImpl;
    protected JsonDeserializer<Object> _defaultImplDeserializer;
    protected final HashMap<String, JsonDeserializer<Object>> _deserializers;
    protected final TypeIdResolver _idResolver;
    protected final BeanProperty _property;
    protected final boolean _typeIdVisible;
    protected final String _typePropertyName;
    
    protected TypeDeserializerBase(final JavaType baseType, final TypeIdResolver idResolver, final String typePropertyName, final boolean typeIdVisible, final Class<?> clazz) {
        this._baseType = baseType;
        this._idResolver = idResolver;
        this._typePropertyName = typePropertyName;
        this._typeIdVisible = typeIdVisible;
        this._deserializers = new HashMap<String, JsonDeserializer<Object>>();
        if (clazz == null) {
            this._defaultImpl = null;
        }
        else {
            this._defaultImpl = baseType.forcedNarrowBy(clazz);
        }
        this._property = null;
    }
    
    protected TypeDeserializerBase(final TypeDeserializerBase typeDeserializerBase, final BeanProperty property) {
        this._baseType = typeDeserializerBase._baseType;
        this._idResolver = typeDeserializerBase._idResolver;
        this._typePropertyName = typeDeserializerBase._typePropertyName;
        this._typeIdVisible = typeDeserializerBase._typeIdVisible;
        this._deserializers = typeDeserializerBase._deserializers;
        this._defaultImpl = typeDeserializerBase._defaultImpl;
        this._defaultImplDeserializer = typeDeserializerBase._defaultImplDeserializer;
        this._property = property;
    }
    
    protected final JsonDeserializer<Object> _findDefaultImplDeserializer(final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        if (this._defaultImpl == null) {
            if (!deserializationContext.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)) {
                return NullifyingDeserializer.instance;
            }
            return null;
        }
        else {
            if (this._defaultImpl.getRawClass() == NoClass.class) {
                return NullifyingDeserializer.instance;
            }
            synchronized (this._defaultImpl) {
                if (this._defaultImplDeserializer == null) {
                    this._defaultImplDeserializer = deserializationContext.findContextualValueDeserializer(this._defaultImpl, this._property);
                }
                return this._defaultImplDeserializer;
            }
        }
    }
    
    protected final JsonDeserializer<Object> _findDeserializer(final DeserializationContext deserializationContext, final String s) throws IOException, JsonProcessingException {
        // monitorexit(hashMap)
        while (true) {
            Object typeFromId = null;
            final DeserializationContext deserializationContext2;
            Label_0093: {
                JsonDeserializer<Object> jsonDeserializer = null;
                Label_0088: {
                    synchronized (this._deserializers) {
                        typeFromId = (jsonDeserializer = this._deserializers.get(s));
                        if (typeFromId != null) {
                            break Label_0088;
                        }
                        typeFromId = this._idResolver.typeFromId(s);
                        if (typeFromId != null) {
                            break Label_0093;
                        }
                        if (this._defaultImpl == null) {
                            throw deserializationContext.unknownTypeException(this._baseType, s);
                        }
                    }
                    final JsonDeserializer<Object> jsonDeserializer2 = this._findDefaultImplDeserializer(deserializationContext2);
                    this._deserializers.put(s, jsonDeserializer2);
                    jsonDeserializer = jsonDeserializer2;
                }
                return jsonDeserializer;
            }
            Object narrowBy = typeFromId;
            if (this._baseType != null) {
                narrowBy = typeFromId;
                if (this._baseType.getClass() == ((JsonDeserializer<Object>)typeFromId).getClass()) {
                    narrowBy = this._baseType.narrowBy(((JavaType)typeFromId).getRawClass());
                }
            }
            final JsonDeserializer<Object> jsonDeserializer2 = deserializationContext2.findContextualValueDeserializer((JavaType)narrowBy, this._property);
            continue;
        }
    }
    
    public String baseTypeName() {
        return this._baseType.getRawClass().getName();
    }
    
    @Override
    public Class<?> getDefaultImpl() {
        if (this._defaultImpl == null) {
            return null;
        }
        return this._defaultImpl.getRawClass();
    }
    
    @Override
    public final String getPropertyName() {
        return this._typePropertyName;
    }
    
    @Override
    public TypeIdResolver getTypeIdResolver() {
        return this._idResolver;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append('[').append(this.getClass().getName());
        sb.append("; base-type:").append(this._baseType);
        sb.append("; id-resolver: ").append(this._idResolver);
        sb.append(']');
        return sb.toString();
    }
}
