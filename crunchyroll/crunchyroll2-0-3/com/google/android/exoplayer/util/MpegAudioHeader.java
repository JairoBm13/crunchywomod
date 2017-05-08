// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.util;

public final class MpegAudioHeader
{
    private static final int[] BITRATE_V1_L1;
    private static final int[] BITRATE_V1_L2;
    private static final int[] BITRATE_V1_L3;
    private static final int[] BITRATE_V2;
    private static final int[] BITRATE_V2_L1;
    private static final String[] MIME_TYPE_BY_LAYER;
    private static final int[] SAMPLING_RATE_V1;
    public int bitrate;
    public int channels;
    public int frameSize;
    public String mimeType;
    public int sampleRate;
    public int samplesPerFrame;
    public int version;
    
    static {
        MIME_TYPE_BY_LAYER = new String[] { "audio/mpeg-L1", "audio/mpeg-L2", "audio/mpeg" };
        SAMPLING_RATE_V1 = new int[] { 44100, 48000, 32000 };
        BITRATE_V1_L1 = new int[] { 32, 64, 96, 128, 160, 192, 224, 256, 288, 320, 352, 384, 416, 448 };
        BITRATE_V2_L1 = new int[] { 32, 48, 56, 64, 80, 96, 112, 128, 144, 160, 176, 192, 224, 256 };
        BITRATE_V1_L2 = new int[] { 32, 48, 56, 64, 80, 96, 112, 128, 160, 192, 224, 256, 320, 384 };
        BITRATE_V1_L3 = new int[] { 32, 40, 48, 56, 64, 80, 96, 112, 128, 160, 192, 224, 256, 320 };
        BITRATE_V2 = new int[] { 8, 16, 24, 32, 40, 48, 56, 64, 80, 96, 112, 128, 144, 160 };
    }
    
    public static int getFrameSize(int n) {
        if ((n & 0xFFE00000) == 0xFFE00000) {
            final int n2 = n >>> 19 & 0x3;
            if (n2 != 1) {
                final int n3 = n >>> 17 & 0x3;
                if (n3 != 0) {
                    final int n4 = n >>> 12 & 0xF;
                    if (n4 != 0 && n4 != 15) {
                        final int n5 = n >>> 10 & 0x3;
                        if (n5 != 3) {
                            final int n6 = MpegAudioHeader.SAMPLING_RATE_V1[n5];
                            int n7;
                            if (n2 == 2) {
                                n7 = n6 / 2;
                            }
                            else {
                                n7 = n6;
                                if (n2 == 0) {
                                    n7 = n6 / 4;
                                }
                            }
                            final int n8 = n >>> 9 & 0x1;
                            if (n3 == 3) {
                                if (n2 == 3) {
                                    n = MpegAudioHeader.BITRATE_V1_L1[n4 - 1];
                                }
                                else {
                                    n = MpegAudioHeader.BITRATE_V2_L1[n4 - 1];
                                }
                                return (n * 12000 / n7 + n8) * 4;
                            }
                            if (n2 == 3) {
                                if (n3 == 2) {
                                    n = MpegAudioHeader.BITRATE_V1_L2[n4 - 1];
                                }
                                else {
                                    n = MpegAudioHeader.BITRATE_V1_L3[n4 - 1];
                                }
                            }
                            else {
                                n = MpegAudioHeader.BITRATE_V2[n4 - 1];
                            }
                            if (n2 == 3) {
                                return 144000 * n / n7 + n8;
                            }
                            int n9;
                            if (n3 == 1) {
                                n9 = 72000;
                            }
                            else {
                                n9 = 144000;
                            }
                            return n9 * n / n7 + n8;
                        }
                    }
                }
            }
        }
        return -1;
    }
    
    public static boolean populateHeader(int n, final MpegAudioHeader mpegAudioHeader) {
        if ((0xFFE00000 & n) != 0xFFE00000) {
            return false;
        }
        final int n2 = n >>> 19 & 0x3;
        if (n2 == 1) {
            return false;
        }
        final int n3 = n >>> 17 & 0x3;
        if (n3 == 0) {
            return false;
        }
        final int n4 = n >>> 12 & 0xF;
        if (n4 == 0 || n4 == 15) {
            return false;
        }
        final int n5 = n >>> 10 & 0x3;
        if (n5 == 3) {
            return false;
        }
        final int n6 = MpegAudioHeader.SAMPLING_RATE_V1[n5];
        int n7;
        if (n2 == 2) {
            n7 = n6 / 2;
        }
        else {
            n7 = n6;
            if (n2 == 0) {
                n7 = n6 / 4;
            }
        }
        final int n8 = n >>> 9 & 0x1;
        int n9;
        int n10;
        int n11;
        if (n3 == 3) {
            if (n2 == 3) {
                n9 = MpegAudioHeader.BITRATE_V1_L1[n4 - 1];
            }
            else {
                n9 = MpegAudioHeader.BITRATE_V2_L1[n4 - 1];
            }
            n10 = (n9 * 12000 / n7 + n8) * 4;
            n11 = 384;
        }
        else if (n2 == 3) {
            if (n3 == 2) {
                n9 = MpegAudioHeader.BITRATE_V1_L2[n4 - 1];
            }
            else {
                n9 = MpegAudioHeader.BITRATE_V1_L3[n4 - 1];
            }
            n11 = 1152;
            n10 = 144000 * n9 / n7 + n8;
        }
        else {
            n9 = MpegAudioHeader.BITRATE_V2[n4 - 1];
            if (n3 == 1) {
                n11 = 576;
            }
            else {
                n11 = 1152;
            }
            int n12;
            if (n3 == 1) {
                n12 = 72000;
            }
            else {
                n12 = 144000;
            }
            n10 = n12 * n9 / n7 + n8;
        }
        final String s = MpegAudioHeader.MIME_TYPE_BY_LAYER[3 - n3];
        if ((n >> 6 & 0x3) == 0x3) {
            n = 1;
        }
        else {
            n = 2;
        }
        mpegAudioHeader.setValues(n2, s, n10, n7, n, n9, n11);
        return true;
    }
    
    private void setValues(final int version, final String mimeType, final int frameSize, final int sampleRate, final int channels, final int bitrate, final int samplesPerFrame) {
        this.version = version;
        this.mimeType = mimeType;
        this.frameSize = frameSize;
        this.sampleRate = sampleRate;
        this.channels = channels;
        this.bitrate = bitrate;
        this.samplesPerFrame = samplesPerFrame;
    }
}
