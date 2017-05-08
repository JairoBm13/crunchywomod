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

public class NullifyingDeserializer extends StdDeserializer<Object>
{
    public static final NullifyingDeserializer instance;
    
    static {
        instance = new NullifyingDeserializer();
    }
    
    public NullifyingDeserializer() {
        super(Object.class);
    }
    
    @Override
    public Object deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        jsonParser.skipChildren();
        return null;
    }
    
    @Override
    public Object deserializeWithType(final JsonParser jsonParser, final DeserializationContext deserializationContext, final TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        switch (jsonParser.getCurrentToken()) {
            default: {
                return null;
            }
            case START_ARRAY:
            case START_OBJECT:
            case FIELD_NAME: {
                return typeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext);
            }
        }
    }
}
