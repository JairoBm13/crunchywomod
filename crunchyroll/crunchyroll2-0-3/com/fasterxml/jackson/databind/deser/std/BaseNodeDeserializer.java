// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.JsonNode;

abstract class BaseNodeDeserializer extends StdDeserializer<JsonNode>
{
    public BaseNodeDeserializer() {
        super(JsonNode.class);
    }
    
    protected void _handleDuplicateField(final String s, final ObjectNode objectNode, final JsonNode jsonNode, final JsonNode jsonNode2) throws JsonProcessingException {
    }
    
    protected final JsonNode deserializeAny(final JsonParser jsonParser, final DeserializationContext deserializationContext, final JsonNodeFactory jsonNodeFactory) throws IOException, JsonProcessingException {
        switch (jsonParser.getCurrentToken()) {
            default: {
                throw deserializationContext.mappingException(this.getValueClass());
            }
            case START_OBJECT: {
                return this.deserializeObject(jsonParser, deserializationContext, jsonNodeFactory);
            }
            case START_ARRAY: {
                return this.deserializeArray(jsonParser, deserializationContext, jsonNodeFactory);
            }
            case FIELD_NAME: {
                return this.deserializeObject(jsonParser, deserializationContext, jsonNodeFactory);
            }
            case VALUE_EMBEDDED_OBJECT: {
                final Object embeddedObject = jsonParser.getEmbeddedObject();
                if (embeddedObject == null) {
                    return jsonNodeFactory.nullNode();
                }
                if (((byte[])embeddedObject).getClass() == byte[].class) {
                    return jsonNodeFactory.binaryNode((byte[])embeddedObject);
                }
                return jsonNodeFactory.pojoNode(embeddedObject);
            }
            case VALUE_STRING: {
                return jsonNodeFactory.textNode(jsonParser.getText());
            }
            case VALUE_NUMBER_INT: {
                final JsonParser.NumberType numberType = jsonParser.getNumberType();
                if (numberType == JsonParser.NumberType.BIG_INTEGER || deserializationContext.isEnabled(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS)) {
                    return jsonNodeFactory.numberNode(jsonParser.getBigIntegerValue());
                }
                if (numberType == JsonParser.NumberType.INT) {
                    return jsonNodeFactory.numberNode(jsonParser.getIntValue());
                }
                return jsonNodeFactory.numberNode(jsonParser.getLongValue());
            }
            case VALUE_NUMBER_FLOAT: {
                if (jsonParser.getNumberType() == JsonParser.NumberType.BIG_DECIMAL || deserializationContext.isEnabled(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)) {
                    return jsonNodeFactory.numberNode(jsonParser.getDecimalValue());
                }
                return jsonNodeFactory.numberNode(jsonParser.getDoubleValue());
            }
            case VALUE_TRUE: {
                return jsonNodeFactory.booleanNode(true);
            }
            case VALUE_FALSE: {
                return jsonNodeFactory.booleanNode(false);
            }
            case VALUE_NULL: {
                return jsonNodeFactory.nullNode();
            }
        }
    }
    
    protected final ArrayNode deserializeArray(final JsonParser jsonParser, final DeserializationContext deserializationContext, final JsonNodeFactory jsonNodeFactory) throws IOException, JsonProcessingException {
        final ArrayNode arrayNode = jsonNodeFactory.arrayNode();
        while (true) {
            final JsonToken nextToken = jsonParser.nextToken();
            if (nextToken == null) {
                throw deserializationContext.mappingException("Unexpected end-of-input when binding data into ArrayNode");
            }
            switch (nextToken) {
                default: {
                    arrayNode.add(this.deserializeAny(jsonParser, deserializationContext, jsonNodeFactory));
                    continue;
                }
                case START_OBJECT: {
                    arrayNode.add(this.deserializeObject(jsonParser, deserializationContext, jsonNodeFactory));
                    continue;
                }
                case START_ARRAY: {
                    arrayNode.add(this.deserializeArray(jsonParser, deserializationContext, jsonNodeFactory));
                    continue;
                }
                case VALUE_STRING: {
                    arrayNode.add(jsonNodeFactory.textNode(jsonParser.getText()));
                    continue;
                }
                case END_ARRAY: {
                    return arrayNode;
                }
            }
        }
    }
    
    protected final ObjectNode deserializeObject(final JsonParser jsonParser, final DeserializationContext deserializationContext, final JsonNodeFactory jsonNodeFactory) throws IOException, JsonProcessingException {
        final ObjectNode objectNode = jsonNodeFactory.objectNode();
        JsonToken jsonToken;
        if ((jsonToken = jsonParser.getCurrentToken()) == JsonToken.START_OBJECT) {
            jsonToken = jsonParser.nextToken();
        }
        while (jsonToken == JsonToken.FIELD_NAME) {
            final String currentName = jsonParser.getCurrentName();
            JsonNode jsonNode = null;
            switch (jsonParser.nextToken()) {
                default: {
                    jsonNode = this.deserializeAny(jsonParser, deserializationContext, jsonNodeFactory);
                    break;
                }
                case START_OBJECT: {
                    jsonNode = this.deserializeObject(jsonParser, deserializationContext, jsonNodeFactory);
                    break;
                }
                case START_ARRAY: {
                    jsonNode = this.deserializeArray(jsonParser, deserializationContext, jsonNodeFactory);
                    break;
                }
                case VALUE_STRING: {
                    jsonNode = jsonNodeFactory.textNode(jsonParser.getText());
                    break;
                }
            }
            final JsonNode replace = objectNode.replace(currentName, jsonNode);
            if (replace != null) {
                this._handleDuplicateField(currentName, objectNode, replace, jsonNode);
            }
            jsonToken = jsonParser.nextToken();
        }
        return objectNode;
    }
    
    @Override
    public Object deserializeWithType(final JsonParser jsonParser, final DeserializationContext deserializationContext, final TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        return typeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext);
    }
    
    @Override
    public JsonNode getNullValue() {
        return NullNode.getInstance();
    }
}
