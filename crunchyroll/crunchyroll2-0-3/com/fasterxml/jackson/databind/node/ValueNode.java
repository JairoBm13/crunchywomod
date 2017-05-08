// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.TreeNode;

public abstract class ValueNode extends BaseJsonNode
{
    @Override
    public final JsonNode get(final String s) {
        return null;
    }
    
    @Override
    public final boolean has(final String s) {
        return false;
    }
    
    @Override
    public final JsonNode path(final int n) {
        return MissingNode.getInstance();
    }
    
    @Override
    public final JsonNode path(final String s) {
        return MissingNode.getInstance();
    }
    
    @Override
    public void serializeWithType(final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider, final TypeSerializer typeSerializer) throws IOException, JsonProcessingException {
        typeSerializer.writeTypePrefixForScalar(this, jsonGenerator);
        this.serialize(jsonGenerator, serializerProvider);
        typeSerializer.writeTypeSuffixForScalar(this, jsonGenerator);
    }
    
    @Override
    public String toString() {
        return this.asText();
    }
}
