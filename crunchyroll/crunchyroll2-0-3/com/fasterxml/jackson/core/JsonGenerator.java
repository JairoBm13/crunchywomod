// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core;

import java.math.BigInteger;
import java.math.BigDecimal;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import java.io.IOException;
import java.io.Flushable;
import java.io.Closeable;

public abstract class JsonGenerator implements Versioned, Closeable, Flushable
{
    protected PrettyPrinter _cfgPrettyPrinter;
    
    @Override
    public abstract void close() throws IOException;
    
    @Override
    public abstract void flush() throws IOException;
    
    public PrettyPrinter getPrettyPrinter() {
        return this._cfgPrettyPrinter;
    }
    
    public JsonGenerator setCharacterEscapes(final CharacterEscapes characterEscapes) {
        return this;
    }
    
    public JsonGenerator setHighestNonEscapedChar(final int n) {
        return this;
    }
    
    public JsonGenerator setPrettyPrinter(final PrettyPrinter cfgPrettyPrinter) {
        this._cfgPrettyPrinter = cfgPrettyPrinter;
        return this;
    }
    
    public JsonGenerator setRootValueSeparator(final SerializableString serializableString) {
        throw new UnsupportedOperationException();
    }
    
    public abstract JsonGenerator useDefaultPrettyPrinter();
    
    @Override
    public abstract Version version();
    
    public final void writeArrayFieldStart(final String s) throws IOException, JsonGenerationException {
        this.writeFieldName(s);
        this.writeStartArray();
    }
    
    public abstract void writeBinary(final Base64Variant p0, final byte[] p1, final int p2, final int p3) throws IOException, JsonGenerationException;
    
    public void writeBinary(final byte[] array) throws IOException, JsonGenerationException {
        this.writeBinary(Base64Variants.getDefaultVariant(), array, 0, array.length);
    }
    
    public abstract void writeBoolean(final boolean p0) throws IOException, JsonGenerationException;
    
    public abstract void writeEndArray() throws IOException, JsonGenerationException;
    
    public abstract void writeEndObject() throws IOException, JsonGenerationException;
    
    public abstract void writeFieldName(final SerializableString p0) throws IOException, JsonGenerationException;
    
    public abstract void writeFieldName(final String p0) throws IOException, JsonGenerationException;
    
    public abstract void writeNull() throws IOException, JsonGenerationException;
    
    public abstract void writeNumber(final double p0) throws IOException, JsonGenerationException;
    
    public abstract void writeNumber(final float p0) throws IOException, JsonGenerationException;
    
    public abstract void writeNumber(final int p0) throws IOException, JsonGenerationException;
    
    public abstract void writeNumber(final long p0) throws IOException, JsonGenerationException;
    
    public abstract void writeNumber(final String p0) throws IOException, JsonGenerationException, UnsupportedOperationException;
    
    public abstract void writeNumber(final BigDecimal p0) throws IOException, JsonGenerationException;
    
    public abstract void writeNumber(final BigInteger p0) throws IOException, JsonGenerationException;
    
    public void writeNumber(final short n) throws IOException, JsonGenerationException {
        this.writeNumber((int)n);
    }
    
    public final void writeNumberField(final String s, final int n) throws IOException, JsonGenerationException {
        this.writeFieldName(s);
        this.writeNumber(n);
    }
    
    public abstract void writeObject(final Object p0) throws IOException, JsonProcessingException;
    
    public final void writeObjectField(final String s, final Object o) throws IOException, JsonProcessingException {
        this.writeFieldName(s);
        this.writeObject(o);
    }
    
    public final void writeObjectFieldStart(final String s) throws IOException, JsonGenerationException {
        this.writeFieldName(s);
        this.writeStartObject();
    }
    
    public abstract void writeRaw(final char p0) throws IOException, JsonGenerationException;
    
    public void writeRaw(final SerializableString serializableString) throws IOException, JsonGenerationException {
        this.writeRaw(serializableString.getValue());
    }
    
    public abstract void writeRaw(final String p0) throws IOException, JsonGenerationException;
    
    public abstract void writeRaw(final char[] p0, final int p1, final int p2) throws IOException, JsonGenerationException;
    
    public abstract void writeRawValue(final String p0) throws IOException, JsonGenerationException;
    
    public abstract void writeStartArray() throws IOException, JsonGenerationException;
    
    public abstract void writeStartObject() throws IOException, JsonGenerationException;
    
    public abstract void writeString(final SerializableString p0) throws IOException, JsonGenerationException;
    
    public abstract void writeString(final String p0) throws IOException, JsonGenerationException;
    
    public abstract void writeString(final char[] p0, final int p1, final int p2) throws IOException, JsonGenerationException;
    
    public void writeStringField(final String s, final String s2) throws IOException, JsonGenerationException {
        this.writeFieldName(s);
        this.writeString(s2);
    }
    
    public enum Feature
    {
        AUTO_CLOSE_JSON_CONTENT(true), 
        AUTO_CLOSE_TARGET(true), 
        ESCAPE_NON_ASCII(false), 
        FLUSH_PASSED_TO_STREAM(true), 
        QUOTE_FIELD_NAMES(true), 
        QUOTE_NON_NUMERIC_NUMBERS(true), 
        WRITE_NUMBERS_AS_STRINGS(false);
        
        private final boolean _defaultState;
        private final int _mask;
        
        private Feature(final boolean defaultState) {
            this._mask = 1 << this.ordinal();
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
            return this._mask;
        }
    }
}
