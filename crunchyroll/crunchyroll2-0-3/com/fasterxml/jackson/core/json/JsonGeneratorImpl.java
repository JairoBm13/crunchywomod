// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.base.GeneratorBase;

public abstract class JsonGeneratorImpl extends GeneratorBase
{
    protected static final int[] sOutputEscapes;
    protected CharacterEscapes _characterEscapes;
    protected final IOContext _ioContext;
    protected int _maximumNonEscapedChar;
    protected int[] _outputEscapes;
    protected SerializableString _rootValueSeparator;
    
    static {
        sOutputEscapes = CharTypes.get7BitOutputEscapes();
    }
    
    public JsonGeneratorImpl(final IOContext ioContext, final int n, final ObjectCodec objectCodec) {
        super(n, objectCodec);
        this._outputEscapes = JsonGeneratorImpl.sOutputEscapes;
        this._rootValueSeparator = DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR;
        this._ioContext = ioContext;
        if (this.isEnabled(Feature.ESCAPE_NON_ASCII)) {
            this.setHighestNonEscapedChar(127);
        }
    }
    
    @Override
    public JsonGenerator setCharacterEscapes(final CharacterEscapes characterEscapes) {
        this._characterEscapes = characterEscapes;
        if (characterEscapes == null) {
            this._outputEscapes = JsonGeneratorImpl.sOutputEscapes;
            return this;
        }
        this._outputEscapes = characterEscapes.getEscapeCodesForAscii();
        return this;
    }
    
    @Override
    public JsonGenerator setHighestNonEscapedChar(final int n) {
        int maximumNonEscapedChar = n;
        if (n < 0) {
            maximumNonEscapedChar = 0;
        }
        this._maximumNonEscapedChar = maximumNonEscapedChar;
        return this;
    }
    
    @Override
    public JsonGenerator setRootValueSeparator(final SerializableString rootValueSeparator) {
        this._rootValueSeparator = rootValueSeparator;
        return this;
    }
    
    @Override
    public Version version() {
        return VersionUtil.versionFor(this.getClass());
    }
    
    @Override
    public final void writeStringField(final String s, final String s2) throws IOException, JsonGenerationException {
        this.writeFieldName(s);
        this.writeString(s2);
    }
}
