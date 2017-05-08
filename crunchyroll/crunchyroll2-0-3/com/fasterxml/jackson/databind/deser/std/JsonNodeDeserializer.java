// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class JsonNodeDeserializer extends BaseNodeDeserializer
{
    private static final JsonNodeDeserializer instance;
    
    static {
        instance = new JsonNodeDeserializer();
    }
    
    public static JsonDeserializer<? extends JsonNode> getDeserializer(final Class<?> clazz) {
        if (clazz == ObjectNode.class) {
            return ObjectDeserializer.getInstance();
        }
        if (clazz == ArrayNode.class) {
            return ArrayDeserializer.getInstance();
        }
        return JsonNodeDeserializer.instance;
    }
    
    @Override
    public JsonNode deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        switch (jsonParser.getCurrentToken()) {
            default: {
                return this.deserializeAny(jsonParser, deserializationContext, deserializationContext.getNodeFactory());
            }
            case START_OBJECT: {
                return this.deserializeObject(jsonParser, deserializationContext, deserializationContext.getNodeFactory());
            }
            case START_ARRAY: {
                return this.deserializeArray(jsonParser, deserializationContext, deserializationContext.getNodeFactory());
            }
        }
    }
    
    static final class ArrayDeserializer extends BaseNodeDeserializer
    {
        protected static final ArrayDeserializer _instance;
        
        static {
            _instance = new ArrayDeserializer();
        }
        
        public static ArrayDeserializer getInstance() {
            return ArrayDeserializer._instance;
        }
        
        @Override
        public ArrayNode deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            if (jsonParser.isExpectedStartArrayToken()) {
                return this.deserializeArray(jsonParser, deserializationContext, deserializationContext.getNodeFactory());
            }
            throw deserializationContext.mappingException(ArrayNode.class);
        }
    }
    
    static final class ObjectDeserializer extends BaseNodeDeserializer
    {
        protected static final ObjectDeserializer _instance;
        
        static {
            _instance = new ObjectDeserializer();
        }
        
        public static ObjectDeserializer getInstance() {
            return ObjectDeserializer._instance;
        }
        
        @Override
        public ObjectNode deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            if (jsonParser.getCurrentToken() == JsonToken.START_OBJECT) {
                jsonParser.nextToken();
                return this.deserializeObject(jsonParser, deserializationContext, deserializationContext.getNodeFactory());
            }
            if (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                return this.deserializeObject(jsonParser, deserializationContext, deserializationContext.getNodeFactory());
            }
            throw deserializationContext.mappingException(ObjectNode.class);
        }
    }
}
