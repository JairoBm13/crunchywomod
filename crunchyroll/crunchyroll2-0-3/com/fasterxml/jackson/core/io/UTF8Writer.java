// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

public final class UTF8Writer extends Writer
{
    private final IOContext _context;
    private OutputStream _out;
    private byte[] _outBuffer;
    private final int _outBufferEnd;
    private int _outPtr;
    private int _surrogate;
    
    public UTF8Writer(final IOContext context, final OutputStream out) {
        this._surrogate = 0;
        this._context = context;
        this._out = out;
        this._outBuffer = context.allocWriteEncodingBuffer();
        this._outBufferEnd = this._outBuffer.length - 4;
        this._outPtr = 0;
    }
    
    protected static void illegalSurrogate(final int n) throws IOException {
        throw new IOException(illegalSurrogateDesc(n));
    }
    
    protected static String illegalSurrogateDesc(final int n) {
        if (n > 1114111) {
            return "Illegal character point (0x" + Integer.toHexString(n) + ") to output; max is 0x10FFFF as per RFC 4627";
        }
        if (n < 55296) {
            return "Illegal character point (0x" + Integer.toHexString(n) + ") to output";
        }
        if (n <= 56319) {
            return "Unmatched first part of surrogate pair (0x" + Integer.toHexString(n) + ")";
        }
        return "Unmatched second part of surrogate pair (0x" + Integer.toHexString(n) + ")";
    }
    
    @Override
    public Writer append(final char c) throws IOException {
        this.write(c);
        return this;
    }
    
    @Override
    public void close() throws IOException {
        if (this._out != null) {
            if (this._outPtr > 0) {
                this._out.write(this._outBuffer, 0, this._outPtr);
                this._outPtr = 0;
            }
            final OutputStream out = this._out;
            this._out = null;
            final byte[] outBuffer = this._outBuffer;
            if (outBuffer != null) {
                this._outBuffer = null;
                this._context.releaseWriteEncodingBuffer(outBuffer);
            }
            out.close();
            final int surrogate = this._surrogate;
            this._surrogate = 0;
            if (surrogate > 0) {
                illegalSurrogate(surrogate);
            }
        }
    }
    
    protected int convertSurrogate(final int n) throws IOException {
        final int surrogate = this._surrogate;
        this._surrogate = 0;
        if (n < 56320 || n > 57343) {
            throw new IOException("Broken surrogate pair: first char 0x" + Integer.toHexString(surrogate) + ", second 0x" + Integer.toHexString(n) + "; illegal combination");
        }
        return (surrogate - 55296 << 10) + 65536 + (n - 56320);
    }
    
    @Override
    public void flush() throws IOException {
        if (this._out != null) {
            if (this._outPtr > 0) {
                this._out.write(this._outBuffer, 0, this._outPtr);
                this._outPtr = 0;
            }
            this._out.flush();
        }
    }
    
    @Override
    public void write(int outPtr) throws IOException {
        int convertSurrogate;
        if (this._surrogate > 0) {
            convertSurrogate = this.convertSurrogate(outPtr);
        }
        else {
            convertSurrogate = outPtr;
            if (outPtr >= 55296 && (convertSurrogate = outPtr) <= 57343) {
                if (outPtr > 56319) {
                    illegalSurrogate(outPtr);
                }
                this._surrogate = outPtr;
                return;
            }
        }
        if (this._outPtr >= this._outBufferEnd) {
            this._out.write(this._outBuffer, 0, this._outPtr);
            this._outPtr = 0;
        }
        if (convertSurrogate < 128) {
            final byte[] outBuffer = this._outBuffer;
            outPtr = this._outPtr++;
            outBuffer[outPtr] = (byte)convertSurrogate;
            return;
        }
        outPtr = this._outPtr;
        if (convertSurrogate < 2048) {
            final byte[] outBuffer2 = this._outBuffer;
            final int n = outPtr + 1;
            outBuffer2[outPtr] = (byte)(convertSurrogate >> 6 | 0xC0);
            final byte[] outBuffer3 = this._outBuffer;
            outPtr = n + 1;
            outBuffer3[n] = (byte)((convertSurrogate & 0x3F) | 0x80);
        }
        else if (convertSurrogate <= 65535) {
            final byte[] outBuffer4 = this._outBuffer;
            final int n2 = outPtr + 1;
            outBuffer4[outPtr] = (byte)(convertSurrogate >> 12 | 0xE0);
            final byte[] outBuffer5 = this._outBuffer;
            final int n3 = n2 + 1;
            outBuffer5[n2] = (byte)((convertSurrogate >> 6 & 0x3F) | 0x80);
            final byte[] outBuffer6 = this._outBuffer;
            outPtr = n3 + 1;
            outBuffer6[n3] = (byte)((convertSurrogate & 0x3F) | 0x80);
        }
        else {
            if (convertSurrogate > 1114111) {
                illegalSurrogate(convertSurrogate);
            }
            final byte[] outBuffer7 = this._outBuffer;
            final int n4 = outPtr + 1;
            outBuffer7[outPtr] = (byte)(convertSurrogate >> 18 | 0xF0);
            final byte[] outBuffer8 = this._outBuffer;
            outPtr = n4 + 1;
            outBuffer8[n4] = (byte)((convertSurrogate >> 12 & 0x3F) | 0x80);
            final byte[] outBuffer9 = this._outBuffer;
            final int n5 = outPtr + 1;
            outBuffer9[outPtr] = (byte)((convertSurrogate >> 6 & 0x3F) | 0x80);
            final byte[] outBuffer10 = this._outBuffer;
            outPtr = n5 + 1;
            outBuffer10[n5] = (byte)((convertSurrogate & 0x3F) | 0x80);
        }
        this._outPtr = outPtr;
    }
    
    @Override
    public void write(final String s) throws IOException {
        this.write(s, 0, s.length());
    }
    
    @Override
    public void write(final String s, int outPtr, int i) throws IOException {
        if (i < 2) {
            if (i == 1) {
                this.write(s.charAt(outPtr));
            }
            return;
        }
        int n = outPtr;
        int n2 = i;
        if (this._surrogate > 0) {
            final char char1 = s.charAt(outPtr);
            n2 = i - 1;
            this.write(this.convertSurrogate(char1));
            n = outPtr + 1;
        }
        outPtr = this._outPtr;
        final byte[] outBuffer = this._outBuffer;
        final int outBufferEnd = this._outBufferEnd;
        final int n3 = n2 + n;
        i = n;
        int outPtr2;
        while (true) {
            outPtr2 = outPtr;
            if (i >= n3) {
                break;
            }
            int n4;
            if ((n4 = outPtr) >= outBufferEnd) {
                this._out.write(outBuffer, 0, outPtr);
                n4 = 0;
            }
            final int n5 = i + 1;
            final char char2 = s.charAt(i);
            int surrogate = 0;
            Label_0215: {
                if (char2 < '\u0080') {
                    outPtr = n4 + 1;
                    outBuffer[n4] = (byte)char2;
                    int n6 = n3 - n5;
                    i = outBufferEnd - outPtr;
                    if (n6 > i) {
                        n6 = i;
                    }
                    int n7;
                    char char3;
                    for (i = n5; i < n6 + n5; i = n7) {
                        n7 = i + 1;
                        char3 = s.charAt(i);
                        if (char3 >= '\u0080') {
                            i = n7;
                            surrogate = char3;
                            break Label_0215;
                        }
                        outBuffer[outPtr] = (byte)char3;
                        ++outPtr;
                    }
                    continue;
                }
                i = n5;
                final char c = char2;
                outPtr = n4;
                surrogate = c;
            }
            if (surrogate < 2048) {
                final int n8 = outPtr + 1;
                outBuffer[outPtr] = (byte)(surrogate >> 6 | 0xC0);
                outPtr = n8 + 1;
                outBuffer[n8] = (byte)((surrogate & 0x3F) | 0x80);
            }
            else if (surrogate < 55296 || surrogate > 57343) {
                final int n9 = outPtr + 1;
                outBuffer[outPtr] = (byte)(surrogate >> 12 | 0xE0);
                final int n10 = n9 + 1;
                outBuffer[n9] = (byte)((surrogate >> 6 & 0x3F) | 0x80);
                outPtr = n10 + 1;
                outBuffer[n10] = (byte)((surrogate & 0x3F) | 0x80);
            }
            else {
                if (surrogate > 56319) {
                    this._outPtr = outPtr;
                    illegalSurrogate(surrogate);
                }
                this._surrogate = surrogate;
                if (i >= n3) {
                    outPtr2 = outPtr;
                    break;
                }
                final int convertSurrogate = this.convertSurrogate(s.charAt(i));
                if (convertSurrogate > 1114111) {
                    this._outPtr = outPtr;
                    illegalSurrogate(convertSurrogate);
                }
                final int n11 = outPtr + 1;
                outBuffer[outPtr] = (byte)(convertSurrogate >> 18 | 0xF0);
                outPtr = n11 + 1;
                outBuffer[n11] = (byte)((convertSurrogate >> 12 & 0x3F) | 0x80);
                final int n12 = outPtr + 1;
                outBuffer[outPtr] = (byte)((convertSurrogate >> 6 & 0x3F) | 0x80);
                outPtr = n12 + 1;
                outBuffer[n12] = (byte)((convertSurrogate & 0x3F) | 0x80);
                ++i;
            }
        }
        this._outPtr = outPtr2;
    }
    
    @Override
    public void write(final char[] array) throws IOException {
        this.write(array, 0, array.length);
    }
    
    @Override
    public void write(final char[] array, int outPtr, int i) throws IOException {
        if (i < 2) {
            if (i == 1) {
                this.write(array[outPtr]);
            }
            return;
        }
        int n = outPtr;
        int n2 = i;
        if (this._surrogate > 0) {
            final char c = array[outPtr];
            n2 = i - 1;
            this.write(this.convertSurrogate(c));
            n = outPtr + 1;
        }
        outPtr = this._outPtr;
        final byte[] outBuffer = this._outBuffer;
        final int outBufferEnd = this._outBufferEnd;
        final int n3 = n2 + n;
        i = n;
        int outPtr2;
        while (true) {
            outPtr2 = outPtr;
            if (i >= n3) {
                break;
            }
            int n4;
            if ((n4 = outPtr) >= outBufferEnd) {
                this._out.write(outBuffer, 0, outPtr);
                n4 = 0;
            }
            final int n5 = i + 1;
            final char c2 = array[i];
            int surrogate = 0;
            Label_0207: {
                if (c2 < '\u0080') {
                    outPtr = n4 + 1;
                    outBuffer[n4] = (byte)c2;
                    int n6 = n3 - n5;
                    i = outBufferEnd - outPtr;
                    if (n6 > i) {
                        n6 = i;
                    }
                    int n7;
                    char c3;
                    for (i = n5; i < n6 + n5; i = n7) {
                        n7 = i + 1;
                        c3 = array[i];
                        if (c3 >= '\u0080') {
                            i = n7;
                            surrogate = c3;
                            break Label_0207;
                        }
                        outBuffer[outPtr] = (byte)c3;
                        ++outPtr;
                    }
                    continue;
                }
                i = n5;
                final char c4 = c2;
                outPtr = n4;
                surrogate = c4;
            }
            if (surrogate < 2048) {
                final int n8 = outPtr + 1;
                outBuffer[outPtr] = (byte)(surrogate >> 6 | 0xC0);
                outPtr = n8 + 1;
                outBuffer[n8] = (byte)((surrogate & 0x3F) | 0x80);
            }
            else if (surrogate < 55296 || surrogate > 57343) {
                final int n9 = outPtr + 1;
                outBuffer[outPtr] = (byte)(surrogate >> 12 | 0xE0);
                final int n10 = n9 + 1;
                outBuffer[n9] = (byte)((surrogate >> 6 & 0x3F) | 0x80);
                outPtr = n10 + 1;
                outBuffer[n10] = (byte)((surrogate & 0x3F) | 0x80);
            }
            else {
                if (surrogate > 56319) {
                    this._outPtr = outPtr;
                    illegalSurrogate(surrogate);
                }
                this._surrogate = surrogate;
                if (i >= n3) {
                    outPtr2 = outPtr;
                    break;
                }
                final int convertSurrogate = this.convertSurrogate(array[i]);
                if (convertSurrogate > 1114111) {
                    this._outPtr = outPtr;
                    illegalSurrogate(convertSurrogate);
                }
                final int n11 = outPtr + 1;
                outBuffer[outPtr] = (byte)(convertSurrogate >> 18 | 0xF0);
                outPtr = n11 + 1;
                outBuffer[n11] = (byte)((convertSurrogate >> 12 & 0x3F) | 0x80);
                final int n12 = outPtr + 1;
                outBuffer[outPtr] = (byte)((convertSurrogate >> 6 & 0x3F) | 0x80);
                outPtr = n12 + 1;
                outBuffer[n12] = (byte)((convertSurrogate & 0x3F) | 0x80);
                ++i;
            }
        }
        this._outPtr = outPtr2;
    }
}
