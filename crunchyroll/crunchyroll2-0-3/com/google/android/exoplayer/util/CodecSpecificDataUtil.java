// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.util;

import android.util.Log;
import android.util.Pair;

public final class CodecSpecificDataUtil
{
    private static final int[] AUDIO_SPECIFIC_CONFIG_CHANNEL_COUNT_TABLE;
    private static final int[] AUDIO_SPECIFIC_CONFIG_SAMPLING_RATE_TABLE;
    private static final byte[] NAL_START_CODE;
    
    static {
        NAL_START_CODE = new byte[] { 0, 0, 0, 1 };
        AUDIO_SPECIFIC_CONFIG_SAMPLING_RATE_TABLE = new int[] { 96000, 88200, 64000, 48000, 44100, 32000, 24000, 22050, 16000, 12000, 11025, 8000, 7350 };
        AUDIO_SPECIFIC_CONFIG_CHANNEL_COUNT_TABLE = new int[] { 0, 1, 2, 3, 4, 5, 6, 8 };
    }
    
    public static byte[] buildAacAudioSpecificConfig(final int n, final int n2, final int n3) {
        return new byte[] { (byte)((n << 3 & 0xF8) | (n2 >> 1 & 0x7)), (byte)((n2 << 7 & 0x80) | (n3 << 3 & 0x78)) };
    }
    
    public static Pair<Integer, Integer> parseAacAudioSpecificConfig(final byte[] array) {
        boolean b = true;
        final int n = array[0] >> 3 & 0x1F;
        int n2;
        if (n == 5 || n == 29) {
            n2 = 1;
        }
        else {
            n2 = 0;
        }
        final int n3 = (array[n2] & 0x7) << 1 | (array[n2 + 1] >> 7 & 0x1);
        if (n3 >= 13) {
            b = false;
        }
        Assertions.checkState(b);
        return (Pair<Integer, Integer>)Pair.create((Object)CodecSpecificDataUtil.AUDIO_SPECIFIC_CONFIG_SAMPLING_RATE_TABLE[n3], (Object)(array[n2 + 1] >> 3 & 0xF));
    }
    
    public static SpsData parseSpsNalUnit(final ParsableBitArray parsableBitArray) {
        final int bits = parsableBitArray.readBits(8);
        parsableBitArray.skipBits(16);
        parsableBitArray.readUnsignedExpGolombCodedInt();
        int n = 1;
        if (bits == 100 || bits == 110 || bits == 122 || bits == 244 || bits == 44 || bits == 83 || bits == 86 || bits == 118 || bits == 128 || bits == 138) {
            final int unsignedExpGolombCodedInt = parsableBitArray.readUnsignedExpGolombCodedInt();
            if (unsignedExpGolombCodedInt == 3) {
                parsableBitArray.skipBits(1);
            }
            parsableBitArray.readUnsignedExpGolombCodedInt();
            parsableBitArray.readUnsignedExpGolombCodedInt();
            parsableBitArray.skipBits(1);
            n = unsignedExpGolombCodedInt;
            if (parsableBitArray.readBit()) {
                int n2;
                if (unsignedExpGolombCodedInt != 3) {
                    n2 = 8;
                }
                else {
                    n2 = 12;
                }
                int n3 = 0;
                while (true) {
                    n = unsignedExpGolombCodedInt;
                    if (n3 >= n2) {
                        break;
                    }
                    if (parsableBitArray.readBit()) {
                        int n4;
                        if (n3 < 6) {
                            n4 = 16;
                        }
                        else {
                            n4 = 64;
                        }
                        skipScalingList(parsableBitArray, n4);
                    }
                    ++n3;
                }
            }
        }
        parsableBitArray.readUnsignedExpGolombCodedInt();
        final long n5 = parsableBitArray.readUnsignedExpGolombCodedInt();
        if (n5 == 0L) {
            parsableBitArray.readUnsignedExpGolombCodedInt();
        }
        else if (n5 == 1L) {
            parsableBitArray.skipBits(1);
            parsableBitArray.readSignedExpGolombCodedInt();
            parsableBitArray.readSignedExpGolombCodedInt();
            final long n6 = parsableBitArray.readUnsignedExpGolombCodedInt();
            for (int n7 = 0; n7 < n6; ++n7) {
                parsableBitArray.readUnsignedExpGolombCodedInt();
            }
        }
        parsableBitArray.readUnsignedExpGolombCodedInt();
        parsableBitArray.skipBits(1);
        final int unsignedExpGolombCodedInt2 = parsableBitArray.readUnsignedExpGolombCodedInt();
        final int unsignedExpGolombCodedInt3 = parsableBitArray.readUnsignedExpGolombCodedInt();
        final boolean bit = parsableBitArray.readBit();
        int n8;
        if (bit) {
            n8 = 1;
        }
        else {
            n8 = 0;
        }
        if (!bit) {
            parsableBitArray.skipBits(1);
        }
        parsableBitArray.skipBits(1);
        final int n9 = (unsignedExpGolombCodedInt2 + 1) * 16;
        int n10 = (2 - n8) * (unsignedExpGolombCodedInt3 + 1) * 16;
        int n11 = n9;
        if (parsableBitArray.readBit()) {
            final int unsignedExpGolombCodedInt4 = parsableBitArray.readUnsignedExpGolombCodedInt();
            final int unsignedExpGolombCodedInt5 = parsableBitArray.readUnsignedExpGolombCodedInt();
            final int unsignedExpGolombCodedInt6 = parsableBitArray.readUnsignedExpGolombCodedInt();
            final int unsignedExpGolombCodedInt7 = parsableBitArray.readUnsignedExpGolombCodedInt();
            int n12;
            int n14;
            if (n == 0) {
                n12 = 1;
                int n13;
                if (bit) {
                    n13 = 1;
                }
                else {
                    n13 = 0;
                }
                n14 = 2 - n13;
            }
            else {
                int n15;
                if (n == 3) {
                    n15 = 1;
                }
                else {
                    n15 = 2;
                }
                int n16;
                if (n == 1) {
                    n16 = 2;
                }
                else {
                    n16 = 1;
                }
                int n17;
                if (bit) {
                    n17 = 1;
                }
                else {
                    n17 = 0;
                }
                final int n18 = n16 * (2 - n17);
                n12 = n15;
                n14 = n18;
            }
            final int n19 = n9 - (unsignedExpGolombCodedInt4 + unsignedExpGolombCodedInt5) * n12;
            n10 -= (unsignedExpGolombCodedInt6 + unsignedExpGolombCodedInt7) * n14;
            n11 = n19;
        }
        float n21;
        final float n20 = n21 = 1.0f;
        if (parsableBitArray.readBit()) {
            n21 = n20;
            if (parsableBitArray.readBit()) {
                final int bits2 = parsableBitArray.readBits(8);
                if (bits2 == 255) {
                    final int bits3 = parsableBitArray.readBits(16);
                    final int bits4 = parsableBitArray.readBits(16);
                    n21 = n20;
                    if (bits3 != 0) {
                        n21 = n20;
                        if (bits4 != 0) {
                            n21 = bits3 / bits4;
                        }
                    }
                }
                else if (bits2 < NalUnitUtil.ASPECT_RATIO_IDC_VALUES.length) {
                    n21 = NalUnitUtil.ASPECT_RATIO_IDC_VALUES[bits2];
                }
                else {
                    Log.w("CodecSpecificDataUtil", "Unexpected aspect_ratio_idc value: " + bits2);
                    n21 = n20;
                }
            }
        }
        return new SpsData(n11, n10, n21);
    }
    
    private static void skipScalingList(final ParsableBitArray parsableBitArray, final int n) {
        int n2 = 8;
        int n3 = 8;
        int n4;
        for (int i = 0; i < n; ++i, n3 = n4) {
            if ((n4 = n3) != 0) {
                n4 = (n2 + parsableBitArray.readSignedExpGolombCodedInt() + 256) % 256;
            }
            if (n4 != 0) {
                n2 = n4;
            }
        }
    }
    
    public static final class SpsData
    {
        public final int height;
        public final float pixelWidthAspectRatio;
        public final int width;
        
        public SpsData(final int width, final int height, final float pixelWidthAspectRatio) {
            this.width = width;
            this.height = height;
            this.pixelWidthAspectRatio = pixelWidthAspectRatio;
        }
    }
}
