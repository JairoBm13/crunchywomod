// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.JsonNode;

public abstract class BaseJsonNode extends JsonNode implements JsonSerializable
{
    @Override
    public JsonParser.NumberType numberType() {
        return null;
    }
    
    @Override
    public abstract void serialize(final JsonGenerator p0, final SerializerProvider p1) throws IOException, JsonProcessingException;
    
    @Override
    public JsonParser traverse() {
        return new TreeTraversingParser(this);
    }
}
