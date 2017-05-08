// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.util.TextBuffer;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;

public final class IOContext
{
    protected byte[] _base64Buffer;
    protected final BufferRecycler _bufferRecycler;
    protected char[] _concatCBuffer;
    protected JsonEncoding _encoding;
    protected final boolean _managedResource;
    protected char[] _nameCopyBuffer;
    protected byte[] _readIOBuffer;
    protected final Object _sourceRef;
    protected char[] _tokenCBuffer;
    protected byte[] _writeEncodingBuffer;
    
    public IOContext(final BufferRecycler bufferRecycler, final Object sourceRef, final boolean managedResource) {
        this._readIOBuffer = null;
        this._writeEncodingBuffer = null;
        this._base64Buffer = null;
        this._tokenCBuffer = null;
        this._concatCBuffer = null;
        this._nameCopyBuffer = null;
        this._bufferRecycler = bufferRecycler;
        this._sourceRef = sourceRef;
        this._managedResource = managedResource;
    }
    
    private final void _verifyAlloc(final Object o) {
        if (o != null) {
            throw new IllegalStateException("Trying to call same allocXxx() method second time");
        }
    }
    
    private final void _verifyRelease(final Object o, final Object o2) {
        if (o != o2) {
            throw new IllegalArgumentException("Trying to release buffer not owned by the context");
        }
    }
    
    public char[] allocConcatBuffer() {
        this._verifyAlloc(this._concatCBuffer);
        return this._concatCBuffer = this._bufferRecycler.allocCharBuffer(BufferRecycler.CharBufferType.CONCAT_BUFFER);
    }
    
    public char[] allocNameCopyBuffer(final int n) {
        this._verifyAlloc(this._nameCopyBuffer);
        return this._nameCopyBuffer = this._bufferRecycler.allocCharBuffer(BufferRecycler.CharBufferType.NAME_COPY_BUFFER, n);
    }
    
    public char[] allocTokenBuffer() {
        this._verifyAlloc(this._tokenCBuffer);
        return this._tokenCBuffer = this._bufferRecycler.allocCharBuffer(BufferRecycler.CharBufferType.TOKEN_BUFFER);
    }
    
    public byte[] allocWriteEncodingBuffer() {
        this._verifyAlloc(this._writeEncodingBuffer);
        return this._writeEncodingBuffer = this._bufferRecycler.allocByteBuffer(BufferRecycler.ByteBufferType.WRITE_ENCODING_BUFFER);
    }
    
    public TextBuffer constructTextBuffer() {
        return new TextBuffer(this._bufferRecycler);
    }
    
    public Object getSourceReference() {
        return this._sourceRef;
    }
    
    public boolean isResourceManaged() {
        return this._managedResource;
    }
    
    public void releaseConcatBuffer(final char[] array) {
        if (array != null) {
            this._verifyRelease(array, this._concatCBuffer);
            this._concatCBuffer = null;
            this._bufferRecycler.releaseCharBuffer(BufferRecycler.CharBufferType.CONCAT_BUFFER, array);
        }
    }
    
    public void releaseNameCopyBuffer(final char[] array) {
        if (array != null) {
            this._verifyRelease(array, this._nameCopyBuffer);
            this._nameCopyBuffer = null;
            this._bufferRecycler.releaseCharBuffer(BufferRecycler.CharBufferType.NAME_COPY_BUFFER, array);
        }
    }
    
    public void releaseReadIOBuffer(final byte[] array) {
        if (array != null) {
            this._verifyRelease(array, this._readIOBuffer);
            this._readIOBuffer = null;
            this._bufferRecycler.releaseByteBuffer(BufferRecycler.ByteBufferType.READ_IO_BUFFER, array);
        }
    }
    
    public void releaseTokenBuffer(final char[] array) {
        if (array != null) {
            this._verifyRelease(array, this._tokenCBuffer);
            this._tokenCBuffer = null;
            this._bufferRecycler.releaseCharBuffer(BufferRecycler.CharBufferType.TOKEN_BUFFER, array);
        }
    }
    
    public void releaseWriteEncodingBuffer(final byte[] array) {
        if (array != null) {
            this._verifyRelease(array, this._writeEncodingBuffer);
            this._writeEncodingBuffer = null;
            this._bufferRecycler.releaseByteBuffer(BufferRecycler.ByteBufferType.WRITE_ENCODING_BUFFER, array);
        }
    }
    
    public void setEncoding(final JsonEncoding encoding) {
        this._encoding = encoding;
    }
}
