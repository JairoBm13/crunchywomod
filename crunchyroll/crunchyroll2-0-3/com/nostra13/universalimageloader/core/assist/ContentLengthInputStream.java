// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.core.assist;

import java.io.IOException;
import java.io.InputStream;

public class ContentLengthInputStream extends InputStream
{
    private final int length;
    private final InputStream stream;
    
    public ContentLengthInputStream(final InputStream stream, final int length) {
        this.stream = stream;
        this.length = length;
    }
    
    @Override
    public int available() {
        return this.length;
    }
    
    @Override
    public void close() throws IOException {
        this.stream.close();
    }
    
    @Override
    public void mark(final int n) {
        this.stream.mark(n);
    }
    
    @Override
    public boolean markSupported() {
        return this.stream.markSupported();
    }
    
    @Override
    public int read() throws IOException {
        return this.stream.read();
    }
    
    @Override
    public int read(final byte[] array) throws IOException {
        return this.stream.read(array);
    }
    
    @Override
    public int read(final byte[] array, final int n, final int n2) throws IOException {
        return this.stream.read(array, n, n2);
    }
    
    @Override
    public void reset() throws IOException {
        this.stream.reset();
    }
    
    @Override
    public long skip(final long n) throws IOException {
        return this.stream.skip(n);
    }
}
