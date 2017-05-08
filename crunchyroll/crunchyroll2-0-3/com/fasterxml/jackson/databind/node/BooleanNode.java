// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonToken;

public final class BooleanNode extends ValueNode
{
    public static final BooleanNode FALSE;
    public static final BooleanNode TRUE;
    private final boolean _value;
    
    static {
        TRUE = new BooleanNode(true);
        FALSE = new BooleanNode(false);
    }
    
    private BooleanNode(final boolean value) {
        this._value = value;
    }
    
    public static BooleanNode getFalse() {
        return BooleanNode.FALSE;
    }
    
    public static BooleanNode getTrue() {
        return BooleanNode.TRUE;
    }
    
    @Override
    public boolean asBoolean() {
        return this._value;
    }
    
    @Override
    public boolean asBoolean(final boolean b) {
        return this._value;
    }
    
    @Override
    public int asInt(final int n) {
        if (this._value) {
            return 1;
        }
        return 0;
    }
    
    @Override
    public String asText() {
        if (this._value) {
            return "true";
        }
        return "false";
    }
    
    @Override
    public JsonToken asToken() {
        if (this._value) {
            return JsonToken.VALUE_TRUE;
        }
        return JsonToken.VALUE_FALSE;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o != this) {
            if (o == null) {
                return false;
            }
            if (o.getClass() != this.getClass()) {
                return false;
            }
            if (this._value != ((BooleanNode)o)._value) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public JsonNodeType getNodeType() {
        return JsonNodeType.BOOLEAN;
    }
    
    @Override
    public final void serialize(final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeBoolean(this._value);
    }
}
