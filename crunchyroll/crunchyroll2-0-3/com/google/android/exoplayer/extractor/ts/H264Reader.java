// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.extractor.ts;

import java.util.Arrays;
import java.util.List;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.util.CodecSpecificDataUtil;
import com.google.android.exoplayer.util.ParsableBitArray;
import java.util.ArrayList;
import com.google.android.exoplayer.util.NalUnitUtil;
import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.util.ParsableByteArray;

final class H264Reader extends ElementaryStreamReader
{
    private boolean foundFirstSample;
    private boolean hasOutputFormat;
    private final IfrParserBuffer ifrParserBuffer;
    private boolean isKeyframe;
    private final NalUnitTargetBuffer pps;
    private final boolean[] prefixFlags;
    private long samplePosition;
    private long sampleTimeUs;
    private final NalUnitTargetBuffer sei;
    private final SeiReader seiReader;
    private final ParsableByteArray seiWrapper;
    private final NalUnitTargetBuffer sps;
    private long totalBytesWritten;
    
    public H264Reader(final TrackOutput trackOutput, final SeiReader seiReader, final boolean b) {
        super(trackOutput);
        this.seiReader = seiReader;
        this.prefixFlags = new boolean[3];
        IfrParserBuffer ifrParserBuffer;
        if (b) {
            ifrParserBuffer = null;
        }
        else {
            ifrParserBuffer = new IfrParserBuffer();
        }
        this.ifrParserBuffer = ifrParserBuffer;
        this.sps = new NalUnitTargetBuffer(7, 128);
        this.pps = new NalUnitTargetBuffer(8, 128);
        this.sei = new NalUnitTargetBuffer(6, 128);
        this.seiWrapper = new ParsableByteArray();
    }
    
    private void feedNalUnitTargetBuffersData(final byte[] array, final int n, final int n2) {
        if (this.ifrParserBuffer != null) {
            this.ifrParserBuffer.appendToNalUnit(array, n, n2);
        }
        if (!this.hasOutputFormat) {
            this.sps.appendToNalUnit(array, n, n2);
            this.pps.appendToNalUnit(array, n, n2);
        }
        this.sei.appendToNalUnit(array, n, n2);
    }
    
    private void feedNalUnitTargetBuffersStart(final int n) {
        if (this.ifrParserBuffer != null) {
            this.ifrParserBuffer.startNalUnit(n);
        }
        if (!this.hasOutputFormat) {
            this.sps.startNalUnit(n);
            this.pps.startNalUnit(n);
        }
        this.sei.startNalUnit(n);
    }
    
    private void feedNalUnitTargetEnd(final long n, int unescapeStream) {
        this.sps.endNalUnit(unescapeStream);
        this.pps.endNalUnit(unescapeStream);
        if (this.sei.endNalUnit(unescapeStream)) {
            unescapeStream = NalUnitUtil.unescapeStream(this.sei.nalData, this.sei.nalLength);
            this.seiWrapper.reset(this.sei.nalData, unescapeStream);
            this.seiWrapper.setPosition(4);
            this.seiReader.consume(this.seiWrapper, n, true);
        }
    }
    
    private void parseMediaFormat(final NalUnitTargetBuffer nalUnitTargetBuffer, final NalUnitTargetBuffer nalUnitTargetBuffer2) {
        final byte[] array = new byte[nalUnitTargetBuffer.nalLength];
        final byte[] array2 = new byte[nalUnitTargetBuffer2.nalLength];
        System.arraycopy(nalUnitTargetBuffer.nalData, 0, array, 0, nalUnitTargetBuffer.nalLength);
        System.arraycopy(nalUnitTargetBuffer2.nalData, 0, array2, 0, nalUnitTargetBuffer2.nalLength);
        final ArrayList<byte[]> list = new ArrayList<byte[]>();
        list.add(array);
        list.add(array2);
        NalUnitUtil.unescapeStream(nalUnitTargetBuffer.nalData, nalUnitTargetBuffer.nalLength);
        final ParsableBitArray parsableBitArray = new ParsableBitArray(nalUnitTargetBuffer.nalData);
        parsableBitArray.skipBits(32);
        final CodecSpecificDataUtil.SpsData spsNalUnit = CodecSpecificDataUtil.parseSpsNalUnit(parsableBitArray);
        this.output.format(MediaFormat.createVideoFormat(-1, "video/avc", -1, -1, -1L, spsNalUnit.width, spsNalUnit.height, list, -1, spsNalUnit.pixelWidthAspectRatio));
        this.hasOutputFormat = true;
    }
    
    @Override
    public void consume(final ParsableByteArray parsableByteArray, final long sampleTimeUs, final boolean b) {
        while (parsableByteArray.bytesLeft() > 0) {
            int i = parsableByteArray.getPosition();
            final int limit = parsableByteArray.limit();
            final byte[] data = parsableByteArray.data;
            this.totalBytesWritten += parsableByteArray.bytesLeft();
            this.output.sampleData(parsableByteArray, parsableByteArray.bytesLeft());
            while (i < limit) {
                final int nalUnit = NalUnitUtil.findNalUnit(data, i, limit, this.prefixFlags);
                if (nalUnit < limit) {
                    final int n = nalUnit - i;
                    if (n > 0) {
                        this.feedNalUnitTargetBuffersData(data, i, nalUnit);
                    }
                    final int nalUnitType = NalUnitUtil.getNalUnitType(data, nalUnit);
                    final int n2 = limit - nalUnit;
                    switch (nalUnitType) {
                        case 5: {
                            this.isKeyframe = true;
                            break;
                        }
                        case 9: {
                            if (this.foundFirstSample) {
                                if (this.ifrParserBuffer != null && this.ifrParserBuffer.isCompleted()) {
                                    final int sliceType = this.ifrParserBuffer.getSliceType();
                                    this.isKeyframe |= (sliceType == 2 || sliceType == 7);
                                    this.ifrParserBuffer.reset();
                                }
                                if (this.isKeyframe && !this.hasOutputFormat && this.sps.isCompleted() && this.pps.isCompleted()) {
                                    this.parseMediaFormat(this.sps, this.pps);
                                }
                                int n3;
                                if (this.isKeyframe) {
                                    n3 = 1;
                                }
                                else {
                                    n3 = 0;
                                }
                                this.output.sampleMetadata(this.sampleTimeUs, n3, (int)(this.totalBytesWritten - this.samplePosition) - n2, n2, null);
                            }
                            this.foundFirstSample = true;
                            this.samplePosition = this.totalBytesWritten - n2;
                            this.sampleTimeUs = sampleTimeUs;
                            this.isKeyframe = false;
                            break;
                        }
                    }
                    int n4;
                    if (n < 0) {
                        n4 = -n;
                    }
                    else {
                        n4 = 0;
                    }
                    this.feedNalUnitTargetEnd(sampleTimeUs, n4);
                    this.feedNalUnitTargetBuffersStart(nalUnitType);
                    i = nalUnit + 3;
                }
                else {
                    this.feedNalUnitTargetBuffersData(data, i, limit);
                    i = limit;
                }
            }
        }
    }
    
    @Override
    public void packetFinished() {
    }
    
    private static final class IfrParserBuffer
    {
        private byte[] ifrData;
        private int ifrLength;
        private boolean isFilling;
        private final ParsableBitArray scratchSliceType;
        private int sliceType;
        
        public IfrParserBuffer() {
            this.ifrData = new byte[128];
            this.scratchSliceType = new ParsableBitArray(this.ifrData);
            this.reset();
        }
        
        public void appendToNalUnit(final byte[] array, int n, int n2) {
            if (this.isFilling) {
                n2 -= n;
                if (this.ifrData.length < this.ifrLength + n2) {
                    this.ifrData = Arrays.copyOf(this.ifrData, (this.ifrLength + n2) * 2);
                }
                System.arraycopy(array, n, this.ifrData, this.ifrLength, n2);
                this.ifrLength += n2;
                this.scratchSliceType.reset(this.ifrData, this.ifrLength);
                this.scratchSliceType.skipBits(8);
                n = this.scratchSliceType.peekExpGolombCodedNumLength();
                if (n != -1 && n <= this.scratchSliceType.bitsLeft()) {
                    this.scratchSliceType.skipBits(n);
                    n = this.scratchSliceType.peekExpGolombCodedNumLength();
                    if (n != -1 && n <= this.scratchSliceType.bitsLeft()) {
                        this.sliceType = this.scratchSliceType.readUnsignedExpGolombCodedInt();
                        this.isFilling = false;
                    }
                }
            }
        }
        
        public int getSliceType() {
            return this.sliceType;
        }
        
        public boolean isCompleted() {
            return this.sliceType != -1;
        }
        
        public void reset() {
            this.isFilling = false;
            this.ifrLength = 0;
            this.sliceType = -1;
        }
        
        public void startNalUnit(final int n) {
            if (n == 1) {
                this.reset();
                this.isFilling = true;
            }
        }
    }
}
