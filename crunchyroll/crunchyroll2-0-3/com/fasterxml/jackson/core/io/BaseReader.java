// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

abstract class BaseReader extends Reader
{
    protected byte[] _buffer;
    protected final IOContext _context;
    protected InputStream _in;
    protected int _length;
    protected int _ptr;
    protected char[] _tmpBuf;
    
    @Override
    public void close() throws IOException {
        final InputStream in = this._in;
        if (in != null) {
            this._in = null;
            this.freeBuffers();
            in.close();
        }
    }
    
    public final void freeBuffers() {
        final byte[] buffer = this._buffer;
        if (buffer != null) {
            this._buffer = null;
            this._context.releaseReadIOBuffer(buffer);
        }
    }
    
    @Override
    public int read() throws IOException {
        if (this._tmpBuf == null) {
            this._tmpBuf = new char[1];
        }
        if (this.read(this._tmpBuf, 0, 1) < 1) {
            return -1;
        }
        return this._tmpBuf[0];
    }
    
    protected void reportBounds(final char[] array, final int n, final int n2) throws IOException {
        throw new ArrayIndexOutOfBoundsException("read(buf," + n + "," + n2 + "), cbuf[" + array.length + "]");
    }
    
    protected void reportStrangeStream() throws IOException {
        throw new IOException("Strange I/O stream, returned 0 bytes on read");
    }
}
