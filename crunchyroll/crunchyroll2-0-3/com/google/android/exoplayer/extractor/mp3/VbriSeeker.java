// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.extractor.mp3;

import com.google.android.exoplayer.util.Util;
import com.google.android.exoplayer.util.ParsableByteArray;
import com.google.android.exoplayer.util.MpegAudioHeader;

final class VbriSeeker implements Seeker
{
    private final long basePosition;
    private final long durationUs;
    private final long[] positions;
    private final long[] timesUs;
    
    private VbriSeeker(final long[] timesUs, final long[] positions, final long basePosition, final long durationUs) {
        this.timesUs = timesUs;
        this.positions = positions;
        this.basePosition = basePosition;
        this.durationUs = durationUs;
    }
    
    public static VbriSeeker create(final MpegAudioHeader mpegAudioHeader, final ParsableByteArray parsableByteArray, long n) {
        parsableByteArray.skipBytes(10);
        final int int1 = parsableByteArray.readInt();
        if (int1 <= 0) {
            return null;
        }
        final int sampleRate = mpegAudioHeader.sampleRate;
        final long n2 = int1;
        int n3;
        if (sampleRate >= 32000) {
            n3 = 1152;
        }
        else {
            n3 = 576;
        }
        final long scaleLargeTimestamp = Util.scaleLargeTimestamp(n2, 1000000L * n3, sampleRate);
        final int unsignedShort = parsableByteArray.readUnsignedShort();
        final int unsignedShort2 = parsableByteArray.readUnsignedShort();
        final int unsignedShort3 = parsableByteArray.readUnsignedShort();
        final long[] array = new long[unsignedShort];
        final long[] array2 = new long[unsignedShort];
        final long n4 = scaleLargeTimestamp / unsignedShort;
        long n5 = 0L;
        for (int i = 0; i < unsignedShort; ++i) {
            int n6 = 0;
            switch (unsignedShort3) {
                default: {
                    return null;
                }
                case 1: {
                    n6 = parsableByteArray.readUnsignedByte();
                    break;
                }
                case 2: {
                    n6 = parsableByteArray.readUnsignedShort();
                    break;
                }
                case 3: {
                    n6 = parsableByteArray.readUnsignedInt24();
                    break;
                }
                case 4: {
                    n6 = parsableByteArray.readUnsignedIntToInt();
                    break;
                }
            }
            n5 += n4;
            array[i] = n5;
            n += n6 * unsignedShort2;
            array2[i] = n;
        }
        return new VbriSeeker(array, array2, mpegAudioHeader.frameSize + n, scaleLargeTimestamp);
    }
    
    @Override
    public long getDurationUs() {
        return this.durationUs;
    }
    
    @Override
    public long getTimeUs(final long n) {
        return this.timesUs[Util.binarySearchFloor(this.positions, n, true, true)];
    }
}
