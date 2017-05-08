// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import java.util.Collection;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;

public class StdTypeResolverBuilder implements TypeResolverBuilder<StdTypeResolverBuilder>
{
    protected TypeIdResolver _customIdResolver;
    protected Class<?> _defaultImpl;
    protected JsonTypeInfo.Id _idType;
    protected JsonTypeInfo.As _includeAs;
    protected boolean _typeIdVisible;
    protected String _typeProperty;
    
    public StdTypeResolverBuilder() {
        this._typeIdVisible = false;
    }
    
    public static StdTypeResolverBuilder noTypeInfoBuilder() {
        return new StdTypeResolverBuilder().init(JsonTypeInfo.Id.NONE, (TypeIdResolver)null);
    }
    
    @Override
    public TypeDeserializer buildTypeDeserializer(final DeserializationConfig deserializationConfig, final JavaType javaType, final Collection<NamedType> collection) {
        if (this._idType == JsonTypeInfo.Id.NONE) {
            return null;
        }
        final TypeIdResolver idResolver = this.idResolver(deserializationConfig, javaType, collection, false, true);
        switch (this._includeAs) {
            default: {
                throw new IllegalStateException("Do not know how to construct standard type serializer for inclusion type: " + this._includeAs);
            }
            case WRAPPER_ARRAY: {
                return new AsArrayTypeDeserializer(javaType, idResolver, this._typeProperty, this._typeIdVisible, this._defaultImpl);
            }
            case PROPERTY: {
                return new AsPropertyTypeDeserializer(javaType, idResolver, this._typeProperty, this._typeIdVisible, this._defaultImpl);
            }
            case WRAPPER_OBJECT: {
                return new AsWrapperTypeDeserializer(javaType, idResolver, this._typeProperty, this._typeIdVisible, this._defaultImpl);
            }
            case EXTERNAL_PROPERTY: {
                return new AsExternalTypeDeserializer(javaType, idResolver, this._typeProperty, this._typeIdVisible, this._defaultImpl);
            }
        }
    }
    
    @Override
    public TypeSerializer buildTypeSerializer(final SerializationConfig serializationConfig, final JavaType javaType, final Collection<NamedType> collection) {
        if (this._idType == JsonTypeInfo.Id.NONE) {
            return null;
        }
        final TypeIdResolver idResolver = this.idResolver(serializationConfig, javaType, collection, true, false);
        switch (this._includeAs) {
            default: {
                throw new IllegalStateException("Do not know how to construct standard type serializer for inclusion type: " + this._includeAs);
            }
            case WRAPPER_ARRAY: {
                return new AsArrayTypeSerializer(idResolver, null);
            }
            case PROPERTY: {
                return new AsPropertyTypeSerializer(idResolver, null, this._typeProperty);
            }
            case WRAPPER_OBJECT: {
                return new AsWrapperTypeSerializer(idResolver, null);
            }
            case EXTERNAL_PROPERTY: {
                return new AsExternalTypeSerializer(idResolver, null, this._typeProperty);
            }
        }
    }
    
    @Override
    public StdTypeResolverBuilder defaultImpl(final Class<?> defaultImpl) {
        this._defaultImpl = defaultImpl;
        return this;
    }
    
    @Override
    public Class<?> getDefaultImpl() {
        return this._defaultImpl;
    }
    
    protected TypeIdResolver idResolver(final MapperConfig<?> mapperConfig, final JavaType javaType, final Collection<NamedType> collection, final boolean b, final boolean b2) {
        if (this._customIdResolver != null) {
            return this._customIdResolver;
        }
        if (this._idType == null) {
            throw new IllegalStateException("Can not build, 'init()' not yet called");
        }
        switch (this._idType) {
            default: {
                throw new IllegalStateException("Do not know how to construct standard type id resolver for idType: " + this._idType);
            }
            case CLASS: {
                return new ClassNameIdResolver(javaType, mapperConfig.getTypeFactory());
            }
            case MINIMAL_CLASS: {
                return new MinimalClassNameIdResolver(javaType, mapperConfig.getTypeFactory());
            }
            case NAME: {
                return TypeNameIdResolver.construct(mapperConfig, javaType, collection, b, b2);
            }
            case NONE: {
                return null;
            }
        }
    }
    
    @Override
    public StdTypeResolverBuilder inclusion(final JsonTypeInfo.As includeAs) {
        if (includeAs == null) {
            throw new IllegalArgumentException("includeAs can not be null");
        }
        this._includeAs = includeAs;
        return this;
    }
    
    @Override
    public StdTypeResolverBuilder init(final JsonTypeInfo.Id idType, final TypeIdResolver customIdResolver) {
        if (idType == null) {
            throw new IllegalArgumentException("idType can not be null");
        }
        this._idType = idType;
        this._customIdResolver = customIdResolver;
        this._typeProperty = idType.getDefaultPropertyName();
        return this;
    }
    
    @Override
    public StdTypeResolverBuilder typeIdVisibility(final boolean typeIdVisible) {
        this._typeIdVisible = typeIdVisible;
        return this;
    }
    
    @Override
    public StdTypeResolverBuilder typeProperty(final String s) {
        String defaultPropertyName = null;
        Label_0021: {
            if (s != null) {
                defaultPropertyName = s;
                if (s.length() != 0) {
                    break Label_0021;
                }
            }
            defaultPropertyName = this._idType.getDefaultPropertyName();
        }
        this._typeProperty = defaultPropertyName;
        return this;
    }
}
