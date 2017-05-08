// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.extractor.ts;

import java.util.List;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.util.ParsableByteArray;
import com.google.android.exoplayer.util.MpegAudioHeader;

final class MpegAudioReader extends ElementaryStreamReader
{
    private int frameBytesRead;
    private long frameDurationUs;
    private int frameSize;
    private boolean hasOutputFormat;
    private final MpegAudioHeader header;
    private final ParsableByteArray headerScratch;
    private boolean lastByteWasFF;
    private int state;
    private long timeUs;
    
    public MpegAudioReader(final TrackOutput trackOutput) {
        super(trackOutput);
        this.state = 0;
        this.headerScratch = new ParsableByteArray(4);
        this.headerScratch.data[0] = -1;
        this.header = new MpegAudioHeader();
    }
    
    private void findHeader(final ParsableByteArray parsableByteArray) {
        final byte[] data = parsableByteArray.data;
        int i;
        int limit;
        for (i = parsableByteArray.getPosition(), limit = parsableByteArray.limit(); i < limit; ++i) {
            final boolean lastByteWasFF = (data[i] & 0xFF) == 0xFF;
            int n;
            if (this.lastByteWasFF && (data[i] & 0xE0) == 0xE0) {
                n = 1;
            }
            else {
                n = 0;
            }
            this.lastByteWasFF = lastByteWasFF;
            if (n != 0) {
                parsableByteArray.setPosition(i + 1);
                this.lastByteWasFF = false;
                this.headerScratch.data[1] = data[i];
                this.frameBytesRead = 2;
                this.state = 1;
                return;
            }
        }
        parsableByteArray.setPosition(limit);
    }
    
    private void readFrameRemainder(final ParsableByteArray parsableByteArray) {
        final int min = Math.min(parsableByteArray.bytesLeft(), this.frameSize - this.frameBytesRead);
        this.output.sampleData(parsableByteArray, min);
        this.frameBytesRead += min;
        if (this.frameBytesRead < this.frameSize) {
            return;
        }
        this.output.sampleMetadata(this.timeUs, 1, this.frameSize, 0, null);
        this.timeUs += this.frameDurationUs;
        this.frameBytesRead = 0;
        this.state = 0;
    }
    
    private void readHeaderRemainder(final ParsableByteArray parsableByteArray) {
        final int min = Math.min(parsableByteArray.bytesLeft(), 4 - this.frameBytesRead);
        parsableByteArray.readBytes(this.headerScratch.data, this.frameBytesRead, min);
        this.frameBytesRead += min;
        if (this.frameBytesRead < 4) {
            return;
        }
        this.headerScratch.setPosition(0);
        if (!MpegAudioHeader.populateHeader(this.headerScratch.readInt(), this.header)) {
            this.frameBytesRead = 0;
            this.state = 1;
            return;
        }
        this.frameSize = this.header.frameSize;
        if (!this.hasOutputFormat) {
            this.frameDurationUs = 1000000L * this.header.samplesPerFrame / this.header.sampleRate;
            this.output.format(MediaFormat.createAudioFormat(-1, this.header.mimeType, -1, 4096, -1L, this.header.channels, this.header.sampleRate, null, null));
            this.hasOutputFormat = true;
        }
        this.headerScratch.setPosition(0);
        this.output.sampleData(this.headerScratch, 4);
        this.state = 2;
    }
    
    @Override
    public void consume(final ParsableByteArray parsableByteArray, final long timeUs, final boolean b) {
        if (b) {
            this.timeUs = timeUs;
        }
        while (parsableByteArray.bytesLeft() > 0) {
            switch (this.state) {
                default: {
                    continue;
                }
                case 0: {
                    this.findHeader(parsableByteArray);
                    continue;
                }
                case 1: {
                    this.readHeaderRemainder(parsableByteArray);
                    continue;
                }
                case 2: {
                    this.readFrameRemainder(parsableByteArray);
                    continue;
                }
            }
        }
    }
    
    @Override
    public void packetFinished() {
    }
}
