// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.sym.Name;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonParseException;
import java.io.IOException;
import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.sym.BytesToNameCanonicalizer;
import com.fasterxml.jackson.core.ObjectCodec;
import java.io.InputStream;
import com.fasterxml.jackson.core.base.ParserBase;

public final class UTF8StreamJsonParser extends ParserBase
{
    private static final int[] sInputCodesLatin1;
    private static final int[] sInputCodesUtf8;
    protected boolean _bufferRecyclable;
    protected byte[] _inputBuffer;
    protected InputStream _inputStream;
    protected ObjectCodec _objectCodec;
    private int _quad1;
    protected int[] _quadBuffer;
    protected final BytesToNameCanonicalizer _symbols;
    protected boolean _tokenIncomplete;
    
    static {
        sInputCodesUtf8 = CharTypes.getInputCodeUtf8();
        sInputCodesLatin1 = CharTypes.getInputCodeLatin1();
    }
    
    private int _decodeUtf8_2(final int n) throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd) {
            this.loadMoreGuaranteed();
        }
        final byte b = this._inputBuffer[this._inputPtr++];
        if ((b & 0xC0) != 0x80) {
            this._reportInvalidOther(b & 0xFF, this._inputPtr);
        }
        return (b & 0x3F) | (n & 0x1F) << 6;
    }
    
    private int _decodeUtf8_3(final int n) throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd) {
            this.loadMoreGuaranteed();
        }
        final byte b = this._inputBuffer[this._inputPtr++];
        if ((b & 0xC0) != 0x80) {
            this._reportInvalidOther(b & 0xFF, this._inputPtr);
        }
        if (this._inputPtr >= this._inputEnd) {
            this.loadMoreGuaranteed();
        }
        final byte b2 = this._inputBuffer[this._inputPtr++];
        if ((b2 & 0xC0) != 0x80) {
            this._reportInvalidOther(b2 & 0xFF, this._inputPtr);
        }
        return ((n & 0xF) << 6 | (b & 0x3F)) << 6 | (b2 & 0x3F);
    }
    
    private int _decodeUtf8_3fast(final int n) throws IOException, JsonParseException {
        final byte b = this._inputBuffer[this._inputPtr++];
        if ((b & 0xC0) != 0x80) {
            this._reportInvalidOther(b & 0xFF, this._inputPtr);
        }
        final byte b2 = this._inputBuffer[this._inputPtr++];
        if ((b2 & 0xC0) != 0x80) {
            this._reportInvalidOther(b2 & 0xFF, this._inputPtr);
        }
        return ((n & 0xF) << 6 | (b & 0x3F)) << 6 | (b2 & 0x3F);
    }
    
    private int _decodeUtf8_4(final int n) throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd) {
            this.loadMoreGuaranteed();
        }
        final byte b = this._inputBuffer[this._inputPtr++];
        if ((b & 0xC0) != 0x80) {
            this._reportInvalidOther(b & 0xFF, this._inputPtr);
        }
        if (this._inputPtr >= this._inputEnd) {
            this.loadMoreGuaranteed();
        }
        final byte b2 = this._inputBuffer[this._inputPtr++];
        if ((b2 & 0xC0) != 0x80) {
            this._reportInvalidOther(b2 & 0xFF, this._inputPtr);
        }
        if (this._inputPtr >= this._inputEnd) {
            this.loadMoreGuaranteed();
        }
        final byte b3 = this._inputBuffer[this._inputPtr++];
        if ((b3 & 0xC0) != 0x80) {
            this._reportInvalidOther(b3 & 0xFF, this._inputPtr);
        }
        return ((((b & 0x3F) | (n & 0x7) << 6) << 6 | (b2 & 0x3F)) << 6 | (b3 & 0x3F)) - 65536;
    }
    
    private void _finishString2(char[] array, int inputPtr) throws IOException, JsonParseException {
        final int[] sInputCodesUtf8 = UTF8StreamJsonParser.sInputCodesUtf8;
        final byte[] inputBuffer = this._inputBuffer;
        char[] array2 = array;
        int currentLength = 0;
    Block_5:
        while (true) {
            int i;
            if ((i = this._inputPtr) >= this._inputEnd) {
                this.loadMoreGuaranteed();
                i = this._inputPtr;
            }
            array = array2;
            if ((currentLength = inputPtr) >= array2.length) {
                array = this._textBuffer.finishCurrentSegment();
                currentLength = 0;
            }
            while (i < Math.min(this._inputEnd, array.length - currentLength + i)) {
                inputPtr = i + 1;
                final int n = inputBuffer[i] & 0xFF;
                if (sInputCodesUtf8[n] != 0) {
                    this._inputPtr = inputPtr;
                    if (n == 34) {
                        break Block_5;
                    }
                    switch (sInputCodesUtf8[n]) {
                        default: {
                            if (n < 32) {
                                this._throwUnquotedSpace(n, "string value");
                                inputPtr = n;
                                break;
                            }
                            this._reportInvalidChar(n);
                            inputPtr = n;
                            break;
                        }
                        case 1: {
                            inputPtr = this._decodeEscaped();
                            break;
                        }
                        case 2: {
                            inputPtr = this._decodeUtf8_2(n);
                            break;
                        }
                        case 3: {
                            if (this._inputEnd - this._inputPtr >= 2) {
                                inputPtr = this._decodeUtf8_3fast(n);
                                break;
                            }
                            inputPtr = this._decodeUtf8_3(n);
                            break;
                        }
                        case 4: {
                            final int decodeUtf8_4 = this._decodeUtf8_4(n);
                            final int n2 = currentLength + 1;
                            array[currentLength] = (char)(0xD800 | decodeUtf8_4 >> 10);
                            inputPtr = n2;
                            char[] finishCurrentSegment = array;
                            if (n2 >= array.length) {
                                finishCurrentSegment = this._textBuffer.finishCurrentSegment();
                                inputPtr = 0;
                            }
                            currentLength = inputPtr;
                            inputPtr = ((decodeUtf8_4 & 0x3FF) | 0xDC00);
                            array = finishCurrentSegment;
                            break;
                        }
                    }
                    if (currentLength >= array.length) {
                        array = this._textBuffer.finishCurrentSegment();
                        currentLength = 0;
                    }
                    final int n3 = currentLength + 1;
                    array[currentLength] = (char)inputPtr;
                    array2 = array;
                    inputPtr = n3;
                    continue Block_5;
                }
                else {
                    array[currentLength] = (char)n;
                    i = inputPtr;
                    ++currentLength;
                }
            }
            this._inputPtr = i;
            array2 = array;
            inputPtr = currentLength;
        }
        this._textBuffer.setCurrentLength(currentLength);
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
    
    private JsonToken _nextTokenNotInObject(final int n) throws IOException, JsonParseException {
        if (n == 34) {
            this._tokenIncomplete = true;
            return this._currToken = JsonToken.VALUE_STRING;
        }
        switch (n) {
            default: {
                return this._currToken = this._handleUnexpectedValue(n);
            }
            case 91: {
                this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
                return this._currToken = JsonToken.START_ARRAY;
            }
            case 123: {
                this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
                return this._currToken = JsonToken.START_OBJECT;
            }
            case 93:
            case 125: {
                this._reportUnexpectedChar(n, "expected a value");
            }
            case 116: {
                this._matchToken("true", 1);
                return this._currToken = JsonToken.VALUE_TRUE;
            }
            case 102: {
                this._matchToken("false", 1);
                return this._currToken = JsonToken.VALUE_FALSE;
            }
            case 110: {
                this._matchToken("null", 1);
                return this._currToken = JsonToken.VALUE_NULL;
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
                return this._currToken = this.parseNumberText(n);
            }
        }
    }
    
    private JsonToken _parseFloatText(char[] array, int n, int n2, final boolean b, final int n3) throws IOException, JsonParseException {
        final int n4 = 0;
        final boolean b2 = false;
        final boolean b3 = false;
        int n8 = 0;
        int n10 = 0;
        char[] array2 = null;
        Label_0076: {
            if (n2 == 46) {
                final int n5 = n + 1;
                array[n] = (char)n2;
                int n6 = n2;
                n = n5;
                n2 = n4;
                while (true) {
                    while (this._inputPtr < this._inputEnd || this.loadMore()) {
                        final int n7 = this._inputBuffer[this._inputPtr++] & 0xFF;
                        n8 = (b3 ? 1 : 0);
                        if ((n6 = n7) >= 48) {
                            n8 = (b3 ? 1 : 0);
                            if ((n6 = n7) <= 57) {
                                ++n2;
                                if (n >= array.length) {
                                    array = this._textBuffer.finishCurrentSegment();
                                    n = 0;
                                }
                                final int n9 = n + 1;
                                array[n] = (char)n7;
                                n = n9;
                                n6 = n7;
                                continue;
                            }
                        }
                        if (n2 == 0) {
                            this.reportUnexpectedNumberChar(n6, "Decimal point not followed by a digit");
                        }
                        n10 = n2;
                        n2 = n6;
                        array2 = array;
                        break Label_0076;
                    }
                    n8 = 1;
                    continue;
                }
            }
            n10 = 0;
            array2 = array;
            n8 = (b2 ? 1 : 0);
        }
        int n16 = 0;
        int n17 = 0;
        int currentLength = 0;
        Label_0367: {
            if (n2 == 101 || n2 == 69) {
                int n11 = n;
                array = array2;
                if (n >= array2.length) {
                    array = this._textBuffer.finishCurrentSegment();
                    n11 = 0;
                }
                n = n11 + 1;
                array[n11] = (char)n2;
                if (this._inputPtr >= this._inputEnd) {
                    this.loadMoreGuaranteed();
                }
                final byte[] inputBuffer = this._inputBuffer;
                n2 = this._inputPtr++;
                int n12 = inputBuffer[n2] & 0xFF;
                if (n12 == 45 || n12 == 43) {
                    if (n >= array.length) {
                        array = this._textBuffer.finishCurrentSegment();
                        n = 0;
                    }
                    array[n] = (char)n12;
                    if (this._inputPtr >= this._inputEnd) {
                        this.loadMoreGuaranteed();
                    }
                    final byte[] inputBuffer2 = this._inputBuffer;
                    n2 = this._inputPtr++;
                    n12 = (inputBuffer2[n2] & 0xFF);
                    ++n;
                    n2 = 0;
                }
                else {
                    n2 = 0;
                }
                while (true) {
                    while (n12 <= 57 && n12 >= 48) {
                        ++n2;
                        int n13 = n;
                        char[] finishCurrentSegment = array;
                        if (n >= array.length) {
                            finishCurrentSegment = this._textBuffer.finishCurrentSegment();
                            n13 = 0;
                        }
                        n = n13 + 1;
                        finishCurrentSegment[n13] = (char)n12;
                        if (this._inputPtr >= this._inputEnd && !this.loadMore()) {
                            final int n14 = n2;
                            n2 = 1;
                            final int n15 = n;
                            n = n14;
                            n16 = n;
                            n17 = n2;
                            currentLength = n15;
                            if (n == 0) {
                                this.reportUnexpectedNumberChar(n12, "Exponent indicator not followed by a digit");
                                currentLength = n15;
                                n17 = n2;
                                n16 = n;
                            }
                            break Label_0367;
                        }
                        else {
                            n12 = (this._inputBuffer[this._inputPtr++] & 0xFF);
                            array = finishCurrentSegment;
                        }
                    }
                    final int n18 = n;
                    n = n2;
                    n2 = n8;
                    final int n15 = n18;
                    continue;
                }
            }
            n16 = 0;
            n17 = n8;
            currentLength = n;
        }
        if (n17 == 0) {
            --this._inputPtr;
        }
        this._textBuffer.setCurrentLength(currentLength);
        return this.resetFloat(b, n3, n10, n16);
    }
    
    private JsonToken _parserNumber2(char[] finishCurrentSegment, int n, final boolean b, int n2) throws IOException, JsonParseException {
        while (this._inputPtr < this._inputEnd || this.loadMore()) {
            final int n3 = this._inputBuffer[this._inputPtr++] & 0xFF;
            if (n3 > 57 || n3 < 48) {
                if (n3 == 46 || n3 == 101 || n3 == 69) {
                    return this._parseFloatText(finishCurrentSegment, n, n3, b, n2);
                }
                --this._inputPtr;
                this._textBuffer.setCurrentLength(n);
                return this.resetInt(b, n2);
            }
            else {
                if (n >= finishCurrentSegment.length) {
                    finishCurrentSegment = this._textBuffer.finishCurrentSegment();
                    n = 0;
                }
                final int n4 = n + 1;
                finishCurrentSegment[n] = (char)n3;
                ++n2;
                n = n4;
            }
        }
        this._textBuffer.setCurrentLength(n);
        return this.resetInt(b, n2);
    }
    
    private void _skipCComment() throws IOException, JsonParseException {
        final int[] inputCodeComment = CharTypes.getInputCodeComment();
    Label_0142:
        while (this._inputPtr < this._inputEnd || this.loadMore()) {
            final int n = this._inputBuffer[this._inputPtr++] & 0xFF;
            final int n2 = inputCodeComment[n];
            if (n2 != 0) {
                switch (n2) {
                    default: {
                        this._reportInvalidChar(n);
                        continue;
                    }
                    case 42: {
                        if (this._inputPtr >= this._inputEnd && !this.loadMore()) {
                            break Label_0142;
                        }
                        if (this._inputBuffer[this._inputPtr] == 47) {
                            ++this._inputPtr;
                            return;
                        }
                        continue;
                    }
                    case 10: {
                        this._skipLF();
                        continue;
                    }
                    case 13: {
                        this._skipCR();
                        continue;
                    }
                    case 2: {
                        this._skipUtf8_2(n);
                        continue;
                    }
                    case 3: {
                        this._skipUtf8_3(n);
                        continue;
                    }
                    case 4: {
                        this._skipUtf8_4(n);
                        continue;
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
        final int n = this._inputBuffer[this._inputPtr++] & 0xFF;
        if (n == 47) {
            this._skipCppComment();
            return;
        }
        if (n == 42) {
            this._skipCComment();
            return;
        }
        this._reportUnexpectedChar(n, "was expecting either '*' or '/' for a comment");
    }
    
    private void _skipCppComment() throws IOException, JsonParseException {
        final int[] inputCodeComment = CharTypes.getInputCodeComment();
    Label_0128:
        while (this._inputPtr < this._inputEnd || this.loadMore()) {
            final int n = this._inputBuffer[this._inputPtr++] & 0xFF;
            final int n2 = inputCodeComment[n];
            if (n2 != 0) {
                switch (n2) {
                    case 4: {
                        this._skipUtf8_4(n);
                        continue;
                    }
                    case 3: {
                        this._skipUtf8_3(n);
                        continue;
                    }
                    case 2: {
                        this._skipUtf8_2(n);
                    }
                    case 42: {
                        continue;
                    }
                    default: {
                        this._reportInvalidChar(n);
                        continue;
                    }
                    case 10: {
                        this._skipLF();
                        break Label_0128;
                    }
                    case 13: {
                        this._skipCR();
                        return;
                    }
                }
            }
        }
    }
    
    private void _skipUtf8_2(int n) throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd) {
            this.loadMoreGuaranteed();
        }
        final byte[] inputBuffer = this._inputBuffer;
        n = this._inputPtr++;
        n = inputBuffer[n];
        if ((n & 0xC0) != 0x80) {
            this._reportInvalidOther(n & 0xFF, this._inputPtr);
        }
    }
    
    private void _skipUtf8_3(int n) throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd) {
            this.loadMoreGuaranteed();
        }
        final byte[] inputBuffer = this._inputBuffer;
        n = this._inputPtr++;
        n = inputBuffer[n];
        if ((n & 0xC0) != 0x80) {
            this._reportInvalidOther(n & 0xFF, this._inputPtr);
        }
        if (this._inputPtr >= this._inputEnd) {
            this.loadMoreGuaranteed();
        }
        final byte[] inputBuffer2 = this._inputBuffer;
        n = this._inputPtr++;
        n = inputBuffer2[n];
        if ((n & 0xC0) != 0x80) {
            this._reportInvalidOther(n & 0xFF, this._inputPtr);
        }
    }
    
    private void _skipUtf8_4(int n) throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd) {
            this.loadMoreGuaranteed();
        }
        final byte[] inputBuffer = this._inputBuffer;
        n = this._inputPtr++;
        n = inputBuffer[n];
        if ((n & 0xC0) != 0x80) {
            this._reportInvalidOther(n & 0xFF, this._inputPtr);
        }
        if (this._inputPtr >= this._inputEnd) {
            this.loadMoreGuaranteed();
        }
        final byte[] inputBuffer2 = this._inputBuffer;
        n = this._inputPtr++;
        n = inputBuffer2[n];
        if ((n & 0xC0) != 0x80) {
            this._reportInvalidOther(n & 0xFF, this._inputPtr);
        }
        if (this._inputPtr >= this._inputEnd) {
            this.loadMoreGuaranteed();
        }
        final byte[] inputBuffer3 = this._inputBuffer;
        n = this._inputPtr++;
        n = inputBuffer3[n];
        if ((n & 0xC0) != 0x80) {
            this._reportInvalidOther(n & 0xFF, this._inputPtr);
        }
    }
    
    private int _skipWS() throws IOException, JsonParseException {
        while (this._inputPtr < this._inputEnd || this.loadMore()) {
            final int n = this._inputBuffer[this._inputPtr++] & 0xFF;
            if (n > 32) {
                if (n != 47) {
                    return n;
                }
                this._skipComment();
            }
            else {
                if (n == 32) {
                    continue;
                }
                if (n == 10) {
                    this._skipLF();
                }
                else if (n == 13) {
                    this._skipCR();
                }
                else {
                    if (n == 9) {
                        continue;
                    }
                    this._throwInvalidSpace(n);
                }
            }
        }
        throw this._constructError("Unexpected end-of-input within/between " + this._parsingContext.getTypeDesc() + " entries");
    }
    
    private int _skipWSOrEnd() throws IOException, JsonParseException {
        while (this._inputPtr < this._inputEnd || this.loadMore()) {
            final int n = this._inputBuffer[this._inputPtr++] & 0xFF;
            if (n > 32) {
                if (n != 47) {
                    return n;
                }
                this._skipComment();
            }
            else {
                if (n == 32) {
                    continue;
                }
                if (n == 10) {
                    this._skipLF();
                }
                else if (n == 13) {
                    this._skipCR();
                }
                else {
                    if (n == 9) {
                        continue;
                    }
                    this._throwInvalidSpace(n);
                }
            }
        }
        this._handleEOF();
        return -1;
    }
    
    private int _verifyNoLeadingZeroes() throws IOException, JsonParseException {
        int n;
        if (this._inputPtr >= this._inputEnd && !this.loadMore()) {
            n = 48;
        }
        else {
            final int n2 = this._inputBuffer[this._inputPtr] & 0xFF;
            if (n2 < 48 || n2 > 57) {
                return 48;
            }
            if (!this.isEnabled(Feature.ALLOW_NUMERIC_LEADING_ZEROS)) {
                this.reportInvalidNumber("Leading zeroes not allowed");
            }
            ++this._inputPtr;
            if ((n = n2) == 48) {
                n = n2;
                while (this._inputPtr < this._inputEnd || this.loadMore()) {
                    final int n3 = this._inputBuffer[this._inputPtr] & 0xFF;
                    if (n3 < 48 || n3 > 57) {
                        return 48;
                    }
                    ++this._inputPtr;
                    if ((n = n3) != 48) {
                        return n3;
                    }
                }
            }
        }
        return n;
    }
    
    private Name addName(final int[] array, final int n, final int n2) throws JsonParseException {
        final int n3 = (n << 2) - 4 + n2;
        int n4;
        if (n2 < 4) {
            n4 = array[n - 1];
            array[n - 1] = n4 << (4 - n2 << 3);
        }
        else {
            n4 = 0;
        }
        char[] emptyAndGetCurrentSegment = this._textBuffer.emptyAndGetCurrentSegment();
        int n5 = 0;
        int i = 0;
    Label_0408_Outer:
        while (i < n3) {
            final int n6 = array[i >> 2] >> (3 - (i & 0x3) << 3) & 0xFF;
            final int n7 = i + 1;
            int n8 = n6;
            int n9 = n7;
            while (true) {
                Label_0559: {
                    if (n6 <= 127) {
                        break Label_0559;
                    }
                    int n10;
                    int n11;
                    if ((n6 & 0xE0) == 0xC0) {
                        n10 = (n6 & 0x1F);
                        n11 = 1;
                    }
                    else if ((n6 & 0xF0) == 0xE0) {
                        n10 = (n6 & 0xF);
                        n11 = 2;
                    }
                    else if ((n6 & 0xF8) == 0xF0) {
                        n10 = (n6 & 0x7);
                        n11 = 3;
                    }
                    else {
                        this._reportInvalidInitial(n6);
                        n10 = 1;
                        n11 = 1;
                    }
                    if (n7 + n11 > n3) {
                        this._reportInvalidEOF(" in field name");
                    }
                    final int n12 = array[n7 >> 2] >> (3 - (n7 & 0x3) << 3);
                    final int n13 = n7 + 1;
                    if ((n12 & 0xC0) != 0x80) {
                        this._reportInvalidOther(n12);
                    }
                    int n15;
                    final int n14 = n15 = (n10 << 6 | (n12 & 0x3F));
                    i = n13;
                    if (n11 > 1) {
                        final int n16 = array[n13 >> 2] >> (3 - (n13 & 0x3) << 3);
                        final int n17 = n13 + 1;
                        if ((n16 & 0xC0) != 0x80) {
                            this._reportInvalidOther(n16);
                        }
                        final int n18 = n15 = (n14 << 6 | (n16 & 0x3F));
                        i = n17;
                        if (n11 > 2) {
                            final int n19 = array[n17 >> 2] >> (3 - (n17 & 0x3) << 3);
                            i = n17 + 1;
                            if ((n19 & 0xC0) != 0x80) {
                                this._reportInvalidOther(n19 & 0xFF);
                            }
                            n15 = (n18 << 6 | (n19 & 0x3F));
                        }
                    }
                    n8 = n15;
                    n9 = i;
                    if (n11 <= 2) {
                        break Label_0559;
                    }
                    final int n20 = n15 - 65536;
                    char[] expandCurrentSegment = emptyAndGetCurrentSegment;
                    if (n5 >= emptyAndGetCurrentSegment.length) {
                        expandCurrentSegment = this._textBuffer.expandCurrentSegment();
                    }
                    expandCurrentSegment[n5] = (char)(55296 + (n20 >> 10));
                    final int n21 = n5 + 1;
                    emptyAndGetCurrentSegment = expandCurrentSegment;
                    final int n22 = (n20 & 0x3FF) | 0xDC00;
                    char[] expandCurrentSegment2 = emptyAndGetCurrentSegment;
                    if (n21 >= emptyAndGetCurrentSegment.length) {
                        expandCurrentSegment2 = this._textBuffer.expandCurrentSegment();
                    }
                    n5 = n21 + 1;
                    expandCurrentSegment2[n21] = (char)n22;
                    emptyAndGetCurrentSegment = expandCurrentSegment2;
                    continue Label_0408_Outer;
                }
                final int n22 = n8;
                i = n9;
                final int n21 = n5;
                continue;
            }
        }
        final String s = new String(emptyAndGetCurrentSegment, 0, n5);
        if (n2 < 4) {
            array[n - 1] = n4;
        }
        return this._symbols.addName(s, array, n);
    }
    
    private Name findName(final int n, final int n2) throws JsonParseException {
        final Name name = this._symbols.findName(n);
        if (name != null) {
            return name;
        }
        this._quadBuffer[0] = n;
        return this.addName(this._quadBuffer, 1, n2);
    }
    
    private Name findName(final int n, final int n2, final int n3) throws JsonParseException {
        final Name name = this._symbols.findName(n, n2);
        if (name != null) {
            return name;
        }
        this._quadBuffer[0] = n;
        this._quadBuffer[1] = n2;
        return this.addName(this._quadBuffer, 2, n3);
    }
    
    private Name findName(final int[] array, final int n, final int n2, final int n3) throws JsonParseException {
        int[] growArrayBy = array;
        if (n >= array.length) {
            growArrayBy = growArrayBy(array, array.length);
            this._quadBuffer = growArrayBy;
        }
        final int n4 = n + 1;
        growArrayBy[n] = n2;
        Name name;
        if ((name = this._symbols.findName(growArrayBy, n4)) == null) {
            name = this.addName(growArrayBy, n4, n3);
        }
        return name;
    }
    
    public static int[] growArrayBy(final int[] array, final int n) {
        if (array == null) {
            return new int[n];
        }
        final int length = array.length;
        final int[] array2 = new int[length + n];
        System.arraycopy(array, 0, array2, 0, length);
        return array2;
    }
    
    private int nextByte() throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd) {
            this.loadMoreGuaranteed();
        }
        return this._inputBuffer[this._inputPtr++] & 0xFF;
    }
    
    private Name parseFieldName(final int n, final int n2, final int n3) throws IOException, JsonParseException {
        return this.parseEscapedFieldName(this._quadBuffer, 0, n, n2, n3);
    }
    
    private Name parseFieldName(final int n, final int n2, final int n3, final int n4) throws IOException, JsonParseException {
        this._quadBuffer[0] = n;
        return this.parseEscapedFieldName(this._quadBuffer, 1, n2, n3, n4);
    }
    
    @Override
    protected void _closeInput() throws IOException {
        if (this._inputStream != null) {
            if (this._ioContext.isResourceManaged() || this.isEnabled(Feature.AUTO_CLOSE_SOURCE)) {
                this._inputStream.close();
            }
            this._inputStream = null;
        }
    }
    
    protected byte[] _decodeBase64(final Base64Variant base64Variant) throws IOException, JsonParseException {
        final ByteArrayBuilder getByteArrayBuilder = this._getByteArrayBuilder();
        while (true) {
            if (this._inputPtr >= this._inputEnd) {
                this.loadMoreGuaranteed();
            }
            final int n = this._inputBuffer[this._inputPtr++] & 0xFF;
            if (n > 32) {
                int n2;
                if ((n2 = base64Variant.decodeBase64Char(n)) < 0) {
                    if (n == 34) {
                        return getByteArrayBuilder.toByteArray();
                    }
                    n2 = this._decodeBase64Escape(base64Variant, n, 0);
                    if (n2 < 0) {
                        continue;
                    }
                }
                if (this._inputPtr >= this._inputEnd) {
                    this.loadMoreGuaranteed();
                }
                final int n3 = this._inputBuffer[this._inputPtr++] & 0xFF;
                int n4;
                if ((n4 = base64Variant.decodeBase64Char(n3)) < 0) {
                    n4 = this._decodeBase64Escape(base64Variant, n3, 1);
                }
                final int n5 = n4 | n2 << 6;
                if (this._inputPtr >= this._inputEnd) {
                    this.loadMoreGuaranteed();
                }
                final int n6 = this._inputBuffer[this._inputPtr++] & 0xFF;
                final int decodeBase64Char = base64Variant.decodeBase64Char(n6);
                int n7;
                if ((n7 = decodeBase64Char) < 0) {
                    int decodeBase64Escape;
                    if ((decodeBase64Escape = decodeBase64Char) != -2) {
                        if (n6 == 34 && !base64Variant.usesPadding()) {
                            getByteArrayBuilder.append(n5 >> 4);
                            return getByteArrayBuilder.toByteArray();
                        }
                        decodeBase64Escape = this._decodeBase64Escape(base64Variant, n6, 2);
                    }
                    if ((n7 = decodeBase64Escape) == -2) {
                        if (this._inputPtr >= this._inputEnd) {
                            this.loadMoreGuaranteed();
                        }
                        final int n8 = this._inputBuffer[this._inputPtr++] & 0xFF;
                        if (!base64Variant.usesPaddingChar(n8)) {
                            throw this.reportInvalidBase64Char(base64Variant, n8, 3, "expected padding character '" + base64Variant.getPaddingChar() + "'");
                        }
                        getByteArrayBuilder.append(n5 >> 4);
                        continue;
                    }
                }
                final int n9 = n5 << 6 | n7;
                if (this._inputPtr >= this._inputEnd) {
                    this.loadMoreGuaranteed();
                }
                final int n10 = this._inputBuffer[this._inputPtr++] & 0xFF;
                final int decodeBase64Char2 = base64Variant.decodeBase64Char(n10);
                int n11;
                if ((n11 = decodeBase64Char2) < 0) {
                    int decodeBase64Escape2;
                    if ((decodeBase64Escape2 = decodeBase64Char2) != -2) {
                        if (n10 == 34 && !base64Variant.usesPadding()) {
                            getByteArrayBuilder.appendTwoBytes(n9 >> 2);
                            return getByteArrayBuilder.toByteArray();
                        }
                        decodeBase64Escape2 = this._decodeBase64Escape(base64Variant, n10, 3);
                    }
                    if ((n11 = decodeBase64Escape2) == -2) {
                        getByteArrayBuilder.appendTwoBytes(n9 >> 2);
                        continue;
                    }
                }
                getByteArrayBuilder.appendThreeBytes(n11 | n9 << 6);
            }
        }
    }
    
    protected int _decodeCharForError(int n) throws IOException, JsonParseException {
        int n2 = n;
        if (n < 0) {
            int n3;
            if ((n & 0xE0) == 0xC0) {
                n &= 0x1F;
                n3 = 1;
            }
            else if ((n & 0xF0) == 0xE0) {
                n &= 0xF;
                n3 = 2;
            }
            else if ((n & 0xF8) == 0xF0) {
                n &= 0x7;
                n3 = 3;
            }
            else {
                this._reportInvalidInitial(n & 0xFF);
                n3 = 1;
            }
            final int nextByte = this.nextByte();
            if ((nextByte & 0xC0) != 0x80) {
                this._reportInvalidOther(nextByte & 0xFF);
            }
            n = (n2 = (n << 6 | (nextByte & 0x3F)));
            if (n3 > 1) {
                final int nextByte2 = this.nextByte();
                if ((nextByte2 & 0xC0) != 0x80) {
                    this._reportInvalidOther(nextByte2 & 0xFF);
                }
                n = (n2 = (n << 6 | (nextByte2 & 0x3F)));
                if (n3 > 2) {
                    final int nextByte3 = this.nextByte();
                    if ((nextByte3 & 0xC0) != 0x80) {
                        this._reportInvalidOther(nextByte3 & 0xFF);
                    }
                    n2 = (n << 6 | (nextByte3 & 0x3F));
                }
            }
        }
        return n2;
    }
    
    @Override
    protected char _decodeEscaped() throws IOException, JsonParseException {
        int i = 0;
        if (this._inputPtr >= this._inputEnd && !this.loadMore()) {
            this._reportInvalidEOF(" in character escape sequence");
        }
        final byte b = this._inputBuffer[this._inputPtr++];
        switch (b) {
            default: {
                return this._handleUnrecognizedCharacterEscape((char)this._decodeCharForError(b));
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
            case 34:
            case 47:
            case 92: {
                return (char)b;
            }
            case 117: {
                int n = 0;
                while (i < 4) {
                    if (this._inputPtr >= this._inputEnd && !this.loadMore()) {
                        this._reportInvalidEOF(" in character escape sequence");
                    }
                    final byte b2 = this._inputBuffer[this._inputPtr++];
                    final int charToHex = CharTypes.charToHex(b2);
                    if (charToHex < 0) {
                        this._reportUnexpectedChar(b2, "expected a hex-digit for character escape sequence");
                    }
                    n = (n << 4 | charToHex);
                    ++i;
                }
                return (char)n;
            }
        }
    }
    
    @Override
    protected void _finishString() throws IOException, JsonParseException {
        int i;
        if ((i = this._inputPtr) >= this._inputEnd) {
            this.loadMoreGuaranteed();
            i = this._inputPtr;
        }
        final char[] emptyAndGetCurrentSegment = this._textBuffer.emptyAndGetCurrentSegment();
        final int[] sInputCodesUtf8 = UTF8StreamJsonParser.sInputCodesUtf8;
        final int min = Math.min(this._inputEnd, emptyAndGetCurrentSegment.length + i);
        final byte[] inputBuffer = this._inputBuffer;
        int currentLength = 0;
        while (i < min) {
            final int n = inputBuffer[i] & 0xFF;
            if (sInputCodesUtf8[n] != 0) {
                if (n == 34) {
                    this._inputPtr = i + 1;
                    this._textBuffer.setCurrentLength(currentLength);
                    return;
                }
                break;
            }
            else {
                emptyAndGetCurrentSegment[currentLength] = (char)n;
                ++currentLength;
                ++i;
            }
        }
        this._inputPtr = i;
        this._finishString2(emptyAndGetCurrentSegment, currentLength);
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
        char[] emptyAndGetCurrentSegment = this._textBuffer.emptyAndGetCurrentSegment();
        final int[] sInputCodesUtf8 = UTF8StreamJsonParser.sInputCodesUtf8;
        final byte[] inputBuffer = this._inputBuffer;
        int n = 0;
        int currentLength = 0;
    Label_0022:
        while (true) {
            if (this._inputPtr >= this._inputEnd) {
                this.loadMoreGuaranteed();
            }
            char[] array = emptyAndGetCurrentSegment;
            if ((currentLength = n) >= emptyAndGetCurrentSegment.length) {
                array = this._textBuffer.finishCurrentSegment();
                currentLength = 0;
            }
            int inputEnd = this._inputEnd;
            final int n2 = this._inputPtr + (array.length - currentLength);
            if (n2 < inputEnd) {
                inputEnd = n2;
            }
            int n3;
            while (true) {
                emptyAndGetCurrentSegment = array;
                n = currentLength;
                if (this._inputPtr >= inputEnd) {
                    continue Label_0022;
                }
                n3 = (inputBuffer[this._inputPtr++] & 0xFF);
                if (n3 == 39 || sInputCodesUtf8[n3] != 0) {
                    break;
                }
                array[currentLength] = (char)n3;
                ++currentLength;
            }
            if (n3 == 39) {
                break;
            }
            switch (sInputCodesUtf8[n3]) {
                default: {
                    if (n3 < 32) {
                        this._throwUnquotedSpace(n3, "string value");
                    }
                    this._reportInvalidChar(n3);
                    break;
                }
                case 1: {
                    if (n3 != 34) {
                        n3 = this._decodeEscaped();
                        break;
                    }
                    break;
                }
                case 2: {
                    n3 = this._decodeUtf8_2(n3);
                    break;
                }
                case 3: {
                    if (this._inputEnd - this._inputPtr >= 2) {
                        n3 = this._decodeUtf8_3fast(n3);
                        break;
                    }
                    n3 = this._decodeUtf8_3(n3);
                    break;
                }
                case 4: {
                    final int decodeUtf8_4 = this._decodeUtf8_4(n3);
                    final int n4 = currentLength + 1;
                    array[currentLength] = (char)(0xD800 | decodeUtf8_4 >> 10);
                    if (n4 >= array.length) {
                        array = this._textBuffer.finishCurrentSegment();
                        currentLength = 0;
                    }
                    else {
                        currentLength = n4;
                    }
                    n3 = (0xDC00 | (decodeUtf8_4 & 0x3FF));
                    break;
                }
            }
            if (currentLength >= array.length) {
                array = this._textBuffer.finishCurrentSegment();
                currentLength = 0;
            }
            final int n5 = currentLength + 1;
            array[currentLength] = (char)n3;
            emptyAndGetCurrentSegment = array;
            n = n5;
        }
        this._textBuffer.setCurrentLength(currentLength);
        return JsonToken.VALUE_STRING;
    }
    
    protected JsonToken _handleInvalidNumberStart(int n, final boolean b) throws IOException, JsonParseException {
        int n2;
        while (true) {
            n2 = n;
            if (n != 73) {
                break;
            }
            if (this._inputPtr >= this._inputEnd && !this.loadMore()) {
                this._reportInvalidEOFInValue();
            }
            final byte[] inputBuffer = this._inputBuffer;
            n = this._inputPtr++;
            n = inputBuffer[n];
            String s;
            if (n == 78) {
                if (b) {
                    s = "-INF";
                }
                else {
                    s = "+INF";
                }
            }
            else {
                if (n != 110) {
                    n2 = n;
                    break;
                }
                if (b) {
                    s = "-Infinity";
                }
                else {
                    s = "+Infinity";
                }
            }
            this._matchToken(s, 3);
            if (this.isEnabled(Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
                double n3;
                if (b) {
                    n3 = Double.NEGATIVE_INFINITY;
                }
                else {
                    n3 = Double.POSITIVE_INFINITY;
                }
                return this.resetAsNaN(s, n3);
            }
            this._reportError("Non-standard token '" + s + "': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
        }
        this.reportUnexpectedNumberChar(n2, "expected digit (0-9) to follow minus sign, for valid numeric value");
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
                final byte[] inputBuffer = this._inputBuffer;
                n = this._inputPtr++;
                return this._handleInvalidNumberStart(inputBuffer[n] & 0xFF, false);
            }
        }
        this._reportUnexpectedChar(n, "expected a valid value (number, String, array, object, 'true', 'false' or 'null')");
        return null;
    }
    
    protected Name _handleUnusualFieldName(int n) throws IOException, JsonParseException {
        if (n == 39 && this.isEnabled(Feature.ALLOW_SINGLE_QUOTES)) {
            return this._parseApostropheFieldName();
        }
        if (!this.isEnabled(Feature.ALLOW_UNQUOTED_FIELD_NAMES)) {
            this._reportUnexpectedChar(n, "was expecting double-quote to start field name");
        }
        final int[] inputCodeUtf8JsNames = CharTypes.getInputCodeUtf8JsNames();
        if (inputCodeUtf8JsNames[n] != 0) {
            this._reportUnexpectedChar(n, "was expecting either valid name character (for unquoted name) or double-quote (for quoted) to start field name");
        }
        int[] quadBuffer = this._quadBuffer;
        int n2 = 0;
        int n3 = 0;
        int n4 = n;
        n = 0;
        int n5;
        while (true) {
            if (n2 < 4) {
                n3 = (n4 | n3 << 8);
                n5 = n2 + 1;
            }
            else {
                int[] growArrayBy = quadBuffer;
                if (n >= quadBuffer.length) {
                    growArrayBy = growArrayBy(quadBuffer, quadBuffer.length);
                    this._quadBuffer = growArrayBy;
                }
                growArrayBy[n] = n3;
                quadBuffer = growArrayBy;
                final int n6 = 1;
                n3 = n4;
                ++n;
                n5 = n6;
            }
            if (this._inputPtr >= this._inputEnd && !this.loadMore()) {
                this._reportInvalidEOF(" in field name");
            }
            final int n7 = this._inputBuffer[this._inputPtr] & 0xFF;
            if (inputCodeUtf8JsNames[n7] != 0) {
                break;
            }
            ++this._inputPtr;
            n2 = n5;
            n4 = n7;
        }
        int n8 = n;
        int[] growArrayBy2 = quadBuffer;
        if (n5 > 0) {
            growArrayBy2 = quadBuffer;
            if (n >= quadBuffer.length) {
                growArrayBy2 = growArrayBy(quadBuffer, quadBuffer.length);
                this._quadBuffer = growArrayBy2;
            }
            growArrayBy2[n] = n3;
            n8 = n + 1;
        }
        final Name name = this._symbols.findName(growArrayBy2, n8);
        if (name == null) {
            return this.addName(growArrayBy2, n8, n5);
        }
        return name;
    }
    
    protected void _matchToken(final String s, int n) throws IOException, JsonParseException {
        int n2;
        do {
            if ((this._inputPtr >= this._inputEnd && !this.loadMore()) || this._inputBuffer[this._inputPtr] != s.charAt(n)) {
                this._reportInvalidToken(s.substring(0, n));
            }
            ++this._inputPtr;
            n2 = n + 1;
        } while ((n = n2) < s.length());
        if (this._inputPtr < this._inputEnd || this.loadMore()) {
            n = (this._inputBuffer[this._inputPtr] & 0xFF);
            if (n >= 48 && n != 93 && n != 125 && Character.isJavaIdentifierPart((char)this._decodeCharForError(n))) {
                this._reportInvalidToken(s.substring(0, n2));
            }
        }
    }
    
    protected Name _parseApostropheFieldName() throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd && !this.loadMore()) {
            this._reportInvalidEOF(": was expecting closing ''' for name");
        }
        int i = this._inputBuffer[this._inputPtr++] & 0xFF;
        if (i == 39) {
            return BytesToNameCanonicalizer.getEmptyName();
        }
        int[] quadBuffer = this._quadBuffer;
        final int[] sInputCodesLatin1 = UTF8StreamJsonParser.sInputCodesLatin1;
        int n = 0;
        int n2 = 0;
        int n3 = 0;
    Label_0302_Outer:
        while (i != 39) {
            while (true) {
                int n4 = 0;
                Label_0566: {
                    if ((n4 = i) == 34) {
                        break Label_0566;
                    }
                    n4 = i;
                    if (sInputCodesLatin1[i] == 0) {
                        break Label_0566;
                    }
                    if (i != 92) {
                        this._throwUnquotedSpace(i, "name");
                    }
                    else {
                        i = this._decodeEscaped();
                    }
                    n4 = i;
                    if (i <= 127) {
                        break Label_0566;
                    }
                    if (n >= 4) {
                        int[] growArrayBy = quadBuffer;
                        if (n3 >= quadBuffer.length) {
                            growArrayBy = growArrayBy(quadBuffer, quadBuffer.length);
                            this._quadBuffer = growArrayBy;
                        }
                        growArrayBy[n3] = n2;
                        n = 0;
                        ++n3;
                        n2 = 0;
                        quadBuffer = growArrayBy;
                    }
                    int n5;
                    int n6;
                    if (i < 2048) {
                        n5 = (n2 << 8 | (i >> 6 | 0xC0));
                        n6 = n + 1;
                    }
                    else {
                        int n7 = n2 << 8 | (i >> 12 | 0xE0);
                        int n8 = n + 1;
                        if (n8 >= 4) {
                            int[] growArrayBy2 = quadBuffer;
                            if (n3 >= quadBuffer.length) {
                                growArrayBy2 = growArrayBy(quadBuffer, quadBuffer.length);
                                this._quadBuffer = growArrayBy2;
                            }
                            growArrayBy2[n3] = n7;
                            ++n3;
                            quadBuffer = growArrayBy2;
                            n8 = 0;
                            n7 = 0;
                        }
                        n5 = (n7 << 8 | ((i >> 6 & 0x3F) | 0x80));
                        n6 = n8 + 1;
                    }
                    final int n9 = n5;
                    final int n10 = n6;
                    final int n11 = (i & 0x3F) | 0x80;
                    final int n12 = n10;
                    int n14;
                    int n15;
                    if (n12 < 4) {
                        n2 = (n11 | n9 << 8);
                        final int n13 = n12 + 1;
                        n14 = n3;
                        n15 = n13;
                    }
                    else {
                        int[] growArrayBy3 = quadBuffer;
                        if (n3 >= quadBuffer.length) {
                            growArrayBy3 = growArrayBy(quadBuffer, quadBuffer.length);
                            this._quadBuffer = growArrayBy3;
                        }
                        growArrayBy3[n3] = n9;
                        quadBuffer = growArrayBy3;
                        final boolean b = true;
                        final int n16 = n3 + 1;
                        n15 = (b ? 1 : 0);
                        n2 = n11;
                        n14 = n16;
                    }
                    if (this._inputPtr >= this._inputEnd && !this.loadMore()) {
                        this._reportInvalidEOF(" in field name");
                    }
                    final int n17 = this._inputBuffer[this._inputPtr++] & 0xFF;
                    final int n18 = n15;
                    n3 = n14;
                    n = n18;
                    i = n17;
                    continue Label_0302_Outer;
                }
                final int n12 = n;
                final int n11 = n4;
                final int n9 = n2;
                continue;
            }
        }
        if (n > 0) {
            int[] growArrayBy4 = quadBuffer;
            if (n3 >= quadBuffer.length) {
                growArrayBy4 = growArrayBy(quadBuffer, quadBuffer.length);
                this._quadBuffer = growArrayBy4;
            }
            growArrayBy4[n3] = n2;
            quadBuffer = growArrayBy4;
            ++n3;
        }
        final Name name = this._symbols.findName(quadBuffer, n3);
        if (name == null) {
            return this.addName(quadBuffer, n3, n);
        }
        return name;
    }
    
    protected Name _parseFieldName(int quad1) throws IOException, JsonParseException {
        if (quad1 != 34) {
            return this._handleUnusualFieldName(quad1);
        }
        if (this._inputPtr + 9 > this._inputEnd) {
            return this.slowParseFieldName();
        }
        final byte[] inputBuffer = this._inputBuffer;
        final int[] sInputCodesLatin1 = UTF8StreamJsonParser.sInputCodesLatin1;
        quad1 = this._inputPtr++;
        quad1 = (inputBuffer[quad1] & 0xFF);
        if (sInputCodesLatin1[quad1] == 0) {
            final int n = inputBuffer[this._inputPtr++] & 0xFF;
            if (sInputCodesLatin1[n] == 0) {
                quad1 = (quad1 << 8 | n);
                final int n2 = inputBuffer[this._inputPtr++] & 0xFF;
                if (sInputCodesLatin1[n2] == 0) {
                    quad1 = (quad1 << 8 | n2);
                    final int n3 = inputBuffer[this._inputPtr++] & 0xFF;
                    if (sInputCodesLatin1[n3] == 0) {
                        quad1 = (quad1 << 8 | n3);
                        final int n4 = inputBuffer[this._inputPtr++] & 0xFF;
                        if (sInputCodesLatin1[n4] == 0) {
                            this._quad1 = quad1;
                            return this.parseMediumFieldName(n4, sInputCodesLatin1);
                        }
                        if (n4 == 34) {
                            return this.findName(quad1, 4);
                        }
                        return this.parseFieldName(quad1, n4, 4);
                    }
                    else {
                        if (n3 == 34) {
                            return this.findName(quad1, 3);
                        }
                        return this.parseFieldName(quad1, n3, 3);
                    }
                }
                else {
                    if (n2 == 34) {
                        return this.findName(quad1, 2);
                    }
                    return this.parseFieldName(quad1, n2, 2);
                }
            }
            else {
                if (n == 34) {
                    return this.findName(quad1, 1);
                }
                return this.parseFieldName(quad1, n, 1);
            }
        }
        else {
            if (quad1 == 34) {
                return BytesToNameCanonicalizer.getEmptyName();
            }
            return this.parseFieldName(0, quad1, 0);
        }
    }
    
    @Override
    protected void _releaseBuffers() throws IOException {
        super._releaseBuffers();
        if (this._bufferRecyclable) {
            final byte[] inputBuffer = this._inputBuffer;
            if (inputBuffer != null) {
                this._inputBuffer = null;
                this._ioContext.releaseReadIOBuffer(inputBuffer);
            }
        }
    }
    
    protected void _reportInvalidChar(final int n) throws JsonParseException {
        if (n < 32) {
            this._throwInvalidSpace(n);
        }
        this._reportInvalidInitial(n);
    }
    
    protected void _reportInvalidInitial(final int n) throws JsonParseException {
        this._reportError("Invalid UTF-8 start byte 0x" + Integer.toHexString(n));
    }
    
    protected void _reportInvalidOther(final int n) throws JsonParseException {
        this._reportError("Invalid UTF-8 middle byte 0x" + Integer.toHexString(n));
    }
    
    protected void _reportInvalidOther(final int n, final int inputPtr) throws JsonParseException {
        this._inputPtr = inputPtr;
        this._reportInvalidOther(n);
    }
    
    protected void _reportInvalidToken(final String s) throws IOException, JsonParseException {
        this._reportInvalidToken(s, "'null', 'true', 'false' or NaN");
    }
    
    protected void _reportInvalidToken(final String s, final String s2) throws IOException, JsonParseException {
        final StringBuilder sb = new StringBuilder(s);
        while (this._inputPtr < this._inputEnd || this.loadMore()) {
            final char c = (char)this._decodeCharForError(this._inputBuffer[this._inputPtr++]);
            if (!Character.isJavaIdentifierPart(c)) {
                break;
            }
            sb.append(c);
        }
        this._reportError("Unrecognized token '" + sb.toString() + "': was expecting " + s2);
    }
    
    protected void _skipCR() throws IOException {
        if ((this._inputPtr < this._inputEnd || this.loadMore()) && this._inputBuffer[this._inputPtr] == 10) {
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
        final int[] sInputCodesUtf8 = UTF8StreamJsonParser.sInputCodesUtf8;
        final byte[] inputBuffer = this._inputBuffer;
    Block_4:
        while (true) {
            final int inputPtr = this._inputPtr;
            int n = this._inputEnd;
            int i = inputPtr;
            if (inputPtr >= n) {
                this.loadMoreGuaranteed();
                i = this._inputPtr;
                n = this._inputEnd;
            }
            while (i < n) {
                final int inputPtr2 = i + 1;
                final int n2 = inputBuffer[i] & 0xFF;
                if (sInputCodesUtf8[n2] != 0) {
                    this._inputPtr = inputPtr2;
                    if (n2 == 34) {
                        break Block_4;
                    }
                    switch (sInputCodesUtf8[n2]) {
                        default: {
                            if (n2 < 32) {
                                this._throwUnquotedSpace(n2, "string value");
                                continue Block_4;
                            }
                            this._reportInvalidChar(n2);
                            continue Block_4;
                        }
                        case 1: {
                            this._decodeEscaped();
                            continue Block_4;
                        }
                        case 2: {
                            this._skipUtf8_2(n2);
                            continue Block_4;
                        }
                        case 3: {
                            this._skipUtf8_3(n2);
                            continue Block_4;
                        }
                        case 4: {
                            this._skipUtf8_4(n2);
                            continue Block_4;
                        }
                    }
                }
                else {
                    i = inputPtr2;
                }
            }
            this._inputPtr = i;
        }
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
    
    @Override
    public String getText() throws IOException, JsonParseException {
        if (this._currToken == JsonToken.VALUE_STRING) {
            if (this._tokenIncomplete) {
                this._tokenIncomplete = false;
                this._finishString();
            }
            return this._textBuffer.contentsAsString();
        }
        return this._getText2(this._currToken);
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
        if (this._inputStream != null) {
            final int read = this._inputStream.read(this._inputBuffer, 0, this._inputBuffer.length);
            if (read > 0) {
                this._inputPtr = 0;
                this._inputEnd = read;
                b2 = true;
            }
            else {
                this._closeInput();
                b2 = b;
                if (read == 0) {
                    throw new IOException("InputStream.read() returned 0 characters when trying to read " + this._inputBuffer.length + " bytes");
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
        if (!this._parsingContext.inObject()) {
            return this._nextTokenNotInObject(skipWS);
        }
        this._parsingContext.setCurrentName(this._parseFieldName(skipWS).getName());
        this._currToken = JsonToken.FIELD_NAME;
        final int skipWS2 = this._skipWS();
        if (skipWS2 != 58) {
            this._reportUnexpectedChar(skipWS2, "was expecting a colon to separate field name and value");
        }
        final int skipWS3 = this._skipWS();
        if (skipWS3 == 34) {
            this._tokenIncomplete = true;
            this._nextToken = JsonToken.VALUE_STRING;
            return this._currToken;
        }
        JsonToken nextToken = null;
        switch (skipWS3) {
            default: {
                nextToken = this._handleUnexpectedValue(skipWS3);
                break;
            }
            case 91: {
                nextToken = JsonToken.START_ARRAY;
                break;
            }
            case 123: {
                nextToken = JsonToken.START_OBJECT;
                break;
            }
            case 93:
            case 125: {
                this._reportUnexpectedChar(skipWS3, "expected a value");
            }
            case 116: {
                this._matchToken("true", 1);
                nextToken = JsonToken.VALUE_TRUE;
                break;
            }
            case 102: {
                this._matchToken("false", 1);
                nextToken = JsonToken.VALUE_FALSE;
                break;
            }
            case 110: {
                this._matchToken("null", 1);
                nextToken = JsonToken.VALUE_NULL;
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
                nextToken = this.parseNumberText(skipWS3);
                break;
            }
        }
        this._nextToken = nextToken;
        return this._currToken;
    }
    
    protected Name parseEscapedFieldName(int[] array, int n, int n2, int decodeEscaped, int n3) throws IOException, JsonParseException {
        final int[] sInputCodesLatin1 = UTF8StreamJsonParser.sInputCodesLatin1;
    Label_0245_Outer:
        while (true) {
            int n4 = decodeEscaped;
            while (true) {
                Label_0485: {
                    if (sInputCodesLatin1[decodeEscaped] == 0) {
                        break Label_0485;
                    }
                    if (decodeEscaped == 34) {
                        break;
                    }
                    if (decodeEscaped != 92) {
                        this._throwUnquotedSpace(decodeEscaped, "name");
                    }
                    else {
                        decodeEscaped = this._decodeEscaped();
                    }
                    n4 = decodeEscaped;
                    if (decodeEscaped <= 127) {
                        break Label_0485;
                    }
                    if (n3 >= 4) {
                        int[] growArrayBy = array;
                        if (n >= array.length) {
                            growArrayBy = growArrayBy(array, array.length);
                            this._quadBuffer = growArrayBy;
                        }
                        final int n5 = n + 1;
                        growArrayBy[n] = n2;
                        n3 = 0;
                        n2 = 0;
                        array = growArrayBy;
                        n = n5;
                    }
                    if (decodeEscaped < 2048) {
                        final int n6 = decodeEscaped >> 6 | 0xC0 | n2 << 8;
                        ++n3;
                        n2 = n;
                        n = n6;
                    }
                    else {
                        final int n7 = decodeEscaped >> 12 | 0xE0 | n2 << 8;
                        n2 = n3 + 1;
                        if (n2 >= 4) {
                            int[] growArrayBy2 = array;
                            if (n >= array.length) {
                                growArrayBy2 = growArrayBy(array, array.length);
                                this._quadBuffer = growArrayBy2;
                            }
                            growArrayBy2[n] = n7;
                            ++n;
                            array = growArrayBy2;
                            n2 = 0;
                            n3 = 0;
                        }
                        else {
                            n3 = n7;
                        }
                        final int n8 = n3 << 8 | ((decodeEscaped >> 6 & 0x3F) | 0x80);
                        n3 = n2 + 1;
                        n2 = n;
                        n = n8;
                    }
                    final int n9 = (decodeEscaped & 0x3F) | 0x80;
                    final int n10 = n3;
                    decodeEscaped = n2;
                    n3 = n;
                    n2 = n9;
                    n = decodeEscaped;
                    decodeEscaped = n3;
                    if (n10 < 4) {
                        n3 = n10 + 1;
                        n2 |= decodeEscaped << 8;
                    }
                    else {
                        int[] growArrayBy3 = array;
                        if (n >= array.length) {
                            growArrayBy3 = growArrayBy(array, array.length);
                            this._quadBuffer = growArrayBy3;
                        }
                        growArrayBy3[n] = decodeEscaped;
                        n3 = 1;
                        ++n;
                        array = growArrayBy3;
                    }
                    if (this._inputPtr >= this._inputEnd && !this.loadMore()) {
                        this._reportInvalidEOF(" in field name");
                    }
                    final byte[] inputBuffer = this._inputBuffer;
                    decodeEscaped = this._inputPtr++;
                    decodeEscaped = (inputBuffer[decodeEscaped] & 0xFF);
                    continue Label_0245_Outer;
                }
                decodeEscaped = n2;
                n2 = n4;
                final int n10 = n3;
                continue;
            }
        }
        int[] growArrayBy4 = array;
        decodeEscaped = n;
        if (n3 > 0) {
            growArrayBy4 = array;
            if (n >= array.length) {
                growArrayBy4 = growArrayBy(array, array.length);
                this._quadBuffer = growArrayBy4;
            }
            growArrayBy4[n] = n2;
            decodeEscaped = n + 1;
        }
        Name name;
        if ((name = this._symbols.findName(growArrayBy4, decodeEscaped)) == null) {
            name = this.addName(growArrayBy4, decodeEscaped, n3);
        }
        return name;
    }
    
    protected Name parseLongFieldName(int n) throws IOException, JsonParseException {
        final int[] sInputCodesLatin1 = UTF8StreamJsonParser.sInputCodesLatin1;
        final int n2 = 2;
        int n3 = n;
        n = n2;
        while (this._inputEnd - this._inputPtr >= 4) {
            final int n4 = this._inputBuffer[this._inputPtr++] & 0xFF;
            if (sInputCodesLatin1[n4] != 0) {
                if (n4 == 34) {
                    return this.findName(this._quadBuffer, n, n3, 1);
                }
                return this.parseEscapedFieldName(this._quadBuffer, n, n3, n4, 1);
            }
            else {
                final int n5 = n3 << 8 | n4;
                final int n6 = this._inputBuffer[this._inputPtr++] & 0xFF;
                if (sInputCodesLatin1[n6] != 0) {
                    if (n6 == 34) {
                        return this.findName(this._quadBuffer, n, n5, 2);
                    }
                    return this.parseEscapedFieldName(this._quadBuffer, n, n5, n6, 2);
                }
                else {
                    final int n7 = n5 << 8 | n6;
                    final int n8 = this._inputBuffer[this._inputPtr++] & 0xFF;
                    if (sInputCodesLatin1[n8] != 0) {
                        if (n8 == 34) {
                            return this.findName(this._quadBuffer, n, n7, 3);
                        }
                        return this.parseEscapedFieldName(this._quadBuffer, n, n7, n8, 3);
                    }
                    else {
                        final int n9 = n7 << 8 | n8;
                        n3 = (this._inputBuffer[this._inputPtr++] & 0xFF);
                        if (sInputCodesLatin1[n3] != 0) {
                            if (n3 == 34) {
                                return this.findName(this._quadBuffer, n, n9, 4);
                            }
                            return this.parseEscapedFieldName(this._quadBuffer, n, n9, n3, 4);
                        }
                        else {
                            if (n >= this._quadBuffer.length) {
                                this._quadBuffer = growArrayBy(this._quadBuffer, n);
                            }
                            this._quadBuffer[n] = n9;
                            ++n;
                        }
                    }
                }
            }
        }
        return this.parseEscapedFieldName(this._quadBuffer, n, 0, n3, 0);
    }
    
    protected Name parseMediumFieldName(int n, final int[] array) throws IOException, JsonParseException {
        final int n2 = this._inputBuffer[this._inputPtr++] & 0xFF;
        if (array[n2] != 0) {
            if (n2 == 34) {
                return this.findName(this._quad1, n, 1);
            }
            return this.parseFieldName(this._quad1, n, n2, 1);
        }
        else {
            n = (n2 | n << 8);
            final int n3 = this._inputBuffer[this._inputPtr++] & 0xFF;
            if (array[n3] != 0) {
                if (n3 == 34) {
                    return this.findName(this._quad1, n, 2);
                }
                return this.parseFieldName(this._quad1, n, n3, 2);
            }
            else {
                n = (n << 8 | n3);
                final int n4 = this._inputBuffer[this._inputPtr++] & 0xFF;
                if (array[n4] != 0) {
                    if (n4 == 34) {
                        return this.findName(this._quad1, n, 3);
                    }
                    return this.parseFieldName(this._quad1, n, n4, 3);
                }
                else {
                    n = (n << 8 | n4);
                    final int n5 = this._inputBuffer[this._inputPtr++] & 0xFF;
                    if (array[n5] == 0) {
                        this._quadBuffer[0] = this._quad1;
                        this._quadBuffer[1] = n;
                        return this.parseLongFieldName(n5);
                    }
                    if (n5 == 34) {
                        return this.findName(this._quad1, n, 4);
                    }
                    return this.parseFieldName(this._quad1, n, n5, 4);
                }
            }
        }
    }
    
    protected JsonToken parseNumberText(int currentLength) throws IOException, JsonParseException {
        final int n = 1;
        final char[] emptyAndGetCurrentSegment = this._textBuffer.emptyAndGetCurrentSegment();
        final boolean b = currentLength == 45;
        int n2;
        if (b) {
            emptyAndGetCurrentSegment[0] = '-';
            if (this._inputPtr >= this._inputEnd) {
                this.loadMoreGuaranteed();
            }
            final byte[] inputBuffer = this._inputBuffer;
            currentLength = this._inputPtr++;
            currentLength = (inputBuffer[currentLength] & 0xFF);
            if (currentLength < 48 || currentLength > 57) {
                return this._handleInvalidNumberStart(currentLength, true);
            }
            n2 = 1;
        }
        else {
            n2 = 0;
        }
        int verifyNoLeadingZeroes = currentLength;
        if (currentLength == 48) {
            verifyNoLeadingZeroes = this._verifyNoLeadingZeroes();
        }
        final int n3 = n2 + 1;
        emptyAndGetCurrentSegment[n2] = (char)verifyNoLeadingZeroes;
        int inputEnd = this._inputPtr + emptyAndGetCurrentSegment.length;
        char[] finishCurrentSegment = emptyAndGetCurrentSegment;
        currentLength = n3;
        int n4 = n;
        if (inputEnd > this._inputEnd) {
            inputEnd = this._inputEnd;
            n4 = n;
            currentLength = n3;
            finishCurrentSegment = emptyAndGetCurrentSegment;
        }
        while (this._inputPtr < inputEnd) {
            final int n5 = this._inputBuffer[this._inputPtr++] & 0xFF;
            if (n5 < 48 || n5 > 57) {
                if (n5 == 46 || n5 == 101 || n5 == 69) {
                    return this._parseFloatText(finishCurrentSegment, currentLength, n5, b, n4);
                }
                --this._inputPtr;
                this._textBuffer.setCurrentLength(currentLength);
                return this.resetInt(b, n4);
            }
            else {
                ++n4;
                if (currentLength >= finishCurrentSegment.length) {
                    finishCurrentSegment = this._textBuffer.finishCurrentSegment();
                    currentLength = 0;
                }
                final int n6 = currentLength + 1;
                finishCurrentSegment[currentLength] = (char)n5;
                currentLength = n6;
            }
        }
        return this._parserNumber2(finishCurrentSegment, currentLength, b, n4);
    }
    
    protected Name slowParseFieldName() throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd && !this.loadMore()) {
            this._reportInvalidEOF(": was expecting closing '\"' for name");
        }
        final int n = this._inputBuffer[this._inputPtr++] & 0xFF;
        if (n == 34) {
            return BytesToNameCanonicalizer.getEmptyName();
        }
        return this.parseEscapedFieldName(this._quadBuffer, 0, 0, n, 0);
    }
}
