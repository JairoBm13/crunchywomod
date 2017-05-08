// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.util;

import java.util.Arrays;
import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.SerializedString;
import java.io.Serializable;
import com.fasterxml.jackson.core.PrettyPrinter;

public class DefaultPrettyPrinter implements PrettyPrinter, Instantiatable<DefaultPrettyPrinter>, Serializable
{
    public static final SerializedString DEFAULT_ROOT_VALUE_SEPARATOR;
    protected Indenter _arrayIndenter;
    protected transient int _nesting;
    protected Indenter _objectIndenter;
    protected final SerializableString _rootSeparator;
    protected boolean _spacesInObjectEntries;
    
    static {
        DEFAULT_ROOT_VALUE_SEPARATOR = new SerializedString(" ");
    }
    
    public DefaultPrettyPrinter() {
        this(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
    }
    
    public DefaultPrettyPrinter(final SerializableString rootSeparator) {
        this._arrayIndenter = (Indenter)FixedSpaceIndenter.instance;
        this._objectIndenter = (Indenter)Lf2SpacesIndenter.instance;
        this._spacesInObjectEntries = true;
        this._nesting = 0;
        this._rootSeparator = rootSeparator;
    }
    
    @Override
    public void beforeArrayValues(final JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
        this._arrayIndenter.writeIndentation(jsonGenerator, this._nesting);
    }
    
    @Override
    public void beforeObjectEntries(final JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
        this._objectIndenter.writeIndentation(jsonGenerator, this._nesting);
    }
    
    @Override
    public void writeArrayValueSeparator(final JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
        jsonGenerator.writeRaw(',');
        this._arrayIndenter.writeIndentation(jsonGenerator, this._nesting);
    }
    
    @Override
    public void writeEndArray(final JsonGenerator jsonGenerator, final int n) throws IOException, JsonGenerationException {
        if (!this._arrayIndenter.isInline()) {
            --this._nesting;
        }
        if (n > 0) {
            this._arrayIndenter.writeIndentation(jsonGenerator, this._nesting);
        }
        else {
            jsonGenerator.writeRaw(' ');
        }
        jsonGenerator.writeRaw(']');
    }
    
    @Override
    public void writeEndObject(final JsonGenerator jsonGenerator, final int n) throws IOException, JsonGenerationException {
        if (!this._objectIndenter.isInline()) {
            --this._nesting;
        }
        if (n > 0) {
            this._objectIndenter.writeIndentation(jsonGenerator, this._nesting);
        }
        else {
            jsonGenerator.writeRaw(' ');
        }
        jsonGenerator.writeRaw('}');
    }
    
    @Override
    public void writeObjectEntrySeparator(final JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
        jsonGenerator.writeRaw(',');
        this._objectIndenter.writeIndentation(jsonGenerator, this._nesting);
    }
    
    @Override
    public void writeObjectFieldValueSeparator(final JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
        if (this._spacesInObjectEntries) {
            jsonGenerator.writeRaw(" : ");
            return;
        }
        jsonGenerator.writeRaw(':');
    }
    
    @Override
    public void writeRootValueSeparator(final JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
        if (this._rootSeparator != null) {
            jsonGenerator.writeRaw(this._rootSeparator);
        }
    }
    
    @Override
    public void writeStartArray(final JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
        if (!this._arrayIndenter.isInline()) {
            ++this._nesting;
        }
        jsonGenerator.writeRaw('[');
    }
    
    @Override
    public void writeStartObject(final JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
        jsonGenerator.writeRaw('{');
        if (!this._objectIndenter.isInline()) {
            ++this._nesting;
        }
    }
    
    public static class FixedSpaceIndenter extends NopIndenter
    {
        public static final FixedSpaceIndenter instance;
        
        static {
            instance = new FixedSpaceIndenter();
        }
        
        @Override
        public boolean isInline() {
            return true;
        }
        
        @Override
        public void writeIndentation(final JsonGenerator jsonGenerator, final int n) throws IOException, JsonGenerationException {
            jsonGenerator.writeRaw(' ');
        }
    }
    
    public interface Indenter
    {
        boolean isInline();
        
        void writeIndentation(final JsonGenerator p0, final int p1) throws IOException, JsonGenerationException;
    }
    
    public static class Lf2SpacesIndenter extends NopIndenter
    {
        static final char[] SPACES;
        private static final String SYS_LF;
        public static final Lf2SpacesIndenter instance;
        
        static {
            instance = new Lf2SpacesIndenter();
            String property = null;
            while (true) {
                try {
                    property = System.getProperty("line.separator");
                    String sys_LF = property;
                    if (property == null) {
                        sys_LF = "\n";
                    }
                    SYS_LF = sys_LF;
                    Arrays.fill(SPACES = new char[64], ' ');
                }
                catch (Throwable t) {
                    continue;
                }
                break;
            }
        }
        
        @Override
        public boolean isInline() {
            return false;
        }
        
        @Override
        public void writeIndentation(final JsonGenerator jsonGenerator, int i) throws IOException, JsonGenerationException {
            jsonGenerator.writeRaw(Lf2SpacesIndenter.SYS_LF);
            if (i > 0) {
                for (i += i; i > 64; i -= Lf2SpacesIndenter.SPACES.length) {
                    jsonGenerator.writeRaw(Lf2SpacesIndenter.SPACES, 0, 64);
                }
                jsonGenerator.writeRaw(Lf2SpacesIndenter.SPACES, 0, i);
            }
        }
    }
    
    public static class NopIndenter implements Indenter, Serializable
    {
        public static final NopIndenter instance;
        
        static {
            instance = new NopIndenter();
        }
        
        @Override
        public boolean isInline() {
            return true;
        }
        
        @Override
        public void writeIndentation(final JsonGenerator jsonGenerator, final int n) throws IOException, JsonGenerationException {
        }
    }
}
