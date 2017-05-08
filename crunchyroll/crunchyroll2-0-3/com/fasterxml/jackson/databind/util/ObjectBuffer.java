// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.util;

import java.lang.reflect.Array;
import java.util.List;

public final class ObjectBuffer
{
    private Node _bufferHead;
    private Node _bufferTail;
    private int _bufferedEntryCount;
    private Object[] _freeBuffer;
    
    protected final void _copyTo(final Object o, final int n, final Object[] array, int n2) {
        Node node = this._bufferHead;
        int n3 = 0;
        while (node != null) {
            final Object[] data = node.getData();
            final int length = data.length;
            System.arraycopy(data, 0, o, n3, length);
            n3 += length;
            node = node.next();
        }
        System.arraycopy(array, 0, o, n3, n2);
        n2 += n3;
        if (n2 != n) {
            throw new IllegalStateException("Should have gotten " + n + " entries, got " + n2);
        }
    }
    
    protected void _reset() {
        if (this._bufferTail != null) {
            this._freeBuffer = this._bufferTail.getData();
        }
        this._bufferTail = null;
        this._bufferHead = null;
        this._bufferedEntryCount = 0;
    }
    
    public Object[] appendCompletedChunk(final Object[] array) {
        final Node bufferTail = new Node(array);
        if (this._bufferHead == null) {
            this._bufferTail = bufferTail;
            this._bufferHead = bufferTail;
        }
        else {
            this._bufferTail.linkNext(bufferTail);
            this._bufferTail = bufferTail;
        }
        final int length = array.length;
        this._bufferedEntryCount += length;
        int n;
        if (length < 16384) {
            n = length + length;
        }
        else {
            n = length + (length >> 2);
        }
        return new Object[n];
    }
    
    public void completeAndClearBuffer(final Object[] array, final int n, final List<Object> list) {
        final int n2 = 0;
        Node node = this._bufferHead;
        int i;
        while (true) {
            i = n2;
            if (node == null) {
                break;
            }
            final Object[] data = node.getData();
            for (int length = data.length, j = 0; j < length; ++j) {
                list.add(data[j]);
            }
            node = node.next();
        }
        while (i < n) {
            list.add(array[i]);
            ++i;
        }
    }
    
    public Object[] completeAndClearBuffer(final Object[] array, final int n) {
        final int n2 = this._bufferedEntryCount + n;
        final Object[] array2 = new Object[n2];
        this._copyTo(array2, n2, array, n);
        return array2;
    }
    
    public <T> T[] completeAndClearBuffer(final Object[] array, final int n, final Class<T> clazz) {
        final int n2 = n + this._bufferedEntryCount;
        final Object[] array2 = (Object[])Array.newInstance(clazz, n2);
        this._copyTo(array2, n2, array, n);
        this._reset();
        return (T[])array2;
    }
    
    public int initialCapacity() {
        if (this._freeBuffer == null) {
            return 0;
        }
        return this._freeBuffer.length;
    }
    
    public Object[] resetAndStart() {
        this._reset();
        if (this._freeBuffer == null) {
            return new Object[12];
        }
        return this._freeBuffer;
    }
    
    static final class Node
    {
        final Object[] _data;
        Node _next;
        
        public Node(final Object[] data) {
            this._data = data;
        }
        
        public Object[] getData() {
            return this._data;
        }
        
        public void linkNext(final Node next) {
            if (this._next != null) {
                throw new IllegalStateException();
            }
            this._next = next;
        }
        
        public Node next() {
            return this._next;
        }
    }
}
