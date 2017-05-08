// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.extractor.ts;

import com.google.android.exoplayer.MediaFormat;
import java.util.Collections;
import android.util.Log;
import com.google.android.exoplayer.util.ParsableBitArray;
import com.google.android.exoplayer.util.NalUnitUtil;
import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.util.ParsableByteArray;

final class H265Reader extends ElementaryStreamReader
{
    private boolean hasOutputFormat;
    private final NalUnitTargetBuffer pps;
    private final boolean[] prefixFlags;
    private final NalUnitTargetBuffer prefixSei;
    private final SampleReader sampleReader;
    private final SeiReader seiReader;
    private final ParsableByteArray seiWrapper;
    private final NalUnitTargetBuffer sps;
    private final NalUnitTargetBuffer suffixSei;
    private long totalBytesWritten;
    private final NalUnitTargetBuffer vps;
    
    public H265Reader(final TrackOutput trackOutput, final SeiReader seiReader) {
        super(trackOutput);
        this.seiReader = seiReader;
        this.prefixFlags = new boolean[3];
        this.vps = new NalUnitTargetBuffer(32, 128);
        this.sps = new NalUnitTargetBuffer(33, 128);
        this.pps = new NalUnitTargetBuffer(34, 128);
        this.prefixSei = new NalUnitTargetBuffer(39, 128);
        this.suffixSei = new NalUnitTargetBuffer(40, 128);
        this.sampleReader = new SampleReader(trackOutput);
        this.seiWrapper = new ParsableByteArray();
    }
    
    private void nalUnitData(final byte[] array, final int n, final int n2) {
        if (this.hasOutputFormat) {
            this.sampleReader.readNalUnitData(array, n, n2);
        }
        else {
            this.vps.appendToNalUnit(array, n, n2);
            this.sps.appendToNalUnit(array, n, n2);
            this.pps.appendToNalUnit(array, n, n2);
        }
        this.prefixSei.appendToNalUnit(array, n, n2);
        this.suffixSei.appendToNalUnit(array, n, n2);
    }
    
    private void nalUnitEnd(final long n, int n2, final int n3, final long n4) {
        if (this.hasOutputFormat) {
            this.sampleReader.endNalUnit(n, n2, n4);
        }
        else {
            this.vps.endNalUnit(n3);
            this.sps.endNalUnit(n3);
            this.pps.endNalUnit(n3);
            if (this.vps.isCompleted() && this.sps.isCompleted() && this.pps.isCompleted()) {
                this.parseMediaFormat(this.vps, this.sps, this.pps);
            }
        }
        if (this.prefixSei.endNalUnit(n3)) {
            n2 = NalUnitUtil.unescapeStream(this.prefixSei.nalData, this.prefixSei.nalLength);
            this.seiWrapper.reset(this.prefixSei.nalData, n2);
            this.seiWrapper.skipBytes(5);
            this.seiReader.consume(this.seiWrapper, n4, true);
        }
        if (this.suffixSei.endNalUnit(n3)) {
            n2 = NalUnitUtil.unescapeStream(this.suffixSei.nalData, this.suffixSei.nalLength);
            this.seiWrapper.reset(this.suffixSei.nalData, n2);
            this.seiWrapper.skipBytes(5);
            this.seiReader.consume(this.seiWrapper, n4, true);
        }
    }
    
    private void parseMediaFormat(final NalUnitTargetBuffer nalUnitTargetBuffer, final NalUnitTargetBuffer nalUnitTargetBuffer2, final NalUnitTargetBuffer nalUnitTargetBuffer3) {
        final byte[] array = new byte[nalUnitTargetBuffer.nalLength + nalUnitTargetBuffer2.nalLength + nalUnitTargetBuffer3.nalLength];
        System.arraycopy(nalUnitTargetBuffer.nalData, 0, array, 0, nalUnitTargetBuffer.nalLength);
        System.arraycopy(nalUnitTargetBuffer2.nalData, 0, array, nalUnitTargetBuffer.nalLength, nalUnitTargetBuffer2.nalLength);
        System.arraycopy(nalUnitTargetBuffer3.nalData, 0, array, nalUnitTargetBuffer.nalLength + nalUnitTargetBuffer2.nalLength, nalUnitTargetBuffer3.nalLength);
        NalUnitUtil.unescapeStream(nalUnitTargetBuffer2.nalData, nalUnitTargetBuffer2.nalLength);
        final ParsableBitArray parsableBitArray = new ParsableBitArray(nalUnitTargetBuffer2.nalData);
        parsableBitArray.skipBits(44);
        final int bits = parsableBitArray.readBits(3);
        parsableBitArray.skipBits(1);
        parsableBitArray.skipBits(88);
        parsableBitArray.skipBits(8);
        int n = 0;
        for (int i = 0; i < bits; ++i) {
            int n2 = n;
            if (parsableBitArray.readBits(1) == 1) {
                n2 = n + 89;
            }
            n = n2;
            if (parsableBitArray.readBits(1) == 1) {
                n = n2 + 8;
            }
        }
        parsableBitArray.skipBits(n);
        if (bits > 0) {
            parsableBitArray.skipBits((8 - bits) * 2);
        }
        parsableBitArray.readUnsignedExpGolombCodedInt();
        final int unsignedExpGolombCodedInt = parsableBitArray.readUnsignedExpGolombCodedInt();
        if (unsignedExpGolombCodedInt == 3) {
            parsableBitArray.skipBits(1);
        }
        final int unsignedExpGolombCodedInt2 = parsableBitArray.readUnsignedExpGolombCodedInt();
        final int unsignedExpGolombCodedInt3 = parsableBitArray.readUnsignedExpGolombCodedInt();
        int n3 = unsignedExpGolombCodedInt2;
        int n4 = unsignedExpGolombCodedInt3;
        if (parsableBitArray.readBit()) {
            final int unsignedExpGolombCodedInt4 = parsableBitArray.readUnsignedExpGolombCodedInt();
            final int unsignedExpGolombCodedInt5 = parsableBitArray.readUnsignedExpGolombCodedInt();
            final int unsignedExpGolombCodedInt6 = parsableBitArray.readUnsignedExpGolombCodedInt();
            final int unsignedExpGolombCodedInt7 = parsableBitArray.readUnsignedExpGolombCodedInt();
            int n5;
            if (unsignedExpGolombCodedInt == 1 || unsignedExpGolombCodedInt == 2) {
                n5 = 2;
            }
            else {
                n5 = 1;
            }
            int n6;
            if (unsignedExpGolombCodedInt == 1) {
                n6 = 2;
            }
            else {
                n6 = 1;
            }
            n3 = unsignedExpGolombCodedInt2 - (unsignedExpGolombCodedInt4 + unsignedExpGolombCodedInt5) * n5;
            n4 = unsignedExpGolombCodedInt3 - (unsignedExpGolombCodedInt6 + unsignedExpGolombCodedInt7) * n6;
        }
        parsableBitArray.readUnsignedExpGolombCodedInt();
        parsableBitArray.readUnsignedExpGolombCodedInt();
        final int unsignedExpGolombCodedInt8 = parsableBitArray.readUnsignedExpGolombCodedInt();
        int j;
        if (parsableBitArray.readBit()) {
            j = 0;
        }
        else {
            j = bits;
        }
        while (j <= bits) {
            parsableBitArray.readUnsignedExpGolombCodedInt();
            parsableBitArray.readUnsignedExpGolombCodedInt();
            parsableBitArray.readUnsignedExpGolombCodedInt();
            ++j;
        }
        parsableBitArray.readUnsignedExpGolombCodedInt();
        parsableBitArray.readUnsignedExpGolombCodedInt();
        parsableBitArray.readUnsignedExpGolombCodedInt();
        parsableBitArray.readUnsignedExpGolombCodedInt();
        parsableBitArray.readUnsignedExpGolombCodedInt();
        parsableBitArray.readUnsignedExpGolombCodedInt();
        if (parsableBitArray.readBit() && parsableBitArray.readBit()) {
            this.skipScalingList(parsableBitArray);
        }
        parsableBitArray.skipBits(2);
        if (parsableBitArray.readBit()) {
            parsableBitArray.skipBits(8);
            parsableBitArray.readUnsignedExpGolombCodedInt();
            parsableBitArray.readUnsignedExpGolombCodedInt();
            parsableBitArray.skipBits(1);
        }
        skipShortTermRefPicSets(parsableBitArray);
        if (parsableBitArray.readBit()) {
            for (int k = 0; k < parsableBitArray.readUnsignedExpGolombCodedInt(); ++k) {
                parsableBitArray.skipBits(unsignedExpGolombCodedInt8 + 4 + 1);
            }
        }
        parsableBitArray.skipBits(2);
        float n8;
        final float n7 = n8 = 1.0f;
        if (parsableBitArray.readBit()) {
            n8 = n7;
            if (parsableBitArray.readBit()) {
                final int bits2 = parsableBitArray.readBits(8);
                if (bits2 == 255) {
                    final int bits3 = parsableBitArray.readBits(16);
                    final int bits4 = parsableBitArray.readBits(16);
                    n8 = n7;
                    if (bits3 != 0) {
                        n8 = n7;
                        if (bits4 != 0) {
                            n8 = bits3 / bits4;
                        }
                    }
                }
                else if (bits2 < NalUnitUtil.ASPECT_RATIO_IDC_VALUES.length) {
                    n8 = NalUnitUtil.ASPECT_RATIO_IDC_VALUES[bits2];
                }
                else {
                    Log.w("H265Reader", "Unexpected aspect_ratio_idc value: " + bits2);
                    n8 = n7;
                }
            }
        }
        this.output.format(MediaFormat.createVideoFormat(-1, "video/hevc", -1, -1, -1L, n3, n4, Collections.singletonList(array), -1, n8));
        this.hasOutputFormat = true;
    }
    
    private void skipScalingList(final ParsableBitArray parsableBitArray) {
        for (int i = 0; i < 4; ++i) {
            int n;
            for (int j = 0; j < 6; j += n) {
                if (!parsableBitArray.readBit()) {
                    parsableBitArray.readUnsignedExpGolombCodedInt();
                }
                else {
                    final int min = Math.min(64, 1 << (i << 1) + 4);
                    if (i > 1) {
                        parsableBitArray.readSignedExpGolombCodedInt();
                    }
                    for (int k = 0; k < min; ++k) {
                        parsableBitArray.readSignedExpGolombCodedInt();
                    }
                }
                if (i == 3) {
                    n = 3;
                }
                else {
                    n = 1;
                }
            }
        }
    }
    
    private static void skipShortTermRefPicSets(final ParsableBitArray parsableBitArray) {
        final int unsignedExpGolombCodedInt = parsableBitArray.readUnsignedExpGolombCodedInt();
        boolean bit = false;
        int n = 0;
        int n3;
        for (int i = 0; i < unsignedExpGolombCodedInt; ++i, n = n3) {
            if (i != 0) {
                bit = parsableBitArray.readBit();
            }
            if (bit) {
                parsableBitArray.skipBits(1);
                parsableBitArray.readUnsignedExpGolombCodedInt();
                int n2 = 0;
                while (true) {
                    n3 = n;
                    if (n2 > n) {
                        break;
                    }
                    if (parsableBitArray.readBit()) {
                        parsableBitArray.skipBits(1);
                    }
                    ++n2;
                }
            }
            else {
                final int unsignedExpGolombCodedInt2 = parsableBitArray.readUnsignedExpGolombCodedInt();
                final int unsignedExpGolombCodedInt3 = parsableBitArray.readUnsignedExpGolombCodedInt();
                final int n4 = unsignedExpGolombCodedInt2 + unsignedExpGolombCodedInt3;
                for (int j = 0; j < unsignedExpGolombCodedInt2; ++j) {
                    parsableBitArray.readUnsignedExpGolombCodedInt();
                    parsableBitArray.skipBits(1);
                }
                int n5 = 0;
                while (true) {
                    n3 = n4;
                    if (n5 >= unsignedExpGolombCodedInt3) {
                        break;
                    }
                    parsableBitArray.readUnsignedExpGolombCodedInt();
                    parsableBitArray.skipBits(1);
                    ++n5;
                }
            }
        }
    }
    
    private void startNalUnit(final long n, final int n2, final int n3) {
        if (!this.hasOutputFormat) {
            this.vps.startNalUnit(n3);
            this.sps.startNalUnit(n3);
            this.pps.startNalUnit(n3);
        }
        this.prefixSei.startNalUnit(n3);
        this.suffixSei.startNalUnit(n3);
        this.sampleReader.startNalUnit(n, n2, n3);
    }
    
    @Override
    public void consume(final ParsableByteArray parsableByteArray, final long n, final boolean b) {
        while (parsableByteArray.bytesLeft() > 0) {
            int i = parsableByteArray.getPosition();
            final int limit = parsableByteArray.limit();
            final byte[] data = parsableByteArray.data;
            this.totalBytesWritten += parsableByteArray.bytesLeft();
            this.output.sampleData(parsableByteArray, parsableByteArray.bytesLeft());
            while (i < limit) {
                final int nalUnit = NalUnitUtil.findNalUnit(data, i, limit, this.prefixFlags);
                if (nalUnit < limit) {
                    final int n2 = nalUnit - i;
                    if (n2 > 0) {
                        this.nalUnitData(data, i, nalUnit);
                    }
                    final int n3 = limit - nalUnit;
                    final long n4 = this.totalBytesWritten - n3;
                    int n5;
                    if (n2 < 0) {
                        n5 = -n2;
                    }
                    else {
                        n5 = 0;
                    }
                    this.nalUnitEnd(n4, n3, n5, n);
                    this.startNalUnit(n4, n3, NalUnitUtil.getH265NalUnitType(data, nalUnit));
                    i = nalUnit + 3;
                }
                else {
                    this.nalUnitData(data, i, limit);
                    i = limit;
                }
            }
        }
    }
    
    @Override
    public void packetFinished() {
    }
    
    private static final class SampleReader
    {
        private boolean firstSliceFlag;
        private boolean lookingForFirstSliceFlag;
        private int nalUnitBytesRead;
        private boolean nalUnitHasKeyframeData;
        private long nalUnitStartPosition;
        private final TrackOutput output;
        private boolean readingSample;
        private boolean sampleIsKeyframe;
        private long samplePosition;
        private long sampleTimeUs;
        
        public SampleReader(final TrackOutput output) {
            this.output = output;
        }
        
        private void outputSample(final int n) {
            int n2;
            if (this.sampleIsKeyframe) {
                n2 = 1;
            }
            else {
                n2 = 0;
            }
            this.output.sampleMetadata(this.sampleTimeUs, n2, (int)(this.nalUnitStartPosition - this.samplePosition), n, null);
        }
        
        public void endNalUnit(final long n, final int n2, final long sampleTimeUs) {
            if (this.firstSliceFlag) {
                if (this.readingSample) {
                    this.outputSample(n2 + (int)(n - this.nalUnitStartPosition));
                }
                this.samplePosition = this.nalUnitStartPosition;
                this.sampleTimeUs = sampleTimeUs;
                this.readingSample = true;
                this.sampleIsKeyframe = this.nalUnitHasKeyframeData;
            }
        }
        
        public void readNalUnitData(final byte[] array, final int n, final int n2) {
            if (this.lookingForFirstSliceFlag) {
                final int n3 = n + 2 - this.nalUnitBytesRead;
                if (n3 >= n2) {
                    this.nalUnitBytesRead += n2 - n;
                    return;
                }
                this.firstSliceFlag = ((array[n3] & 0x80) != 0x0);
                this.lookingForFirstSliceFlag = false;
            }
        }
        
        public void startNalUnit(final long nalUnitStartPosition, final int n, final int n2) {
            final boolean b = false;
            this.firstSliceFlag = false;
            this.nalUnitBytesRead = 0;
            this.nalUnitStartPosition = nalUnitStartPosition;
            if (n2 >= 32 && this.readingSample) {
                this.outputSample(n);
                this.readingSample = false;
            }
            boolean lookingForFirstSliceFlag = false;
            Label_0086: {
                if (!(this.nalUnitHasKeyframeData = (n2 >= 16 && n2 <= 21))) {
                    lookingForFirstSliceFlag = b;
                    if (n2 > 9) {
                        break Label_0086;
                    }
                }
                lookingForFirstSliceFlag = true;
            }
            this.lookingForFirstSliceFlag = lookingForFirstSliceFlag;
        }
    }
}
