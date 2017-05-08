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

public final class DoubleNode extends NumericNode
{
    protected final double _value;
    
    public DoubleNode(final double value) {
        this._value = value;
    }
    
    public static DoubleNode valueOf(final double n) {
        return new DoubleNode(n);
    }
    
    @Override
    public String asText() {
        return NumberOutput.toString(this._value);
    }
    
    @Override
    public JsonToken asToken() {
        return JsonToken.VALUE_NUMBER_FLOAT;
    }
    
    @Override
    public BigInteger bigIntegerValue() {
        return this.decimalValue().toBigInteger();
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
            if (Double.compare(this._value, ((DoubleNode)o)._value) != 0) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        final long doubleToLongBits = Double.doubleToLongBits(this._value);
        return (int)(doubleToLongBits >> 32) ^ (int)doubleToLongBits;
    }
    
    @Override
    public int intValue() {
        return (int)this._value;
    }
    
    @Override
    public long longValue() {
        return (long)this._value;
    }
    
    @Override
    public JsonParser.NumberType numberType() {
        return JsonParser.NumberType.DOUBLE;
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
