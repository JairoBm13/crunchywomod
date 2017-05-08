// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.io;

import java.io.CharConversionException;
import java.io.IOException;

public class UTF32Reader extends BaseReader
{
    protected final boolean _bigEndian;
    protected int _byteCount;
    protected int _charCount;
    protected final boolean _managedBuffers;
    protected char _surrogate;
    
    private boolean loadMore(int n) throws IOException {
        this._byteCount += this._length - n;
        if (n > 0) {
            if (this._ptr > 0) {
                for (int i = 0; i < n; ++i) {
                    this._buffer[i] = this._buffer[this._ptr + i];
                }
                this._ptr = 0;
            }
            this._length = n;
        }
        else {
            this._ptr = 0;
            if (this._in == null) {
                n = -1;
            }
            else {
                n = this._in.read(this._buffer);
            }
            if (n < 1) {
                this._length = 0;
                if (n < 0) {
                    if (this._managedBuffers) {
                        this.freeBuffers();
                    }
                    return false;
                }
                this.reportStrangeStream();
            }
            this._length = n;
        }
        while (this._length < 4) {
            if (this._in == null) {
                n = -1;
            }
            else {
                n = this._in.read(this._buffer, this._length, this._buffer.length - this._length);
            }
            if (n < 1) {
                if (n < 0) {
                    if (this._managedBuffers) {
                        this.freeBuffers();
                    }
                    this.reportUnexpectedEOF(this._length, 4);
                }
                this.reportStrangeStream();
            }
            this._length += n;
        }
        return true;
    }
    
    private void reportInvalid(final int n, final int n2, final String s) throws IOException {
        throw new CharConversionException("Invalid UTF-32 character 0x" + Integer.toHexString(n) + s + " at char #" + (this._charCount + n2) + ", byte #" + (this._byteCount + this._ptr - 1) + ")");
    }
    
    private void reportUnexpectedEOF(final int n, final int n2) throws IOException {
        throw new CharConversionException("Unexpected EOF in the middle of a 4-byte UTF-32 char: got " + n + ", needed " + n2 + ", at char #" + this._charCount + ", byte #" + (this._byteCount + n) + ")");
    }
    
    @Override
    public int read(final char[] array, int n, int i) throws IOException {
        int n2;
        if (this._buffer == null) {
            n2 = -1;
        }
        else if ((n2 = i) >= 1) {
            if (n < 0 || n + i > array.length) {
                this.reportBounds(array, n, i);
            }
            final int n3 = i + n;
            if (this._surrogate != '\0') {
                i = n + 1;
                array[n] = this._surrogate;
                this._surrogate = '\0';
            }
            else {
                i = this._length - this._ptr;
                if (i < 4 && !this.loadMore(i)) {
                    return -1;
                }
                i = n;
            }
            while (true) {
                while (i < n3) {
                    final int ptr = this._ptr;
                    int n4;
                    if (this._bigEndian) {
                        n4 = ((this._buffer[ptr + 3] & 0xFF) | (this._buffer[ptr] << 24 | (this._buffer[ptr + 1] & 0xFF) << 16 | (this._buffer[ptr + 2] & 0xFF) << 8));
                    }
                    else {
                        n4 = (this._buffer[ptr + 3] << 24 | ((this._buffer[ptr] & 0xFF) | (this._buffer[ptr + 1] & 0xFF) << 8 | (this._buffer[ptr + 2] & 0xFF) << 16));
                    }
                    this._ptr += 4;
                    int n7;
                    if (n4 > 65535) {
                        if (n4 > 1114111) {
                            this.reportInvalid(n4, i - n, "(above " + Integer.toHexString(1114111) + ") ");
                        }
                        final int n5 = n4 - 65536;
                        final int n6 = i + 1;
                        array[i] = (char)(55296 + (n5 >> 10));
                        n7 = ((n5 & 0x3FF) | 0xDC00);
                        if ((i = n6) >= n3) {
                            this._surrogate = (char)n7;
                            i = n6;
                            break;
                        }
                    }
                    else {
                        n7 = n4;
                    }
                    final int n8 = i + 1;
                    array[i] = (char)n7;
                    i = n8;
                    if (this._ptr < this._length) {
                        continue;
                    }
                    i = n8;
                    n = i - n;
                    this._charCount += n;
                    return n;
                }
                continue;
            }
        }
        return n2;
    }
}
