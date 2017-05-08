// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.io.NumberInput;
import java.math.BigDecimal;
import java.util.ArrayList;

public final class TextBuffer
{
    static final char[] NO_CHARS;
    private final BufferRecycler _allocator;
    private char[] _currentSegment;
    private int _currentSize;
    private boolean _hasSegments;
    private char[] _inputBuffer;
    private int _inputLen;
    private int _inputStart;
    private char[] _resultArray;
    private String _resultString;
    private int _segmentSize;
    private ArrayList<char[]> _segments;
    
    static {
        NO_CHARS = new char[0];
    }
    
    public TextBuffer(final BufferRecycler allocator) {
        this._hasSegments = false;
        this._allocator = allocator;
    }
    
    private char[] _charArray(final int n) {
        return new char[n];
    }
    
    private char[] buildResultArray() {
        if (this._resultString != null) {
            return this._resultString.toCharArray();
        }
        if (this._inputStart >= 0) {
            if (this._inputLen < 1) {
                return TextBuffer.NO_CHARS;
            }
            final char[] charArray = this._charArray(this._inputLen);
            System.arraycopy(this._inputBuffer, this._inputStart, charArray, 0, this._inputLen);
            return charArray;
        }
        else {
            final int size = this.size();
            if (size < 1) {
                return TextBuffer.NO_CHARS;
            }
            final char[] charArray2 = this._charArray(size);
            int n;
            if (this._segments != null) {
                final int size2 = this._segments.size();
                int i = 0;
                n = 0;
                while (i < size2) {
                    final char[] array = this._segments.get(i);
                    final int length = array.length;
                    System.arraycopy(array, 0, charArray2, n, length);
                    n += length;
                    ++i;
                }
            }
            else {
                n = 0;
            }
            System.arraycopy(this._currentSegment, 0, charArray2, n, this._currentSize);
            return charArray2;
        }
    }
    
    private void clearSegments() {
        this._hasSegments = false;
        this._segments.clear();
        this._segmentSize = 0;
        this._currentSize = 0;
    }
    
    private void expand(int n) {
        if (this._segments == null) {
            this._segments = new ArrayList<char[]>();
        }
        final char[] currentSegment = this._currentSegment;
        this._hasSegments = true;
        this._segments.add(currentSegment);
        this._segmentSize += currentSegment.length;
        final int length = currentSegment.length;
        final int n2 = length >> 1;
        if (n2 >= n) {
            n = n2;
        }
        final char[] charArray = this._charArray(Math.min(262144, length + n));
        this._currentSize = 0;
        this._currentSegment = charArray;
    }
    
    private char[] findBuffer(final int n) {
        if (this._allocator != null) {
            return this._allocator.allocCharBuffer(BufferRecycler.CharBufferType.TEXT_BUFFER, n);
        }
        return new char[Math.max(n, 1000)];
    }
    
    private void unshare(int n) {
        final int inputLen = this._inputLen;
        this._inputLen = 0;
        final char[] inputBuffer = this._inputBuffer;
        this._inputBuffer = null;
        final int inputStart = this._inputStart;
        this._inputStart = -1;
        n += inputLen;
        if (this._currentSegment == null || n > this._currentSegment.length) {
            this._currentSegment = this.findBuffer(n);
        }
        if (inputLen > 0) {
            System.arraycopy(inputBuffer, inputStart, this._currentSegment, 0, inputLen);
        }
        this._segmentSize = 0;
        this._currentSize = inputLen;
    }
    
    public void append(final char c) {
        if (this._inputStart >= 0) {
            this.unshare(16);
        }
        this._resultString = null;
        this._resultArray = null;
        char[] array;
        if (this._currentSize >= (array = this._currentSegment).length) {
            this.expand(1);
            array = this._currentSegment;
        }
        array[this._currentSize++] = c;
    }
    
    public void append(final String s, int min, final int n) {
        if (this._inputStart >= 0) {
            this.unshare(n);
        }
        this._resultString = null;
        this._resultArray = null;
        final char[] currentSegment = this._currentSegment;
        final int n2 = currentSegment.length - this._currentSize;
        if (n2 >= n) {
            s.getChars(min, min + n, currentSegment, this._currentSize);
            this._currentSize += n;
            return;
        }
        int n3 = min;
        int n4 = n;
        if (n2 > 0) {
            s.getChars(min, min + n2, currentSegment, this._currentSize);
            n4 = n - n2;
            n3 = min + n2;
        }
        do {
            this.expand(n4);
            min = Math.min(this._currentSegment.length, n4);
            s.getChars(n3, n3 + min, this._currentSegment, 0);
            this._currentSize += min;
            n3 += min;
            min = n4 - min;
        } while ((n4 = min) > 0);
    }
    
    public void append(final char[] array, int min, final int n) {
        if (this._inputStart >= 0) {
            this.unshare(n);
        }
        this._resultString = null;
        this._resultArray = null;
        final char[] currentSegment = this._currentSegment;
        final int n2 = currentSegment.length - this._currentSize;
        if (n2 >= n) {
            System.arraycopy(array, min, currentSegment, this._currentSize, n);
            this._currentSize += n;
            return;
        }
        int n3 = min;
        int n4 = n;
        if (n2 > 0) {
            System.arraycopy(array, min, currentSegment, this._currentSize, n2);
            n3 = min + n2;
            n4 = n - n2;
        }
        do {
            this.expand(n4);
            min = Math.min(this._currentSegment.length, n4);
            System.arraycopy(array, n3, this._currentSegment, 0, min);
            this._currentSize += min;
            n3 += min;
            min = n4 - min;
        } while ((n4 = min) > 0);
    }
    
    public char[] contentsAsArray() {
        char[] resultArray;
        if ((resultArray = this._resultArray) == null) {
            resultArray = this.buildResultArray();
            this._resultArray = resultArray;
        }
        return resultArray;
    }
    
    public BigDecimal contentsAsDecimal() throws NumberFormatException {
        if (this._resultArray != null) {
            return new BigDecimal(this._resultArray);
        }
        if (this._inputStart >= 0) {
            return new BigDecimal(this._inputBuffer, this._inputStart, this._inputLen);
        }
        if (this._segmentSize == 0) {
            return new BigDecimal(this._currentSegment, 0, this._currentSize);
        }
        return new BigDecimal(this.contentsAsArray());
    }
    
    public double contentsAsDouble() throws NumberFormatException {
        return NumberInput.parseDouble(this.contentsAsString());
    }
    
    public String contentsAsString() {
        if (this._resultString == null) {
            if (this._resultArray != null) {
                this._resultString = new String(this._resultArray);
            }
            else if (this._inputStart >= 0) {
                if (this._inputLen < 1) {
                    return this._resultString = "";
                }
                this._resultString = new String(this._inputBuffer, this._inputStart, this._inputLen);
            }
            else {
                final int segmentSize = this._segmentSize;
                final int currentSize = this._currentSize;
                if (segmentSize == 0) {
                    String resultString;
                    if (currentSize == 0) {
                        resultString = "";
                    }
                    else {
                        resultString = new String(this._currentSegment, 0, currentSize);
                    }
                    this._resultString = resultString;
                }
                else {
                    final StringBuilder sb = new StringBuilder(segmentSize + currentSize);
                    if (this._segments != null) {
                        for (int size = this._segments.size(), i = 0; i < size; ++i) {
                            final char[] array = this._segments.get(i);
                            sb.append(array, 0, array.length);
                        }
                    }
                    sb.append(this._currentSegment, 0, this._currentSize);
                    this._resultString = sb.toString();
                }
            }
        }
        return this._resultString;
    }
    
    public char[] emptyAndGetCurrentSegment() {
        this._inputStart = -1;
        this._currentSize = 0;
        this._inputLen = 0;
        this._inputBuffer = null;
        this._resultString = null;
        this._resultArray = null;
        if (this._hasSegments) {
            this.clearSegments();
        }
        char[] currentSegment;
        if ((currentSegment = this._currentSegment) == null) {
            currentSegment = this.findBuffer(0);
            this._currentSegment = currentSegment;
        }
        return currentSegment;
    }
    
    public char[] expandCurrentSegment() {
        final char[] currentSegment = this._currentSegment;
        final int length = currentSegment.length;
        int min;
        if (length == 262144) {
            min = 262145;
        }
        else {
            min = Math.min(262144, (length >> 1) + length);
        }
        System.arraycopy(currentSegment, 0, this._currentSegment = this._charArray(min), 0, length);
        return this._currentSegment;
    }
    
    public char[] finishCurrentSegment() {
        if (this._segments == null) {
            this._segments = new ArrayList<char[]>();
        }
        this._hasSegments = true;
        this._segments.add(this._currentSegment);
        final int length = this._currentSegment.length;
        this._segmentSize += length;
        final char[] charArray = this._charArray(Math.min(length + (length >> 1), 262144));
        this._currentSize = 0;
        return this._currentSegment = charArray;
    }
    
    public char[] getCurrentSegment() {
        if (this._inputStart >= 0) {
            this.unshare(1);
        }
        else {
            final char[] currentSegment = this._currentSegment;
            if (currentSegment == null) {
                this._currentSegment = this.findBuffer(0);
            }
            else if (this._currentSize >= currentSegment.length) {
                this.expand(1);
            }
        }
        return this._currentSegment;
    }
    
    public int getCurrentSegmentSize() {
        return this._currentSize;
    }
    
    public char[] getTextBuffer() {
        if (this._inputStart >= 0) {
            return this._inputBuffer;
        }
        if (this._resultArray != null) {
            return this._resultArray;
        }
        if (this._resultString != null) {
            return this._resultArray = this._resultString.toCharArray();
        }
        if (!this._hasSegments) {
            return this._currentSegment;
        }
        return this.contentsAsArray();
    }
    
    public int getTextOffset() {
        if (this._inputStart >= 0) {
            return this._inputStart;
        }
        return 0;
    }
    
    public void releaseBuffers() {
        if (this._allocator == null) {
            this.resetWithEmpty();
        }
        else if (this._currentSegment != null) {
            this.resetWithEmpty();
            final char[] currentSegment = this._currentSegment;
            this._currentSegment = null;
            this._allocator.releaseCharBuffer(BufferRecycler.CharBufferType.TEXT_BUFFER, currentSegment);
        }
    }
    
    public void resetWithCopy(final char[] array, final int n, final int n2) {
        this._inputBuffer = null;
        this._inputStart = -1;
        this._inputLen = 0;
        this._resultString = null;
        this._resultArray = null;
        if (this._hasSegments) {
            this.clearSegments();
        }
        else if (this._currentSegment == null) {
            this._currentSegment = this.findBuffer(n2);
        }
        this._segmentSize = 0;
        this._currentSize = 0;
        this.append(array, n, n2);
    }
    
    public void resetWithEmpty() {
        this._inputStart = -1;
        this._currentSize = 0;
        this._inputLen = 0;
        this._inputBuffer = null;
        this._resultString = null;
        this._resultArray = null;
        if (this._hasSegments) {
            this.clearSegments();
        }
    }
    
    public void resetWithShared(final char[] inputBuffer, final int inputStart, final int inputLen) {
        this._resultString = null;
        this._resultArray = null;
        this._inputBuffer = inputBuffer;
        this._inputStart = inputStart;
        this._inputLen = inputLen;
        if (this._hasSegments) {
            this.clearSegments();
        }
    }
    
    public void resetWithString(final String resultString) {
        this._inputBuffer = null;
        this._inputStart = -1;
        this._inputLen = 0;
        this._resultString = resultString;
        this._resultArray = null;
        if (this._hasSegments) {
            this.clearSegments();
        }
        this._currentSize = 0;
    }
    
    public void setCurrentLength(final int currentSize) {
        this._currentSize = currentSize;
    }
    
    public int size() {
        if (this._inputStart >= 0) {
            return this._inputLen;
        }
        if (this._resultArray != null) {
            return this._resultArray.length;
        }
        if (this._resultString != null) {
            return this._resultString.length();
        }
        return this._segmentSize + this._currentSize;
    }
    
    @Override
    public String toString() {
        return this.contentsAsString();
    }
}
