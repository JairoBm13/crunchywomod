// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonToken;

public final class POJONode extends ValueNode
{
    protected final Object _value;
    
    public POJONode(final Object value) {
        this._value = value;
    }
    
    @Override
    public boolean asBoolean(final boolean b) {
        boolean booleanValue = b;
        if (this._value != null) {
            booleanValue = b;
            if (this._value instanceof Boolean) {
                booleanValue = (boolean)this._value;
            }
        }
        return booleanValue;
    }
    
    @Override
    public int asInt(int intValue) {
        if (this._value instanceof Number) {
            intValue = ((Number)this._value).intValue();
        }
        return intValue;
    }
    
    @Override
    public String asText() {
        if (this._value == null) {
            return "null";
        }
        return this._value.toString();
    }
    
    @Override
    public JsonToken asToken() {
        return JsonToken.VALUE_EMBEDDED_OBJECT;
    }
    
    @Override
    public byte[] binaryValue() throws IOException {
        if (this._value instanceof byte[]) {
            return (byte[])this._value;
        }
        return super.binaryValue();
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
            final POJONode pojoNode = (POJONode)o;
            if (this._value != null) {
                return this._value.equals(pojoNode._value);
            }
            if (pojoNode._value != null) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public JsonNodeType getNodeType() {
        return JsonNodeType.POJO;
    }
    
    public Object getPojo() {
        return this._value;
    }
    
    @Override
    public int hashCode() {
        return this._value.hashCode();
    }
    
    @Override
    public final void serialize(final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        if (this._value == null) {
            serializerProvider.defaultSerializeNull(jsonGenerator);
            return;
        }
        jsonGenerator.writeObject(this._value);
    }
    
    @Override
    public String toString() {
        return String.valueOf(this._value);
    }
}
