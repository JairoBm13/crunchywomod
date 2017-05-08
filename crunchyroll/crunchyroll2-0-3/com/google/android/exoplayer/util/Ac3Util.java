// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.util;

import java.util.List;
import com.google.android.exoplayer.MediaFormat;

public final class Ac3Util
{
    private static final int[] BITRATES;
    private static final int[] CHANNEL_COUNTS;
    private static final int[] FRMSIZECOD_TO_FRAME_SIZE_44_1;
    private static final int[] SAMPLE_RATES;
    
    static {
        SAMPLE_RATES = new int[] { 48000, 44100, 32000 };
        CHANNEL_COUNTS = new int[] { 2, 1, 2, 3, 3, 4, 4, 5 };
        BITRATES = new int[] { 32, 40, 48, 56, 64, 80, 96, 112, 128, 160, 192, 224, 256, 320, 384, 448, 512, 576, 640 };
        FRMSIZECOD_TO_FRAME_SIZE_44_1 = new int[] { 69, 87, 104, 121, 139, 174, 208, 243, 278, 348, 417, 487, 557, 696, 835, 975, 1114, 1253, 1393 };
    }
    
    public static int getBitrate(final int n, final int n2) {
        return (768000 + n * 8 * n2) / 1536000;
    }
    
    public static MediaFormat parseFrameAc3Format(final ParsableBitArray parsableBitArray, final int n, final long n2, final String s) {
        parsableBitArray.skipBits(32);
        final int bits = parsableBitArray.readBits(2);
        parsableBitArray.skipBits(14);
        final int bits2 = parsableBitArray.readBits(3);
        if ((bits2 & 0x1) != 0x0 && bits2 != 1) {
            parsableBitArray.skipBits(2);
        }
        if ((bits2 & 0x4) != 0x0) {
            parsableBitArray.skipBits(2);
        }
        if (bits2 == 2) {
            parsableBitArray.skipBits(2);
        }
        final boolean bit = parsableBitArray.readBit();
        final int n3 = Ac3Util.CHANNEL_COUNTS[bits2];
        int n4;
        if (bit) {
            n4 = 1;
        }
        else {
            n4 = 0;
        }
        return MediaFormat.createAudioFormat(n, "audio/ac3", -1, -1, n2, n3 + n4, Ac3Util.SAMPLE_RATES[bits], null, s);
    }
    
    public static int parseFrameSize(final ParsableBitArray parsableBitArray) {
        parsableBitArray.skipBits(32);
        final int bits = parsableBitArray.readBits(2);
        final int bits2 = parsableBitArray.readBits(6);
        final int n = Ac3Util.SAMPLE_RATES[bits];
        final int n2 = Ac3Util.BITRATES[bits2 / 2];
        if (n == 32000) {
            return n2 * 6;
        }
        if (n == 44100) {
            return (Ac3Util.FRMSIZECOD_TO_FRAME_SIZE_44_1[bits2 / 2] + bits2 % 2) * 2;
        }
        return n2 * 4;
    }
}
