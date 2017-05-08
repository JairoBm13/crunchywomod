// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.base;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.io.NumberInput;
import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonParser;

public abstract class ParserMinimalBase extends JsonParser
{
    protected JsonToken _currToken;
    protected JsonToken _lastClearedToken;
    
    protected ParserMinimalBase() {
    }
    
    protected ParserMinimalBase(final int n) {
        super(n);
    }
    
    protected static final String _getCharDesc(final int n) {
        final char c = (char)n;
        if (Character.isISOControl(c)) {
            return "(CTRL-CHAR, code " + n + ")";
        }
        if (n > 255) {
            return "'" + c + "' (code " + n + " / 0x" + Integer.toHexString(n) + ")";
        }
        return "'" + c + "' (code " + n + ")";
    }
    
    protected final JsonParseException _constructError(final String s, final Throwable t) {
        return new JsonParseException(s, this.getCurrentLocation(), t);
    }
    
    protected void _decodeBase64(final String s, final ByteArrayBuilder byteArrayBuilder, final Base64Variant base64Variant) throws IOException, JsonParseException {
        try {
            base64Variant.decode(s, byteArrayBuilder);
        }
        catch (IllegalArgumentException ex) {
            this._reportError(ex.getMessage());
        }
    }
    
    protected abstract void _handleEOF() throws JsonParseException;
    
    protected char _handleUnrecognizedCharacterEscape(final char c) throws JsonProcessingException {
        if (!this.isEnabled(Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER) && (c != '\'' || !this.isEnabled(Feature.ALLOW_SINGLE_QUOTES))) {
            this._reportError("Unrecognized character escape " + _getCharDesc(c));
            return c;
        }
        return c;
    }
    
    protected final void _reportError(final String s) throws JsonParseException {
        throw this._constructError(s);
    }
    
    protected void _reportInvalidEOF() throws JsonParseException {
        this._reportInvalidEOF(" in " + this._currToken);
    }
    
    protected void _reportInvalidEOF(final String s) throws JsonParseException {
        this._reportError("Unexpected end-of-input" + s);
    }
    
    protected void _reportInvalidEOFInValue() throws JsonParseException {
        this._reportInvalidEOF(" in a value");
    }
    
    protected void _reportUnexpectedChar(final int n, final String s) throws JsonParseException {
        String s2 = "Unexpected character (" + _getCharDesc(n) + ")";
        if (s != null) {
            s2 = s2 + ": " + s;
        }
        this._reportError(s2);
    }
    
    protected final void _throwInternal() {
        VersionUtil.throwInternal();
    }
    
    protected void _throwInvalidSpace(int n) throws JsonParseException {
        n = (char)n;
        this._reportError("Illegal character (" + _getCharDesc(n) + "): only regular white space (\\r, \\n, \\t) is allowed between tokens");
    }
    
    protected void _throwUnquotedSpace(int n, final String s) throws JsonParseException {
        if (!this.isEnabled(Feature.ALLOW_UNQUOTED_CONTROL_CHARS) || n >= 32) {
            n = (char)n;
            this._reportError("Illegal unquoted character (" + _getCharDesc(n) + "): has to be escaped using backslash to be included in " + s);
        }
    }
    
    protected final void _wrapError(final String s, final Throwable t) throws JsonParseException {
        throw this._constructError(s, t);
    }
    
    @Override
    public void clearCurrentToken() {
        if (this._currToken != null) {
            this._lastClearedToken = this._currToken;
            this._currToken = null;
        }
    }
    
    @Override
    public JsonToken getCurrentToken() {
        return this._currToken;
    }
    
    @Override
    public abstract String getText() throws IOException, JsonParseException;
    
    @Override
    public int getValueAsInt(final int n) throws IOException, JsonParseException {
        if (this._currToken != null) {
            switch (this._currToken) {
                case VALUE_NUMBER_INT:
                case VALUE_NUMBER_FLOAT: {
                    return this.getIntValue();
                }
                case VALUE_TRUE: {
                    return 1;
                }
                case VALUE_FALSE:
                case VALUE_NULL: {
                    return 0;
                }
                case VALUE_STRING: {
                    return NumberInput.parseAsInt(this.getText(), n);
                }
                case VALUE_EMBEDDED_OBJECT: {
                    final Object embeddedObject = this.getEmbeddedObject();
                    if (embeddedObject instanceof Number) {
                        return ((Number)embeddedObject).intValue();
                    }
                    break;
                }
            }
        }
        return n;
    }
    
    @Override
    public long getValueAsLong(final long n) throws IOException, JsonParseException {
        if (this._currToken != null) {
            switch (this._currToken) {
                case VALUE_NUMBER_INT:
                case VALUE_NUMBER_FLOAT: {
                    return this.getLongValue();
                }
                case VALUE_TRUE: {
                    return 1L;
                }
                case VALUE_FALSE:
                case VALUE_NULL: {
                    return 0L;
                }
                case VALUE_STRING: {
                    return NumberInput.parseAsLong(this.getText(), n);
                }
                case VALUE_EMBEDDED_OBJECT: {
                    final Object embeddedObject = this.getEmbeddedObject();
                    if (embeddedObject instanceof Number) {
                        return ((Number)embeddedObject).longValue();
                    }
                    break;
                }
            }
        }
        return n;
    }
    
    @Override
    public String getValueAsString(final String s) throws IOException, JsonParseException {
        if (this._currToken != JsonToken.VALUE_STRING && (this._currToken == null || this._currToken == JsonToken.VALUE_NULL || !this._currToken.isScalarValue())) {
            return s;
        }
        return this.getText();
    }
    
    @Override
    public boolean hasCurrentToken() {
        return this._currToken != null;
    }
    
    @Override
    public abstract JsonToken nextToken() throws IOException, JsonParseException;
    
    @Override
    public JsonToken nextValue() throws IOException, JsonParseException {
        JsonToken jsonToken;
        if ((jsonToken = this.nextToken()) == JsonToken.FIELD_NAME) {
            jsonToken = this.nextToken();
        }
        return jsonToken;
    }
    
    @Override
    public JsonParser skipChildren() throws IOException, JsonParseException {
        if (this._currToken != JsonToken.START_OBJECT && this._currToken != JsonToken.START_ARRAY) {
            return this;
        }
        int n = 1;
        while (true) {
            final JsonToken nextToken = this.nextToken();
            if (nextToken == null) {
                this._handleEOF();
                return this;
            }
            switch (nextToken) {
                default: {
                    continue;
                }
                case START_OBJECT:
                case START_ARRAY: {
                    ++n;
                    continue;
                }
                case END_OBJECT:
                case END_ARRAY: {
                    if (--n == 0) {
                        return this;
                    }
                    continue;
                }
            }
        }
    }
    
    @Override
    public Version version() {
        return VersionUtil.versionFor(this.getClass());
    }
}
