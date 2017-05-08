// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.base;

import com.fasterxml.jackson.core.json.PackageVersion;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.io.NumberInput;
import com.fasterxml.jackson.core.JsonParseException;
import java.io.IOException;
import com.fasterxml.jackson.core.util.TextBuffer;
import com.fasterxml.jackson.core.json.JsonReadContext;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import java.math.BigInteger;
import java.math.BigDecimal;

public abstract class ParserBase extends ParserMinimalBase
{
    static final BigDecimal BD_MAX_INT;
    static final BigDecimal BD_MAX_LONG;
    static final BigDecimal BD_MIN_INT;
    static final BigDecimal BD_MIN_LONG;
    static final BigInteger BI_MAX_INT;
    static final BigInteger BI_MAX_LONG;
    static final BigInteger BI_MIN_INT;
    static final BigInteger BI_MIN_LONG;
    protected byte[] _binaryValue;
    protected ByteArrayBuilder _byteArrayBuilder;
    protected boolean _closed;
    protected long _currInputProcessed;
    protected int _currInputRow;
    protected int _currInputRowStart;
    protected int _expLength;
    protected int _fractLength;
    protected int _inputEnd;
    protected int _inputPtr;
    protected int _intLength;
    protected final IOContext _ioContext;
    protected boolean _nameCopied;
    protected char[] _nameCopyBuffer;
    protected JsonToken _nextToken;
    protected int _numTypesValid;
    protected BigDecimal _numberBigDecimal;
    protected BigInteger _numberBigInt;
    protected double _numberDouble;
    protected int _numberInt;
    protected long _numberLong;
    protected boolean _numberNegative;
    protected JsonReadContext _parsingContext;
    protected final TextBuffer _textBuffer;
    protected int _tokenInputCol;
    protected int _tokenInputRow;
    protected long _tokenInputTotal;
    
    static {
        BI_MIN_INT = BigInteger.valueOf(-2147483648L);
        BI_MAX_INT = BigInteger.valueOf(2147483647L);
        BI_MIN_LONG = BigInteger.valueOf(Long.MIN_VALUE);
        BI_MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);
        BD_MIN_LONG = new BigDecimal(ParserBase.BI_MIN_LONG);
        BD_MAX_LONG = new BigDecimal(ParserBase.BI_MAX_LONG);
        BD_MIN_INT = new BigDecimal(ParserBase.BI_MIN_INT);
        BD_MAX_INT = new BigDecimal(ParserBase.BI_MAX_INT);
    }
    
    protected ParserBase(final IOContext ioContext, final int features) {
        this._inputPtr = 0;
        this._inputEnd = 0;
        this._currInputProcessed = 0L;
        this._currInputRow = 1;
        this._currInputRowStart = 0;
        this._tokenInputTotal = 0L;
        this._tokenInputRow = 1;
        this._tokenInputCol = 0;
        this._nameCopyBuffer = null;
        this._nameCopied = false;
        this._byteArrayBuilder = null;
        this._numTypesValid = 0;
        this._features = features;
        this._ioContext = ioContext;
        this._textBuffer = ioContext.constructTextBuffer();
        this._parsingContext = JsonReadContext.createRootContext();
    }
    
    private void _parseSlowFloatValue(final int n) throws IOException, JsonParseException {
        Label_0024: {
            if (n != 16) {
                break Label_0024;
            }
            try {
                this._numberBigDecimal = this._textBuffer.contentsAsDecimal();
                this._numTypesValid = 16;
                return;
                this._numberDouble = this._textBuffer.contentsAsDouble();
                this._numTypesValid = 8;
            }
            catch (NumberFormatException ex) {
                this._wrapError("Malformed numeric value '" + this._textBuffer.contentsAsString() + "'", ex);
            }
        }
    }
    
    private void _parseSlowIntValue(final int n, final char[] array, final int n2, final int n3) throws IOException, JsonParseException {
        final String contentsAsString = this._textBuffer.contentsAsString();
        try {
            if (NumberInput.inLongRange(array, n2, n3, this._numberNegative)) {
                this._numberLong = Long.parseLong(contentsAsString);
                this._numTypesValid = 2;
                return;
            }
            this._numberBigInt = new BigInteger(contentsAsString);
            this._numTypesValid = 4;
        }
        catch (NumberFormatException ex) {
            this._wrapError("Malformed numeric value '" + contentsAsString + "'", ex);
        }
    }
    
    protected abstract void _closeInput() throws IOException;
    
    protected final int _decodeBase64Escape(final Base64Variant base64Variant, final char c, final int n) throws IOException, JsonParseException {
        if (c != '\\') {
            throw this.reportInvalidBase64Char(base64Variant, c, n);
        }
        final char decodeEscaped = this._decodeEscaped();
        int decodeBase64Char;
        if (decodeEscaped <= ' ' && n == 0) {
            decodeBase64Char = -1;
        }
        else if ((decodeBase64Char = base64Variant.decodeBase64Char(decodeEscaped)) < 0) {
            throw this.reportInvalidBase64Char(base64Variant, decodeEscaped, n);
        }
        return decodeBase64Char;
    }
    
    protected final int _decodeBase64Escape(final Base64Variant base64Variant, int decodeBase64Char, final int n) throws IOException, JsonParseException {
        if (decodeBase64Char != 92) {
            throw this.reportInvalidBase64Char(base64Variant, decodeBase64Char, n);
        }
        final char decodeEscaped = this._decodeEscaped();
        if (decodeEscaped <= ' ' && n == 0) {
            decodeBase64Char = -1;
        }
        else if ((decodeBase64Char = base64Variant.decodeBase64Char((int)decodeEscaped)) < 0) {
            throw this.reportInvalidBase64Char(base64Variant, decodeEscaped, n);
        }
        return decodeBase64Char;
    }
    
    protected char _decodeEscaped() throws IOException, JsonParseException {
        throw new UnsupportedOperationException();
    }
    
    protected abstract void _finishString() throws IOException, JsonParseException;
    
    public ByteArrayBuilder _getByteArrayBuilder() {
        if (this._byteArrayBuilder == null) {
            this._byteArrayBuilder = new ByteArrayBuilder();
        }
        else {
            this._byteArrayBuilder.reset();
        }
        return this._byteArrayBuilder;
    }
    
    @Override
    protected void _handleEOF() throws JsonParseException {
        if (!this._parsingContext.inRoot()) {
            this._reportInvalidEOF(": expected close marker for " + this._parsingContext.getTypeDesc() + " (from " + this._parsingContext.getStartLocation(this._ioContext.getSourceReference()) + ")");
        }
    }
    
    protected void _parseNumericValue(int int1) throws IOException, JsonParseException {
        if (this._currToken == JsonToken.VALUE_NUMBER_INT) {
            final char[] textBuffer = this._textBuffer.getTextBuffer();
            final int textOffset = this._textBuffer.getTextOffset();
            final int intLength = this._intLength;
            int n = textOffset;
            if (this._numberNegative) {
                n = textOffset + 1;
            }
            if (intLength <= 9) {
                final int n2 = int1 = NumberInput.parseInt(textBuffer, n, intLength);
                if (this._numberNegative) {
                    int1 = -n2;
                }
                this._numberInt = int1;
                this._numTypesValid = 1;
                return;
            }
            if (intLength <= 18) {
                long long1;
                final long n3 = long1 = NumberInput.parseLong(textBuffer, n, intLength);
                if (this._numberNegative) {
                    long1 = -n3;
                }
                if (intLength == 10) {
                    if (this._numberNegative) {
                        if (long1 >= -2147483648L) {
                            this._numberInt = (int)long1;
                            this._numTypesValid = 1;
                            return;
                        }
                    }
                    else if (long1 <= 2147483647L) {
                        this._numberInt = (int)long1;
                        this._numTypesValid = 1;
                        return;
                    }
                }
                this._numberLong = long1;
                this._numTypesValid = 2;
                return;
            }
            this._parseSlowIntValue(int1, textBuffer, n, intLength);
        }
        else {
            if (this._currToken == JsonToken.VALUE_NUMBER_FLOAT) {
                this._parseSlowFloatValue(int1);
                return;
            }
            this._reportError("Current token (" + this._currToken + ") not numeric, can not use numeric value accessors");
        }
    }
    
    protected void _releaseBuffers() throws IOException {
        this._textBuffer.releaseBuffers();
        final char[] nameCopyBuffer = this._nameCopyBuffer;
        if (nameCopyBuffer != null) {
            this._nameCopyBuffer = null;
            this._ioContext.releaseNameCopyBuffer(nameCopyBuffer);
        }
    }
    
    protected void _reportMismatchedEndMarker(final int n, final char c) throws JsonParseException {
        this._reportError("Unexpected close marker '" + (char)n + "': expected '" + c + "' (for " + this._parsingContext.getTypeDesc() + " starting at " + ("" + this._parsingContext.getStartLocation(this._ioContext.getSourceReference())) + ")");
    }
    
    @Override
    public void close() throws IOException {
        if (this._closed) {
            return;
        }
        this._closed = true;
        try {
            this._closeInput();
        }
        finally {
            this._releaseBuffers();
        }
    }
    
    protected void convertNumberToBigDecimal() throws IOException, JsonParseException {
        if ((this._numTypesValid & 0x8) != 0x0) {
            this._numberBigDecimal = new BigDecimal(this.getText());
        }
        else if ((this._numTypesValid & 0x4) != 0x0) {
            this._numberBigDecimal = new BigDecimal(this._numberBigInt);
        }
        else if ((this._numTypesValid & 0x2) != 0x0) {
            this._numberBigDecimal = BigDecimal.valueOf(this._numberLong);
        }
        else if ((this._numTypesValid & 0x1) != 0x0) {
            this._numberBigDecimal = BigDecimal.valueOf(this._numberInt);
        }
        else {
            this._throwInternal();
        }
        this._numTypesValid |= 0x10;
    }
    
    protected void convertNumberToBigInteger() throws IOException, JsonParseException {
        if ((this._numTypesValid & 0x10) != 0x0) {
            this._numberBigInt = this._numberBigDecimal.toBigInteger();
        }
        else if ((this._numTypesValid & 0x2) != 0x0) {
            this._numberBigInt = BigInteger.valueOf(this._numberLong);
        }
        else if ((this._numTypesValid & 0x1) != 0x0) {
            this._numberBigInt = BigInteger.valueOf(this._numberInt);
        }
        else if ((this._numTypesValid & 0x8) != 0x0) {
            this._numberBigInt = BigDecimal.valueOf(this._numberDouble).toBigInteger();
        }
        else {
            this._throwInternal();
        }
        this._numTypesValid |= 0x4;
    }
    
    protected void convertNumberToDouble() throws IOException, JsonParseException {
        if ((this._numTypesValid & 0x10) != 0x0) {
            this._numberDouble = this._numberBigDecimal.doubleValue();
        }
        else if ((this._numTypesValid & 0x4) != 0x0) {
            this._numberDouble = this._numberBigInt.doubleValue();
        }
        else if ((this._numTypesValid & 0x2) != 0x0) {
            this._numberDouble = this._numberLong;
        }
        else if ((this._numTypesValid & 0x1) != 0x0) {
            this._numberDouble = this._numberInt;
        }
        else {
            this._throwInternal();
        }
        this._numTypesValid |= 0x8;
    }
    
    protected void convertNumberToInt() throws IOException, JsonParseException {
        if ((this._numTypesValid & 0x2) != 0x0) {
            final int numberInt = (int)this._numberLong;
            if (numberInt != this._numberLong) {
                this._reportError("Numeric value (" + this.getText() + ") out of range of int");
            }
            this._numberInt = numberInt;
        }
        else if ((this._numTypesValid & 0x4) != 0x0) {
            if (ParserBase.BI_MIN_INT.compareTo(this._numberBigInt) > 0 || ParserBase.BI_MAX_INT.compareTo(this._numberBigInt) < 0) {
                this.reportOverflowInt();
            }
            this._numberInt = this._numberBigInt.intValue();
        }
        else if ((this._numTypesValid & 0x8) != 0x0) {
            if (this._numberDouble < -2.147483648E9 || this._numberDouble > 2.147483647E9) {
                this.reportOverflowInt();
            }
            this._numberInt = (int)this._numberDouble;
        }
        else if ((this._numTypesValid & 0x10) != 0x0) {
            if (ParserBase.BD_MIN_INT.compareTo(this._numberBigDecimal) > 0 || ParserBase.BD_MAX_INT.compareTo(this._numberBigDecimal) < 0) {
                this.reportOverflowInt();
            }
            this._numberInt = this._numberBigDecimal.intValue();
        }
        else {
            this._throwInternal();
        }
        this._numTypesValid |= 0x1;
    }
    
    protected void convertNumberToLong() throws IOException, JsonParseException {
        if ((this._numTypesValid & 0x1) != 0x0) {
            this._numberLong = this._numberInt;
        }
        else if ((this._numTypesValid & 0x4) != 0x0) {
            if (ParserBase.BI_MIN_LONG.compareTo(this._numberBigInt) > 0 || ParserBase.BI_MAX_LONG.compareTo(this._numberBigInt) < 0) {
                this.reportOverflowLong();
            }
            this._numberLong = this._numberBigInt.longValue();
        }
        else if ((this._numTypesValid & 0x8) != 0x0) {
            if (this._numberDouble < -9.223372036854776E18 || this._numberDouble > 9.223372036854776E18) {
                this.reportOverflowLong();
            }
            this._numberLong = (long)this._numberDouble;
        }
        else if ((this._numTypesValid & 0x10) != 0x0) {
            if (ParserBase.BD_MIN_LONG.compareTo(this._numberBigDecimal) > 0 || ParserBase.BD_MAX_LONG.compareTo(this._numberBigDecimal) < 0) {
                this.reportOverflowLong();
            }
            this._numberLong = this._numberBigDecimal.longValue();
        }
        else {
            this._throwInternal();
        }
        this._numTypesValid |= 0x2;
    }
    
    @Override
    public BigInteger getBigIntegerValue() throws IOException, JsonParseException {
        if ((this._numTypesValid & 0x4) == 0x0) {
            if (this._numTypesValid == 0) {
                this._parseNumericValue(4);
            }
            if ((this._numTypesValid & 0x4) == 0x0) {
                this.convertNumberToBigInteger();
            }
        }
        return this._numberBigInt;
    }
    
    @Override
    public JsonLocation getCurrentLocation() {
        return new JsonLocation(this._ioContext.getSourceReference(), this._currInputProcessed + this._inputPtr - 1L, this._currInputRow, this._inputPtr - this._currInputRowStart + 1);
    }
    
    @Override
    public String getCurrentName() throws IOException, JsonParseException {
        if (this._currToken == JsonToken.START_OBJECT || this._currToken == JsonToken.START_ARRAY) {
            return this._parsingContext.getParent().getCurrentName();
        }
        return this._parsingContext.getCurrentName();
    }
    
    @Override
    public BigDecimal getDecimalValue() throws IOException, JsonParseException {
        if ((this._numTypesValid & 0x10) == 0x0) {
            if (this._numTypesValid == 0) {
                this._parseNumericValue(16);
            }
            if ((this._numTypesValid & 0x10) == 0x0) {
                this.convertNumberToBigDecimal();
            }
        }
        return this._numberBigDecimal;
    }
    
    @Override
    public double getDoubleValue() throws IOException, JsonParseException {
        if ((this._numTypesValid & 0x8) == 0x0) {
            if (this._numTypesValid == 0) {
                this._parseNumericValue(8);
            }
            if ((this._numTypesValid & 0x8) == 0x0) {
                this.convertNumberToDouble();
            }
        }
        return this._numberDouble;
    }
    
    @Override
    public Object getEmbeddedObject() throws IOException, JsonParseException {
        return null;
    }
    
    @Override
    public float getFloatValue() throws IOException, JsonParseException {
        return (float)this.getDoubleValue();
    }
    
    @Override
    public int getIntValue() throws IOException, JsonParseException {
        if ((this._numTypesValid & 0x1) == 0x0) {
            if (this._numTypesValid == 0) {
                this._parseNumericValue(1);
            }
            if ((this._numTypesValid & 0x1) == 0x0) {
                this.convertNumberToInt();
            }
        }
        return this._numberInt;
    }
    
    @Override
    public long getLongValue() throws IOException, JsonParseException {
        if ((this._numTypesValid & 0x2) == 0x0) {
            if (this._numTypesValid == 0) {
                this._parseNumericValue(2);
            }
            if ((this._numTypesValid & 0x2) == 0x0) {
                this.convertNumberToLong();
            }
        }
        return this._numberLong;
    }
    
    @Override
    public NumberType getNumberType() throws IOException, JsonParseException {
        if (this._numTypesValid == 0) {
            this._parseNumericValue(0);
        }
        if (this._currToken == JsonToken.VALUE_NUMBER_INT) {
            if ((this._numTypesValid & 0x1) != 0x0) {
                return NumberType.INT;
            }
            if ((this._numTypesValid & 0x2) != 0x0) {
                return NumberType.LONG;
            }
            return NumberType.BIG_INTEGER;
        }
        else {
            if ((this._numTypesValid & 0x10) != 0x0) {
                return NumberType.BIG_DECIMAL;
            }
            return NumberType.DOUBLE;
        }
    }
    
    @Override
    public Number getNumberValue() throws IOException, JsonParseException {
        if (this._numTypesValid == 0) {
            this._parseNumericValue(0);
        }
        if (this._currToken == JsonToken.VALUE_NUMBER_INT) {
            if ((this._numTypesValid & 0x1) != 0x0) {
                return this._numberInt;
            }
            if ((this._numTypesValid & 0x2) != 0x0) {
                return this._numberLong;
            }
            if ((this._numTypesValid & 0x4) != 0x0) {
                return this._numberBigInt;
            }
            return this._numberBigDecimal;
        }
        else {
            if ((this._numTypesValid & 0x10) != 0x0) {
                return this._numberBigDecimal;
            }
            if ((this._numTypesValid & 0x8) == 0x0) {
                this._throwInternal();
            }
            return this._numberDouble;
        }
    }
    
    public long getTokenCharacterOffset() {
        return this._tokenInputTotal;
    }
    
    public int getTokenColumnNr() {
        final int tokenInputCol = this._tokenInputCol;
        if (tokenInputCol < 0) {
            return tokenInputCol;
        }
        return tokenInputCol + 1;
    }
    
    public int getTokenLineNr() {
        return this._tokenInputRow;
    }
    
    @Override
    public JsonLocation getTokenLocation() {
        return new JsonLocation(this._ioContext.getSourceReference(), this.getTokenCharacterOffset(), this.getTokenLineNr(), this.getTokenColumnNr());
    }
    
    @Override
    public boolean hasTextCharacters() {
        return this._currToken == JsonToken.VALUE_STRING || (this._currToken == JsonToken.FIELD_NAME && this._nameCopied);
    }
    
    protected abstract boolean loadMore() throws IOException;
    
    protected final void loadMoreGuaranteed() throws IOException {
        if (!this.loadMore()) {
            this._reportInvalidEOF();
        }
    }
    
    protected IllegalArgumentException reportInvalidBase64Char(final Base64Variant base64Variant, final int n, final int n2) throws IllegalArgumentException {
        return this.reportInvalidBase64Char(base64Variant, n, n2, null);
    }
    
    protected IllegalArgumentException reportInvalidBase64Char(final Base64Variant base64Variant, final int n, final int n2, final String s) throws IllegalArgumentException {
        String s2;
        if (n <= 32) {
            s2 = "Illegal white space character (code 0x" + Integer.toHexString(n) + ") as character #" + (n2 + 1) + " of 4-char base64 unit: can only used between units";
        }
        else if (base64Variant.usesPaddingChar(n)) {
            s2 = "Unexpected padding character ('" + base64Variant.getPaddingChar() + "') as character #" + (n2 + 1) + " of 4-char base64 unit: padding only legal as 3rd or 4th character";
        }
        else if (!Character.isDefined(n) || Character.isISOControl(n)) {
            s2 = "Illegal character (code 0x" + Integer.toHexString(n) + ") in base64 content";
        }
        else {
            s2 = "Illegal character '" + (char)n + "' (code 0x" + Integer.toHexString(n) + ") in base64 content";
        }
        String string = s2;
        if (s != null) {
            string = s2 + ": " + s;
        }
        return new IllegalArgumentException(string);
    }
    
    protected void reportInvalidNumber(final String s) throws JsonParseException {
        this._reportError("Invalid numeric value: " + s);
    }
    
    protected void reportOverflowInt() throws IOException, JsonParseException {
        this._reportError("Numeric value (" + this.getText() + ") out of range of int (" + Integer.MIN_VALUE + " - " + Integer.MAX_VALUE + ")");
    }
    
    protected void reportOverflowLong() throws IOException, JsonParseException {
        this._reportError("Numeric value (" + this.getText() + ") out of range of long (" + Long.MIN_VALUE + " - " + Long.MAX_VALUE + ")");
    }
    
    protected void reportUnexpectedNumberChar(final int n, final String s) throws JsonParseException {
        String s2 = "Unexpected character (" + ParserMinimalBase._getCharDesc(n) + ") in numeric value";
        if (s != null) {
            s2 = s2 + ": " + s;
        }
        this._reportError(s2);
    }
    
    protected final JsonToken reset(final boolean b, final int n, final int n2, final int n3) {
        if (n2 < 1 && n3 < 1) {
            return this.resetInt(b, n);
        }
        return this.resetFloat(b, n, n2, n3);
    }
    
    protected final JsonToken resetAsNaN(final String s, final double numberDouble) {
        this._textBuffer.resetWithString(s);
        this._numberDouble = numberDouble;
        this._numTypesValid = 8;
        return JsonToken.VALUE_NUMBER_FLOAT;
    }
    
    protected final JsonToken resetFloat(final boolean numberNegative, final int intLength, final int fractLength, final int expLength) {
        this._numberNegative = numberNegative;
        this._intLength = intLength;
        this._fractLength = fractLength;
        this._expLength = expLength;
        this._numTypesValid = 0;
        return JsonToken.VALUE_NUMBER_FLOAT;
    }
    
    protected final JsonToken resetInt(final boolean numberNegative, final int intLength) {
        this._numberNegative = numberNegative;
        this._intLength = intLength;
        this._fractLength = 0;
        this._expLength = 0;
        this._numTypesValid = 0;
        return JsonToken.VALUE_NUMBER_INT;
    }
    
    @Override
    public Version version() {
        return PackageVersion.VERSION;
    }
}
