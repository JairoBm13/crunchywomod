// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.node;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.io.Serializable;

public class JsonNodeFactory implements JsonNodeCreator, Serializable
{
    private static final JsonNodeFactory decimalsAsIs;
    private static final JsonNodeFactory decimalsNormalized;
    public static final JsonNodeFactory instance;
    private final boolean _cfgBigDecimalExact;
    
    static {
        decimalsNormalized = new JsonNodeFactory(false);
        decimalsAsIs = new JsonNodeFactory(true);
        instance = JsonNodeFactory.decimalsNormalized;
    }
    
    protected JsonNodeFactory() {
        this(false);
    }
    
    public JsonNodeFactory(final boolean cfgBigDecimalExact) {
        this._cfgBigDecimalExact = cfgBigDecimalExact;
    }
    
    public ArrayNode arrayNode() {
        return new ArrayNode(this);
    }
    
    public BinaryNode binaryNode(final byte[] array) {
        return BinaryNode.valueOf(array);
    }
    
    public BooleanNode booleanNode(final boolean b) {
        if (b) {
            return BooleanNode.getTrue();
        }
        return BooleanNode.getFalse();
    }
    
    public NullNode nullNode() {
        return NullNode.getInstance();
    }
    
    public NumericNode numberNode(final double n) {
        return DoubleNode.valueOf(n);
    }
    
    public NumericNode numberNode(final int n) {
        return IntNode.valueOf(n);
    }
    
    public NumericNode numberNode(final long n) {
        return LongNode.valueOf(n);
    }
    
    public NumericNode numberNode(final BigDecimal bigDecimal) {
        if (this._cfgBigDecimalExact) {
            return DecimalNode.valueOf(bigDecimal);
        }
        if (bigDecimal.compareTo(BigDecimal.ZERO) == 0) {
            return DecimalNode.ZERO;
        }
        return DecimalNode.valueOf(bigDecimal.stripTrailingZeros());
    }
    
    public NumericNode numberNode(final BigInteger bigInteger) {
        return BigIntegerNode.valueOf(bigInteger);
    }
    
    public ObjectNode objectNode() {
        return new ObjectNode(this);
    }
    
    public ValueNode pojoNode(final Object o) {
        return new POJONode(o);
    }
    
    public TextNode textNode(final String s) {
        return TextNode.valueOf(s);
    }
}
