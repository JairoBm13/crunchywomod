// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core;

public enum JsonToken
{
    END_ARRAY("]"), 
    END_OBJECT("}"), 
    FIELD_NAME((String)null), 
    NOT_AVAILABLE((String)null), 
    START_ARRAY("["), 
    START_OBJECT("{"), 
    VALUE_EMBEDDED_OBJECT((String)null), 
    VALUE_FALSE("false"), 
    VALUE_NULL("null"), 
    VALUE_NUMBER_FLOAT((String)null), 
    VALUE_NUMBER_INT((String)null), 
    VALUE_STRING((String)null), 
    VALUE_TRUE("true");
    
    final String _serialized;
    final byte[] _serializedBytes;
    final char[] _serializedChars;
    
    private JsonToken(final String serialized) {
        if (serialized == null) {
            this._serialized = null;
            this._serializedChars = null;
            this._serializedBytes = null;
        }
        else {
            this._serialized = serialized;
            this._serializedChars = serialized.toCharArray();
            final int length = this._serializedChars.length;
            this._serializedBytes = new byte[length];
            for (i = 0; i < length; ++i) {
                this._serializedBytes[i] = (byte)this._serializedChars[i];
            }
        }
    }
    
    public char[] asCharArray() {
        return this._serializedChars;
    }
    
    public String asString() {
        return this._serialized;
    }
    
    public boolean isNumeric() {
        return this == JsonToken.VALUE_NUMBER_INT || this == JsonToken.VALUE_NUMBER_FLOAT;
    }
    
    public boolean isScalarValue() {
        return this.ordinal() >= JsonToken.VALUE_EMBEDDED_OBJECT.ordinal();
    }
}
