// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.extractor.ts;

import com.google.android.exoplayer.util.Ac3Util;
import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.util.ParsableByteArray;
import com.google.android.exoplayer.util.ParsableBitArray;

final class Ac3Reader extends ElementaryStreamReader
{
    private int bitrate;
    private int bytesRead;
    private long frameDurationUs;
    private final ParsableBitArray headerScratchBits;
    private final ParsableByteArray headerScratchBytes;
    private boolean lastByteWas0B;
    private MediaFormat mediaFormat;
    private int sampleSize;
    private int state;
    private long timeUs;
    
    public Ac3Reader(final TrackOutput trackOutput) {
        super(trackOutput);
        this.headerScratchBits = new ParsableBitArray(new byte[8]);
        this.headerScratchBytes = new ParsableByteArray(this.headerScratchBits.data);
        this.state = 0;
    }
    
    private boolean continueRead(final ParsableByteArray parsableByteArray, final byte[] array, final int n) {
        final int min = Math.min(parsableByteArray.bytesLeft(), n - this.bytesRead);
        parsableByteArray.readBytes(array, this.bytesRead, min);
        this.bytesRead += min;
        return this.bytesRead == n;
    }
    
    private void parseHeader() {
        this.headerScratchBits.setPosition(0);
        this.sampleSize = Ac3Util.parseFrameSize(this.headerScratchBits);
        if (this.mediaFormat == null) {
            this.headerScratchBits.setPosition(0);
            this.mediaFormat = Ac3Util.parseFrameAc3Format(this.headerScratchBits, -1, -1L, null);
            this.output.format(this.mediaFormat);
            this.bitrate = Ac3Util.getBitrate(this.sampleSize, this.mediaFormat.sampleRate);
        }
        this.frameDurationUs = (int)(8000L * this.sampleSize / this.bitrate);
    }
    
    private boolean skipToNextSync(final ParsableByteArray parsableByteArray) {
        while (parsableByteArray.bytesLeft() > 0) {
            if (!this.lastByteWas0B) {
                this.lastByteWas0B = (parsableByteArray.readUnsignedByte() == 11);
            }
            else {
                final int unsignedByte = parsableByteArray.readUnsignedByte();
                if (unsignedByte == 119) {
                    this.lastByteWas0B = false;
                    return true;
                }
                this.lastByteWas0B = (unsignedByte == 11);
            }
        }
        return false;
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
                    if (this.skipToNextSync(parsableByteArray)) {
                        this.state = 1;
                        this.headerScratchBytes.data[0] = 11;
                        this.headerScratchBytes.data[1] = 119;
                        this.bytesRead = 2;
                        continue;
                    }
                    continue;
                }
                case 1: {
                    if (this.continueRead(parsableByteArray, this.headerScratchBytes.data, 8)) {
                        this.parseHeader();
                        this.headerScratchBytes.setPosition(0);
                        this.output.sampleData(this.headerScratchBytes, 8);
                        this.state = 2;
                        continue;
                    }
                    continue;
                }
                case 2: {
                    final int min = Math.min(parsableByteArray.bytesLeft(), this.sampleSize - this.bytesRead);
                    this.output.sampleData(parsableByteArray, min);
                    this.bytesRead += min;
                    if (this.bytesRead == this.sampleSize) {
                        this.output.sampleMetadata(this.timeUs, 1, this.sampleSize, 0, null);
                        this.timeUs += this.frameDurationUs;
                        this.state = 0;
                        continue;
                    }
                    continue;
                }
            }
        }
    }
    
    @Override
    public void packetFinished() {
    }
}
