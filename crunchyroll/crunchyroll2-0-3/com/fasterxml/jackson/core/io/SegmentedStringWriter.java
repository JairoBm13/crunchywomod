// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.io;

import java.io.IOException;
import com.fasterxml.jackson.core.util.TextBuffer;
import java.io.Writer;

public final class SegmentedStringWriter extends Writer
{
    protected final TextBuffer _buffer;
    
    @Override
    public Writer append(final char c) {
        this.write(c);
        return this;
    }
    
    @Override
    public Writer append(final CharSequence charSequence) {
        final String string = charSequence.toString();
        this._buffer.append(string, 0, string.length());
        return this;
    }
    
    @Override
    public Writer append(final CharSequence charSequence, final int n, final int n2) {
        final String string = charSequence.subSequence(n, n2).toString();
        this._buffer.append(string, 0, string.length());
        return this;
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public void flush() {
    }
    
    @Override
    public void write(final int n) {
        this._buffer.append((char)n);
    }
    
    @Override
    public void write(final String s) {
        this._buffer.append(s, 0, s.length());
    }
    
    @Override
    public void write(final String s, final int n, final int n2) {
        this._buffer.append(s, n, n2);
    }
    
    @Override
    public void write(final char[] array) {
        this._buffer.append(array, 0, array.length);
    }
    
    @Override
    public void write(final char[] array, final int n, final int n2) {
        this._buffer.append(array, n, n2);
    }
}
