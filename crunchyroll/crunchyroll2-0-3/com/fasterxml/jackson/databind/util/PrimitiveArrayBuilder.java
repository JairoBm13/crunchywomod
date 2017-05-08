// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.util;

public abstract class PrimitiveArrayBuilder<T>
{
    protected Node<T> _bufferHead;
    protected Node<T> _bufferTail;
    protected int _bufferedEntryCount;
    protected T _freeBuffer;
    
    protected abstract T _constructArray(final int p0);
    
    protected void _reset() {
        if (this._bufferTail != null) {
            this._freeBuffer = this._bufferTail.getData();
        }
        this._bufferTail = null;
        this._bufferHead = null;
        this._bufferedEntryCount = 0;
    }
    
    public final T appendCompletedChunk(final T t, int n) {
        final Node<T> bufferTail = new Node<T>(t, n);
        if (this._bufferHead == null) {
            this._bufferTail = bufferTail;
            this._bufferHead = bufferTail;
        }
        else {
            this._bufferTail.linkNext(bufferTail);
            this._bufferTail = bufferTail;
        }
        this._bufferedEntryCount += n;
        if (n < 16384) {
            n += n;
        }
        else {
            n += n >> 2;
        }
        return this._constructArray(n);
    }
    
    public T completeAndClearBuffer(final T t, int n) {
        final int n2 = n + this._bufferedEntryCount;
        final T constructArray = this._constructArray(n2);
        Node<T> node = this._bufferHead;
        int copyData = 0;
        while (node != null) {
            copyData = node.copyData(constructArray, copyData);
            node = node.next();
        }
        System.arraycopy(t, 0, constructArray, copyData, n);
        n += copyData;
        if (n != n2) {
            throw new IllegalStateException("Should have gotten " + n2 + " entries, got " + n);
        }
        return constructArray;
    }
    
    public T resetAndStart() {
        this._reset();
        if (this._freeBuffer == null) {
            return this._constructArray(12);
        }
        return this._freeBuffer;
    }
    
    static final class Node<T>
    {
        final T _data;
        final int _dataLength;
        Node<T> _next;
        
        public Node(final T data, final int dataLength) {
            this._data = data;
            this._dataLength = dataLength;
        }
        
        public int copyData(final T t, final int n) {
            System.arraycopy(this._data, 0, t, n, this._dataLength);
            return this._dataLength + n;
        }
        
        public T getData() {
            return this._data;
        }
        
        public void linkNext(final Node<T> next) {
            if (this._next != null) {
                throw new IllegalStateException();
            }
            this._next = next;
        }
        
        public Node<T> next() {
            return this._next;
        }
    }
}
