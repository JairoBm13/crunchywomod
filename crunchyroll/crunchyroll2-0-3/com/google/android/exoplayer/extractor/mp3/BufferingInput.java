// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.extractor.mp3;

import java.io.EOFException;
import com.google.android.exoplayer.extractor.TrackOutput;
import java.io.IOException;
import java.nio.BufferOverflowException;
import com.google.android.exoplayer.extractor.ExtractorInput;
import com.google.android.exoplayer.util.ParsableByteArray;

final class BufferingInput
{
    private final ParsableByteArray buffer;
    private final int capacity;
    private int markPosition;
    private int readPosition;
    private int writePosition;
    
    public BufferingInput(final int capacity) {
        this.capacity = capacity;
        this.buffer = new ParsableByteArray(capacity * 2);
    }
    
    private boolean ensureLoaded(final ExtractorInput extractorInput, int n) throws InterruptedException, IOException {
        boolean b = true;
        if (this.readPosition + n - this.markPosition > this.capacity) {
            throw new BufferOverflowException();
        }
        n -= this.writePosition - this.readPosition;
        if (n > 0) {
            if (extractorInput.readFully(this.buffer.data, this.writePosition, n, true)) {
                this.writePosition += n;
                return true;
            }
            b = false;
        }
        return b;
    }
    
    private boolean readInternal(final ExtractorInput extractorInput, final byte[] array, final int n, final int n2) throws InterruptedException, IOException {
        if (!this.ensureLoaded(extractorInput, n2)) {
            return false;
        }
        if (array != null) {
            System.arraycopy(this.buffer.data, this.readPosition, array, n, n2);
        }
        this.readPosition += n2;
        return true;
    }
    
    public int drainToOutput(final TrackOutput trackOutput, int min) {
        if (min == 0) {
            return 0;
        }
        this.buffer.setPosition(this.readPosition);
        min = Math.min(this.writePosition - this.readPosition, min);
        trackOutput.sampleData(this.buffer, min);
        this.readPosition += min;
        return min;
    }
    
    public int getAvailableByteCount() {
        return this.writePosition - this.readPosition;
    }
    
    public ParsableByteArray getParsableByteArray(final ExtractorInput extractorInput, final int n) throws IOException, InterruptedException {
        if (!this.ensureLoaded(extractorInput, n)) {
            throw new EOFException();
        }
        final ParsableByteArray parsableByteArray = new ParsableByteArray(this.buffer.data, this.writePosition);
        parsableByteArray.setPosition(this.readPosition);
        this.readPosition += n;
        return parsableByteArray;
    }
    
    public void mark() {
        if (this.readPosition > this.capacity) {
            System.arraycopy(this.buffer.data, this.readPosition, this.buffer.data, 0, this.writePosition - this.readPosition);
            this.writePosition -= this.readPosition;
            this.readPosition = 0;
        }
        this.markPosition = this.readPosition;
    }
    
    public void read(final ExtractorInput extractorInput, final byte[] array, final int n, final int n2) throws IOException, InterruptedException {
        if (!this.readInternal(extractorInput, array, n, n2)) {
            throw new EOFException();
        }
    }
    
    public boolean readAllowingEndOfInput(final ExtractorInput extractorInput, final byte[] array, final int n, final int n2) throws IOException, InterruptedException {
        return this.readInternal(extractorInput, array, n, n2);
    }
    
    public void reset() {
        this.readPosition = 0;
        this.writePosition = 0;
        this.markPosition = 0;
    }
    
    public void returnToMark() {
        this.readPosition = this.markPosition;
    }
    
    public void skip(final ExtractorInput extractorInput, final int n) throws IOException, InterruptedException {
        if (!this.readInternal(extractorInput, null, 0, n)) {
            throw new EOFException();
        }
    }
}
