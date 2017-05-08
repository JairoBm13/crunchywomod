// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import java.math.BigDecimal;
import java.math.BigInteger;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.io.NumberOutput;

public final class ShortNode extends NumericNode
{
    final short _value;
    
    @Override
    public boolean asBoolean(final boolean b) {
        return this._value != 0;
    }
    
    @Override
    public String asText() {
        return NumberOutput.toString(this._value);
    }
    
    @Override
    public JsonToken asToken() {
        return JsonToken.VALUE_NUMBER_INT;
    }
    
    @Override
    public BigInteger bigIntegerValue() {
        return BigInteger.valueOf(this._value);
    }
    
    @Override
    public BigDecimal decimalValue() {
        return BigDecimal.valueOf(this._value);
    }
    
    @Override
    public double doubleValue() {
        return this._value;
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
            if (((ShortNode)o)._value != this._value) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        return this._value;
    }
    
    @Override
    public int intValue() {
        return this._value;
    }
    
    @Override
    public long longValue() {
        return this._value;
    }
    
    @Override
    public JsonParser.NumberType numberType() {
        return JsonParser.NumberType.INT;
    }
    
    @Override
    public Number numberValue() {
        return this._value;
    }
    
    @Override
    public final void serialize(final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeNumber(this._value);
    }
}
