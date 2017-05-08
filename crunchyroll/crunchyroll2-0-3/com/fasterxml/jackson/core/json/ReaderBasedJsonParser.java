// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.base.ParserMinimalBase;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParseException;
import java.io.IOException;
import com.fasterxml.jackson.core.util.TextBuffer;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.sym.CharsToNameCanonicalizer;
import java.io.Reader;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.base.ParserBase;

public final class ReaderBasedJsonParser extends ParserBase
{
    protected final int _hashSeed;
    protected char[] _inputBuffer;
    protected ObjectCodec _objectCodec;
    protected Reader _reader;
    protected final CharsToNameCanonicalizer _symbols;
    protected boolean _tokenIncomplete;
    
    public ReaderBasedJsonParser(final IOContext ioContext, final int n, final Reader reader, final ObjectCodec objectCodec, final CharsToNameCanonicalizer symbols) {
        super(ioContext, n);
        this._tokenIncomplete = false;
        this._reader = reader;
        this._inputBuffer = ioContext.allocTokenBuffer();
        this._objectCodec = objectCodec;
        this._symbols = symbols;
        this._hashSeed = symbols.hashSeed();
    }
    
    private JsonToken _nextAfterName() {
        this._nameCopied = false;
        final JsonToken nextToken = this._nextToken;
        this._nextToken = null;
        if (nextToken == JsonToken.START_ARRAY) {
            this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
        }
        else if (nextToken == JsonToken.START_OBJECT) {
            this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
        }
        return this._currToken = nextToken;
    }
    
    private String _parseFieldName2(int currentLength, int n, int size) throws IOException, JsonParseException {
        this._textBuffer.resetWithShared(this._inputBuffer, currentLength, this._inputPtr - currentLength);
        char[] array = this._textBuffer.getCurrentSegment();
        currentLength = this._textBuffer.getCurrentSegmentSize();
    Label_0129_Outer:
        while (true) {
            if (this._inputPtr >= this._inputEnd && !this.loadMore()) {
                this._reportInvalidEOF(": was expecting closing '" + (char)size + "' for name");
            }
            final char c = this._inputBuffer[this._inputPtr++];
            while (true) {
                Label_0243: {
                    if (c > '\\') {
                        break Label_0243;
                    }
                    final char decodeEscaped;
                    if (c == '\\') {
                        decodeEscaped = this._decodeEscaped();
                    }
                    else {
                        if (c > size) {
                            break Label_0243;
                        }
                        if (c == size) {
                            break;
                        }
                        if (c < ' ') {
                            this._throwUnquotedSpace(c, "name");
                        }
                        break Label_0243;
                    }
                    n = n * 33 + c;
                    final int n2 = currentLength + 1;
                    array[currentLength] = decodeEscaped;
                    if (n2 >= array.length) {
                        array = this._textBuffer.finishCurrentSegment();
                        currentLength = 0;
                        continue Label_0129_Outer;
                    }
                    currentLength = n2;
                    continue Label_0129_Outer;
                }
                char decodeEscaped = c;
                continue;
            }
        }
        this._textBuffer.setCurrentLength(currentLength);
        final TextBuffer textBuffer = this._textBuffer;
        final char[] textBuffer2 = textBuffer.getTextBuffer();
        currentLength = textBuffer.getTextOffset();
        size = textBuffer.size();
        return this._symbols.findSymbol(textBuffer2, currentLength, size, n);
    }
    
    private String _parseUnusualFieldName2(int currentLength, int n, final int[] array) throws IOException, JsonParseException {
        this._textBuffer.resetWithShared(this._inputBuffer, currentLength, this._inputPtr - currentLength);
        char[] array2 = this._textBuffer.getCurrentSegment();
        currentLength = this._textBuffer.getCurrentSegmentSize();
        final int length = array.length;
        while (this._inputPtr < this._inputEnd || this.loadMore()) {
            final char c = this._inputBuffer[this._inputPtr];
            if (c <= length) {
                if (array[c] != 0) {
                    break;
                }
            }
            else if (!Character.isJavaIdentifierPart(c)) {
                break;
            }
            ++this._inputPtr;
            n = n * 33 + c;
            final int n2 = currentLength + 1;
            array2[currentLength] = c;
            if (n2 >= array2.length) {
                array2 = this._textBuffer.finishCurrentSegment();
                currentLength = 0;
            }
            else {
                currentLength = n2;
            }
        }
        this._textBuffer.setCurrentLength(currentLength);
        final TextBuffer textBuffer = this._textBuffer;
        final char[] textBuffer2 = textBuffer.getTextBuffer();
        currentLength = textBuffer.getTextOffset();
        return this._symbols.findSymbol(textBuffer2, currentLength, textBuffer.size(), n);
    }
    
    private void _skipCComment() throws IOException, JsonParseException {
        while (this._inputPtr < this._inputEnd || this.loadMore()) {
            final char c = this._inputBuffer[this._inputPtr++];
            if (c <= '*') {
                if (c == '*') {
                    if (this._inputPtr >= this._inputEnd && !this.loadMore()) {
                        break;
                    }
                    if (this._inputBuffer[this._inputPtr] == '/') {
                        ++this._inputPtr;
                        return;
                    }
                    continue;
                }
                else {
                    if (c >= ' ') {
                        continue;
                    }
                    if (c == '\n') {
                        this._skipLF();
                    }
                    else if (c == '\r') {
                        this._skipCR();
                    }
                    else {
                        if (c == '\t') {
                            continue;
                        }
                        this._throwInvalidSpace(c);
                    }
                }
            }
        }
        this._reportInvalidEOF(" in a comment");
    }
    
    private void _skipComment() throws IOException, JsonParseException {
        if (!this.isEnabled(Feature.ALLOW_COMMENTS)) {
            this._reportUnexpectedChar(47, "maybe a (non-standard) comment? (not recognized as one since Feature 'ALLOW_COMMENTS' not enabled for parser)");
        }
        if (this._inputPtr >= this._inputEnd && !this.loadMore()) {
            this._reportInvalidEOF(" in a comment");
        }
        final char c = this._inputBuffer[this._inputPtr++];
        if (c == '/') {
            this._skipCppComment();
            return;
        }
        if (c == '*') {
            this._skipCComment();
            return;
        }
        this._reportUnexpectedChar(c, "was expecting either '*' or '/' for a comment");
    }
    
    private void _skipCppComment() throws IOException, JsonParseException {
        while (this._inputPtr < this._inputEnd || this.loadMore()) {
            final char c = this._inputBuffer[this._inputPtr++];
            if (c < ' ') {
                if (c == '\n') {
                    this._skipLF();
                    break;
                }
                if (c == '\r') {
                    this._skipCR();
                    return;
                }
                if (c == '\t') {
                    continue;
                }
                this._throwInvalidSpace(c);
            }
        }
    }
    
    private int _skipWS() throws IOException, JsonParseException {
        while (this._inputPtr < this._inputEnd || this.loadMore()) {
            final char c = this._inputBuffer[this._inputPtr++];
            if (c > ' ') {
                if (c != '/') {
                    return c;
                }
                this._skipComment();
            }
            else {
                if (c == ' ') {
                    continue;
                }
                if (c == '\n') {
                    this._skipLF();
                }
                else if (c == '\r') {
                    this._skipCR();
                }
                else {
                    if (c == '\t') {
                        continue;
                    }
                    this._throwInvalidSpace(c);
                }
            }
        }
        throw this._constructError("Unexpected end-of-input within/between " + this._parsingContext.getTypeDesc() + " entries");
    }
    
    private int _skipWSOrEnd() throws IOException, JsonParseException {
        while (this._inputPtr < this._inputEnd || this.loadMore()) {
            final char c = this._inputBuffer[this._inputPtr++];
            if (c > ' ') {
                final int n;
                if ((n = c) != 47) {
                    return n;
                }
                this._skipComment();
            }
            else {
                if (c == ' ') {
                    continue;
                }
                if (c == '\n') {
                    this._skipLF();
                }
                else if (c == '\r') {
                    this._skipCR();
                }
                else {
                    if (c == '\t') {
                        continue;
                    }
                    this._throwInvalidSpace(c);
                }
            }
        }
        this._handleEOF();
        return -1;
    }
    
    private char _verifyNoLeadingZeroes() throws IOException, JsonParseException {
        char c;
        if (this._inputPtr >= this._inputEnd && !this.loadMore()) {
            c = '0';
        }
        else {
            final char c2 = this._inputBuffer[this._inputPtr];
            if (c2 < '0' || c2 > '9') {
                return '0';
            }
            if (!this.isEnabled(Feature.ALLOW_NUMERIC_LEADING_ZEROS)) {
                this.reportInvalidNumber("Leading zeroes not allowed");
            }
            ++this._inputPtr;
            if ((c = c2) == '0') {
                c = c2;
                while (this._inputPtr < this._inputEnd || this.loadMore()) {
                    final char c3 = this._inputBuffer[this._inputPtr];
                    if (c3 < '0' || c3 > '9') {
                        return '0';
                    }
                    ++this._inputPtr;
                    if ((c = c3) != '0') {
                        return c3;
                    }
                }
            }
        }
        return c;
    }
    
    private JsonToken parseNumberText2(final boolean b) throws IOException, JsonParseException {
        int n = 0;
        char[] emptyAndGetCurrentSegment = this._textBuffer.emptyAndGetCurrentSegment();
        int n2;
        if (b) {
            emptyAndGetCurrentSegment[0] = '-';
            n2 = 1;
        }
        else {
            n2 = 0;
        }
        char nextChar;
        if (this._inputPtr < this._inputEnd) {
            nextChar = this._inputBuffer[this._inputPtr++];
        }
        else {
            nextChar = this.getNextChar("No digit following minus sign");
        }
        char verifyNoLeadingZeroes = nextChar;
        if (nextChar == '0') {
            verifyNoLeadingZeroes = this._verifyNoLeadingZeroes();
        }
        int n3 = 0;
        while (true) {
            char[] array;
        Label_0252_Outer:
            for (char c = verifyNoLeadingZeroes; c >= '0' && c <= '9'; c = this._inputBuffer[this._inputPtr++], emptyAndGetCurrentSegment = array) {
                ++n3;
                int n4 = n2;
                array = emptyAndGetCurrentSegment;
                if (n2 >= emptyAndGetCurrentSegment.length) {
                    array = this._textBuffer.finishCurrentSegment();
                    n4 = 0;
                }
                n2 = n4 + 1;
                array[n4] = c;
                if (this._inputPtr >= this._inputEnd && !this.loadMore()) {
                    final boolean b2 = true;
                    c = '\0';
                    final int n5 = n3;
                    final int n6 = n2;
                    int n7 = b2 ? 1 : 0;
                    if (n5 == 0) {
                        this.reportInvalidNumber("Missing integer part (next char " + ParserMinimalBase._getCharDesc(c) + ")");
                    }
                    int n11 = 0;
                    int n13 = 0;
                    char[] array2 = null;
                    int n14 = 0;
                    Label_0285: {
                        if (c == '.') {
                            array[n6] = c;
                            int n8 = n6 + 1;
                            int n9 = 0;
                            while (true) {
                                while (this._inputPtr < this._inputEnd || this.loadMore()) {
                                    c = this._inputBuffer[this._inputPtr++];
                                    if (c >= '0') {
                                        if (c <= '9') {
                                            ++n9;
                                            if (n8 >= array.length) {
                                                array = this._textBuffer.finishCurrentSegment();
                                                n8 = 0;
                                            }
                                            final int n10 = n8 + 1;
                                            array[n8] = c;
                                            n8 = n10;
                                            continue Label_0252_Outer;
                                        }
                                    }
                                    if (n9 == 0) {
                                        this.reportUnexpectedNumberChar(c, "Decimal point not followed by a digit");
                                    }
                                    n11 = n9;
                                    final int n12 = n8;
                                    n13 = n7;
                                    array2 = array;
                                    n14 = n12;
                                    break Label_0285;
                                }
                                n7 = 1;
                                continue;
                            }
                        }
                        n11 = 0;
                        final int n15 = n7;
                        n14 = n6;
                        array2 = array;
                        n13 = n15;
                    }
                    int n22 = 0;
                    int currentLength = 0;
                    Label_0582: {
                        if (c == 'e' || c == 'E') {
                            int n16 = n14;
                            char[] array3 = array2;
                            if (n14 >= array2.length) {
                                array3 = this._textBuffer.finishCurrentSegment();
                                n16 = 0;
                            }
                            int n17 = n16 + 1;
                            array3[n16] = c;
                            char c2;
                            if (this._inputPtr < this._inputEnd) {
                                c2 = this._inputBuffer[this._inputPtr++];
                            }
                            else {
                                c2 = this.getNextChar("expected a digit for number exponent");
                            }
                            int n18;
                            if (c2 == '-' || c2 == '+') {
                                if (n17 >= array3.length) {
                                    array3 = this._textBuffer.finishCurrentSegment();
                                    n17 = 0;
                                }
                                array3[n17] = c2;
                                if (this._inputPtr < this._inputEnd) {
                                    c2 = this._inputBuffer[this._inputPtr++];
                                }
                                else {
                                    c2 = this.getNextChar("expected a digit for number exponent");
                                }
                                ++n17;
                                n18 = 0;
                            }
                            else {
                                n18 = 0;
                            }
                            while (true) {
                                while (c2 <= '9' && c2 >= '0') {
                                    ++n18;
                                    int n19 = n17;
                                    char[] finishCurrentSegment = array3;
                                    if (n17 >= array3.length) {
                                        finishCurrentSegment = this._textBuffer.finishCurrentSegment();
                                        n19 = 0;
                                    }
                                    n17 = n19 + 1;
                                    finishCurrentSegment[n19] = c2;
                                    if (this._inputPtr >= this._inputEnd && !this.loadMore()) {
                                        final boolean b3 = true;
                                        final int n20 = n17;
                                        final int n21 = b3 ? 1 : 0;
                                        n22 = n21;
                                        currentLength = n20;
                                        n = n18;
                                        if (n18 == 0) {
                                            this.reportUnexpectedNumberChar(c2, "Exponent indicator not followed by a digit");
                                            n = n18;
                                            currentLength = n20;
                                            n22 = n21;
                                        }
                                        break Label_0582;
                                    }
                                    else {
                                        c2 = this._inputBuffer[this._inputPtr++];
                                        array3 = finishCurrentSegment;
                                    }
                                }
                                final int n23 = n17;
                                final int n21 = n13;
                                final int n20 = n23;
                                continue;
                            }
                        }
                        n22 = n13;
                        currentLength = n14;
                    }
                    if (n22 == 0) {
                        --this._inputPtr;
                    }
                    this._textBuffer.setCurrentLength(currentLength);
                    return this.reset(b, n5, n11, n);
                }
            }
            final boolean b4 = false;
            final int n5 = n3;
            array = emptyAndGetCurrentSegment;
            final int n6 = n2;
            int n7 = b4 ? 1 : 0;
            continue;
        }
    }
    
    @Override
    protected void _closeInput() throws IOException {
        if (this._reader != null) {
            if (this._ioContext.isResourceManaged() || this.isEnabled(Feature.AUTO_CLOSE_SOURCE)) {
                this._reader.close();
            }
            this._reader = null;
        }
    }
    
    protected byte[] _decodeBase64(final Base64Variant base64Variant) throws IOException, JsonParseException {
        final ByteArrayBuilder getByteArrayBuilder = this._getByteArrayBuilder();
        while (true) {
            if (this._inputPtr >= this._inputEnd) {
                this.loadMoreGuaranteed();
            }
            final char c = this._inputBuffer[this._inputPtr++];
            if (c > ' ') {
                int n;
                if ((n = base64Variant.decodeBase64Char(c)) < 0) {
                    if (c == '\"') {
                        return getByteArrayBuilder.toByteArray();
                    }
                    n = this._decodeBase64Escape(base64Variant, c, 0);
                    if (n < 0) {
                        continue;
                    }
                }
                if (this._inputPtr >= this._inputEnd) {
                    this.loadMoreGuaranteed();
                }
                final char c2 = this._inputBuffer[this._inputPtr++];
                int n2;
                if ((n2 = base64Variant.decodeBase64Char(c2)) < 0) {
                    n2 = this._decodeBase64Escape(base64Variant, c2, 1);
                }
                final int n3 = n2 | n << 6;
                if (this._inputPtr >= this._inputEnd) {
                    this.loadMoreGuaranteed();
                }
                final char c3 = this._inputBuffer[this._inputPtr++];
                final int decodeBase64Char = base64Variant.decodeBase64Char(c3);
                int n4;
                if ((n4 = decodeBase64Char) < 0) {
                    int decodeBase64Escape;
                    if ((decodeBase64Escape = decodeBase64Char) != -2) {
                        if (c3 == '\"' && !base64Variant.usesPadding()) {
                            getByteArrayBuilder.append(n3 >> 4);
                            return getByteArrayBuilder.toByteArray();
                        }
                        decodeBase64Escape = this._decodeBase64Escape(base64Variant, c3, 2);
                    }
                    if ((n4 = decodeBase64Escape) == -2) {
                        if (this._inputPtr >= this._inputEnd) {
                            this.loadMoreGuaranteed();
                        }
                        final char c4 = this._inputBuffer[this._inputPtr++];
                        if (!base64Variant.usesPaddingChar(c4)) {
                            throw this.reportInvalidBase64Char(base64Variant, c4, 3, "expected padding character '" + base64Variant.getPaddingChar() + "'");
                        }
                        getByteArrayBuilder.append(n3 >> 4);
                        continue;
                    }
                }
                final int n5 = n3 << 6 | n4;
                if (this._inputPtr >= this._inputEnd) {
                    this.loadMoreGuaranteed();
                }
                final char c5 = this._inputBuffer[this._inputPtr++];
                final int decodeBase64Char2 = base64Variant.decodeBase64Char(c5);
                int n6;
                if ((n6 = decodeBase64Char2) < 0) {
                    int decodeBase64Escape2;
                    if ((decodeBase64Escape2 = decodeBase64Char2) != -2) {
                        if (c5 == '\"' && !base64Variant.usesPadding()) {
                            getByteArrayBuilder.appendTwoBytes(n5 >> 2);
                            return getByteArrayBuilder.toByteArray();
                        }
                        decodeBase64Escape2 = this._decodeBase64Escape(base64Variant, c5, 3);
                    }
                    if ((n6 = decodeBase64Escape2) == -2) {
                        getByteArrayBuilder.appendTwoBytes(n5 >> 2);
                        continue;
                    }
                }
                getByteArrayBuilder.appendThreeBytes(n6 | n5 << 6);
            }
        }
    }
    
    @Override
    protected char _decodeEscaped() throws IOException, JsonParseException {
        int n = 0;
        if (this._inputPtr >= this._inputEnd && !this.loadMore()) {
            this._reportInvalidEOF(" in character escape sequence");
        }
        char handleUnrecognizedCharacterEscape;
        final char c = handleUnrecognizedCharacterEscape = this._inputBuffer[this._inputPtr++];
        switch (c) {
            default: {
                handleUnrecognizedCharacterEscape = this._handleUnrecognizedCharacterEscape(c);
                return handleUnrecognizedCharacterEscape;
            }
            case 34:
            case 47:
            case 92: {
                return handleUnrecognizedCharacterEscape;
            }
            case 98: {
                return '\b';
            }
            case 116: {
                return '\t';
            }
            case 110: {
                return '\n';
            }
            case 102: {
                return '\f';
            }
            case 114: {
                return '\r';
            }
            case 117: {
                for (int i = 0; i < 4; ++i) {
                    if (this._inputPtr >= this._inputEnd && !this.loadMore()) {
                        this._reportInvalidEOF(" in character escape sequence");
                    }
                    final char c2 = this._inputBuffer[this._inputPtr++];
                    final int charToHex = CharTypes.charToHex(c2);
                    if (charToHex < 0) {
                        this._reportUnexpectedChar(c2, "expected a hex-digit for character escape sequence");
                    }
                    n = (n << 4 | charToHex);
                }
                return (char)n;
            }
        }
    }
    
    @Override
    protected void _finishString() throws IOException, JsonParseException {
        int inputPtr = this._inputPtr;
        final int inputEnd = this._inputEnd;
        int inputPtr2 = inputPtr;
        if (inputPtr < inputEnd) {
            final int[] inputCodeLatin1 = CharTypes.getInputCodeLatin1();
            final int length = inputCodeLatin1.length;
            do {
                final char c = this._inputBuffer[inputPtr];
                if (c < length && inputCodeLatin1[c] != 0) {
                    inputPtr2 = inputPtr;
                    if (c == '\"') {
                        this._textBuffer.resetWithShared(this._inputBuffer, this._inputPtr, inputPtr - this._inputPtr);
                        this._inputPtr = inputPtr + 1;
                        return;
                    }
                    break;
                }
                else {
                    inputPtr2 = inputPtr + 1;
                }
            } while ((inputPtr = inputPtr2) < inputEnd);
        }
        this._textBuffer.resetWithCopy(this._inputBuffer, this._inputPtr, inputPtr2 - this._inputPtr);
        this._inputPtr = inputPtr2;
        this._finishString2();
    }
    
    protected void _finishString2() throws IOException, JsonParseException {
        char[] array = this._textBuffer.getCurrentSegment();
        int currentSegmentSize = this._textBuffer.getCurrentSegmentSize();
        while (true) {
            if (this._inputPtr >= this._inputEnd && !this.loadMore()) {
                this._reportInvalidEOF(": was expecting closing quote for a string value");
            }
            final char c = this._inputBuffer[this._inputPtr++];
            char decodeEscaped;
            if ((decodeEscaped = c) <= '\\') {
                if (c == '\\') {
                    decodeEscaped = this._decodeEscaped();
                }
                else if ((decodeEscaped = c) <= '\"') {
                    if (c == '\"') {
                        break;
                    }
                    if ((decodeEscaped = c) < ' ') {
                        this._throwUnquotedSpace(c, "string value");
                        decodeEscaped = c;
                    }
                }
            }
            if (currentSegmentSize >= array.length) {
                array = this._textBuffer.finishCurrentSegment();
                currentSegmentSize = 0;
            }
            final int n = currentSegmentSize + 1;
            array[currentSegmentSize] = decodeEscaped;
            currentSegmentSize = n;
        }
        this._textBuffer.setCurrentLength(currentSegmentSize);
    }
    
    protected String _getText2(final JsonToken jsonToken) {
        if (jsonToken == null) {
            return null;
        }
        switch (jsonToken) {
            default: {
                return jsonToken.asString();
            }
            case FIELD_NAME: {
                return this._parsingContext.getCurrentName();
            }
            case VALUE_STRING:
            case VALUE_NUMBER_INT:
            case VALUE_NUMBER_FLOAT: {
                return this._textBuffer.contentsAsString();
            }
        }
    }
    
    protected JsonToken _handleApostropheValue() throws IOException, JsonParseException {
        char[] array = this._textBuffer.emptyAndGetCurrentSegment();
        int currentSegmentSize = this._textBuffer.getCurrentSegmentSize();
        while (true) {
            if (this._inputPtr >= this._inputEnd && !this.loadMore()) {
                this._reportInvalidEOF(": was expecting closing quote for a string value");
            }
            final char c = this._inputBuffer[this._inputPtr++];
            char decodeEscaped;
            if ((decodeEscaped = c) <= '\\') {
                if (c == '\\') {
                    decodeEscaped = this._decodeEscaped();
                }
                else if ((decodeEscaped = c) <= '\'') {
                    if (c == '\'') {
                        break;
                    }
                    if ((decodeEscaped = c) < ' ') {
                        this._throwUnquotedSpace(c, "string value");
                        decodeEscaped = c;
                    }
                }
            }
            if (currentSegmentSize >= array.length) {
                array = this._textBuffer.finishCurrentSegment();
                currentSegmentSize = 0;
            }
            final int n = currentSegmentSize + 1;
            array[currentSegmentSize] = decodeEscaped;
            currentSegmentSize = n;
        }
        this._textBuffer.setCurrentLength(currentSegmentSize);
        return JsonToken.VALUE_STRING;
    }
    
    protected JsonToken _handleInvalidNumberStart(int n, final boolean b) throws IOException, JsonParseException {
        double n2 = Double.NEGATIVE_INFINITY;
        int n3 = n;
        if (n == 73) {
            if (this._inputPtr >= this._inputEnd && !this.loadMore()) {
                this._reportInvalidEOFInValue();
            }
            final char[] inputBuffer = this._inputBuffer;
            n = this._inputPtr++;
            n = inputBuffer[n];
            if (n == 78) {
                String s;
                if (b) {
                    s = "-INF";
                }
                else {
                    s = "+INF";
                }
                this._matchToken(s, 3);
                if (this.isEnabled(Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
                    if (!b) {
                        n2 = Double.POSITIVE_INFINITY;
                    }
                    return this.resetAsNaN(s, n2);
                }
                this._reportError("Non-standard token '" + s + "': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
                n3 = n;
            }
            else if ((n3 = n) == 110) {
                String s2;
                if (b) {
                    s2 = "-Infinity";
                }
                else {
                    s2 = "+Infinity";
                }
                this._matchToken(s2, 3);
                if (this.isEnabled(Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
                    if (!b) {
                        n2 = Double.POSITIVE_INFINITY;
                    }
                    return this.resetAsNaN(s2, n2);
                }
                this._reportError("Non-standard token '" + s2 + "': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
                n3 = n;
            }
        }
        this.reportUnexpectedNumberChar(n3, "expected digit (0-9) to follow minus sign, for valid numeric value");
        return null;
    }
    
    protected JsonToken _handleUnexpectedValue(int n) throws IOException, JsonParseException {
        switch (n) {
            case 39: {
                if (this.isEnabled(Feature.ALLOW_SINGLE_QUOTES)) {
                    return this._handleApostropheValue();
                }
                break;
            }
            case 78: {
                this._matchToken("NaN", 1);
                if (this.isEnabled(Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
                    return this.resetAsNaN("NaN", Double.NaN);
                }
                this._reportError("Non-standard token 'NaN': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
                break;
            }
            case 73: {
                this._matchToken("Infinity", 1);
                if (this.isEnabled(Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
                    return this.resetAsNaN("Infinity", Double.POSITIVE_INFINITY);
                }
                this._reportError("Non-standard token 'Infinity': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
                break;
            }
            case 43: {
                if (this._inputPtr >= this._inputEnd && !this.loadMore()) {
                    this._reportInvalidEOFInValue();
                }
                final char[] inputBuffer = this._inputBuffer;
                n = this._inputPtr++;
                return this._handleInvalidNumberStart(inputBuffer[n], false);
            }
        }
        this._reportUnexpectedChar(n, "expected a valid value (number, String, array, object, 'true', 'false' or 'null')");
        return null;
    }
    
    protected String _handleUnusualFieldName(int inputPtr) throws IOException, JsonParseException {
        if (inputPtr == 39 && this.isEnabled(Feature.ALLOW_SINGLE_QUOTES)) {
            return this._parseApostropheFieldName();
        }
        if (!this.isEnabled(Feature.ALLOW_UNQUOTED_FIELD_NAMES)) {
            this._reportUnexpectedChar(inputPtr, "was expecting double-quote to start field name");
        }
        final int[] inputCodeLatin1JsNames = CharTypes.getInputCodeLatin1JsNames();
        final int length = inputCodeLatin1JsNames.length;
        int javaIdentifierPart;
        if (inputPtr < length) {
            if (inputCodeLatin1JsNames[inputPtr] == 0 && (inputPtr < 48 || inputPtr > 57)) {
                javaIdentifierPart = 1;
            }
            else {
                javaIdentifierPart = 0;
            }
        }
        else {
            javaIdentifierPart = (Character.isJavaIdentifierPart((char)inputPtr) ? 1 : 0);
        }
        if (javaIdentifierPart == 0) {
            this._reportUnexpectedChar(inputPtr, "was expecting either valid name character (for unquoted name) or double-quote (for quoted) to start field name");
        }
        final int inputPtr2 = this._inputPtr;
        int hashSeed = this._hashSeed;
        final int inputEnd = this._inputEnd;
        int n = hashSeed;
        if ((inputPtr = inputPtr2) < inputEnd) {
            inputPtr = inputPtr2;
            int n2;
            do {
                final char c = this._inputBuffer[inputPtr];
                if (c < length) {
                    if (inputCodeLatin1JsNames[c] != 0) {
                        final int n3 = this._inputPtr - 1;
                        this._inputPtr = inputPtr;
                        return this._symbols.findSymbol(this._inputBuffer, n3, inputPtr - n3, hashSeed);
                    }
                }
                else if (!Character.isJavaIdentifierPart(c)) {
                    final int n4 = this._inputPtr - 1;
                    this._inputPtr = inputPtr;
                    return this._symbols.findSymbol(this._inputBuffer, n4, inputPtr - n4, hashSeed);
                }
                n = hashSeed * 33 + c;
                n2 = inputPtr + 1;
                hashSeed = n;
            } while ((inputPtr = n2) < inputEnd);
            inputPtr = n2;
        }
        final int inputPtr3 = this._inputPtr;
        this._inputPtr = inputPtr;
        return this._parseUnusualFieldName2(inputPtr3 - 1, n, inputCodeLatin1JsNames);
    }
    
    protected void _matchToken(final String s, int n) throws IOException, JsonParseException {
        int n2;
        do {
            if (this._inputPtr >= this._inputEnd && !this.loadMore()) {
                this._reportInvalidToken(s.substring(0, n));
            }
            if (this._inputBuffer[this._inputPtr] != s.charAt(n)) {
                this._reportInvalidToken(s.substring(0, n));
            }
            ++this._inputPtr;
            n2 = n + 1;
        } while ((n = n2) < s.length());
        if (this._inputPtr < this._inputEnd || this.loadMore()) {
            final char c = this._inputBuffer[this._inputPtr];
            if (c >= '0' && c != ']' && c != '}' && Character.isJavaIdentifierPart(c)) {
                this._reportInvalidToken(s.substring(0, n2));
            }
        }
    }
    
    protected String _parseApostropheFieldName() throws IOException, JsonParseException {
        final int inputPtr = this._inputPtr;
        final int hashSeed = this._hashSeed;
        final int inputEnd = this._inputEnd;
        int n = hashSeed;
        int inputPtr2 = inputPtr;
        Label_0099: {
            if (inputPtr < inputEnd) {
                final int[] inputCodeLatin1 = CharTypes.getInputCodeLatin1();
                final int length = inputCodeLatin1.length;
                inputPtr2 = inputPtr;
                n = hashSeed;
                int n2;
                int n3;
                do {
                    final char c = this._inputBuffer[inputPtr2];
                    if (c == '\'') {
                        final int inputPtr3 = this._inputPtr;
                        this._inputPtr = inputPtr2 + 1;
                        return this._symbols.findSymbol(this._inputBuffer, inputPtr3, inputPtr2 - inputPtr3, n);
                    }
                    if (c < length && inputCodeLatin1[c] != 0) {
                        break Label_0099;
                    }
                    n3 = n * 33 + c;
                    n2 = inputPtr2 + 1;
                    n = n3;
                } while ((inputPtr2 = n2) < inputEnd);
                n = n3;
                inputPtr2 = n2;
            }
        }
        final int inputPtr4 = this._inputPtr;
        this._inputPtr = inputPtr2;
        return this._parseFieldName2(inputPtr4, n, 39);
    }
    
    protected String _parseFieldName(int n) throws IOException, JsonParseException {
        if (n != 34) {
            return this._handleUnusualFieldName(n);
        }
        n = this._inputPtr;
        int hashSeed = this._hashSeed;
        final int inputEnd = this._inputEnd;
        int n2 = hashSeed;
        int inputPtr;
        if ((inputPtr = n) < inputEnd) {
            final int[] inputCodeLatin1 = CharTypes.getInputCodeLatin1();
            final int length = inputCodeLatin1.length;
            do {
                final char c = this._inputBuffer[n];
                if (c < length && inputCodeLatin1[c] != 0) {
                    n2 = hashSeed;
                    inputPtr = n;
                    if (c == '\"') {
                        final int inputPtr2 = this._inputPtr;
                        this._inputPtr = n + 1;
                        return this._symbols.findSymbol(this._inputBuffer, inputPtr2, n - inputPtr2, hashSeed);
                    }
                    break;
                }
                else {
                    n2 = hashSeed * 33 + c;
                    inputPtr = n + 1;
                    hashSeed = n2;
                }
            } while ((n = inputPtr) < inputEnd);
        }
        n = this._inputPtr;
        this._inputPtr = inputPtr;
        return this._parseFieldName2(n, n2, 34);
    }
    
    @Override
    protected void _releaseBuffers() throws IOException {
        super._releaseBuffers();
        final char[] inputBuffer = this._inputBuffer;
        if (inputBuffer != null) {
            this._inputBuffer = null;
            this._ioContext.releaseTokenBuffer(inputBuffer);
        }
    }
    
    protected void _reportInvalidToken(final String s) throws IOException, JsonParseException {
        this._reportInvalidToken(s, "'null', 'true', 'false' or NaN");
    }
    
    protected void _reportInvalidToken(final String s, final String s2) throws IOException, JsonParseException {
        final StringBuilder sb = new StringBuilder(s);
        while (this._inputPtr < this._inputEnd || this.loadMore()) {
            final char c = this._inputBuffer[this._inputPtr];
            if (!Character.isJavaIdentifierPart(c)) {
                break;
            }
            ++this._inputPtr;
            sb.append(c);
        }
        this._reportError("Unrecognized token '" + sb.toString() + "': was expecting ");
    }
    
    protected void _skipCR() throws IOException {
        if ((this._inputPtr < this._inputEnd || this.loadMore()) && this._inputBuffer[this._inputPtr] == '\n') {
            ++this._inputPtr;
        }
        ++this._currInputRow;
        this._currInputRowStart = this._inputPtr;
    }
    
    protected void _skipLF() throws IOException {
        ++this._currInputRow;
        this._currInputRowStart = this._inputPtr;
    }
    
    protected void _skipString() throws IOException, JsonParseException {
        this._tokenIncomplete = false;
        int n = this._inputPtr;
        int n2 = this._inputEnd;
        final char[] inputBuffer = this._inputBuffer;
        while (true) {
            int inputEnd = n2;
            int inputPtr = n;
            if (n >= n2) {
                this._inputPtr = n;
                if (!this.loadMore()) {
                    this._reportInvalidEOF(": was expecting closing quote for a string value");
                }
                inputPtr = this._inputPtr;
                inputEnd = this._inputEnd;
            }
            n = inputPtr + 1;
            final char c = inputBuffer[inputPtr];
            if (c <= '\\') {
                if (c == '\\') {
                    this._inputPtr = n;
                    this._decodeEscaped();
                    n = this._inputPtr;
                    n2 = this._inputEnd;
                    continue;
                }
                if (c <= '\"') {
                    if (c == '\"') {
                        break;
                    }
                    if (c < ' ') {
                        this._inputPtr = n;
                        this._throwUnquotedSpace(c, "string value");
                    }
                }
            }
            n2 = inputEnd;
        }
        this._inputPtr = n;
    }
    
    @Override
    public void close() throws IOException {
        super.close();
        this._symbols.release();
    }
    
    @Override
    public byte[] getBinaryValue(final Base64Variant base64Variant) throws IOException, JsonParseException {
        if (this._currToken != JsonToken.VALUE_STRING && (this._currToken != JsonToken.VALUE_EMBEDDED_OBJECT || this._binaryValue == null)) {
            this._reportError("Current token (" + this._currToken + ") not VALUE_STRING or VALUE_EMBEDDED_OBJECT, can not access as binary");
        }
        while (true) {
            Label_0125: {
                if (!this._tokenIncomplete) {
                    break Label_0125;
                }
                try {
                    this._binaryValue = this._decodeBase64(base64Variant);
                    this._tokenIncomplete = false;
                    return this._binaryValue;
                }
                catch (IllegalArgumentException ex) {
                    throw this._constructError("Failed to decode VALUE_STRING as base64 (" + base64Variant + "): " + ex.getMessage());
                }
            }
            if (this._binaryValue == null) {
                final ByteArrayBuilder getByteArrayBuilder = this._getByteArrayBuilder();
                this._decodeBase64(this.getText(), getByteArrayBuilder, base64Variant);
                this._binaryValue = getByteArrayBuilder.toByteArray();
                continue;
            }
            continue;
        }
    }
    
    @Override
    public ObjectCodec getCodec() {
        return this._objectCodec;
    }
    
    protected char getNextChar(final String s) throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd && !this.loadMore()) {
            this._reportInvalidEOF(s);
        }
        return this._inputBuffer[this._inputPtr++];
    }
    
    @Override
    public String getText() throws IOException, JsonParseException {
        final JsonToken currToken = this._currToken;
        if (currToken == JsonToken.VALUE_STRING) {
            if (this._tokenIncomplete) {
                this._tokenIncomplete = false;
                this._finishString();
            }
            return this._textBuffer.contentsAsString();
        }
        return this._getText2(currToken);
    }
    
    @Override
    public char[] getTextCharacters() throws IOException, JsonParseException {
        if (this._currToken == null) {
            return null;
        }
        switch (this._currToken) {
            default: {
                return this._currToken.asCharArray();
            }
            case FIELD_NAME: {
                if (!this._nameCopied) {
                    final String currentName = this._parsingContext.getCurrentName();
                    final int length = currentName.length();
                    if (this._nameCopyBuffer == null) {
                        this._nameCopyBuffer = this._ioContext.allocNameCopyBuffer(length);
                    }
                    else if (this._nameCopyBuffer.length < length) {
                        this._nameCopyBuffer = new char[length];
                    }
                    currentName.getChars(0, length, this._nameCopyBuffer, 0);
                    this._nameCopied = true;
                }
                return this._nameCopyBuffer;
            }
            case VALUE_STRING: {
                if (this._tokenIncomplete) {
                    this._tokenIncomplete = false;
                    this._finishString();
                    return this._textBuffer.getTextBuffer();
                }
                return this._textBuffer.getTextBuffer();
            }
            case VALUE_NUMBER_INT:
            case VALUE_NUMBER_FLOAT: {
                return this._textBuffer.getTextBuffer();
            }
        }
    }
    
    @Override
    public int getTextLength() throws IOException, JsonParseException {
        int length = 0;
        if (this._currToken != null) {
            switch (this._currToken) {
                default: {
                    length = this._currToken.asCharArray().length;
                    break;
                }
                case FIELD_NAME: {
                    return this._parsingContext.getCurrentName().length();
                }
                case VALUE_STRING: {
                    if (this._tokenIncomplete) {
                        this._tokenIncomplete = false;
                        this._finishString();
                        return this._textBuffer.size();
                    }
                    return this._textBuffer.size();
                }
                case VALUE_NUMBER_INT:
                case VALUE_NUMBER_FLOAT: {
                    return this._textBuffer.size();
                }
            }
        }
        return length;
    }
    
    @Override
    public int getTextOffset() throws IOException, JsonParseException {
        if (this._currToken != null) {
            switch (this._currToken) {
                case VALUE_STRING: {
                    if (this._tokenIncomplete) {
                        this._tokenIncomplete = false;
                        this._finishString();
                        return this._textBuffer.getTextOffset();
                    }
                    return this._textBuffer.getTextOffset();
                }
                case VALUE_NUMBER_INT:
                case VALUE_NUMBER_FLOAT: {
                    return this._textBuffer.getTextOffset();
                }
            }
        }
        return 0;
    }
    
    @Override
    public String getValueAsString() throws IOException, JsonParseException {
        if (this._currToken == JsonToken.VALUE_STRING) {
            if (this._tokenIncomplete) {
                this._tokenIncomplete = false;
                this._finishString();
            }
            return this._textBuffer.contentsAsString();
        }
        return super.getValueAsString(null);
    }
    
    @Override
    public String getValueAsString(final String s) throws IOException, JsonParseException {
        if (this._currToken == JsonToken.VALUE_STRING) {
            if (this._tokenIncomplete) {
                this._tokenIncomplete = false;
                this._finishString();
            }
            return this._textBuffer.contentsAsString();
        }
        return super.getValueAsString(s);
    }
    
    @Override
    protected boolean loadMore() throws IOException {
        final boolean b = false;
        this._currInputProcessed += this._inputEnd;
        this._currInputRowStart -= this._inputEnd;
        boolean b2 = b;
        if (this._reader != null) {
            final int read = this._reader.read(this._inputBuffer, 0, this._inputBuffer.length);
            if (read > 0) {
                this._inputPtr = 0;
                this._inputEnd = read;
                b2 = true;
            }
            else {
                this._closeInput();
                b2 = b;
                if (read == 0) {
                    throw new IOException("Reader returned 0 characters when trying to read " + this._inputEnd);
                }
            }
        }
        return b2;
    }
    
    @Override
    public JsonToken nextToken() throws IOException, JsonParseException {
        this._numTypesValid = 0;
        if (this._currToken == JsonToken.FIELD_NAME) {
            return this._nextAfterName();
        }
        if (this._tokenIncomplete) {
            this._skipString();
        }
        final int skipWSOrEnd = this._skipWSOrEnd();
        if (skipWSOrEnd < 0) {
            this.close();
            return this._currToken = null;
        }
        this._tokenInputTotal = this._currInputProcessed + this._inputPtr - 1L;
        this._tokenInputRow = this._currInputRow;
        this._tokenInputCol = this._inputPtr - this._currInputRowStart - 1;
        this._binaryValue = null;
        if (skipWSOrEnd == 93) {
            if (!this._parsingContext.inArray()) {
                this._reportMismatchedEndMarker(skipWSOrEnd, '}');
            }
            this._parsingContext = this._parsingContext.getParent();
            return this._currToken = JsonToken.END_ARRAY;
        }
        if (skipWSOrEnd == 125) {
            if (!this._parsingContext.inObject()) {
                this._reportMismatchedEndMarker(skipWSOrEnd, ']');
            }
            this._parsingContext = this._parsingContext.getParent();
            return this._currToken = JsonToken.END_OBJECT;
        }
        int skipWS = skipWSOrEnd;
        if (this._parsingContext.expectComma()) {
            if (skipWSOrEnd != 44) {
                this._reportUnexpectedChar(skipWSOrEnd, "was expecting comma to separate " + this._parsingContext.getTypeDesc() + " entries");
            }
            skipWS = this._skipWS();
        }
        final boolean inObject = this._parsingContext.inObject();
        int skipWS2 = skipWS;
        if (inObject) {
            this._parsingContext.setCurrentName(this._parseFieldName(skipWS));
            this._currToken = JsonToken.FIELD_NAME;
            final int skipWS3 = this._skipWS();
            if (skipWS3 != 58) {
                this._reportUnexpectedChar(skipWS3, "was expecting a colon to separate field name and value");
            }
            skipWS2 = this._skipWS();
        }
        JsonToken jsonToken = null;
        switch (skipWS2) {
            default: {
                jsonToken = this._handleUnexpectedValue(skipWS2);
                break;
            }
            case 34: {
                this._tokenIncomplete = true;
                jsonToken = JsonToken.VALUE_STRING;
                break;
            }
            case 91: {
                if (!inObject) {
                    this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
                }
                jsonToken = JsonToken.START_ARRAY;
                break;
            }
            case 123: {
                if (!inObject) {
                    this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
                }
                jsonToken = JsonToken.START_OBJECT;
                break;
            }
            case 93:
            case 125: {
                this._reportUnexpectedChar(skipWS2, "expected a value");
            }
            case 116: {
                this._matchToken("true", 1);
                jsonToken = JsonToken.VALUE_TRUE;
                break;
            }
            case 102: {
                this._matchToken("false", 1);
                jsonToken = JsonToken.VALUE_FALSE;
                break;
            }
            case 110: {
                this._matchToken("null", 1);
                jsonToken = JsonToken.VALUE_NULL;
                break;
            }
            case 45:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57: {
                jsonToken = this.parseNumberText(skipWS2);
                break;
            }
        }
        if (inObject) {
            this._nextToken = jsonToken;
            return this._currToken;
        }
        return this._currToken = jsonToken;
    }
    
    protected JsonToken parseNumberText(int n) throws IOException, JsonParseException {
        int n2 = 1;
        int n3 = 0;
        final int n4 = 0;
        final boolean b = n == 45;
        final int inputPtr = this._inputPtr;
        final int n5 = inputPtr - 1;
        final int inputEnd = this._inputEnd;
    Label_0047:
        while (true) {
            int n7 = 0;
            Label_0129: {
                if (!b) {
                    final int n6 = inputPtr;
                    n7 = n;
                    n = n6;
                    break Label_0129;
                }
                if (inputPtr < this._inputEnd) {
                    final char[] inputBuffer = this._inputBuffer;
                    final int inputPtr2 = inputPtr + 1;
                    final char c = inputBuffer[inputPtr];
                    if (c <= '9') {
                        n = inputPtr2;
                        if ((n7 = c) >= 48) {
                            break Label_0129;
                        }
                    }
                    this._inputPtr = inputPtr2;
                    return this._handleInvalidNumberStart(c, true);
                }
                if (b) {
                    n = n5 + 1;
                }
                else {
                    n = n5;
                }
                this._inputPtr = n;
                return this.parseNumberText2(b);
            }
            if (n7 != 48) {
                for (int i = n; i < this._inputEnd; i = n) {
                    final char[] inputBuffer2 = this._inputBuffer;
                    n = i + 1;
                    char c2 = inputBuffer2[i];
                    if (c2 < '0' || c2 > '9') {
                        int n10 = 0;
                        Label_0234: {
                            if (c2 == '.') {
                                final int n8 = 0;
                                int j = n;
                                n = n8;
                                while (j < inputEnd) {
                                    final char[] inputBuffer3 = this._inputBuffer;
                                    final int n9 = j + 1;
                                    c2 = inputBuffer3[j];
                                    if (c2 < '0' || c2 > '9') {
                                        if (n == 0) {
                                            this.reportUnexpectedNumberChar(c2, "Decimal point not followed by a digit");
                                        }
                                        n10 = n;
                                        n = n9;
                                        break Label_0234;
                                    }
                                    ++n;
                                    j = n9;
                                }
                                break;
                            }
                            n10 = 0;
                        }
                        int n11 = 0;
                        Label_0386: {
                            if (c2 != 'e') {
                                n11 = n;
                                if (c2 != 'E') {
                                    break Label_0386;
                                }
                            }
                            if (n >= inputEnd) {
                                break;
                            }
                            final char[] inputBuffer4 = this._inputBuffer;
                            final int n12 = n + 1;
                            char c3 = inputBuffer4[n];
                            int n13;
                            if (c3 == '-' || c3 == '+') {
                                if (n12 >= inputEnd) {
                                    break;
                                }
                                final char[] inputBuffer5 = this._inputBuffer;
                                n = n12 + 1;
                                c3 = inputBuffer5[n12];
                                n13 = n4;
                            }
                            else {
                                n = n12;
                                n13 = n4;
                            }
                            while (c3 <= '9' && c3 >= '0') {
                                ++n13;
                                if (n >= inputEnd) {
                                    continue Label_0047;
                                }
                                c3 = this._inputBuffer[n];
                                ++n;
                            }
                            n3 = n13;
                            n11 = n;
                            if (n13 == 0) {
                                this.reportUnexpectedNumberChar(c3, "Exponent indicator not followed by a digit");
                                n11 = n;
                                n3 = n13;
                            }
                        }
                        n = n11 - 1;
                        this._inputPtr = n;
                        this._textBuffer.resetWithShared(this._inputBuffer, n5, n - n5);
                        return this.reset(b, n2, n10, n3);
                    }
                    ++n2;
                }
            }
            continue Label_0047;
        }
    }
}
