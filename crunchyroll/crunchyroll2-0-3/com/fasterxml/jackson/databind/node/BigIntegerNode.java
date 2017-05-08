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
import com.fasterxml.jackson.core.JsonToken;
import java.math.BigInteger;

public final class BigIntegerNode extends NumericNode
{
    private static final BigInteger MAX_INTEGER;
    private static final BigInteger MAX_LONG;
    private static final BigInteger MIN_INTEGER;
    private static final BigInteger MIN_LONG;
    protected final BigInteger _value;
    
    static {
        MIN_INTEGER = BigInteger.valueOf(-2147483648L);
        MAX_INTEGER = BigInteger.valueOf(2147483647L);
        MIN_LONG = BigInteger.valueOf(Long.MIN_VALUE);
        MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);
    }
    
    public BigIntegerNode(final BigInteger value) {
        this._value = value;
    }
    
    public static BigIntegerNode valueOf(final BigInteger bigInteger) {
        return new BigIntegerNode(bigInteger);
    }
    
    @Override
    public boolean asBoolean(final boolean b) {
        return !BigInteger.ZERO.equals(this._value);
    }
    
    @Override
    public String asText() {
        return this._value.toString();
    }
    
    @Override
    public JsonToken asToken() {
        return JsonToken.VALUE_NUMBER_INT;
    }
    
    @Override
    public BigInteger bigIntegerValue() {
        return this._value;
    }
    
    @Override
    public BigDecimal decimalValue() {
        return new BigDecimal(this._value);
    }
    
    @Override
    public double doubleValue() {
        return this._value.doubleValue();
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = false;
        boolean b2;
        if (o == this) {
            b2 = true;
        }
        else {
            b2 = b;
            if (o != null) {
                b2 = b;
                if (o.getClass() == this.getClass()) {
                    return ((BigIntegerNode)o)._value.equals(this._value);
                }
            }
        }
        return b2;
    }
    
    @Override
    public int hashCode() {
        return this._value.hashCode();
    }
    
    @Override
    public int intValue() {
        return this._value.intValue();
    }
    
    @Override
    public long longValue() {
        return this._value.longValue();
    }
    
    @Override
    public JsonParser.NumberType numberType() {
        return JsonParser.NumberType.BIG_INTEGER;
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
