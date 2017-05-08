// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import java.util.Arrays;
import java.io.Serializable;

public final class Base64Variant implements Serializable
{
    private final transient int[] _asciiToBase64;
    private final transient byte[] _base64ToAsciiB;
    private final transient char[] _base64ToAsciiC;
    protected final transient int _maxLineLength;
    protected final String _name;
    protected final transient char _paddingChar;
    protected final transient boolean _usesPadding;
    
    public Base64Variant(final Base64Variant base64Variant, final String s, final int n) {
        this(base64Variant, s, base64Variant._usesPadding, base64Variant._paddingChar, n);
    }
    
    public Base64Variant(final Base64Variant base64Variant, final String name, final boolean usesPadding, final char paddingChar, final int maxLineLength) {
        this._asciiToBase64 = new int[128];
        this._base64ToAsciiC = new char[64];
        this._base64ToAsciiB = new byte[64];
        this._name = name;
        final byte[] base64ToAsciiB = base64Variant._base64ToAsciiB;
        System.arraycopy(base64ToAsciiB, 0, this._base64ToAsciiB, 0, base64ToAsciiB.length);
        final char[] base64ToAsciiC = base64Variant._base64ToAsciiC;
        System.arraycopy(base64ToAsciiC, 0, this._base64ToAsciiC, 0, base64ToAsciiC.length);
        final int[] asciiToBase64 = base64Variant._asciiToBase64;
        System.arraycopy(asciiToBase64, 0, this._asciiToBase64, 0, asciiToBase64.length);
        this._usesPadding = usesPadding;
        this._paddingChar = paddingChar;
        this._maxLineLength = maxLineLength;
    }
    
    public Base64Variant(final String name, final String s, final boolean usesPadding, final char paddingChar, int i) {
        final int n = 0;
        this._asciiToBase64 = new int[128];
        this._base64ToAsciiC = new char[64];
        this._base64ToAsciiB = new byte[64];
        this._name = name;
        this._usesPadding = usesPadding;
        this._paddingChar = paddingChar;
        this._maxLineLength = i;
        final int length = s.length();
        if (length != 64) {
            throw new IllegalArgumentException("Base64Alphabet length must be exactly 64 (was " + length + ")");
        }
        s.getChars(0, length, this._base64ToAsciiC, 0);
        Arrays.fill(this._asciiToBase64, -1);
        char c;
        for (i = n; i < length; ++i) {
            c = this._base64ToAsciiC[i];
            this._base64ToAsciiB[i] = (byte)c;
            this._asciiToBase64[c] = i;
        }
        if (usesPadding) {
            this._asciiToBase64[paddingChar] = -2;
        }
    }
    
    protected void _reportBase64EOF() throws IllegalArgumentException {
        throw new IllegalArgumentException("Unexpected end-of-String in base64 content");
    }
    
    protected void _reportInvalidBase64(final char c, final int n, final String s) throws IllegalArgumentException {
        String s2;
        if (c <= ' ') {
            s2 = "Illegal white space character (code 0x" + Integer.toHexString(c) + ") as character #" + (n + 1) + " of 4-char base64 unit: can only used between units";
        }
        else if (this.usesPaddingChar(c)) {
            s2 = "Unexpected padding character ('" + this.getPaddingChar() + "') as character #" + (n + 1) + " of 4-char base64 unit: padding only legal as 3rd or 4th character";
        }
        else if (!Character.isDefined(c) || Character.isISOControl(c)) {
            s2 = "Illegal character (code 0x" + Integer.toHexString(c) + ") in base64 content";
        }
        else {
            s2 = "Illegal character '" + c + "' (code 0x" + Integer.toHexString(c) + ") in base64 content";
        }
        String string = s2;
        if (s != null) {
            string = s2 + ": " + s;
        }
        throw new IllegalArgumentException(string);
    }
    
    public void decode(final String s, final ByteArrayBuilder byteArrayBuilder) throws IllegalArgumentException {
        final int length = s.length();
        int i = 0;
    Label_0036:
        while (i < length) {
            int n;
            char char1;
            while (true) {
                n = i + 1;
                char1 = s.charAt(i);
                if (n >= length) {
                    break Label_0036;
                }
                if (char1 > ' ') {
                    break;
                }
                i = n;
            }
            final int decodeBase64Char = this.decodeBase64Char(char1);
            if (decodeBase64Char < 0) {
                this._reportInvalidBase64(char1, 0, null);
            }
            if (n >= length) {
                this._reportBase64EOF();
            }
            final int n2 = n + 1;
            final char char2 = s.charAt(n);
            final int decodeBase64Char2 = this.decodeBase64Char(char2);
            if (decodeBase64Char2 < 0) {
                this._reportInvalidBase64(char2, 1, null);
            }
            final int n3 = decodeBase64Char << 6 | decodeBase64Char2;
            if (n2 >= length) {
                if (!this.usesPadding()) {
                    byteArrayBuilder.append(n3 >> 4);
                    return;
                }
                this._reportBase64EOF();
            }
            final int n4 = n2 + 1;
            final char char3 = s.charAt(n2);
            final int decodeBase64Char3 = this.decodeBase64Char(char3);
            if (decodeBase64Char3 < 0) {
                if (decodeBase64Char3 != -2) {
                    this._reportInvalidBase64(char3, 2, null);
                }
                if (n4 >= length) {
                    this._reportBase64EOF();
                }
                i = n4 + 1;
                final char char4 = s.charAt(n4);
                if (!this.usesPaddingChar(char4)) {
                    this._reportInvalidBase64(char4, 3, "expected padding character '" + this.getPaddingChar() + "'");
                }
                byteArrayBuilder.append(n3 >> 4);
            }
            else {
                final int n5 = n3 << 6 | decodeBase64Char3;
                if (n4 >= length) {
                    if (!this.usesPadding()) {
                        byteArrayBuilder.appendTwoBytes(n5 >> 2);
                        return;
                    }
                    this._reportBase64EOF();
                }
                i = n4 + 1;
                final char char5 = s.charAt(n4);
                final int decodeBase64Char4 = this.decodeBase64Char(char5);
                if (decodeBase64Char4 < 0) {
                    if (decodeBase64Char4 != -2) {
                        this._reportInvalidBase64(char5, 3, null);
                    }
                    byteArrayBuilder.appendTwoBytes(n5 >> 2);
                }
                else {
                    byteArrayBuilder.appendThreeBytes(n5 << 6 | decodeBase64Char4);
                }
            }
        }
    }
    
    public int decodeBase64Char(final char c) {
        if (c <= '\u007f') {
            return this._asciiToBase64[c];
        }
        return -1;
    }
    
    public int decodeBase64Char(final int n) {
        if (n <= 127) {
            return this._asciiToBase64[n];
        }
        return -1;
    }
    
    public String encode(final byte[] array, final boolean b) {
        final int length = array.length;
        final StringBuilder sb = new StringBuilder((length >> 2) + length + (length >> 3));
        if (b) {
            sb.append('\"');
        }
        final int maxLineLength = this.getMaxLineLength();
        int i = 0;
        int n = maxLineLength >> 2;
        while (i <= length - 3) {
            final int n2 = i + 1;
            final byte b2 = array[i];
            final int n3 = n2 + 1;
            this.encodeBase64Chunk(sb, (b2 << 8 | (array[n2] & 0xFF)) << 8 | (array[n3] & 0xFF));
            int n4;
            if ((n4 = n - 1) <= 0) {
                sb.append('\\');
                sb.append('n');
                n4 = this.getMaxLineLength() >> 2;
            }
            n = n4;
            i = n3 + 1;
        }
        final int n5 = length - i;
        if (n5 > 0) {
            final int n6 = i + 1;
            int n7 = array[i] << 16;
            if (n5 == 2) {
                n7 |= (array[n6] & 0xFF) << 8;
            }
            this.encodeBase64Partial(sb, n7, n5);
        }
        if (b) {
            sb.append('\"');
        }
        return sb.toString();
    }
    
    public int encodeBase64Chunk(final int n, final byte[] array, int n2) {
        final int n3 = n2 + 1;
        array[n2] = this._base64ToAsciiB[n >> 18 & 0x3F];
        n2 = n3 + 1;
        array[n3] = this._base64ToAsciiB[n >> 12 & 0x3F];
        final int n4 = n2 + 1;
        array[n2] = this._base64ToAsciiB[n >> 6 & 0x3F];
        array[n4] = this._base64ToAsciiB[n & 0x3F];
        return n4 + 1;
    }
    
    public int encodeBase64Chunk(final int n, final char[] array, int n2) {
        final int n3 = n2 + 1;
        array[n2] = this._base64ToAsciiC[n >> 18 & 0x3F];
        n2 = n3 + 1;
        array[n3] = this._base64ToAsciiC[n >> 12 & 0x3F];
        final int n4 = n2 + 1;
        array[n2] = this._base64ToAsciiC[n >> 6 & 0x3F];
        array[n4] = this._base64ToAsciiC[n & 0x3F];
        return n4 + 1;
    }
    
    public void encodeBase64Chunk(final StringBuilder sb, final int n) {
        sb.append(this._base64ToAsciiC[n >> 18 & 0x3F]);
        sb.append(this._base64ToAsciiC[n >> 12 & 0x3F]);
        sb.append(this._base64ToAsciiC[n >> 6 & 0x3F]);
        sb.append(this._base64ToAsciiC[n & 0x3F]);
    }
    
    public int encodeBase64Partial(final int n, final int n2, final byte[] array, int n3) {
        final int n4 = n3 + 1;
        array[n3] = this._base64ToAsciiB[n >> 18 & 0x3F];
        n3 = n4 + 1;
        array[n4] = this._base64ToAsciiB[n >> 12 & 0x3F];
        if (this._usesPadding) {
            final byte b = (byte)this._paddingChar;
            final int n5 = n3 + 1;
            byte b2;
            if (n2 == 2) {
                b2 = this._base64ToAsciiB[n >> 6 & 0x3F];
            }
            else {
                b2 = b;
            }
            array[n3] = b2;
            array[n5] = b;
            return n5 + 1;
        }
        if (n2 == 2) {
            array[n3] = this._base64ToAsciiB[n >> 6 & 0x3F];
            return n3 + 1;
        }
        return n3;
    }
    
    public int encodeBase64Partial(final int n, final int n2, final char[] array, int n3) {
        final int n4 = n3 + 1;
        array[n3] = this._base64ToAsciiC[n >> 18 & 0x3F];
        n3 = n4 + 1;
        array[n4] = this._base64ToAsciiC[n >> 12 & 0x3F];
        if (this._usesPadding) {
            final int n5 = n3 + 1;
            char paddingChar;
            if (n2 == 2) {
                paddingChar = this._base64ToAsciiC[n >> 6 & 0x3F];
            }
            else {
                paddingChar = this._paddingChar;
            }
            array[n3] = paddingChar;
            array[n5] = this._paddingChar;
            return n5 + 1;
        }
        if (n2 == 2) {
            array[n3] = this._base64ToAsciiC[n >> 6 & 0x3F];
            return n3 + 1;
        }
        return n3;
    }
    
    public void encodeBase64Partial(final StringBuilder sb, final int n, final int n2) {
        sb.append(this._base64ToAsciiC[n >> 18 & 0x3F]);
        sb.append(this._base64ToAsciiC[n >> 12 & 0x3F]);
        if (this._usesPadding) {
            char paddingChar;
            if (n2 == 2) {
                paddingChar = this._base64ToAsciiC[n >> 6 & 0x3F];
            }
            else {
                paddingChar = this._paddingChar;
            }
            sb.append(paddingChar);
            sb.append(this._paddingChar);
        }
        else if (n2 == 2) {
            sb.append(this._base64ToAsciiC[n >> 6 & 0x3F]);
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this;
    }
    
    public int getMaxLineLength() {
        return this._maxLineLength;
    }
    
    public char getPaddingChar() {
        return this._paddingChar;
    }
    
    @Override
    public int hashCode() {
        return this._name.hashCode();
    }
    
    @Override
    public String toString() {
        return this._name;
    }
    
    public boolean usesPadding() {
        return this._usesPadding;
    }
    
    public boolean usesPaddingChar(final char c) {
        return c == this._paddingChar;
    }
    
    public boolean usesPaddingChar(final int n) {
        return n == this._paddingChar;
    }
}
