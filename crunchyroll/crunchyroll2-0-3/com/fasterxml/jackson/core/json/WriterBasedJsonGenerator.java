// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.json;

import java.math.BigInteger;
import java.math.BigDecimal;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.NumberOutput;
import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.io.CharTypes;
import java.io.Writer;
import com.fasterxml.jackson.core.SerializableString;

public final class WriterBasedJsonGenerator extends JsonGeneratorImpl
{
    protected static final char[] HEX_CHARS;
    protected SerializableString _currentEscape;
    protected char[] _entityBuffer;
    protected char[] _outputBuffer;
    protected int _outputEnd;
    protected int _outputHead;
    protected int _outputTail;
    protected final Writer _writer;
    
    static {
        HEX_CHARS = CharTypes.copyHexChars();
    }
    
    public WriterBasedJsonGenerator(final IOContext ioContext, final int n, final ObjectCodec objectCodec, final Writer writer) {
        super(ioContext, n, objectCodec);
        this._outputHead = 0;
        this._outputTail = 0;
        this._writer = writer;
        this._outputBuffer = ioContext.allocConcatBuffer();
        this._outputEnd = this._outputBuffer.length;
    }
    
    private char[] _allocateEntityBuffer() {
        return this._entityBuffer = new char[] { '\\', '\0', '\\', 'u', '0', '0', '\0', '\0', '\\', 'u', '\0', '\0', '\0', '\0' };
    }
    
    private void _appendCharacterEscape(char c, int outputTail) throws IOException, JsonGenerationException {
        if (outputTail >= 0) {
            if (this._outputTail + 2 > this._outputEnd) {
                this._flushBuffer();
            }
            this._outputBuffer[this._outputTail++] = '\\';
            this._outputBuffer[this._outputTail++] = (char)outputTail;
            return;
        }
        if (outputTail != -2) {
            if (this._outputTail + 2 > this._outputEnd) {
                this._flushBuffer();
            }
            outputTail = this._outputTail;
            final char[] outputBuffer = this._outputBuffer;
            final int n = outputTail + 1;
            outputBuffer[outputTail] = '\\';
            outputTail = n + 1;
            outputBuffer[n] = 'u';
            if (c > '\u00ff') {
                final char c2 = (char)(c >> 8 & '\u00ff');
                final int n2 = outputTail + 1;
                outputBuffer[outputTail] = WriterBasedJsonGenerator.HEX_CHARS[c2 >> 4];
                outputTail = n2 + 1;
                outputBuffer[n2] = WriterBasedJsonGenerator.HEX_CHARS[c2 & '\u000f'];
                c &= '\u00ff';
            }
            else {
                final int n3 = outputTail + 1;
                outputBuffer[outputTail] = '0';
                outputTail = n3 + 1;
                outputBuffer[n3] = '0';
            }
            final int outputTail2 = outputTail + 1;
            outputBuffer[outputTail] = WriterBasedJsonGenerator.HEX_CHARS[c >> 4];
            outputBuffer[outputTail2] = WriterBasedJsonGenerator.HEX_CHARS[c & '\u000f'];
            this._outputTail = outputTail2;
            return;
        }
        String s;
        if (this._currentEscape == null) {
            s = this._characterEscapes.getEscapeSequence(c).getValue();
        }
        else {
            s = this._currentEscape.getValue();
            this._currentEscape = null;
        }
        final int length = s.length();
        if (this._outputTail + length > this._outputEnd) {
            this._flushBuffer();
            if (length > this._outputEnd) {
                this._writer.write(s);
                return;
            }
        }
        s.getChars(0, length, this._outputBuffer, this._outputTail);
        this._outputTail += length;
    }
    
    private int _prependOrWriteCharacterEscape(char[] array, int n, int n2, char c, int n3) throws IOException, JsonGenerationException {
        if (n3 >= 0) {
            if (n > 1 && n < n2) {
                n -= 2;
                array[n] = '\\';
                array[n + 1] = (char)n3;
                return n;
            }
            if ((array = this._entityBuffer) == null) {
                array = this._allocateEntityBuffer();
            }
            array[1] = (char)n3;
            this._writer.write(array, 0, 2);
            return n;
        }
        else if (n3 != -2) {
            if (n > 5 && n < n2) {
                n -= 6;
                n2 = n + 1;
                array[n] = '\\';
                n = n2 + 1;
                array[n2] = 'u';
                if (c > '\u00ff') {
                    n2 = (c >> 8 & '\u00ff');
                    n3 = n + 1;
                    array[n] = WriterBasedJsonGenerator.HEX_CHARS[n2 >> 4];
                    n = n3 + 1;
                    array[n3] = WriterBasedJsonGenerator.HEX_CHARS[n2 & 0xF];
                    c &= '\u00ff';
                }
                else {
                    n2 = n + 1;
                    array[n] = '0';
                    n = n2 + 1;
                    array[n2] = '0';
                }
                n2 = n + 1;
                array[n] = WriterBasedJsonGenerator.HEX_CHARS[c >> 4];
                array[n2] = WriterBasedJsonGenerator.HEX_CHARS[c & '\u000f'];
                return n2 - 5;
            }
            if ((array = this._entityBuffer) == null) {
                array = this._allocateEntityBuffer();
            }
            this._outputHead = this._outputTail;
            if (c > '\u00ff') {
                n2 = (c >> 8 & '\u00ff');
                final char c2 = (char)(c & '\u00ff');
                array[10] = WriterBasedJsonGenerator.HEX_CHARS[n2 >> 4];
                array[11] = WriterBasedJsonGenerator.HEX_CHARS[n2 & 0xF];
                array[12] = WriterBasedJsonGenerator.HEX_CHARS[c2 >> 4];
                array[13] = WriterBasedJsonGenerator.HEX_CHARS[c2 & '\u000f'];
                this._writer.write(array, 8, 6);
                return n;
            }
            array[6] = WriterBasedJsonGenerator.HEX_CHARS[c >> 4];
            array[7] = WriterBasedJsonGenerator.HEX_CHARS[c & '\u000f'];
            this._writer.write(array, 2, 6);
            return n;
        }
        else {
            String s;
            if (this._currentEscape == null) {
                s = this._characterEscapes.getEscapeSequence(c).getValue();
            }
            else {
                s = this._currentEscape.getValue();
                this._currentEscape = null;
            }
            final int length = s.length();
            if (n >= length && n < n2) {
                n -= length;
                s.getChars(0, length, array, n);
                return n;
            }
            this._writer.write(s);
            return n;
        }
    }
    
    private void _prependOrWriteCharacterEscape(char c, int n) throws IOException, JsonGenerationException {
        if (n >= 0) {
            if (this._outputTail >= 2) {
                final int outputHead = this._outputTail - 2;
                this._outputHead = outputHead;
                this._outputBuffer[outputHead] = '\\';
                this._outputBuffer[outputHead + 1] = (char)n;
                return;
            }
            char[] array;
            if ((array = this._entityBuffer) == null) {
                array = this._allocateEntityBuffer();
            }
            this._outputHead = this._outputTail;
            array[1] = (char)n;
            this._writer.write(array, 0, 2);
        }
        else if (n != -2) {
            if (this._outputTail >= 6) {
                final char[] outputBuffer = this._outputBuffer;
                n = this._outputTail - 6;
                outputBuffer[this._outputHead = n] = '\\';
                ++n;
                outputBuffer[n] = 'u';
                if (c > '\u00ff') {
                    final char c2 = (char)(c >> 8 & '\u00ff');
                    ++n;
                    outputBuffer[n] = WriterBasedJsonGenerator.HEX_CHARS[c2 >> 4];
                    ++n;
                    outputBuffer[n] = WriterBasedJsonGenerator.HEX_CHARS[c2 & '\u000f'];
                    c &= '\u00ff';
                }
                else {
                    ++n;
                    outputBuffer[n] = '0';
                    ++n;
                    outputBuffer[n] = '0';
                }
                ++n;
                outputBuffer[n] = WriterBasedJsonGenerator.HEX_CHARS[c >> 4];
                outputBuffer[n + 1] = WriterBasedJsonGenerator.HEX_CHARS[c & '\u000f'];
                return;
            }
            char[] array2;
            if ((array2 = this._entityBuffer) == null) {
                array2 = this._allocateEntityBuffer();
            }
            this._outputHead = this._outputTail;
            if (c > '\u00ff') {
                n = (c >> 8 & '\u00ff');
                final char c3 = (char)(c & '\u00ff');
                array2[10] = WriterBasedJsonGenerator.HEX_CHARS[n >> 4];
                array2[11] = WriterBasedJsonGenerator.HEX_CHARS[n & 0xF];
                array2[12] = WriterBasedJsonGenerator.HEX_CHARS[c3 >> 4];
                array2[13] = WriterBasedJsonGenerator.HEX_CHARS[c3 & '\u000f'];
                this._writer.write(array2, 8, 6);
                return;
            }
            array2[6] = WriterBasedJsonGenerator.HEX_CHARS[c >> 4];
            array2[7] = WriterBasedJsonGenerator.HEX_CHARS[c & '\u000f'];
            this._writer.write(array2, 2, 6);
        }
        else {
            String s;
            if (this._currentEscape == null) {
                s = this._characterEscapes.getEscapeSequence(c).getValue();
            }
            else {
                s = this._currentEscape.getValue();
                this._currentEscape = null;
            }
            final int length = s.length();
            if (this._outputTail >= length) {
                n = this._outputTail - length;
                this._outputHead = n;
                s.getChars(0, length, this._outputBuffer, n);
                return;
            }
            this._outputHead = this._outputTail;
            this._writer.write(s);
        }
    }
    
    private void _writeLongString(final String s) throws IOException, JsonGenerationException {
        this._flushBuffer();
        final int length = s.length();
        int n = 0;
        int outputEnd;
        do {
            if (n + (outputEnd = this._outputEnd) > length) {
                outputEnd = length - n;
            }
            s.getChars(n, n + outputEnd, this._outputBuffer, 0);
            if (this._characterEscapes != null) {
                this._writeSegmentCustom(outputEnd);
            }
            else if (this._maximumNonEscapedChar != 0) {
                this._writeSegmentASCII(outputEnd, this._maximumNonEscapedChar);
            }
            else {
                this._writeSegment(outputEnd);
            }
        } while ((n += outputEnd) < length);
    }
    
    private void _writeNull() throws IOException {
        if (this._outputTail + 4 >= this._outputEnd) {
            this._flushBuffer();
        }
        final int outputTail = this._outputTail;
        final char[] outputBuffer = this._outputBuffer;
        outputBuffer[outputTail] = 'n';
        final int n = outputTail + 1;
        outputBuffer[n] = 'u';
        final int n2 = n + 1;
        outputBuffer[n2] = 'l';
        final int n3 = n2 + 1;
        outputBuffer[n3] = 'l';
        this._outputTail = n3 + 1;
    }
    
    private void _writeQuotedInt(int n) throws IOException {
        if (this._outputTail + 13 >= this._outputEnd) {
            this._flushBuffer();
        }
        this._outputBuffer[this._outputTail++] = '\"';
        this._outputTail = NumberOutput.outputInt(n, this._outputBuffer, this._outputTail);
        final char[] outputBuffer = this._outputBuffer;
        n = this._outputTail++;
        outputBuffer[n] = '\"';
    }
    
    private void _writeQuotedLong(final long n) throws IOException {
        if (this._outputTail + 23 >= this._outputEnd) {
            this._flushBuffer();
        }
        this._outputBuffer[this._outputTail++] = '\"';
        this._outputTail = NumberOutput.outputLong(n, this._outputBuffer, this._outputTail);
        this._outputBuffer[this._outputTail++] = '\"';
    }
    
    private void _writeQuotedRaw(final Object o) throws IOException {
        if (this._outputTail >= this._outputEnd) {
            this._flushBuffer();
        }
        this._outputBuffer[this._outputTail++] = '\"';
        this.writeRaw(o.toString());
        if (this._outputTail >= this._outputEnd) {
            this._flushBuffer();
        }
        this._outputBuffer[this._outputTail++] = '\"';
    }
    
    private void _writeQuotedShort(final short n) throws IOException {
        if (this._outputTail + 8 >= this._outputEnd) {
            this._flushBuffer();
        }
        this._outputBuffer[this._outputTail++] = '\"';
        this._outputTail = NumberOutput.outputInt(n, this._outputBuffer, this._outputTail);
        this._outputBuffer[this._outputTail++] = '\"';
    }
    
    private void _writeSegment(final int n) throws IOException, JsonGenerationException {
        final int[] outputEscapes = this._outputEscapes;
        final int length = outputEscapes.length;
        int i = 0;
        int prependOrWriteCharacterEscape = 0;
    Label_0072:
        while (i < n) {
            while (true) {
                int n2;
                do {
                    final char c = this._outputBuffer[i];
                    if (c < length && outputEscapes[c] != 0) {
                        final int n3 = i - prependOrWriteCharacterEscape;
                        if (n3 > 0) {
                            this._writer.write(this._outputBuffer, prependOrWriteCharacterEscape, n3);
                            if (i >= n) {
                                break Label_0072;
                            }
                        }
                        ++i;
                        prependOrWriteCharacterEscape = this._prependOrWriteCharacterEscape(this._outputBuffer, i, n, c, outputEscapes[c]);
                        continue Label_0072;
                    }
                    n2 = i + 1;
                } while ((i = n2) < n);
                i = n2;
                continue;
            }
        }
    }
    
    private void _writeSegmentASCII(final int n, final int n2) throws IOException, JsonGenerationException {
        int n3 = 0;
        final int[] outputEscapes = this._outputEscapes;
        final int min = Math.min(outputEscapes.length, n2 + 1);
        int prependOrWriteCharacterEscape = 0;
        int i = 0;
    Label_0061_Outer:
        while (i < n) {
            int n4 = n3;
        Label_0061:
            while (true) {
                int n5;
                do {
                    final char c = this._outputBuffer[i];
                    if (c < min) {
                        n3 = outputEscapes[c];
                        if (n3 != 0) {
                            break Label_0061;
                        }
                    }
                    else {
                        n3 = n4;
                        if (c > n2) {
                            n3 = -1;
                            break Label_0061;
                        }
                    }
                    n5 = i + 1;
                    n4 = n3;
                    continue Label_0061_Outer;
                    final int n6 = i - prependOrWriteCharacterEscape;
                    if (n6 > 0) {
                        this._writer.write(this._outputBuffer, prependOrWriteCharacterEscape, n6);
                        if (i >= n) {
                            break Label_0061_Outer;
                        }
                    }
                    ++i;
                    prependOrWriteCharacterEscape = this._prependOrWriteCharacterEscape(this._outputBuffer, i, n, c, n3);
                    continue Label_0061_Outer;
                } while ((i = n5) < n);
                i = n5;
                continue Label_0061;
            }
        }
    }
    
    private void _writeSegmentCustom(final int n) throws IOException, JsonGenerationException {
        int i = 0;
        final int[] outputEscapes = this._outputEscapes;
        int maximumNonEscapedChar;
        if (this._maximumNonEscapedChar < 1) {
            maximumNonEscapedChar = 65535;
        }
        else {
            maximumNonEscapedChar = this._maximumNonEscapedChar;
        }
        final int min = Math.min(outputEscapes.length, maximumNonEscapedChar + '\u0001');
        final CharacterEscapes characterEscapes = this._characterEscapes;
        int prependOrWriteCharacterEscape = 0;
        int n2 = 0;
    Label_0076_Outer:
        while (i < n) {
            int n3 = n2;
        Label_0076:
            while (true) {
                int n4;
                do {
                    final char c = this._outputBuffer[i];
                    if (c < min) {
                        n2 = outputEscapes[c];
                        if (n2 != 0) {
                            break Label_0076;
                        }
                    }
                    else {
                        if (c > maximumNonEscapedChar) {
                            n2 = -1;
                            break Label_0076;
                        }
                        final SerializableString escapeSequence = characterEscapes.getEscapeSequence(c);
                        this._currentEscape = escapeSequence;
                        n2 = n3;
                        if (escapeSequence != null) {
                            n2 = -2;
                            break Label_0076;
                        }
                    }
                    n4 = i + 1;
                    n3 = n2;
                    continue Label_0076_Outer;
                    final int n5 = i - prependOrWriteCharacterEscape;
                    if (n5 > 0) {
                        this._writer.write(this._outputBuffer, prependOrWriteCharacterEscape, n5);
                        if (i >= n) {
                            break Label_0076_Outer;
                        }
                    }
                    ++i;
                    prependOrWriteCharacterEscape = this._prependOrWriteCharacterEscape(this._outputBuffer, i, n, c, n2);
                    continue Label_0076_Outer;
                } while ((i = n4) < n);
                i = n4;
                continue Label_0076;
            }
        }
    }
    
    private void _writeString(final String s) throws IOException, JsonGenerationException {
        final int length = s.length();
        if (length > this._outputEnd) {
            this._writeLongString(s);
            return;
        }
        if (this._outputTail + length > this._outputEnd) {
            this._flushBuffer();
        }
        s.getChars(0, length, this._outputBuffer, this._outputTail);
        if (this._characterEscapes != null) {
            this._writeStringCustom(length);
            return;
        }
        if (this._maximumNonEscapedChar != 0) {
            this._writeStringASCII(length, this._maximumNonEscapedChar);
            return;
        }
        this._writeString2(length);
    }
    
    private void _writeString(final char[] array, int i, int n) throws IOException, JsonGenerationException {
        Label_0014: {
            if (this._characterEscapes != null) {
                this._writeStringCustom(array, i, n);
            }
            else {
                if (this._maximumNonEscapedChar != 0) {
                    this._writeStringASCII(array, i, n, this._maximumNonEscapedChar);
                    return;
                }
                final int n2 = n + i;
                final int[] outputEscapes = this._outputEscapes;
                final int length = outputEscapes.length;
            Label_0050:
                while (i < n2) {
                    n = i;
                    while (true) {
                        int n3;
                        do {
                            final char c = array[n];
                            if (c < length && outputEscapes[c] != 0) {
                                final int n4 = n - i;
                                if (n4 < 32) {
                                    if (this._outputTail + n4 > this._outputEnd) {
                                        this._flushBuffer();
                                    }
                                    if (n4 > 0) {
                                        System.arraycopy(array, i, this._outputBuffer, this._outputTail, n4);
                                        this._outputTail += n4;
                                    }
                                }
                                else {
                                    this._flushBuffer();
                                    this._writer.write(array, i, n4);
                                }
                                if (n < n2) {
                                    i = n + 1;
                                    final char c2 = array[n];
                                    this._appendCharacterEscape(c2, outputEscapes[c2]);
                                    continue Label_0050;
                                }
                                break Label_0014;
                            }
                            else {
                                n3 = n + 1;
                            }
                        } while ((n = n3) < n2);
                        n = n3;
                        continue;
                    }
                }
            }
        }
    }
    
    private void _writeString2(int n) throws IOException, JsonGenerationException {
        n += this._outputTail;
        final int[] outputEscapes = this._outputEscapes;
        final int length = outputEscapes.length;
    Label_0017:
        while (this._outputTail < n) {
            do {
                final char c = this._outputBuffer[this._outputTail];
                if (c < length && outputEscapes[c] != 0) {
                    final int n2 = this._outputTail - this._outputHead;
                    if (n2 > 0) {
                        this._writer.write(this._outputBuffer, this._outputHead, n2);
                    }
                    final char c2 = this._outputBuffer[this._outputTail++];
                    this._prependOrWriteCharacterEscape(c2, outputEscapes[c2]);
                    continue Label_0017;
                }
            } while (++this._outputTail < n);
            break;
        }
    }
    
    private void _writeStringASCII(int outputTail, final int n) throws IOException, JsonGenerationException {
        final int n2 = this._outputTail + outputTail;
        final int[] outputEscapes = this._outputEscapes;
        final int min = Math.min(outputEscapes.length, n + 1);
    Label_0025:
        while (this._outputTail < n2) {
            do {
                final char c = this._outputBuffer[this._outputTail];
                Label_0059: {
                    if (c < min) {
                        outputTail = outputEscapes[c];
                        if (outputTail != 0) {
                            break Label_0059;
                        }
                    }
                    else if (c > n) {
                        outputTail = -1;
                        break Label_0059;
                    }
                    outputTail = this._outputTail + 1;
                    continue;
                }
                final int n3 = this._outputTail - this._outputHead;
                if (n3 > 0) {
                    this._writer.write(this._outputBuffer, this._outputHead, n3);
                }
                ++this._outputTail;
                this._prependOrWriteCharacterEscape(c, outputTail);
                continue Label_0025;
            } while ((this._outputTail = outputTail) < n2);
            break;
        }
    }
    
    private void _writeStringASCII(final char[] array, int n, int i, final int n2) throws IOException, JsonGenerationException {
        final int n3 = i + n;
        final int[] outputEscapes = this._outputEscapes;
        final int min = Math.min(outputEscapes.length, n2 + 1);
        final int n4 = 0;
        i = n;
        n = n4;
    Label_0074_Outer:
        while (i < n3) {
            int n5 = i;
            int n6 = n;
        Label_0074:
            while (true) {
                int n7;
                do {
                    final char c = array[n5];
                    if (c < min) {
                        final int n8 = outputEscapes[c];
                        if ((n = n8) != 0) {
                            n = n8;
                            break Label_0074;
                        }
                    }
                    else {
                        n = n6;
                        if (c > n2) {
                            n = -1;
                            break Label_0074;
                        }
                    }
                    n7 = n5 + 1;
                    n6 = n;
                    continue Label_0074_Outer;
                    final int n9 = n5 - i;
                    if (n9 < 32) {
                        if (this._outputTail + n9 > this._outputEnd) {
                            this._flushBuffer();
                        }
                        if (n9 > 0) {
                            System.arraycopy(array, i, this._outputBuffer, this._outputTail, n9);
                            this._outputTail += n9;
                        }
                    }
                    else {
                        this._flushBuffer();
                        this._writer.write(array, i, n9);
                    }
                    if (n5 >= n3) {
                        break Label_0074_Outer;
                    }
                    i = n5 + 1;
                    this._appendCharacterEscape(c, n);
                    continue Label_0074_Outer;
                } while ((n5 = n7) < n3);
                n5 = n7;
                continue Label_0074;
            }
        }
    }
    
    private void _writeStringCustom(int outputTail) throws IOException, JsonGenerationException {
        final int n = this._outputTail + outputTail;
        final int[] outputEscapes = this._outputEscapes;
        int maximumNonEscapedChar;
        if (this._maximumNonEscapedChar < 1) {
            maximumNonEscapedChar = 65535;
        }
        else {
            maximumNonEscapedChar = this._maximumNonEscapedChar;
        }
        final int min = Math.min(outputEscapes.length, maximumNonEscapedChar + '\u0001');
        final CharacterEscapes characterEscapes = this._characterEscapes;
    Label_0042:
        while (this._outputTail < n) {
            do {
                final char c = this._outputBuffer[this._outputTail];
                Label_0076: {
                    if (c < min) {
                        outputTail = outputEscapes[c];
                        if (outputTail != 0) {
                            break Label_0076;
                        }
                    }
                    else {
                        if (c > maximumNonEscapedChar) {
                            outputTail = -1;
                            break Label_0076;
                        }
                        if ((this._currentEscape = characterEscapes.getEscapeSequence(c)) != null) {
                            outputTail = -2;
                            break Label_0076;
                        }
                    }
                    outputTail = this._outputTail + 1;
                    continue;
                }
                final int n2 = this._outputTail - this._outputHead;
                if (n2 > 0) {
                    this._writer.write(this._outputBuffer, this._outputHead, n2);
                }
                ++this._outputTail;
                this._prependOrWriteCharacterEscape(c, outputTail);
                continue Label_0042;
            } while ((this._outputTail = outputTail) < n);
            break;
        }
    }
    
    private void _writeStringCustom(final char[] array, int n, int i) throws IOException, JsonGenerationException {
        final int n2 = i + n;
        final int[] outputEscapes = this._outputEscapes;
        int maximumNonEscapedChar;
        if (this._maximumNonEscapedChar < 1) {
            maximumNonEscapedChar = 65535;
        }
        else {
            maximumNonEscapedChar = this._maximumNonEscapedChar;
        }
        final int min = Math.min(outputEscapes.length, maximumNonEscapedChar + '\u0001');
        final CharacterEscapes characterEscapes = this._characterEscapes;
        final int n3 = 0;
        i = n;
        n = n3;
    Label_0092_Outer:
        while (i < n2) {
            int n4 = i;
            int n5 = n;
        Label_0092:
            while (true) {
                int n6;
                do {
                    final char c = array[n4];
                    if (c < min) {
                        final int n7 = outputEscapes[c];
                        if ((n = n7) != 0) {
                            n = n7;
                            break Label_0092;
                        }
                    }
                    else {
                        if (c > maximumNonEscapedChar) {
                            n = -1;
                            break Label_0092;
                        }
                        final SerializableString escapeSequence = characterEscapes.getEscapeSequence(c);
                        this._currentEscape = escapeSequence;
                        n = n5;
                        if (escapeSequence != null) {
                            n = -2;
                            break Label_0092;
                        }
                    }
                    n6 = n4 + 1;
                    n5 = n;
                    continue Label_0092_Outer;
                    final int n8 = n4 - i;
                    if (n8 < 32) {
                        if (this._outputTail + n8 > this._outputEnd) {
                            this._flushBuffer();
                        }
                        if (n8 > 0) {
                            System.arraycopy(array, i, this._outputBuffer, this._outputTail, n8);
                            this._outputTail += n8;
                        }
                    }
                    else {
                        this._flushBuffer();
                        this._writer.write(array, i, n8);
                    }
                    if (n4 >= n2) {
                        break Label_0092_Outer;
                    }
                    i = n4 + 1;
                    this._appendCharacterEscape(c, n);
                    continue Label_0092_Outer;
                } while ((n4 = n6) < n2);
                n4 = n6;
                continue Label_0092;
            }
        }
    }
    
    private void writeRawLong(final String s) throws IOException, JsonGenerationException {
        int n = this._outputEnd - this._outputTail;
        s.getChars(0, n, this._outputBuffer, this._outputTail);
        this._outputTail += n;
        this._flushBuffer();
        int i;
        int outputEnd;
        for (i = s.length() - n; i > this._outputEnd; i -= outputEnd) {
            outputEnd = this._outputEnd;
            s.getChars(n, n + outputEnd, this._outputBuffer, 0);
            this._outputHead = 0;
            this._outputTail = outputEnd;
            this._flushBuffer();
            n += outputEnd;
        }
        s.getChars(n, n + i, this._outputBuffer, 0);
        this._outputHead = 0;
        this._outputTail = i;
    }
    
    protected void _flushBuffer() throws IOException {
        final int n = this._outputTail - this._outputHead;
        if (n > 0) {
            final int outputHead = this._outputHead;
            this._outputHead = 0;
            this._outputTail = 0;
            this._writer.write(this._outputBuffer, outputHead, n);
        }
    }
    
    @Override
    protected void _releaseBuffers() {
        final char[] outputBuffer = this._outputBuffer;
        if (outputBuffer != null) {
            this._outputBuffer = null;
            this._ioContext.releaseConcatBuffer(outputBuffer);
        }
    }
    
    protected void _verifyPrettyValueWrite(final String s, final int n) throws IOException, JsonGenerationException {
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
    protected void _verifyValueWrite(final String s) throws IOException, JsonGenerationException {
        final int writeValue = this._writeContext.writeValue();
        if (writeValue == 5) {
            this._reportError("Can not " + s + ", expecting field name");
        }
        if (this._cfgPrettyPrinter == null) {
            char c = '\0';
            Label_0080: {
                switch (writeValue) {
                    case 1: {
                        c = ',';
                        break Label_0080;
                    }
                    case 2: {
                        c = ':';
                        break Label_0080;
                    }
                    case 3: {
                        if (this._rootValueSeparator != null) {
                            this.writeRaw(this._rootValueSeparator.getValue());
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
            this._outputBuffer[this._outputTail] = c;
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
                final char[] outputBuffer = this._outputBuffer;
                n = this._outputTail++;
                outputBuffer[n] = '\\';
                final char[] outputBuffer2 = this._outputBuffer;
                n = this._outputTail++;
                outputBuffer2[n] = 'n';
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
    
    public void _writeFieldName(final SerializableString serializableString, final boolean b) throws IOException, JsonGenerationException {
        if (this._cfgPrettyPrinter != null) {
            this._writePPFieldName(serializableString, b);
            return;
        }
        if (this._outputTail + 1 >= this._outputEnd) {
            this._flushBuffer();
        }
        if (b) {
            this._outputBuffer[this._outputTail++] = ',';
        }
        final char[] quotedChars = serializableString.asQuotedChars();
        if (!this.isEnabled(Feature.QUOTE_FIELD_NAMES)) {
            this.writeRaw(quotedChars, 0, quotedChars.length);
            return;
        }
        this._outputBuffer[this._outputTail++] = '\"';
        final int length = quotedChars.length;
        if (this._outputTail + length + 1 >= this._outputEnd) {
            this.writeRaw(quotedChars, 0, length);
            if (this._outputTail >= this._outputEnd) {
                this._flushBuffer();
            }
            this._outputBuffer[this._outputTail++] = '\"';
            return;
        }
        System.arraycopy(quotedChars, 0, this._outputBuffer, this._outputTail, length);
        this._outputTail += length;
        this._outputBuffer[this._outputTail++] = '\"';
    }
    
    protected void _writeFieldName(final String s, final boolean b) throws IOException, JsonGenerationException {
        if (this._cfgPrettyPrinter != null) {
            this._writePPFieldName(s, b);
            return;
        }
        if (this._outputTail + 1 >= this._outputEnd) {
            this._flushBuffer();
        }
        if (b) {
            this._outputBuffer[this._outputTail++] = ',';
        }
        if (!this.isEnabled(Feature.QUOTE_FIELD_NAMES)) {
            this._writeString(s);
            return;
        }
        this._outputBuffer[this._outputTail++] = '\"';
        this._writeString(s);
        if (this._outputTail >= this._outputEnd) {
            this._flushBuffer();
        }
        this._outputBuffer[this._outputTail++] = '\"';
    }
    
    protected void _writePPFieldName(final SerializableString serializableString, final boolean b) throws IOException, JsonGenerationException {
        if (b) {
            this._cfgPrettyPrinter.writeObjectEntrySeparator(this);
        }
        else {
            this._cfgPrettyPrinter.beforeObjectEntries(this);
        }
        final char[] quotedChars = serializableString.asQuotedChars();
        if (this.isEnabled(Feature.QUOTE_FIELD_NAMES)) {
            if (this._outputTail >= this._outputEnd) {
                this._flushBuffer();
            }
            this._outputBuffer[this._outputTail++] = '\"';
            this.writeRaw(quotedChars, 0, quotedChars.length);
            if (this._outputTail >= this._outputEnd) {
                this._flushBuffer();
            }
            this._outputBuffer[this._outputTail++] = '\"';
            return;
        }
        this.writeRaw(quotedChars, 0, quotedChars.length);
    }
    
    protected void _writePPFieldName(final String s, final boolean b) throws IOException, JsonGenerationException {
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
            this._outputBuffer[this._outputTail++] = '\"';
            this._writeString(s);
            if (this._outputTail >= this._outputEnd) {
                this._flushBuffer();
            }
            this._outputBuffer[this._outputTail++] = '\"';
            return;
        }
        this._writeString(s);
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
        if (this._writer != null) {
            if (this._ioContext.isResourceManaged() || this.isEnabled(Feature.AUTO_CLOSE_TARGET)) {
                this._writer.close();
            }
            else if (this.isEnabled(Feature.FLUSH_PASSED_TO_STREAM)) {
                this._writer.flush();
            }
        }
        this._releaseBuffers();
    }
    
    @Override
    public void flush() throws IOException {
        this._flushBuffer();
        if (this._writer != null && this.isEnabled(Feature.FLUSH_PASSED_TO_STREAM)) {
            this._writer.flush();
        }
    }
    
    @Override
    public void writeBinary(final Base64Variant base64Variant, final byte[] array, int n, final int n2) throws IOException, JsonGenerationException {
        this._verifyValueWrite("write binary value");
        if (this._outputTail >= this._outputEnd) {
            this._flushBuffer();
        }
        this._outputBuffer[this._outputTail++] = '\"';
        this._writeBinary(base64Variant, array, n, n + n2);
        if (this._outputTail >= this._outputEnd) {
            this._flushBuffer();
        }
        final char[] outputBuffer = this._outputBuffer;
        n = this._outputTail++;
        outputBuffer[n] = '\"';
    }
    
    @Override
    public void writeBoolean(final boolean b) throws IOException, JsonGenerationException {
        this._verifyValueWrite("write boolean value");
        if (this._outputTail + 5 >= this._outputEnd) {
            this._flushBuffer();
        }
        final int outputTail = this._outputTail;
        final char[] outputBuffer = this._outputBuffer;
        int n3;
        if (b) {
            outputBuffer[outputTail] = 't';
            final int n = outputTail + 1;
            outputBuffer[n] = 'r';
            final int n2 = n + 1;
            outputBuffer[n2] = 'u';
            n3 = n2 + 1;
            outputBuffer[n3] = 'e';
        }
        else {
            outputBuffer[outputTail] = 'f';
            final int n4 = outputTail + 1;
            outputBuffer[n4] = 'a';
            final int n5 = n4 + 1;
            outputBuffer[n5] = 'l';
            final int n6 = n5 + 1;
            outputBuffer[n6] = 's';
            n3 = n6 + 1;
            outputBuffer[n3] = 'e';
        }
        this._outputTail = n3 + 1;
    }
    
    @Override
    public void writeEndArray() throws IOException, JsonGenerationException {
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
            this._outputBuffer[this._outputTail++] = ']';
        }
        this._writeContext = this._writeContext.getParent();
    }
    
    @Override
    public void writeEndObject() throws IOException, JsonGenerationException {
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
            this._outputBuffer[this._outputTail++] = '}';
        }
        this._writeContext = this._writeContext.getParent();
    }
    
    @Override
    public void writeFieldName(final SerializableString serializableString) throws IOException, JsonGenerationException {
        boolean b = true;
        final int writeFieldName = this._writeContext.writeFieldName(serializableString.getValue());
        if (writeFieldName == 4) {
            this._reportError("Can not write a field name, expecting a value");
        }
        if (writeFieldName != 1) {
            b = false;
        }
        this._writeFieldName(serializableString, b);
    }
    
    @Override
    public void writeFieldName(final String s) throws IOException, JsonGenerationException {
        boolean b = true;
        final int writeFieldName = this._writeContext.writeFieldName(s);
        if (writeFieldName == 4) {
            this._reportError("Can not write a field name, expecting a value");
        }
        if (writeFieldName != 1) {
            b = false;
        }
        this._writeFieldName(s, b);
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
        if (this._cfgNumbersAsStrings) {
            this._writeQuotedInt(n);
            return;
        }
        if (this._outputTail + 11 >= this._outputEnd) {
            this._flushBuffer();
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
        if (this._cfgNumbersAsStrings) {
            this._writeQuotedShort(n);
            return;
        }
        if (this._outputTail + 6 >= this._outputEnd) {
            this._flushBuffer();
        }
        this._outputTail = NumberOutput.outputInt(n, this._outputBuffer, this._outputTail);
    }
    
    @Override
    public void writeRaw(final char c) throws IOException, JsonGenerationException {
        if (this._outputTail >= this._outputEnd) {
            this._flushBuffer();
        }
        this._outputBuffer[this._outputTail++] = c;
    }
    
    @Override
    public void writeRaw(final SerializableString serializableString) throws IOException, JsonGenerationException {
        this.writeRaw(serializableString.getValue());
    }
    
    @Override
    public void writeRaw(final String s) throws IOException, JsonGenerationException {
        final int length = s.length();
        int n;
        if ((n = this._outputEnd - this._outputTail) == 0) {
            this._flushBuffer();
            n = this._outputEnd - this._outputTail;
        }
        if (n >= length) {
            s.getChars(0, length, this._outputBuffer, this._outputTail);
            this._outputTail += length;
            return;
        }
        this.writeRawLong(s);
    }
    
    @Override
    public void writeRaw(final char[] array, final int n, final int n2) throws IOException, JsonGenerationException {
        if (n2 < 32) {
            if (n2 > this._outputEnd - this._outputTail) {
                this._flushBuffer();
            }
            System.arraycopy(array, n, this._outputBuffer, this._outputTail, n2);
            this._outputTail += n2;
            return;
        }
        this._flushBuffer();
        this._writer.write(array, n, n2);
    }
    
    @Override
    public void writeStartArray() throws IOException, JsonGenerationException {
        this._verifyValueWrite("start an array");
        this._writeContext = this._writeContext.createChildArrayContext();
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeStartArray(this);
            return;
        }
        if (this._outputTail >= this._outputEnd) {
            this._flushBuffer();
        }
        this._outputBuffer[this._outputTail++] = '[';
    }
    
    @Override
    public void writeStartObject() throws IOException, JsonGenerationException {
        this._verifyValueWrite("start an object");
        this._writeContext = this._writeContext.createChildObjectContext();
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeStartObject(this);
            return;
        }
        if (this._outputTail >= this._outputEnd) {
            this._flushBuffer();
        }
        this._outputBuffer[this._outputTail++] = '{';
    }
    
    @Override
    public void writeString(final SerializableString serializableString) throws IOException, JsonGenerationException {
        this._verifyValueWrite("write text value");
        if (this._outputTail >= this._outputEnd) {
            this._flushBuffer();
        }
        this._outputBuffer[this._outputTail++] = '\"';
        final char[] quotedChars = serializableString.asQuotedChars();
        final int length = quotedChars.length;
        if (length < 32) {
            if (length > this._outputEnd - this._outputTail) {
                this._flushBuffer();
            }
            System.arraycopy(quotedChars, 0, this._outputBuffer, this._outputTail, length);
            this._outputTail += length;
        }
        else {
            this._flushBuffer();
            this._writer.write(quotedChars, 0, length);
        }
        if (this._outputTail >= this._outputEnd) {
            this._flushBuffer();
        }
        this._outputBuffer[this._outputTail++] = '\"';
    }
    
    @Override
    public void writeString(final String s) throws IOException, JsonGenerationException {
        this._verifyValueWrite("write text value");
        if (s == null) {
            this._writeNull();
            return;
        }
        if (this._outputTail >= this._outputEnd) {
            this._flushBuffer();
        }
        this._outputBuffer[this._outputTail++] = '\"';
        this._writeString(s);
        if (this._outputTail >= this._outputEnd) {
            this._flushBuffer();
        }
        this._outputBuffer[this._outputTail++] = '\"';
    }
    
    @Override
    public void writeString(char[] outputBuffer, int n, final int n2) throws IOException, JsonGenerationException {
        this._verifyValueWrite("write text value");
        if (this._outputTail >= this._outputEnd) {
            this._flushBuffer();
        }
        this._outputBuffer[this._outputTail++] = '\"';
        this._writeString(outputBuffer, n, n2);
        if (this._outputTail >= this._outputEnd) {
            this._flushBuffer();
        }
        outputBuffer = this._outputBuffer;
        n = this._outputTail++;
        outputBuffer[n] = '\"';
    }
}
