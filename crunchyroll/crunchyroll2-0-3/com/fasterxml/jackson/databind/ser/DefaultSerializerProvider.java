// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.io.IOException;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;
import java.util.IdentityHashMap;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import java.util.ArrayList;
import java.io.Serializable;
import com.fasterxml.jackson.databind.SerializerProvider;

public abstract class DefaultSerializerProvider extends SerializerProvider implements Serializable
{
    protected transient ArrayList<ObjectIdGenerator<?>> _objectIdGenerators;
    protected transient IdentityHashMap<Object, WritableObjectId> _seenObjectIds;
    
    protected DefaultSerializerProvider() {
    }
    
    protected DefaultSerializerProvider(final SerializerProvider serializerProvider, final SerializationConfig serializationConfig, final SerializerFactory serializerFactory) {
        super(serializerProvider, serializationConfig, serializerFactory);
    }
    
    public abstract DefaultSerializerProvider createInstance(final SerializationConfig p0, final SerializerFactory p1);
    
    @Override
    public WritableObjectId findObjectId(final Object o, final ObjectIdGenerator<?> objectIdGenerator) {
        if (this._seenObjectIds == null) {
            this._seenObjectIds = new IdentityHashMap<Object, WritableObjectId>();
        }
        else {
            final WritableObjectId writableObjectId = this._seenObjectIds.get(o);
            if (writableObjectId != null) {
                return writableObjectId;
            }
        }
        ObjectIdGenerator<?> objectIdGenerator2 = null;
        Label_0041: {
            if (this._objectIdGenerators == null) {
                this._objectIdGenerators = new ArrayList<ObjectIdGenerator<?>>(8);
                objectIdGenerator2 = null;
            }
            else {
                for (int size = this._objectIdGenerators.size(), i = 0; i < size; ++i) {
                    if ((objectIdGenerator2 = this._objectIdGenerators.get(i)).canUseFor(objectIdGenerator)) {
                        break Label_0041;
                    }
                }
                objectIdGenerator2 = null;
            }
        }
        ObjectIdGenerator<?> forSerialization;
        if ((forSerialization = objectIdGenerator2) == null) {
            forSerialization = objectIdGenerator.newForSerialization(this);
            this._objectIdGenerators.add(forSerialization);
        }
        final WritableObjectId writableObjectId2 = new WritableObjectId(forSerialization);
        this._seenObjectIds.put(o, writableObjectId2);
        return writableObjectId2;
    }
    
    public void serializeValue(final JsonGenerator jsonGenerator, final Object o) throws IOException, JsonGenerationException {
        int enabled = 0;
        Label_0029: {
            if (o != null) {
                break Label_0029;
            }
            JsonSerializer<Object> jsonSerializer = this.getDefaultNullValueSerializer();
        Block_6_Outer:
            while (true) {
                try {
                    jsonSerializer.serialize(o, jsonGenerator, this);
                    if (enabled != 0) {
                        jsonGenerator.writeEndObject();
                    }
                    return;
                    // iftrue(Label_0096:, rootName != null)
                    // iftrue(Label_0107:, rootName.length() != 0)
                    Block_4: {
                        String rootName;
                        while (true) {
                            continue Block_6_Outer;
                            jsonSerializer = this.findTypedValueSerializer(o.getClass(), true, null);
                            rootName = this._config.getRootName();
                            break Block_4;
                            Label_0096: {
                                continue;
                            }
                        }
                        Label_0107: {
                            jsonGenerator.writeStartObject();
                        }
                        jsonGenerator.writeFieldName(rootName);
                        enabled = 1;
                        continue Block_6_Outer;
                    }
                    enabled = (this._config.isEnabled(SerializationFeature.WRAP_ROOT_VALUE) ? 1 : 0);
                    // iftrue(Label_0182:, enabled == 0)
                    jsonGenerator.writeStartObject();
                    jsonGenerator.writeFieldName(this._rootNames.findRootName(o.getClass(), this._config));
                    continue Block_6_Outer;
                }
                catch (IOException ex) {
                    throw ex;
                }
                catch (Exception ex2) {
                    String s;
                    if ((s = ex2.getMessage()) == null) {
                        s = "[no message for " + ex2.getClass().getName() + "]";
                    }
                    throw new JsonMappingException(s, ex2);
                }
                Label_0182:;
            }
        }
    }
    
    @Override
    public JsonSerializer<Object> serializerInstance(final Annotated annotated, final Object o) throws JsonMappingException {
        final JsonSerializer<?> jsonSerializer = null;
        if (o != null) {
            JsonSerializer<?> serializerInstance;
            if (o instanceof JsonSerializer) {
                serializerInstance = (JsonSerializer<?>)o;
            }
            else {
                if (!(o instanceof Class)) {
                    throw new IllegalStateException("AnnotationIntrospector returned serializer definition of type " + o.getClass().getName() + "; expected type JsonSerializer or Class<JsonSerializer> instead");
                }
                final Class clazz = (Class)o;
                if (clazz == JsonSerializer.None.class || clazz == NoClass.class) {
                    return null;
                }
                if (!JsonSerializer.class.isAssignableFrom(clazz)) {
                    throw new IllegalStateException("AnnotationIntrospector returned Class " + clazz.getName() + "; expected Class<JsonSerializer>");
                }
                final HandlerInstantiator handlerInstantiator = this._config.getHandlerInstantiator();
                if (handlerInstantiator == null) {
                    serializerInstance = jsonSerializer;
                }
                else {
                    serializerInstance = handlerInstantiator.serializerInstance(this._config, annotated, clazz);
                }
                if (serializerInstance == null) {
                    serializerInstance = ClassUtil.createInstance((Class<JsonSerializer<?>>)clazz, this._config.canOverrideAccessModifiers());
                }
            }
            return this._handleResolvable(serializerInstance);
        }
        return null;
    }
    
    public static final class Impl extends DefaultSerializerProvider
    {
        public Impl() {
        }
        
        protected Impl(final SerializerProvider serializerProvider, final SerializationConfig serializationConfig, final SerializerFactory serializerFactory) {
            super(serializerProvider, serializationConfig, serializerFactory);
        }
        
        @Override
        public Impl createInstance(final SerializationConfig serializationConfig, final SerializerFactory serializerFactory) {
            return new Impl(this, serializationConfig, serializerFactory);
        }
    }
}
