// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind;

import java.util.NoSuchElementException;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonParser;
import java.util.Iterator;
import java.io.Closeable;

public class MappingIterator<T> implements Closeable, Iterator<T>
{
    protected static final MappingIterator<?> EMPTY_ITERATOR;
    protected final boolean _closeParser;
    protected final DeserializationContext _context;
    protected final JsonDeserializer<T> _deserializer;
    protected boolean _hasNextChecked;
    protected JsonParser _parser;
    protected final JavaType _type;
    protected final T _updatedValue;
    
    static {
        EMPTY_ITERATOR = new MappingIterator<Object>(null, null, null, null, false, null);
    }
    
    protected MappingIterator(final JavaType type, final JsonParser parser, final DeserializationContext context, final JsonDeserializer<?> deserializer, final boolean closeParser, final Object updatedValue) {
        this._type = type;
        this._parser = parser;
        this._context = context;
        this._deserializer = (JsonDeserializer<T>)deserializer;
        this._closeParser = closeParser;
        if (updatedValue == null) {
            this._updatedValue = null;
        }
        else {
            this._updatedValue = (T)updatedValue;
        }
        if (closeParser && parser != null && parser.getCurrentToken() == JsonToken.START_ARRAY) {
            parser.clearCurrentToken();
        }
    }
    
    @Override
    public void close() throws IOException {
        if (this._parser != null) {
            this._parser.close();
        }
    }
    
    @Override
    public boolean hasNext() {
        try {
            return this.hasNextValue();
        }
        catch (JsonMappingException ex) {
            throw new RuntimeJsonMappingException(ex.getMessage(), ex);
        }
        catch (IOException ex2) {
            throw new RuntimeException(ex2.getMessage(), ex2);
        }
    }
    
    public boolean hasNextValue() throws IOException {
        if (this._parser != null) {
            if (!this._hasNextChecked) {
                final JsonToken currentToken = this._parser.getCurrentToken();
                this._hasNextChecked = true;
                if (currentToken == null) {
                    final JsonToken nextToken = this._parser.nextToken();
                    if (nextToken == null || nextToken == JsonToken.END_ARRAY) {
                        final JsonParser parser = this._parser;
                        this._parser = null;
                        if (this._closeParser) {
                            parser.close();
                            return false;
                        }
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    @Override
    public T next() {
        try {
            return this.nextValue();
        }
        catch (JsonMappingException ex) {
            throw new RuntimeJsonMappingException(ex.getMessage(), ex);
        }
        catch (IOException ex2) {
            throw new RuntimeException(ex2.getMessage(), ex2);
        }
    }
    
    public T nextValue() throws IOException {
        if (!this._hasNextChecked && !this.hasNextValue()) {
            throw new NoSuchElementException();
        }
        if (this._parser == null) {
            throw new NoSuchElementException();
        }
        this._hasNextChecked = false;
        T t;
        if (this._updatedValue == null) {
            t = this._deserializer.deserialize(this._parser, this._context);
        }
        else {
            this._deserializer.deserialize(this._parser, this._context, this._updatedValue);
            t = this._updatedValue;
        }
        this._parser.clearCurrentToken();
        return t;
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
