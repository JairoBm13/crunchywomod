// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.extractor.ts;

import com.google.android.exoplayer.util.ParsableByteArray;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.extractor.TrackOutput;

final class Id3Reader extends ElementaryStreamReader
{
    private int sampleSize;
    private long sampleTimeUs;
    private boolean writingSample;
    
    public Id3Reader(final TrackOutput trackOutput) {
        super(trackOutput);
        trackOutput.format(MediaFormat.createFormatForMimeType(-1, "application/id3", -1, -1L));
    }
    
    @Override
    public void consume(final ParsableByteArray parsableByteArray, final long sampleTimeUs, final boolean b) {
        if (b) {
            this.writingSample = true;
            this.sampleTimeUs = sampleTimeUs;
            this.sampleSize = 0;
        }
        if (this.writingSample) {
            this.sampleSize += parsableByteArray.bytesLeft();
            this.output.sampleData(parsableByteArray, parsableByteArray.bytesLeft());
        }
    }
    
    @Override
    public void packetFinished() {
        this.output.sampleMetadata(this.sampleTimeUs, 1, this.sampleSize, 0, null);
        this.writingSample = false;
    }
}
