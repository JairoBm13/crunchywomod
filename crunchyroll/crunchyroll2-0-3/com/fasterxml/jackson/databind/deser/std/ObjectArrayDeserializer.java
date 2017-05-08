// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.databind.util.ObjectBuffer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.lang.reflect.Array;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

@JacksonStdImpl
public class ObjectArrayDeserializer extends ContainerDeserializerBase<Object[]> implements ContextualDeserializer
{
    protected final ArrayType _arrayType;
    protected final Class<?> _elementClass;
    protected JsonDeserializer<Object> _elementDeserializer;
    protected final TypeDeserializer _elementTypeDeserializer;
    protected final boolean _untyped;
    
    public ObjectArrayDeserializer(final ArrayType arrayType, final JsonDeserializer<Object> elementDeserializer, final TypeDeserializer elementTypeDeserializer) {
        super(Object[].class);
        this._arrayType = arrayType;
        this._elementClass = arrayType.getContentType().getRawClass();
        this._untyped = (this._elementClass == Object.class);
        this._elementDeserializer = elementDeserializer;
        this._elementTypeDeserializer = elementTypeDeserializer;
    }
    
    private final Object[] handleNonArray(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING && deserializationContext.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT) && jsonParser.getText().length() == 0) {
            return null;
        }
        if (deserializationContext.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
            Object o;
            if (jsonParser.getCurrentToken() == JsonToken.VALUE_NULL) {
                o = null;
            }
            else if (this._elementTypeDeserializer == null) {
                o = this._elementDeserializer.deserialize(jsonParser, deserializationContext);
            }
            else {
                o = this._elementDeserializer.deserializeWithType(jsonParser, deserializationContext, this._elementTypeDeserializer);
            }
            Object[] array;
            if (this._untyped) {
                array = new Object[] { null };
            }
            else {
                array = (Object[])Array.newInstance(this._elementClass, 1);
            }
            array[0] = o;
            return array;
        }
        if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING && this._elementClass == Byte.class) {
            return this.deserializeFromBase64(jsonParser, deserializationContext);
        }
        throw deserializationContext.mappingException(this._arrayType.getRawClass());
    }
    
    @Override
    public JsonDeserializer<?> createContextual(final DeserializationContext deserializationContext, final BeanProperty beanProperty) throws JsonMappingException {
        final JsonDeserializer<?> convertingContentDeserializer = this.findConvertingContentDeserializer(deserializationContext, beanProperty, this._elementDeserializer);
        JsonDeserializer<?> jsonDeserializer;
        if (convertingContentDeserializer == null) {
            jsonDeserializer = deserializationContext.findContextualValueDeserializer(this._arrayType.getContentType(), beanProperty);
        }
        else {
            jsonDeserializer = convertingContentDeserializer;
            if (convertingContentDeserializer instanceof ContextualDeserializer) {
                jsonDeserializer = ((ContextualDeserializer)convertingContentDeserializer).createContextual(deserializationContext, beanProperty);
            }
        }
        final TypeDeserializer elementTypeDeserializer = this._elementTypeDeserializer;
        TypeDeserializer forProperty;
        if ((forProperty = elementTypeDeserializer) != null) {
            forProperty = elementTypeDeserializer.forProperty(beanProperty);
        }
        return this.withDeserializer(forProperty, jsonDeserializer);
    }
    
    @Override
    public Object[] deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        if (!jsonParser.isExpectedStartArrayToken()) {
            return this.handleNonArray(jsonParser, deserializationContext);
        }
        final ObjectBuffer leaseObjectBuffer = deserializationContext.leaseObjectBuffer();
        Object[] array = leaseObjectBuffer.resetAndStart();
        final TypeDeserializer elementTypeDeserializer = this._elementTypeDeserializer;
        int n = 0;
        while (true) {
            final JsonToken nextToken = jsonParser.nextToken();
            if (nextToken == JsonToken.END_ARRAY) {
                break;
            }
            Object o;
            if (nextToken == JsonToken.VALUE_NULL) {
                o = null;
            }
            else if (elementTypeDeserializer == null) {
                o = this._elementDeserializer.deserialize(jsonParser, deserializationContext);
            }
            else {
                o = this._elementDeserializer.deserializeWithType(jsonParser, deserializationContext, elementTypeDeserializer);
            }
            if (n >= array.length) {
                array = leaseObjectBuffer.appendCompletedChunk(array);
                n = 0;
            }
            final int n2 = n + 1;
            array[n] = o;
            n = n2;
        }
        Object[] array2;
        if (this._untyped) {
            array2 = leaseObjectBuffer.completeAndClearBuffer(array, n);
        }
        else {
            array2 = leaseObjectBuffer.completeAndClearBuffer(array, n, this._elementClass);
        }
        deserializationContext.returnObjectBuffer(leaseObjectBuffer);
        return array2;
    }
    
    protected Byte[] deserializeFromBase64(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final byte[] binaryValue = jsonParser.getBinaryValue(deserializationContext.getBase64Variant());
        final Byte[] array = new Byte[binaryValue.length];
        for (int i = 0; i < binaryValue.length; ++i) {
            array[i] = binaryValue[i];
        }
        return array;
    }
    
    @Override
    public Object[] deserializeWithType(final JsonParser jsonParser, final DeserializationContext deserializationContext, final TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        return (Object[])typeDeserializer.deserializeTypedFromArray(jsonParser, deserializationContext);
    }
    
    @Override
    public JsonDeserializer<Object> getContentDeserializer() {
        return this._elementDeserializer;
    }
    
    public ObjectArrayDeserializer withDeserializer(final TypeDeserializer typeDeserializer, final JsonDeserializer<?> jsonDeserializer) {
        if (jsonDeserializer == this._elementDeserializer && typeDeserializer == this._elementTypeDeserializer) {
            return this;
        }
        return new ObjectArrayDeserializer(this._arrayType, (JsonDeserializer<Object>)jsonDeserializer, typeDeserializer);
    }
}
