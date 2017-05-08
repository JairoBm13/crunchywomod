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

public class AsWrapperTypeDeserializer extends TypeDeserializerBase implements Serializable
{
    public AsWrapperTypeDeserializer(final JavaType javaType, final TypeIdResolver typeIdResolver, final String s, final boolean b, final Class<?> clazz) {
        super(javaType, typeIdResolver, s, b, null);
    }
    
    protected AsWrapperTypeDeserializer(final AsWrapperTypeDeserializer asWrapperTypeDeserializer, final BeanProperty beanProperty) {
        super(asWrapperTypeDeserializer, beanProperty);
    }
    
    private final Object _deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        if (jsonParser.getCurrentToken() != JsonToken.START_OBJECT) {
            throw deserializationContext.wrongTokenException(jsonParser, JsonToken.START_OBJECT, "need JSON Object to contain As.WRAPPER_OBJECT type information for class " + this.baseTypeName());
        }
        if (jsonParser.nextToken() != JsonToken.FIELD_NAME) {
            throw deserializationContext.wrongTokenException(jsonParser, JsonToken.FIELD_NAME, "need JSON String that contains type id (for subtype of " + this.baseTypeName() + ")");
        }
        final String text = jsonParser.getText();
        final JsonDeserializer<Object> findDeserializer = this._findDeserializer(deserializationContext, text);
        jsonParser.nextToken();
        JsonParser flattened = jsonParser;
        if (this._typeIdVisible) {
            flattened = jsonParser;
            if (jsonParser.getCurrentToken() == JsonToken.START_OBJECT) {
                final TokenBuffer tokenBuffer = new TokenBuffer(null);
                tokenBuffer.writeStartObject();
                tokenBuffer.writeFieldName(this._typePropertyName);
                tokenBuffer.writeString(text);
                flattened = JsonParserSequence.createFlattened(tokenBuffer.asParser(jsonParser), jsonParser);
                flattened.nextToken();
            }
        }
        final Object deserialize = findDeserializer.deserialize(flattened, deserializationContext);
        if (flattened.nextToken() != JsonToken.END_OBJECT) {
            throw deserializationContext.wrongTokenException(flattened, JsonToken.END_OBJECT, "expected closing END_OBJECT after type information and deserialized value");
        }
        return deserialize;
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
        return new AsWrapperTypeDeserializer(this, beanProperty);
    }
    
    @Override
    public JsonTypeInfo.As getTypeInclusion() {
        return JsonTypeInfo.As.WRAPPER_OBJECT;
    }
}
