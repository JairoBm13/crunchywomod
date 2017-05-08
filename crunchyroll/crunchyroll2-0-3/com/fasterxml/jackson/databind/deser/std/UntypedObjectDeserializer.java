// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.std;

import java.util.LinkedHashMap;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;

@JacksonStdImpl
public class UntypedObjectDeserializer extends StdDeserializer<Object>
{
    private static final Object[] NO_OBJECTS;
    public static final UntypedObjectDeserializer instance;
    
    static {
        NO_OBJECTS = new Object[0];
        instance = new UntypedObjectDeserializer();
    }
    
    public UntypedObjectDeserializer() {
        super(Object.class);
    }
    
    @Override
    public Object deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        switch (jsonParser.getCurrentToken()) {
            default: {
                throw deserializationContext.mappingException(Object.class);
            }
            case START_OBJECT: {
                return this.mapObject(jsonParser, deserializationContext);
            }
            case START_ARRAY: {
                return this.mapArray(jsonParser, deserializationContext);
            }
            case FIELD_NAME: {
                return this.mapObject(jsonParser, deserializationContext);
            }
            case VALUE_EMBEDDED_OBJECT: {
                return jsonParser.getEmbeddedObject();
            }
            case VALUE_STRING: {
                return jsonParser.getText();
            }
            case VALUE_NUMBER_INT: {
                if (deserializationContext.isEnabled(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS)) {
                    return jsonParser.getBigIntegerValue();
                }
                return jsonParser.getNumberValue();
            }
            case VALUE_NUMBER_FLOAT: {
                if (deserializationContext.isEnabled(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)) {
                    return jsonParser.getDecimalValue();
                }
                return jsonParser.getDoubleValue();
            }
            case VALUE_TRUE: {
                return Boolean.TRUE;
            }
            case VALUE_FALSE: {
                return Boolean.FALSE;
            }
            case VALUE_NULL: {
                return null;
            }
        }
    }
    
    @Override
    public Object deserializeWithType(final JsonParser jsonParser, final DeserializationContext deserializationContext, final TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        switch (jsonParser.getCurrentToken()) {
            default: {
                throw deserializationContext.mappingException(Object.class);
            }
            case START_OBJECT:
            case START_ARRAY:
            case FIELD_NAME: {
                return typeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext);
            }
            case VALUE_STRING: {
                return jsonParser.getText();
            }
            case VALUE_NUMBER_INT: {
                if (deserializationContext.isEnabled(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS)) {
                    return jsonParser.getBigIntegerValue();
                }
                return jsonParser.getNumberValue();
            }
            case VALUE_NUMBER_FLOAT: {
                if (deserializationContext.isEnabled(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)) {
                    return jsonParser.getDecimalValue();
                }
                return jsonParser.getDoubleValue();
            }
            case VALUE_TRUE: {
                return Boolean.TRUE;
            }
            case VALUE_FALSE: {
                return Boolean.FALSE;
            }
            case VALUE_EMBEDDED_OBJECT: {
                return jsonParser.getEmbeddedObject();
            }
            case VALUE_NULL: {
                return null;
            }
        }
    }
    
    protected Object mapArray(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        if (deserializationContext.isEnabled(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY)) {
            return this.mapArrayToArray(jsonParser, deserializationContext);
        }
        if (jsonParser.nextToken() == JsonToken.END_ARRAY) {
            return new ArrayList(4);
        }
        final ObjectBuffer leaseObjectBuffer = deserializationContext.leaseObjectBuffer();
        Object[] resetAndStart = leaseObjectBuffer.resetAndStart();
        int n = 0;
        int n2 = 0;
        int n3;
        Object[] appendCompletedChunk;
        int n4;
        do {
            final Object deserialize = this.deserialize(jsonParser, deserializationContext);
            n3 = n2 + 1;
            if (n >= resetAndStart.length) {
                appendCompletedChunk = leaseObjectBuffer.appendCompletedChunk(resetAndStart);
                n = 0;
            }
            else {
                appendCompletedChunk = resetAndStart;
            }
            n4 = n + 1;
            appendCompletedChunk[n] = deserialize;
            n2 = n3;
            n = n4;
            resetAndStart = appendCompletedChunk;
        } while (jsonParser.nextToken() != JsonToken.END_ARRAY);
        final ArrayList list = new ArrayList<Object>(n3 + (n3 >> 3) + 1);
        leaseObjectBuffer.completeAndClearBuffer(appendCompletedChunk, n4, (List<Object>)list);
        return list;
    }
    
    protected Object[] mapArrayToArray(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        if (jsonParser.nextToken() == JsonToken.END_ARRAY) {
            return UntypedObjectDeserializer.NO_OBJECTS;
        }
        final ObjectBuffer leaseObjectBuffer = deserializationContext.leaseObjectBuffer();
        Object[] resetAndStart = leaseObjectBuffer.resetAndStart();
        int n = 0;
        Object[] appendCompletedChunk;
        int n2;
        do {
            final Object deserialize = this.deserialize(jsonParser, deserializationContext);
            if (n >= resetAndStart.length) {
                appendCompletedChunk = leaseObjectBuffer.appendCompletedChunk(resetAndStart);
                n = 0;
            }
            else {
                appendCompletedChunk = resetAndStart;
            }
            n2 = n + 1;
            appendCompletedChunk[n] = deserialize;
            n = n2;
            resetAndStart = appendCompletedChunk;
        } while (jsonParser.nextToken() != JsonToken.END_ARRAY);
        return leaseObjectBuffer.completeAndClearBuffer(appendCompletedChunk, n2);
    }
    
    protected Object mapObject(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonToken jsonToken;
        if ((jsonToken = jsonParser.getCurrentToken()) == JsonToken.START_OBJECT) {
            jsonToken = jsonParser.nextToken();
        }
        if (jsonToken != JsonToken.FIELD_NAME) {
            return new LinkedHashMap(4);
        }
        final String text = jsonParser.getText();
        jsonParser.nextToken();
        final Object deserialize = this.deserialize(jsonParser, deserializationContext);
        if (jsonParser.nextToken() != JsonToken.FIELD_NAME) {
            final LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>(4);
            linkedHashMap.put(text, deserialize);
            return linkedHashMap;
        }
        final String text2 = jsonParser.getText();
        jsonParser.nextToken();
        final Object deserialize2 = this.deserialize(jsonParser, deserializationContext);
        if (jsonParser.nextToken() != JsonToken.FIELD_NAME) {
            final LinkedHashMap<String, Object> linkedHashMap2 = new LinkedHashMap<String, Object>(4);
            linkedHashMap2.put(text, deserialize);
            linkedHashMap2.put(text2, deserialize2);
            return linkedHashMap2;
        }
        final LinkedHashMap<String, Object> linkedHashMap3 = new LinkedHashMap<String, Object>();
        linkedHashMap3.put(text, deserialize);
        linkedHashMap3.put(text2, deserialize2);
        do {
            final String text3 = jsonParser.getText();
            jsonParser.nextToken();
            linkedHashMap3.put(text3, this.deserialize(jsonParser, deserializationContext));
        } while (jsonParser.nextToken() != JsonToken.END_OBJECT);
        return linkedHashMap3;
    }
}
