// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.json;

import java.math.BigInteger;
import java.math.BigDecimal;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.io.NumberOutput;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.io.CharTypes;
import java.io.OutputStream;

public class UTF8JsonGenerator extends JsonGeneratorImpl
{
    private static final byte[] FALSE_BYTES;
    static final byte[] HEX_CHARS;
    private static final byte[] NULL_BYTES;
    private static final byte[] TRUE_BYTES;
    protected boolean _bufferRecyclable;
    protected char[] _charBuffer;
    protected final int _charBufferLength;
    protected byte[] _outputBuffer;
    protected final int _outputEnd;
    protected final int _outputMaxContiguous;
    protected final OutputStream _outputStream;
    protected int _outputTail;
    
    static {
        HEX_CHARS = CharTypes.copyHexBytes();
        NULL_BYTES = new byte[] { 110, 117, 108, 108 };
        TRUE_BYTES = new byte[] { 116, 114, 117, 101 };
        FALSE_BYTES = new byte[] { 102, 97, 108, 115, 101 };
    }
    
    public UTF8JsonGenerator(final IOContext ioContext, final int n, final ObjectCodec objectCodec, final OutputStream outputStream) {
        super(ioContext, n, objectCodec);
        this._outputTail = 0;
        this._outputStream = outputStream;
        this._bufferRecyclable = true;
        this._outputBuffer = ioContext.allocWriteEncodingBuffer();
        this._outputEnd = this._outputBuffer.length;
        this._outputMaxContiguous = this._outputEnd >> 3;
        this._charBuffer = ioContext.allocConcatBuffer();
        this._charBufferLength = this._charBuffer.length;
        if (this.isEnabled(Feature.ESCAPE_NON_ASCII)) {
            this.setHighestNonEscapedChar(127);
        }
    }
    
    private int _handleLongCustomEscape(final byte[] array, int outputTail, final int n, final byte[] array2, final int n2) throws IOException, JsonGenerationException {
        final int length = array2.length;
        Label_0067: {
            if (outputTail + length <= n) {
                break Label_0067;
            }
            this._outputTail = outputTail;
            this._flushBuffer();
            final int outputTail2 = this._outputTail;
            if (length <= array.length) {
                System.arraycopy(array2, 0, array, outputTail2, length);
                outputTail = outputTail2 + length;
                break Label_0067;
            }
            this._outputStream.write(array2, 0, length);
            return outputTail2;
        }
        final int outputTail2 = outputTail;
        if (n2 * 6 + outputTail > n) {
            this._flushBuffer();
            return this._outputTail;
        }
        return outputTail2;
    }
    
    private int _outputMultiByteChar(final int n, int n2) throws IOException {
        final byte[] outputBuffer = this._outputBuffer;
        if (n >= 55296 && n <= 57343) {
            final int n3 = n2 + 1;
            outputBuffer[n2] = 92;
            n2 = n3 + 1;
            outputBuffer[n3] = 117;
            final int n4 = n2 + 1;
            outputBuffer[n2] = UTF8JsonGenerator.HEX_CHARS[n >> 12 & 0xF];
            n2 = n4 + 1;
            outputBuffer[n4] = UTF8JsonGenerator.HEX_CHARS[n >> 8 & 0xF];
            final int n5 = n2 + 1;
            outputBuffer[n2] = UTF8JsonGenerator.HEX_CHARS[n >> 4 & 0xF];
            outputBuffer[n5] = UTF8JsonGenerator.HEX_CHARS[n & 0xF];
            return n5 + 1;
        }
        final int n6 = n2 + 1;
        outputBuffer[n2] = (byte)(n >> 12 | 0xE0);
        n2 = n6 + 1;
        outputBuffer[n6] = (byte)((n >> 6 & 0x3F) | 0x80);
        outputBuffer[n2] = (byte)((n & 0x3F) | 0x80);
        return n2 + 1;
    }
    
    private int _outputRawMultiByteChar(final int n, final char[] array, final int n2, int n3) throws IOException {
        if (n >= 55296 && n <= 57343) {
            if (n2 >= n3) {
                this._reportError("Split surrogate on writeRaw() input (last character)");
            }
            this._outputSurrogates(n, array[n2]);
            return n2 + 1;
        }
        final byte[] outputBuffer = this._outputBuffer;
        n3 = this._outputTail++;
        outputBuffer[n3] = (byte)(n >> 12 | 0xE0);
        n3 = this._outputTail++;
        outputBuffer[n3] = (byte)((n >> 6 & 0x3F) | 0x80);
        n3 = this._outputTail++;
        outputBuffer[n3] = (byte)((n & 0x3F) | 0x80);
        return n2;
    }
    
    private final void _writeBytes(final byte[] array) throws IOException {
        final int length = array.length;
        if (this._outputTail + length > this._outputEnd) {
            this._flushBuffer();
            if (length > 512) {
                this._outputStream.write(array, 0, length);
                return;
            }
        }
        System.arraycopy(array, 0, this._outputBuffer, this._outputTail, length);
        this._outputTail += length;
    }
    
    private int _writeCustomEscape(final byte[] array, final int n, final SerializableString serializableString, final int n2) throws IOException, JsonGenerationException {
        final byte[] unquotedUTF8 = serializableString.asUnquotedUTF8();
        final int length = unquotedUTF8.length;
        if (length > 6) {
            return this._handleLongCustomEscape(array, n, this._outputEnd, unquotedUTF8, n2);
        }
        System.arraycopy(unquotedUTF8, 0, array, n, length);
        return length + n;
    }
    
    private void _writeCustomStringSegment2(final char[] array, int outputTail, final int n) throws IOException, JsonGenerationException {
        if (this._outputTail + (n - outputTail) * 6 > this._outputEnd) {
            this._flushBuffer();
        }
        final int outputTail2 = this._outputTail;
        final byte[] outputBuffer = this._outputBuffer;
        final int[] outputEscapes = this._outputEscapes;
        int maximumNonEscapedChar;
        if (this._maximumNonEscapedChar <= 0) {
            maximumNonEscapedChar = 65535;
        }
        else {
            maximumNonEscapedChar = this._maximumNonEscapedChar;
        }
        final CharacterEscapes characterEscapes = this._characterEscapes;
        int i = outputTail;
        outputTail = outputTail2;
        while (i < n) {
            final int n2 = i + 1;
            final char c = array[i];
            if (c <= '\u007f') {
                if (outputEscapes[c] == 0) {
                    outputBuffer[outputTail] = (byte)c;
                    ++outputTail;
                    i = n2;
                }
                else {
                    final int n3 = outputEscapes[c];
                    if (n3 > 0) {
                        final int n4 = outputTail + 1;
                        outputBuffer[outputTail] = 92;
                        outputTail = n4 + 1;
                        outputBuffer[n4] = (byte)n3;
                        i = n2;
                    }
                    else if (n3 == -2) {
                        final SerializableString escapeSequence = characterEscapes.getEscapeSequence(c);
                        if (escapeSequence == null) {
                            this._reportError("Invalid custom escape definitions; custom escape not found for character code 0x" + Integer.toHexString(c) + ", although was supposed to have one");
                        }
                        outputTail = this._writeCustomEscape(outputBuffer, outputTail, escapeSequence, n - n2);
                        i = n2;
                    }
                    else {
                        outputTail = this._writeGenericEscape(c, outputTail);
                        i = n2;
                    }
                }
            }
            else if (c > maximumNonEscapedChar) {
                outputTail = this._writeGenericEscape(c, outputTail);
                i = n2;
            }
            else {
                final SerializableString escapeSequence2 = characterEscapes.getEscapeSequence(c);
                if (escapeSequence2 != null) {
                    outputTail = this._writeCustomEscape(outputBuffer, outputTail, escapeSequence2, n - n2);
                    i = n2;
                }
                else {
                    if (c <= '\u07ff') {
                        final int n5 = outputTail + 1;
                        outputBuffer[outputTail] = (byte)(c >> 6 | '\u00c0');
                        outputTail = n5 + 1;
                        outputBuffer[n5] = (byte)((c & '?') | '\u0080');
                    }
                    else {
                        outputTail = this._outputMultiByteChar(c, outputTail);
                    }
                    i = n2;
                }
            }
        }
        this._outputTail = outputTail;
    }
    
    private int _writeGenericEscape(int n, int n2) throws IOException {
        final byte[] outputBuffer = this._outputBuffer;
        final int n3 = n2 + 1;
        outputBuffer[n2] = 92;
        n2 = n3 + 1;
        outputBuffer[n3] = 117;
        if (n > 255) {
            final int n4 = n >> 8 & 0xFF;
            final int n5 = n2 + 1;
            outputBuffer[n2] = UTF8JsonGenerator.HEX_CHARS[n4 >> 4];
            n2 = n5 + 1;
            outputBuffer[n5] = UTF8JsonGenerator.HEX_CHARS[n4 & 0xF];
            n &= 0xFF;
        }
        else {
            final int n6 = n2 + 1;
            outputBuffer[n2] = 48;
            n2 = n6 + 1;
            outputBuffer[n6] = 48;
        }
        final int n7 = n2 + 1;
        outputBuffer[n2] = UTF8JsonGenerator.HEX_CHARS[n >> 4];
        outputBuffer[n7] = UTF8JsonGenerator.HEX_CHARS[n & 0xF];
        return n7 + 1;
    }
    
    private void _writeLongString(final String s) throws IOException, JsonGenerationException {
        if (this._outputTail >= this._outputEnd) {
            this._flushBuffer();
        }
        this._outputBuffer[this._outputTail++] = 34;
        this._writeStringSegments(s);
        if (this._outputTail >= this._outputEnd) {
            this._flushBuffer();
        }
        this._outputBuffer[this._outputTail++] = 34;
    }
    
    private void _writeLongString(final char[] array, int n, final int n2) throws IOException, JsonGenerationException {
        if (this._outputTail >= this._outputEnd) {
            this._flushBuffer();
        }
        final byte[] outputBuffer = this._outputBuffer;
        n = this._outputTail++;
        outputBuffer[n] = 34;
        this._writeStringSegments(this._charBuffer, 0, n2);
        if (this._outputTail >= this._outputEnd) {
            this._flushBuffer();
        }
        final byte[] outputBuffer2 = this._outputBuffer;
        n = this._outputTail++;
        outputBuffer2[n] = 34;
    }
    
    private void _writeNull() throws IOException {
        if (this._outputTail + 4 >= this._outputEnd) {
            this._flushBuffer();
        }
        System.arraycopy(UTF8JsonGenerator.NULL_BYTES, 0, this._outputBuffer, this._outputTail, 4);
        this._outputTail += 4;
    }
    
    private void _writeQuotedInt(int n) throws IOException {
        if (this._outputTail + 13 >= this._outputEnd) {
            this._flushBuffer();
        }
        this._outputBuffer[this._outputTail++] = 34;
        this._outputTail = NumberOutput.outputInt(n, this._outputBuffer, this._outputTail);
        final byte[] outputBuffer = this._outputBuffer;
        n = this._outputTail++;
        outputBuffer[n] = 34;
    }
    
    private void _writeQuotedLong(final long n) throws IOException {
        if (this._outputTail + 23 >= this._outputEnd) {
            this._flushBuffer();
        }
        this._outputBuffer[this._outputTail++] = 34;
        this._outputTail = NumberOutput.outputLong(n, this._outputBuffer, this._outputTail);
        this._outputBuffer[this._outputTail++] = 34;
    }
    
    private void _writeQuotedRaw(final Object o) throws IOException {
        if (this._outputTail >= this._outputEnd) {
            this._flushBuffer();
        }
        this._outputBuffer[this._outputTail++] = 34;
        this.writeRaw(o.toString());
        if (this._outputTail >= this._outputEnd) {
            this._flushBuffer();
        }
        this._outputBuffer[this._outputTail++] = 34;
    }
    
    private void _writeQuotedShort(final short n) throws IOException {
        if (this._outputTail + 8 >= this._outputEnd) {
            this._flushBuffer();
        }
        this._outputBuffer[this._outputTail++] = 34;
        this._outputTail = NumberOutput.outputInt(n, this._outputBuffer, this._outputTail);
        this._outputBuffer[this._outputTail++] = 34;
    }
    
    private final void _writeSegmentedRaw(final char[] array, int i, final int n) throws IOException, JsonGenerationException {
        final int outputEnd = this._outputEnd;
        final byte[] outputBuffer = this._outputBuffer;
    Label_0012:
        while (i < n) {
            do {
                final char c = array[i];
                if (c >= '\u0080') {
                    if (this._outputTail + 3 >= this._outputEnd) {
                        this._flushBuffer();
                    }
                    final int n2 = i + 1;
                    i = array[i];
                    if (i < 2048) {
                        outputBuffer[this._outputTail++] = (byte)(i >> 6 | 0xC0);
                        outputBuffer[this._outputTail++] = (byte)((i & 0x3F) | 0x80);
                    }
                    else {
                        this._outputRawMultiByteChar(i, array, n2, n);
                    }
                    i = n2;
                    continue Label_0012;
                }
                if (this._outputTail >= outputEnd) {
                    this._flushBuffer();
                }
                outputBuffer[this._outputTail++] = (byte)c;
            } while (++i < n);
            break;
        }
    }
    
    private final void _writeStringSegment(final char[] array, int outputTail, int i) throws IOException, JsonGenerationException {
        final int n = i + outputTail;
        final int outputTail2 = this._outputTail;
        final byte[] outputBuffer = this._outputBuffer;
        final int[] outputEscapes = this._outputEscapes;
        char c;
        for (i = outputTail, outputTail = outputTail2; i < n; ++i, ++outputTail) {
            c = array[i];
            if (c > '\u007f' || outputEscapes[c] != 0) {
                break;
            }
            outputBuffer[outputTail] = (byte)c;
        }
        this._outputTail = outputTail;
        if (i < n) {
            if (this._characterEscapes != null) {
                this._writeCustomStringSegment2(array, i, n);
            }
            else {
                if (this._maximumNonEscapedChar == 0) {
                    this._writeStringSegment2(array, i, n);
                    return;
                }
                this._writeStringSegmentASCII2(array, i, n);
            }
        }
    }
    
    private final void _writeStringSegment2(final char[] array, int outputTail, final int n) throws IOException, JsonGenerationException {
        if (this._outputTail + (n - outputTail) * 6 > this._outputEnd) {
            this._flushBuffer();
        }
        final int outputTail2 = this._outputTail;
        final byte[] outputBuffer = this._outputBuffer;
        final int[] outputEscapes = this._outputEscapes;
        int i = outputTail;
        outputTail = outputTail2;
        while (i < n) {
            final int n2 = i + 1;
            final char c = array[i];
            if (c <= '\u007f') {
                if (outputEscapes[c] == 0) {
                    outputBuffer[outputTail] = (byte)c;
                    ++outputTail;
                    i = n2;
                }
                else {
                    final int n3 = outputEscapes[c];
                    if (n3 > 0) {
                        final int n4 = outputTail + 1;
                        outputBuffer[outputTail] = 92;
                        outputTail = n4 + 1;
                        outputBuffer[n4] = (byte)n3;
                        i = n2;
                    }
                    else {
                        outputTail = this._writeGenericEscape(c, outputTail);
                        i = n2;
                    }
                }
            }
            else {
                if (c <= '\u07ff') {
                    final int n5 = outputTail + 1;
                    outputBuffer[outputTail] = (byte)(c >> 6 | '\u00c0');
                    outputTail = n5 + 1;
                    outputBuffer[n5] = (byte)((c & '?') | '\u0080');
                }
                else {
                    outputTail = this._outputMultiByteChar(c, outputTail);
                }
                i = n2;
            }
        }
        this._outputTail = outputTail;
    }
    
    private final void _writeStringSegmentASCII2(final char[] array, int outputTail, final int n) throws IOException, JsonGenerationException {
        if (this._outputTail + (n - outputTail) * 6 > this._outputEnd) {
            this._flushBuffer();
        }
        final int outputTail2 = this._outputTail;
        final byte[] outputBuffer = this._outputBuffer;
        final int[] outputEscapes = this._outputEscapes;
        final int maximumNonEscapedChar = this._maximumNonEscapedChar;
        int i = outputTail;
        outputTail = outputTail2;
        while (i < n) {
            final int n2 = i + 1;
            final char c = array[i];
            if (c <= '\u007f') {
                if (outputEscapes[c] == 0) {
                    outputBuffer[outputTail] = (byte)c;
                    ++outputTail;
                    i = n2;
                }
                else {
                    final int n3 = outputEscapes[c];
                    if (n3 > 0) {
                        final int n4 = outputTail + 1;
                        outputBuffer[outputTail] = 92;
                        outputTail = n4 + 1;
                        outputBuffer[n4] = (byte)n3;
                        i = n2;
                    }
                    else {
                        outputTail = this._writeGenericEscape(c, outputTail);
                        i = n2;
                    }
                }
            }
            else if (c > maximumNonEscapedChar) {
                outputTail = this._writeGenericEscape(c, outputTail);
                i = n2;
            }
            else {
                if (c <= '\u07ff') {
                    final int n5 = outputTail + 1;
                    outputBuffer[outputTail] = (byte)(c >> 6 | '\u00c0');
                    outputTail = n5 + 1;
                    outputBuffer[n5] = (byte)((c & '?') | '\u0080');
                }
                else {
                    outputTail = this._outputMultiByteChar(c, outputTail);
                }
                i = n2;
            }
        }
        this._outputTail = outputTail;
    }
    
    private final void _writeStringSegments(final String s) throws IOException, JsonGenerationException {
        int i = s.length();
        final char[] charBuffer = this._charBuffer;
        int n = 0;
        while (i > 0) {
            final int min = Math.min(this._outputMaxContiguous, i);
            s.getChars(n, n + min, charBuffer, 0);
            if (this._outputTail + min > this._outputEnd) {
                this._flushBuffer();
            }
            this._writeStringSegment(charBuffer, 0, min);
            n += min;
            i -= min;
        }
    }
    
    private final void _writeStringSegments(final char[] array, int n, int n2) throws IOException, JsonGenerationException {
        int min;
        do {
            min = Math.min(this._outputMaxContiguous, n2);
            if (this._outputTail + min > this._outputEnd) {
                this._flushBuffer();
            }
            this._writeStringSegment(array, n, min);
            n += min;
        } while ((n2 -= min) > 0);
    }
    
    protected final int _decodeSurrogate(final int n, final int n2) throws IOException {
        if (n2 < 56320 || n2 > 57343) {
            this._reportError("Incomplete surrogate pair: first char 0x" + Integer.toHexString(n) + ", second 0x" + Integer.toHexString(n2));
        }
        return 65536 + (n - 55296 << 10) + (n2 - 56320);
    }
    
    protected final void _flushBuffer() throws IOException {
        final int outputTail = this._outputTail;
        if (outputTail > 0) {
            this._outputTail = 0;
            this._outputStream.write(this._outputBuffer, 0, outputTail);
        }
    }
    
    protected final void _outputSurrogates(int decodeSurrogate, int n) throws IOException {
        decodeSurrogate = this._decodeSurrogate(decodeSurrogate, n);
        if (this._outputTail + 4 > this._outputEnd) {
            this._flushBuffer();
        }
        final byte[] outputBuffer = this._outputBuffer;
        n = this._outputTail++;
        outputBuffer[n] = (byte)(decodeSurrogate >> 18 | 0xF0);
        n = this._outputTail++;
        outputBuffer[n] = (byte)((decodeSurrogate >> 12 & 0x3F) | 0x80);
        n = this._outputTail++;
        outputBuffer[n] = (byte)((decodeSurrogate >> 6 & 0x3F) | 0x80);
        n = this._outputTail++;
        outputBuffer[n] = (byte)((decodeSurrogate & 0x3F) | 0x80);
    }
    
    @Override
    protected void _releaseBuffers() {
        final byte[] outputBuffer = this._outputBuffer;
        if (outputBuffer != null && this._bufferRecyclable) {
            this._outputBuffer = null;
            this._ioContext.releaseWriteEncodingBuffer(outputBuffer);
        }
        final char[] charBuffer = this._charBuffer;
        if (charBuffer != null) {
            this._charBuffer = null;
            this._ioContext.releaseConcatBuffer(charBuffer);
        }
    }
    
    protected final void _verifyPrettyValueWrite(final String s, final int n) throws IOException, JsonGenerationException {
        switch (n) {
            default: {
                this._throwInternal();
                break;
            }
            case 1: {
                this._cfgPrettyPrinter.writeArrayValueSeparator(this);
            }
            case 2: {
                this._cfgPrettyPrinter.writeObjectFieldValueSeparator(this);
            }
            case 3: {
                this._cfgPrettyPrinter.writeRootValueSeparator(this);
            }
            case 0: {
                if (this._writeContext.inArray()) {
                    this._cfgPrettyPrinter.beforeArrayValues(this);
                    return;
                }
                if (this._writeContext.inObject()) {
                    this._cfgPrettyPrinter.beforeObjectEntries(this);
                    return;
                }
                break;
            }
        }
    }
    
    @Override
    protected final void _verifyValueWrite(final String s) throws IOException, JsonGenerationException {
        final int writeValue = this._writeContext.writeValue();
        if (writeValue == 5) {
            this._reportError("Can not " + s + ", expecting field name");
        }
        if (this._cfgPrettyPrinter == null) {
            byte b = 0;
            Label_0080: {
                switch (writeValue) {
                    case 1: {
                        b = 44;
                        break Label_0080;
                    }
                    case 2: {
                        b = 58;
                        break Label_0080;
                    }
                    case 3: {
                        if (this._rootValueSeparator == null) {
                            break;
                        }
                        final byte[] unquotedUTF8 = this._rootValueSeparator.asUnquotedUTF8();
                        if (unquotedUTF8.length > 0) {
                            this._writeBytes(unquotedUTF8);
                            return;
                        }
                        break;
                    }
                }
                return;
            }
            if (this._outputTail >= this._outputEnd) {
                this._flushBuffer();
            }
            this._outputBuffer[this._outputTail] = b;
            ++this._outputTail;
            return;
        }
        this._verifyPrettyValueWrite(s, writeValue);
    }
    
    protected void _writeBinary(final Base64Variant base64Variant, final byte[] array, int n, int n2) throws IOException, JsonGenerationException {
        final int n3 = this._outputEnd - 6;
        final int n4 = base64Variant.getMaxLineLength() >> 2;
        int i = n;
        n = n4;
        while (i <= n2 - 3) {
            if (this._outputTail > n3) {
                this._flushBuffer();
            }
            final int n5 = i + 1;
            final byte b = array[i];
            final int n6 = n5 + 1;
            final byte b2 = array[n5];
            final int n7 = n6 + 1;
            this._outputTail = base64Variant.encodeBase64Chunk(((b2 & 0xFF) | b << 8) << 8 | (array[n6] & 0xFF), this._outputBuffer, this._outputTail);
            final int n8 = --n;
            i = n7;
            if (n8 <= 0) {
                final byte[] outputBuffer = this._outputBuffer;
                n = this._outputTail++;
                outputBuffer[n] = 92;
                final byte[] outputBuffer2 = this._outputBuffer;
                n = this._outputTail++;
                outputBuffer2[n] = 110;
                n = base64Variant.getMaxLineLength() >> 2;
                i = n7;
            }
        }
        final int n9 = n2 - i;
        if (n9 > 0) {
            if (this._outputTail > n3) {
                this._flushBuffer();
            }
            final int n10 = i + 1;
            n2 = (n = array[i] << 16);
            if (n9 == 2) {
                n = (n2 | (array[n10] & 0xFF) << 8);
            }
            this._outputTail = base64Variant.encodeBase64Partial(n, n9, this._outputBuffer, this._outputTail);
        }
    }
    
    protected final void _writeFieldName(final SerializableString serializableString) throws IOException, JsonGenerationException {
        if (this.isEnabled(Feature.QUOTE_FIELD_NAMES)) {
            if (this._outputTail >= this._outputEnd) {
                this._flushBuffer();
            }
            this._outputBuffer[this._outputTail++] = 34;
            final int appendQuotedUTF8 = serializableString.appendQuotedUTF8(this._outputBuffer, this._outputTail);
            if (appendQuotedUTF8 < 0) {
                this._writeBytes(serializableString.asQuotedUTF8());
            }
            else {
                this._outputTail += appendQuotedUTF8;
            }
            if (this._outputTail >= this._outputEnd) {
                this._flushBuffer();
            }
            this._outputBuffer[this._outputTail++] = 34;
            return;
        }
        final int appendQuotedUTF9 = serializableString.appendQuotedUTF8(this._outputBuffer, this._outputTail);
        if (appendQuotedUTF9 < 0) {
            this._writeBytes(serializableString.asQuotedUTF8());
            return;
        }
        this._outputTail += appendQuotedUTF9;
    }
    
    protected final void _writeFieldName(final String s) throws IOException, JsonGenerationException {
        if (!this.isEnabled(Feature.QUOTE_FIELD_NAMES)) {
            this._writeStringSegments(s);
            return;
        }
        if (this._outputTail >= this._outputEnd) {
            this._flushBuffer();
        }
        this._outputBuffer[this._outputTail++] = 34;
        final int length = s.length();
        if (length <= this._charBufferLength) {
            s.getChars(0, length, this._charBuffer, 0);
            if (length <= this._outputMaxContiguous) {
                if (this._outputTail + length > this._outputEnd) {
                    this._flushBuffer();
                }
                this._writeStringSegment(this._charBuffer, 0, length);
            }
            else {
                this._writeStringSegments(this._charBuffer, 0, length);
            }
        }
        else {
            this._writeStringSegments(s);
        }
        if (this._outputTail >= this._outputEnd) {
            this._flushBuffer();
        }
        this._outputBuffer[this._outputTail++] = 34;
    }
    
    protected final void _writePPFieldName(final SerializableString serializableString, final boolean b) throws IOException, JsonGenerationException {
        if (b) {
            this._cfgPrettyPrinter.writeObjectEntrySeparator(this);
        }
        else {
            this._cfgPrettyPrinter.beforeObjectEntries(this);
        }
        final boolean enabled = this.isEnabled(Feature.QUOTE_FIELD_NAMES);
        if (enabled) {
            if (this._outputTail >= this._outputEnd) {
                this._flushBuffer();
            }
            this._outputBuffer[this._outputTail++] = 34;
        }
        this._writeBytes(serializableString.asQuotedUTF8());
        if (enabled) {
            if (this._outputTail >= this._outputEnd) {
                this._flushBuffer();
            }
            this._outputBuffer[this._outputTail++] = 34;
        }
    }
    
    protected final void _writePPFieldName(final String s, final boolean b) throws IOException, JsonGenerationException {
        if (b) {
            this._cfgPrettyPrinter.writeObjectEntrySeparator(this);
        }
        else {
            this._cfgPrettyPrinter.beforeObjectEntries(this);
        }
        if (this.isEnabled(Feature.QUOTE_FIELD_NAMES)) {
            if (this._outputTail >= this._outputEnd) {
                this._flushBuffer();
            }
            this._outputBuffer[this._outputTail++] = 34;
            final int length = s.length();
            if (length <= this._charBufferLength) {
                s.getChars(0, length, this._charBuffer, 0);
                if (length <= this._outputMaxContiguous) {
                    if (this._outputTail + length > this._outputEnd) {
                        this._flushBuffer();
                    }
                    this._writeStringSegment(this._charBuffer, 0, length);
                }
                else {
                    this._writeStringSegments(this._charBuffer, 0, length);
                }
            }
            else {
                this._writeStringSegments(s);
            }
            if (this._outputTail >= this._outputEnd) {
                this._flushBuffer();
            }
            this._outputBuffer[this._outputTail++] = 34;
            return;
        }
        this._writeStringSegments(s);
    }
    
    @Override
    public void close() throws IOException {
        super.close();
        if (this._outputBuffer != null && this.isEnabled(Feature.AUTO_CLOSE_JSON_CONTENT)) {
            while (true) {
                final JsonWriteContext outputContext = this.getOutputContext();
                if (outputContext.inArray()) {
                    this.writeEndArray();
                }
                else {
                    if (!outputContext.inObject()) {
                        break;
                    }
                    this.writeEndObject();
                }
            }
        }
        this._flushBuffer();
        if (this._outputStream != null) {
            if (this._ioContext.isResourceManaged() || this.isEnabled(Feature.AUTO_CLOSE_TARGET)) {
                this._outputStream.close();
            }
            else if (this.isEnabled(Feature.FLUSH_PASSED_TO_STREAM)) {
                this._outputStream.flush();
            }
        }
        this._releaseBuffers();
    }
    
    @Override
    public final void flush() throws IOException {
        this._flushBuffer();
        if (this._outputStream != null && this.isEnabled(Feature.FLUSH_PASSED_TO_STREAM)) {
            this._outputStream.flush();
        }
    }
    
    @Override
    public void writeBinary(final Base64Variant base64Variant, final byte[] array, int n, final int n2) throws IOException, JsonGenerationException {
        this._verifyValueWrite("write binary value");
        if (this._outputTail >= this._outputEnd) {
            this._flushBuffer();
        }
        this._outputBuffer[this._outputTail++] = 34;
        this._writeBinary(base64Variant, array, n, n + n2);
        if (this._outputTail >= this._outputEnd) {
            this._flushBuffer();
        }
        final byte[] outputBuffer = this._outputBuffer;
        n = this._outputTail++;
        outputBuffer[n] = 34;
    }
    
    @Override
    public void writeBoolean(final boolean b) throws IOException, JsonGenerationException {
        this._verifyValueWrite("write boolean value");
        if (this._outputTail + 5 >= this._outputEnd) {
            this._flushBuffer();
        }
        byte[] array;
        if (b) {
            array = UTF8JsonGenerator.TRUE_BYTES;
        }
        else {
            array = UTF8JsonGenerator.FALSE_BYTES;
        }
        final int length = array.length;
        System.arraycopy(array, 0, this._outputBuffer, this._outputTail, length);
        this._outputTail += length;
    }
    
    @Override
    public final void writeEndArray() throws IOException, JsonGenerationException {
        if (!this._writeContext.inArray()) {
            this._reportError("Current context not an ARRAY but " + this._writeContext.getTypeDesc());
        }
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeEndArray(this, this._writeContext.getEntryCount());
        }
        else {
            if (this._outputTail >= this._outputEnd) {
                this._flushBuffer();
            }
            this._outputBuffer[this._outputTail++] = 93;
        }
        this._writeContext = this._writeContext.getParent();
    }
    
    @Override
    public final void writeEndObject() throws IOException, JsonGenerationException {
        if (!this._writeContext.inObject()) {
            this._reportError("Current context not an object but " + this._writeContext.getTypeDesc());
        }
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeEndObject(this, this._writeContext.getEntryCount());
        }
        else {
            if (this._outputTail >= this._outputEnd) {
                this._flushBuffer();
            }
            this._outputBuffer[this._outputTail++] = 125;
        }
        this._writeContext = this._writeContext.getParent();
    }
    
    @Override
    public final void writeFieldName(final SerializableString serializableString) throws IOException, JsonGenerationException {
        boolean b = true;
        final int writeFieldName = this._writeContext.writeFieldName(serializableString.getValue());
        if (writeFieldName == 4) {
            this._reportError("Can not write a field name, expecting a value");
        }
        if (this._cfgPrettyPrinter != null) {
            if (writeFieldName != 1) {
                b = false;
            }
            this._writePPFieldName(serializableString, b);
            return;
        }
        if (writeFieldName == 1) {
            if (this._outputTail >= this._outputEnd) {
                this._flushBuffer();
            }
            this._outputBuffer[this._outputTail++] = 44;
        }
        this._writeFieldName(serializableString);
    }
    
    @Override
    public final void writeFieldName(final String s) throws IOException, JsonGenerationException {
        boolean b = true;
        final int writeFieldName = this._writeContext.writeFieldName(s);
        if (writeFieldName == 4) {
            this._reportError("Can not write a field name, expecting a value");
        }
        if (this._cfgPrettyPrinter != null) {
            if (writeFieldName != 1) {
                b = false;
            }
            this._writePPFieldName(s, b);
            return;
        }
        if (writeFieldName == 1) {
            if (this._outputTail >= this._outputEnd) {
                this._flushBuffer();
            }
            this._outputBuffer[this._outputTail++] = 44;
        }
        this._writeFieldName(s);
    }
    
    @Override
    public void writeNull() throws IOException, JsonGenerationException {
        this._verifyValueWrite("write null value");
        this._writeNull();
    }
    
    @Override
    public void writeNumber(final double n) throws IOException, JsonGenerationException {
        if (this._cfgNumbersAsStrings || ((Double.isNaN(n) || Double.isInfinite(n)) && this.isEnabled(Feature.QUOTE_NON_NUMERIC_NUMBERS))) {
            this.writeString(String.valueOf(n));
            return;
        }
        this._verifyValueWrite("write number");
        this.writeRaw(String.valueOf(n));
    }
    
    @Override
    public void writeNumber(final float n) throws IOException, JsonGenerationException {
        if (this._cfgNumbersAsStrings || ((Float.isNaN(n) || Float.isInfinite(n)) && this.isEnabled(Feature.QUOTE_NON_NUMERIC_NUMBERS))) {
            this.writeString(String.valueOf(n));
            return;
        }
        this._verifyValueWrite("write number");
        this.writeRaw(String.valueOf(n));
    }
    
    @Override
    public void writeNumber(final int n) throws IOException, JsonGenerationException {
        this._verifyValueWrite("write number");
        if (this._outputTail + 11 >= this._outputEnd) {
            this._flushBuffer();
        }
        if (this._cfgNumbersAsStrings) {
            this._writeQuotedInt(n);
            return;
        }
        this._outputTail = NumberOutput.outputInt(n, this._outputBuffer, this._outputTail);
    }
    
    @Override
    public void writeNumber(final long n) throws IOException, JsonGenerationException {
        this._verifyValueWrite("write number");
        if (this._cfgNumbersAsStrings) {
            this._writeQuotedLong(n);
            return;
        }
        if (this._outputTail + 21 >= this._outputEnd) {
            this._flushBuffer();
        }
        this._outputTail = NumberOutput.outputLong(n, this._outputBuffer, this._outputTail);
    }
    
    @Override
    public void writeNumber(final String s) throws IOException, JsonGenerationException {
        this._verifyValueWrite("write number");
        if (this._cfgNumbersAsStrings) {
            this._writeQuotedRaw(s);
            return;
        }
        this.writeRaw(s);
    }
    
    @Override
    public void writeNumber(final BigDecimal bigDecimal) throws IOException, JsonGenerationException {
        this._verifyValueWrite("write number");
        if (bigDecimal == null) {
            this._writeNull();
            return;
        }
        if (this._cfgNumbersAsStrings) {
            this._writeQuotedRaw(bigDecimal);
            return;
        }
        this.writeRaw(bigDecimal.toString());
    }
    
    @Override
    public void writeNumber(final BigInteger bigInteger) throws IOException, JsonGenerationException {
        this._verifyValueWrite("write number");
        if (bigInteger == null) {
            this._writeNull();
            return;
        }
        if (this._cfgNumbersAsStrings) {
            this._writeQuotedRaw(bigInteger);
            return;
        }
        this.writeRaw(bigInteger.toString());
    }
    
    @Override
    public void writeNumber(final short n) throws IOException, JsonGenerationException {
        this._verifyValueWrite("write number");
        if (this._outputTail + 6 >= this._outputEnd) {
            this._flushBuffer();
        }
        if (this._cfgNumbersAsStrings) {
            this._writeQuotedShort(n);
            return;
        }
        this._outputTail = NumberOutput.outputInt(n, this._outputBuffer, this._outputTail);
    }
    
    @Override
    public void writeRaw(final char c) throws IOException, JsonGenerationException {
        if (this._outputTail + 3 >= this._outputEnd) {
            this._flushBuffer();
        }
        final byte[] outputBuffer = this._outputBuffer;
        if (c <= '\u007f') {
            outputBuffer[this._outputTail++] = (byte)c;
            return;
        }
        if (c < '\u0800') {
            outputBuffer[this._outputTail++] = (byte)(c >> 6 | '\u00c0');
            outputBuffer[this._outputTail++] = (byte)((c & '?') | '\u0080');
            return;
        }
        this._outputRawMultiByteChar(c, null, 0, 0);
    }
    
    @Override
    public void writeRaw(final SerializableString serializableString) throws IOException, JsonGenerationException {
        final byte[] unquotedUTF8 = serializableString.asUnquotedUTF8();
        if (unquotedUTF8.length > 0) {
            this._writeBytes(unquotedUTF8);
        }
    }
    
    @Override
    public void writeRaw(final String s) throws IOException, JsonGenerationException {
        int i = s.length();
        int n = 0;
        while (i > 0) {
            final char[] charBuffer = this._charBuffer;
            int length;
            if (i < (length = charBuffer.length)) {
                length = i;
            }
            s.getChars(n, n + length, charBuffer, 0);
            this.writeRaw(charBuffer, 0, length);
            n += length;
            i -= length;
        }
    }
    
    @Override
    public final void writeRaw(final char[] array, int i, int n) throws IOException, JsonGenerationException {
        final int n2 = n + n + n;
        Label_0042: {
            if (this._outputTail + n2 <= this._outputEnd) {
                break Label_0042;
            }
            if (this._outputEnd >= n2) {
                this._flushBuffer();
                break Label_0042;
            }
            this._writeSegmentedRaw(array, i, n);
            return;
        }
        final int n3 = n + i;
    Label_0047:
        while (i < n3) {
            do {
                n = array[i];
                if (n > 127) {
                    n = i + 1;
                    i = array[i];
                    if (i < 2048) {
                        this._outputBuffer[this._outputTail++] = (byte)(i >> 6 | 0xC0);
                        this._outputBuffer[this._outputTail++] = (byte)((i & 0x3F) | 0x80);
                    }
                    else {
                        this._outputRawMultiByteChar(i, array, n, n3);
                    }
                    i = n;
                    continue Label_0047;
                }
                this._outputBuffer[this._outputTail++] = (byte)n;
                n = i + 1;
            } while ((i = n) < n3);
        }
    }
    
    @Override
    public final void writeStartArray() throws IOException, JsonGenerationException {
        this._verifyValueWrite("start an array");
        this._writeContext = this._writeContext.createChildArrayContext();
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeStartArray(this);
            return;
        }
        if (this._outputTail >= this._outputEnd) {
            this._flushBuffer();
        }
        this._outputBuffer[this._outputTail++] = 91;
    }
    
    @Override
    public final void writeStartObject() throws IOException, JsonGenerationException {
        this._verifyValueWrite("start an object");
        this._writeContext = this._writeContext.createChildObjectContext();
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeStartObject(this);
            return;
        }
        if (this._outputTail >= this._outputEnd) {
            this._flushBuffer();
        }
        this._outputBuffer[this._outputTail++] = 123;
    }
    
    @Override
    public final void writeString(final SerializableString serializableString) throws IOException, JsonGenerationException {
        this._verifyValueWrite("write text value");
        if (this._outputTail >= this._outputEnd) {
            this._flushBuffer();
        }
        this._outputBuffer[this._outputTail++] = 34;
        final int appendQuotedUTF8 = serializableString.appendQuotedUTF8(this._outputBuffer, this._outputTail);
        if (appendQuotedUTF8 < 0) {
            this._writeBytes(serializableString.asQuotedUTF8());
        }
        else {
            this._outputTail += appendQuotedUTF8;
        }
        if (this._outputTail >= this._outputEnd) {
            this._flushBuffer();
        }
        this._outputBuffer[this._outputTail++] = 34;
    }
    
    @Override
    public void writeString(final String s) throws IOException, JsonGenerationException {
        this._verifyValueWrite("write text value");
        if (s == null) {
            this._writeNull();
            return;
        }
        final int length = s.length();
        if (length > this._charBufferLength) {
            this._writeLongString(s);
            return;
        }
        s.getChars(0, length, this._charBuffer, 0);
        if (length > this._outputMaxContiguous) {
            this._writeLongString(this._charBuffer, 0, length);
            return;
        }
        if (this._outputTail + length >= this._outputEnd) {
            this._flushBuffer();
        }
        this._outputBuffer[this._outputTail++] = 34;
        this._writeStringSegment(this._charBuffer, 0, length);
        if (this._outputTail >= this._outputEnd) {
            this._flushBuffer();
        }
        this._outputBuffer[this._outputTail++] = 34;
    }
    
    @Override
    public void writeString(final char[] array, int n, final int n2) throws IOException, JsonGenerationException {
        this._verifyValueWrite("write text value");
        if (this._outputTail >= this._outputEnd) {
            this._flushBuffer();
        }
        this._outputBuffer[this._outputTail++] = 34;
        if (n2 <= this._outputMaxContiguous) {
            if (this._outputTail + n2 > this._outputEnd) {
                this._flushBuffer();
            }
            this._writeStringSegment(array, n, n2);
        }
        else {
            this._writeStringSegments(array, n, n2);
        }
        if (this._outputTail >= this._outputEnd) {
            this._flushBuffer();
        }
        final byte[] outputBuffer = this._outputBuffer;
        n = this._outputTail++;
        outputBuffer[n] = 34;
    }
}
