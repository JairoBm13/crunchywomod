// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.extractor.ts;

import android.util.Pair;
import com.google.android.exoplayer.MediaFormat;
import java.util.Collections;
import com.google.android.exoplayer.util.CodecSpecificDataUtil;
import com.google.android.exoplayer.util.ParsableByteArray;
import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.util.ParsableBitArray;

final class AdtsReader extends ElementaryStreamReader
{
    private final ParsableBitArray adtsScratch;
    private int bytesRead;
    private long frameDurationUs;
    private boolean hasCrc;
    private boolean hasOutputFormat;
    private boolean lastByteWasFF;
    private int sampleSize;
    private int state;
    private long timeUs;
    
    public AdtsReader(final TrackOutput trackOutput) {
        super(trackOutput);
        this.adtsScratch = new ParsableBitArray(new byte[7]);
        this.state = 0;
    }
    
    private boolean continueRead(final ParsableByteArray parsableByteArray, final byte[] array, final int n) {
        final int min = Math.min(parsableByteArray.bytesLeft(), n - this.bytesRead);
        parsableByteArray.readBytes(array, this.bytesRead, min);
        this.bytesRead += min;
        return this.bytesRead == n;
    }
    
    private void parseHeader() {
        this.adtsScratch.setPosition(0);
        if (!this.hasOutputFormat) {
            final int bits = this.adtsScratch.readBits(2);
            final int bits2 = this.adtsScratch.readBits(4);
            this.adtsScratch.skipBits(1);
            final byte[] buildAacAudioSpecificConfig = CodecSpecificDataUtil.buildAacAudioSpecificConfig(bits + 1, bits2, this.adtsScratch.readBits(3));
            final Pair<Integer, Integer> aacAudioSpecificConfig = CodecSpecificDataUtil.parseAacAudioSpecificConfig(buildAacAudioSpecificConfig);
            final MediaFormat audioFormat = MediaFormat.createAudioFormat(-1, "audio/mp4a-latm", -1, -1, -1L, (int)aacAudioSpecificConfig.second, (int)aacAudioSpecificConfig.first, Collections.singletonList(buildAacAudioSpecificConfig), null);
            this.frameDurationUs = 1024000000L / audioFormat.sampleRate;
            this.output.format(audioFormat);
            this.hasOutputFormat = true;
        }
        else {
            this.adtsScratch.skipBits(10);
        }
        this.adtsScratch.skipBits(4);
        this.sampleSize = this.adtsScratch.readBits(13) - 2 - 5;
        if (this.hasCrc) {
            this.sampleSize -= 2;
        }
    }
    
    private boolean skipToNextSync(final ParsableByteArray parsableByteArray) {
        final byte[] data = parsableByteArray.data;
        int i;
        int limit;
        for (i = parsableByteArray.getPosition(), limit = parsableByteArray.limit(); i < limit; ++i) {
            final boolean lastByteWasFF = (data[i] & 0xFF) == 0xFF;
            int n;
            if (this.lastByteWasFF && !lastByteWasFF && (data[i] & 0xF0) == 0xF0) {
                n = 1;
            }
            else {
                n = 0;
            }
            this.lastByteWasFF = lastByteWasFF;
            if (n != 0) {
                this.hasCrc = ((data[i] & 0x1) == 0x0);
                parsableByteArray.setPosition(i + 1);
                this.lastByteWasFF = false;
                return true;
            }
        }
        parsableByteArray.setPosition(limit);
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
                        this.bytesRead = 0;
                        this.state = 1;
                        continue;
                    }
                    continue;
                }
                case 1: {
                    int n;
                    if (this.hasCrc) {
                        n = 7;
                    }
                    else {
                        n = 5;
                    }
                    if (this.continueRead(parsableByteArray, this.adtsScratch.data, n)) {
                        this.parseHeader();
                        this.bytesRead = 0;
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
                        this.bytesRead = 0;
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
