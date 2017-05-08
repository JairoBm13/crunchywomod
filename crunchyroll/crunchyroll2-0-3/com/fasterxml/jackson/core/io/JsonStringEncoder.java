// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import java.lang.ref.SoftReference;

public final class JsonStringEncoder
{
    private static final byte[] HEX_BYTES;
    private static final char[] HEX_CHARS;
    protected static final ThreadLocal<SoftReference<JsonStringEncoder>> _threadEncoder;
    protected ByteArrayBuilder _byteBuilder;
    protected final char[] _quoteBuffer;
    protected TextBuffer _textBuffer;
    
    static {
        HEX_CHARS = CharTypes.copyHexChars();
        HEX_BYTES = CharTypes.copyHexBytes();
        _threadEncoder = new ThreadLocal<SoftReference<JsonStringEncoder>>();
    }
    
    public JsonStringEncoder() {
        (this._quoteBuffer = new char[6])[0] = '\\';
        this._quoteBuffer[2] = '0';
        this._quoteBuffer[3] = '0';
    }
    
    private int _appendByteEscape(int n, int n2, final ByteArrayBuilder byteArrayBuilder, final int currentSegmentLength) {
        byteArrayBuilder.setCurrentSegmentLength(currentSegmentLength);
        byteArrayBuilder.append(92);
        if (n2 < 0) {
            byteArrayBuilder.append(117);
            if (n > 255) {
                n2 = n >> 8;
                byteArrayBuilder.append(JsonStringEncoder.HEX_BYTES[n2 >> 4]);
                byteArrayBuilder.append(JsonStringEncoder.HEX_BYTES[n2 & 0xF]);
                n &= 0xFF;
            }
            else {
                byteArrayBuilder.append(48);
                byteArrayBuilder.append(48);
            }
            byteArrayBuilder.append(JsonStringEncoder.HEX_BYTES[n >> 4]);
            byteArrayBuilder.append(JsonStringEncoder.HEX_BYTES[n & 0xF]);
        }
        else {
            byteArrayBuilder.append((byte)n2);
        }
        return byteArrayBuilder.getCurrentSegmentLength();
    }
    
    private int _appendNamedEscape(final int n, final char[] array) {
        array[1] = (char)n;
        return 2;
    }
    
    private int _appendNumericEscape(final int n, final char[] array) {
        array[1] = 'u';
        array[4] = JsonStringEncoder.HEX_CHARS[n >> 4];
        array[5] = JsonStringEncoder.HEX_CHARS[n & 0xF];
        return 6;
    }
    
    protected static int _convertSurrogate(final int n, final int n2) {
        if (n2 < 56320 || n2 > 57343) {
            throw new IllegalArgumentException("Broken surrogate pair: first char 0x" + Integer.toHexString(n) + ", second 0x" + Integer.toHexString(n2) + "; illegal combination");
        }
        return 65536 + (n - 55296 << 10) + (n2 - 56320);
    }
    
    protected static void _illegalSurrogate(final int n) {
        throw new IllegalArgumentException(UTF8Writer.illegalSurrogateDesc(n));
    }
    
    public static JsonStringEncoder getInstance() {
        final SoftReference<JsonStringEncoder> softReference = JsonStringEncoder._threadEncoder.get();
        JsonStringEncoder jsonStringEncoder;
        if (softReference == null) {
            jsonStringEncoder = null;
        }
        else {
            jsonStringEncoder = softReference.get();
        }
        JsonStringEncoder jsonStringEncoder2 = jsonStringEncoder;
        if (jsonStringEncoder == null) {
            jsonStringEncoder2 = new JsonStringEncoder();
            JsonStringEncoder._threadEncoder.set(new SoftReference<JsonStringEncoder>(jsonStringEncoder2));
        }
        return jsonStringEncoder2;
    }
    
    public byte[] encodeAsUTF8(final String s) {
        ByteArrayBuilder byteBuilder;
        if ((byteBuilder = this._byteBuilder) == null) {
            byteBuilder = new ByteArrayBuilder(null);
            this._byteBuilder = byteBuilder;
        }
        final int length = s.length();
        byte[] array = byteBuilder.resetAndGetFirstSegment();
        int length2 = array.length;
        int n = 0;
        int i = 0;
        while (i < length) {
            final int n2 = i + 1;
            final char char1 = s.charAt(i);
            int n3 = length2;
            Label_0136: {
                char char2;
                int n4;
                int length3;
                for (i = n2, char2 = char1; char2 <= '\u007f'; char2 = s.charAt(i), ++i, n3 = length3) {
                    if ((n4 = n) >= (length3 = n3)) {
                        array = byteBuilder.finishCurrentSegment();
                        length3 = array.length;
                        n4 = 0;
                    }
                    n = n4 + 1;
                    array[n4] = (byte)char2;
                    if (i >= length) {
                        break Label_0136;
                    }
                }
                int n5;
                int n6;
                if (n >= n3) {
                    array = byteBuilder.finishCurrentSegment();
                    n5 = array.length;
                    n6 = 0;
                }
                else {
                    final int n7 = n;
                    n5 = n3;
                    n6 = n7;
                }
                int n9;
                int n10;
                if (char2 < '\u0800') {
                    final int n8 = n6 + 1;
                    array[n6] = (byte)(char2 >> 6 | '\u00c0');
                    n9 = char2;
                    n10 = n8;
                }
                else if (char2 < '\ud800' || char2 > '\udfff') {
                    final int n11 = n6 + 1;
                    array[n6] = (byte)(char2 >> 12 | '\u00e0');
                    int n12;
                    int length4;
                    if ((n12 = n11) >= (length4 = n5)) {
                        array = byteBuilder.finishCurrentSegment();
                        length4 = array.length;
                        n12 = 0;
                    }
                    array[n12] = (byte)((char2 >> 6 & '?') | '\u0080');
                    final int n13 = n12 + 1;
                    final char c = char2;
                    n5 = length4;
                    n10 = n13;
                    n9 = c;
                }
                else {
                    if (char2 > '\udbff') {
                        _illegalSurrogate(char2);
                    }
                    if (i >= length) {
                        _illegalSurrogate(char2);
                    }
                    final int convertSurrogate = _convertSurrogate(char2, s.charAt(i));
                    if (convertSurrogate > 1114111) {
                        _illegalSurrogate(convertSurrogate);
                    }
                    final int n14 = n6 + 1;
                    array[n6] = (byte)(convertSurrogate >> 18 | 0xF0);
                    int n15;
                    int length5;
                    if ((n15 = n14) >= (length5 = n5)) {
                        array = byteBuilder.finishCurrentSegment();
                        length5 = array.length;
                        n15 = 0;
                    }
                    final int n16 = n15 + 1;
                    array[n15] = (byte)((convertSurrogate >> 12 & 0x3F) | 0x80);
                    int n17;
                    if (n16 >= length5) {
                        array = byteBuilder.finishCurrentSegment();
                        n5 = array.length;
                        n17 = 0;
                    }
                    else {
                        final int n18 = n16;
                        n5 = length5;
                        n17 = n18;
                    }
                    array[n17] = (byte)((convertSurrogate >> 6 & 0x3F) | 0x80);
                    n10 = n17 + 1;
                    n9 = convertSurrogate;
                    ++i;
                }
                int length6 = n5;
                int n19 = n10;
                if (n10 >= n5) {
                    array = byteBuilder.finishCurrentSegment();
                    length6 = array.length;
                    n19 = 0;
                }
                array[n19] = (byte)((n9 & 0x3F) | 0x80);
                length2 = length6;
                n = n19 + 1;
                continue;
            }
            return this._byteBuilder.completeAndCoalesce(n);
        }
        return this._byteBuilder.completeAndCoalesce(n);
    }
    
    public char[] quoteAsString(final String s) {
        TextBuffer textBuffer;
        if ((textBuffer = this._textBuffer) == null) {
            textBuffer = new TextBuffer(null);
            this._textBuffer = textBuffer;
        }
        char[] array = textBuffer.emptyAndGetCurrentSegment();
        final int[] get7BitOutputEscapes = CharTypes.get7BitOutputEscapes();
        final int length = get7BitOutputEscapes.length;
        final int length2 = s.length();
        int n = 0;
        int n2 = 0;
        int currentLength = 0;
        char char1;
        char char2;
        int n3;
        int n4 = 0;
        int n5;
        int n6;
        Label_0121_Outer:Label_0182_Outer:
        while (true) {
            currentLength = n;
            if (n2 < length2) {
                while (true) {
                Label_0265:
                    while (true) {
                        while (true) {
                            char1 = s.charAt(n2);
                            if (char1 < length && get7BitOutputEscapes[char1] != 0) {
                                char2 = s.charAt(n2);
                                n3 = get7BitOutputEscapes[char2];
                                if (n3 >= 0) {
                                    break;
                                }
                                n4 = this._appendNumericEscape(char2, this._quoteBuffer);
                                if (n + n4 > array.length) {
                                    n5 = array.length - n;
                                    if (n5 > 0) {
                                        System.arraycopy(this._quoteBuffer, 0, array, n, n5);
                                    }
                                    array = textBuffer.finishCurrentSegment();
                                    n = n4 - n5;
                                    System.arraycopy(this._quoteBuffer, n5, array, 0, n);
                                    ++n2;
                                    continue Label_0121_Outer;
                                }
                                break Label_0265;
                            }
                            else {
                                if (n >= array.length) {
                                    array = textBuffer.finishCurrentSegment();
                                    n = 0;
                                }
                                currentLength = n + 1;
                                array[n] = char1;
                                n6 = n2 + 1;
                                n = currentLength;
                                n2 = n6;
                                if (n6 >= length2) {
                                    break Label_0121_Outer;
                                }
                                continue Label_0121_Outer;
                            }
                        }
                        n4 = this._appendNamedEscape(n3, this._quoteBuffer);
                        continue Label_0182_Outer;
                    }
                    System.arraycopy(this._quoteBuffer, 0, array, n, n4);
                    n += n4;
                    continue;
                }
            }
            break;
        }
        textBuffer.setCurrentLength(currentLength);
        return textBuffer.contentsAsArray();
    }
    
    public byte[] quoteAsUTF8(final String s) {
        ByteArrayBuilder byteBuilder;
        if ((byteBuilder = this._byteBuilder) == null) {
            byteBuilder = new ByteArrayBuilder(null);
            this._byteBuilder = byteBuilder;
        }
        final int length = s.length();
        byte[] array = byteBuilder.resetAndGetFirstSegment();
        int appendByteEscape = 0;
        int n = 0;
        int n2 = 0;
    Label_0202:
        while (true) {
            n2 = appendByteEscape;
            if (n >= length) {
                break;
            }
            final int[] get7BitOutputEscapes = CharTypes.get7BitOutputEscapes();
            byte[] finishCurrentSegment = array;
            byte[] array2;
            int n3;
            int n4;
            char char2;
            while (true) {
                final char char1 = s.charAt(n);
                if (char1 > '\u007f' || get7BitOutputEscapes[char1] != 0) {
                    array2 = finishCurrentSegment;
                    if ((n3 = appendByteEscape) >= finishCurrentSegment.length) {
                        array2 = byteBuilder.finishCurrentSegment();
                        n3 = 0;
                    }
                    n4 = n + 1;
                    char2 = s.charAt(n);
                    if (char2 <= '\u007f') {
                        appendByteEscape = this._appendByteEscape(char2, get7BitOutputEscapes[char2], byteBuilder, n3);
                        array = byteBuilder.getCurrentSegment();
                        n = n4;
                        continue Label_0202;
                    }
                    break;
                }
                else {
                    if (appendByteEscape >= finishCurrentSegment.length) {
                        finishCurrentSegment = byteBuilder.finishCurrentSegment();
                        appendByteEscape = 0;
                    }
                    n2 = appendByteEscape + 1;
                    finishCurrentSegment[appendByteEscape] = (byte)char1;
                    final int n5 = n + 1;
                    appendByteEscape = n2;
                    n = n5;
                    if (n5 >= length) {
                        break Label_0202;
                    }
                    continue;
                }
            }
            int n6;
            int n7;
            if (char2 <= '\u07ff') {
                n6 = n3 + 1;
                array2[n3] = (byte)(char2 >> 6 | '\u00c0');
                n7 = ((char2 & '?') | '\u0080');
            }
            else if (char2 < '\ud800' || char2 > '\udfff') {
                int n8 = n3 + 1;
                array2[n3] = (byte)(char2 >> 12 | '\u00e0');
                if (n8 >= array2.length) {
                    array2 = byteBuilder.finishCurrentSegment();
                    n8 = 0;
                }
                final int n9 = n8 + 1;
                array2[n8] = (byte)((char2 >> 6 & '?') | '\u0080');
                n7 = ((char2 & '?') | '\u0080');
                n6 = n9;
            }
            else {
                if (char2 > '\udbff') {
                    _illegalSurrogate(char2);
                }
                if (n4 >= length) {
                    _illegalSurrogate(char2);
                }
                final int convertSurrogate = _convertSurrogate(char2, s.charAt(n4));
                if (convertSurrogate > 1114111) {
                    _illegalSurrogate(convertSurrogate);
                }
                int n10 = n3 + 1;
                array2[n3] = (byte)(convertSurrogate >> 18 | 0xF0);
                if (n10 >= array2.length) {
                    array2 = byteBuilder.finishCurrentSegment();
                    n10 = 0;
                }
                final int n11 = n10 + 1;
                array2[n10] = (byte)((convertSurrogate >> 12 & 0x3F) | 0x80);
                int n12;
                if (n11 >= array2.length) {
                    array2 = byteBuilder.finishCurrentSegment();
                    n12 = 0;
                }
                else {
                    n12 = n11;
                }
                final int n13 = n12 + 1;
                array2[n12] = (byte)((convertSurrogate >> 6 & 0x3F) | 0x80);
                ++n4;
                n7 = ((convertSurrogate & 0x3F) | 0x80);
                n6 = n13;
            }
            byte[] finishCurrentSegment2 = array2;
            int n14 = n6;
            if (n6 >= array2.length) {
                finishCurrentSegment2 = byteBuilder.finishCurrentSegment();
                n14 = 0;
            }
            finishCurrentSegment2[n14] = (byte)n7;
            array = finishCurrentSegment2;
            n = n4;
            appendByteEscape = n14 + 1;
        }
        return this._byteBuilder.completeAndCoalesce(n2);
    }
}
