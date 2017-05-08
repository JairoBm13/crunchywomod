// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import java.math.BigInteger;
import com.fasterxml.jackson.core.JsonToken;
import java.math.BigDecimal;

public final class DecimalNode extends NumericNode
{
    private static final BigDecimal MAX_INTEGER;
    private static final BigDecimal MAX_LONG;
    private static final BigDecimal MIN_INTEGER;
    private static final BigDecimal MIN_LONG;
    public static final DecimalNode ZERO;
    protected final BigDecimal _value;
    
    static {
        ZERO = new DecimalNode(BigDecimal.ZERO);
        MIN_INTEGER = BigDecimal.valueOf(-2147483648L);
        MAX_INTEGER = BigDecimal.valueOf(2147483647L);
        MIN_LONG = BigDecimal.valueOf(Long.MIN_VALUE);
        MAX_LONG = BigDecimal.valueOf(Long.MAX_VALUE);
    }
    
    public DecimalNode(final BigDecimal value) {
        this._value = value;
    }
    
    public static DecimalNode valueOf(final BigDecimal bigDecimal) {
        return new DecimalNode(bigDecimal);
    }
    
    @Override
    public String asText() {
        return this._value.toString();
    }
    
    @Override
    public JsonToken asToken() {
        return JsonToken.VALUE_NUMBER_FLOAT;
    }
    
    @Override
    public BigInteger bigIntegerValue() {
        return this._value.toBigInteger();
    }
    
    @Override
    public BigDecimal decimalValue() {
        return this._value;
    }
    
    @Override
    public double doubleValue() {
        return this._value.doubleValue();
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
            if (((DecimalNode)o)._value.compareTo(this._value) != 0) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        return Double.valueOf(this._value.doubleValue()).hashCode();
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
        return JsonParser.NumberType.BIG_DECIMAL;
    }
    
    @Override
    public Number numberValue() {
        return this._value;
    }
    
    @Override
    public final void serialize(final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        if (serializerProvider.isEnabled(SerializationFeature.WRITE_BIGDECIMAL_AS_PLAIN) && !(jsonGenerator instanceof TokenBuffer)) {
            jsonGenerator.writeNumber(this._value.toPlainString());
            return;
        }
        jsonGenerator.writeNumber(this._value);
    }
}
