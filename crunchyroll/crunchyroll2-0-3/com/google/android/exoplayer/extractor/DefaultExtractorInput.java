// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.extractor;

import java.io.EOFException;
import java.io.IOException;
import com.google.android.exoplayer.upstream.DataSource;

public final class DefaultExtractorInput implements ExtractorInput
{
    private static final byte[] SCRATCH_SPACE;
    private final DataSource dataSource;
    private byte[] peekBuffer;
    private int peekBufferLength;
    private int peekBufferPosition;
    private long position;
    private final long streamLength;
    
    static {
        SCRATCH_SPACE = new byte[4096];
    }
    
    public DefaultExtractorInput(final DataSource dataSource, final long position, final long streamLength) {
        this.dataSource = dataSource;
        this.position = position;
        this.streamLength = streamLength;
        this.peekBuffer = new byte[8192];
    }
    
    private void updatePeekBuffer(final int n) {
        this.peekBufferLength -= n;
        this.peekBufferPosition = 0;
        System.arraycopy(this.peekBuffer, n, this.peekBuffer, 0, this.peekBufferLength);
    }
    
    @Override
    public long getLength() {
        return this.streamLength;
    }
    
    @Override
    public long getPosition() {
        return this.position;
    }
    
    @Override
    public int read(final byte[] array, int n, int read) throws IOException, InterruptedException {
        final int n2 = 0;
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        final int min = Math.min(this.peekBufferLength, read);
        System.arraycopy(this.peekBuffer, 0, array, n, min);
        final int n3 = read - min;
        read = n2;
        if (n3 != 0) {
            read = this.dataSource.read(array, n + min, n3);
        }
        if (read == -1) {
            return -1;
        }
        this.updatePeekBuffer(min);
        n = read + min;
        this.position += n;
        return n;
    }
    
    @Override
    public void readFully(final byte[] array, final int n, final int n2) throws IOException, InterruptedException {
        this.readFully(array, n, n2, false);
    }
    
    @Override
    public boolean readFully(final byte[] array, int i, final int n, final boolean b) throws IOException, InterruptedException {
        final int min = Math.min(this.peekBufferLength, n);
        System.arraycopy(this.peekBuffer, 0, array, i, min);
        int n2 = i + min;
        i = n - min;
        while (i > 0) {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            final int read = this.dataSource.read(array, n2, i);
            if (read == -1) {
                if (b && i == n) {
                    return false;
                }
                throw new EOFException();
            }
            else {
                n2 += read;
                i -= read;
            }
        }
        this.updatePeekBuffer(min);
        this.position += n;
        return true;
    }
    
    @Override
    public void skipFully(final int n) throws IOException, InterruptedException {
        final int min = Math.min(this.peekBufferLength, n);
        int read;
        for (int i = n - min; i > 0; i -= read) {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            read = this.dataSource.read(DefaultExtractorInput.SCRATCH_SPACE, 0, Math.min(DefaultExtractorInput.SCRATCH_SPACE.length, i));
            if (read == -1) {
                throw new EOFException();
            }
        }
        this.updatePeekBuffer(min);
        this.position += n;
    }
}
