// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.JavaType;

public class AsPropertyTypeDeserializer extends AsArrayTypeDeserializer
{
    public AsPropertyTypeDeserializer(final JavaType javaType, final TypeIdResolver typeIdResolver, final String s, final boolean b, final Class<?> clazz) {
        super(javaType, typeIdResolver, s, b, clazz);
    }
    
    public AsPropertyTypeDeserializer(final AsPropertyTypeDeserializer asPropertyTypeDeserializer, final BeanProperty beanProperty) {
        super(asPropertyTypeDeserializer, beanProperty);
    }
    
    protected final Object _deserializeTypedForId(final JsonParser jsonParser, final DeserializationContext deserializationContext, final TokenBuffer tokenBuffer) throws IOException, JsonProcessingException {
        final String text = jsonParser.getText();
        final JsonDeserializer<Object> findDeserializer = this._findDeserializer(deserializationContext, text);
        TokenBuffer tokenBuffer2 = tokenBuffer;
        if (this._typeIdVisible) {
            if ((tokenBuffer2 = tokenBuffer) == null) {
                tokenBuffer2 = new TokenBuffer(null);
            }
            tokenBuffer2.writeFieldName(jsonParser.getCurrentName());
            tokenBuffer2.writeString(text);
        }
        JsonParser flattened = jsonParser;
        if (tokenBuffer2 != null) {
            flattened = JsonParserSequence.createFlattened(tokenBuffer2.asParser(jsonParser), jsonParser);
        }
        flattened.nextToken();
        return findDeserializer.deserialize(flattened, deserializationContext);
    }
    
    protected Object _deserializeTypedUsingDefaultImpl(final JsonParser jsonParser, final DeserializationContext deserializationContext, final TokenBuffer tokenBuffer) throws IOException, JsonProcessingException {
        final JsonDeserializer<Object> findDefaultImplDeserializer = this._findDefaultImplDeserializer(deserializationContext);
        Object o;
        if (findDefaultImplDeserializer != null) {
            JsonParser parser = jsonParser;
            if (tokenBuffer != null) {
                tokenBuffer.writeEndObject();
                parser = tokenBuffer.asParser(jsonParser);
                parser.nextToken();
            }
            o = findDefaultImplDeserializer.deserialize(parser, deserializationContext);
        }
        else if ((o = TypeDeserializer.deserializeIfNatural(jsonParser, deserializationContext, this._baseType)) == null) {
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                return super.deserializeTypedFromAny(jsonParser, deserializationContext);
            }
            throw deserializationContext.wrongTokenException(jsonParser, JsonToken.FIELD_NAME, "missing property '" + this._typePropertyName + "' that is to contain type id  (for class " + this.baseTypeName() + ")");
        }
        return o;
    }
    
    @Override
    public Object deserializeTypedFromAny(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
            return super.deserializeTypedFromArray(jsonParser, deserializationContext);
        }
        return this.deserializeTypedFromObject(jsonParser, deserializationContext);
    }
    
    @Override
    public Object deserializeTypedFromObject(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final JsonToken currentToken = jsonParser.getCurrentToken();
        JsonToken nextToken;
        if (currentToken == JsonToken.START_OBJECT) {
            nextToken = jsonParser.nextToken();
        }
        else {
            if (currentToken == JsonToken.START_ARRAY) {
                return this._deserializeTypedUsingDefaultImpl(jsonParser, deserializationContext, null);
            }
            if ((nextToken = currentToken) != JsonToken.FIELD_NAME) {
                return this._deserializeTypedUsingDefaultImpl(jsonParser, deserializationContext, null);
            }
        }
        final TokenBuffer tokenBuffer = null;
        JsonToken jsonToken = nextToken;
        TokenBuffer tokenBuffer2 = tokenBuffer;
        while (jsonToken == JsonToken.FIELD_NAME) {
            final String currentName = jsonParser.getCurrentName();
            jsonParser.nextToken();
            if (this._typePropertyName.equals(currentName)) {
                return this._deserializeTypedForId(jsonParser, deserializationContext, tokenBuffer2);
            }
            TokenBuffer tokenBuffer3;
            if ((tokenBuffer3 = tokenBuffer2) == null) {
                tokenBuffer3 = new TokenBuffer(null);
            }
            tokenBuffer3.writeFieldName(currentName);
            tokenBuffer3.copyCurrentStructure(jsonParser);
            final JsonToken nextToken2 = jsonParser.nextToken();
            tokenBuffer2 = tokenBuffer3;
            jsonToken = nextToken2;
        }
        return this._deserializeTypedUsingDefaultImpl(jsonParser, deserializationContext, tokenBuffer2);
    }
    
    @Override
    public TypeDeserializer forProperty(final BeanProperty beanProperty) {
        if (beanProperty == this._property) {
            return this;
        }
        return new AsPropertyTypeDeserializer(this, beanProperty);
    }
    
    @Override
    public JsonTypeInfo.As getTypeInclusion() {
        return JsonTypeInfo.As.PROPERTY;
    }
}
