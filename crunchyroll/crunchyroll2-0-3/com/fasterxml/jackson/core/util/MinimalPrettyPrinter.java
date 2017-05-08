// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import java.io.Serializable;
import com.fasterxml.jackson.core.PrettyPrinter;

public class MinimalPrettyPrinter implements PrettyPrinter, Serializable
{
    protected String _rootValueSeparator;
    
    public MinimalPrettyPrinter() {
        this(" ");
    }
    
    public MinimalPrettyPrinter(final String rootValueSeparator) {
        this._rootValueSeparator = " ";
        this._rootValueSeparator = rootValueSeparator;
    }
    
    @Override
    public void beforeArrayValues(final JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
    }
    
    @Override
    public void beforeObjectEntries(final JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
    }
    
    @Override
    public void writeArrayValueSeparator(final JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
        jsonGenerator.writeRaw(',');
    }
    
    @Override
    public void writeEndArray(final JsonGenerator jsonGenerator, final int n) throws IOException, JsonGenerationException {
        jsonGenerator.writeRaw(']');
    }
    
    @Override
    public void writeEndObject(final JsonGenerator jsonGenerator, final int n) throws IOException, JsonGenerationException {
        jsonGenerator.writeRaw('}');
    }
    
    @Override
    public void writeObjectEntrySeparator(final JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
        jsonGenerator.writeRaw(',');
    }
    
    @Override
    public void writeObjectFieldValueSeparator(final JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
        jsonGenerator.writeRaw(':');
    }
    
    @Override
    public void writeRootValueSeparator(final JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
        if (this._rootValueSeparator != null) {
            jsonGenerator.writeRaw(this._rootValueSeparator);
        }
    }
    
    @Override
    public void writeStartArray(final JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
        jsonGenerator.writeRaw('[');
    }
    
    @Override
    public void writeStartObject(final JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
        jsonGenerator.writeRaw('{');
    }
}
