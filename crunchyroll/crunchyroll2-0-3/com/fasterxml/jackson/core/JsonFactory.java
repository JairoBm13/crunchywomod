// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.json.PackageVersion;
import java.io.StringReader;
import java.io.OutputStreamWriter;
import com.fasterxml.jackson.core.io.UTF8Writer;
import com.fasterxml.jackson.core.json.UTF8JsonGenerator;
import java.io.OutputStream;
import com.fasterxml.jackson.core.json.ReaderBasedJsonParser;
import java.io.Reader;
import java.io.IOException;
import com.fasterxml.jackson.core.json.WriterBasedJsonGenerator;
import java.io.Writer;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.sym.CharsToNameCanonicalizer;
import com.fasterxml.jackson.core.sym.BytesToNameCanonicalizer;
import com.fasterxml.jackson.core.io.OutputDecorator;
import com.fasterxml.jackson.core.io.InputDecorator;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.util.BufferRecycler;
import java.lang.ref.SoftReference;
import java.io.Serializable;

public class JsonFactory implements Versioned, Serializable
{
    protected static final int DEFAULT_FACTORY_FEATURE_FLAGS;
    protected static final int DEFAULT_GENERATOR_FEATURE_FLAGS;
    protected static final int DEFAULT_PARSER_FEATURE_FLAGS;
    private static final SerializableString DEFAULT_ROOT_VALUE_SEPARATOR;
    protected static final ThreadLocal<SoftReference<BufferRecycler>> _recyclerRef;
    protected CharacterEscapes _characterEscapes;
    protected int _factoryFeatures;
    protected int _generatorFeatures;
    protected InputDecorator _inputDecorator;
    protected ObjectCodec _objectCodec;
    protected OutputDecorator _outputDecorator;
    protected int _parserFeatures;
    protected final transient BytesToNameCanonicalizer _rootByteSymbols;
    protected final transient CharsToNameCanonicalizer _rootCharSymbols;
    protected SerializableString _rootValueSeparator;
    
    static {
        DEFAULT_FACTORY_FEATURE_FLAGS = Feature.collectDefaults();
        DEFAULT_PARSER_FEATURE_FLAGS = JsonParser.Feature.collectDefaults();
        DEFAULT_GENERATOR_FEATURE_FLAGS = JsonGenerator.Feature.collectDefaults();
        DEFAULT_ROOT_VALUE_SEPARATOR = DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR;
        _recyclerRef = new ThreadLocal<SoftReference<BufferRecycler>>();
    }
    
    public JsonFactory() {
        this(null);
    }
    
    public JsonFactory(final ObjectCodec objectCodec) {
        this._rootCharSymbols = CharsToNameCanonicalizer.createRoot();
        this._rootByteSymbols = BytesToNameCanonicalizer.createRoot();
        this._factoryFeatures = JsonFactory.DEFAULT_FACTORY_FEATURE_FLAGS;
        this._parserFeatures = JsonFactory.DEFAULT_PARSER_FEATURE_FLAGS;
        this._generatorFeatures = JsonFactory.DEFAULT_GENERATOR_FEATURE_FLAGS;
        this._rootValueSeparator = JsonFactory.DEFAULT_ROOT_VALUE_SEPARATOR;
        this._objectCodec = objectCodec;
    }
    
    protected IOContext _createContext(final Object o, final boolean b) {
        return new IOContext(this._getBufferRecycler(), o, b);
    }
    
    protected JsonGenerator _createGenerator(final Writer writer, final IOContext ioContext) throws IOException {
        final WriterBasedJsonGenerator writerBasedJsonGenerator = new WriterBasedJsonGenerator(ioContext, this._generatorFeatures, this._objectCodec, writer);
        if (this._characterEscapes != null) {
            writerBasedJsonGenerator.setCharacterEscapes(this._characterEscapes);
        }
        final SerializableString rootValueSeparator = this._rootValueSeparator;
        if (rootValueSeparator != JsonFactory.DEFAULT_ROOT_VALUE_SEPARATOR) {
            writerBasedJsonGenerator.setRootValueSeparator(rootValueSeparator);
        }
        return writerBasedJsonGenerator;
    }
    
    protected JsonParser _createParser(final Reader reader, final IOContext ioContext) throws IOException, JsonParseException {
        return new ReaderBasedJsonParser(ioContext, this._parserFeatures, reader, this._objectCodec, this._rootCharSymbols.makeChild(this.isEnabled(Feature.CANONICALIZE_FIELD_NAMES), this.isEnabled(Feature.INTERN_FIELD_NAMES)));
    }
    
    protected JsonGenerator _createUTF8Generator(final OutputStream outputStream, final IOContext ioContext) throws IOException {
        final UTF8JsonGenerator utf8JsonGenerator = new UTF8JsonGenerator(ioContext, this._generatorFeatures, this._objectCodec, outputStream);
        if (this._characterEscapes != null) {
            utf8JsonGenerator.setCharacterEscapes(this._characterEscapes);
        }
        final SerializableString rootValueSeparator = this._rootValueSeparator;
        if (rootValueSeparator != JsonFactory.DEFAULT_ROOT_VALUE_SEPARATOR) {
            utf8JsonGenerator.setRootValueSeparator(rootValueSeparator);
        }
        return utf8JsonGenerator;
    }
    
    protected Writer _createWriter(final OutputStream outputStream, final JsonEncoding jsonEncoding, final IOContext ioContext) throws IOException {
        if (jsonEncoding == JsonEncoding.UTF8) {
            return new UTF8Writer(ioContext, outputStream);
        }
        return new OutputStreamWriter(outputStream, jsonEncoding.getJavaName());
    }
    
    public BufferRecycler _getBufferRecycler() {
        final SoftReference<BufferRecycler> softReference = JsonFactory._recyclerRef.get();
        BufferRecycler bufferRecycler;
        if (softReference == null) {
            bufferRecycler = null;
        }
        else {
            bufferRecycler = softReference.get();
        }
        BufferRecycler bufferRecycler2 = bufferRecycler;
        if (bufferRecycler == null) {
            bufferRecycler2 = new BufferRecycler();
            JsonFactory._recyclerRef.set(new SoftReference<BufferRecycler>(bufferRecycler2));
        }
        return bufferRecycler2;
    }
    
    public JsonGenerator createGenerator(final OutputStream outputStream) throws IOException {
        return this.createGenerator(outputStream, JsonEncoding.UTF8);
    }
    
    public JsonGenerator createGenerator(final OutputStream outputStream, final JsonEncoding encoding) throws IOException {
        final IOContext createContext = this._createContext(outputStream, false);
        createContext.setEncoding(encoding);
        if (encoding == JsonEncoding.UTF8) {
            OutputStream decorate = outputStream;
            if (this._outputDecorator != null) {
                decorate = this._outputDecorator.decorate(createContext, outputStream);
            }
            return this._createUTF8Generator(decorate, createContext);
        }
        Writer writer = this._createWriter(outputStream, encoding, createContext);
        if (this._outputDecorator != null) {
            writer = this._outputDecorator.decorate(createContext, writer);
        }
        return this._createGenerator(writer, createContext);
    }
    
    public JsonParser createParser(final String s) throws IOException, JsonParseException {
        final StringReader stringReader = new StringReader(s);
        final IOContext createContext = this._createContext(stringReader, true);
        Reader decorate = stringReader;
        if (this._inputDecorator != null) {
            decorate = this._inputDecorator.decorate(createContext, stringReader);
        }
        return this._createParser(decorate, createContext);
    }
    
    public ObjectCodec getCodec() {
        return this._objectCodec;
    }
    
    public String getFormatName() {
        if (this.getClass() == JsonFactory.class) {
            return "JSON";
        }
        return null;
    }
    
    public final boolean isEnabled(final Feature feature) {
        return (this._factoryFeatures & feature.getMask()) != 0x0;
    }
    
    public JsonFactory setCodec(final ObjectCodec objectCodec) {
        this._objectCodec = objectCodec;
        return this;
    }
    
    @Override
    public Version version() {
        return PackageVersion.VERSION;
    }
    
    public enum Feature
    {
        CANONICALIZE_FIELD_NAMES(true), 
        INTERN_FIELD_NAMES(true);
        
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
}
