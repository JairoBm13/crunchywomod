// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.Version;
import java.math.BigDecimal;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonParseException;
import java.math.BigInteger;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonParser;

public class JsonParserDelegate extends JsonParser
{
    protected JsonParser delegate;
    
    public JsonParserDelegate(final JsonParser delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public void clearCurrentToken() {
        this.delegate.clearCurrentToken();
    }
    
    @Override
    public void close() throws IOException {
        this.delegate.close();
    }
    
    @Override
    public BigInteger getBigIntegerValue() throws IOException, JsonParseException {
        return this.delegate.getBigIntegerValue();
    }
    
    @Override
    public byte[] getBinaryValue(final Base64Variant base64Variant) throws IOException, JsonParseException {
        return this.delegate.getBinaryValue(base64Variant);
    }
    
    @Override
    public byte getByteValue() throws IOException, JsonParseException {
        return this.delegate.getByteValue();
    }
    
    @Override
    public ObjectCodec getCodec() {
        return this.delegate.getCodec();
    }
    
    @Override
    public JsonLocation getCurrentLocation() {
        return this.delegate.getCurrentLocation();
    }
    
    @Override
    public String getCurrentName() throws IOException, JsonParseException {
        return this.delegate.getCurrentName();
    }
    
    @Override
    public JsonToken getCurrentToken() {
        return this.delegate.getCurrentToken();
    }
    
    @Override
    public BigDecimal getDecimalValue() throws IOException, JsonParseException {
        return this.delegate.getDecimalValue();
    }
    
    @Override
    public double getDoubleValue() throws IOException, JsonParseException {
        return this.delegate.getDoubleValue();
    }
    
    @Override
    public Object getEmbeddedObject() throws IOException, JsonParseException {
        return this.delegate.getEmbeddedObject();
    }
    
    @Override
    public float getFloatValue() throws IOException, JsonParseException {
        return this.delegate.getFloatValue();
    }
    
    @Override
    public int getIntValue() throws IOException, JsonParseException {
        return this.delegate.getIntValue();
    }
    
    @Override
    public long getLongValue() throws IOException, JsonParseException {
        return this.delegate.getLongValue();
    }
    
    @Override
    public NumberType getNumberType() throws IOException, JsonParseException {
        return this.delegate.getNumberType();
    }
    
    @Override
    public Number getNumberValue() throws IOException, JsonParseException {
        return this.delegate.getNumberValue();
    }
    
    @Override
    public short getShortValue() throws IOException, JsonParseException {
        return this.delegate.getShortValue();
    }
    
    @Override
    public String getText() throws IOException, JsonParseException {
        return this.delegate.getText();
    }
    
    @Override
    public char[] getTextCharacters() throws IOException, JsonParseException {
        return this.delegate.getTextCharacters();
    }
    
    @Override
    public int getTextLength() throws IOException, JsonParseException {
        return this.delegate.getTextLength();
    }
    
    @Override
    public int getTextOffset() throws IOException, JsonParseException {
        return this.delegate.getTextOffset();
    }
    
    @Override
    public JsonLocation getTokenLocation() {
        return this.delegate.getTokenLocation();
    }
    
    @Override
    public int getValueAsInt() throws IOException, JsonParseException {
        return this.delegate.getValueAsInt();
    }
    
    @Override
    public int getValueAsInt(final int n) throws IOException, JsonParseException {
        return this.delegate.getValueAsInt(n);
    }
    
    @Override
    public long getValueAsLong() throws IOException, JsonParseException {
        return this.delegate.getValueAsLong();
    }
    
    @Override
    public long getValueAsLong(final long n) throws IOException, JsonParseException {
        return this.delegate.getValueAsLong(n);
    }
    
    @Override
    public String getValueAsString() throws IOException, JsonParseException {
        return this.delegate.getValueAsString();
    }
    
    @Override
    public String getValueAsString(final String s) throws IOException, JsonParseException {
        return this.delegate.getValueAsString(s);
    }
    
    @Override
    public boolean hasCurrentToken() {
        return this.delegate.hasCurrentToken();
    }
    
    @Override
    public boolean hasTextCharacters() {
        return this.delegate.hasTextCharacters();
    }
    
    @Override
    public boolean isEnabled(final Feature feature) {
        return this.delegate.isEnabled(feature);
    }
    
    @Override
    public JsonToken nextToken() throws IOException, JsonParseException {
        return this.delegate.nextToken();
    }
    
    @Override
    public JsonToken nextValue() throws IOException, JsonParseException {
        return this.delegate.nextValue();
    }
    
    @Override
    public JsonParser skipChildren() throws IOException, JsonParseException {
        this.delegate.skipChildren();
        return this;
    }
    
    @Override
    public Version version() {
        return this.delegate.version();
    }
}
