// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.util;

public class BufferRecycler
{
    protected final byte[][] _byteBuffers;
    protected final char[][] _charBuffers;
    
    public BufferRecycler() {
        this._byteBuffers = new byte[ByteBufferType.values().length][];
        this._charBuffers = new char[CharBufferType.values().length][];
    }
    
    private byte[] balloc(final int n) {
        return new byte[n];
    }
    
    private char[] calloc(final int n) {
        return new char[n];
    }
    
    public final byte[] allocByteBuffer(final ByteBufferType byteBufferType) {
        final int ordinal = byteBufferType.ordinal();
        final byte[] array = this._byteBuffers[ordinal];
        if (array == null) {
            return this.balloc(byteBufferType.size);
        }
        this._byteBuffers[ordinal] = null;
        return array;
    }
    
    public final char[] allocCharBuffer(final CharBufferType charBufferType) {
        return this.allocCharBuffer(charBufferType, 0);
    }
    
    public final char[] allocCharBuffer(final CharBufferType charBufferType, int ordinal) {
        int size = ordinal;
        if (charBufferType.size > ordinal) {
            size = charBufferType.size;
        }
        ordinal = charBufferType.ordinal();
        final char[] array = this._charBuffers[ordinal];
        if (array == null || array.length < size) {
            return this.calloc(size);
        }
        this._charBuffers[ordinal] = null;
        return array;
    }
    
    public final void releaseByteBuffer(final ByteBufferType byteBufferType, final byte[] array) {
        this._byteBuffers[byteBufferType.ordinal()] = array;
    }
    
    public final void releaseCharBuffer(final CharBufferType charBufferType, final char[] array) {
        this._charBuffers[charBufferType.ordinal()] = array;
    }
    
    public enum ByteBufferType
    {
        BASE64_CODEC_BUFFER(2000), 
        READ_IO_BUFFER(4000), 
        WRITE_CONCAT_BUFFER(2000), 
        WRITE_ENCODING_BUFFER(4000);
        
        protected final int size;
        
        private ByteBufferType(final int size) {
            this.size = size;
        }
    }
    
    public enum CharBufferType
    {
        CONCAT_BUFFER(2000), 
        NAME_COPY_BUFFER(200), 
        TEXT_BUFFER(200), 
        TOKEN_BUFFER(2000);
        
        protected final int size;
        
        private CharBufferType(final int size) {
            this.size = size;
        }
    }
}
