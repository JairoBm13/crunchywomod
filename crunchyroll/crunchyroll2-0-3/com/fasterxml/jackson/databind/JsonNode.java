// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.databind.node.JsonNodeType;
import java.util.Map;
import com.fasterxml.jackson.databind.util.EmptyIterator;
import java.util.Iterator;
import java.math.BigDecimal;
import java.io.IOException;
import java.math.BigInteger;
import com.fasterxml.jackson.core.TreeNode;

public abstract class JsonNode implements TreeNode, Iterable<JsonNode>
{
    public boolean asBoolean() {
        return this.asBoolean(false);
    }
    
    public boolean asBoolean(final boolean b) {
        return b;
    }
    
    public int asInt() {
        return this.asInt(0);
    }
    
    public int asInt(final int n) {
        return n;
    }
    
    public abstract String asText();
    
    public BigInteger bigIntegerValue() {
        return BigInteger.ZERO;
    }
    
    public byte[] binaryValue() throws IOException {
        return null;
    }
    
    public BigDecimal decimalValue() {
        return BigDecimal.ZERO;
    }
    
    public double doubleValue() {
        return 0.0;
    }
    
    public Iterator<JsonNode> elements() {
        return EmptyIterator.instance();
    }
    
    @Override
    public abstract boolean equals(final Object p0);
    
    public Iterator<Map.Entry<String, JsonNode>> fields() {
        return EmptyIterator.instance();
    }
    
    @Override
    public JsonNode get(final String s) {
        return null;
    }
    
    public abstract JsonNodeType getNodeType();
    
    public boolean has(final String s) {
        return this.get(s) != null;
    }
    
    public int intValue() {
        return 0;
    }
    
    public final boolean isArray() {
        return this.getNodeType() == JsonNodeType.ARRAY;
    }
    
    public final boolean isBinary() {
        return this.getNodeType() == JsonNodeType.BINARY;
    }
    
    public final boolean isMissingNode() {
        return this.getNodeType() == JsonNodeType.MISSING;
    }
    
    public final boolean isNull() {
        return this.getNodeType() == JsonNodeType.NULL;
    }
    
    public final boolean isNumber() {
        return this.getNodeType() == JsonNodeType.NUMBER;
    }
    
    public final boolean isObject() {
        return this.getNodeType() == JsonNodeType.OBJECT;
    }
    
    public final boolean isPojo() {
        return this.getNodeType() == JsonNodeType.POJO;
    }
    
    @Override
    public final Iterator<JsonNode> iterator() {
        return this.elements();
    }
    
    public long longValue() {
        return 0L;
    }
    
    public Number numberValue() {
        return null;
    }
    
    public abstract JsonNode path(final int p0);
    
    public abstract JsonNode path(final String p0);
    
    public int size() {
        return 0;
    }
    
    public String textValue() {
        return null;
    }
    
    @Override
    public abstract String toString();
}
