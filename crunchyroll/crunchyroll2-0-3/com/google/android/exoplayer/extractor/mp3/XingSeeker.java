// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.extractor.mp3;

import com.google.android.exoplayer.util.Util;
import com.google.android.exoplayer.util.ParsableByteArray;
import com.google.android.exoplayer.util.MpegAudioHeader;

final class XingSeeker implements Seeker
{
    private final long durationUs;
    private final long firstFramePosition;
    private final long inputLength;
    private final long sizeBytes;
    private final long[] tableOfContents;
    
    private XingSeeker(final long n, final long n2, final long n3) {
        this(n, n2, n3, null, 0L);
    }
    
    private XingSeeker(final long inputLength, final long firstFramePosition, final long durationUs, final long[] tableOfContents, final long sizeBytes) {
        this.tableOfContents = tableOfContents;
        this.firstFramePosition = firstFramePosition;
        this.sizeBytes = sizeBytes;
        this.durationUs = durationUs;
        this.inputLength = inputLength;
    }
    
    public static XingSeeker create(final MpegAudioHeader mpegAudioHeader, final ParsableByteArray parsableByteArray, long n, final long n2) {
        final int samplesPerFrame = mpegAudioHeader.samplesPerFrame;
        final int sampleRate = mpegAudioHeader.sampleRate;
        n += mpegAudioHeader.frameSize;
        final int int1 = parsableByteArray.readInt();
        if ((int1 & 0x1) == 0x1) {
            final int unsignedIntToInt = parsableByteArray.readUnsignedIntToInt();
            if (unsignedIntToInt != 0) {
                final long scaleLargeTimestamp = Util.scaleLargeTimestamp(unsignedIntToInt, samplesPerFrame * 1000000L, sampleRate);
                if ((int1 & 0x6) != 0x6) {
                    return new XingSeeker(n2, n, scaleLargeTimestamp);
                }
                final long n3 = parsableByteArray.readUnsignedIntToInt();
                parsableByteArray.skipBytes(1);
                final long[] array = new long[99];
                for (int i = 0; i < 99; ++i) {
                    array[i] = parsableByteArray.readUnsignedByte();
                }
                return new XingSeeker(n2, n, scaleLargeTimestamp, array, n3);
            }
        }
        return null;
    }
    
    private long getTimeUsForTocIndex(final int n) {
        return this.durationUs * (n + 1) / 100L;
    }
    
    @Override
    public long getDurationUs() {
        return this.durationUs;
    }
    
    @Override
    public long getTimeUs(long timeUsForTocIndex) {
        if (!this.isSeekable()) {
            timeUsForTocIndex = 0L;
        }
        else {
            final double n = 256.0 * (timeUsForTocIndex - this.firstFramePosition) / this.sizeBytes;
            final int binarySearchFloor = Util.binarySearchFloor(this.tableOfContents, (long)n, true, false);
            final long n2 = timeUsForTocIndex = this.getTimeUsForTocIndex(binarySearchFloor);
            if (binarySearchFloor != 98) {
                if (binarySearchFloor == -1) {
                    timeUsForTocIndex = 0L;
                }
                else {
                    timeUsForTocIndex = this.tableOfContents[binarySearchFloor];
                }
                final long n3 = this.tableOfContents[binarySearchFloor + 1];
                final long timeUsForTocIndex2 = this.getTimeUsForTocIndex(binarySearchFloor + 1);
                if (n3 == timeUsForTocIndex) {
                    timeUsForTocIndex = 0L;
                }
                else {
                    timeUsForTocIndex = (long)((timeUsForTocIndex2 - n2) * (n - timeUsForTocIndex) / (n3 - timeUsForTocIndex));
                }
                return n2 + timeUsForTocIndex;
            }
        }
        return timeUsForTocIndex;
    }
    
    public boolean isSeekable() {
        return this.tableOfContents != null;
    }
}
