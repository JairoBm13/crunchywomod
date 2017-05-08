// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.io;

import java.io.IOException;
import java.io.InputStream;

public final class MergedStream extends InputStream
{
    byte[] _buffer;
    protected final IOContext _context;
    final int _end;
    final InputStream _in;
    int _ptr;
    
    private void freeMergedBuffer() {
        final byte[] buffer = this._buffer;
        if (buffer != null) {
            this._buffer = null;
            if (this._context != null) {
                this._context.releaseReadIOBuffer(buffer);
            }
        }
    }
    
    @Override
    public int available() throws IOException {
        if (this._buffer != null) {
            return this._end - this._ptr;
        }
        return this._in.available();
    }
    
    @Override
    public void close() throws IOException {
        this.freeMergedBuffer();
        this._in.close();
    }
    
    @Override
    public void mark(final int n) {
        if (this._buffer == null) {
            this._in.mark(n);
        }
    }
    
    @Override
    public boolean markSupported() {
        return this._buffer == null && this._in.markSupported();
    }
    
    @Override
    public int read() throws IOException {
        if (this._buffer != null) {
            final byte b = this._buffer[this._ptr++];
            if (this._ptr >= this._end) {
                this.freeMergedBuffer();
            }
            return b & 0xFF;
        }
        return this._in.read();
    }
    
    @Override
    public int read(final byte[] array) throws IOException {
        return this.read(array, 0, array.length);
    }
    
    @Override
    public int read(final byte[] array, final int n, final int n2) throws IOException {
        if (this._buffer != null) {
            final int n3 = this._end - this._ptr;
            int n4;
            if ((n4 = n2) > n3) {
                n4 = n3;
            }
            System.arraycopy(this._buffer, this._ptr, array, n, n4);
            this._ptr += n4;
            if (this._ptr >= this._end) {
                this.freeMergedBuffer();
            }
            return n4;
        }
        return this._in.read(array, n, n2);
    }
    
    @Override
    public void reset() throws IOException {
        if (this._buffer == null) {
            this._in.reset();
        }
    }
    
    @Override
    public long skip(long n) throws IOException {
        long n3;
        if (this._buffer != null) {
            final int n2 = this._end - this._ptr;
            if (n2 > n) {
                this._ptr += (int)n;
                return n;
            }
            this.freeMergedBuffer();
            n3 = n2 + 0L;
            n -= n2;
        }
        else {
            n3 = 0L;
        }
        long n4 = n3;
        if (n > 0L) {
            n4 = n3 + this._in.skip(n);
        }
        return n4;
    }
}
