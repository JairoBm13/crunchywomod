// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.JavaType;
import java.io.Serializable;

public class AsArrayTypeDeserializer extends TypeDeserializerBase implements Serializable
{
    public AsArrayTypeDeserializer(final JavaType javaType, final TypeIdResolver typeIdResolver, final String s, final boolean b, final Class<?> clazz) {
        super(javaType, typeIdResolver, s, b, clazz);
    }
    
    public AsArrayTypeDeserializer(final AsArrayTypeDeserializer asArrayTypeDeserializer, final BeanProperty beanProperty) {
        super(asArrayTypeDeserializer, beanProperty);
    }
    
    private final Object _deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final boolean expectedStartArrayToken = jsonParser.isExpectedStartArrayToken();
        final String locateTypeId = this._locateTypeId(jsonParser, deserializationContext);
        final JsonDeserializer<Object> findDeserializer = this._findDeserializer(deserializationContext, locateTypeId);
        JsonParser flattened = jsonParser;
        if (this._typeIdVisible) {
            flattened = jsonParser;
            if (jsonParser.getCurrentToken() == JsonToken.START_OBJECT) {
                final TokenBuffer tokenBuffer = new TokenBuffer(null);
                tokenBuffer.writeStartObject();
                tokenBuffer.writeFieldName(this._typePropertyName);
                tokenBuffer.writeString(locateTypeId);
                flattened = JsonParserSequence.createFlattened(tokenBuffer.asParser(jsonParser), jsonParser);
                flattened.nextToken();
            }
        }
        final Object deserialize = findDeserializer.deserialize(flattened, deserializationContext);
        if (expectedStartArrayToken && flattened.nextToken() != JsonToken.END_ARRAY) {
            throw deserializationContext.wrongTokenException(flattened, JsonToken.END_ARRAY, "expected closing END_ARRAY after type information and deserialized value");
        }
        return deserialize;
    }
    
    protected final String _locateTypeId(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        if (!jsonParser.isExpectedStartArrayToken()) {
            if (this._defaultImpl != null) {
                return this._idResolver.idFromBaseType();
            }
            throw deserializationContext.wrongTokenException(jsonParser, JsonToken.START_ARRAY, "need JSON Array to contain As.WRAPPER_ARRAY type information for class " + this.baseTypeName());
        }
        else {
            if (jsonParser.nextToken() == JsonToken.VALUE_STRING) {
                final String text = jsonParser.getText();
                jsonParser.nextToken();
                return text;
            }
            if (this._defaultImpl != null) {
                return this._idResolver.idFromBaseType();
            }
            throw deserializationContext.wrongTokenException(jsonParser, JsonToken.VALUE_STRING, "need JSON String that contains type id (for subtype of " + this.baseTypeName() + ")");
        }
    }
    
    @Override
    public Object deserializeTypedFromAny(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        return this._deserialize(jsonParser, deserializationContext);
    }
    
    @Override
    public Object deserializeTypedFromArray(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        return this._deserialize(jsonParser, deserializationContext);
    }
    
    @Override
    public Object deserializeTypedFromObject(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        return this._deserialize(jsonParser, deserializationContext);
    }
    
    @Override
    public Object deserializeTypedFromScalar(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        return this._deserialize(jsonParser, deserializationContext);
    }
    
    @Override
    public TypeDeserializer forProperty(final BeanProperty beanProperty) {
        if (beanProperty == this._property) {
            return this;
        }
        return new AsArrayTypeDeserializer(this, beanProperty);
    }
    
    @Override
    public JsonTypeInfo.As getTypeInclusion() {
        return JsonTypeInfo.As.WRAPPER_ARRAY;
    }
}
