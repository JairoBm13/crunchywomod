// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;

@JacksonStdImpl
public final class StringDeserializer extends StdScalarDeserializer<String>
{
    public static final StringDeserializer instance;
    
    static {
        instance = new StringDeserializer();
    }
    
    public StringDeserializer() {
        super(String.class);
    }
    
    @Override
    public String deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final String valueAsString = jsonParser.getValueAsString();
        if (valueAsString != null) {
            return valueAsString;
        }
        final JsonToken currentToken = jsonParser.getCurrentToken();
        if (currentToken != JsonToken.VALUE_EMBEDDED_OBJECT) {
            throw deserializationContext.mappingException(this._valueClass, currentToken);
        }
        final Object embeddedObject = jsonParser.getEmbeddedObject();
        if (embeddedObject == null) {
            return null;
        }
        if (embeddedObject instanceof byte[]) {
            return Base64Variants.getDefaultVariant().encode((byte[])embeddedObject, false);
        }
        return embeddedObject.toString();
    }
    
    @Override
    public String deserializeWithType(final JsonParser jsonParser, final DeserializationContext deserializationContext, final TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        return this.deserialize(jsonParser, deserializationContext);
    }
}
