// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.io.OutputStream;

public final class ByteArrayBuilder extends OutputStream
{
    private static final byte[] NO_BYTES;
    private final BufferRecycler _bufferRecycler;
    private byte[] _currBlock;
    private int _currBlockPtr;
    private final LinkedList<byte[]> _pastBlocks;
    private int _pastLen;
    
    static {
        NO_BYTES = new byte[0];
    }
    
    public ByteArrayBuilder() {
        this(null);
    }
    
    public ByteArrayBuilder(final int n) {
        this(null, n);
    }
    
    public ByteArrayBuilder(final BufferRecycler bufferRecycler) {
        this(bufferRecycler, 500);
    }
    
    public ByteArrayBuilder(final BufferRecycler bufferRecycler, final int n) {
        this._pastBlocks = new LinkedList<byte[]>();
        this._bufferRecycler = bufferRecycler;
        if (bufferRecycler == null) {
            this._currBlock = new byte[n];
            return;
        }
        this._currBlock = bufferRecycler.allocByteBuffer(BufferRecycler.ByteBufferType.WRITE_CONCAT_BUFFER);
    }
    
    private void _allocMore() {
        int n = 262144;
        this._pastLen += this._currBlock.length;
        final int max = Math.max(this._pastLen >> 1, 1000);
        if (max <= 262144) {
            n = max;
        }
        this._pastBlocks.add(this._currBlock);
        this._currBlock = new byte[n];
        this._currBlockPtr = 0;
    }
    
    public void append(final int n) {
        if (this._currBlockPtr >= this._currBlock.length) {
            this._allocMore();
        }
        this._currBlock[this._currBlockPtr++] = (byte)n;
    }
    
    public void appendThreeBytes(final int n) {
        if (this._currBlockPtr + 2 < this._currBlock.length) {
            this._currBlock[this._currBlockPtr++] = (byte)(n >> 16);
            this._currBlock[this._currBlockPtr++] = (byte)(n >> 8);
            this._currBlock[this._currBlockPtr++] = (byte)n;
            return;
        }
        this.append(n >> 16);
        this.append(n >> 8);
        this.append(n);
    }
    
    public void appendTwoBytes(final int n) {
        if (this._currBlockPtr + 1 < this._currBlock.length) {
            this._currBlock[this._currBlockPtr++] = (byte)(n >> 8);
            this._currBlock[this._currBlockPtr++] = (byte)n;
            return;
        }
        this.append(n >> 8);
        this.append(n);
    }
    
    @Override
    public void close() {
    }
    
    public byte[] completeAndCoalesce(final int currBlockPtr) {
        this._currBlockPtr = currBlockPtr;
        return this.toByteArray();
    }
    
    public byte[] finishCurrentSegment() {
        this._allocMore();
        return this._currBlock;
    }
    
    @Override
    public void flush() {
    }
    
    public byte[] getCurrentSegment() {
        return this._currBlock;
    }
    
    public int getCurrentSegmentLength() {
        return this._currBlockPtr;
    }
    
    public void reset() {
        this._pastLen = 0;
        this._currBlockPtr = 0;
        if (!this._pastBlocks.isEmpty()) {
            this._pastBlocks.clear();
        }
    }
    
    public byte[] resetAndGetFirstSegment() {
        this.reset();
        return this._currBlock;
    }
    
    public void setCurrentSegmentLength(final int currBlockPtr) {
        this._currBlockPtr = currBlockPtr;
    }
    
    public byte[] toByteArray() {
        final int n = this._pastLen + this._currBlockPtr;
        if (n == 0) {
            return ByteArrayBuilder.NO_BYTES;
        }
        final byte[] array = new byte[n];
        final Iterator<byte[]> iterator = this._pastBlocks.iterator();
        int n2 = 0;
        while (iterator.hasNext()) {
            final byte[] array2 = iterator.next();
            final int length = array2.length;
            System.arraycopy(array2, 0, array, n2, length);
            n2 += length;
        }
        System.arraycopy(this._currBlock, 0, array, n2, this._currBlockPtr);
        final int n3 = this._currBlockPtr + n2;
        if (n3 != n) {
            throw new RuntimeException("Internal error: total len assumed to be " + n + ", copied " + n3 + " bytes");
        }
        if (!this._pastBlocks.isEmpty()) {
            this.reset();
        }
        return array;
    }
    
    @Override
    public void write(final int n) {
        this.append(n);
    }
    
    @Override
    public void write(final byte[] array) {
        this.write(array, 0, array.length);
    }
    
    @Override
    public void write(final byte[] array, int n, int n2) {
        int n3 = n;
        while (true) {
            final int min = Math.min(this._currBlock.length - this._currBlockPtr, n2);
            int n4 = n3;
            n = n2;
            if (min > 0) {
                System.arraycopy(array, n3, this._currBlock, this._currBlockPtr, min);
                n4 = n3 + min;
                this._currBlockPtr += min;
                n = n2 - min;
            }
            if (n <= 0) {
                break;
            }
            this._allocMore();
            n3 = n4;
            n2 = n;
        }
    }
}
