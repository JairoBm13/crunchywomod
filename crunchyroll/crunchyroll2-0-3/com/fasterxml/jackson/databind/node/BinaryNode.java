// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import java.util.Arrays;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.Base64Variants;

public final class BinaryNode extends ValueNode
{
    static final BinaryNode EMPTY_BINARY_NODE;
    final byte[] _data;
    
    static {
        EMPTY_BINARY_NODE = new BinaryNode(new byte[0]);
    }
    
    public BinaryNode(final byte[] data) {
        this._data = data;
    }
    
    public static BinaryNode valueOf(final byte[] array) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return BinaryNode.EMPTY_BINARY_NODE;
        }
        return new BinaryNode(array);
    }
    
    @Override
    public String asText() {
        return Base64Variants.getDefaultVariant().encode(this._data, false);
    }
    
    @Override
    public JsonToken asToken() {
        return JsonToken.VALUE_EMBEDDED_OBJECT;
    }
    
    @Override
    public byte[] binaryValue() {
        return this._data;
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
                    return Arrays.equals(((BinaryNode)o)._data, this._data);
                }
            }
        }
        return b2;
    }
    
    @Override
    public JsonNodeType getNodeType() {
        return JsonNodeType.BINARY;
    }
    
    @Override
    public int hashCode() {
        if (this._data == null) {
            return -1;
        }
        return this._data.length;
    }
    
    @Override
    public final void serialize(final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeBinary(serializerProvider.getConfig().getBase64Variant(), this._data, 0, this._data.length);
    }
    
    @Override
    public String toString() {
        return Base64Variants.getDefaultVariant().encode(this._data, true);
    }
}
