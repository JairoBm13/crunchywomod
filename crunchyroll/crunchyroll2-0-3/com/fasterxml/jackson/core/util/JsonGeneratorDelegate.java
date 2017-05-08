// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.math.BigInteger;
import java.math.BigDecimal;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.PrettyPrinter;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;

public class JsonGeneratorDelegate extends JsonGenerator
{
    protected JsonGenerator delegate;
    
    @Override
    public void close() throws IOException {
        this.delegate.close();
    }
    
    @Override
    public void flush() throws IOException {
        this.delegate.flush();
    }
    
    @Override
    public PrettyPrinter getPrettyPrinter() {
        return this.delegate.getPrettyPrinter();
    }
    
    @Override
    public JsonGenerator setCharacterEscapes(final CharacterEscapes characterEscapes) {
        this.delegate.setCharacterEscapes(characterEscapes);
        return this;
    }
    
    @Override
    public JsonGenerator setHighestNonEscapedChar(final int highestNonEscapedChar) {
        this.delegate.setHighestNonEscapedChar(highestNonEscapedChar);
        return this;
    }
    
    @Override
    public JsonGenerator setPrettyPrinter(final PrettyPrinter prettyPrinter) {
        this.delegate.setPrettyPrinter(prettyPrinter);
        return this;
    }
    
    @Override
    public JsonGenerator setRootValueSeparator(final SerializableString rootValueSeparator) {
        this.delegate.setRootValueSeparator(rootValueSeparator);
        return this;
    }
    
    @Override
    public JsonGenerator useDefaultPrettyPrinter() {
        this.delegate.useDefaultPrettyPrinter();
        return this;
    }
    
    @Override
    public Version version() {
        return this.delegate.version();
    }
    
    @Override
    public void writeBinary(final Base64Variant base64Variant, final byte[] array, final int n, final int n2) throws IOException, JsonGenerationException {
        this.delegate.writeBinary(base64Variant, array, n, n2);
    }
    
    @Override
    public void writeBoolean(final boolean b) throws IOException, JsonGenerationException {
        this.delegate.writeBoolean(b);
    }
    
    @Override
    public void writeEndArray() throws IOException, JsonGenerationException {
        this.delegate.writeEndArray();
    }
    
    @Override
    public void writeEndObject() throws IOException, JsonGenerationException {
        this.delegate.writeEndObject();
    }
    
    @Override
    public void writeFieldName(final SerializableString serializableString) throws IOException, JsonGenerationException {
        this.delegate.writeFieldName(serializableString);
    }
    
    @Override
    public void writeFieldName(final String s) throws IOException, JsonGenerationException {
        this.delegate.writeFieldName(s);
    }
    
    @Override
    public void writeNull() throws IOException, JsonGenerationException {
        this.delegate.writeNull();
    }
    
    @Override
    public void writeNumber(final double n) throws IOException, JsonGenerationException {
        this.delegate.writeNumber(n);
    }
    
    @Override
    public void writeNumber(final float n) throws IOException, JsonGenerationException {
        this.delegate.writeNumber(n);
    }
    
    @Override
    public void writeNumber(final int n) throws IOException, JsonGenerationException {
        this.delegate.writeNumber(n);
    }
    
    @Override
    public void writeNumber(final long n) throws IOException, JsonGenerationException {
        this.delegate.writeNumber(n);
    }
    
    @Override
    public void writeNumber(final String s) throws IOException, JsonGenerationException, UnsupportedOperationException {
        this.delegate.writeNumber(s);
    }
    
    @Override
    public void writeNumber(final BigDecimal bigDecimal) throws IOException, JsonGenerationException {
        this.delegate.writeNumber(bigDecimal);
    }
    
    @Override
    public void writeNumber(final BigInteger bigInteger) throws IOException, JsonGenerationException {
        this.delegate.writeNumber(bigInteger);
    }
    
    @Override
    public void writeNumber(final short n) throws IOException, JsonGenerationException {
        this.delegate.writeNumber(n);
    }
    
    @Override
    public void writeObject(final Object o) throws IOException, JsonProcessingException {
        this.delegate.writeObject(o);
    }
    
    @Override
    public void writeRaw(final char c) throws IOException, JsonGenerationException {
        this.delegate.writeRaw(c);
    }
    
    @Override
    public void writeRaw(final SerializableString serializableString) throws IOException, JsonGenerationException {
        this.delegate.writeRaw(serializableString);
    }
    
    @Override
    public void writeRaw(final String s) throws IOException, JsonGenerationException {
        this.delegate.writeRaw(s);
    }
    
    @Override
    public void writeRaw(final char[] array, final int n, final int n2) throws IOException, JsonGenerationException {
        this.delegate.writeRaw(array, n, n2);
    }
    
    @Override
    public void writeRawValue(final String s) throws IOException, JsonGenerationException {
        this.delegate.writeRawValue(s);
    }
    
    @Override
    public void writeStartArray() throws IOException, JsonGenerationException {
        this.delegate.writeStartArray();
    }
    
    @Override
    public void writeStartObject() throws IOException, JsonGenerationException {
        this.delegate.writeStartObject();
    }
    
    @Override
    public void writeString(final SerializableString serializableString) throws IOException, JsonGenerationException {
        this.delegate.writeString(serializableString);
    }
    
    @Override
    public void writeString(final String s) throws IOException, JsonGenerationException {
        this.delegate.writeString(s);
    }
    
    @Override
    public void writeString(final char[] array, final int n, final int n2) throws IOException, JsonGenerationException {
        this.delegate.writeString(array, n, n2);
    }
}
