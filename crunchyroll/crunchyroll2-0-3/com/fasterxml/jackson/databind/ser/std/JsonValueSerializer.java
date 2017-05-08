// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.lang.reflect.InvocationTargetException;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import java.lang.reflect.Modifier;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.BeanProperty;
import java.lang.reflect.Method;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;

@JacksonStdImpl
public class JsonValueSerializer extends StdSerializer<Object> implements JsonFormatVisitable, SchemaAware, ContextualSerializer
{
    protected final Method _accessorMethod;
    protected final boolean _forceTypeInformation;
    protected final BeanProperty _property;
    protected final JsonSerializer<Object> _valueSerializer;
    
    public JsonValueSerializer(final JsonValueSerializer jsonValueSerializer, final BeanProperty property, final JsonSerializer<?> valueSerializer, final boolean forceTypeInformation) {
        super(_notNullClass(jsonValueSerializer.handledType()));
        this._accessorMethod = jsonValueSerializer._accessorMethod;
        this._valueSerializer = (JsonSerializer<Object>)valueSerializer;
        this._property = property;
        this._forceTypeInformation = forceTypeInformation;
    }
    
    public JsonValueSerializer(final Method accessorMethod, final JsonSerializer<Object> valueSerializer) {
        super(Object.class);
        this._accessorMethod = accessorMethod;
        this._valueSerializer = valueSerializer;
        this._property = null;
        this._forceTypeInformation = true;
    }
    
    private static final Class<Object> _notNullClass(final Class<?> clazz) {
        Class<Object> clazz2 = (Class<Object>)clazz;
        if (clazz == null) {
            clazz2 = Object.class;
        }
        return clazz2;
    }
    
    @Override
    public JsonSerializer<?> createContextual(final SerializerProvider serializerProvider, final BeanProperty beanProperty) throws JsonMappingException {
        final JsonSerializer<Object> valueSerializer = this._valueSerializer;
        JsonValueSerializer withResolved;
        if (valueSerializer == null) {
            if (!serializerProvider.isEnabled(MapperFeature.USE_STATIC_TYPING)) {
                withResolved = this;
                if (!Modifier.isFinal(this._accessorMethod.getReturnType().getModifiers())) {
                    return withResolved;
                }
            }
            final JavaType constructType = serializerProvider.constructType(this._accessorMethod.getGenericReturnType());
            final JsonSerializer<Object> typedValueSerializer = serializerProvider.findTypedValueSerializer(constructType, false, this._property);
            withResolved = this.withResolved(beanProperty, typedValueSerializer, this.isNaturalTypeWithStdHandling(constructType.getRawClass(), typedValueSerializer));
        }
        else {
            withResolved = this;
            if (valueSerializer instanceof ContextualSerializer) {
                return this.withResolved(beanProperty, ((ContextualSerializer)valueSerializer).createContextual(serializerProvider, beanProperty), this._forceTypeInformation);
            }
        }
        return withResolved;
    }
    
    protected boolean isNaturalTypeWithStdHandling(final Class<?> clazz, final JsonSerializer<?> jsonSerializer) {
        if (clazz.isPrimitive()) {
            if (clazz == Integer.TYPE || clazz == Boolean.TYPE || clazz == Double.TYPE) {
                return this.isDefaultSerializer(jsonSerializer);
            }
        }
        else if (clazz == String.class || clazz == Integer.class || clazz == Boolean.class || clazz == Double.class) {
            return this.isDefaultSerializer(jsonSerializer);
        }
        return false;
    }
    
    @Override
    public void serialize(final Object o, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        try {
            final Object invoke = this._accessorMethod.invoke(o, new Object[0]);
            if (invoke == null) {
                serializerProvider.defaultSerializeNull(jsonGenerator);
                return;
            }
            JsonSerializer<Object> jsonSerializer;
            if ((jsonSerializer = this._valueSerializer) == null) {
                jsonSerializer = serializerProvider.findTypedValueSerializer(invoke.getClass(), true, this._property);
            }
            jsonSerializer.serialize(invoke, jsonGenerator, serializerProvider);
        }
        catch (IOException ex) {
            throw ex;
        }
        catch (Exception t) {
            goto Label_0069;
        }
    }
    
    @Override
    public void serializeWithType(final Object o, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider, final TypeSerializer typeSerializer) throws IOException, JsonProcessingException {
        try {
            final Object invoke = this._accessorMethod.invoke(o, new Object[0]);
            if (invoke == null) {
                serializerProvider.defaultSerializeNull(jsonGenerator);
                return;
            }
            if (this._valueSerializer == null) {
                serializerProvider.findValueSerializer(invoke.getClass(), this._property).serializeWithType(invoke, jsonGenerator, serializerProvider, typeSerializer);
                return;
            }
            goto Label_0066;
        }
        catch (IOException ex) {
            throw ex;
        }
        catch (Exception cause) {
            while (cause instanceof InvocationTargetException && cause.getCause() != null) {
                cause = cause.getCause();
            }
            if (cause instanceof Error) {
                throw (Error)cause;
            }
            throw JsonMappingException.wrapWithPath(cause, o, this._accessorMethod.getName() + "()");
        }
    }
    
    @Override
    public String toString() {
        return "(@JsonValue serializer for method " + this._accessorMethod.getDeclaringClass() + "#" + this._accessorMethod.getName() + ")";
    }
    
    public JsonValueSerializer withResolved(final BeanProperty beanProperty, final JsonSerializer<?> jsonSerializer, final boolean b) {
        if (this._property == beanProperty && this._valueSerializer == jsonSerializer && b == this._forceTypeInformation) {
            return this;
        }
        return new JsonValueSerializer(this, beanProperty, jsonSerializer, b);
    }
}
