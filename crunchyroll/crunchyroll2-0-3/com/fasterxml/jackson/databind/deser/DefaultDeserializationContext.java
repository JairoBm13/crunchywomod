// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import java.util.LinkedHashMap;
import java.io.Serializable;
import com.fasterxml.jackson.databind.DeserializationContext;

public abstract class DefaultDeserializationContext extends DeserializationContext implements Serializable
{
    protected transient LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId> _objectIds;
    
    protected DefaultDeserializationContext(final DefaultDeserializationContext defaultDeserializationContext, final DeserializationConfig deserializationConfig, final JsonParser jsonParser, final InjectableValues injectableValues) {
        super(defaultDeserializationContext, deserializationConfig, jsonParser, injectableValues);
    }
    
    protected DefaultDeserializationContext(final DeserializerFactory deserializerFactory, final DeserializerCache deserializerCache) {
        super(deserializerFactory, deserializerCache);
    }
    
    public abstract DefaultDeserializationContext createInstance(final DeserializationConfig p0, final JsonParser p1, final InjectableValues p2);
    
    @Override
    public JsonDeserializer<Object> deserializerInstance(final Annotated annotated, final Object o) throws JsonMappingException {
        final ResolvableDeserializer resolvableDeserializer = null;
        final JsonDeserializer<Object> jsonDeserializer = null;
        Object o2;
        if (o == null) {
            o2 = jsonDeserializer;
        }
        else {
            Object o3;
            if (o instanceof JsonDeserializer) {
                o3 = o;
            }
            else {
                if (!(o instanceof Class)) {
                    throw new IllegalStateException("AnnotationIntrospector returned deserializer definition of type " + o.getClass().getName() + "; expected type JsonDeserializer or Class<JsonDeserializer> instead");
                }
                final Class clazz = (Class)o;
                o2 = jsonDeserializer;
                if (clazz == JsonDeserializer.None.class) {
                    return (JsonDeserializer<Object>)o2;
                }
                o2 = jsonDeserializer;
                if (clazz == NoClass.class) {
                    return (JsonDeserializer<Object>)o2;
                }
                if (!JsonDeserializer.class.isAssignableFrom(clazz)) {
                    throw new IllegalStateException("AnnotationIntrospector returned Class " + clazz.getName() + "; expected Class<JsonDeserializer>");
                }
                final HandlerInstantiator handlerInstantiator = this._config.getHandlerInstantiator();
                Object deserializerInstance;
                if (handlerInstantiator == null) {
                    deserializerInstance = resolvableDeserializer;
                }
                else {
                    deserializerInstance = handlerInstantiator.deserializerInstance(this._config, annotated, clazz);
                }
                o3 = deserializerInstance;
                if (deserializerInstance == null) {
                    o3 = ClassUtil.createInstance((Class<JsonDeserializer<Object>>)clazz, this._config.canOverrideAccessModifiers());
                }
            }
            o2 = o3;
            if (o3 instanceof ResolvableDeserializer) {
                ((ResolvableDeserializer)o3).resolve(this);
                return (JsonDeserializer<Object>)o3;
            }
        }
        return (JsonDeserializer<Object>)o2;
    }
    
    @Override
    public ReadableObjectId findObjectId(final Object o, final ObjectIdGenerator<?> objectIdGenerator) {
        final ObjectIdGenerator.IdKey key = objectIdGenerator.key(o);
        if (this._objectIds == null) {
            this._objectIds = new LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId>();
        }
        else {
            final ReadableObjectId readableObjectId = this._objectIds.get(key);
            if (readableObjectId != null) {
                return readableObjectId;
            }
        }
        final ReadableObjectId readableObjectId2 = new ReadableObjectId(o);
        this._objectIds.put(key, readableObjectId2);
        return readableObjectId2;
    }
    
    @Override
    public final KeyDeserializer keyDeserializerInstance(final Annotated annotated, final Object o) throws JsonMappingException {
        final ResolvableDeserializer resolvableDeserializer = null;
        final KeyDeserializer keyDeserializer = null;
        Object o2;
        if (o == null) {
            o2 = keyDeserializer;
        }
        else {
            KeyDeserializer keyDeserializer2;
            if (o instanceof KeyDeserializer) {
                keyDeserializer2 = (KeyDeserializer)o;
            }
            else {
                if (!(o instanceof Class)) {
                    throw new IllegalStateException("AnnotationIntrospector returned key deserializer definition of type " + o.getClass().getName() + "; expected type KeyDeserializer or Class<KeyDeserializer> instead");
                }
                final Class clazz = (Class)o;
                o2 = keyDeserializer;
                if (clazz == KeyDeserializer.None.class) {
                    return (KeyDeserializer)o2;
                }
                o2 = keyDeserializer;
                if (clazz == NoClass.class) {
                    return (KeyDeserializer)o2;
                }
                if (!KeyDeserializer.class.isAssignableFrom(clazz)) {
                    throw new IllegalStateException("AnnotationIntrospector returned Class " + clazz.getName() + "; expected Class<KeyDeserializer>");
                }
                final HandlerInstantiator handlerInstantiator = this._config.getHandlerInstantiator();
                Object keyDeserializerInstance;
                if (handlerInstantiator == null) {
                    keyDeserializerInstance = resolvableDeserializer;
                }
                else {
                    keyDeserializerInstance = handlerInstantiator.keyDeserializerInstance(this._config, annotated, clazz);
                }
                keyDeserializer2 = (KeyDeserializer)keyDeserializerInstance;
                if (keyDeserializerInstance == null) {
                    keyDeserializer2 = ClassUtil.createInstance((Class<KeyDeserializer>)clazz, this._config.canOverrideAccessModifiers());
                }
            }
            o2 = keyDeserializer2;
            if (keyDeserializer2 instanceof ResolvableDeserializer) {
                ((ResolvableDeserializer)keyDeserializer2).resolve(this);
                return keyDeserializer2;
            }
        }
        return (KeyDeserializer)o2;
    }
    
    public static final class Impl extends DefaultDeserializationContext
    {
        protected Impl(final Impl impl, final DeserializationConfig deserializationConfig, final JsonParser jsonParser, final InjectableValues injectableValues) {
            super(impl, deserializationConfig, jsonParser, injectableValues);
        }
        
        public Impl(final DeserializerFactory deserializerFactory) {
            super(deserializerFactory, null);
        }
        
        @Override
        public DefaultDeserializationContext createInstance(final DeserializationConfig deserializationConfig, final JsonParser jsonParser, final InjectableValues injectableValues) {
            return new Impl(this, deserializationConfig, jsonParser, injectableValues);
        }
    }
}
