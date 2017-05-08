// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

@JacksonStdImpl
public final class StringArrayDeserializer extends StdDeserializer<String[]> implements ContextualDeserializer
{
    public static final StringArrayDeserializer instance;
    protected JsonDeserializer<String> _elementDeserializer;
    
    static {
        instance = new StringArrayDeserializer();
    }
    
    public StringArrayDeserializer() {
        super(String[].class);
        this._elementDeserializer = null;
    }
    
    protected StringArrayDeserializer(final JsonDeserializer<?> elementDeserializer) {
        super(String[].class);
        this._elementDeserializer = (JsonDeserializer<String>)elementDeserializer;
    }
    
    private final String[] handleNonArray(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final String s = null;
        if (deserializationContext.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
            String parseString;
            if (jsonParser.getCurrentToken() == JsonToken.VALUE_NULL) {
                parseString = s;
            }
            else {
                parseString = this._parseString(jsonParser, deserializationContext);
            }
            return new String[] { parseString };
        }
        if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING && deserializationContext.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT) && jsonParser.getText().length() == 0) {
            return null;
        }
        throw deserializationContext.mappingException(this._valueClass);
    }
    
    protected final String[] _deserializeCustom(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final ObjectBuffer leaseObjectBuffer = deserializationContext.leaseObjectBuffer();
        Object[] array = leaseObjectBuffer.resetAndStart();
        final JsonDeserializer<String> elementDeserializer = this._elementDeserializer;
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
            else {
                o = elementDeserializer.deserialize(jsonParser, deserializationContext);
            }
            if (n >= array.length) {
                array = leaseObjectBuffer.appendCompletedChunk(array);
                n = 0;
            }
            array[n] = o;
            ++n;
        }
        final String[] array2 = leaseObjectBuffer.completeAndClearBuffer(array, n, String.class);
        deserializationContext.returnObjectBuffer(leaseObjectBuffer);
        return array2;
    }
    
    @Override
    public JsonDeserializer<?> createContextual(final DeserializationContext deserializationContext, final BeanProperty beanProperty) throws JsonMappingException {
        final JsonDeserializer<?> convertingContentDeserializer = this.findConvertingContentDeserializer(deserializationContext, beanProperty, this._elementDeserializer);
        Object o;
        if (convertingContentDeserializer == null) {
            o = deserializationContext.findContextualValueDeserializer(deserializationContext.constructType(String.class), beanProperty);
        }
        else {
            o = convertingContentDeserializer;
            if (convertingContentDeserializer instanceof ContextualDeserializer) {
                o = ((ContextualDeserializer)convertingContentDeserializer).createContextual(deserializationContext, beanProperty);
            }
        }
        JsonDeserializer<String> jsonDeserializer;
        if ((jsonDeserializer = (JsonDeserializer<String>)o) != null) {
            jsonDeserializer = (JsonDeserializer<String>)o;
            if (this.isDefaultDeserializer((JsonDeserializer<?>)o)) {
                jsonDeserializer = null;
            }
        }
        StringArrayDeserializer stringArrayDeserializer = this;
        if (this._elementDeserializer != jsonDeserializer) {
            stringArrayDeserializer = new StringArrayDeserializer(jsonDeserializer);
        }
        return stringArrayDeserializer;
    }
    
    @Override
    public String[] deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        if (!jsonParser.isExpectedStartArrayToken()) {
            return this.handleNonArray(jsonParser, deserializationContext);
        }
        if (this._elementDeserializer != null) {
            return this._deserializeCustom(jsonParser, deserializationContext);
        }
        final ObjectBuffer leaseObjectBuffer = deserializationContext.leaseObjectBuffer();
        Object[] array = leaseObjectBuffer.resetAndStart();
        int n = 0;
        while (true) {
            final JsonToken nextToken = jsonParser.nextToken();
            if (nextToken == JsonToken.END_ARRAY) {
                break;
            }
            String s;
            if (nextToken == JsonToken.VALUE_STRING) {
                s = jsonParser.getText();
            }
            else if (nextToken == JsonToken.VALUE_NULL) {
                s = null;
            }
            else {
                s = this._parseString(jsonParser, deserializationContext);
            }
            if (n >= array.length) {
                array = leaseObjectBuffer.appendCompletedChunk(array);
                n = 0;
            }
            final int n2 = n + 1;
            array[n] = s;
            n = n2;
        }
        final String[] array2 = leaseObjectBuffer.completeAndClearBuffer(array, n, String.class);
        deserializationContext.returnObjectBuffer(leaseObjectBuffer);
        return array2;
    }
    
    @Override
    public Object deserializeWithType(final JsonParser jsonParser, final DeserializationContext deserializationContext, final TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        return typeDeserializer.deserializeTypedFromArray(jsonParser, deserializationContext);
    }
}
