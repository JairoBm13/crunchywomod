// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonToken;

public final class MissingNode extends ValueNode
{
    private static final MissingNode instance;
    
    static {
        instance = new MissingNode();
    }
    
    public static MissingNode getInstance() {
        return MissingNode.instance;
    }
    
    @Override
    public String asText() {
        return "";
    }
    
    @Override
    public JsonToken asToken() {
        return JsonToken.NOT_AVAILABLE;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this;
    }
    
    @Override
    public JsonNodeType getNodeType() {
        return JsonNodeType.MISSING;
    }
    
    @Override
    public final void serialize(final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeNull();
    }
    
    @Override
    public void serializeWithType(final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider, final TypeSerializer typeSerializer) throws IOException, JsonProcessingException {
        jsonGenerator.writeNull();
    }
    
    @Override
    public String toString() {
        return "";
    }
}
