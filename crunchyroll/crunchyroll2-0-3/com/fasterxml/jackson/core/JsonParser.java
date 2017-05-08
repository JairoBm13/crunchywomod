// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.io.IOException;
import java.io.Closeable;

public abstract class JsonParser implements Versioned, Closeable
{
    protected int _features;
    
    protected JsonParser() {
    }
    
    protected JsonParser(final int features) {
        this._features = features;
    }
    
    protected JsonParseException _constructError(final String s) {
        return new JsonParseException(s, this.getCurrentLocation());
    }
    
    public abstract void clearCurrentToken();
    
    @Override
    public abstract void close() throws IOException;
    
    public abstract BigInteger getBigIntegerValue() throws IOException, JsonParseException;
    
    public abstract byte[] getBinaryValue(final Base64Variant p0) throws IOException, JsonParseException;
    
    public byte getByteValue() throws IOException, JsonParseException {
        final int intValue = this.getIntValue();
        if (intValue < -128 || intValue > 255) {
            throw this._constructError("Numeric value (" + this.getText() + ") out of range of Java byte");
        }
        return (byte)intValue;
    }
    
    public abstract ObjectCodec getCodec();
    
    public abstract JsonLocation getCurrentLocation();
    
    public abstract String getCurrentName() throws IOException, JsonParseException;
    
    public abstract JsonToken getCurrentToken();
    
    public abstract BigDecimal getDecimalValue() throws IOException, JsonParseException;
    
    public abstract double getDoubleValue() throws IOException, JsonParseException;
    
    public abstract Object getEmbeddedObject() throws IOException, JsonParseException;
    
    public abstract float getFloatValue() throws IOException, JsonParseException;
    
    public abstract int getIntValue() throws IOException, JsonParseException;
    
    public abstract long getLongValue() throws IOException, JsonParseException;
    
    public abstract NumberType getNumberType() throws IOException, JsonParseException;
    
    public abstract Number getNumberValue() throws IOException, JsonParseException;
    
    public short getShortValue() throws IOException, JsonParseException {
        final int intValue = this.getIntValue();
        if (intValue < -32768 || intValue > 32767) {
            throw this._constructError("Numeric value (" + this.getText() + ") out of range of Java short");
        }
        return (short)intValue;
    }
    
    public abstract String getText() throws IOException, JsonParseException;
    
    public abstract char[] getTextCharacters() throws IOException, JsonParseException;
    
    public abstract int getTextLength() throws IOException, JsonParseException;
    
    public abstract int getTextOffset() throws IOException, JsonParseException;
    
    public abstract JsonLocation getTokenLocation();
    
    public int getValueAsInt() throws IOException, JsonParseException {
        return this.getValueAsInt(0);
    }
    
    public int getValueAsInt(final int n) throws IOException, JsonParseException {
        return n;
    }
    
    public long getValueAsLong() throws IOException, JsonParseException {
        return this.getValueAsLong(0L);
    }
    
    public long getValueAsLong(final long n) throws IOException, JsonParseException {
        return n;
    }
    
    public String getValueAsString() throws IOException, JsonParseException {
        return this.getValueAsString(null);
    }
    
    public abstract String getValueAsString(final String p0) throws IOException, JsonParseException;
    
    public abstract boolean hasCurrentToken();
    
    public abstract boolean hasTextCharacters();
    
    public boolean isEnabled(final Feature feature) {
        return (this._features & feature.getMask()) != 0x0;
    }
    
    public boolean isExpectedStartArrayToken() {
        return this.getCurrentToken() == JsonToken.START_ARRAY;
    }
    
    public abstract JsonToken nextToken() throws IOException, JsonParseException;
    
    public abstract JsonToken nextValue() throws IOException, JsonParseException;
    
    public <T extends TreeNode> T readValueAsTree() throws IOException, JsonProcessingException {
        final ObjectCodec codec = this.getCodec();
        if (codec == null) {
            throw new IllegalStateException("No ObjectCodec defined for the parser, can not deserialize JSON into JsonNode tree");
        }
        return (T)codec.readTree(this);
    }
    
    public abstract JsonParser skipChildren() throws IOException, JsonParseException;
    
    @Override
    public abstract Version version();
    
    public enum Feature
    {
        ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER(false), 
        ALLOW_COMMENTS(false), 
        ALLOW_NON_NUMERIC_NUMBERS(false), 
        ALLOW_NUMERIC_LEADING_ZEROS(false), 
        ALLOW_SINGLE_QUOTES(false), 
        ALLOW_UNQUOTED_CONTROL_CHARS(false), 
        ALLOW_UNQUOTED_FIELD_NAMES(false), 
        AUTO_CLOSE_SOURCE(true);
        
        private final boolean _defaultState;
        
        private Feature(final boolean defaultState) {
            this._defaultState = defaultState;
        }
        
        public static int collectDefaults() {
            int n = 0;
            final Feature[] values = values();
            int n2;
            for (int length = values.length, i = 0; i < length; ++i, n = n2) {
                final Feature feature = values[i];
                n2 = n;
                if (feature.enabledByDefault()) {
                    n2 = (n | feature.getMask());
                }
            }
            return n;
        }
        
        public boolean enabledByDefault() {
            return this._defaultState;
        }
        
        public int getMask() {
            return 1 << this.ordinal();
        }
    }
    
    public enum NumberType
    {
        BIG_DECIMAL, 
        BIG_INTEGER, 
        DOUBLE, 
        FLOAT, 
        INT, 
        LONG;
    }
}
